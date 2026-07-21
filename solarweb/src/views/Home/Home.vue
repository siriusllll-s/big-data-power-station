<template>
  <div class="home-page">
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="c in cards" :key="c.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-label">{{ c.label }}</div>
          <div class="stat-value">{{ c.value }}</div>
          <div class="stat-tip">{{ c.tip }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="16">
      <el-col :span="16">
        <el-card shadow="never">
          <div slot="header">快捷入口（业务模块接口页）</div>
          <div class="entry-grid">
            <div class="entry" v-for="m in entries" :key="m.path" @click="$router.push(m.path)">
              <i :class="m.icon"></i>
              <span>{{ m.name }}</span>
              <el-tag size="mini" :type="m.status===ready?success:info">{{ m.status }}</el-tag>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never">
          <div slot="header">当前会话</div>
          <p>用户：<b>{{ userName || "未命名" }}</b></p>
          <p>Token：已注入 localStorage.Authorization</p>
          <el-button type="primary" size="small" @click="$router.push(/team-board)">打开协作台</el-button>
          <el-button size="small" @click="logout">退出登录</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import modules from @/config/modules
export default {
  name: Home,
  data () {
    return {
      userName: localStorage.getItem(userName) || ,
      cards: [
        { label: 接入电站, value: —, tip: 待 /api/home/summary },
        { label: 在线设备, value: —, tip: 待实时监控接口 },
        { label: 今日发电, value: —, tip: kWh },
        { label: 待办工单, value: —, tip: 待巡检运维接口 }
      ]
    }
  },
  computed: {
    entries () {
      return modules.filter(m => m.path !== /home)
    }
  },
  methods: {
    logout () {
      localStorage.removeItem(Authorization)
      localStorage.removeItem(userMenu)
      localStorage.removeItem(userName)
      this.$router.replace(/login)
    }
  }
}
</script>
<style scoped>
.home-page { padding: 4px; }
.stat-row { margin-bottom: 16px; }
.stat-card { border-radius: 10px; }
.stat-label { color:#909399; font-size:13px; }
.stat-value { font-size:28px; font-weight:700; margin: 8px 0; color:#303133; }
.stat-tip { color:#c0c4cc; font-size:12px; }
.entry-grid { display:grid; grid-template-columns: repeat(3, 1fr); gap:12px; }
.entry {
  border:1px solid #ebeef5; border-radius:10px; padding:14px;
  cursor:pointer; display:flex; flex-direction:column; gap:8px;
  transition: all .15s;
}
.entry:hover { border-color:#409EFF; box-shadow:0 4px 12px rgba(64,158,255,.15); }
.entry i { font-size:22px; color:#409EFF; }
</style>
