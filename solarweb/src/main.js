import Vue from 'vue'
import App from './App'
import router from './router'
import store from './store/index'
import './assets/css/basic.css'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import './assets/icon/iconfont.css'
import './utils/index'
import * as echarts from 'echarts'
import axios from 'axios'
import moment from 'moment'

Vue.prototype.$echarts = echarts
Vue.prototype.$axios = axios
Vue.prototype.$moment = moment
Vue.config.productionTip = false
Vue.use(ElementUI)

new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
