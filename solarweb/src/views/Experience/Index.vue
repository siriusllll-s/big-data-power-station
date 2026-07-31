<template>
  <div>
    <el-form :inline="true">
      <el-form-item label="关键词"><el-input v-model="form.keyword" clearable /></el-form-item>
      <el-button type="primary" @click="search" icon="el-icon-search">查询</el-button>
      <el-button type="primary" @click="openEdit()" icon="el-icon-plus">新增</el-button>
    </el-form>
    <el-table v-loading="loading" :data="list" border stripe>
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="deviceType" label="设备类型" width="120" />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="140">
        <template slot-scope="s">
          <el-button type="text" @click="openEdit(s.row)">编辑</el-button>
          <el-button type="text" @click="remove(s.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pager :pageIndex.sync="form.page" :pageSize.sync="form.limit" :totalItemCount="form.total" @change="load" />
    <el-dialog :title="edit.id?'编辑经验':'新增经验'" :visible.sync="visible" width="40%" append-to-body>
      <el-form :model="edit" label-width="90px">
        <el-form-item label="标题"><el-input v-model="edit.title" /></el-form-item>
        <el-form-item label="设备类型"><el-input v-model="edit.deviceType" /></el-form-item>
        <el-form-item label="内容"><el-input type="textarea" :rows="5" v-model="edit.content" /></el-form-item>
      </el-form>
      <span slot="footer">
        <el-button @click="visible=false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import Pager from '@/components/Pager'
import { pageExperience, saveExperience, delExperience } from '@/api/Experience/Experience'
export default {
  name: 'Experience',
  components: { Pager },
  data () {
    return {
      form: { keyword: '', page: 1, limit: 10, total: 0 },
      list: [], loading: false, visible: false,
      edit: { id: null, title: '', deviceType: '', content: '' }
    }
  },
  mounted () { this.load() },
  methods: {
    search () { this.form.page = 1; this.load() },
    load () {
      this.loading = true
      pageExperience(this.form).then(res => {
        this.loading = false
        if (res.successful && res.resultValue) {
          this.list = res.resultValue.list || []
          this.form.total = Number(res.resultValue.total) || 0
        } else this.$message.error((res && res.resultHint) || '失败')
      }).catch(() => { this.loading = false })
    },
    openEdit (row) {
      this.edit = row ? Object.assign({}, row) : { id: null, title: '', deviceType: '', content: '' }
      this.visible = true
    },
    save () {
      saveExperience(this.edit).then(res => {
        if (res.successful) {
          this.$message.success('保存成功')
          this.visible = false
          this.load()
        } else this.$message.error(res.resultHint || '失败')
      })
    },
    remove (id) {
      this.$confirm('确认删除?', '提示').then(() => {
        delExperience(id).then(res => {
          if (res.successful) { this.$message.success('已删除'); this.load() }
          else this.$message.error(res.resultHint)
        })
      }).catch(() => {})
    }
  }
}
</script>
