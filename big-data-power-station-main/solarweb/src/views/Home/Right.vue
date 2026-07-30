<template>
  <div>
    <div class="grid-content h270">
      <div class="echarts" ref="echarts1" :style="{width: '100%', height: '100%'}"></div>
    </div>
    <div class="grid-content mt20 h650">
      <div class="echarts" ref="echarts2" :style="{width: '100%', height: '100%'}"></div>
    </div>
  </div>
</template>

<script>
import ECharts from "echarts";
import { getStationFaultCount,getStationMonthKWhStatistic } from "@/api/Home/Home";
export default {
  name: "Right",
  data: function() {
    return {
      myChart1: {},
      myChart2: {},
      dataList1:[],
      dataList2:[],
    };
  },
  mounted() {
    this.initECharts()
    this.getEchart1()
    this.getEchart2()
  },
  methods: {
    
    initECharts() {
      let echarts = require("echarts");
      this.myChart1 = echarts.init(this.$refs["echarts1"]);
      this.myChart2 = echarts.init(this.$refs["echarts2"]);
    },
    
    
    getEchart1() {
      getStationFaultCount().then(data => {
        if (data.successful && !!data.resultValue) {
          this.dataList1 = data.resultValue;
          this.echart1(this.dataList1);
        } else {
          this.$message.error(data.resultHint);
        }
      })
    },
    
    echart1(list) {
      var dataName = [];
      var data1 = [];
      var myColor = ["#f39426", "#51b8f9", "#f258b6", "#6c84ff"];
      for (let index = 0; index < list.length; index++) {
        dataName.push(list[index].type);
        data1.push(list[index].count);
      }
      let option = {
          title: {
              text: "故障数统计", top: 20, left: 20, textStyle: {
                  color: "#fff",
                  fontWeight: 'normal'
              }
          },
          tooltip: {
              trigger: "axis",
              axisPointer: {
                  crossStyle: {
                      color: "#999"
                  }
              }
          },
          grid: {
              top: "20%",
              left: 20,
              right: 20,
              bottom: "10%",
              containLabel: true
          },
          xAxis: [
              {
                  type: "category",
                  data: dataName,
                  axisLine: {
                      show: true,
                      lineStyle: {
                          color: "#d2dde7"
                      }
                  },
                  axisLabel: {
                      show: true,
                      textStyle: {
                          color: "#bac8d5",
                          // fontWeight: "bold",
                          fontSize: 14
                      }
                  },
                  axisTick: {
                      show: false
                  },
                  axisPointer: {
                      type: "shadow",
                      triggerTooltip: true
                  }
              }
          ],
          yAxis: [
              {
                  type: "value",
                  min: 0,
                  // max: 100,
                  interval: 25,
                  axisLine: {
                      show: false,
                      lineStyle: {
                          color: "#bac8d5"
                      }
                  },
                  axisTick: {
                      show: false
                  },
                  splitLine: {
                      show: false,
                      lineStyle: {
                          color: "#d2dde7"
                      }
                  },
                  axisLabel: {
                      show: false,
                      color: "#bac8d5",
                      formatter: "{value}"
                  }
              }
          ],
          series: [
              {
                  name: "故障数",
                  type: "bar",
                  data: data1,
                  barWidth: 40, //柱图宽度
                  itemStyle: {
                      normal: {
                          barBorderRadius: [5],
                          color: function (params) {
                              return myColor[params.dataIndex];
                          },
                          label: {
                              show: false,
                              position: "top",
                              formatter: "{c}%"
                          }
                      }
                  }
              }
          ]
      };
      this.myChart1.setOption(option);
      
    },
    
    getEchart2() {
      getStationMonthKWhStatistic().then(data => {
        if (data.successful && !!data.resultValue) {
          this.dataList2 = data.resultValue;
          this.echart2(this.dataList2);
        } else {
          this.$message.error(data.resultHint);
        }
      })
    },
    
    echart2(list) {
      var dataName = [];
      var data2 = [];
      for (let index = 0; index < list.length; index++) {
        dataName.push(list[index].powerDate);
        data2.push(list[index].kwh);
      }
      let option = {
          title: {
              text: "12个月的发电量", top: 20, left: 20, textStyle: {
                  color: "#fff",
                  fontWeight: 'normal',
              }
          },
          tooltip: {
              trigger: "axis",
              axisPointer: {
                  crossStyle: {
                      color: "#999"
                  }
              },
              formatter: function (p) {
                  return '日期：' + p[0].name + '<br>' + '发电量：' + p[0].value + ' kW·h';
              },
          },
          grid: {
              top: 55,
              left: 20,
              right: 20,
              bottom: 20,
              containLabel: true
          },
          xAxis: [
              {
                  type: "value",
                  axisLine: {
                      show: false,
                      lineStyle: {
                          color: "#d2dde7"
                      }
                  },
                  splitLine: {
                      show: false
                  },
                  axisLabel: {
                      show: false,
                      textStyle: {
                          color: "#bac8d5",
                          // fontWeight: "bold",
                          fontSize: 14
                      }
                  },
                  axisTick: {
                      show: false
                  }
              }
          ],
          yAxis: [
              {
                  type: "category",
                  axisLine: {
                      show: true,
                      lineStyle: {
                          color: "#bac8d5"
                      }
                  },
                  axisTick: {
                      show: false
                  },
                  splitLine: {
                      show: false,
                      lineStyle: {
                          color: "#d2dde7"
                      }
                  },
                  axisLabel: {
                      show: true,
                      textStyle: {
                          color: "#bac8d5",
                          fontSize: 14
                      },
                      formatter: "{value}"
                  },
                  axisPointer: {
                      type: "shadow",
                      triggerTooltip: true
                  },
                  data: dataName
              }
          ],
          series: [
              {
                  name: "发电量",
                  type: "bar",
                  barWidth: 12, //柱图宽度
                  itemStyle: {
                      normal: {
                          barBorderRadius: [0, 5, 5, 0],
                          color: "#51b8f9",
                          label: {
                              show: false,
                              position: "top",
                              formatter: "{c}%"
                          }
                      }
                  },
                  data: data2
              }
          ]
      };
      this.myChart2.setOption(option);
      
    },
    
    
  }
};
</script>

<style scoped>
.grid-content {
  overflow: hidden;
  position: relative;
  background: #082540;
  min-height: 160px;
  border-radius: 10px;
  -moz-box-shadow: 0px 0px 10px rgba(19, 47, 154, 0.19);
  -webkit-box-shadow: 0px 0px 10px rgba(19, 47, 154, 0.19);
  box-shadow: 0px 0px 10px rgba(19, 47, 154, 0.19);
}
.h270 {
  height: 270px;
}
.h650 {
  height: 650px;
}

.mt20 {
  margin-top: 20px;
}
@media (max-width: 1400px) {
  .h270 {
  height: 250px;
  }
  .h650 {
    height: 450px;
  }
}
</style>
