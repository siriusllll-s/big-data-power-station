# 设备管理

更新：2026-08-01

## 页面

| 路由 | 组件 |
|------|------|
| `/device` | `solarweb/src/views/Device/DeviceList.vue` |
| `/factory` | `solarweb/src/views/Device/VendorList.vue` |

API：`solarweb/src/api/Device/Device.js`、`api/DeviceF/DeviceF.js`  
路由：`solarweb/src/router/device.js`

## 后端位置（分包后）

| 类型 | 路径 |
|------|------|
| Controller | `qrsoft.information.device.controller.DeviceController` |
| | `qrsoft.information.device.controller.DeviceFactoryController` |
| Service | `qrsoft.information.device.service.*` |
| 实体 | `qrsoft.common.entity.Device` / `DeviceFactory` |

架构总览：`docs/ARCHITECTURE.md`。

## 建表

```bash
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/device_management.sql
```

## 接口

### 设备 `/device`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/device/pageByParam` | 分页（编号/名称/类型/安装到期日） |
| POST | `/device/save` | 新增/修改 |
| GET | `/device/detail/{id}` | 详情 |
| GET | `/device/delete/{id}` | 逻辑删除 |
| GET | `/device/deviceByType/{type}` | 按类型列表 |
| GET | `/device/deviceList` | 全部设备 |
| GET | `/device/listByType/{type}` | 兼容工单下拉（库优先，否则常量名） |

设备类型：`0` 逆变器 · `1` 汇流箱 · `2` 直流柜 · `3` 气象站 · `4` 电表。

### 厂商 `/factory`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/factory/pageByParam` | 分页 |
| POST | `/factory/save` | 新增/修改 |
| GET | `/factory/detail/{id}` | 详情 |
| GET | `/factory/delete/{id}` | 逻辑删除 |
| GET | `/factory/factoryList` | 下拉列表 |

## 关联

- 工单/巡检选设备：优先 `listByType` / `deviceByType`
- 监控实时适配器：从 `device` 表取逆变器/电表名称（见 `DeviceTableRealtimeDataAdapter`）
