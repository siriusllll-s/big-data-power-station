import Vue from 'vue'
import { hasPermission } from './permission'

Vue.directive('dbClick', {
  inserted (el, binding) {
    el.addEventListener('click', () => {
      if (!el.disabled) {
        el.disabled = true
        setTimeout(() => {
          el.disabled = false
        }, binding.value || 1000)
      }
    })
  }
})

Vue.directive('permission', {
  inserted (el, binding) {
    if (binding.value && !hasPermission(binding.value)) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  }
})
