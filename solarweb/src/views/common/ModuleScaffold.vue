<template>
  <div class="module-scaffold">
    <div class="head">
      <div>
        <h2><i :class="meta.icon"></i> {{ meta.name }}</h2>
        <p class="desc">{{ meta.desc }}</p>
      </div>
      <div class="tags">
        <el-tag size="small" :type="meta.status===ready ? success : warning">
          {{ meta.status === ready ? 可用 : 待开发 }}
        </el-tag>
        <el-tag size="small" type="info">负责人: {{ meta.owner || 待认领 }}</el-tag>
      </div>
    </div>

    <el-alert
      title="本页为团队协作接口/界面骨架：先对接 API 与交互，再补完整业务 UI"
      type="info"
      show-icon
      :closable="false"
      class="mb16" />

    <el-row :gutter="16">
      <el-col :span="14">
        <el-card shadow="never">
          <div slot="header">后端接口契约</div>
          <el-table :data="meta.apis || []" size="small" empty-text="本模块暂无独立接口">
            <el-table-column prop="method" label="方法" width="90" />
            <el-table-column prop="url" label="URL" min-width="220" />
            <el-table-column prop="desc" label="说明" />
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="10">
        <el-card shadow="never">
          <div slot="header">前端实现清单</div>
          <ul class="checklist">
            <li>页面路由: <code>{{ meta.path }}</code></li>
            <li>组件: <code>src/views/{{ meta.component }}.vue</code></li>
            <li>API 封装: <code>src/api/...</code></li>
            <li>对接模块: photovoltaic-information</li>
          </ul>
          <el-button type="primary" size="small" plain @click="copyPath">复制组件路径</el-button>
        </el-card>
        <el-card shadow="never" class="mt16">
          <div slot="header">联调状态（本地 mock）</div>
          <el-form label-width="80px" size="small">
            <el-form-item label="接口">
              <el-select v-model="tryApi" placeholder="选择接口" style="width:100%">
                <el-option v-for="a in meta.apis" :key="a.url" :label="a.method +   + a.url" :value="a.url" />
              </el-select>
            </el-form-item>
            <el-button type="success" size="small" :disabled="!tryApi" @click="mockCall">模拟请求</el-button>
          </el-form>
          <pre class="mock-out" v-if="mockResult">{{ mockResult }}</pre>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import modules from @/config/modules
export default {
  name: ModuleScaffold,
  props: {
    modulePath: { type: String, required: true }
  },
  data () {
    return {
      tryApi: ,
      mockResult: 
    }
  },
  computed: {
    meta () {
      return modules.find(m => m.path === this.modulePath) || {
        name: 未命名模块,
        desc: ,
        icon: el-icon-document,
        status: todo,
        apis: [],
        path: this.modulePath,
        component: 
      }
    }
  },
  methods: {
    copyPath () {
      const t = src/views/ + this.meta.component + .vue
      this.$message.success(组件路径:  + t)
    },
    mockCall () {
      const api = (this.meta.apis || []).find(a => a.url === this.tryApi)
      this.mockResult = JSON.stringify({
        ok: true,
        mock: true,
        method: api && api.method,
        url: this.tryApi,
        data: [],
        message: 占位响应，待后端实现后替换 request 调用
      }, null, 2)
    }
  }
}
</script>

<style scoped>
.module-scaffold { padding: 8px 4px 24px; }
.head { display:flex; justify-content:space-between; align-items:flex-start; margin-bottom: 12px; }
.head h2 { margin: 0 0 6px; font-size: 20px; color: #1f2d3d; }
.head .desc { margin: 0; color: #909399; }
.tags > * { margin-left: 8px; }
.mb16 { margin-bottom: 16px; }
.mt16 { margin-top: 16px; }
.checklist { padding-left: 18px; line-height: 1.9; color: #606266; }
.checklist code { background: #f5f7fa; padding: 1px 6px; border-radius: 3px; }
.mock-out { background:#0f172a; color:#e2e8f0; padding:12px; border-radius:8px; font-size:12px; overflow:auto; max-height:220px; }
</style>
