export default {
  router: [{
    path: '/data-search',
    component: resolve => require(['@/views/DataSearch/Index'], resolve),
    name: 'dataSearch',
    meta: { title: '异常检索' }
  }]
}
