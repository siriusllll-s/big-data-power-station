<template>
  <el-dialog title="巡检点详情" :visible.sync="visible" width="40%" append-to-body>
    <el-form label-width="120px" v-if="info">
      <el-form-item label="名称：">{{ info.name }}</el-form-item>
      <el-form-item label="项目：">{{ info.projectName }}</el-form-item>
      <el-form-item label="事项：">{{ (info.itemNames || []).join('、') }}</el-form-item>
      <el-form-item label="设备：">{{ (info.deviceNames || []).join('、') }}</el-form-item>
      <el-form-item label="备注：">{{ info.memo }}</el-form-item>
    </el-form>
    <span slot="footer"><el-button type="primary" @click="visible=false">关闭</el-button></span>
  </el-dialog>
</template>
<script>
import { getInfo } from '@/api/Inspection/InspectionPoint'
export default {
  data () { return { visible: false, info: null } },
  methods: {
    showDialog (id) {
      this.visible = true
      getInfo(id).then(res => {
        if (res.successful) this.info = res.resultValue
        else this.$message.error(res.resultHint)
      })
    }
  }
}
</script>
