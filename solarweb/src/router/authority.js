export default {
  router: [{
    path: '/authority',
    component: resolve => require(['@/views/Authority/Index'], resolve),
    name: 'authority',
    meta: { title: '权限用户' }
  }]
}
