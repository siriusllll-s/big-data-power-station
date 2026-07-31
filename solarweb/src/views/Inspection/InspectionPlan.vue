<template>
  <div>
    <el-form ref="form" :inline="true" :model="form">
      <el-form-item label="巡检计划名称：">
        <el-input v-model="form.planName" maxlength="20"></el-input>
      </el-form-item>
      <el-form-item label="日期范围：">
        <el-date-picker v-model="form.beginDate" type="date" value-format="yyyy-MM-dd" placeholder="起始时间"></el-date-picker>
        -
        <el-date-picker v-model="form.endDate" type="date" value-format="yyyy-MM-dd" placeholder="结束时间"></el-date-picker>
      </el-form-item>
      <el-form-item class="fl_r">
        <el-button type="primary" @click="getInspectionPlanData(0)" icon="el-icon-search">查询</el-button>
        <el-button type="primary" @click="toAddOrEdit()" icon="el-icon-plus">新增巡检计划</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="inspectionPlanData.list" border stripe>
      <el-table-column prop="name" label="巡检计划名称"></el-table-column>
      <el-table-column prop="beginDate" label="巡检开始日期"></el-table-column>
      <el-table-column prop="endDate" label="巡检结束日期"></el-table-column>
      <el-table-column fixed="right" label="操作" width="180">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="toAddOrEdit(scope.row.id)">编辑</el-button>
          <el-button type="text" size="small" @click="detail(scope.row.id, scope.row.name)">详情</el-button>
          <el-button type="text" size="small" @click="del(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pager :pageIndex.sync="form.page" :pageSize.sync="form.limit" :totalItemCount="form.total" @change="getInspectionPlanData"></pager>
    <AddPlan ref="AddPlan" @close="getInspectionPlanData"></AddPlan>
    <PlanDetail ref="PlanDetail" @close="getInspectionPlanData"></PlanDetail>
  </div>
</template>
<script>
import Pager from '@/components/Pager'
import AddPlan from './AddPlan'
import PlanDetail from './PlanDetail'
import { pageByParam, del } from '@/api/Inspection/InspectionPlan'
export default {
  components: { Pager, AddPlan, PlanDetail },
  data () {
    return {
      form: { planName: '', beginDate: '', endDate: '', page: 1, limit: 10, total: 0 },
      inspectionPlanData: { list: [] },
      loading: true
    }
  },
  mounted () { this.getInspectionPlanData() },
  methods: {
    getInspectionPlanData (searchType) {
      if (searchType === 0) this.form.page = 1
      this.loading = true
      pageByParam(this.form).then(data => {
        this.loading = false
        if (data.successful) {
          this.inspectionPlanData = data.resultValue || { list: [] }
          this.form.page = Number(data.resultValue.page) || 1
          this.form.limit = Number(data.resultValue.limit) || 10
          this.form.total = Number(data.resultValue.total) || 0
        } else this.$message.error(data.resultHint)
      }).catch(() => { this.loading = false })
    },
    toAddOrEdit (id) { this.$refs.AddPlan.showDialog(id) },
    detail (id, name) { this.$refs.PlanDetail.showDialog(id, name) },
    del (id) {
      this.$confirm('确认删除吗', '提示', { type: 'warning' }).then(() => {
        del(id).then(data => {
          if (data.successful) {
            this.$message.success('删除成功!')
            this.getInspectionPlanData()
          } else this.$message.error(data.resultHint)
        })
      }).catch(() => {})
    }
  }
}
</script>
