export default {
  router: [{
    path: '/data-monitoring',
    component: resolve => require(['@/views/DataMonitoring/Index'], resolve),
    name: 'dataMonitoring',
    meta: { title: '实时监控' }
  }]
}
