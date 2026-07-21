# 光伏大数据实验环境 - OpenCode 全局记忆

## 身份与节点
- 本集群共 6 台：master / slave1 / slave2 / client1 / client2 / client3
- 已安装 opencode 的节点：master、client1、client2
- 启动命令：`opencode`（需密码）
- 启动密码：`051856`
- 也可用：`OPENCODE_PASSWORD=051856 opencode`
- 配置文件：`/root/.config/opencode/opencode.json`
- 默认模型：`claude/grok-4.5`；小模型：`claude/grok-3-mini-fast`
- API：`https://siriusgrok.ccwu.cc/v1`（apiKey 见 opencode.json）

## 主机与 IP
| 主机 | IP | 角色 |
|------|-----|------|
| master | 172.18.4.59 | HDFS NN、YARN RM、HBase Master 等 |
| slave1 | 172.18.4.144 | 从节点（Redis 任务机等） |
| slave2 | 172.18.4.70 | 从节点（MySQL 任务主库等） |
| client1 | 172.18.4.236 | 开发主端（IDEA/前端/常用） |
| client2 | 172.18.4.52 | 开发端（opencode） |
| client3 | 172.18.4.80 | 开发端 |

- SSH：root；各机密码为实验环境随机密码
- client 有 VNC GUI：`:1` / 端口 `5901`

## 统一软件路径（各机已同步，opencode 除外）
- JDK：`/opt/module/jdk1.8.0_301`
- ZK：`/opt/module/zookeeper-3.4.13`（`ZK_HOME`）
- Hadoop：`/opt/module/hadoop`
- Kafka：`/opt/module/kafka`
- HBase：`/opt/module/hbase`
- Spark：`/opt/module/spark`
- Flume：`/opt/module/apache-flume-1.9.0-bin`
- Redis：`/opt/module/redis-4.0.8`
- ES7：`/usr/local/es/elasticsearch`
- Maven：`/opt/apache-maven-3.9.1`
- Node14（前端）：`/opt/module/node-v14.16.1-linux-x64`
- 安装包：`/opt/software`
- 后端工程：`/opt/module/photovoltaic`（多模块 Spring Boot）
- 前端工程：`/home/webspace/solarweb`（Vue2 solarweb）
- demo：`/opt/module/demo`
- 环境变量：`/etc/profile`

## 账号与服务
- MySQL：各机 active；`root` / `123456`；库：`photovoltaic`、`testMyBatisPlus`
- Redis：密码 `123456`；`redis-cli -a 123456 ping`
- 大数据进程默认在 master/slave1/slave2 运行；client 装齐软件，不默认当 DN
- Web：HDFS `http://master:50070`；YARN `http://master:8088`；HBase `http://master:60010`；ES `http://master:9200`；Head `http://master:9100`
- 前端 dev：`cd /home/webspace/solarweb && npm run serve` → 8080
- 前端 API 代理：`.env` 中 `VUE_APP_BASE_API=http://localhost:8113`

## Git 仓库 big-data
- 本地：`/root/big-data`
- 远程：`git@github.com:siriusllll-s/big-data.git`
- 分支：`main`
- 初始提交：`first commit`（README: `# big-data`）
- 说明文件：`/root/big-data/GIT_NOTES.md`、`/root/OPENCODE_SHARED_NOTES.md`
- push 需 GitHub SSH 公钥；若 `Permission denied (publickey)` 先添加 `~/.ssh/id_rsa.pub`
```bash
cd /root/big-data
git status
git add .
git commit -m "msg"
git push -u origin main
```

## 后端模块（photovoltaic）
- 根：`photovoltaic`（pom）
- `photovoltaic-common`：通用/实体/工具
- `photovoltaic-datagenerate`：模拟数据
- `photovoltaic-information`：核心业务 Web
- `photovoltaic-spark`：SparkStreaming+Kafka
- `photovoltaic-sparkrdd`：Spark 预测
- 构建：`cd /opt/module/photovoltaic && mvn -DskipTests clean install`

## 前端结构（solarweb）
- `src/api/*`、`src/views/*`、`src/router`、`src/store`、`src/utils`
- 配置：`vue.config.js`、`.env`、`package.json`
- Node 建议 14.16.1

## 工作约定
1. 改集群配置优先在 master 验证，再分发到 slave1/slave2
2. 开发代码优先 client1/client2 的 `/opt/module/photovoltaic` 与 `/home/webspace/solarweb`
3. 勿将密钥/密码提交到公开仓库（含 opencode.json、MySQL/Redis 密码）
4. 需要集群服务状态时先 `jps` / `systemctl status mysqld` / `redis-cli -a 123456 ping`
5. 本文件为全局规则：`~/.config/opencode/AGENTS.md`，每次启动 opencode 会加载

## 桌面项目框架（Git）
- 路径：（桌面快捷：）
- 内容：photovoltaic / solarweb / demo
- Git：已 init，分支 main，远程 git@github.com:siriusllll-s/big-data.git
- 首次提交：first commit: photovoltaic framework

## 桌面项目框架（Git）
- 路径: /root/Desktop/photovoltaic-framework
- 桌面快捷: 光伏项目框架
- 内容: photovoltaic / solarweb / demo
- Git: 已 init，分支 main
- 远程: git@github.com:siriusllll-s/big-data.git
- 首次提交: first commit: photovoltaic framework
