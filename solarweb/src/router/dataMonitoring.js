export default {
  router: [
    {
      path: '/dataMonitoring/threshold',
      component: resolve => require(['@/views/DataMonitoring/Threshold'], resolve),
      name: 'threshold',
      meta: { title: '阈值设置' }
    },
    {
      path: '/dataMonitoring/historyData',
      component: resolve => require(['@/views/DataMonitoring/HistoryData'], resolve),
      name: 'historyData',
      meta: { title: '设备实时数据查询' }
    },
    {
      path: '/dataMonitoring/stationPower',
      component: resolve => require(['@/views/DataMonitoring/StationPower'], resolve),
      name: 'stationPower',
      meta: { title: '电站发电量查询' }
    },
    {
      path: '/dataMonitoring/inverter',
      component: resolve => require(['@/views/DataMonitoring/Inverter'], resolve),
      name: 'inverter',
      meta: { title: '逆变器实时监控' }
    },
    {
      path: '/dataMonitoring/combinerBox',
      component: resolve => require(['@/views/DataMonitoring/CombinerBox'], resolve),
      name: 'combinerBox',
      meta: { title: '汇流箱实时监控' }
    },
    {
      path: '/dataMonitoring/meter',
      component: resolve => require(['@/views/DataMonitoring/Meter'], resolve),
      name: 'meter',
      meta: { title: '电表实时监控' }
    },
    {
      path: '/data-monitoring',
      redirect: '/dataMonitoring/threshold'
    }
  ]
}
