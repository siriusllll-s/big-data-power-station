<template>
  <div class="station-stats-page">
    <div class="page-title">电站发电综合及平均统计</div>
    <div class="ju-row">
      <div class="ju-col" v-for="item in cards" :key="item.key">
        <div class="grid-content" :class="item.cls">
          <div class="title">{{ item.title }}</div>
          <div class="content1">
            {{ display(item.key) }}
            <span>{{ item.unit }}</span>
          </div>
        </div>
      </div>
    </div>
    <div class="actions">
      <el-button type="primary" size="small" :loading="loading" @click="loadData">刷新数据</el-button>
      <span class="tip">接口：GET /api/screen/stationAllAndAverage</span>
    </div>
  </div>
</template>
<script>
import { getStationAllAndAverage } from '@/api/Home/Home'
export default {
  name: 'Home',
  data () {
    return {
      loading: false,
      indexTopArray: {},
      cards: [
        { key: 'allKWh', title: '累计发电量', unit: 'kW·h', cls: 'c1' },
        { key: 'averageKWh', title: '日均发电量', unit: 'kW·h', cls: 'c1' },
        { key: 'allInCome', title: '累计收入', unit: '万元', cls: 'c2' },
        { key: 'averageInCome', title: '日均收入', unit: '万元', cls: 'c2' },
        { key: 'todayInCome', title: '今日收入', unit: '万元', cls: 'c2' },
        { key: 'allReduceCO2', title: '总CO2减排量', unit: 't', cls: 'c3' },
        { key: 'allReduceCoal', title: '总节煤量', unit: 't', cls: 'c3' },
        { key: 'averageReduceCO2', title: '日均CO2减排量', unit: 't', cls: 'c3' },
        { key: 'averageReduceCoal', title: '日均节煤量', unit: 't', cls: 'c3' }
      ]
    }
  },
  mounted () { this.loadData() },
  methods: {
    display (key) {
      const v = this.indexTopArray[key]
      return (v === undefined || v === null || v === '') ? '--' : v
    },
    loadData () {
      this.loading = true
      getStationAllAndAverage().then(data => {
        if (data && data.successful) {
          this.indexTopArray = data.resultValue || {}
        } else {
          this.$message.error((data && data.resultHint) || '获取统计失败')
        }
      }).catch(err => {
        this.$message.error('接口请求失败，请确认后端 8113 已启动')
        console.error(err)
      }).finally(() => { this.loading = false })
    }
  }
}
</script>
<style scoped>
.station-stats-page { padding: 10px; }
.page-title { color:#fff; font-size:20px; margin-bottom:16px; font-weight:600; }
.ju-row { display:flex; flex-wrap:wrap; margin:0 -8px; }
.ju-col { width:11.11%; padding:0 8px; box-sizing:border-box; margin-bottom:16px; min-width:120px; flex:1 1 11%; }
.grid-content { background:#082540; min-height:92px; border-radius:10px; box-shadow:0 0 10px rgba(19,47,154,.19); position:relative; overflow:hidden; }
.grid-content:before { content:""; height:5px; width:100%; position:absolute; bottom:0; }
.grid-content.c1:before { background:#51b8f9; }
.grid-content.c2:before { background:#ff6b6b; }
.grid-content.c3:before { background:#6be1be; }
.title { padding:12px 15px 0; font-size:14px; color:#fff; }
.content1 { padding:6px 15px 12px; color:#fff; font-size:20px; }
.content1 span { display:block; font-size:12px; opacity:.85; margin-top:4px; }
.actions { margin-top:8px; color:#909399; }
.tip { margin-left:12px; font-size:12px; }
</style>
