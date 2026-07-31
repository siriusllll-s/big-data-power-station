export default {
  router: [{
    path: '/team-board',
    component: resolve => require(['@/views/Team/Board'], resolve),
    name: 'teamBoard',
    meta: { title: '接口协作台' }
  }]
}
