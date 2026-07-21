export default {
  router: [{
    path: '/home',
    component: resolve => require(['@/views/Home/Home'], resolve),
    name: 'home',
    meta: { title: '首页' }
  }]
}
