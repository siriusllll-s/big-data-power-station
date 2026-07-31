export default {
  router: [{
    path: '/device',
    component: resolve => require(['@/views/Device/Index'], resolve),
    name: 'device',
    meta: { title: '设备管理' }
  }]
}
