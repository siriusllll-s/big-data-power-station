<template>
  <el-dialog :title="edit?'编辑阈值设置':'新增阈值设置'" :append-to-body="true"
             :visible.sync="dialogAdd" :width="dialogWidth" center
             :show-close="false" :close-on-click-modal="false" :close-on-press-escape="false">
    <div class="mainInput">
      <el-form ref="addForm" :model="addForm" label-position="right" label-width="160px" :rules="rules">
        <el-col :span="12">
          <el-form-item label="报警分类：" prop="classification">
            <el-select v-model="addForm.classification" placeholder="请选择" @change="changeType">
              <el-option label="电站" :value="0"></el-option>
              <el-option label="汇流箱" :value="1"></el-option>
              <el-option label="逆变器" :value="2"></el-option>
              <el-option label="直流柜" :value="3"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报警级别：" prop="level">
            <el-select v-model="addForm.level" placeholder="请选择">
              <el-option label="低" :value="0"></el-option>
              <el-option label="中" :value="1"></el-option>
              <el-option label="高" :value="2"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报警类型：" prop="type">
            <el-select v-model="addForm.type" placeholder="请选择">
              <el-option label="效能比" :value="0" v-show="addForm.classification==0"></el-option>
              <el-option label="发电量" :value="1" v-show="addForm.classification==0"></el-option>
              <el-option label="电流/功率" :value="2" v-show="addForm.classification==1"></el-option>
              <el-option label="效率" :value="3" v-show="addForm.classification==2"></el-option>
              <el-option label="电流/功率" :value="4" v-show="addForm.classification==3"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="判断周期：" prop="cycle">
            <el-select v-model="addForm.cycle" placeholder="请选择">
              <el-option label="10分钟" :value="0"></el-option>
              <el-option label="30分钟" :value="1"></el-option>
              <el-option label="45分钟" :value="2"></el-option>
              <el-option label="1小时" :value="3"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="周期开始时间：" prop="startTime">
            <el-select v-model="addForm.startTime" placeholder="请选择">
              <el-option v-for="h in 24" :key="'s'+h" :label="(h-1)+'时'" :value="h-1"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="周期结束时间：" prop="endTime">
            <el-select v-model="addForm.endTime" placeholder="请选择">
              <el-option v-for="h in 24" :key="'e'+h" :label="(h-1)+'时'" :value="h-1"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="是否启用：" prop="isEnable">
            <el-select v-model="addForm.isEnable" placeholder="请选择">
              <el-option label="启用" :value="0"></el-option>
              <el-option label="禁用" :value="1"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注：" prop="memo">
            <el-input type="textarea" v-model="addForm.memo"></el-input>
          </el-form-item>
        </el-col>
      </el-form>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog()">取 消</el-button>
      <el-button type="primary" @click="confirm()">确 定</el-button>
    </span>
  </el-dialog>
</template>
<script>
import { getDetail, save } from '@/api/DataMonitoring/Threshold'
export default {
  name: 'AddThreshold',
  data () {
    return {
      edit: false,
      addForm: {},
      dialogAdd: false,
      dialogWidth: '50%',
      rules: {
        classification: [{ required: true, message: '请选择报警分类', trigger: 'change' }],
        cycle: [{ required: true, message: '请选择判断周期', trigger: 'change' }],
        level: [{ required: true, message: '请选择报警级别', trigger: 'change' }],
        startTime: [{ required: true, message: '请选择周期开始时间', trigger: 'change' }],
        endTime: [{ required: true, message: '请选择周期结束时间', trigger: 'change' }],
        type: [{ required: true, message: '请选择报警类型', trigger: 'change' }],
        isEnable: [{ required: true, message: '请选择是否启用', trigger: 'change' }]
      }
    }
  },
  methods: {
    changeType () { delete this.addForm.type },
    showDialog (id) {
      this.dialogAdd = true
      this.addForm = {}
      this.edit = false
      if (id) {
        this.edit = true
        getDetail(id).then(data => {
          if (data.successful) this.addForm = data.resultValue || {}
          else this.$message.error(data.resultHint)
        })
      }
    },
    closeDialog () {
      if (this.$refs.addForm) this.$refs.addForm.resetFields()
      this.dialogAdd = false
      this.edit = false
      this.addForm = {}
      this.$emit('close')
    },
    confirm () {
      this.$refs.addForm.validate(valid => {
        if (!valid) return
        save(this.addForm).then(data => {
          if (data.successful) {
            this.$message.success(this.edit ? '编辑成功' : '新增成功')
            this.closeDialog()
          } else this.$message.error(data.resultHint)
        })
      })
    }
  }
}
</script>
<style scoped>
.mainInput .el-input { width: 70%; }
</style>
