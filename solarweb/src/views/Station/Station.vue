<template>
  <div>
    <el-form v-if="detail" v-loading="loading">
      <el-row>
        <el-col :span="24">
          <div class="div1" >
            
              <el-row>
                <el-col :span="1"><img src="../../assets/images/stationdetail/installcapacity.png"></el-col>
                <el-col :span="3"><h2 class="font-bold title-font" v-if="detail.installCapacity">{{detail.installCapacity}}<label>MWp</label></h2><span class="title-span"> 装机容量 </span></el-col>
                
                <el-col :span="1"><img src="../../assets/images/stationdetail/stationstatus.png"></el-col>
                <el-col :span="3">
                  <h2 class="font-bold title-font" v-if="detail.status === 0">运行中</h2>
                  <h2 class="font-bold title-font" v-if="detail.status === 1">建设中</h2>
                  <h2 class="font-bold title-font" v-if="detail.status === 2">未开工</h2>
                  <h2 class="font-bold title-font" v-if="detail.status === 3">维修中</h2>
                  <span class="title-span"> 电站状态 </span>
                </el-col>
                
                <el-col :span="1"><img src="../../assets/images/stationdetail/stationtype.png"></el-col>
                <el-col :span="3">
                  <h2 class="font-bold title-font" v-if="detail.type === 0">分布式电站</h2>
                  <h2 class="font-bold title-font" v-if="detail.type === 1">地面式电站</h2>
                  <h2 class="font-bold title-font" v-if="detail.type === 2">大棚电站</h2>
                  <span class="title-span"> 电站类型 </span>
                </el-col>
                
                <el-col :span="1"><img src="../../assets/images/stationdetail/stationnettime.png"></el-col>
                <el-col :span="3"><h2 class="font-bold title-font" v-if="detail.netTime">{{detail.netTime}}</h2><span class="title-span"> 并网时间 </span></el-col>
                
                <el-col :span="1"><img src="../../assets/images/stationdetail/stationratio.png"></el-col>
                <el-col :span="3"><h2 class="font-bold title-font" v-if="detail.efficiency != null">{{detail.efficiency}}<label>%</label></h2><span class="title-span"> 模拟发电效率 </span></el-col>
                
                <el-col :span="1"><img src="../../assets/images/stationdetail/protocolpr.png"></el-col>
                <el-col :span="3"><h2 class="font-bold title-font" v-if="detail.protocolPr != null">{{detail.protocolPr}}<label>%</label></h2><span class="title-span"> 协议效能比 </span></el-col>
                
              </el-row>
           
            
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <div class="div2-left" >
            <img :src="detail.photoPath" style="width:100%;height:320px;">
          </div>
        </el-col>
        <el-col :span="12">
          <div class="div2-right" >
            <el-row>
              <el-col :span="18"><h2 class="ps-name" v-if="detail.name">{{detail.name}}</h2></el-col>
              <el-col :span="6">
                <el-button type="primary" size="small" @click="upload(detail.id,detail.name)">上传图片</el-button>
                <el-button type="primary" size="small" @click="edit(detail.id,detail.name)">编辑</el-button>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="24">
                <div class="ps-desc" >
                  {{detail.stationDesc}}
                </div>  
              </el-col>
            </el-row>  
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <div class="div3">
            <img src="../../assets/images/stationdetail/icon_location.png">
            <span class="address-short"  v-if="detail.province">{{detail.province.province}}{{detail.city.city}}{{detail.area.area}}</span>
            <span class="address-detail">{{detail.address}}</span>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <div class="div4-left">
            <el-row>
              <el-col :span="2"><img src="../../assets/images/stationdetail/maintain.png"></el-col>
              <el-col :span="6">
                <h2 class="font-bold title-font" v-if="detail.maintainPersonObj">{{detail.maintainPersonObj.name}}</h2>
                <span class="title-span"> 运维负责人 </span>
              </el-col>
              <el-col :span="2"><img src="../../assets/images/stationdetail/stationdirector.png"></el-col>
              <el-col :span="6"><h2 class="font-bold title-font" v-if="detail.stationPersonObj">{{detail.stationPersonObj.name}}</h2><span class="title-span"> 电站负责人 </span></el-col>
              <el-col :span="2"><img src="../../assets/images/stationdetail/saletype.png"></el-col>
              <el-col :span="6">
                <h2 class="font-bold title-font" v-if="detail.saleType === 0">全额上网</h2>
                <h2 class="font-bold title-font" v-if="detail.saleType === 1">自发自用</h2>
                <h2 class="font-bold title-font" v-if="detail.saleType === 2">余电上网</h2>
                <span class="title-span"> 售电方式 </span>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="2"><img src="../../assets/images/stationdetail/buildcycle.png"></el-col>
              <el-col :span="6">
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===1">1个月</h2>
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===2">2个月</h2>
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===3">3个月</h2>
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===4">4个月</h2>
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===5">5个月</h2>
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===6">6个月</h2>
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===7">71个月</h2>
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===8">8个月</h2>
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===9">9个月</h2>
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===10">10个月</h2>
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===11">11个月</h2>
                <h2 class="font-bold title-font" v-if="detail.buildCycle ===12">12个月</h2>
                <span class="title-span"> 建设周期 </span>
              </el-col>
              <el-col :span="2"><img src="../../assets/images/stationdetail/stationown.png"></el-col>
              <el-col :span="6"><h2 class="font-bold title-font" v-if="detail.own">{{detail.own}}</h2><span class="title-span"> 电站业主 </span></el-col>
              <el-col :span="2"><img src="../../assets/images/stationdetail/contracttime.png"></el-col>
              <el-col :span="6"><h2 class="font-bold title-font" v-if="detail.contractTime">{{ formatDate(detail.contractTime) }}</h2><span class="title-span"> 合同时间 </span></el-col>
            </el-row>
            <el-row>
              <el-col :span="2"><img src="../../assets/images/stationdetail/avgratio.png"></el-col>
              <el-col :span="6"><h2 class="font-bold title-font" v-if="detail.avgRadio != null">{{detail.avgRadio}}<span>Wh/㎡</span></h2><span class="title-span"> 年均辐照量 </span></el-col>
              <el-col :span="2"><img src="../../assets/images/stationdetail/contactpower.png"></el-col>
              <el-col :span="6"><h2 class="font-bold title-font" v-if="detail.contractPower != null">{{detail.contractPower}}<span>kWh</span></h2><span class="title-span"> 合同发电量 </span></el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="div4-right" id="container" style="height: 300px;" v-loading="loading">
          </div>
        </el-col>
      </el-row>
    </el-form>
    <EditStation ref="EditStation" @close="getDetail"></EditStation>
    <UploadImage ref="UploadImage" @close="getDetail"></UploadImage>
    
  </div>
