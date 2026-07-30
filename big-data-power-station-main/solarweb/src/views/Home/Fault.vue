<template>
  <div class="fault-box">
    <div class="fault-btn" @click="faultPopup()"></div>
    <div class="table-box" v-if="popup">
      <div class="el-dialog__header">
        <span class="el-dialog__title">电站故障列表</span>
        <button type="button" @click="faultPopup()" class="el-dialog__headerbtn">
          <i class="el-icon-close"></i>
        </button>
      </div>
      <div class="el-message-box__content">
        <el-table :data="tableData" border class="faultTable" v-loading="loading" :header-cell-style="{background:'#dbf2fd',color:'#606266'}">
          <el-table-column prop="name" label="电站名称" width="150"></el-table-column>
          <el-table-column prop="deviceName" label="故障设备" width="100"></el-table-column>
          <el-table-column prop="faultTime" label="异常时间" width="180"></el-table-column>
          <el-table-column prop="faultDesc" label="异常描述"></el-table-column>
          <el-table-column prop="faultLevel" width="80" label="异常级别" align="center">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.faultLevel === 0" type="primary" effect="dark">一般</el-tag>
              <el-tag v-if="scope.row.faultLevel === 1" type="warning" effect="dark">较重</el-tag>
              <el-tag v-if="scope.row.faultLevel === 2" type="danger" effect="dark">严重</el-tag>
            </template>
          </el-table-column>
        </el-table>
        <Pager
          :pageIndex.sync="searchForm.page"
          :pageSize.sync="searchForm.limit"
          :totalItemCount="searchForm.total"
          @change="faultList"
        ></Pager>
      </div>
    </div>
  </div>
</template>

<script>
import Pager from "@/components/Pager"
import {getExceptionPage} from '@/api/DataSearch/dataSearch'
import {getCurrentTimeZone,getCurrentTime} from '@/utils/date'
export default {
  name: "fault",
  components: {
    Pager
  },
  data: function() {
    return {
      popup: false,
      tableData: [],
      searchForm:{
        page: 1,
        limit: 5,
        total: 0,
      },
      dates:[getCurrentTimeZone(),getCurrentTime()],
      loading:false,
    };
  },
  mounted() {
    this.faultList();
  },
  methods: {
    faultList() {
      this.searchForm.start = "";
      this.searchForm.end = "";
      if(!!this.dates && this.dates.length > 0){
        this.searchForm.start = this.dates[0];
        this.searchForm.end = this.dates[1];
      }
      getExceptionPage(this.searchForm).then(data => {
        if (data.successful) {
          this.tableData = data.resultValue.list;
          this.searchForm.limit = data.resultValue.limit;
          this.searchForm.total = data.resultValue.total;
          this.loading = false;
        } else {
          this.loading = false;
          this.$message.error(data.resultHint);
        }
      })
    },
    faultPopup: function() {
      this.popup = !this.popup;
    },
    
  }
};
</script>

<style scoped>
.spHeight {
  height: 380px;
}
.fault-box {
  position: fixed;
  bottom: 80px;
  right: 20px;
}
.table-box {
  background: #fff;
  border-radius: 10px;
  width: 846px;
  position: absolute;
  bottom: 0;
  right: 0;
  -webkit-box-shadow: 0px 0px 10px rgba(19, 47, 154, 0.19);
  box-shadow: 0px 0px 10px rgba(19, 47, 154, 0.19);
}
.fault-btn {
  cursor: pointer;
  width: 60px;
  height: 60px;
  background: url(../../assets/images/fault.png) no-repeat;
}


</style>
