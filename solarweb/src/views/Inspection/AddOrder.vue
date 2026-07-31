<template>
  <el-dialog :title="(form.id ? '编辑' : '新增') + '故障工单'" :visible.sync="visible" width="45%" append-to-body :close-on-click-modal="false">
    <el-form ref="form" :model="form" label-width="120px" :rules="rules">
      <el-form-item label="工单标题" prop="title"><el-input v-model="form.title"></el-input></el-form-item>
      <el-form-item label="工单来源">
        <el-select v-model="form.type" style="width:100%">
          <el-option label="自动生成" :value="0"></el-option>
          <el-option label="手动生成" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="设备类型" prop="deviceType">
        <el-select v-model="form.deviceType" style="width:100%" @change="loadDevices">
          <el-option label="集中式逆变器" :value="0"></el-option>
          <el-option label="直流汇流箱" :value="1"></el-option>
          <el-option label="直流汇流柜" :value="2"></el-option>
          <el-option label="气象站" :value="3"></el-option>
          <el-option label="电表" :value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="故障设备">
        <el-select v-model="form.deviceNames" multiple style="width:100%">
          <el-option v-for="d in devices" :key="d.id" :label="d.name" :value="d.name"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="故障时间">
        <el-date-picker type="datetime" value-format="yyyy-MM-dd HH:mm:ss" v-model="form.exceptionTime" style="width:100%"></el-date-picker>
      </el-form-item>
      <el-form-item label="预计恢复">
        <el-date-picker type="datetime" value-format="yyyy-MM-dd HH:mm:ss" v-model="form.forecastTime" style="width:100%"></el-date-picker>
      </el-form-item>
      <el-form-item label="处理人">
        <el-input v-model="form.userName"></el-input>
      </el-form-item>
      <el-form-item label="描述">
        <el-input type="textarea" v-model="form.description"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="visible=false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { getDetail, save } from '@/api/Inspection/Inspection'
import { getDeviceListByType } from '@/api/Device/Device'
export default {
  data () {
    return {
      visible: false,
      devices: [],
      form: {
        id: null, title: '', type: 1, deviceType: 0, deviceNames: [],
        exceptionTime: '', forecastTime: '', userName: 'admin', description: ''
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }]
      }
    }
  },
  methods: {
    showDialog (id) {
      this.visible = true
      this.form = {
        id: null, title: '', type: 1, deviceType: 0, deviceNames: [],
        exceptionTime: '', forecastTime: '', userName: 'admin', description: ''
      }
      this.loadDevices()
      if (id) {
        getDetail(id).then(res => {
          if (res.successful && res.resultValue) {
            const v = res.resultValue
            this.form = {
              id: v.id,
              title: v.title,
              type: v.type,
              deviceType: v.deviceType,
              deviceNames: v.deviceNames || [],
              exceptionTime: v.exceptionTime,
              forecastTime: v.forecastTime,
              userName: (v.userNames && v.userNames[0]) || 'admin',
              description: v.description
            }
            this.loadDevices()
          }
        })
      }
    },
    loadDevices () {
      if (this.form.deviceType == null) return
      getDeviceListByType(this.form.deviceType).then(res => {
        if (res.successful) this.devices = res.resultValue || []
      })
    },
    submit () {
      this.$refs.form.validate(ok => {
        if (!ok) return
        const payload = Object.assign({}, this.form, {
          userNames: this.form.userName ? [this.form.userName] : []
        })
        save(payload).then(res => {
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
