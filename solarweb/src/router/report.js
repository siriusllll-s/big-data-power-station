export default {
  router: [
    {
      path: '/report/dayReport',
      component: resolve => require(['@/views/Report/DayReport'], resolve),
      name: 'dayReport',
      meta: { title: '电站运行日报' }
    }
  ]
}
