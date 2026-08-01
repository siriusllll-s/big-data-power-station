# 功能进度

更新：2026-08-01

## 仓库结构

工程根目录即为完整项目（无嵌套 `big-data-power-station-main/`）：

| 目录 | 说明 |
|------|------|
| `photovoltaic/` | 后端多模块 Spring Boot 2.4 / Java 8 |
| `solarweb/` | 前端 Vue2 + Element + ECharts |
| `demo/` | MyBatis-Plus 演示 |
| `deploy/cloud/` | 云鉴权 docker-compose |
| `docs/` | 进度、架构、专题说明、SQL |
| `big-data-notes/` | 集群 / Git 备忘 |
| `AGENTS.md` | 实验环境路径记忆 |
| `使用说明.txt` | 命令与接口速查 |

### 后端 information 分包（2026-08-01）

详见 **`docs/ARCHITECTURE.md`**。Controller/Service 已按业务上下文拆分：

| 包 | 业务 |
|----|------|
| `station` | 电站、电价、合同、大屏、统计、字典 |
| `ops` | 巡检、工单 |
| `monitor` | 阈值、实时/历史监控（含 `RealtimeDataPort`） |
| `report` | 运行日报 + 定时任务 |
| `device` | 设备、厂商 |
| `identity` | 登录、用户、Security |
| `query` | 异常检索、经验库 |
| `shared` | 横切配置 / 结果外壳 |
| `infrastructure` | MinIO 本地上传 |
| `dto` / `mapper` | 共享模型与持久化（暂未再拆） |

**HTTP 路径未因分包变更。** 最新相关提交：`631fe11`。

---

## 已完成功能

### 架构重构

- [x] information 业务上下文分包
- [x] 监控实时数据 `RealtimeDataPort` + `DeviceTableRealtimeDataAdapter`
- [x] `docs/ARCHITECTURE.md`、`package-info.java`

### 数据监控

- [x] 阈值 CRUD：`/threshold/*`，表 `threshold`，SQL `docs/sql/data_monitoring.sql`
- [x] 监控：`POST /monitor/historyData|stationPower`，`GET /monitor/inverter|combinerBox|meter`
- [x] 前端：`DataMonitoring/{Threshold,AddThreshold,HistoryData,StationPower,Inverter,CombinerBox,Meter}.vue`
- [x] 路由：`/dataMonitoring/*`；侧栏多入口

### 设备管理

- [x] 设备 `/device/*`、厂商 `/factory/*`
- [x] 页面：`DeviceList.vue`、`VendorList.vue`
- [x] SQL：`docs/sql/device_management.sql`
- [x] 专题：`docs/DEVICE_MGMT.md`

### 运维管理

- [x] 巡检点 / 计划 / 实施日历 / 故障工单
- [x] SQL：`docs/sql/inspection_ops.sql`
- [x] 专题：`docs/INSPECTION_OPS.md`

### 报表（运行日报）

- [x] 表 `power_data_report`，定时任务生成
- [x] 页面 `/report/dayReport`
- [x] SQL：`docs/sql/power_data_report.sql`
- [x] 清单：`docs/REPORT_CHECKLIST.md`

### 电站管理

- [x] 电站详情/编辑/图片
- [x] 电价、合同 CRUD
- [x] 省市区字典、MinIO 兼容上传
- [x] SQL：`docs/sql/station_management.sql`

### 大屏

| 功能 | 接口 | 前端 | 状态 |
|------|------|------|------|
| 近 30 天发电/效率 | `GET /screen/stationLastThirtyDayPower/{id}` | `Home/Centre.vue` | ✅ |
| 故障柱状图 | `GET /screen/stationFaultCount/{id}` | `Home/Right.vue` | ✅ |
| 12 个月发电量 | `GET /screen/stationMonthKWhStatistic/{id}` | `Home/Right.vue` | ✅ |
| 本月发电/效率 | `GET /screen/stationMonthPower` | `Home.vue` | ✅ |
| 电表 ES+HBase | `POST /screen/ammeter` | `Home.vue` | ✅ |
| 今日/今年与节能 | `GET /screen/stationDayAndYearPower/{id}` | `Home/Left.vue` | ✅ |
| 电站地图 | `GET /station/detail/{id}` | Home / Station | ✅ |
| 发电量按天/月 | `POST /statistics/station` | `StationKWhStatistics.vue` | ✅ |

