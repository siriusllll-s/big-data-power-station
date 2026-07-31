<template>
  <div>
    <el-form ref="form" :inline="true" :model="form">
      <el-form-item label="报警分类：">
        <el-select v-model="form.classification" placeholder="请选择" @change="changeType" clearable>
          <el-option label="电站" value="0"></el-option>
          <el-option label="汇流箱" value="1"></el-option>
          <el-option label="逆变器" value="2"></el-option>
          <el-option label="直流柜" value="3"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="报警类型：">
        <el-select v-model="form.type" placeholder="请选择" clearable>
          <el-option label="效能比" :value="0" v-show="form.classification=='0'"></el-option>
          <el-option label="发电量" :value="1" v-show="form.classification=='0'"></el-option>
          <el-option label="电流/功率" :value="2" v-show="form.classification=='1'"></el-option>
          <el-option label="效率" :value="3" v-show="form.classification=='2'"></el-option>
          <el-option label="电流/功率" :value="4" v-show="form.classification=='3'"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item class="fl_r">
        <el-button type="primary" @click="getThresholdData(0)" icon="el-icon-search">查询</el-button>
        <el-button type="primary" @click="toAddOrEdit()" icon="el-icon-plus">新增阈值设置</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="thresholdData.list" border stripe>
      <el-table-column prop="classification" label="报警分类">
        <template slot-scope="scope">
          <span>{{ {0:'电站',1:'汇流箱',2:'逆变器',3:'直流柜'}[scope.row.classification] || { '0':'电站','1':'汇流箱','2':'逆变器','3':'直流柜' }[scope.row.classification] || '' }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="type" label="报警类型">
        <template slot-scope="scope">
          <span>{{ {0:'效能比',1:'发电量',2:'电流/功率',3:'效率',4:'电流/功率'}[scope.row.type] || { '0':'效能比','1':'发电量','2':'电流/功率','3':'效率','4':'电流/功率' }[scope.row.type] || '' }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="toAddOrEdit(scope.row.id)">编辑</el-button>
          <el-button type="text" size="small" @click="toDetail(scope.row.id)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pager :pageIndex.sync="form.page" :pageSize.sync="form.limit" :totalItemCount="form.total" @change="getThresholdData"></pager>
    <AddThreshold ref="AddThreshold" @close="getThresholdData"></AddThreshold>
    <el-dialog title="阈值详情" :append-to-body="true" :visible.sync="dialogDetail" :width="dialogWidth" center>
      <el-form label-position="left" size="small">
        <el-row>
          <el-col :span="12"><el-form-item label="报警分类：">
            <span v-if="detail.classification==0">电站</span>
            <span v-if="detail.classification==1">汇流箱</span>
            <span v-if="detail.classification==2">逆变器</span>
            <span v-if="detail.classification==3">直流柜</span>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="报警级别：">
            <span v-if="detail.level==0">低</span>
            <span v-if="detail.level==1">中</span>
            <span v-if="detail.level==2">高</span>
          </el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="12"><el-form-item label="报警类型：">
            <span v-if="detail.type==0">效能比</span>
            <span v-if="detail.type==1">发电量</span>
            <span v-if="detail.type==2">电流/功率</span>
            <span v-if="detail.type==3">效率</span>
            <span v-if="detail.type==4">电流/功率</span>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="判断周期：">
            <span v-if="detail.cycle==0">10分钟</span>
            <span v-if="detail.cycle==1">30分钟</span>
            <span v-if="detail.cycle==2">45分钟</span>
            <span v-if="detail.cycle==3">1小时</span>
          </el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="12"><el-form-item label="周期开始时间："><span>{{detail.startTime}}时</span></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="周期结束时间："><span>{{ detail.endTime }}时</span></el-form-item></el-col>
        </el-row>
        <el-row>
          <el-col :span="12"><el-form-item label="是否启用："><span>{{ detail.isEnable ? '禁用' : '启用' }}</span></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="备注："><span>{{ detail.memo }}</span></el-form-item></el-col>
        </el-row>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import Pager from '@/components/Pager'
import AddThreshold from './AddThreshold'
import { pageByParam, getDetail } from '@/api/DataMonitoring/Threshold'
export default {
  components: { Pager, AddThreshold },
  data () {
    return {
      form: { classification: '', type: '', page: 1, limit: 10, total: 0 },
      thresholdData: { list: [] },
      loading: true,
      dialogWidth: '40%',
      dialogDetail: false,
      detail: {}
    }
  },
  mounted () { this.getThresholdData() },
  methods: {
    changeType () { delete this.form.type },
    getThresholdData (searchType) {
      if (searchType === 0) this.form.page = 1
      this.loading = true
      pageByParam(this.form).then(data => {
        this.loading = false
        if (data.successful) {
          this.thresholdData = data.resultValue || { list: [] }
          this.form.page = Number(data.resultValue.page) || 1
          this.form.limit = Number(data.resultValue.limit) || 10
          this.form.total = Number(data.resultValue.total) || 0
        } else this.$message.error(data.resultHint)
      }).catch(() => { this.loading = false })
    },
    toAddOrEdit (id) { this.$refs.AddThreshold.showDialog(id) },
    toDetail (id) {
      this.dialogDetail = true
      getDetail(id).then(data => {
        if (data.successful) this.detail = data.resultValue || {}
        else this.$message.error(data.resultHint)
      })
    }
  }
}
</script>
