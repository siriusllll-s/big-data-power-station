<template>
  <el-dialog :title="(form.id ? '编辑' : '新增') + '巡检点'" :visible.sync="visible" width="45%" append-to-body :close-on-click-modal="false">
    <el-form ref="form" :model="form" label-width="120px" :rules="rules">
      <el-form-item label="巡检点名称" prop="name">
        <el-input v-model="form.name" maxlength="50"></el-input>
      </el-form-item>
      <el-form-item label="巡检项目" prop="projectId">
        <el-select v-model="form.projectId" style="width:100%" @change="onProject">
          <el-option v-for="p in projects" :key="p.id" :label="p.name" :value="p.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="巡检事项">
        <el-checkbox-group v-model="form.itemIds">
          <div v-for="c in contents" :key="c.contentId" style="margin-bottom:8px">
            <div style="font-weight:600">{{ c.contentName }}</div>
            <el-checkbox v-for="it in c.items" :key="it.id" :label="it.id">{{ it.name }}</el-checkbox>
          </div>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="设备类型">
        <el-select v-model="form.deviceType" style="width:100%" @change="loadDevices">
          <el-option label="集中式逆变器" :value="0"></el-option>
          <el-option label="直流汇流箱" :value="1"></el-option>
          <el-option label="直流汇流柜" :value="2"></el-option>
          <el-option label="气象站" :value="3"></el-option>
          <el-option label="电表" :value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="关联设备">
        <el-select v-model="form.deviceNames" multiple style="width:100%">
          <el-option v-for="d in devices" :key="d.id" :label="d.name" :value="d.name"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="备注">
        <el-input type="textarea" v-model="form.memo"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer">
      <el-button @click="visible=false">取消</el-button>
      <el-button type="primary" @click="submit">确定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { getProjectList, getItemListByProject, getDetail, save } from '@/api/Inspection/InspectionPoint'
import { getDeviceListByType } from '@/api/Device/Device'
export default {
  data () {
    return {
      visible: false,
      form: { id: null, name: '', projectId: null, itemIds: [], deviceType: 0, deviceNames: [], memo: '' },
      projects: [],
      contents: [],
      devices: [],
      rules: {
        name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        projectId: [{ required: true, message: '请选择项目', trigger: 'change' }]
      }
    }
  },
  methods: {
    showDialog (id) {
      this.visible = true
      this.form = { id: null, name: '', projectId: null, itemIds: [], deviceType: 0, deviceNames: [], memo: '' }
      this.contents = []
      getProjectList().then(res => { if (res.successful) this.projects = res.resultValue || [] })
      this.loadDevices()
      if (id) {
        getDetail(id).then(res => {
          if (res.successful && res.resultValue) {
            const v = res.resultValue
            this.form = {
              id: v.id,
              name: v.name,
              projectId: v.projectId || v.project,
              itemIds: v.itemIds || [],
              deviceType: v.deviceType != null ? v.deviceType : 0,
              deviceNames: v.deviceNames || [],
              memo: v.memo
            }
            if (this.form.projectId) this.onProject(this.form.projectId)
            this.loadDevices()
          }
        })
      }
    },
    onProject (pid) {
      getItemListByProject(pid).then(res => {
        if (res.successful) this.contents = res.resultValue || []
      })
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