### 其他业务页

- [x] 基础数据（省字典）、异常检索、经验库、权限用户列表、协作台
- [x] SQL：`docs/sql/experience_fault.sql`（`fault` / `experience`）
- [x] 路由/脚手架语法修复

### 数据链路（实训骨架）

- `datagenerate` → 日志 → Flume/Kafka → `spark` → ES/HBase/MySQL
- 业务 API：`photovoltaic-information` **:8113**

---

## 数据准备（建议按序执行）

```bash
# 库 photovoltaic 上执行
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/station_management.sql
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/power_data_report.sql
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/inspection_ops.sql
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/device_management.sql
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/data_monitoring.sql
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/experience_fault.sql

# 大屏另需 k_wh_station / fault_count 等 station=1 样例（可由 Spark 链路或手工灌入）
```

---

## 自测 curl

```bash
# 电站 / 电价
curl -s http://localhost:8113/station/detail/1
curl -s -X POST http://localhost:8113/stationSolarPrice/pageByParam \
  -H 'Content-Type: application/json' -d '{"page":1,"limit":10}'

# 大屏
curl -s http://localhost:8113/screen/stationMonthKWhStatistic/1
curl -s http://localhost:8113/screen/stationFaultCount/1

# 阈值 / 监控
curl -s -X POST http://localhost:8113/threshold/pageByParam \
  -H 'Content-Type: application/json' -d '{"page":1,"limit":10}'
curl -s http://localhost:8113/monitor/inverter
curl -s http://localhost:8113/monitor/meter

# 设备 / 厂商
curl -s -X POST http://localhost:8113/device/pageByParam \
  -H 'Content-Type: application/json' -d '{"page":1,"limit":10}'
curl -s http://localhost:8113/factory/factoryList

# 日报
curl -s -X POST http://localhost:8113/powerDataReport/pageByParam \
  -H 'Content-Type: application/json' -d '{"page":1,"limit":10,"start":"2026-07-01","end":"2026-08-01"}'
```

---

## 文档索引

| 文件 | 内容 |
|------|------|
| `docs/ARCHITECTURE.md` | information 分包与 RealtimeDataPort |
| `docs/PROGRESS.md` | 本文件：功能进度 |
| `docs/DEVICE_MGMT.md` | 设备/厂商 |
| `docs/INSPECTION_OPS.md` | 运维巡检工单 |
| `docs/REPORT_CHECKLIST.md` | 运行日报清单 |
| `docs/sql/*.sql` | 建表与样例 |
| `README.md` | 总览与启动 |
| `使用说明.txt` | 速查 |
| `AGENTS.md` | 集群路径 |

---

## 已知问题 / 后续

1. HBase/ES 异常时大屏电表等可能无数据；监控实时测点目前为**确定性样例**（端口可换真实源）
2. 登录可用 mock（`Login.js`）或真实 `/login`；侧栏菜单依赖 `modules.js` / localStorage
3. `photovoltaic-sparkrdd` 预测仍无业务源码
4. 生产需收紧 `permitAll`、明文密码与硬编码主机
5. 架构后续：dto 按上下文拆分、ArchUnit、工单状态机领域化

## 关键代码位置（重构后）

```
# 电站
.../station/controller/StationController.java
.../station/service/impl/StationServiceImpl.java
.../station/service/impl/StationSolarPriceServiceImpl.java
.../station/service/impl/StationContractServiceImpl.java

# 监控
.../monitor/controller/ThresholdController.java
.../monitor/controller/MonitorController.java
.../monitor/port/RealtimeDataPort.java
.../monitor/adapter/DeviceTableRealtimeDataAdapter.java

# 运维 / 设备 / 报表
.../ops/controller/WorkOrderController.java
.../device/controller/DeviceController.java
.../report/task/PowerDataReportTask.java

# 基础设施
.../infrastructure/minio/MinioController.java
.../identity/security/WebSecurityConfig.java
```
