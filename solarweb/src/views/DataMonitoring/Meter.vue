<template>
  <div>
    <el-table :data="resDate" v-loading="loading" border stripe>
      <el-table-column type="index" label="#"></el-table-column>
      <el-table-column prop="name" label="电表名称"></el-table-column>
      <el-table-column prop="createTime" label="采集时间"></el-table-column>
      <el-table-column prop="dailyPower" label="电表读数(KWH)"></el-table-column>
    </el-table>
  </div>
</template>
<script>
import { getMeterData } from '@/api/DataMonitoring/Monitor'
export default {
  data () { return { loading: true, resDate: [] } },
  mounted () { this.getMeterData() },
  methods: {
    getMeterData () {
      this.loading = true
      getMeterData().then(data => {
        this.loading = false
        if (data.successful) this.resDate = data.resultValue || []
        else this.$message.error(data.resultHint)
      }).catch(() => { this.loading = false })
    }
  }
}
</script>
