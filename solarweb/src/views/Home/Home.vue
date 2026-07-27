<template>
  <div class="home-page">
    <el-row :gutter="16">
      <el-col :span="6">
        <HomeLeft />
      </el-col>
      <el-col :span="10">
        <HomeCentre />
        <el-card shadow="never" class="map-card" style="margin-top:16px">
          <div slot="header" class="clearfix">
            <span>电站地图</span>
            <el-button style="float:right;padding:3px 0" type="text" @click="getStationDetail">刷新位置</el-button>
          </div>
          <div ref="mapContainer" id="stationMap" class="map-box"></div>
          <div class="map-info" v-if="mapData">
            <span>{{ mapData.name || mapData.shortName }}</span>
            <span v-if="mapData.address"> · {{ mapData.address }}</span>
            <span v-if="mapData.lon != null"> · 经度 {{ mapData.lon }}</span>
            <span v-if="mapData.lat != null"> · 纬度 {{ mapData.lat }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="never" class="chart-card">
          <div ref="echarts1" class="echarts-box"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-card shadow="never" class="ammeter-card">
      <div slot="header" class="clearfix">
        <span>电站电表读数</span>
        <el-button style="float:right;padding:3px 0" type="text" @click="getAmmeterData" :loading="loading">刷新</el-button>
      </div>
      <el-form :inline="true" size="small" class="query-form">
        <el-form-item label="电表">
          <el-select v-model="name" placeholder="选择电表" @change="getAmmeterData" clearable style="width:160px">
            <el-option v-for="item in ammeterOptions" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getAmmeterData" :loading="loading">查询</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="ammeterData" v-loading="loading" border stripe style="width:100%" empty-text="暂无电表读数">
        <el-table-column prop="station" label="电站ID" width="90" />
        <el-table-column prop="name" label="电表名称" min-width="120" />
        <el-table-column prop="inverter" label="逆变器" min-width="120" />
        <el-table-column prop="ammeter" label="电表读数(kWh)" min-width="140" />
        <el-table-column prop="createTime" label="采集时间" min-width="180">
          <template slot-scope="scope">
            {{ formatTime(scope.row.createTime) }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { getAmmeterData, getStationMonthPower } from '@/api/Home/Home'
import { getStationDetail } from '@/api/Station/Station'
import HomeLeft from '@/views/Home/Left'
import HomeCentre from '@/views/Home/Centre'

export default {
  name: 'Home',
  components: { HomeLeft, HomeCentre },
  data () {
    return {
      loading: false,
      name: '01号电表',
      ammeterOptions: ['01号电表', '02号电表'],
      form: {
        name: '',
        station: 1
      },
      ammeterData: [],
      myChart1: null,
      dataList1: null,
      mapData: null,
      map: null,
      marker: null
    }
  },
  mounted () {
    this.$nextTick(() => {
      this.initECharts()
      this.getEchart1()
      this.getStationDetail()
    })
    this.getAmmeterData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy () {
    window.removeEventListener('resize', this.handleResize)
    if (this.myChart1) this.myChart1.dispose()
    if (this.map) {
      this.map.destroy()
      this.map = null
    }
  },
  methods: {
    handleResize () {
      if (this.myChart1) this.myChart1.resize()
      if (this.map) this.map.getSize && this.map.getSize()
    },
    formatTime (val) {
      if (!val) return ''
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      const pad = n => (n < 10 ? '0' + n : '' + n)
      return d.getFullYear() + '-' + pad(d.getMonth() + 1) + '-' + pad(d.getDate()) +
        ' ' + pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds())
    },
    /** 获取电站详情（经纬度）并初始化地图 */
    getStationDetail () {
      getStationDetail().then(data => {
        if (data.successful) {
          this.mapData = data.resultValue
          this.initMap(this.mapData)
        } else {
          this.$message.error(data.resultHint || '获取电站信息失败')
        }
      }).catch(err => {
        console.error(err)
        this.$message.error('电站详情请求失败')
      })
    },
    /** 高德地图：根据 lon/lat 打点展示 */
    initMap (data) {
      if (typeof window.AMap === 'undefined') {
        this.$message.warning('高德地图脚本未加载，请检查 public/index.html 中的 Key')
        return
      }
      const lon = data && data.lon != null ? Number(data.lon) : 120.412455
      const lat = data && data.lat != null ? Number(data.lat) : 36.113991
      const title = (data && (data.name || data.shortName)) || '电站'
      const address = (data && data.address) || ''

      if (!this.map) {
        this.map = new window.AMap.Map(this.$refs.mapContainer || 'stationMap', {
          zoom: 12,
          center: [lon, lat],
          viewMode: '2D'
        })
      } else {
        this.map.setZoomAndCenter(12, [lon, lat])
      }

      if (this.marker) {
        this.map.remove(this.marker)
        this.marker = null
      }
      this.marker = new window.AMap.Marker({
        position: [lon, lat],
        title: title,
        map: this.map
      })
      const info = new window.AMap.InfoWindow({
        content: '<div style="padding:6px 10px;line-height:1.5">' +
          '<b>' + title + '</b><br/>' +
          (address ? (address + '<br/>') : '') +
          '经度: ' + lon + '<br/>纬度: ' + lat +
          '</div>',
        offset: new window.AMap.Pixel(0, -30)
      })
      this.marker.on('click', () => {
        info.open(this.map, this.marker.getPosition())
      })
      info.open(this.map, this.marker.getPosition())
    },
    initECharts () {
      const echarts = require('echarts')
      this.myChart1 = echarts.init(this.$refs['echarts1'])
    },
    getEchart1 () {
      getStationMonthPower().then(data => {
        if (data.successful && data.resultValue) {
          this.dataList1 = data.resultValue
          this.echart1(this.dataList1)
        } else {
          this.$message.error((data && data.resultHint) || '获取本月发电数据失败')
        }
      }).catch(err => {
        console.error(err)
      })
    },
    echart1 (list) {
      if (!this.myChart1 || !list) return
      const echarts = require('echarts')
      const dataName = []
      const data1 = []
      const data2 = []
      const stationName = (list.stationInfo && (list.stationInfo.name || list.stationInfo.shortName)) || '电站'
      dataName.push(stationName)
      data1.push(list.allKWh != null ? list.allKWh : (list.monthPower || 0))
      data2.push(list.powerRatio != null ? list.powerRatio : (list.monthRatio || 0))
      const option = {
        backgroundColor: '#0b1a33',
        title: {
          text: '本月发电量/发电效率完成比',
          top: 12,
          left: 12,
          textStyle: { color: '#fff', fontWeight: 'normal', fontSize: 14 }
        },
        tooltip: { trigger: 'axis' },
        grid: { top: '22%', left: 40, right: 40, bottom: '10%', containLabel: true },
        legend: {
          icon: 'circle',
          data: ['发电量', '发电效率'],
          right: '4%',
          top: 12,
          textStyle: { color: '#fff', fontSize: 12 }
        },
        xAxis: {
          type: 'category',
          data: dataName,
          axisLine: { lineStyle: { color: '#d2dde7' } },
          axisLabel: { color: '#fff' }
        },
        yAxis: [
          {
            type: 'value',
            name: '发电量',
            nameTextStyle: { color: '#bac8d5' },
            axisLabel: { color: '#bac8d5' },
            splitLine: { lineStyle: { color: 'rgba(210,221,231,0.2)' } }
          },
          {
            type: 'value',
            name: '效率',
            nameTextStyle: { color: '#bac8d5' },
            axisLabel: { color: '#bac8d5' },
            splitLine: { show: false }
          }
        ],
        series: [
          {
            name: '发电量',
            type: 'line',
            data: data1,
            smooth: true,
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#2c7bed' },
                { offset: 1, color: 'rgba(255,255,255,.2)' }
              ])
            },
            itemStyle: { color: '#2c7bed' }
          },
          {
            name: '发电效率',
            type: 'line',
            yAxisIndex: 1,
            data: data2,
            smooth: true,
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0.2, color: '#31c5ea' },
                { offset: 1, color: 'rgba(255,255,255,.2)' }
              ])
            },
            itemStyle: { color: '#31c5ea' }
          }
        ]
      }
      this.myChart1.setOption(option)
    },
    getAmmeterData () {
      this.loading = true
      this.form.name = (this.name || '').slice(0, 2)
      getAmmeterData(this.form).then(data => {
        if (data && data.successful) {
          this.ammeterData = data.resultValue || []
          this.loading = false
        } else {
          this.loading = false
          this.$message.error((data && data.resultHint) || '查询失败')
        }
      }).catch(err => {
        this.loading = false
        console.error(err)
      })
    }
  }
}
</script>

<style scoped>
.home-page { padding: 4px; }
.map-card { border-radius: 10px; margin-bottom: 16px; }
.map-box { width: 100%; height: 360px; }
.map-info { margin-top: 8px; color: #606266; font-size: 13px; line-height: 1.5; }
.chart-card { border-radius: 10px; margin-bottom: 16px; background: #0b1a33; }
.echarts-box { width: 100%; height: 360px; }
.ammeter-card { border-radius: 10px; }
.query-form { margin-bottom: 8px; }
</style>
