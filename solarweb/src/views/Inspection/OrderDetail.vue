<template>
  <el-dialog title="工单详情" :visible.sync="visible" width="50%" append-to-body>
    <el-form label-width="120px" v-if="info">
      <el-form-item label="标题：">{{ info.title }}</el-form-item>
      <el-form-item label="状态：">{{ statusText(info.status) }}</el-form-item>
      <el-form-item label="设备：">{{ (info.deviceNames || []).join(',') }}</el-form-item>
      <el-form-item label="故障时间：">{{ info.exceptionTime }}</el-form-item>
      <el-form-item label="预计恢复：">{{ info.forecastTime }}</el-form-item>
      <el-form-item label="处理人：">{{ (info.userNames || []).join(',') }}</el-form-item>
      <el-form-item label="描述：">{{ info.description }}</el-form-item>
      <el-form-item label="处理记录：">
        <el-timeline v-if="info.history && info.history.length">
          <el-timeline-item v-for="(h, i) in info.history" :key="i" :timestamp="h.handleTime">
            {{ statusText(h.status) }} / {{ h.handleUser }}：{{ h.handleDesc }}
          </el-timeline-item>
        </el-timeline>
        <span v-else>无</span>
      </el-form-item>
    </el-form>
    <span slot="footer"><el-button type="primary" @click="visible=false">关闭</el-button></span>
  </el-dialog>
</template>
<script>
import { getDetail } from '@/api/Inspection/Inspection'
export default {
  data () { return { visible: false, info: null } },
  methods: {
    statusText (s) {
      return { 1: '新建', 2: '处理中', 3: '已解决', 4: '关闭' }[s] || s
    },
    showDialog (id) {
      this.visible = true
      getDetail(id).then(res => {
        if (res.successful) this.info = res.resultValue
        else this.$message.error(res.resultHint)
      })
    }
  }
}
</script>
