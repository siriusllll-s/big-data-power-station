<template>
  <div>
    <el-form ref="form" :inline="true" :model="form">
      <el-form-item label="厂商名称：">
        <el-input v-model="form.name" maxlength="20" clearable></el-input>
      </el-form-item>
      <el-form-item label="联系人：">
        <el-input v-model="form.person" maxlength="20" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="getFactoryData(0)" icon="el-icon-search">查询</el-button>
        <el-button type="primary" @click="addFactory()" icon="el-icon-plus">新增设备厂商</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="factoryData.list" border stripe>
      <el-table-column type="index" label="#" width="50"></el-table-column>
      <el-table-column prop="name" label="厂商名称"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="person" label="联系人"></el-table-column>
      <el-table-column prop="personTel" label="联系人电话"></el-table-column>
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
    <pager :pageIndex.sync="form.page" :pageSize.sync="form.limit" :totalItemCount="form.total" @change="getFactoryData"></pager>

    <el-dialog
      :title="editBtn ? '编辑设备厂商' : '新增设备厂商'"
      :append-to-body="true"
      :visible.sync="dialogFactory"
      :width="dialogWidth"
      center
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form label-width="30%" ref="addForm" :rules="rules" :model="addData">
        <el-form-item label="厂商名称：" prop="name">
          <el-input v-model="addData.name" maxlength="50"></el-input>
        </el-form-item>
        <el-form-item label="地址：" prop="address">
          <el-input v-model="addData.address" maxlength="100"></el-input>
        </el-form-item>
        <el-form-item label="联系人：" prop="person">
          <el-input v-model="addData.person" maxlength="20"></el-input>
        </el-form-item>
        <el-form-item label="联系电话：" prop="personTel">
          <el-input v-model="addData.personTel" maxlength="20"></el-input>
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
import { pageByParam, save, getFactory, delFactory } from '@/api/DeviceF/DeviceF'
export default {
  components: { Pager },
  data () {
    return {
      form: { page: 1, limit: 10, total: 0, name: '', person: '' },
      factoryData: { list: [] },
      addData: {},
      loading: true,
      dialogFactory: false,
      dialogWidth: '40%',
      editBtn: false,
      rules: {
        name: [{ required: true, message: '请输入厂商名称', trigger: 'blur' }]
      }
    }
  },
  mounted () { this.getFactoryData(0) },
  methods: {
    closeDialog () {
      this.dialogFactory = false
      this.$nextTick(() => { if (this.$refs.addForm) this.$refs.addForm.clearValidate() })
    },
    getFactoryData (searchType) {
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
    addFactory () {
      this.dialogFactory = true
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
            this.getFactoryData()
            this.closeDialog()
          } else this.$message.error(data.resultHint)
        })
      })
    },
    edit (id) {
      this.dialogFactory = true
      getFactory(id).then(data => {
        if (data.successful) {
          this.addData = data.resultValue || {}
          this.editBtn = true
        } else this.$message.error(data.resultHint)
      })
    },
    del (id) {
      this.$confirm('确认删除吗', '提示', { type: 'warning' }).then(() => {
        delFactory(id).then(data => {
          if (data.successful) {
            this.$message.success('删除成功!')
            this.getFactoryData()
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
