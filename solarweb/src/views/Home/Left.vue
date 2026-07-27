<template>
  <div class="home-left">
    <div class="grid-ul grid-content">
      <div class="title">发电量</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="value">{{ leftArray.dayKWh | num }}</div>
          <div class="label">今日发电量(kW·h)</div>
        </el-col>
        <el-col :span="12">
          <div class="value">{{ leftArray.yearKWh | num }}</div>
          <div class="label">今年发电量(kW·h)</div>
        </el-col>
      </el-row>
    </div>
    <div class="grid-ul grid-content mt20">
      <div class="title">节煤量</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="value">{{ leftArray.dayReduceCoal | num }}</div>
          <div class="label">今日节煤量(kg)</div>
        </el-col>
        <el-col :span="12">
          <div class="value">{{ leftArray.yearReduceCoal | num }}</div>
          <div class="label">今年节煤量(kg)</div>
        </el-col>
      </el-row>
    </div>
    <div class="grid-ul grid-content mt20">
      <div class="title">CO2减排量</div>
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="value">{{ leftArray.dayReduceCO2 | num }}</div>
          <div class="label">今日减排量(kg)</div>
        </el-col>
        <el-col :span="12">
          <div class="value">{{ leftArray.yearReduceCO2 | num }}</div>
          <div class="label">今年减排量(kg)</div>
        </el-col>
      </el-row>
    </div>
    <div class="grid-ul grid-content mt20 h400">
      <div class="title">电站基本信息</div>
      <div class="info-row">
        <dl>
          <dt v-if="detailArray.name">电站名称：{{ detailArray.name }}</dt>
        </dl>
        <dl>
          <dt v-if="detailArray.installCapacity != null">装机容量：{{ detailArray.installCapacity }} MWp</dt>
        </dl>
        <dl>
          <dt v-if="cityText">所在城市：{{ cityText }}</dt>
        </dl>
        <dl>
          <dt v-if="detailArray.saleTypeText">售电方式：{{ detailArray.saleTypeText }}</dt>
        </dl>
        <dl>
          <dt v-if="maintainName">运维负责人：{{ maintainName }}</dt>
        </dl>
        <dl>
          <dt v-if="detailArray.contractPower != null">年度合同发电量：{{ detailArray.contractPower }} kW·h</dt>
        </dl>
        <dl>
          <dt v-if="detailArray.own">电站业主：{{ detailArray.own }}</dt>
        </dl>
        <dl>
          <dt v-if="detailArray.statusText">电站状态：{{ detailArray.statusText }}</dt>
        </dl>
        <dl>
          <dt v-if="detailArray.address">地址：{{ detailArray.address }}</dt>
        </dl>
      </div>
    </div>
  </div>
</template>

<script>
import { getStationDayAndYearPower } from '@/api/Home/Home'
import { getStationDetail } from '@/api/Station/Station'

export default {
  name: 'HomeLeft',
  filters: {
    num (v) {
      if (v === null || v === undefined || v === '') return '--'
      const n = Number(v)
      if (isNaN(n)) return v
      return n.toLocaleString(undefined, { maximumFractionDigits: 2 })
    }
  },
  data () {
    return {
      leftArray: {
        dayKWh: 0,
        yearKWh: 0,
        dayReduceCoal: 0,
        yearReduceCoal: 0,
        dayReduceCO2: 0,
        yearReduceCO2: 0
      },
      detailArray: {}
    }
  },
  computed: {
    cityText () {
      const d = this.detailArray || {}
      const p = d.province && d.province.province ? d.province.province : ''
      const c = d.city && d.city.city ? d.city.city : ''
      return (p || '') + (c || '')
    },
    maintainName () {
      const o = this.detailArray && this.detailArray.maintainPersonObj
      if (!o) return ''
      return o.trueName || o.name || ''
    }
  },
  mounted () {
    this.leftInfo()
    this.information()
  },
  methods: {
    leftInfo () {
      getStationDayAndYearPower().then(data => {
        if (data.successful) {
          this.leftArray = data.resultValue || this.leftArray
        } else {
          this.$message.error(data.resultHint || '获取发电量失败')
        }
      }).catch(err => {
        console.error(err)
      })
    },
    information () {
      getStationDetail().then(data => {
        if (data.successful) {
          const detail = Object.assign({}, data.resultValue || {})
          if (detail.status == 0) {
            detail.statusText = '运行中'
          } else if (detail.status == 1) {
            detail.statusText = '建设中'
          } else if (detail.status == 2) {
            detail.statusText = '未开工'
          } else {
            detail.statusText = '维修中'
          }

          if (detail.saleType == 1) {
            detail.saleTypeText = '全额上网'
          } else if (detail.saleType == 2) {
            detail.saleTypeText = '自发自用'
          } else {
            detail.saleTypeText = '余电上网'
          }
          this.detailArray = detail
        } else {
          this.$message.error(data.resultHint || '获取电站信息失败')
        }
      }).catch(err => {
        console.error(err)
      })
    }
  }
}
</script>

<style scoped>
.home-left { color: #fff; }
.info-row { line-height: 40px; }
.info-row dl {
  padding: 0 15px;
  font-size: 14px;
  color: #fff;
  display: flex;
  margin: 0;
}
.info-row dl dt {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.mt20 { margin-top: 20px; }
.title {
  font-size: 18px;
  color: #fff;
}
.value {
  font-size: 28px;
  font-weight: bold;
  font-family: Arial, Helvetica, sans-serif;
}
.label {
  font-size: 14px;
  color: #fff;
}
.grid-content {
  overflow: hidden;
  position: relative;
  background: #082540;
  height: 160px;
  padding: 0 15px;
  line-height: 52px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(19, 47, 154, 0.19);
}
.grid-content:before {
  content: "";
  height: 5px;
  width: 100%;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
}
.grid-ul:nth-child(1) .value { color: #51b8f9; }
.grid-ul:nth-child(2) .value { color: #ff6a6c; }
.grid-ul:nth-child(3) .value { color: #58dbb4; }
.grid-content:nth-child(1):before { background: #51b8f9; }
.grid-content:nth-child(2):before { background: #ff6a6c; }
.grid-content:nth-child(3):before { background: #58dbb4; }
.h400 { height: auto; min-height: 360px; padding-bottom: 12px; }
@media (max-width: 1440px) {
  .title { font-size: 16px; }
  .value { font-size: 22px; }
  .label { font-size: 12px; }
  .grid-content {
    height: 120px;
    padding: 0 10px;
    line-height: 40px;
  }
  .info-row { line-height: 30px; }
  .info-row dl { padding: 0 5px; font-size: 12px; }
  .h400 { min-height: 280px; }
}
</style>
