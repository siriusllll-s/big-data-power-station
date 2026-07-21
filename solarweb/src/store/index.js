import Vue from 'vue'
import Vuex from 'vuex'
import menu from './modules/menu'
Vue.use(Vuex)
export default new Vuex.Store({
  modules: {
    menu
  },
  state: {
    Authorization: localStorage.getItem('Authorization') ? localStorage.getItem('Authorization') : ''
  },
  mutations: {
    changeLogin (state, user) {
      state.Authorization = user.Authorization
      localStorage.setItem('Authorization', user.Authorization)
      console.log(state.Authorization)
    }
  }
})
