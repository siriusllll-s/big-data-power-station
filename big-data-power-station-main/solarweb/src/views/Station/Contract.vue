<template>
  <div>
    <el-form ref="form" :inline="true" :model="form">
      <el-form-item label="合同时间：">
        <el-date-picker v-model="form.beginDate" type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="开始时间">
        </el-date-picker>
        -
        <el-date-picker v-model="form.endDate" type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item class="fl_r">
        <el-button type="primary" @click="getContractData(0)" icon="el-icon-search">查询</el-button>
        <el-button type="primary" @click="addContract()" icon="el-icon-plus">新增合同时间</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="contractData.list" border stripe>
      <el-table-column prop="stationObj.name" label="所属电站"> </el-table-column>
      <el-table-column prop="no" label="合同编号"> </el-table-column>
      <el-table-column prop="contractPower" label="合同发电量(kWh)"> </el-table-column>
      <el-table-column prop="beginDate" label="合同开始日期"> </el-table-column>
      <el-table-column prop="endDate" label="合同结束日期"> </el-table-column>
      <el-table-column prop="efficiency" label="模拟发电效率(%)"> </el-table-column>
      <el-table-column prop="protocolPr" label="协议效能比(%)"> </el-table-column>
      <el-table-column prop="avgRadio" label="预计年均辐照值((Wh/㎡)"> </el-table-column>
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="edit(scope.row.id)">编辑</el-button>
          <el-button type="text" size="small" @click="del(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 页尾 -->
    <pager :pageIndex.sync="form.page" :pageSize.sync="form.limit" :totalItemCount="form.total" @change="getContractData"></pager>

    <!-- 新增、修改电价-->
    <el-dialog
      :title="editBtn ? '编辑合同时间' : '新增合同时间'"
      :append-to-body="true"
      :visible.sync="dialogVisible"
      :width="dialogWidth"
      center
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form label-width="30%" ref="addData" :rules="rules" :model="addData">
        <el-form-item label="合同编号：" prop="no">
          <el-input v-model="addData.no" maxlength="20"></el-input>
        </el-form-item>
        <el-form-item label="开始时间：" prop="beginDate">
          <el-date-picker type="date" value-format="yyyy-MM-dd" v-model="addData.beginDate"
          :picker-options="startDatePicker"
          clearable placeholder="请选择开始时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间：" prop="endDate">
          <el-date-picker type="date" value-format="yyyy-MM-dd" v-model="addData.endDate" :picker-options="endDatePicker"
           clearable placeholder="请选择结束时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="合同期电量：" prop="contractPower">
          <el-input v-model="addData.contractPower" maxlength="20" type="number"><template slot="append">kWh</template></el-input>
        </el-form-item>
        <el-form-item label="协议效能比：" prop="protocolPr">
          <el-input v-model="addData.protocolPr" maxlength="20" type="number"><template slot="append">%</template></el-input>
        </el-form-item>
        <el-form-item label="模拟发电效率：" prop="efficiency">
          <el-input v-model="addData.efficiency" maxlength="20" type="number"><template slot="append">%</template></el-input>
        </el-form-item>
        <el-form-item label="预计年均辐照值：" prop="avgRadio">
          <el-input v-model="addData.avgRadio" maxlength="20" type="number"><template slot="append">Wh/㎡</template></el-input>
        </el-form-item>
        <el-form-item label="备注：" prop="memo">
          <el-input type="textarea" v-model="addData.memo"  maxlength="100"></el-input>
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
import Pager from "@/components/Pager";
import {pageByParam,save,del,getDetail} from "@/api/Station/Contract";
export default {
  components: {
    Pager,
  },
  data() {
    return {
      form: {
        no: "",
        beginDate:"",
        endDate:"",
        contractPower:"",
        protocolPr:"",
        efficiency:"",
        avgRadio:"",
        memo:"",
        stationObj:"",
        page: 1,
        limit: 10,
        total: 0,
      
      },
      contractData: {},
      addData: {},
      editBtn: false,
      dialogVisible: false,
      stationId: 1,
      rules: {
        beginDate: [{ required: true, message: "请选择合同开始时间", trigger: "blur" },],
        endDate: [{ required: true, message: "请选择合同结束时间", trigger: "blur" },],
        contractPower: [{ required: true, message: "请输入合同期电量", trigger: "blur" }],
        protocolPr: [{ required: true, message: "请输入协议效能比", trigger: "blur" },],
        efficiency: [{ required: true, message: "请输入模拟发电效率", trigger: "blur" },],
        avgRadio: [{ required: true, message: "请输入预计年均辐照值", trigger: "blur" }],
        
      },
      loading: true,
      dialogWidth: "40%",
      startDatePicker: this.beginDate(),
      endDatePicker: this.processDate()
      
    };
  },
  mounted() {
    this.getContractData();
    window.onresize = () => {
      return (() => {
        this.setDialogWidth();
      })();
    };
  },
  methods: {
    beginDate () {
      const self = this
      return {
        disabledDate (time) {
          if (self.addData.endDate) { // 如果结束时间不为空，则小于结束时间
            return new Date(self.addData.endDate).getTime() < time.getTime()
          } else {
            //return time.getTime() > Date.now()//开始时间不选时，结束时间最大值小于等于当天
          }
        }
      }
    },
    processDate () {
      const self = this
      return {
        disabledDate (time) {
          if (self.addData.beginDate) { // 如果开始时间不为空，则结束时间大于开始时间
            return new Date(self.addData.beginDate).getTime() > time.getTime()
          } else {
            // return time.getTime() > Date.now()// 开始时间不选时，结束时间最大值小于等于当天
          }
        }
      }
    },
            
    
    setDialogWidth() {
      var val = document.body.clientWidth;
      const def = 1200; // 默认宽度
      if (val > def) {
        this.dialogWidth = "40%";
      } else {
        this.dialogWidth = "500px";
      }
    },
    
    
    
    //关闭弹框，取消表单验证
    closeDialog() {
      this.dialogVisible = false;
      this.$nextTick(() => {
        this.$refs.addData.clearValidate()
      })
    },
    
    // 加载列表数据
    getContractData(searchType) {
      if (searchType == 0) {
        this.form.page = 1;
      }
      pageByParam(this.form).then((data) => {
        if (data.successful) {
          this.contractData = data.resultValue;
          if (this.contractData.list.length == 0) {
            this.form.page = 1;
          } else {
            this.form.page = Number(data.resultValue.page);
          }
          this.form.limit = Number(data.resultValue.limit);
          this.form.total = Number(data.resultValue.total);
          this.loading = false;
        } else {
          this.loading = false;
          this.$message.error(data.resultHint);
        }
      });
    },
    
    addContract() {
      this.dialogVisible = true
      this.editBtn = false;
      this.addData = {
        no: "",
        beginDate:"",
        endDate:"",
        contractPower:"",
        protocolPr:"",
        efficiency:"",
        avgRadio:"",
        memo:"",
  
      }
      
      this.$nextTick(() => {
        this.$refs.addData.clearValidate()
      })
    },
    
    
    
    add() {
      this.$refs.addData.validate((valid) => {
        if (valid) {
          this.addData.station = this.stationId;
          save(this.addData).then((data) => {
            if (data.successful) {
              if (this.editBtn) {
                this.$message({
                  message: "编辑成功",
                  type: "success",
                });
              } else {
                this.$message({
                  message: "新增成功",
                  type: "success",
                });
              }
              this.addData = {};
              this.getContractData();
              this.closeDialog();
            } else {
              this.$message.error(data.resultHint);
            }
          });
        }
      });
    },
    // 编辑
    edit(id) {
      
      this.dialogVisible = true
      getDetail(id).then((data) => {
        if (data.successful) {
          this.addData = data.resultValue;
          this.editBtn = true;
        } else {
          this.$message.error(data.resultHint);
        }
      });
    },
    
    
    
    // 删除
    del(id) {
      this.$confirm("确认删除吗", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          del(id).then((data) => {
            if (data.successful) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getContractData();
            } else {
              this.$message.error(data.resultHint);
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    
  },
};
</script>
<style scoped>
.el-dialog .el-input,
.el-dialog .el-select {
  width: 300px;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409eff;
  font-size: 12px;
}
</style>
