# 光伏大数据项目框架

电站大数据实验项目：模拟采集 → Flume/Kafka → Spark → ES/HBase/MySQL → Spring Boot API → Vue2 大屏/业务前端。

**进度：** `docs/PROGRESS.md`（更新 2026-08-01）  
**架构：** `docs/ARCHITECTURE.md`（information 业务分包 + RealtimeDataPort）  
**速查：** `使用说明.txt`

---

## 后端分包

`photovoltaic-information` 按业务上下文组织（**HTTP 路径不变**）：

| 包 | 说明 |
|----|------|
| `station` | 电站、电价、合同、大屏、统计、字典 |
| `ops` | 巡检、工单 |
| `monitor` | 阈值、实时/历史监控；读模型经 `RealtimeDataPort` |
| `report` | 运行日报与定时任务 |
| `device` | 设备与厂商 |
| `identity` | 登录、用户、Security |
| `query` | 异常检索、经验库 |
| `shared` / `infrastructure` | 横切与 MinIO 上传 |
| `dto` / `mapper` | 共享 DTO 与 MyBatis Mapper |

详情见 **`docs/ARCHITECTURE.md`**。

---

## 仓库结构

```
.
├── photovoltaic/                 # 后端（Java 8 / Spring Boot 2.4.4）
│   ├── photovoltaic-common       # 实体、常量
│   ├── photovoltaic-datagenerate # 模拟日志
│   ├── photovoltaic-information  # 业务 Web :8113（按上下文分包）
│   ├── photovoltaic-spark        # Streaming 入库
│   └── photovoltaic-sparkrdd     # 预测骨架（待实现）
├── solarweb/                     # 前端 Vue2 :8080
├── demo/
├── deploy/cloud/
├── docs/
│   ├── ARCHITECTURE.md
│   ├── PROGRESS.md
│   ├── DEVICE_MGMT.md
│   ├── INSPECTION_OPS.md
│   ├── REPORT_CHECKLIST.md
│   └── sql/                      # 全部建表脚本
├── AGENTS.md
└── 使用说明.txt
```

> 已取消嵌套目录 `big-data-power-station-main/`，代码只在仓库根维护。

---

## 数据流

```
datagenerate → logs/*.log → Flume → Kafka
    → SparkStreaming → ES + HBase + MySQL(k_wh_*)
前端 solarweb → information(:8113) → ES/HBase/MySQL/Redis
```

| 组件 | 端口 / 说明 |
|------|-------------|
| photovoltaic-information | **8113** |
| solarweb dev | **8080**，`/api` → 8113 |
| 云鉴权（可选） | 8787，`deploy/cloud` |
| MySQL | slave2:3306 / `photovoltaic` |
| Redis | slave1:6379 |
| ES | master:9200 |

---

## 功能一览

### 电站管理

| 功能 | 前端 | 后端 |
|------|------|------|
| 电站信息 | `Station.vue` | `/station/*` |
| 电价 | `Price.vue` | `/stationSolarPrice/*` |
| 合同 | `Contract.vue` | `/stationContract/*` |
| 图片 | `UploadImage.vue` | `/minio/*` + `savePhoto` |
| 省市区 | — | `/dictionary/*` |

SQL：`docs/sql/station_management.sql`

### 设备管理

| 功能 | 路由 | SQL / 文档 |
|------|------|------------|
| 设备信息 | `/device` | `docs/sql/device_management.sql` |
| 设备厂商 | `/factory` | `docs/DEVICE_MGMT.md` |

### 运维管理

| 功能 | 路由 |
|------|------|
| 巡检点 / 计划 / 实施 | `/inspection/point\|plan\|implement` |
| 工单 | `/workOrder` |

SQL：`docs/sql/inspection_ops.sql` · 文档：`docs/INSPECTION_OPS.md`

### 报表

| 功能 | 路由 | SQL |
|------|------|-----|
| 运行日报 | `/report/dayReport` | `docs/sql/power_data_report.sql` |

### 数据监控

| 功能 | 路由 | 接口 |
|------|------|------|
| 阈值设置 | `/dataMonitoring/threshold` | `/threshold/*` |
| 设备数据查询 | `/dataMonitoring/historyData` | `POST /monitor/historyData` |
| 电站发电量 | `/dataMonitoring/stationPower` | `POST /monitor/stationPower` |
| 逆变器/汇流箱/电表 | `/dataMonitoring/inverter\|combinerBox\|meter` | `GET /monitor/*` |

SQL：`docs/sql/data_monitoring.sql`

### 大屏 `/screen`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/screen/ammeter` | 电表 ES+HBase |
| GET | `/screen/stationMonthPower` | 本月发电/效率 |
| GET | `/screen/stationLastThirtyDayPower/{id}` | 近 30 天 |
| GET | `/screen/stationMonthKWhStatistic/{id}` | 12 个月发电量 |
| GET | `/screen/stationFaultCount/{id}` | 故障统计 |
| GET | `/screen/stationDayAndYearPower/{id}` | 今日/今年与节能 |
| GET | `/screen/weather` | 天气 |

实验环境大量业务路径已 `permitAll`（见 `WebSecurityConfig`）。

---

## 建表（建议全量）

```bash
DB="mysql -h slave2 -uroot -p123456 photovoltaic"
$DB < docs/sql/station_management.sql
$DB < docs/sql/power_data_report.sql
$DB < docs/sql/inspection_ops.sql
$DB < docs/sql/device_management.sql
$DB < docs/sql/data_monitoring.sql
$DB < docs/sql/experience_fault.sql
```

---

## 构建与启动

```bash
export JAVA_HOME=/opt/module/jdk1.8.0_301   # 按本机调整
cd photovoltaic
mvn -DskipTests clean package -pl photovoltaic-common,photovoltaic-information -am
java -jar photovoltaic-information/target/photovoltaic-information-0.0.1-SNAPSHOT.jar
# → :8113

cd solarweb && npm install && npm run serve
# → :8080
```

### 接口自测示例

```bash
curl -s http://localhost:8113/station/detail/1
curl -s http://localhost:8113/monitor/inverter
curl -s -X POST http://localhost:8113/threshold/pageByParam \
  -H 'Content-Type: application/json' -d '{"page":1,"limit":10}'
curl -s http://localhost:8113/screen/stationMonthKWhStatistic/1
```

更多见 `docs/PROGRESS.md` 自测节与 `使用说明.txt`。

---

## 集群角色（实验）

| 主机 | IP | 角色 |
|------|-----|------|
| master | 172.18.4.59 | HDFS/YARN/HBase/ES/业务 jar |
| slave1 | 172.18.4.144 | Redis 等 |
| slave2 | 172.18.4.70 | MySQL |
| client1–3 | … | 开发端 |

---

## 文档

| 路径 | 说明 |
|------|------|
| `docs/PROGRESS.md` | 功能进度与自测 |
| `docs/ARCHITECTURE.md` | 后端架构分包 |
| `docs/DEVICE_MGMT.md` | 设备管理 |
| `docs/INSPECTION_OPS.md` | 运维 |
| `docs/REPORT_CHECKLIST.md` | 运行日报 |
| `docs/sql/` | 建表脚本 |
| `使用说明.txt` | 速查 |
| `AGENTS.md` | 环境路径 |

---

## 注意

1. 勿将生产密钥提交公开仓库  
2. 生产请收紧 Security 与数据库密码  
3. 前端多处 `stationId = 1`，样例数据请对齐  
4. 监控实时测点默认经端口生成确定性样例，可换 Redis/ES 适配器（见架构文档）  
