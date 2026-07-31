<template>
  <div>
    <el-form ref="form" :inline="true" :model="form">
      <el-form-item label="查询类型：">
        <el-select v-model="form.dateType" @change="changeType">
          <el-option label="天" :value="0"></el-option>
          <el-option label="月" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期：">
        <el-date-picker type="daterange" :format="dateFormat" value-format="yyyy-MM-dd" v-model="dates"
          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" :clearable="false"></el-date-picker>
      </el-form-item>
      <el-form-item label="设备类型：">
        <el-select v-model="form.type" @change="changeDeviceType">
          <el-option label="集中式逆变器" :value="0"></el-option>
          <el-option label="直流汇流箱" :value="1"></el-option>
          <el-option label="直流汇流柜" :value="2"></el-option>
          <el-option label="电表" :value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="设备名称：">
        <el-select v-model="form.devices" multiple collapse-tags placeholder="请选择">
          <el-option value="01号逆变器" label="01号逆变器" v-if="form.type==0||form.type=='0'"></el-option>
          <el-option value="02号逆变器" label="02号逆变器" v-if="form.type==0||form.type=='0'"></el-option>
          <el-option value="01号汇流箱" label="01号汇流箱" v-if="form.type==1||form.type=='1'"></el-option>
          <el-option value="02号汇流箱" label="02号汇流箱" v-if="form.type==1||form.type=='1'"></el-option>
          <el-option value="03号汇流箱" label="03号汇流箱" v-if="form.type==1||form.type=='1'"></el-option>
          <el-option value="04号汇流箱" label="04号汇流箱" v-if="form.type==1||form.type=='1'"></el-option>
          <el-option value="05号汇流箱" label="05号汇流箱" v-if="form.type==1||form.type=='1'"></el-option>
          <el-option value="01号直流柜" label="01号直流柜" v-if="form.type==2||form.type=='2'"></el-option>
          <el-option value="02号直流柜" label="02号直流柜" v-if="form.type==2||form.type=='2'"></el-option>
          <el-option value="01号电表" label="01号电表" v-if="form.type==4||form.type=='4'"></el-option>
          <el-option value="02号电表" label="02号电表" v-if="form.type==4||form.type=='4'"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="fl_r">
        <el-button type="primary" @click="getDataList()" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="historyData" border stripe>
      <el-table-column type="index" label="#"></el-table-column>
      <el-table-column prop="date" label="日期"></el-table-column>
      <el-table-column prop="name" label="设备名称"></el-table-column>
      <el-table-column prop="value" label="发电量"></el-table-column>
    </el-table>
  </div>
</template>
<script>
import { getHistoryData } from '@/api/DataMonitoring/Monitor'
import { getCurrentDate, getCurrentMonthFirstDay } from '@/utils/date'
export default {
  data () {
    return {
      form: { dateType: 0, type: 0, devices: ['01号逆变器', '02号逆变器'] },
      dates: [getCurrentMonthFirstDay(), getCurrentDate()],
      dateFormat: 'yyyy-MM-dd',
      historyData: [],
      loading: false
    }
  },
  mounted () { this.getDataList() },
  methods: {
    changeType (value) {
      this.dateFormat = value == 1 ? 'yyyy-MM' : 'yyyy-MM-dd'
    },
    changeDeviceType () { this.form.devices = [] },
    getDataList () {
      if (!this.form.devices || this.form.devices.length === 0) {
        this.$message.error('请选择设备')
        return
      }
      this.form.startDate = this.dates && this.dates[0] || ''
      this.form.endDate = this.dates && this.dates[1] || ''
      this.loading = true
      getHistoryData(this.form).then(data => {
        this.loading = false
        if (data.successful) this.historyData = data.resultValue || []
        else this.$message.error(data.resultHint)
      }).catch(() => { this.loading = false })
    }
  }
}
</script>
