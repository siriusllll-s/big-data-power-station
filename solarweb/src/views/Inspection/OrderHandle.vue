<template>
  <el-dialog title="处理工单" :visible.sync="visible" width="40%" append-to-body :close-on-click-modal="false">
    <el-form :model="form" label-width="100px">
      <el-form-item label="工单">{{ title }}</el-form-item>
      <el-form-item label="状态">
        <el-select v-model="form.status" style="width:100%">
          <el-option label="处理中" :value="2"></el-option>
          <el-option label="已解决" :value="3"></el-option>
          <el-option label="关闭" :value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="处理人">
        <el-input v-model="form.handleUser"></el-input>
      </el-form-item>
      <el-form-item label="处理说明">
        <el-input type="textarea" v-model="form.handleDesc"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="visible=false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { handle } from '@/api/Inspection/Inspection'
export default {
  data () {
    return {
      visible: false,
      title: '',
      form: { id: null, status: 2, handleUser: 'admin', handleDesc: '' }
    }
  },
  methods: {
    showDialog (id, title) {
      this.visible = true
      this.title = title || ''
      this.form = { id, status: 2, handleUser: 'admin', handleDesc: '' }
    },
    submit () {
      handle(this.form).then(res => {
        if (res.successful) {
          this.$message.success('处理成功')
          this.visible = false
          this.$emit('close')
        } else this.$message.error(res.resultHint || '失败')
      })
    }
  }
}
</script>
