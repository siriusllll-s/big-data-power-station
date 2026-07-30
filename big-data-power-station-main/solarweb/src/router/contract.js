export default {
    router: [{
      path: "/contract",
      component: resolve => require(['@/views/Station/Contract'], resolve),
      name: "contract",
      meta: {
        title: "合同管理"
      }
    }, ]
}
