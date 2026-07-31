import request from '../../utils/request'
const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/device'

export function pageByParam (data) {
  return request({ url: baseUrl + '/pageByParam', method: 'post', data })
}
export function save (data) {
  return request({ url: baseUrl + '/save', method: 'post', data })
}
export function getDevice (id) {
  return request({ url: baseUrl + `/detail/${id}`, method: 'get' })
}
export function delDevice (id) {
  return request({ url: baseUrl + `/delete/${id}`, method: 'get' })
}
export function getDeviceListByType (type) {
  return request({ url: baseUrl + `/deviceByType/${type}`, method: 'get' }).then(res => {
    // 兼容工单：统一为 {id,name}，优先 name 作 id
    if (res && res.successful && Array.isArray(res.resultValue)) {
      res.resultValue = res.resultValue.map(d => ({
        id: d.name || d.id,
        name: d.name || d.id,
        raw: d
      }))
    }
    return res
  }).catch(() => {
    // 回退 listByType
    return request({ url: baseUrl + `/listByType/${type}`, method: 'get' })
  })
}
export function getDeviceList () {
  return request({ url: baseUrl + '/deviceList', method: 'get' })
}
