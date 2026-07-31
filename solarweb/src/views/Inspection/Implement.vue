<template>
  <div>
    <el-calendar>
      <template slot="dateCell" slot-scope="{ date, data }">
        <p>
          {{ data.day.split('-').slice(1).join('-') }}
          <br />
          <span style="font-size:12px;color:#409EFF;white-space:pre-wrap">{{ dealMyDate(data.day) }}</span>
        </p>
      </template>
    </el-calendar>
  </div>
</template>
<script>
import { getManageList } from '@/api/Inspection/InspectionPoint'
export default {
  data () {
    return { resDate: {} }
  },
  mounted () { this.getData() },
  methods: {
    getData () {
      getManageList().then(data => {
        if (data.successful) this.resDate = data.resultValue || {}
        else this.$message.error(data.resultHint)
      })
    },
    dealMyDate (value) {
      const arr = this.resDate[value]
      if (!arr || !arr.length) return ''
      return arr.map(item => {
        let st = item.status
        if (st === 0) st = '未巡检'
        else if (st === 1) st = '巡检中'
        else if (st === 2) st = '已巡检'
        else st = '未完成'
        return (item.name || '') + ',' + st
      }).join('\n')
    }
  }
}
</script>
