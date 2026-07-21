import Vue from 'vue'

let _vue_ = {
  STORE_MAIN_KEY: 'templateweb',
  set store (data) {
    this._data = data
    localStorage[this.STORE_MAIN_KEY] = JSON.stringify(data)
  },
  get store () {
    if (!this._data) {
      var storeStr = localStorage[this.STORE_MAIN_KEY]
      if (storeStr === undefined) {
        localStorage[this.STORE_MAIN_KEY] = '{}'
        this._data = {}
      } else {
        this._data = JSON.parse(storeStr)
      }
    }
    return this._data
  }
}

Vue.getUser = function () {
  return Vue.getData('loginuserReport')
}

Vue.setData = function () {
  var obj, key, val, lastobj, initData
  if (arguments.length === 3) {
    obj = arguments[0]
    key = arguments[1]
    val = arguments[2]
    lastobj = obj
  } else {
    key = arguments[0]
    val = arguments[1]
    initData = lastobj = _vue_.store
  }
  var paths = key.split('.')
  for (var i = 0; i < paths.length - 1; i++) {
    if (Vue.isNone(lastobj[paths[i]])) {
      lastobj[paths[i]] = {}
    }
    lastobj = lastobj[paths[i]]
  }
  lastobj[paths[paths.length - 1]] = val
  if (arguments.length === 2) _vue_.store = initData
}

Vue.getData = function () {
  var obj, key, lastobj, defaultVal
  var args = arguments
  if (typeof args[0] !== 'string' && typeof args[1] === 'string') {
    obj = args[0]
    lastobj = obj
    Array.prototype.shift.call(args)
  } else {
    lastobj = _vue_.store
  }
  key = args[0]
  defaultVal = args[1]
  var paths = key.split('.')
  for (var i = 0; i < paths.length; i++) {
    if (Vue.isNone(lastobj)) return defaultVal
    lastobj = lastobj[paths[i]]
  }
  return lastobj === undefined ? defaultVal : lastobj
}

Vue.isNone = function (obj) {
  return obj === null || obj === undefined
}

Vue.isNull = function (obj) {
  return obj === null || obj === undefined || obj === ''
}

Vue.cleanUp = function () {
  Vue.setData('loginuserReport', {})
  Vue.setData('tabListReport', '')
  Vue.setData('supplierKey', '')
}

Vue.isEmpty = function (obj) {
  return Vue.isNone(obj) || (obj.length === 0 && typeof obj !== 'function') || (typeof obj === 'object' && obj.length === 0)
}

Vue.isEmptyObject = function (obj) {
  var empty = true
  obj = obj || {}
  for (var prop in obj) {
    if (obj.hasOwnProperty(prop)) {
      empty = false
      break
    }
  }
  return empty
}

Vue.getProperties = function (target, propertyNames, inDeep) {
  var ret = {}
  for (var i = 0; i < propertyNames.length; i++) {
    ret[propertyNames[i]] = inDeep ? Vue.getData(target, propertyNames[i]) : target[propertyNames[i]]
  }
  return ret
}

Vue.setProperties = function (target, properties) {
  for (var name in properties) {
    target[name] = properties[name]
  }
}

Vue.authorize = function (auths) {
  var arr = auths.split(',')
  var result = false
  var authArr = Vue.getData('loginuserReport.authList') || []
  result = authArr.some(auth => arr.indexOf(auth.authCode) > -1)
  return result
}

Vue.getRandomString = function (len, allowed) {
  var text = ''
  allowed = typeof allowed === 'string' ? allowed : 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789'
  for (var i = 0; i < len; i++) {
    text += allowed.charAt(Math.floor(Math.random() * allowed.length))
  }
  return text
}

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

Vue.objectAssign = function (target, ...sources) {
  sources.forEach(source => {
    if (Object.prototype.toString.call(source) === '[object Object]') {
      for (var key in source) {
        var subObj = source[key]
        if (!target) {
          target = {}
        }
        target[key] = Vue.objectAssign(target[key], subObj)
      }
    } else if (Object.prototype.toString.call(source) === '[object Array]') {
      source.forEach(subSource => {
        if (!target) {
          target = []
        }
        if (!target.some(a => Object.prototype.toString.call(subSource) === Object.prototype.toString.call(a) && JSON.stringify(a) === JSON.stringify(subSource))) {
          target.push(subSource)
        }
      })
    } else {
      target = source
    }
  })
  return target
}

Vue.copy = function (data) {
  const t = Object.prototype.toString.call(data)
  let o
  if (t === '[object Array]') {
    o = []
  } else if (t === '[object Object]') {
    o = {}
  } else {
    return data
  }
  if (t === '[object Array]') {
    for (let i = 0; i < data.length; i++) {
      o.push(Vue.copy(data[i]))
    }
  } else if (t === '[object Object]') {
    for (let i in data) {
      o[i] = Vue.copy(data[i])
    }
  }
  return o
}
