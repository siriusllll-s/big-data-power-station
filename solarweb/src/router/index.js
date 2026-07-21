import Vue from 'vue'
import Router from 'vue-router'

const originalPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

Vue.use(Router)
const files = require.context('./', true, /\.js$/)
let rlt = []
files.keys().forEach(key => {
  let obj = {
    key: key,
    context: files(key),
    globalPath: files.resolve(key)
  }
  if (obj.key !== './index.js' && obj.context.default && (obj.context.default.router || []).length > 0) {
    rlt = rlt.concat(obj.context.default.router || [])
  }
})

const router = new Router({
  base: process.env.BASE_URL,
  mode: 'history',
  routes: [
    {
      path: '/login',
      component: resolve => require(['@/views/Login/Login'], resolve),
      meta: {
        auth: true
      }
    },
    {
      path: '/',
      name: 'Main',
      component: () => import('@/views/Layout/Layout'),
      children: rlt
    },
    {
      path: '/main',
      redirect: '/home'
    },
    {
      path: '*',
      component: () => import('@/views/Login/Login')
    }
  ]
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    next()
  } else {
    let token = localStorage.getItem('Authorization')
    if (token === null || token === '') {
      next('/login')
    } else {
      next()
    }
  }
})

export default router
