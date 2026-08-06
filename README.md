# 光伏电站大数据监控与分析平台

> 课程实训项目文档（README）
> 技术栈：Vue2 + ElementUI + ECharts / Spring Boot / HDFS / HBase / MySQL / Redis / Kafka / SparkStreaming / ElasticSearch

---

## 第一章 系统概述

本项目构建一个**光伏电站大数据监控与分析平台**，实现从模拟数据采集、实时清洗、分布式存储，到对外提供查询接口与可视化大屏展示的完整链路。

平台模拟 6 节点大数据集群环境（master / slave1 / slave2 / client1 / client2 / client3）：

| 节点 | IP | 角色 |
|------|-----|------|
| master | 172.18.4.59 | HDFS NameNode、YARN ResourceManager、HBase Master、Kafka |
| slave1 | 172.18.4.144 | HDFS DataNode、HBase RegionServer、Kafka、Redis |
| slave2 | 172.18.4.70 | HDFS DataNode、HBase RegionServer、Kafka、MySQL |
| client1~3 | 172.18.4.236/52/80 | 开发调试节点 |

### 系统效果

- 首页大屏：累计/日均发电量、累计/日均/今日收入、CO2 减排量、节煤量，近 30 天发电效率、未来 30 天发电预测、天气信息、电站地图、电表读数、故障统计
- 业务页面：电站信息、电价管理、售电合同、设备与厂商管理、阈值设置、实时/历史监控、巡检计划/巡检点/工单、运行日报、异常检索、经验库

### 技术架构（四层）

```
┌─────────────────────────────────────────────┐
│ 应用层   Vue2 + ElementUI + ECharts (8080)   │
│          Spring Boot API (8113) + Redis 缓存 │
├─────────────────────────────────────────────┤
│ 数据分析层 SparkStreaming(Kafka) / Spark SQL │
├─────────────────────────────────────────────┤
│ 数据存储层 HDFS / HBase / MySQL / ES        │
├─────────────────────────────────────────────┤
│ 数据导入层 模拟采集 → Flume → Kafka → Spark  │
└─────────────────────────────────────────────┘
```

| 功能 | 组件 |
|------|------|
| 消息中间件 | Kafka（数据缓冲） |
| 数据采集与清洗 | SparkStreaming、Flume |
| 存储 | HDFS、HBase、MySQL、Redis |
| 实时计算 | SparkStreaming |
| 资源调度与集群协调 | YARN、ZooKeeper |
| 运行日志监控 | ELK（ElasticSearch） |

---

## 第二章 系统需求分析

### 2.1 任务概述

在"基础代码框架"基础上开发电站发电综合及平均统计等功能：从数据库读取电站发电数据，计算综合（累计发电量、累计收入、CO2 减排、节煤量）与平均（日均发电量、日均收入）指标，通过 Vue + Element UI 展示到页面。

### 2.2 系统用例图（主要用例）

- 用户登录/退出（JWT + Redis 单会话）
- 电站信息管理（分页查询、详情、增删改、照片上传）
- 电价管理 / 售电合同管理
- 设备与厂商管理
- 实时数据监控（逆变器、汇流箱、电表、电站天发电量）
- 阈值设置与异常检索
- 巡检项目管理、巡检计划、巡检点、工单流转
- 电站运行日报生成与查询
- 大屏综合统计展示

### 2.3 功能性需求

1. 登录认证：用户名密码登录，服务端签发 JWT，Redis 保存会话，接口鉴权
2. 电站发电综合及平均统计：累计发电量、日均发电量、累计收入、日均收入、今日收入、总 CO2 减排量、总节煤量、日均 CO2 减排量、日均节煤量
3. 大屏展示：天气、近 30 天发电、未来 30 天预测、月度统计、故障统计、电表读数
4. 业务 CRUD：电站、电价、合同、设备、厂商、巡检、工单、日报等

### 2.4 非功能性需求

- 安全性：接口鉴权、密码加密存储、敏感配置不入库不入仓
- 可用性：集群组件独立运行，单点故障不影响数据传输（高可用设计）
- 易用性：前端统一 ElementUI 组件，分页、表单校验、错误提示

### 2.5 开发技术简介

**开发工具**：IDEA 2020.3（旗舰版，Spring Boot 运行配置一键启动）、VSCode（Java 扩展 + JDK17 语言服务器）、Maven 3.9.1、Node 14/16

