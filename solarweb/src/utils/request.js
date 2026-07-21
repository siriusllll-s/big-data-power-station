import Vue from 'vue'
import axios from 'axios'
Vue.prototype.$ajax = Vue.ajax = axios

axios.defaults.baseURL = process.env.NODE_ENV !== 'production' ? '' : (process.env.VUE_APP_BASE_API)

axios.interceptors.request.use(config => {
  if (localStorage.getItem('Authorization')) {
    config.headers.common['Authorization'] = localStorage.getItem('Authorization')
  }
  return config
}, err => {
  return Promise.reject(err)
})

axios.interceptors.response.use(function (response) {
  if (response.headers.authorization) {
    localStorage.setItem('Authorization', response.headers.authorization)
  }
  return response.data
}, function (error) {
  if (JSON.stringify(error).indexOf('401') !== -1) {
    localStorage.removeItem('Authorization')
  }
  console.log('error', error)
  return Promise.reject(error)
})

export default axios
