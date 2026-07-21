# photovoltaic-framework

光伏大数据项目框架（桌面副本）

## 目录
- `photovoltaic/` 后端多模块 Spring Boot（common / datagenerate / information / spark / sparkrdd）
- `solarweb/` 前端 Vue2 项目
- `demo/` Spring Boot + MyBatis-Plus 演示
- `big-data-notes/` Git 相关说明（若有）

## 路径对应
- 系统安装位后端：`/opt/module/photovoltaic`
- 系统安装位前端：`/home/webspace/solarweb`

## 远程仓库参考
- `git@github.com:siriusllll-s/big-data.git`（main）

## 常用
```bash
# 后端
cd photovoltaic && mvn -DskipTests clean install

# 前端
cd solarweb && npm run serve
```
