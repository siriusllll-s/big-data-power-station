
# big-data Git 仓库要点（三台 opencode 共用）

## 仓库
- 本地目录（master）: /root/big-data
- 远程: git@github.com:siriusllll-s/big-data.git
- 默认分支: main
- 初始提交: first commit（README.md: # big-data）

## 常用命令
```bash
cd /root/big-data   # 或同步后的同名目录
git status
git add .
git commit -m "your message"
git push -u origin main
```

## 初始化命令备忘
```bash
echo "# big-data" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin git@github.com:siriusllll-s/big-data.git
git push -u origin main
```

## 注意
1. 推送需要本机 SSH 公钥已添加到 GitHub 账号 siriusllll-s
2. 当前 master 上 `ssh -T git@github.com` 曾出现 Permission denied (publickey)，需配置 deploy key 或账号密钥后再 push
3. opencode 启动密码（三台）: 051856
4. opencode 节点: master / client1 / client2

