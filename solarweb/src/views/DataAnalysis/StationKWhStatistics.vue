<template>
  <div class="page">
    <el-form ref="searchForm" :inline="true" :model="searchForm" size="small">
      <el-form-item label="查询类型：">
        <el-select v-model="searchForm.type" @change="changeType" style="width:100px">
          <el-option label="天" value="0"></el-option>
          <el-option label="月" value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="日期：">
        <el-date-picker
          type="daterange"
          value-format="yyyy-MM-dd"
          :format="dateFormat"
          v-model="dates"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :clearable="false"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getDataList" icon="el-icon-search">查询</el-button>
      </el-form-item>
    </el-form>

    <div class="echarts_bg" v-show="showEcharts">
      <div class="echarts" ref="echarts"></div>
    </div>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column type="index" label="#" width="60"></el-table-column>
      <el-table-column prop="powerDate" label="发电日期" min-width="120"></el-table-column>
      <el-table-column prop="kwh" label="发电量(kWh)" min-width="120"></el-table-column>
      <el-table-column prop="radiation" label="辐照量(Wh/㎡)" min-width="130"></el-table-column>
      <el-table-column prop="powerRatio" label="发电效率(%)" min-width="120"></el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getStationKWhPage } from '@/api/DataAnalysis/dataAnalysis'
import { getCurrentDate, getCurrentMonthFirstDay, compareGTOneMonth } from '@/utils/date'

export default {
  name: 'StationKWhStatistics',
  data () {
    return {
      searchForm: {
        type: '0'
      },
      dateFormat: 'yyyy-MM-dd',
      loading: false,
      dataList: [],
      dates: [getCurrentMonthFirstDay(), getCurrentDate()],
      myCharts: null,
      chartTitle: '发电量统计',
      showEcharts: true
    }
  },
  mounted () {
    this.$nextTick(() => {
      this.initECharts()
      this.getDataList()
    })
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy () {
    window.removeEventListener('resize', this.handleResize)
    if (this.myCharts) this.myCharts.dispose()
  },
  methods: {
    handleResize () {
      if (this.myCharts) this.myCharts.resize()
    },
    initECharts () {
      const echarts = require('echarts')
      this.myCharts = echarts.init(this.$refs['echarts'])
    },
    changeType (value) {
      if (value == 0) {
        this.dateFormat = 'yyyy-MM-dd'
      } else if (value == 1) {
        this.dateFormat = 'yyyy-MM'
      }
    },
    getDataList () {
      this.searchForm.start = ''
      this.searchForm.end = ''
      if (this.dates && this.dates.length > 0) {
        this.searchForm.start = this.dates[0]
        this.searchForm.end = this.dates[1]
        if (this.searchForm.type == '0' && compareGTOneMonth(this.dates[0], this.dates[1])) {
          this.$message.error('选择时间不能超过一个月')
          return
        }
      }
      this.loading = true
      this.showEcharts = false
      const payload = {
        type: Number(this.searchForm.type),
        start: this.searchForm.start,
        end: this.searchForm.end
      }
      getStationKWhPage(payload).then(data => {
        this.loading = false
        if (data.successful && data.resultValue) {
          this.dataList = data.resultValue
          if (this.dataList.length > 0) {
            this.showEcharts = true
            this.$nextTick(() => this.setEchartsOption(this.dataList))
          }
        } else {
          this.$message.error((data && data.resultHint) || '查询失败')
        }
      }).catch(err => {
        this.loading = false
        console.error(err)
        this.$message.error('电站发电量统计请求失败')
      })
    },
    setEchartsOption (list) {
      if (!this.myCharts) this.initECharts()
      const powerDateArr = []
      const powerRatioArr = []
      const radiationArr = []
      const kwhArr = []

      list.forEach(item => {
        if (this.searchForm.type == '0') {
          item.powerDate = item.powerDateDay || item.powerDate
          powerDateArr.push(item.powerDateDay || item.powerDate)
        } else {
          item.powerDate = item.powerDateMonth || item.powerDate
          powerDateArr.push(item.powerDateMonth || item.powerDate)
        }
        powerRatioArr.push(item.powerRatio)
        radiationArr.push(item.radiation)
        kwhArr.push(item.kwh)
      })
      const option = {
        title: {
          left: 'center',
          text: this.chartTitle
        },
        color: ['#87D6C6', '#54cdb4', '#5B8FF9'],
        grid: {
          left: '7%',
          right: '13%',
          bottom: 50
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
            shadowStyle: { opacity: 0.3 }
          }
        },
        legend: {
          data: ['辐照量', '发电量', '发电效率'],
          bottom: 0
        },
        xAxis: [{
          type: 'category',
          data: powerDateArr,
          splitLine: { show: false },
          axisLabel: {
            rotate: powerDateArr.length > 15 ? 45 : 0
          }
        }],
        yAxis: [
          {
            type: 'value',
            name: '辐照量',
            nameTextStyle: { align: 'left' },
            nameGap: 20,
            position: 'right',
            splitLine: { show: false },
            axisLine: { show: false },
            axisLabel: { formatter: '{value} Wh/㎡' },
            axisTick: { show: false }
          },
          {
            type: 'value',
            name: '发电量',
            nameTextStyle: { align: 'right' },
            nameGap: 20,
            position: 'left',
            splitLine: { show: false },
            axisLine: { show: false },
            axisLabel: { formatter: '{value} kWh' },
            axisTick: { show: false }
          },
          {
            type: 'value',
            name: '发电效率',
            nameTextStyle: { align: 'left' },
            nameGap: 20,
            position: 'right',
            splitLine: { show: false },
            offset: 90,
            axisLine: { show: false },
            axisLabel: { formatter: '{value} %' },
            axisTick: { show: false }
          }
        ],
        series: [
          {
            name: '辐照量',
            type: 'line',
            data: radiationArr,
            symbol: 'circle',
            yAxisIndex: 0,
            areaStyle: {}
          },
          {
            name: '发电量',
            type: 'bar',
            itemStyle: {
              borderType: 'solid',
              borderWidth: 1,
              borderColor: 'white'
            },
            barWidth: '60%',
            yAxisIndex: 1,
            data: kwhArr
          },
          {
            name: '发电效率',
            type: 'line',
            symbol: 'diamond',
            smooth: true,
            yAxisIndex: 2,
            data: powerRatioArr
          }
        ]
      }
      this.myCharts.setOption(option, true)
      this.$nextTick(() => this.myCharts && this.myCharts.resize())
    }
  }
}
</script>

<style scoped>
.page { padding: 8px; }
.echarts_bg {
  background: white;
  padding: 20px 0;
  margin-bottom: 12px;
}
.echarts {
  height: 350px;
}
</style>
