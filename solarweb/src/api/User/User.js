import request from '../../utils/request'
const baseUrl = '/api'

export function changePassword (data) {
  return request({ url: baseUrl + '/user/changePassword', method: 'post', data })
}
export function loginOut () {
  return request({ url: baseUrl + '/user/logout', method: 'post' }).catch(() => {})
}
export function listUsers () {
  return request({ url: baseUrl + '/user/list', method: 'get' })
}
