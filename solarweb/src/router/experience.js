export default {
  router: [{
    path: '/experience',
    component: resolve => require(['@/views/Experience/Index'], resolve),
    name: 'experience',
    meta: { title: '经验库' }
  }]
}
