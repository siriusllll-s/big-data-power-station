# 运维管理（巡检 + 工单）

更新：2026-08-01

## 页面

| 路由 | 组件 |
|------|------|
| `/inspection/point` | `InspectionPoint.vue` |
| `/inspection/plan` | `InspectionPlan.vue` |
| `/inspection/implement` | `Implement.vue`（日历进度） |
| `/workOrder` | `workOrder.vue` |

API：`solarweb/src/api/Inspection/*`  
路由：`solarweb/src/router/inspection.js`

## 后端位置（分包后）

| 类型 | 包 |
|------|-----|
| Controller / Service | `qrsoft.information.ops.*` |
| 实体 | `Inspection*`、`WorkOrder*`（`photovoltaic-common`） |

架构总览：`docs/ARCHITECTURE.md`。

## 建表

```bash
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/inspection_ops.sql
```

含：巡检项目/内容/事项/点、计划与进度、工单及关联表，并带样例数据。

## 接口前缀

| 前缀 | 说明 |
|------|------|
| `/inspectionPoint/point/*` | 巡检点分页/保存/详情/删除 |
| `/inspectionPoint/pointList` | 巡检点列表 |
| `/inspectionPlan/*` | 计划 CRUD；保存时生成 `inspection_manage` 进度 |
| `/inspection/projectList` | 巡检项目 |
| `/inspection/itemList/{projectId}` | 项目下事项 |
| `/inspection/manageList` | 日历进度 `Map<日期, List>` |
| `/workerOrder/*` | 工单分页/保存/详情/删除/处理 |

设备下拉：`GET /device/listByType/{type}`（见设备文档）。

## 安全

实验环境 `/inspection/**`、`/inspectionPlan/**`、`/inspectionPoint/**`、`/workerOrder/**` 已放行。
