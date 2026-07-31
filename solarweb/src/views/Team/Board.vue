<template>
  <div>
    <h2>接口协作台</h2>
    <el-table :data="modules" border stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="模块" width="140" />
      <el-table-column prop="path" label="路径" min-width="160" />
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="s">
          <el-tag :type="s.row.status==='ready'?'success':'warning'" size="small">{{ s.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="owner" label="负责人" width="140" />
      <el-table-column prop="desc" label="说明" min-width="200" />
    </el-table>
  </div>
</template>
<script>
import modules from '@/config/modules'
export default {
  name: 'TeamBoard',
  data () { return { modules } }
}
</script>
