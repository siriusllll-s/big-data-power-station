<template>
  <div>
    <el-form :inline="true" :model="form">
      <el-form-item label="时间范围">
        <el-date-picker v-model="dates" type="datetimerange" value-format="yyyy-MM-dd HH:mm:ss"
          range-separator="-" start-placeholder="开始" end-placeholder="结束" />
      </el-form-item>
      <el-form-item label="设备名">
        <el-input v-model="form.deviceName" clearable />
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
    </el-form>
    <el-table v-loading="loading" :data="list" border stripe>
      <el-table-column prop="name" label="电站名称" width="140" />
      <el-table-column prop="deviceName" label="故障设备" width="120" />
      <el-table-column prop="faultTime" label="异常时间" width="180" />
      <el-table-column prop="faultDesc" label="异常描述" />
      <el-table-column prop="faultLevel" label="级别" width="90" align="center">
        <template slot-scope="s">
          <el-tag v-if="s.row.faultLevel===0" type="primary" effect="dark">一般</el-tag>
          <el-tag v-if="s.row.faultLevel===1" type="warning" effect="dark">较重</el-tag>
          <el-tag v-if="s.row.faultLevel===2" type="danger" effect="dark">严重</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <pager :pageIndex.sync="form.page" :pageSize.sync="form.limit" :totalItemCount="form.total" @change="load" />
  </div>
</template>
<script>
import Pager from '@/components/Pager'
import { getExceptionPage } from '@/api/DataSearch/dataSearch'
import { getCurrentTimeZone, getCurrentTime } from '@/utils/date'
export default {
  name: 'DataSearch',
  components: { Pager },
  data () {
    return {
      dates: [getCurrentTimeZone(), getCurrentTime()],
      form: { page: 1, limit: 10, total: 0, deviceName: '', start: '', end: '' },
      list: [],
      loading: false
    }
  },
  mounted () { this.load() },
  methods: {
    search () { this.form.page = 1; this.load() },
    load () {
      this.loading = true
      this.form.start = this.dates && this.dates[0] || ''
      this.form.end = this.dates && this.dates[1] || ''
      getExceptionPage(this.form).then(res => {
        this.loading = false
        if (res.successful && res.resultValue) {
          this.list = res.resultValue.list || []
          this.form.total = Number(res.resultValue.total) || 0
          this.form.limit = Number(res.resultValue.limit) || this.form.limit
        } else this.$message.error((res && res.resultHint) || '查询失败')
      }).catch(() => { this.loading = false })
    }
  }
}
</script>