</template>

<script>
  import { getStationDetail} from "@/api/Station/Station";
  import EditStation from "./EditStation"
  import UploadImage from "./UploadImage"
  const AMap = window.AMap;
  export default{
    
    data() {
      return {
        map: null,
        marker: null,
        detail: {},
        id: '',
        dialogWidth: "60%",
        loading: true,

      }
    },
    
    mounted() {
      this.getDetail();
    },
    components: {
      EditStation,
      UploadImage
    },
    
    methods:{
      
      //电站详情
      getDetail() {
        getStationDetail().then((data) => {
          if (data.successful) {
            this.detail = data.resultValue || {};
            if (this.detail.photoPath) {
              const base = (process.env.NODE_ENV === "development" ? "/api" : (process.env.VUE_APP_BASE_API || ""));
              // 已是完整 URL 则不重复拼接
              if (!/^https?:\/\//.test(this.detail.photoPath) && this.detail.photoPath.indexOf('/minio/') !== 0) {
                this.detail.photoPath = base + "/minio/preViewPicture/" + this.detail.photoPath;
              }
            } else {
              this.detail.photoPath = '';
            }
            this.initMap(this.detail.lon, this.detail.lat);
          } else {
            this.$message.error("数据获取失败");
          }
          this.loading = false;
        }).catch(() => { this.loading = false; });
      },
      
      // 修改电站信息
      edit(id,name) {
        this.$refs.EditStation.showDialog(id,name);
      },
      
      //上传图片
      upload(id,name) {
        this.$refs.UploadImage.showDialog(id,name);
      },
      
      //地图
      initMap(lon, lat) {
        if (lon == null || lat == null || typeof AMap === 'undefined') {
          this.loading = false
          return
        }
        this.map = new AMap.Map('container', {
          resizeEnable: true,
          center: [lon, lat],
          zoom: 10
        })
        this.marker = new AMap.Marker({
          position: [lon, lat],
          map: this.map
        })
        this.loading = false
      },

      formatDate(val) {
        if (!val) return ''
        if (typeof val === 'string') return val.length >= 10 ? val.substr(0, 10) : val
        try {
          const d = new Date(val)
          const m = (d.getMonth() + 1).toString().padStart(2, '0')
          const day = d.getDate().toString().padStart(2, '0')
          return d.getFullYear() + '-' + m + '-' + day
        } catch (e) {
          return ''
        }
      }
    }
  }
</script>

<style>
 .ps-name{
   font-weight: 900 !important;
   font-size: 21px !important;
   margin-top: 5px !important;
   margin-bottom: 15px !important;
   margin-left: 21px !important;
 }
 
 .div2-left{
   margin-top: 10px;
   margin-right: 10px ;
 }
 
 .div2-right{
   margin-top: 10px;
   
 }
 
 .ps-desc{
   border-top:1px solid #e7eaec;
   padding-left:22px;
   padding-right:22px;
 }
 
 .div3{
   margin-top:5px;
   margin-bottom:10px;
 }
 .address-short{
   padding-left: 1px;
 }
 .address-detail{
   padding-left: 7px;
 }
</style>
