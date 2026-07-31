<template>
  <div>
    <el-form ref="form" :inline="true" :model="form">
      <el-form-item label="日期范围：">
        <el-date-picker v-model="form.beginDate" type="date" value-format="yyyy-MM-dd" placeholder="起始时间"></el-date-picker>
        -
        <el-date-picker v-model="form.endDate" type="date" value-format="yyyy-MM-dd" placeholder="结束时间"></el-date-picker>
      </el-form-item>
      <el-form-item label="工单状态：">
        <el-select v-model="form.status" clearable>
          <el-option label="请选择" value=""></el-option>
          <el-option label="新建" :value="1"></el-option>
          <el-option label="处理中" :value="2"></el-option>
          <el-option label="已解决" :value="3"></el-option>
          <el-option label="关闭" :value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="工单来源：">
        <el-select v-model="form.type" clearable>
          <el-option label="请选择" value=""></el-option>
          <el-option label="自动生成" :value="0"></el-option>
          <el-option label="手动生成" :value="1"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="设备类型：">
        <el-select v-model="form.deviceType" clearable @change="typeChange">
          <el-option label="请选择" value=""></el-option>
          <el-option label="集中式逆变器" :value="0"></el-option>
          <el-option label="直流汇流箱" :value="1"></el-option>
          <el-option label="直流汇流柜" :value="2"></el-option>
          <el-option label="气象站" :value="3"></el-option>
          <el-option label="电表" :value="4"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="故障设备：">
        <el-select v-model="form.deviceName" clearable placeholder="请选择">
          <el-option v-for="item in deviceArr" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="处理人员：">
        <el-input v-model="form.userName" maxlength="20"></el-input>
      </el-form-item>
      <el-form-item class="fl_r">
        <el-button type="primary" @click="getWorkOrderData(0)" icon="el-icon-search">查询</el-button>
        <el-button type="primary" @click="toAddOrEdit()" icon="el-icon-plus">新增故障工单</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="workOrderData.list" border stripe>
      <el-table-column type="index" label="#" width="50"></el-table-column>
      <el-table-column prop="title" label="工单标题"></el-table-column>
      <el-table-column prop="deviceType" label="设备类型">
        <template slot-scope="scope">
          {{ deviceTypeLabel(scope.row.deviceType) }}
        </template>
      </el-table-column>
      <el-table-column prop="deviceNames" label="故障设备">
        <template slot-scope="scope">{{ (scope.row.deviceNames || []).join(',') }}</template>
      </el-table-column>
      <el-table-column prop="exceptionTime" label="故障开始时间" min-width="150"></el-table-column>
      <el-table-column prop="forecastTime" label="预计恢复时间" min-width="150"></el-table-column>
      <el-table-column prop="status" label="工单状态" width="90">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" type="primary" effect="plain">新建</el-tag>
          <el-tag v-if="scope.row.status === 2" type="danger" effect="plain">处理中</el-tag>
          <el-tag v-if="scope.row.status === 3" type="warning" effect="plain">已解决</el-tag>
          <el-tag v-if="scope.row.status === 4" type="info" effect="plain">关闭</el-tag>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="160">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="detail(scope.row.id)">详情</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-dropdown trigger="click" size="small">
            <span class="el-dropdown-link">更多<i class="el-icon-arrow-down el-icon--right"></i></span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button type="text" size="small" @click="toAddOrEdit(scope.row.id, scope.row.title)">编辑</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button type="text" size="small" @click="del(scope.row.id)">删除</el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button v-if="scope.row.status != 4" type="text" size="small" @click="handle(scope.row.id, scope.row.title)">处理</el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <pager :pageIndex.sync="form.page" :pageSize.sync="form.limit" :totalItemCount="form.total" @change="getWorkOrderData"></pager>
    <AddOrder ref="AddOrder" @close="getWorkOrderData"></AddOrder>
    <OrderDetail ref="OrderDetail" @close="getWorkOrderData"></OrderDetail>
    <OrderHandle ref="OrderHandle" @close="getWorkOrderData"></OrderHandle>
  </div>
</template>
<script>
import Pager from '@/components/Pager'
import AddOrder from './AddOrder'
import OrderDetail from './OrderDetail'
import OrderHandle from './OrderHandle'
import { pageByParam, del } from '@/api/Inspection/Inspection'
import { getDeviceListByType } from '@/api/Device/Device'
export default {
  components: { Pager, AddOrder, OrderDetail, OrderHandle },
  data () {
    return {
      form: {
        beginDate: '', endDate: '', status: '', type: '', deviceType: '',
        deviceName: '', userName: '', page: 1, limit: 10, total: 0
      },
      workOrderData: { list: [] },
      loading: true,
      deviceArr: []
    }
  },
  mounted () { this.getWorkOrderData() },
  methods: {
    deviceTypeLabel (t) {
      const m = { 0: '集中式逆变器', 1: '直流汇流箱', 2: '直流汇流柜', 3: '气象站', 4: '电表' }
      return m[t] != null ? m[t] : (m[String(t)] || '')
    },
    getWorkOrderData (searchType) {
      if (searchType === 0) this.form.page = 1
      this.loading = true
      pageByParam(this.form).then(data => {
        this.loading = false
        if (data.successful) {
          this.workOrderData = data.resultValue || { list: [] }
          this.form.page = Number(data.resultValue.page) || 1
          this.form.limit = Number(data.resultValue.limit) || 10
          this.form.total = Number(data.resultValue.total) || 0
        } else this.$message.error(data.resultHint)
      }).catch(() => { this.loading = false })
    },
    toAddOrEdit (id, title) { this.$refs.AddOrder.showDialog(id, title) },
    detail (id) { this.$refs.OrderDetail.showDialog(id) },
    handle (id, title) { this.$refs.OrderHandle.showDialog(id, title) },
    del (id) {
      this.$confirm('确认删除吗', '提示', { type: 'warning' }).then(() => {
        del(id).then(data => {
          if (data.successful) {
            this.$message.success('删除成功!')
            this.getWorkOrderData()
          } else this.$message.error(data.resultHint)
        })
      }).catch(() => {})
    },
    typeChange (value) {
      this.form.deviceName = ''
      this.deviceArr = []
      if (value === '' || value == null) return
      getDeviceListByType(value).then(data => {
        if (data.successful) this.deviceArr = data.resultValue || []
        else this.$message.error(data.resultHint)
      })
    }
  }
}
</script>
<style scoped>
.el-dropdown-link { cursor: pointer; color: #409eff; font-size: 12px; }
</style>
