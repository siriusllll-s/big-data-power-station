import request from '../../utils/request'
const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/inspectionPlan'

export function pageByParam (data) {
  return request({ url: baseUrl + '/pageByParam', method: 'post', data })
}
export function getDetail (id) {
  return request({ url: baseUrl + `/detail/${id}`, method: 'get' })
}
export function save (data) {
  return request({ url: baseUrl + '/save', method: 'post', data })
}
export function del (id) {
  return request({ url: baseUrl + `/delete/${id}`, method: 'get' })
}
export function getView (id) {
  return request({ url: baseUrl + `/view/${id}`, method: 'get' })
}
