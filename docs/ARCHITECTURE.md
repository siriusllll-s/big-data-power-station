# photovoltaic-information 架构说明

## 目标

在**不拆微服务**的前提下，按业务上下文分包，并把监控「假数据/多存储」从应用服务中剥离为端口适配器。

## 包结构

```
qrsoft.information
├── station/          电站档案、电价、合同、大屏、统计、字典
├── ops/              巡检、工单
├── monitor/          阈值、实时/历史监控
│   ├── port/         RealtimeDataPort（读模型端口）
│   └── adapter/      DeviceTableRealtimeDataAdapter、CompositeRealtimeDataPort
├── report/           运行日报 + 定时任务
├── device/           设备、厂商
├── identity/         登录、用户、Security、Token 过滤器
├── query/            异常检索、经验库
├── shared/           横切：config、aspect、handler、API 外壳(dto.vo)
├── infrastructure/   MinIO/本地上传
├── dto/              共享请求/响应模型（暂未再拆）
└── mapper/           MyBatis Mapper（@MapperScan 指向此处）
```

## 监控读路径

```
MonitorController → IRealDataService (RealDataServiceImpl)
                         ├─ pageHistory / pageStationPower  （应用内查询/合成）
                         └─ getInverter/Combiner/Meter
                                → RealtimeDataPort (@Primary Composite)
                                     → DeviceTableRealtimeDataAdapter
                                          （device 表设备名 + 确定性测点；空则 BaseConstant）
```

替换真实时源时：新增 `RedisRealtimeDataAdapter` 实现 `RealtimeDataPort`，改 `CompositeRealtimeDataPort` 委托即可，**不必改 Controller/页面**。

## 扫描与安全

- `@SpringBootApplication(scanBasePackages = {"qrsoft.information", "qrsoft.common"})`
- `@MapperScan({"qrsoft.information.mapper"})`
- HTTP 路径**未改**（仅 Java 包移动），前端无感。

## 后续（未做）

- dto 按上下文拆分
- ArchUnit 禁止 controller → mapper
- 工单状态机收到 ops 领域类
