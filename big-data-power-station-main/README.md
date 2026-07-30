# 光伏大数据项目框架

电站大数据实验项目：模拟采集 → Flume/Kafka → Spark → ES/HBase/MySQL → Spring Boot API → Vue2 大屏/业务前端。

## 目录结构

| 目录 | 说明 |
|------|------|
| `photovoltaic/` | 后端多模块 Spring Boot 2.4.4 / Java 8 |
| `solarweb/` | 前端 Vue2 + Element UI + ECharts |
| `demo/` | MyBatis-Plus 演示工程 |
| `deploy/` | 云部署相关（docker/nginx 等） |
| `big-data-notes/` | Git / 集群备忘 |

### 后端模块（photovoltaic）

| 模块 | 作用 |
|------|------|
| `photovoltaic-common` | 实体、常量、DateUtil、MyBatis-Plus 配置 |
| `photovoltaic-datagenerate` | 定时模拟电站设备日志（15s/1h） |
| `photovoltaic-information` | 核心业务 Web（登录、大屏、电表读数等），端口 **8113** |
| `photovoltaic-spark` | SparkStreaming + Kafka 清洗入库 |
| `photovoltaic-sparkrdd` | Spark 预测相关 |

### 前端（solarweb）

- 技术：Vue2、Element-UI、ECharts、axios、vue-router、vuex
- 开发：`npm run serve` → **8080**
- API 代理：`/api` → `http://localhost:8113`（见 `vue.config.js` / `.env`）
- 首页 `views/Home/Home.vue`：电表读数表 + 本月发电量/效率双 Y 轴图

## 路径对照

| 用途 | 路径 |
|------|------|
| 桌面 Git 工程（开发副本） | `/root/Desktop/光伏项目框架` |
| 系统安装后端 | `/opt/module/photovoltaic` |
| 运行 jar（information） | `/opt/module/photovoltaic/information/` |
| 系统安装前端 | `/home/webspace/solarweb` |
| 模拟日志 | `/opt/module/photovoltaic/logs/*.log` |
| Flume 配置 | `/opt/module/photovoltaic/flume-kafka-conf/` |

## 数据流

```
datagenerate → logs/*.log → Flume → Kafka(topic)
    → SparkStreaming → ES 索引 + HBase 表 + MySQL(k_wh_*)
前端 solarweb → information(:8113) → ES/HBase/MySQL
```

### 电表读数链路

1. ES 索引 `ammeter` 按 name/createTime 查文档，取 `rowKey`
2. HBase 表 `AmmeterInfo` 按 rowKey 取列（info 族）
3. 封装 `DataAmmeterOutput` 返回；HBase 异常时回退 ES 字段

### 本月发电量/效率

- 接口：`GET /screen/stationMonthPower`
- 表：`k_wh_ammeter`、`k_wh_inverter`、`station`
- 前端 ECharts 双 Y 轴：发电量(kWh) + 发电效率

## 集群与账号（实验环境）

| 主机 | IP | 角色 |
|------|-----|------|
| master | 172.18.4.59 | HDFS NN、YARN、HBase Master、ES、业务 jar 常用 |
| slave1 | 172.18.4.144 | DN / RS / Redis 等 |
| slave2 | 172.18.4.70 | DN / RS / **MySQL** |
| client1 | 172.18.4.236 | 开发主端 IDEA/前端 |
| client2 | 172.18.4.52 | 开发端 |
| client3 | 172.18.4.80 | 开发端 |

- MySQL：`slave2:3306` / `root` / `123456` / 库 `photovoltaic`
- Redis：`slave1:6379` / 密码 `123456`
- ES：`http://master:9200`（当前 master 可单节点）
- 后端：`http://master:8113`
- 前端 dev：`http://<client>:8080`

## 已实现大屏接口（/screen）

| 方法 | 路径 | 说明 | 状态 |
|------|------|------|------|
| POST | `/screen/ammeter` | 电表读数 ES+HBase | 已实现 |
| GET | `/screen/stationMonthPower` | 本月发电量/效率 | 已实现 |
| GET | `/screen/weather` 等 | 其它大屏接口 | 占位/待续 |

安全：`/login`、`/register`、`/screen/**` 已放行便于联调（生产需收紧）。

## 构建与启动

```bash
# 后端
export JAVA_HOME=/opt/module/jdk1.8.0_301
export PATH=/opt/apache-maven-3.9.1/bin:$JAVA_HOME/bin:$PATH
cd /root/Desktop/光伏项目框架/photovoltaic   # 或 /opt/module/photovoltaic
mvn -DskipTests clean package -pl photovoltaic-common,photovoltaic-information -am
cp photovoltaic-information/target/photovoltaic-information-0.0.1-SNAPSHOT.jar \
   /opt/module/photovoltaic/information/
cd /opt/module/photovoltaic/information
nohup java -jar photovoltaic-information-0.0.1-SNAPSHOT.jar >> run.log 2>&1 &

# 前端
cd /home/webspace/solarweb   # 或桌面 solarweb
# Node 建议 14.16.1
npm run serve
```

### 接口自测

```bash
# 电表读数
curl -s -X POST http://master:8113/screen/ammeter \
  -H 'Content-Type: application/json' -d '{"name":"01"}'

# 本月发电量/效率
curl -s http://master:8113/screen/stationMonthPower
```

## 代码同步（master → client）

开发以 master 验证后，可 rsync 到 client1/2/3：

```bash
# 示例：同步工程与运行 jar、Home 前端
for h in client1 client2 client3; do
  rsync -az /root/Desktop/光伏项目框架/photovoltaic/ root@$h:/root/Desktop/光伏项目框架/photovoltaic/
  rsync -az /root/Desktop/光伏项目框架/solarweb/src/ root@$h:/root/Desktop/光伏项目框架/solarweb/src/
  rsync -az /opt/module/photovoltaic/information/*.jar root@$h:/opt/module/photovoltaic/information/
  rsync -az /home/webspace/solarweb/src/api/Home/ root@$h:/home/webspace/solarweb/src/api/Home/
  rsync -az /home/webspace/solarweb/src/views/Home/ root@$h:/home/webspace/solarweb/src/views/Home/
done
```

**说明（2026-07-27）**：已同步至 client1 / client2 / client3（源码 + jar + Home 前端）。业务 jar 默认仍在 master:8113 运行。

## Git

- 桌面工程：`/root/Desktop/光伏项目框架`（分支 main）
- 参考远程：`git@github.com:siriusllll-s/big-data.git`
- 备忘：`big-data-notes/`、`/root/big-data/GIT_NOTES.md`

```bash
cd /root/Desktop/光伏项目框架
git status
git add .
git commit -m "msg"
# push 需本机 GitHub SSH 公钥已配置
```

## 注意

1. 勿将密码/密钥提交公开仓库（opencode.json、MySQL/Redis 等）
2. HBase Region 异常时电表接口会回退 ES 字段，修好 RS/建表后可走完整 HBase
3. client1 桌面已清理重复公钥文件与 target 构建垃圾；工程主体保留
4. 改集群配置优先 master 验证，再分发 slave

## 文档索引

- 本文件：`README.md`（项目总览）
- `使用说明.txt`：路径速查
- `AGENTS.md`：OpenCode/集群记忆（环境向）
- `docs/PROGRESS.md`：功能进度
