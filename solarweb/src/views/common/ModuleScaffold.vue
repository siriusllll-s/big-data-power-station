<template>
  <div class="module-scaffold">
    <div class="head">
      <div>
        <h2><i :class="meta.icon"></i> {{ meta.name }}</h2>
        <p class="desc">{{ meta.desc }}</p>
      </div>
      <div class="tags">
        <el-tag size="small" :type="meta.status==='ready' ? 'success' : 'warning'">
          {{ meta.status === 'ready' ? '可用' : '待开发' }}
        </el-tag>
        <el-tag size="small" type="info">负责人: {{ meta.owner || '待认领' }}</el-tag>
      </div>
    </div>
    <el-alert
      title="本页为模块说明骨架，完整业务页请从侧栏对应菜单进入。"
      type="info" show-icon :closable="false" class="mb16" />
    <el-card shadow="never">
      <div slot="header">后端接口契约</div>
      <el-table :data="meta.apis || []" size="small" empty-text="本模块暂无独立接口">
        <el-table-column prop="method" label="方法" width="90" />
        <el-table-column prop="url" label="URL" min-width="220" />
        <el-table-column prop="desc" label="说明" />
      </el-table>
    </el-card>
  </div>
</template>
<script>
import modules from '@/config/modules'
export default {
  name: 'ModuleScaffold',
  props: { modulePath: { type: String, required: true } },
  computed: {
    meta () {
      return modules.find(m => m.path === this.modulePath) || {
        name: '未命名模块', desc: '', icon: 'el-icon-document', status: 'todo', apis: [], path: this.modulePath, component: ''
      }
    }
  }
}
</script>
<style scoped>
.module-scaffold { padding: 8px 4px 24px; }
.head { display:flex; justify-content:space-between; margin-bottom: 12px; }
.head h2 { margin: 0 0 6px; font-size: 20px; }
.desc { margin: 0; color: #909399; }
.mb16 { margin-bottom: 16px; }
</style>
