# 报表管理（运行日报）清单

更新：2026-08-01

## 完成状态

| 任务要求 | 状态 | 位置 |
|----------|------|------|
| 前端 DayReport.vue | ✅ | `solarweb/src/views/Report/DayReport.vue` |
| 路由 report.js | ✅ | `/report/dayReport` |
| API report.js | ✅ | `solarweb/src/api/Report/report.js` |
| 实体 PowerDataReport | ✅ | `photovoltaic-common` |
| DTO / Mapper | ✅ | `dto/*`、`mapper/PowerDataReportMapper` |
| Service | ✅ | `qrsoft.information.report.service.*` |
| Controller | ✅ | `qrsoft.information.report.controller.PowerDataReportController` |
| 定时任务 | ✅ | `qrsoft.information.report.task.PowerDataReportTask` |
| MinIO | ✅ | `infrastructure.minio`（电站图片等） |
| 建表 SQL | ✅ | `docs/sql/power_data_report.sql` |
| 菜单 | ✅ | modules「运行日报」 |

架构分包见 `docs/ARCHITECTURE.md`。

## 行为摘要

- 每天 **00:10** 生成昨日日报；每小时可刷新今日；**启动时补近 30 天**
- 数据源：`k_wh_station` + `weather`（无天气则按辐照推断）
- 接口：`POST /powerDataReport/pageByParam`、`GET /detail/{id}`、`POST /update`、`POST /generate`

## 部署

```bash
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/power_data_report.sql
cd photovoltaic && mvn -DskipTests clean package -pl photovoltaic-common,photovoltaic-information -am
java -jar photovoltaic-information/target/photovoltaic-information-0.0.1-SNAPSHOT.jar
cd solarweb && npm run serve
```

登录 → 侧栏 **运行日报** → 日期查询 / 编辑天气与总结 / 查看详情。
