# 功能进度

更新：2026-07-31

## 仓库结构（已整理）

工程根目录即为完整项目（**已去掉**嵌套的 `big-data-power-station-main/` 重复副本）：

| 目录 | 说明 |
|------|------|
| `photovoltaic/` | 后端多模块 Spring Boot |
| `solarweb/` | 前端 Vue2 |
| `demo/` | MyBatis-Plus 演示 |
| `deploy/` | 云部署 docker/nginx |
| `docs/` | 进度与 SQL |
| `big-data-notes/` | 集群/Git 备忘 |

## 已完成

### 电站管理（本迭代补齐）

- 电站详情/编辑/图片：`StationController` + `StationServiceImpl` + `Station.vue` / `EditStation` / `UploadImage`
- **电价 CRUD**：实体 `station_solar_price`、`StationSolarPriceServiceImpl`、`/stationSolarPrice/**`、页面 `Price.vue`
- **合同 CRUD**：实体 `station_contract`、`StationContractServiceImpl`、`/stationContract/**`、页面 `Contract.vue`
- 省市区字典：`DictionaryController` + `api/Dictionary/Position.js`
- 本地上传兼容 MinIO 路径：`MinioController`（`local.upload-dir`）
- 安全放行：`/stationSolarPrice/**`、`/stationContract/**`、`/dictionary/**`、`/minio/**`
- 建表脚本：`docs/sql/station_management.sql`

### 大屏图表

| 功能 | 接口 | 前端 | 状态 |
|------|------|------|------|
| 近30天发电效率 / 日发电量 | `GET /screen/stationLastThirtyDayPower/{id}` | `Home/Centre.vue` | ✅ |
| 故障数柱状图 | `GET /screen/stationFaultCount/{id}` | `Home/Right.vue` echart1 | ✅ |
| 12个月发电量 | `GET /screen/stationMonthKWhStatistic/{id}` | `Home/Right.vue` echart2 | ✅ |
| 本月发电量/效率 | `GET /screen/stationMonthPower` | `Home.vue` | ✅ |
| 电表读数 ES+HBase | `POST /screen/ammeter` | `Home.vue` | ✅ |
| 今日/今年发电与节能 | `GET /screen/stationDayAndYearPower/{id}` | `Home/Left.vue` | ✅ |
| 电站地图 | `GET /station/detail/{id}` | `Home.vue` / `Station.vue` | ✅ |
| 发电量按天/月统计 | `POST /statistics/station` | `DataAnalysis/StationKWhStatistics.vue` | ✅ |

### 数据链路

- `datagenerate` → 日志 → Flume/Kafka → Spark → ES/HBase/MySQL
- 业务 API：`photovoltaic-information` 端口 **8113**

## 数据准备

```bash
# 电价/合同表 + 样例
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/station_management.sql

# 大屏需 k_wh_station / fault_count 等有 station=1 的样例行
```

## 自测

```bash
# 电站
curl -s http://localhost:8113/station/detail/1

# 电价分页
curl -s -X POST http://localhost:8113/stationSolarPrice/pageByParam \
  -H 'Content-Type: application/json' -d '{"page":1,"limit":10}'

# 合同分页
curl -s -X POST http://localhost:8113/stationContract/pageByParam \
  -H 'Content-Type: application/json' -d '{"page":1,"limit":10}'

# 12个月发电量
curl -s http://localhost:8113/screen/stationMonthKWhStatistic/1

# 故障统计
curl -s http://localhost:8113/screen/stationFaultCount/1
```

## 已知问题 / 后续

1. HBase Region 异常时电表接口可能无数据（依赖集群状态）
2. 登录可用 mock（`Login.js` `USE_MOCK=true`）或真实 `/login`
3. `photovoltaic-sparkrdd` 预测模块仍无业务源码
4. 生产环境需收紧 Security `permitAll` 与明文密码

## 关键类路径（电站管理）

```
photovoltaic-common/.../entity/StationSolarPrice.java
photovoltaic-common/.../entity/StationContract.java
photovoltaic-information/.../service/impl/StationSolarPriceServiceImpl.java
photovoltaic-information/.../service/impl/StationContractServiceImpl.java
photovoltaic-information/.../controller/StationSolarPriceController.java
photovoltaic-information/.../controller/StationContractController.java
photovoltaic-information/.../controller/DictionaryController.java
photovoltaic-information/.../controller/MinioController.java
solarweb/src/views/Station/{Station,Price,Contract,EditStation,UploadImage}.vue
solarweb/src/api/{Station,Price,Dictionary}/*
docs/sql/station_management.sql
```
