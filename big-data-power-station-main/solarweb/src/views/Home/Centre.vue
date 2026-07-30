<template>
  <div>
    <div class="grid-content h520">
      <div class="echarts" ref="echarts1" :style="{width: '100%', height: '100%'}"></div>
    </div>
    <div class="grid-content mt20 h400">
      <div class="echarts" ref="echarts2" :style="{width: '100%', height: '100%'}"></div>
    </div>
  </div>
</template>

<script>
import ECharts from "echarts";
import { getStationLastThirtyDayPower } from "@/api/Home/Home";
export default {
  name: "Centre",
  data: function() {
    return {
      myChart1: {},
      myChart2: {},
      dataList:[],
      
    };
  },
  mounted() {
    this.initECharts()
    this.getEchart();
  },
  methods: {
    
    initECharts() {
      let echarts = require("echarts");
      this.myChart1 = echarts.init(this.$refs["echarts1"]);
      this.myChart2 = echarts.init(this.$refs["echarts2"]);
    },
    
    getEchart() {
      getStationLastThirtyDayPower().then(data => {
        if (data.successful && !!data.resultValue) {
          this.dataList = data.resultValue;
          this.echart1(this.dataList);
          this.echart2(this.dataList);
        } else {
          this.$message.error(data.resultHint);
        }
      })
    },
    
    
    echart1(list) {
      var dataName = [];
      var data1 = [];
      for (let index = 0; index < list.length; index++) {
        dataName.push(list[index].powerDate.substr(5, 5));
        data1.push(list[index].kwh);
      }
      
      let option = {
          title: {
              text: "日发电量统计",
              top: 20,
              left: 20,
              textStyle: {
                  color: "#fff",
                  fontWeight: 'normal',
              }
          },
          tooltip: {
              trigger: "axis",
              axisPointer: {
                  type: "line",
                  lineStyle: {
                      type: "dashed",
                      color: "#d2dde7"
                  }
              },
              formatter: function (p) {
                  return '日期：' + p[0].name + '<br>' + '发电量：' + p[0].value + ' kW·h';
              },
          },
          grid: {
              top: 90,
              left: 40,
              right: 30,
              bottom: "3%",
              containLabel: true
          },
          xAxis: {
              boundaryGap: false,
              splitLine: {
                  show: false
              },
              axisLine: {
                  show: true,
                  lineStyle: {
                      color: "#d2dde7"
                  }
              },
              axisLabel: {
                  show: true,
                  interval: 0,
                  rotate: 55,
                  margin: 15,
                  textStyle: {
                      color: "#bac8d5",
                      fontSize: 14
                  }
              },
              axisTick: {
                  show: false
              },
              axisPointer: {
                  triggerTooltip: true
              },
              data: dataName,
          },
          yAxis: {
              type: "value",
              name: "kW·h",
              nameTextStyle: {
                  padding: [0, 40, 0, 0]
              },
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
                  lineStyle: {
                      color: "#d2dde7"
                  }
              },
              axisLabel: {
                  textStyle: {
                      color: "#bac8d5",
                      fontSize: 14
                  },
                  formatter: "{value}"
              }
          },
          series: [
              {
                  type: "line",
                  symbol: "none",
                  // symbolSize: 6,
                  lineStyle: {
                      normal: {
                          width: 4
                      }
                  },
                  areaStyle: {
                      normal: {
                          opacity: 0.9,
                          color: "#5fccc3"
                      }
                  },
                  itemStyle: {
                      normal: {
                          color: "#5fccc3"
                      }
                  },
                  data: data1
              }
          ]
      };
      this.myChart1.setOption(option);
      
      
    
    },
    
    echart2(list) {
      var dataName = [];
      var data2 = [];
      for (let index = 0; index < list.length; index++) {
        dataName.push(list[index].powerDate.substr(5, 5));
        data2.push(list[index].powerRatio);
      }
      
      let option = {
          title: {
              text: "近30天发电效率",
              top: 20,
              left: 20,
              textStyle: {
                  color: "#fff",
                  fontWeight: 'normal',
              }
          },
          tooltip: {
              trigger: "axis",
              axisPointer: {
                  type: "line",
                  lineStyle: {
                      type: "dashed",
                      color: "#f258b6"
                  }
              },
              formatter: function (p) {
                  return '日期：' + p[0].name + '<br>' + '发电效率：' + p[0].value + '%';
              }
          },
          grid: {
              top: 85,
              left: 35,
              right: 30,
              bottom: "3%",
              containLabel: true
          },
          xAxis: {
              boundaryGap: false,
              splitLine: {
                  show: false
              },
              axisLine: {
                  show: true,
                  lineStyle: {
                      color: "#d2dde7"
                  }
              },
              axisLabel: {
                  show: true,
                  interval: 0,
                  rotate: 55,
                  margin: 15,
                  textStyle: {
                      color: "#bac8d5",
                      fontSize: 14
                  }
              },
              axisTick: {
                  show: false
              },
              axisPointer: {
                  triggerTooltip: true
              },
              data:dataName
          },
          yAxis: {
              type: "value",
              name: "(%)",
              nameTextStyle: {
                  padding: [0, 40, 0, 0]
              },
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
                  lineStyle: {
                      color: "#d2dde7"
                  }
              },
              axisLabel: {
                  textStyle: {
                      color: "#bac8d5",
                      fontSize: 14
                  },
                  formatter: "{value}"
              }
          },
          series: [
              {
                  type: "line",
                  symbol: "none",
                  smooth: true,
                  // symbolSize: 6,
                  lineStyle: {
                      normal: {
                          width: 4,
                          color: "#2566b9"
                      }
                  },
                  areaStyle: {
                      normal: {
                          opacity: 0.9,
                          color: new ECharts.graphic.LinearGradient(
                              0,
                              0,
                              0,
                              1,
                              [
                                  {
                                      offset: 0.2,
                                      color: "#2566b9"
                                  },
                                  {
                                      offset: 1,
                                      color: "rgba(255,255,255, .2)"
                                  }
                              ],
                              false
                          )
                      }
                  },
                  itemStyle: {
                      normal: {
                          color: "#f258b6"
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
.h520 {
  height: 520px;
}
.h400 {
  height: 400px;
}

.mt20 {
  margin-top: 20px;
}

@media (max-width: 1440px) {
  .h520 {
    height: 400px;
  }
  .h400 {
    height: 300px;
  }
  /* .grid-content {
    height: 120px;
    padding: 0 10px;
    line-height: 40px;
  } */
}
</style>
