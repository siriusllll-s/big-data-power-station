<template>
  <div>
    <el-form ref="form" :inline="true" :model="form">
      <el-form-item label="日期：">
        <el-date-picker type="daterange" value-format="yyyy-MM-dd" format="yyyy-MM-dd" v-model="dates"
          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :clearable="false"></el-date-picker>
      </el-form-item>
      <el-form-item class="fl_r">
        <el-button type="primary" @click="getStationPowerData(0)" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="powerData" border stripe>
      <el-table-column type="index" label="#"></el-table-column>
      <el-table-column prop="inPower" label="日输入电量"></el-table-column>
      <el-table-column prop="lossKwh" label="损失发电量"></el-table-column>
      <el-table-column prop="outPower" label="日输出电量"></el-table-column>
      <el-table-column prop="powerDate" label="电站发电日期"></el-table-column>
      <el-table-column prop="powerRatio" label="日发电效率"></el-table-column>
    </el-table>
    <pager :pageIndex.sync="form.page" :pageSize.sync="form.limit" :totalItemCount="form.total" @change="getStationPowerData"></pager>
  </div>
</template>
<script>
import Pager from '@/components/Pager'
import { getStationPowerData } from '@/api/DataMonitoring/Monitor'
import { getCurrentDate, getCurrentMonthFirstDay } from '@/utils/date'
export default {
  components: { Pager },
  data () {
    return {
      form: { page: 1, limit: 10, total: 0 },
      dates: [getCurrentMonthFirstDay(), getCurrentDate()],
      powerData: [],
      loading: false
    }
  },
  mounted () { this.getStationPowerData() },
  methods: {
    getStationPowerData (searchType) {
      if (searchType === 0) this.form.page = 1
      this.form.startDate = this.dates && this.dates[0] || ''
      this.form.endDate = this.dates && this.dates[1] || ''
      this.loading = true
      getStationPowerData(this.form).then(data => {
        this.loading = false
        if (data.successful && data.resultValue) {
          this.powerData = data.resultValue.list || []
          this.form.limit = Number(data.resultValue.limit) || this.form.limit
          this.form.total = Number(data.resultValue.total) || 0
          if (data.resultValue.page) this.form.page = Number(data.resultValue.page)
        } else this.$message.error(data.resultHint)
      }).catch(() => { this.loading = false })
    }
  }
}
</script>
