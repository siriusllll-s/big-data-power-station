<template>
  <div>
    <el-table :data="resDate" v-loading="loading" border stripe>
      <el-table-column type="index" label="#"></el-table-column>
      <el-table-column prop="name" label="逆变器名称"></el-table-column>
      <el-table-column prop="createTime" label="采集时间"></el-table-column>
      <el-table-column prop="dailyPower" label="日发电(KWH)"></el-table-column>
      <el-table-column prop="dc" label="直流电流(A)"></el-table-column>
      <el-table-column prop="dcPower" label="直流功率(KW)"></el-table-column>
      <el-table-column prop="dcVoltage" label="直流电压(V)"></el-table-column>
    </el-table>
  </div>
</template>
<script>
import { getInverterData } from '@/api/DataMonitoring/Monitor'
export default {
  data () { return { loading: true, resDate: [] } },
  mounted () { this.getInverterData() },
  methods: {
    getInverterData () {
      this.loading = true
      getInverterData().then(data => {
        this.loading = false
        if (data.successful) this.resDate = data.resultValue || []
        else this.$message.error(data.resultHint)
      }).catch(() => { this.loading = false })
    }
  }
}
</script>
