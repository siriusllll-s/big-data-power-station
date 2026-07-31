import request from '../../utils/request'
const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/inspectionPoint/point'
const baseUrl2 = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/inspection'
const baseUrl3 = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/inspectionPoint'

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
export function getItemListByProject (projectId) {
  return request({ url: baseUrl2 + `/itemList/${projectId}`, method: 'get' })
}
export function getProjectList () {
  return request({ url: baseUrl2 + '/projectList', method: 'get' })
}
export function getPointList () {
  return request({ url: baseUrl3 + '/pointList', method: 'get' })
}
export function getManageList () {
  return request({ url: baseUrl2 + '/manageList', method: 'get' })
}
export function getInfo (id) {
  return request({ url: baseUrl + `/view/${id}`, method: 'get' })
}
