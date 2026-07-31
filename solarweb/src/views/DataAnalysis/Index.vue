<template>
  <div>
    <el-card>
      <div slot="header">数据分析</div>
      <el-button type="primary" @click="$router.push('/dataAnalysis/stationKWhStatistics')">电站发电量统计</el-button>
      <p style="margin-top:12px;color:#909399">设备/损失统计可在后续扩展，当前电站统计已对接 /statistics/station。</p>
    </el-card>
  </div>
</template>
<script>
export default { name: 'DataAnalysisIndex' }
</script>