**开发环境**：JDK 1.8、Vue 2.6 + ElementUI + ECharts、Spring Boot 2.4.4、MyBatis-Plus 3.4.2、MySQL 5.7、Redis 4.0.8、Hadoop 2.7、HBase 2.1、Kafka、Spark、Elasticsearch 7.9

---

## 第三章 系统总体设计

### 3.1 系统总体功能设计

```
photovoltaic
├── photovoltaic-common        公共模块（实体、常量、工具）
├── photovoltaic-datagenerate  模拟数据生成
├── photovoltaic-information   核心业务（Web API）
│   ├── station     电站档案、电价、合同、大屏、统计、字典
│   ├── monitor     阈值、实时/历史监控
│   ├── ops         巡检、工单
│   ├── report      运行日报 + 定时任务
│   ├── device      设备、厂商
│   ├── identity    登录、用户、JWT 过滤器
│   ├── query       异常检索、经验库
│   └── shared      横切配置、AOP 日志、统一返回
├── photovoltaic-spark        SparkStreaming + Kafka
└── photovoltaic-sparkrdd     Spark 预测
```

### 3.2 系统数据库设计（MySQL：photovoltaic）

核心表：

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| station | 电站信息 | id, name, install_capacity, lon, lat, type, status |
| station_solar_price | 上网电价 | station, price, begin_date, del_flag |
| station_contract | 售电合同 | station, no, begin_date, end_date, del_flag |
| k_wh_station | 电站日发电 | station, power_date, kwh, radiation, power_ratio |
| k_wh_ammeter | 电表日发电 | power_date, kwh, end_kwh |
| k_wh_inverter | 逆变器日发电 | power_date, kwh |
| weather | 天气 | station, record_time, irradiance, ambient_temperature, battery_panel_temperature, wind_speed, wind_direction |
| device / device_factory | 设备 / 厂商 | type, factory, station, del_flag |
| threshold | 阈值设置 | classification, type, level, cycle, del_flag |
| inspection_plan / inspection_point / inspection_manage | 巡检 | plan_id, point_id, project_id, status |
| work_order / work_order_device / work_order_user / work_order_history | 工单 | title, description, status, order_id |
| power_day_report | 运行日报 | station, report_date, ... |
| fault / fault_count | 故障 / 故障统计 | device_name, fault_count |
| experience | 经验库 | title, content, device_type, del_flag |

> 实训期间补齐了 16 张表与代码实体不一致的字段（del_flag、station、power_ratio、project_id、description、order_id 等），修复脚本见 `docs/sql/fix_schema_runtime.sql`。

---

## 第四章 系统主要模块详细设计及实现

### 4.1 登录认证模块（identity）

- `TokenLoginFilter`：登录校验，成功后签发 JWT（有效期 120 分钟），令牌与权限集合写入 Redis，响应头返回 `Authorization`
- `TokenAuthenticationFilter`：请求鉴权，校验 JWT 签名 + Redis 令牌存在性，失败返回 401
- 单会话机制：重复登录会使旧令牌失效

### 4.2 电站发电综合及平均统计（大屏核心功能）

接口：`GET /screen/stationAllAndAverage`

实现（`StationScreenServiceImpl.stationAllAndAverage()`）：

1. 统计 `k_wh_ammeter` 表去重天数（dayNum）
2. 累计发电量 `allKWh = Σ kwh`
3. 日均发电量 `averageKWh = allKWh / dayNum`
4. 累计收入 `allInCome = allKWh × MONEY_FORMAT ÷ 10000`
5. 今日收入（今日电表数据 × 系数）
6. CO2 减排量、节煤量（REDUCE_CO2_FORMAT=997、REDUCE_COAL_FORMAT=400）

实测返回：累计发电量 **448821.33 kW·h**、日均 **4826.04 kW·h**。

大屏其他接口：`/screen/weather`、`/screen/stationLastThirtyDayPower/{id}`、`/screen/stationDayAndYearPower/{id}`、`/screen/stationMonthKWhStatistic/{id}`、`/screen/stationMonthPower`、`/screen/stationTypePower`、`/screen/stationNextThirtyDayPower/{id}`、`/screen/stationFaultCount/{id}`、`/screen/ammeter`

