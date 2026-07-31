<template>
  <div>
    <el-form ref="form" :inline="true" :model="form">
      <el-form-item label="设备编号：">
        <el-input v-model="form.no" maxlength="20" clearable></el-input>
      </el-form-item>
      <el-form-item label="设备名称：">
        <el-input v-model="form.name" maxlength="20" clearable></el-input>
      </el-form-item>
      <el-form-item label="设备类型：" prop="type">
        <el-select v-model="form.type" clearable>
          <el-option label="请选择" value=""></el-option>
          <el-option label="集中式逆变器" :value="0"></el-option>
          <el-option label="直流汇流箱" :value="1"></el-option>
          <el-option label="直流汇流柜" :value="2"></el-option>
          <el-option label="气象站" :value="3"></el-option>
          <el-option label="电表" :value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="安装日期：">
        <el-date-picker v-model="form.installTime" type="date" value-format="yyyy-MM-dd" placeholder="安装日期"></el-date-picker>
      </el-form-item>
      <el-form-item label="到期日期：">
        <el-date-picker v-model="form.endTime" type="date" value-format="yyyy-MM-dd" placeholder="到期日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getDeviceData(0)" icon="el-icon-search">查询</el-button>
        <el-button type="primary" @click="addDevice()" icon="el-icon-plus">新增设备</el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="factoryData.list" border stripe>
      <el-table-column type="index" label="#" width="50"></el-table-column>
      <el-table-column prop="no" label="设备编号"></el-table-column>
      <el-table-column prop="name" label="设备名称"></el-table-column>
      <el-table-column prop="type" label="设备类型" width="120">
        <template slot-scope="scope">
          <span>{{ typeLabel(scope.row.type) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="deviceAddress" label="设备地址"></el-table-column>
      <el-table-column prop="factoryName" label="设备厂家"></el-table-column>
      <el-table-column prop="specifications" label="规格"></el-table-column>
      <el-table-column prop="model" label="型号"></el-table-column>
      <el-table-column prop="installTime" label="安装日期" width="110"></el-table-column>
      <el-table-column prop="endTime" label="到期日期" width="110"></el-table-column>
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="edit(scope.row.id)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-dropdown trigger="click" size="small">
            <span class="el-dropdown-link">更多<i class="el-icon-arrow-down el-icon--right"></i></span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button type="text" size="small" @click="del(scope.row.id)">删除</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <pager :pageIndex.sync="form.page" :pageSize.sync="form.limit" :totalItemCount="form.total" @change="getDeviceData"></pager>

    <el-dialog
      :title="editBtn ? '编辑设备' : '新增设备'"
      :append-to-body="true"
      :visible.sync="dialogDevice"
      :width="dialogWidth"
      center
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form class="add_form" label-width="30%" ref="addForm" :rules="rules" :model="addData">
        <el-form-item label="设备编号：" prop="no">
          <el-input v-model="addData.no" maxlength="20"></el-input>
        </el-form-item>
        <el-form-item label="设备名称：" prop="name">
          <el-input v-model="addData.name" maxlength="20"></el-input>
        </el-form-item>
        <el-form-item label="设备类型：" prop="type">
          <el-select v-model="addData.type">
            <el-option label="请选择" value=""></el-option>
            <el-option label="集中式逆变器" :value="0"></el-option>
            <el-option label="直流汇流箱" :value="1"></el-option>
            <el-option label="直流汇流柜" :value="2"></el-option>
            <el-option label="气象站" :value="3"></el-option>
            <el-option label="电表" :value="4"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="设备厂家：" prop="factory">
          <el-select v-model="addData.factory">
            <el-option label="请选择" value=""></el-option>
            <el-option v-for="(item, index) in factoryArr" :key="index" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="地址：" prop="deviceAddress">
          <el-input v-model="addData.deviceAddress" maxlength="100"></el-input>
        </el-form-item>
        <el-form-item label="规格：" prop="specifications">
          <el-input v-model="addData.specifications" maxlength="100"></el-input>
        </el-form-item>
        <el-form-item label="型号：" prop="model">
          <el-input v-model="addData.model" maxlength="100"></el-input>
        </el-form-item>
        <el-form-item label="数据采集仪编号：" prop="daiId">
          <el-input v-model="addData.daiId" maxlength="30"></el-input>
        </el-form-item>
        <el-form-item label="安装日期：" prop="installTime">
          <el-date-picker type="date" value-format="yyyy-MM-dd" v-model="addData.installTime" style="width:100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="到期日期：" prop="endTime">
          <el-date-picker type="date" value-format="yyyy-MM-dd" v-model="addData.endTime" style="width:100%"></el-date-picker>
        </el-form-item>
        <el-form-item label="备注：" prop="memo">
          <el-input type="textarea" v-model="addData.memo" maxlength="200"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="add">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import Pager from '@/components/Pager'
import { pageByParam, save, getDevice, delDevice } from '@/api/Device/Device'
import { factoryList } from '@/api/DeviceF/DeviceF'
export default {
  components: { Pager },
  data () {
    return {
      form: { page: 1, limit: 10, total: 0, no: '', name: '', type: '', installTime: '', endTime: '' },
      factoryData: { list: [] },
      addData: {},
      loading: true,
      factoryArr: [],
      dialogDevice: false,
      dialogWidth: '40%',
      editBtn: false,
      rules: {
        name: [{ required: true, message: '请输入设备名称', trigger: 'blur' }],
        no: [{ required: true, message: '请输入设备编号', trigger: 'blur' }]
      }
    }
  },
  mounted () {
    this.getDeviceData(0)
    this.getFactoryList()
  },
  methods: {
    typeLabel (t) {
      return { 0: '集中式逆变器', 1: '直流汇流箱', 2: '直流汇流柜', 3: '气象站', 4: '电表' }[t] ||
        { '0': '集中式逆变器', '1': '直流汇流箱', '2': '直流汇流柜', '3': '气象站', '4': '电表' }[t] || ''
    },
    closeDialog () {
      this.dialogDevice = false
      this.$nextTick(() => { if (this.$refs.addForm) this.$refs.addForm.clearValidate() })
    },
    getFactoryList () {
      factoryList().then(data => {
        if (data.successful) this.factoryArr = data.resultValue || []
        else this.$message.error('获取厂商失败' + (data.resultHint || ''))
      })
    },
    getDeviceData (searchType) {
      if (searchType === 0) this.form.page = 1
      this.loading = true
      pageByParam(this.form).then(data => {
        this.loading = false
        if (data.successful) {
          this.factoryData = data.resultValue || { list: [] }
          this.form.page = Number(data.resultValue.page) || 1
          this.form.limit = Number(data.resultValue.limit) || 10
          this.form.total = Number(data.resultValue.total) || 0
        } else this.$message.error(data.resultHint)
      }).catch(() => { this.loading = false })
    },
    addDevice () {
      this.dialogDevice = true
      this.editBtn = false
      this.addData = {}
      this.$nextTick(() => { if (this.$refs.addForm) this.$refs.addForm.clearValidate() })
    },
    add () {
      this.$refs.addForm.validate(valid => {
        if (!valid) return
        save(this.addData).then(data => {
          if (data.successful) {
            this.$message.success(this.editBtn ? '编辑成功' : '新增成功')
            this.addData = {}
            this.getDeviceData()
            this.closeDialog()
          } else this.$message.error(data.resultHint)
        })
      })
    },
    edit (id) {
      this.dialogDevice = true
      getDevice(id).then(data => {
        if (data.successful) {
          this.addData = data.resultValue || {}
          this.editBtn = true
        } else this.$message.error(data.resultHint)
      })
    },
    del (id) {
      this.$confirm('确认删除吗', '提示', { type: 'warning' }).then(() => {
        delDevice(id).then(data => {
          if (data.successful) {
            this.$message.success('删除成功!')
            this.getDeviceData()
          } else this.$message.error(data.resultHint)
        })
      }).catch(() => {})
    }
  }
}
</script>
<style scoped>
.el-dialog .el-input, .el-dialog .el-select { width: 300px; }
.el-dropdown-link { cursor: pointer; color: #409eff; font-size: 12px; }
</style>
