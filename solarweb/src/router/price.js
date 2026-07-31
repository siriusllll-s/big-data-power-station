export default {
    router: [{
      path: "/price",
      component: resolve => require(['@/views/Station/Price'], resolve),
      name: "price",
      meta: {
        title: "电价管理"
      }
    }, ]
}
