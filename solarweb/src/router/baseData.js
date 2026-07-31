export default {
  router: [{
    path: '/base-data',
    component: resolve => require(['@/views/BaseData/Index'], resolve),
    name: 'baseData',
    meta: { title: '基础数据' }
  }]
}
