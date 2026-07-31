import request from '../../utils/request'
const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/experience'
export function pageExperience (data) {
  return request({ url: baseUrl + '/pageByParam', method: 'post', data })
}
export function saveExperience (data) {
  return request({ url: baseUrl + '/save', method: 'post', data })
}
export function delExperience (id) {
  return request({ url: baseUrl + `/delete/${id}`, method: 'get' })
}
