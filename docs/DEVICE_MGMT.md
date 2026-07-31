# 设备管理

## 页面
- `/device` 设备信息 DeviceList.vue
- `/factory` 设备厂商 VendorList.vue

## 建表
```bash
mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/device_management.sql
```

## 接口
- `/device/pageByParam|save|detail/{id}|delete/{id}|deviceByType/{type}|deviceList|listByType/{type}`
- `/factory/pageByParam|save|detail/{id}|delete/{id}|factoryList`
