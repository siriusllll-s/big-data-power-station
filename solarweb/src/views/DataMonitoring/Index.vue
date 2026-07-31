<template>
  <div>
    <el-row :gutter="16">
      <el-col :span="8"><el-card><div>今日发电量</div><h2>{{ day.dayPower || 0 }} kWh</h2></el-card></el-col>
      <el-col :span="8"><el-card><div>今年发电量</div><h2>{{ day.yearPower || 0 }} kWh</h2></el-card></el-col>
      <el-col :span="8"><el-card><div>天气</div><h2>{{ weather.weatherName || '-' }}</h2></el-card></el-col>
    </el-row>
    <el-card class="mt" header="最近电表读数">
      <el-table :data="ammeters" border stripe v-loading="loading">
        <el-table-column prop="name" label="电表" />
        <el-table-column prop="ammeter" label="读数" />
        <el-table-column prop="createTime" label="时间" />
      </el-table>
      <el-button class="mt" type="primary" size="small" @click="refresh">刷新</el-button>
    </el-card>
  </div>
</template>
<script>
import { getStationDayAndYearPower, getWeather, getAmmeterData } from '@/api/Home/Home'
export default {
  name: 'DataMonitoring',
  data () { return { day: {}, weather: {}, ammeters: [], loading: false } },
  mounted () { this.refresh() },
  methods: {
    refresh () {
      this.loading = true
      Promise.all([
        getStationDayAndYearPower(),
        getWeather(),
        getAmmeterData({ name: '' })
      ]).then(([d, w, a]) => {
        this.loading = false
        if (d.successful) this.day = d.resultValue || {}
        if (w.successful) this.weather = w.resultValue || {}
        if (a.successful) this.ammeters = a.resultValue || []
      }).catch(() => { this.loading = false })
    }
  }
}
</script>
<style scoped>
.mt { margin-top: 16px; }
h2 { margin: 8px 0 0; }
</style>
