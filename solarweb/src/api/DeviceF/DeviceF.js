import request from '../../utils/request'
const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/factory'

export function pageByParam (data) {
  return request({ url: baseUrl + '/pageByParam', method: 'post', data })
}
export function factoryList () {
  return request({ url: baseUrl + '/factoryList', method: 'get' })
}
export function save (data) {
  return request({ url: baseUrl + '/save', method: 'post', data })
}
export function getFactory (id) {
  return request({ url: baseUrl + `/detail/${id}`, method: 'get' })
}
export function delFactory (id) {
  return request({ url: baseUrl + `/delete/${id}`, method: 'get' })
}
