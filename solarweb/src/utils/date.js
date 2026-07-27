export function formatDate (date, fmt) {
  if (!date) return ''
  if (typeof date === 'string') date = new Date(date.replace(/-/g, '/'))
  if (typeof date === 'number') date = new Date(date)
  fmt = fmt || 'yyyy-MM-dd HH:mm:ss'
  const o = {
    'M+': date.getMonth() + 1,
    'd+': date.getDate(),
    'H+': date.getHours(),
    'm+': date.getMinutes(),
    's+': date.getSeconds()
  }
  if (/(y+)/.test(fmt)) {
    fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
  }
  for (const k in o) {
    if (new RegExp('(' + k + ')').test(fmt)) {
      fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)))
    }
  }
  return fmt
}

export function getCurrentDate () {
  return formatDate(new Date(), 'yyyy-MM-dd')
}

export function getCurrentMonthFirstDay () {
  const d = new Date()
  d.setDate(1)
  return formatDate(d, 'yyyy-MM-dd')
}

/** 起止间隔是否大于约一个月（按天差 > 31） */
export function compareGTOneMonth (start, end) {
  if (!start || !end) return false
  const s = new Date(String(start).replace(/-/g, '/'))
  const e = new Date(String(end).replace(/-/g, '/'))
  const diff = (e.getTime() - s.getTime()) / (24 * 3600 * 1000)
  return diff > 31
}
