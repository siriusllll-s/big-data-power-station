<template>
  <div>
    <el-form ref="searchForm" :inline="true" :model="searchForm">
      <el-form-item label="日期：">
        <el-date-picker
          type="daterange"
          v-model="dates"
          value-format="yyyy-MM-dd"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :clearable="false"
        ></el-date-picker>
      </el-form-item>
      <el-form-item class="fl_r">
        <el-button type="primary" @click="search" icon="el-icon-search">
          查询
        </el-button>
      </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="dataList" border stripe>
      <el-table-column type="index" label="#" width="50"></el-table-column>
      <el-table-column prop="reportDate" label="日报日期" min-width="110"></el-table-column>
      <el-table-column prop="weather" label="天气" min-width="80"></el-table-column>
      <el-table-column prop="kwh" label="当日发电量(KWh)" min-width="130"></el-table-column>
      <el-table-column prop="radiation" label="当日辐照量(kWh/㎡)" min-width="140"></el-table-column>
      <el-table-column prop="powerRatio" label="发电效率(%)" min-width="110"></el-table-column>
      <el-table-column label="操作" align="center" width="140" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="edit(scope.row.id)">编辑</el-button>
          <el-divider direction="vertical"></el-divider>
          <el-button type="text" size="small" @click="detail(scope.row.id)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pager
      :pageIndex.sync="searchForm.page"
      :pageSize.sync="searchForm.limit"
      :totalItemCount="searchForm.total"
      @change="getDataList"
    ></pager>

    <el-dialog
      title="编辑运行日报"
      :append-to-body="true"
      :visible.sync="dialogShow"
      width="35%"
      center
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form label-width="28%" ref="editForm" :model="editForm">
        <el-form-item label="日报日期：" prop="reportDate">
          <el-input v-model="editForm.reportDate" disabled></el-input>
        </el-form-item>
        <el-form-item label="天气：" prop="weather">
          <el-input v-model="editForm.weather"></el-input>
        </el-form-item>
        <el-form-item label="当日发电量：" prop="kwh">
          <el-input v-model="editForm.kwh" disabled>
            <template slot="append">KWh</template>
          </el-input>
        </el-form-item>
        <el-form-item label="当日辐照量：" prop="radiation">
          <el-input v-model="editForm.radiation" disabled>
            <template slot="append">kWh/㎡</template>
          </el-input>
        </el-form-item>
        <el-form-item label="发电效率：" prop="powerRatio">
          <el-input v-model="editForm.powerRatio" disabled>
            <template slot="append">%</template>
          </el-input>
        </el-form-item>
        <el-form-item label="总结：" prop="summary">
          <el-input type="textarea" :rows="3" v-model="editForm.summary"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
      title="运行日报详情"
      :append-to-body="true"
      :visible.sync="dialogDetailShow"
      width="35%"
      center
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form label-width="28%" :model="editForm">
        <el-form-item label="日报日期：">{{ editForm.reportDate }}</el-form-item>
        <el-form-item label="天气：">{{ editForm.weather }}</el-form-item>
        <el-form-item label="当日发电量：">{{ editForm.kwh }} KWh</el-form-item>
        <el-form-item label="当日辐照量：">{{ editForm.radiation }} kWh/㎡</el-form-item>
        <el-form-item label="发电效率：">{{ editForm.powerRatio }} %</el-form-item>
        <el-form-item label="总结：">{{ editForm.summary }}</el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="closeDetailDialog">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import Pager from '@/components/Pager'
import { getDayReportPage, getDayReportDetail, editDayReport } from '@/api/Report/report'
import { getCurrentDate, getCurrentMonthFirstDay } from '@/utils/date'

export default {
  name: 'DayReport',
  components: {
    Pager
  },
  data () {
    return {
      searchForm: {
        page: 1,
        limit: 10,
        total: 0
      },
      loading: false,
      dataList: [],
      dates: [getCurrentMonthFirstDay(), getCurrentDate()],
      dialogShow: false,
      dialogDetailShow: false,
      editForm: {}
    }
  },
  mounted () {
    this.getDataList()
  },
  methods: {
    search () {
      this.searchForm.page = 1
      this.getDataList()
    },
    getDataList () {
      this.loading = true
      this.searchForm.start = ''
      this.searchForm.end = ''
      if (this.dates && this.dates.length > 0) {
        this.searchForm.start = this.dates[0]
        this.searchForm.end = this.dates[1]
      }
      getDayReportPage(this.searchForm).then(data => {
        this.loading = false
        if (data.successful && data.resultValue) {
          this.dataList = data.resultValue.list || []
          this.searchForm.limit = Number(data.resultValue.limit) || this.searchForm.limit
          this.searchForm.total = Number(data.resultValue.total) || 0
          if (data.resultValue.page) {
            this.searchForm.page = Number(data.resultValue.page)
          }
        } else {
          this.$message.error((data && data.resultHint) || '查询失败')
        }
      }).catch(() => {
        this.loading = false
        this.$message.error('查询失败')
      })
    },
    closeDialog () {
      this.dialogShow = false
      this.editForm = {}
    },
    edit (id) {
      this.dialogShow = true
      getDayReportDetail(id).then(data => {
        if (data.successful) {
          this.editForm = data.resultValue || {}
        } else {
          this.$message.error(data.resultHint || '加载失败')
        }
      })
    },
    save () {
      editDayReport(this.editForm).then(data => {
        if (data.successful) {
          this.$message.success('编辑成功')
          this.closeDialog()
          this.getDataList()
        } else {
          this.$message.error(data.resultHint || '保存失败')
        }
      })
    },
    detail (id) {
      this.dialogDetailShow = true
      getDayReportDetail(id).then(data => {
        if (data.successful) {
          this.editForm = data.resultValue || {}
        } else {
          this.$message.error(data.resultHint || '加载失败')
        }
      })
    },
    closeDetailDialog () {
      this.dialogDetailShow = false
      this.editForm = {}
    }
  }
}
</script>

<style scoped>
</style>
