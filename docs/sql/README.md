# SQL 脚本索引

更新：2026-08-01

在库 `photovoltaic` 上执行（示例主机 slave2）：

```bash
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/<file>.sql
```

| 文件 | 用途 |
|------|------|
| `station_management.sql` | 电价、合同 |
| `power_data_report.sql` | 运行日报 |
| `inspection_ops.sql` | 巡检、工单 |
| `device_management.sql` | 设备、厂商 |
| `data_monitoring.sql` | 阈值 |
| `experience_fault.sql` | 经验库、故障样例 |

进度与接口：`docs/PROGRESS.md`。
