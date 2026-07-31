# 运维管理

## 功能
- 巡检点管理 `/inspection/point`
- 巡检计划管理 `/inspection/plan`
- 巡检实施（日历） `/inspection/implement`
- 故障工单 `/workOrder`

## 建表
```bash
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/inspection_ops.sql
```

## 后端接口前缀
- `/inspectionPoint/point/*` 巡检点
- `/inspectionPlan/*` 计划
- `/inspection/*` 项目/事项/进度
- `/workerOrder/*` 工单
- `/device/listByType/{type}` 设备列表

## 实验放行
Security 已 permitAll 上述路径。
