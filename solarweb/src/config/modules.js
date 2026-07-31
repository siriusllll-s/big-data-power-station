/**
 * 前端业务模块与接口契约（团队协作清单）
 * - 路由 path / 组件路径 / 菜单
 * - 后端 REST 约定（占位，待 information 模块对接）
 * 协作：前端按此建页面，后端按 api 列表实现
 */
export const modules = [
  {
    id: 1,
    name: "首页看板",
    path: "/home",
    icon: "el-icon-s-home",
    group: "核心",
    component: "Home/Home",
    routerFile: "home.js",
    status: "ready",
    owner: "前端-A",
    desc: "登录后默认首页，展示电站概览与快捷入口",
    apis: [
      { method: "GET", url: "/api/home/summary", desc: "首页汇总指标" },
      { method: "GET", url: "/api/home/alarms", desc: "最新告警列表" }
    ]
  },
  {
    id: 2,
    name: "基础数据",
    path: "/base-data",
    icon: "el-icon-notebook-2",
    group: "基础",
    component: "BaseData/Index",
    routerFile: "baseData.js",
    status: "ready",
    owner: "前端-B / 后端-B",
    desc: "省市区、字典、厂商等基础资料维护",
    apis: [
      { method: "GET", url: "/api/dictionary/provinces", desc: "省份列表" },
      { method: "GET", url: "/api/dictionary/cities", desc: "城市列表" },
      { method: "GET", url: "/api/device-factory/list", desc: "设备厂商" }
    ]
  },
  {
    id: 3,
    name: "电站管理",
    path: "/station",
    icon: "el-icon-office-building",
    group: "业务",
    component: "Station/Station",
    routerFile: "station.js",
    status: "ready",
    owner: "前端-A / 后端-A",
    desc: "电站档案、电价 CRUD、合同计划电量",
    apis: [
      { method: "GET", url: "/api/station/detail/{id}", desc: "电站详情" },
      { method: "POST", url: "/api/station/save", desc: "电站保存" },
      { method: "POST", url: "/api/station/savePhoto", desc: "电站图片" },
      { method: "POST", url: "/api/stationSolarPrice/pageByParam", desc: "电价分页" },
      { method: "POST", url: "/api/stationSolarPrice/save", desc: "电价保存" },
      { method: "GET", url: "/api/stationSolarPrice/delete/{id}", desc: "电价删除" },
      { method: "POST", url: "/api/stationContract/pageByParam", desc: "合同分页" },
      { method: "POST", url: "/api/stationContract/save", desc: "合同保存" },
      { method: "GET", url: "/api/stationContract/delete/{id}", desc: "合同删除" }
    ]
  },
  {
    id: 31,
    name: "电价管理",
    path: "/price",
    icon: "el-icon-coin",
    group: "业务",
    component: "Station/Price",
    routerFile: "price.js",
    status: "ready",
    owner: "前端-A / 后端-A",
    desc: "电站电价增删改查",
    apis: [
      { method: "POST", url: "/api/stationSolarPrice/pageByParam", desc: "电价分页" },
      { method: "POST", url: "/api/stationSolarPrice/save", desc: "电价保存" }
    ]
  },
  {
    id: 32,
    name: "合同管理",
    path: "/contract",
    icon: "el-icon-document-checked",
    group: "业务",
    component: "Station/Contract",
    routerFile: "contract.js",
    status: "ready",
    owner: "前端-A / 后端-A",
    desc: "合同计划电量与效能参数",
    apis: [
      { method: "POST", url: "/api/stationContract/pageByParam", desc: "合同分页" },
      { method: "POST", url: "/api/stationContract/save", desc: "合同保存" }
    ]
  },
  {
    id: 4,
    name: "设备管理",
    path: "/device",
    icon: "el-icon-cpu",
    group: "业务",
    component: "Device/Index",
    routerFile: "device.js",
    status: "ready",
    owner: "前端-B / 后端-A",
    desc: "逆变器、汇流箱、电表、直流柜等设备",
    apis: [
      { method: "GET", url: "/api/device/list", desc: "设备列表" },
      { method: "GET", url: "/api/device/{id}", desc: "设备详情" },
      { method: "POST", url: "/api/device", desc: "新增设备" }
    ]
  },
  {
    id: 5,
    name: "实时监控",
    path: "/data-monitoring",
    icon: "el-icon-monitor",
    group: "数据",
    component: "DataMonitoring/Index",
    routerFile: "dataMonitoring.js",
    status: "ready",
    owner: "前端-C / 大数据",
    desc: "设备实时数据、功率曲线（HBase/Redis）",
    apis: [
      { method: "GET", url: "/api/monitor/realtime", desc: "实时测点" },
      { method: "GET", url: "/api/monitor/curve", desc: "功率/发电曲线" }
    ]
  },
  {
    id: 6,
    name: "数据分析",
    path: "/dataAnalysis/stationKWhStatistics",
    icon: "el-icon-data-analysis",
    group: "数据",
    component: "DataAnalysis/Index",
    routerFile: "dataAnalysis.js",
    status: "ready",
    owner: "前端-C / Spark",
    desc: "统计报表、对比分析",
    apis: [
      { method: "GET", url: "/api/analysis/power", desc: "发电量分析" },
      { method: "GET", url: "/api/analysis/loss", desc: "损失分析" }
    ]
  },
  {
    id: 7,
    name: "异常检索",
    path: "/data-search",
    icon: "el-icon-search",
    group: "数据",
    component: "DataSearch/Index",
    routerFile: "dataSearch.js",
    status: "ready",
    owner: "前端-B / ES",
    desc: "故障、异常、损失电量查询（ES）",
    apis: [
      { method: "GET", url: "/api/search/fault", desc: "故障检索" },
      { method: "GET", url: "/api/search/abnormal", desc: "异常检索" }
    ]
  },
  {
    id: 8,
    name: "巡检点",
    path: "/inspection/point",
    icon: "el-icon-s-check",
    group: "运维",
    component: "Inspection/InspectionPoint",
    routerFile: "inspection.js",
    status: "ready",
    owner: "前端-A / 后端-B",
    desc: "巡检点管理",
    apis: [
      { method: "POST", url: "/api/inspectionPoint/point/pageByParam", desc: "巡检点分页" }
    ]
  },
  {
    id: 81,
    name: "巡检计划",
    path: "/inspection/plan",
    icon: "el-icon-date",
    group: "运维",
    component: "Inspection/InspectionPlan",
    routerFile: "inspection.js",
    status: "ready",
    owner: "前端-A / 后端-B",
    desc: "巡检计划管理",
    apis: [
      { method: "POST", url: "/api/inspectionPlan/pageByParam", desc: "计划分页" }
    ]
  },
  {
    id: 82,
    name: "巡检实施",
    path: "/inspection/implement",
    icon: "el-icon-s-claim",
    group: "运维",
    component: "Inspection/Implement",
    routerFile: "inspection.js",
    status: "ready",
    owner: "前端-A / 后端-B",
    desc: "日历查看巡检进度",
    apis: [
      { method: "GET", url: "/api/inspection/manageList", desc: "巡检进度" }
    ]
  },
  {
    id: 83,
    name: "工单管理",
    path: "/workOrder",
    icon: "el-icon-s-order",
    group: "运维",
    component: "Inspection/workOrder",
    routerFile: "inspection.js",
    status: "ready",
    owner: "前端-A / 后端-B",
    desc: "故障工单流程",
    apis: [
      { method: "POST", url: "/api/workerOrder/pageByParam", desc: "工单分页" }
    ]
  },
  {
    id: 9,
    name: "经验库",
    path: "/experience",
    icon: "el-icon-collection",
    group: "运维",
    component: "Experience/Index",
    routerFile: "experience.js",
    status: "ready",
    owner: "前端-B",
    desc: "运维经验沉淀与检索",
    apis: [
      { method: "GET", url: "/api/experience/list", desc: "经验列表" },
      { method: "POST", url: "/api/experience", desc: "新增经验" }
    ]
  },
  {
    id: 10,
    name: "运行日报",
    path: "/report/dayReport",
    icon: "el-icon-document",
    group: "报表",
    component: "Report/DayReport",
    routerFile: "report.js",
    status: "ready",
    owner: "前端-C / 后端-A",
    desc: "定时任务生成日报，分页查询/编辑/查看",
    apis: [
      { method: "POST", url: "/api/powerDataReport/pageByParam", desc: "日报分页" },
      { method: "GET", url: "/api/powerDataReport/detail/{id}", desc: "日报详情" },
      { method: "POST", url: "/api/powerDataReport/update", desc: "日报编辑" },
      { method: "POST", url: "/api/powerDataReport/generate", desc: "手动生成" }
    ]
  },
  {
    id: 11,
    name: "权限用户",
    path: "/authority",
    icon: "el-icon-user",
    group: "系统",
    component: "Authority/Index",
    routerFile: "authority.js",
    status: "ready",
    owner: "前端-A / 后端-A",
    desc: "用户、角色、菜单权限",
    apis: [
      { method: "GET", url: "/api/user/list", desc: "用户列表" },
      { method: "GET", url: "/api/role/list", desc: "角色列表" },
      { method: "POST", url: "/api/auth/menu", desc: "菜单权限" }
    ]
  },
  {
    id: 12,
    name: "接口协作台",
    path: "/team-board",
    icon: "el-icon-s-cooperation",
    group: "协作",
    component: "Team/Board",
    routerFile: "team.js",
    status: "ready",
    owner: "全员",
    desc: "模块/接口进度一览，方便分工",
    apis: []
  }
]

export function modulesToMenu () {
  return modules.map(m => ({
    id: m.id,
    name: m.name,
    menuUrl: m.path,
    menuIcon: m.icon,
    group: m.group,
    status: m.status
  }))
}

export default modules
