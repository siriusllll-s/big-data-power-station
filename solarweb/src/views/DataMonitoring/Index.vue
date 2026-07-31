<template>
  <div>
    <el-button type="primary" @click="$router.push('/dataMonitoring/threshold')">阈值设置</el-button>
    <el-button @click="$router.push('/dataMonitoring/historyData')">设备数据</el-button>
    <el-button @click="$router.push('/dataMonitoring/stationPower')">电站发电量</el-button>
    <el-button @click="$router.push('/dataMonitoring/inverter')">逆变器</el-button>
    <el-button @click="$router.push('/dataMonitoring/combinerBox')">汇流箱</el-button>
    <el-button @click="$router.push('/dataMonitoring/meter')">电表</el-button>
  </div>
</template>
<script>
export default { name: 'DataMonitoringIndex' }
</script>
