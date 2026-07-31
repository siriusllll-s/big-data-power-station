<template>
  <div>
    <el-form ref="form" :inline="true" :model="form">
      <el-form-item label="巡检项目：">
        <el-select v-model="form.project" placeholder="请选择" clearable>
          <el-option label="请选择" value=""></el-option>
          <el-option v-for="item in projectArr" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="巡检点名称：">
        <el-input v-model="form.pointName" maxlength="20"></el-input>
      </el-form-item>
      <el-form-item class="fl_r">
        <el-button type="primary" @click="getInspectionPointData(0)" icon="el-icon-search">查询</el-button>
        <el-button type="primary" @click="toAddOrEdit()" icon="el-icon-plus">新增巡检点</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="inspectionPointData.list" border stripe>
      <el-table-column prop="name" label="巡检点名称"></el-table-column>
      <el-table-column prop="projectName" label="所属巡检项目"></el-table-column>
      <el-table-column fixed="right" label="操作" width="180">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="toAddOrEdit(scope.row.id, scope.row.name)">编辑</el-button>
          <el-button type="text" size="small" @click="detail(scope.row.id, scope.row.name)">详情</el-button>
          <el-button type="text" size="small" @click="del(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pager :pageIndex.sync="form.page" :pageSize.sync="form.limit" :totalItemCount="form.total" @change="getInspectionPointData"></pager>
    <AddPoint ref="AddPoint" @close="getInspectionPointData"></AddPoint>
    <PointDetail ref="PointDetail" @close="getInspectionPointData"></PointDetail>
  </div>
</template>
<script>
import Pager from '@/components/Pager'
import AddPoint from './AddPoint'
import PointDetail from './PointDetail'
import { pageByParam, del, getProjectList } from '@/api/Inspection/InspectionPoint'
export default {
  components: { Pager, AddPoint, PointDetail },
  data () {
    return {
      form: { project: '', pointName: '', page: 1, limit: 10, total: 0 },
      inspectionPointData: { list: [] },
      loading: true,
      projectArr: []
    }
  },
  mounted () {
    this.getInspectionPointData()
    this.getProjectList()
  },
  methods: {
    getInspectionPointData (searchType) {
      if (searchType === 0) this.form.page = 1
      this.loading = true
      pageByParam(this.form).then(data => {
        this.loading = false
        if (data.successful) {
          this.inspectionPointData = data.resultValue || { list: [] }
          this.form.page = Number(data.resultValue.page) || 1
          this.form.limit = Number(data.resultValue.limit) || 10
          this.form.total = Number(data.resultValue.total) || 0
        } else {
          this.$message.error(data.resultHint)
        }
      }).catch(() => { this.loading = false })
    },
    toAddOrEdit (id, name) { this.$refs.AddPoint.showDialog(id, name) },
    detail (id, name) { this.$refs.PointDetail.showDialog(id, name) },
    del (id) {
      this.$confirm('确认删除吗', '提示', { type: 'warning' }).then(() => {
        del(id).then(data => {
          if (data.successful) {
            this.$message.success('删除成功!')
            this.getInspectionPointData()
          } else this.$message.error(data.resultHint)
        })
      }).catch(() => {})
    },
    getProjectList () {
      getProjectList().then(data => {
        if (data.successful) this.projectArr = data.resultValue || []
      })
    }
  }
}
</script>
