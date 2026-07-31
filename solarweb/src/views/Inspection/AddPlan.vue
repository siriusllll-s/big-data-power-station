<template>
  <el-dialog :title="(form.id ? '编辑' : '新增') + '巡检计划'" :visible.sync="visible" width="45%" append-to-body :close-on-click-modal="false">
    <el-form ref="form" :model="form" label-width="120px" :rules="rules">
      <el-form-item label="计划名称" prop="name"><el-input v-model="form.name"></el-input></el-form-item>
      <el-form-item label="开始日期" prop="beginDate">
        <el-date-picker type="date" value-format="yyyy-MM-dd" v-model="form.beginDate" style="width:100%"></el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期" prop="endDate">
        <el-date-picker type="date" value-format="yyyy-MM-dd" v-model="form.endDate" style="width:100%"></el-date-picker>
      </el-form-item>
      <el-form-item label="巡检点">
        <el-select v-model="form.pointIds" multiple style="width:100%">
          <el-option v-for="p in points" :key="p.id" :label="p.name" :value="p.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="备注"><el-input type="textarea" v-model="form.memo"></el-input></el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="visible=false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { getDetail, save } from '@/api/Inspection/InspectionPlan'
import { getPointList } from '@/api/Inspection/InspectionPoint'
export default {
  data () {
    return {
      visible: false,
      points: [],
      form: { id: null, name: '', beginDate: '', endDate: '', pointIds: [], userIds: [1], memo: '' },
      rules: {
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        beginDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }],
        endDate: [{ required: true, message: '请选择结束日期', trigger: 'change' }]
      }
    }
  },
  methods: {
    showDialog (id) {
      this.visible = true
      this.form = { id: null, name: '', beginDate: '', endDate: '', pointIds: [], userIds: [1], memo: '' }
      getPointList().then(res => { if (res.successful) this.points = res.resultValue || [] })
      if (id) {
        getDetail(id).then(res => {
          if (res.successful && res.resultValue) {
            this.form = Object.assign({ userIds: [1] }, res.resultValue)
          }
        })
      }
    },
    submit () {
      this.$refs.form.validate(ok => {
        if (!ok) return
        save(this.form).then(res => {
          if (res.successful) {
            this.$message.success('保存成功')
            this.visible = false
            this.$emit('close')
          } else this.$message.error(res.resultHint || '失败')
        })
      })
    }
  }
}
</script>
