# 功能进度

更新：2026-07-27（master）

## 已完成

### 日发电量统计（Home/Centre）

- 共用接口 `GET /screen/stationLastThirtyDayPower/{id}`
- 前端 `Centre.vue`：日发电量折线 + 近30天效率折线
- 工具 `DateUtils.lastThityDayBetween`；DTO 含 powerDate/kwh/powerRatio


### 0.2 电站基本信息（Home/Left）

- 前端：`views/Home/Left.vue` 展示今日/今年发电、节煤、CO2、电站档案
- API：`getStationDayAndYearPower` + 已有 `getStationDetail`
- 后端：实现 `stationDayAndYearPower` 读 `k_wh_station` + ScreenConstant 系数
- 已嵌入 `Home.vue` 左侧栏；同步 client1/2/3

### 0. 电站地图信息展示（高德地图）

- 后端：`StationController` `GET /station/detail/{id}` → `StationServiceImpl.detail`
- 返回 lon/lat/address 及省市区、负责人
- 前端：`api/Station/Station.js` `getStationDetail`；`Home.vue` `initMap` 用 AMap 打点
- `public/index.html` 引入高德 JS API（**请替换为自己的 Web Key**）
- 样例坐标：青岛即墨 `120.412455, 36.113991`

### 0.1 电站发电量统计

- 后端：`POST /statistics/station`（`StatisticsController` / `StatisticsServiceImpl.stationStatistics`）
- 表：`k_wh_station`（kwh / radiation / power_ratio / power_date）
- type=0 按天；type=1 按月汇总
- 前端：`views/DataAnalysis/StationKWhStatistics.vue` + `api/DataAnalysis/dataAnalysis.js`
- 路由：`/dataAnalysis/stationKWhStatistics`（表格 + 三轴 ECharts）
- 已灌近 30 天样例数据；已同步 client1/2/3

### 1. 电站电表读数（ES + HBase）

- 后端：`StationScreenController.ammeter` → `StationScreenServiceImpl.ammeter`
- 流程：ES 索引 `ammeter` 取 rowKey → HBase `AmmeterInfo` → `DataAmmeterOutput`
- 工具：`HBaseUtil`、`ESConfig`；HBase 失败时回退 ES 字段
- 前端：`api/Home/Home.js` `getAmmeterData`；`Home.vue` 表格展示
- 接口：`POST /screen/ammeter` body `{"name":"01"}`

### 2. 本月发电量 / 发电效率（ECharts 双 Y 轴）

- 后端：`stationMonthPower()` 查 `k_wh_ammeter` / `k_wh_inverter` / `station`
- 输出：`allKWh`、`powerRatio`、`stationInfo`
- 前端：`getStationMonthPower` + `initECharts` / `echart1` 双 Y 轴面积折线
- 接口：`GET /screen/stationMonthPower`

### 3. 环境与工程

- 多模块工程桌面副本 + `/opt` 运行位
- master 构建并启动 information:8113
- 同步至 client1 / client2 / client3
- client1 桌面清理（重复公钥、target、.DS_Store 等）

## 占位 / 待实现（大屏其它接口）

以下 Controller 路由已有，Service 多为空实现或简化：

- `GET /screen/weather`
- `GET /screen/stationLastThirtyDayPower/{id}`
- `GET /screen/stationDayAndYearPower/{id}`
- `GET /screen/stationMonthKWhStatistic/{id}`
- `GET /screen/stationAllAndAverage`
- `GET /screen/stationTypePower`
- `GET /screen/stationNextThirtyDayPower/{id}`
- `GET /screen/stationFaultCount/{id}`

## 已知问题

1. HBase 部分 Region 在 slave1 上报 `NotServingRegion`，Master 偶发 initializing → 电表走 ES 回退
2. ES 集群若三节点 quorum 失败，master 可改 `discovery.type: single-node`（实验用，清 data 慎用）
3. 登录可用 mock（`Login.js` USE_MOCK）或真实 `/login`；大屏 `/screen/**` 当前 permitAll

## 关键类路径

```
photovoltaic-information/.../controller/StationScreenController.java
photovoltaic-information/.../service/IStationScreenService.java
photovoltaic-information/.../service/impl/StationScreenServiceImpl.java
photovoltaic-information/.../utils/HBaseUtil.java
photovoltaic-information/.../config/ESConfig.java
solarweb/src/api/Home/Home.js
solarweb/src/views/Home/Home.vue
```
