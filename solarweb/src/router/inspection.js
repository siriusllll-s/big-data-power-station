export default {
  router: [
    {
      path: '/inspection/point',
      component: resolve => require(['@/views/Inspection/InspectionPoint'], resolve),
      name: 'inspectionPoint',
      meta: { title: '巡检点管理' }
    },
    {
      path: '/inspection/plan',
      component: resolve => require(['@/views/Inspection/InspectionPlan'], resolve),
      name: 'inspectionPlan',
      meta: { title: '巡检计划管理' }
    },
    {
      path: '/inspection/implement',
      component: resolve => require(['@/views/Inspection/Implement'], resolve),
      name: 'inspectionImplement',
      meta: { title: '巡检实施' }
    },
    {
      path: '/workOrder',
      component: resolve => require(['@/views/Inspection/workOrder'], resolve),
      name: 'workOrder',
      meta: { title: '工单管理' }
    }
  ]
}
