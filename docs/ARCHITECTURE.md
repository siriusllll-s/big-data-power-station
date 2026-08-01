# photovoltaic-information 架构说明

更新：2026-08-01

## 目标

在**不拆微服务**的前提下：

1. 按**业务上下文**分包（而非仅 controller/service 技术分层）
2. 把监控「实时设备读模型」从应用服务中剥离为 **端口 + 适配器**

相关提交：`631fe11`。功能进度见 `docs/PROGRESS.md`。

## 包结构

```
qrsoft.information
├── station/          电站档案、电价、合同、大屏、统计、字典
├── ops/              巡检、工单
├── monitor/          阈值、实时/历史监控
│   ├── port/         RealtimeDataPort（读模型端口）
│   ├── adapter/      DeviceTableRealtimeDataAdapter、CompositeRealtimeDataPort
│   ├── controller/   ThresholdController、MonitorController
│   └── service/      IThresholdService、IRealDataService 及 impl
├── report/           运行日报 + 定时任务
├── device/           设备、厂商
├── identity/         登录、用户、Security、Token 过滤器
├── query/            异常检索、经验库
├── shared/           横切：config、aspect、handler、API 外壳(dto.vo)
├── infrastructure/   MinIO/本地上传
├── dto/              共享请求/响应模型（暂未再拆）
└── mapper/           MyBatis Mapper（@MapperScan 指向此处）
```

源码入口说明：`src/main/java/qrsoft/information/package-info.java`。

## 监控读路径

```
MonitorController
  → IRealDataService (RealDataServiceImpl)
       ├─ pageHistory / pageStationPower   （应用内：合成历史 / 查 k_wh_station）
       └─ getInverter / getCombinerBox / getMeter
              → RealtimeDataPort (@Primary CompositeRealtimeDataPort)
                   → DeviceTableRealtimeDataAdapter
                        · 优先 device 表设备名
                        · 无数据则 BaseConstant 实验设备名
                        · 测点为确定性样例（可替换）
```

### 扩展真实时源

1. 新增类实现 `RealtimeDataPort`（如 `RedisRealtimeDataAdapter`）
2. 修改 `CompositeRealtimeDataPort` 委托目标  
**不必改** Controller、前端、HTTP 路径。

## 扫描与安全

- `@SpringBootApplication(scanBasePackages = {"qrsoft.information", "qrsoft.common"})`
- `@MapperScan({"qrsoft.information.mapper"})`
- HTTP 路径**未改**（仅 Java 包移动），前端无感
- 实验环境 `WebSecurityConfig` 对业务路径 `permitAll`（生产需收紧）

## 与其他文档

| 文档 | 关系 |
|------|------|
| `PROGRESS.md` | 功能完成度与自测 |
| `DEVICE_MGMT.md` | 设备业务接口 |
| `INSPECTION_OPS.md` | 运维接口 |
| `REPORT_CHECKLIST.md` | 日报清单 |
| `README.md` / `使用说明.txt` | 总览与速查 |

## 后续（未做）

- dto 按上下文拆分  
- ArchUnit：禁止 controller 直接依赖 mapper  
- 工单状态机收到 ops 领域类  
- common 与 Spark 依赖进一步解耦  
