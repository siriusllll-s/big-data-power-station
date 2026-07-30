<template>
  <el-dialog
    :title="'编辑电站 - ' + name"
    :visible.sync="dialogVisible"
    :width="dialogWidth"
    center
    :show-close="false"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    append-to-body
  >
    <el-form ref="addData" :rules="rules" :model="form" label-width="120px" v-loading="loading">
      <el-form-item label="电站名称" prop="name">
        <el-input v-model="form.name" maxlength="50"></el-input>
      </el-form-item>
      <el-form-item label="装机容量(MWp)" prop="installCapacity">
        <el-input type="number" v-model="form.installCapacity"></el-input>
      </el-form-item>
      <el-form-item label="并网时间" prop="netTime">
        <el-date-picker type="date" value-format="yyyy-MM-dd" v-model="form.netTime" style="width:100%"></el-date-picker>
      </el-form-item>
      <el-form-item label="建设周期" prop="buildCycle">
        <el-select v-model="form.buildCycle" style="width:100%">
          <el-option v-for="i in 12" :key="i" :label="i+'个月'" :value="i"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="售电方式" prop="saleType">
        <el-select v-model="form.saleType" style="width:100%">
          <el-option label="全额上网" :value="0"></el-option>
          <el-option label="自发自用" :value="1"></el-option>
          <el-option label="余电上网" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="电站状态" prop="status">
        <el-select v-model="form.status" style="width:100%">
          <el-option label="运行中" :value="0"></el-option>
          <el-option label="建设中" :value="1"></el-option>
          <el-option label="未开工" :value="2"></el-option>
          <el-option label="维修中" :value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="电站类型" prop="type">
        <el-select v-model="form.type" style="width:100%">
          <el-option label="分布式电站" :value="0"></el-option>
          <el-option label="地面式电站" :value="1"></el-option>
          <el-option label="大棚电站" :value="2"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="电站业主" prop="own">
        <el-input v-model="form.own" maxlength="50"></el-input>
      </el-form-item>
      <el-form-item label="详细地址" prop="address">
        <el-input type="textarea" v-model="form.address" maxlength="200" :rows="2"></el-input>
      </el-form-item>
      <el-form-item label="电站描述" prop="stationDesc">
        <el-input type="textarea" v-model="form.stationDesc" maxlength="500" :rows="3"></el-input>
      </el-form-item>
      <el-form-item label="经度" prop="lon">
        <el-input type="number" v-model="form.lon" step="0.000001"></el-input>
      </el-form-item>
      <el-form-item label="纬度" prop="lat">
        <el-input type="number" v-model="form.lat" step="0.000001"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { getStationDetail, save as saveStation } from '@/api/Station/Station'
export default {
  data() {
    return {
      dialogVisible: false,
      name: '',
      loading: false,
      form: {},
      dialogWidth: '50%',
      rules: {
        name: [{ required: true, message: '请输入电站名称', trigger: 'blur' }],
        installCapacity: [{ required: true, message: '请输入装机容量', trigger: 'blur' }],
        status: [{ required: true, message: '请选择电站状态', trigger: 'change' }],
        type: [{ required: true, message: '请选择电站类型', trigger: 'change' }],
        lon: [{ required: true, message: '请输入经度', trigger: 'blur' }],
        lat: [{ required: true, message: '请输入纬度', trigger: 'blur' }]
      }
    }
  },
  mounted() {
    this.setDialogWidth()
    window.onresize = () => { this.setDialogWidth() }
  },
  methods: {
    setDialogWidth() {
      const w = document.body.clientWidth
      this.dialogWidth = w > 1200 ? '50%' : '700px'
    },
    showDialog(id, name) {
      this.dialogVisible = true
      this.name = name
      this.loading = true
      getStationDetail().then(res => {
        if (res.successful) {
          this.form = Object.assign({}, res.resultValue)
          this.form.id = this.form.id || id
        } else {
          this.$message.error(res.resultHint || '加载失败')
        }
        this.loading = false
      }).catch(() => { this.loading = false })
    },
    save() {
      this.$refs.addData.validate(valid => {
        if (!valid) return
        saveStation(this.form).then(res => {
          if (res.successful) {
            this.$message.success('保存成功')
            this.dialogVisible = false
            this.$emit('close')
          } else {
            this.$message.error(res.resultHint || '保存失败')
          }
        })
      })
    }
  }
}
</script>
