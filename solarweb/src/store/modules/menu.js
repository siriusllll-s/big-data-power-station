const state = () => ({
  collapseMenu: false
})
const getters = {
  isMenuCollapsed: function (state) {
    return state.collapseMenu
  }
}
const actions = {}
const mutations = {
  collapseMenu (state) {
    state.collapseMenu = true
  },
  expandMenu (state) {
    state.collapseMenu = false
  },
  toggleMenu (state) {
    state.collapseMenu = !state.collapseMenu
  }
}

export default {
  namespaced: true,
  state,
  getters,
  actions,
  mutations
}
