import request from '../../utils/request'

/** 业务接口：集群本地后端 */
const baseUrl = '/api'

export function changePassword (data) {
  return request({
    url: baseUrl + '/user/changePassword',
    method: 'post',
    data
  })
}

export function loginOut () {
  return request({
    url: baseUrl + '/user/logout',
    method: 'post'
  }).catch(() => {})
}
