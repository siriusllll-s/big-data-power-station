export default {
  router: [
    {
      path: '/dataAnalysis/stationKWhStatistics',
      component: resolve => require(['@/views/DataAnalysis/StationKWhStatistics'], resolve),
      name: 'stationKWhStatistics',
      meta: { title: '电站发电量统计' }
    },
    {
      path: '/data-analysis',
      component: resolve => require(['@/views/DataAnalysis/Index'], resolve),
      name: 'dataAnalysis',
      meta: { title: '数据分析' }
    }
  ]
}
