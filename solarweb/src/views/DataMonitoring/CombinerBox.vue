<template>
  <div v-if="showDiv">
    <div v-for="(item, key) in resDate" :key="key">
      <el-tag style="font-weight:bold;font-size:15px;margin-top:12px;">{{ key }}</el-tag>
      <el-table :data="item" v-loading="loading" border stripe>
        <el-table-column type="index" label="#"></el-table-column>
        <el-table-column prop="name" label="汇流箱名称"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column prop="combinerBoxIns" label="汇流箱输入值">
          <template slot-scope="scope">{{ (scope.row.combinerBoxIns || []).join(',') }}</template>
        </el-table-column>
        <el-table-column prop="combinerBox" label="汇流箱输出值"></el-table-column>
      </el-table>
    </div>
  </div>
</template>
<script>
import { getCombinerBoxData } from '@/api/DataMonitoring/Monitor'
export default {
  data () { return { loading: true, showDiv: false, resDate: {} } },
  mounted () { this.getCombinerBoxData() },
  methods: {
    getCombinerBoxData () {
      this.loading = true
      getCombinerBoxData().then(data => {
        this.loading = false
        if (data.successful) {
          this.resDate = data.resultValue || {}
          this.showDiv = true
        } else {
          this.showDiv = false
          this.$message.error(data.resultHint)
        }
      }).catch(() => { this.loading = false })
    }
  }
}
</script>
