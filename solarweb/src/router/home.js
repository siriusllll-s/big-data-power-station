export default {
  router: [{
    path: 'home',
    component: resolve => require(['@/views/Home/Home'], resolve),
    name: 'Home',
    meta: { title: '首页' }
  }, {
    path: 'home/detail',
    component: resolve => require(['@/views/Home/Home'], resolve),
    name: 'detail',
    meta: { title: '电站详细数据' }
  }]
}
