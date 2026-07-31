<template>
  <el-dialog title="巡检计划详情" :visible.sync="visible" width="40%" append-to-body>
    <el-form label-width="120px" v-if="info">
      <el-form-item label="名称：">{{ info.name }}</el-form-item>
      <el-form-item label="开始：">{{ info.beginDate }}</el-form-item>
      <el-form-item label="结束：">{{ info.endDate }}</el-form-item>
      <el-form-item label="巡检点：">{{ (info.pointNames || []).join('、') }}</el-form-item>
      <el-form-item label="人员：">{{ (info.userNames || []).join('、') }}</el-form-item>
      <el-form-item label="备注：">{{ info.memo }}</el-form-item>
    </el-form>
    <span slot="footer"><el-button type="primary" @click="visible=false">关闭</el-button></span>
  </el-dialog>
</template>
<script>
import { getView } from '@/api/Inspection/InspectionPlan'
export default {
  data () { return { visible: false, info: null } },
  methods: {
    showDialog (id) {
      this.visible = true
      getView(id).then(res => {
        if (res.successful) this.info = res.resultValue
        else this.$message.error(res.resultHint)
      })
    }
  }
}
</script>
