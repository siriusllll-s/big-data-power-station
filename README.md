# 光伏大数据项目框架

电站大数据实验项目：模拟采集 → Flume/Kafka → Spark → ES/HBase/MySQL → Spring Boot API → Vue2 大屏/业务前端。

## 仓库结构

```
.
├── photovoltaic/          # 后端多模块（Java 8 / Spring Boot 2.4.4）
│   ├── photovoltaic-common
│   ├── photovoltaic-datagenerate
│   ├── photovoltaic-information   # 业务 Web :8113
│   ├── photovoltaic-spark
│   └── photovoltaic-sparkrdd      # 预测骨架（待实现）
├── solarweb/              # 前端 Vue2 + Element UI + ECharts :8080
├── demo/                  # MyBatis-Plus 演示
├── deploy/cloud/          # 云鉴权 docker-compose / nginx
├── docs/
│   ├── PROGRESS.md        # 功能进度
│   └── sql/               # 建表与样例 SQL
├── big-data-notes/        # 集群 / Git 备忘
├── AGENTS.md              # 实验环境记忆
└── 使用说明.txt
```

> **结构说明（2026-07-31）**：已取消嵌套目录 `big-data-power-station-main/`，代码统一在仓库根目录维护，避免改错副本。

## 数据流

```
datagenerate → logs/*.log → Flume → Kafka
    → SparkStreaming → ES + HBase + MySQL(k_wh_*)
前端 solarweb → information(:8113) → ES/HBase/MySQL
```

## 模块与端口

| 组件 | 端口 / 说明 |
|------|-------------|
| photovoltaic-information | **8113** 业务 API |
| solarweb dev | **8080**，`/api` 代理到 8113 |
| 云鉴权（可选） | 8787，见 `deploy/cloud` |
| MySQL | slave2:3306 / 库 `photovoltaic` |
| Redis | slave1:6379 |
| ES | master:9200 |

## 电站管理（已实现）

| 功能 | 前端 | 后端 |
|------|------|------|
| 电站信息展示/编辑 | `views/Station/Station.vue` | `GET/POST /station/*` |
| 电价增删改查 | `views/Station/Price.vue` | `/stationSolarPrice/*` |
| 合同计划电量 | `views/Station/Contract.vue` | `/stationContract/*` |
| 图片上传 | `UploadImage.vue` | `POST /minio/upload` + `savePhoto` |
| 省市区 | `api/Dictionary/Position.js` | `/dictionary/*` |

建表：

```bash
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/station_management.sql
```

## 报表管理（运行日报）

| 功能 | 说明 |
|------|------|
| 定时生成 | `PowerDataReportTask`：每日汇总 `k_wh_station`/`weather` 写入 `power_data_report` |
| 页面 | `/report/dayReport` 日期筛选、分页、编辑天气/总结、查看详情 |
| 建表 | `docs/sql/power_data_report.sql` |

```bash
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/power_data_report.sql
```

## 大屏接口（/screen）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/screen/ammeter` | 电表读数 ES+HBase |
| GET | `/screen/stationMonthPower` | 本月发电量/效率 |
| GET | `/screen/stationLastThirtyDayPower/{id}` | 近 30 天发电量/效率 |
| GET | `/screen/stationMonthKWhStatistic/{id}` | 过去 12 个月发电量 |
| GET | `/screen/stationFaultCount/{id}` | 故障数统计 |
| GET | `/screen/stationDayAndYearPower/{id}` | 今日/今年与节能 |
| GET | `/screen/weather` | 天气 |

`/screen/**`、`/station/**`、`/stationSolarPrice/**`、`/stationContract/**` 实验环境已放行。

## 构建与启动

```bash
# 后端
export JAVA_HOME=/opt/module/jdk1.8.0_301   # 按本机调整
cd photovoltaic
mvn -DskipTests clean package -pl photovoltaic-common,photovoltaic-information -am
java -jar photovoltaic-information/target/photovoltaic-information-0.0.1-SNAPSHOT.jar
# → :8113

# 前端（Node 建议 14.x）
cd solarweb
npm install
npm run serve
# → :8080
```

### 接口自测

```bash
curl -s http://localhost:8113/station/detail/1
curl -s -X POST http://localhost:8113/stationSolarPrice/pageByParam \
  -H 'Content-Type: application/json' -d '{"page":1,"limit":10}'
curl -s http://localhost:8113/screen/stationMonthKWhStatistic/1
```

## 集群角色（实验）

| 主机 | IP | 角色 |
|------|-----|------|
| master | 172.18.4.59 | HDFS/YARN/HBase/ES/业务 jar |
| slave1 | 172.18.4.144 | Redis 等 |
| slave2 | 172.18.4.70 | MySQL |
| client1–3 | … | 开发端 |

## 文档

- `docs/PROGRESS.md` — 功能进度与自测
- `docs/sql/station_management.sql` — 电价/合同表
- `使用说明.txt` — 路径速查
- `AGENTS.md` — 集群与软件路径

## 注意

1. 勿将生产密钥提交公开仓库
2. 生产请收紧 Security 与数据库密码
3. 大屏 `stationId` 前端写死为 `1`，样例数据请对齐