### 4.3 前端展示（Home 首页）

- `src/api/Home/Home.js`：封装全部大屏接口
- `src/views/Home/Home.vue`：9 项综合/平均指标卡（累计发电量、日均发电量、累计收入、日均收入、今日收入、总 CO2 减排量、总节煤量、日均 CO2 减排量、日均节煤量）+ 电表读数表格 + 电站地图
- 修复记录：关闭登录 mock（`USE_MOCK=false`）、登录代理指向本地后端（`VUE_APP_AUTH_API=localhost:8113`）、侧边栏层级菜单渲染、补齐缺失路由（`*` 不再回登录页）

---

## 第五章 系统测试

### 5.1 测试目的

验证系统功能完整性、接口正确性与端到端可用性。

### 5.2 测试用例及结果

**冒烟测试（19 个控制器 43 项接口）**：`docs/test/smoke_all.sh`

| 模块 | 覆盖接口 | 结果 |
|------|----------|------|
| 登录/用户/字典 | /login、/user/list、/dictionary/* | PASS |
| 电站管理 | /station、/stationSolarPrice、/stationContract、/statistics | PASS |
| 大屏 | /screen/* 全部 10 项 | PASS |
| 设备管理 | /device、/factory | PASS |
| 实时监控 | /monitor/*、/threshold | PASS |
| 巡检运维 | /inspection、/inspectionPlan、/inspectionPoint、/workerOrder | PASS |
| 报表查询 | /powerDataReport、/data/exceptionPage、/experience | PASS |

**结果：43/43 全部通过（successful:true）**

**业务用例测试（用例 13-23，14 项）**：`docs/test/test-photovoltaic-station.sh` + `docs/test/详细用例13-23.md` + `docs/test/测试报告-用例13-23.md`

覆盖：电价新增/分页/详情/删除、合同新增/分页/详情/删除、逻辑删除（del_flag）、重复名称校验等，**14/14 全部通过**。

### 5.3 测试发现并修复的问题

1. Redis 未启动导致登录失败 → 重启并纳入一键启动脚本
2. 16 张表缺列（del_flag/station/power_ratio/project_id/description/order_id/device_name...）→ slave2 库补齐
3. 前端 mock 登录（假 token 导致 401 循环）→ 关闭 mock、修正登录代理与 token 持久化
4. 侧边栏点击无响应 → 层级菜单渲染；点击落回登录页 → 补齐 11 个缺失路由
5. 桌面框架副本缺 20+ 实体类（编译失败满屏红）→ rsync 补齐
6. client1 inotify 文件监听上限（ENOSPC）→ 调至 524288
7. VSCode Java 语言服务器需 JDK11+ → 安装 JDK17 并配置
8. ZK 集群配置不完整（master 单机版、myid 缺失）→ 统一集群配置
9. Kafka 三台 broker.id 重复（均为 1）→ 改为 1/2/3 并清理 meta.properties

---

## 总结

本项目完成了光伏电站大数据监控平台从集群环境、数据存储、业务接口到前端可视化的全链路搭建与验证：

- **环境**：ZK/HDFS/YARN/HBase/Kafka/ES/Redis/MySQL 全部组件可一键启动（桌面脚本 `start_all.sh`，9 项冷启动验证通过）
- **后端**：Spring Boot 多模块工程，登录认证、电站/电价/合同/设备/监控/巡检/工单/日报全模块接口可用
- **前端**：Vue2 + ElementUI 大屏与业务页面完整可运行
- **测试**：43 项冒烟 + 14 项业务用例全部通过
- **文档**：架构说明（docs/ARCHITECTURE.md）、进度（docs/PROGRESS.md）、测试脚本与报告（docs/test/）

### 存在的问题

1. ES 依赖的实时电表数据需 HBase+ES 双写链路正常运行才完整
2. 后端为单会话模式，多端同时登录会互踢
3. 生产环境需补充 CI/CD 与容器化部署

---

## 快速启动

```bash
# 1. 一键启动全部环境（ZK→HDFS→YARN→HBase→Kafka→ES→Redis→前端）
bash /root/Desktop/光伏项目框架/start_all.sh

# 2. 后端在 IDEA 中运行（photovoltaic 工程，运行配置"photovoltaic后端启动(8113)"）

# 3. 访问
http://client1:8080   账号 admin / 123456
```
