export default {
  router: [
    {
      path: 'dataAnalysis/stationKWhStatistics',
      component: resolve => require(['@/views/DataAnalysis/StationKWhStatistics'], resolve),
      name: 'stationKWhStatistics',
      meta: { title: '电站发电量统计' }
    },
    {
      path: 'dataAnalysis/deviceKWhStatistic',
      component: resolve => require(['@/views/DataAnalysis/Index'], resolve),
      name: 'deviceKWhStatistic',
      meta: { title: '设备发电量统计' }
    },
    {
      path: 'dataAnalysis/lossKWhStatistics',
      component: resolve => require(['@/views/DataAnalysis/Index'], resolve),
      name: 'lossKWhStatistics',
      meta: { title: '损失发电量统计' }
    }
  ]
}
