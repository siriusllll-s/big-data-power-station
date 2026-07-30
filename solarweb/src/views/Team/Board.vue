<template>
  <div class="team-board">
    <h2><i class="el-icon-s-cooperation"></i> 团队接口协作台</h2>
    <p class="sub">清理登录后无用界面，统一用模块骨架 + 接口契约推进开发</p>
    <el-table :data="list" border size="small" style="width:100%">
      <el-table-column prop="id" label="#" width="50" />
      <el-table-column prop="group" label="分组" width="80" />
      <el-table-column prop="name" label="模块" width="120" />
      <el-table-column prop="path" label="路由" width="150" />
      <el-table-column prop="owner" label="负责人" width="140" />
      <el-table-column prop="status" label="状态" width="90">
        <template slot-scope="s">
          <el-tag size="mini" :type="s.row.status===ready?success:warning">{{ s.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="desc" label="说明" min-width="180" />
      <el-table-column label="操作" width="100">
        <template slot-scope="s">
          <el-button type="text" @click="$router.push(s.row.path)">打开</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
<script>
import modules from @/config/modules
export default {
  name: TeamBoard,
  data () {
    return { list: modules }
  }
}
</script>
<style scoped>
.team-board { padding: 8px; }
.team-board h2 { margin: 0 0 6px; }
.sub { color:#909399; margin: 0 0 16px; }
</style>
