<template>
  <div>
    <el-form ref="form" :inline="true" :model="form">
      <el-form-item label="实施日期：">
        <el-date-picker v-model="form.start" type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="起始时间">
        </el-date-picker>
        -
        <el-date-picker v-model="form.end" type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item class="fl_r">
        <el-button type="primary" @click="getPriceData(0)" icon="el-icon-search">查询</el-button>
        <el-button type="primary" @click="addPrice()" icon="el-icon-plus">新增电价</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="priceData.list" border stripe>
      <el-table-column prop="stationObj.name" label="所属电站"> </el-table-column>
      <el-table-column prop="price" label="电价(元)"> </el-table-column>
      <el-table-column prop="beginDate" label="实施日期"> </el-table-column>
      
      <el-table-column fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="edit(scope.row.id)" v-authorize="'POST:/stationSolarPrice/save'">编辑</el-button>
          <el-button type="text" size="small" @click="del(scope.row.id)" v-authorize="'GET:/stationSolarPrice/delete'">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 页尾 -->
    <pager :pageIndex.sync="form.page" :pageSize.sync="form.limit" :totalItemCount="form.total" @change="getPriceData"></pager>

    <!-- 新增、修改电价-->
    <el-dialog
      :title="editBtn ? '编辑电价' : '新增电价'"
      :append-to-body="true"
      :visible.sync="dialogVisible"
      :width="dialogWidth"
      center
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <el-form label-width="30%" ref="addData" :rules="rules" :model="addData">
        <el-form-item label="所属电站：" prop="name">
          <el-input v-model="addData.name" maxlength="20" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="电价设置：" prop="price">
          <el-input type="number" v-model="addData.price" maxlength="20"><template slot="append">元</template></el-input>
        </el-form-item>
        <el-form-item label="实施日期：" prop="beginDate">
          <el-date-picker type="date" value-format="yyyy-MM-dd" v-model="addData.beginDate" style="width: 100%" :picker-options="pickerOptions"></el-date-picker>
        </el-form-item>
        <el-form-item label="备注：" prop="memo">
          <el-input type="textarea" v-model="addData.memo" ></el-input>
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
import {pageByParam,save,delPrice,getPriceDetail} from "@/api/Price/Price";
import { getStationDetail } from "@/api/Station/Station";
export default {
  components: {
    Pager,
  },
  data() {
    return {
      form: {
        name: "",
        price:"",
        beginDate:"",
        memo:"",
        stationObj:"",
        page: 1,
        limit: 10,
        total: 0,
      
      },
      priceData: {},
      addData: {},
      editBtn: false,
      dialogVisible: false,
      stationId:1,
      rules: {
        name: [
          { required: true, message: "请输入所属电站名称", trigger: "blur" },
        ],
        price: [
          { required: true, message: "请输入电价设置", trigger: "blur" },
        ],
        beginDate: [{ required: true, message: "请选择实施日期", trigger: "blur" }],
        
      },
      loading: true,
      dialogWidth: "40%",
      pickerOptions:{
        disabledDate (time)  {
          return time < Date.now() - 24 * 60 * 60 * 1000;
        }
      }
    };
  },
  mounted() {
    this.getPriceData();
    window.onresize = () => {
      return (() => {
        this.setDialogWidth();
      })();
    };
  },
  methods: {
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
    getPriceData(searchType) {
      if (searchType == 0) {
        this.form.page = 1;
      }
      pageByParam(this.form).then((data) => {
        if (data.successful) {
          this.priceData = data.resultValue;
          if (this.priceData.list.length == 0) {
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
    
    addPrice() {
      this.dialogVisible = true
      this.editBtn = false;
      this.addData = {
        name: '',
        price: '',
        beginDate: '',
        memo:'',
  
      }
      
      getStationDetail().then(res => {
        if (res.successful) {
          this.addData.name= res.resultValue.name
        }
  
      })
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
              this.getPriceData();
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
      getPriceDetail(id).then((data) => {
        if (data.successful) {
          this.addData = data.resultValue;
          this.addData.name= this.addData.stationObj.name
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
          delPrice(id).then((data) => {
            if (data.successful) {
              this.$message({
                type: "success",
                message: "删除成功!",
              });
              this.getPriceData();
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
