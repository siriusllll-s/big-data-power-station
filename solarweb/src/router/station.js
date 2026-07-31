export default {
    router: [{
      path: "/station",
      component: resolve => require(['@/views/Station/Station'], resolve),
      name: "station",
      meta: {
        title: "电站信息"
      }
    },
    ]
}
