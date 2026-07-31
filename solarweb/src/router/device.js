export default {
  router: [
    {
      path: '/factory',
      component: resolve => require(['@/views/Device/VendorList'], resolve),
      name: 'factory',
      meta: { title: '设备厂商信息' }
    },
    {
      path: '/device',
      component: resolve => require(['@/views/Device/DeviceList'], resolve),
      name: 'device',
      meta: { title: '设备信息' }
    }
  ]
}
