# 报表管理任务完成清单

| 任务要求 | 状态 | 位置 |
|----------|------|------|
| 前端 DayReport.vue | ✅ | `solarweb/src/views/Report/DayReport.vue` |
| 路由 report.js | ✅ | `solarweb/src/router/report.js` → `/report/dayReport` |
| API report.js | ✅ | `solarweb/src/api/Report/report.js` |
| 实体 PowerDataReport | ✅ | `photovoltaic-common/.../entity/PowerDataReport.java` |
| DTO Input/Output/Page | ✅ | `dto/input|output|page/PowerDataReport*` |
| Mapper | ✅ | `PowerDataReportMapper.java` |
| Service 接口/实现 | ✅ | `IPowerDataReportService` / `PowerDataReportServiceImpl` |
| Controller | ✅ | `PowerDataReportController` |
| 定时任务生成日报 | ✅ | `task/PowerDataReportTask`（每日 00:10 / 启动补 30 天） |
| IMinioService | ✅ | `service/IMinioService.java` |
| MinioServiceImpl | ✅ | `service/impl/MinioServiceImpl.java` |
| MinioController | ✅ | `controller/MinioController.java` |
| 建表 SQL | ✅ | `docs/sql/power_data_report.sql` |
| 菜单入口 | ✅ | modules「运行日报」 |

## 部署步骤

```bash
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/power_data_report.sql
cd photovoltaic && mvn -DskipTests clean package -pl photovoltaic-common,photovoltaic-information -am
java -jar photovoltaic-information/target/photovoltaic-information-0.0.1-SNAPSHOT.jar
cd solarweb && npm run serve
```

浏览器：登录 → 侧栏「运行日报」→ 按日期查询 / 编辑天气与总结 / 查看详情。
