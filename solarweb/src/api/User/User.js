import request from '../../utils/request'

const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '')

export function changePassword (data) {
  return request({
    url: baseUrl + '/user/changePassword',
    method: 'post',
    data: data
  })
}

export function loginOut () {
  return request({
    url: baseUrl + '/user/logout',
    method: 'post'
  }).catch(() => {})
}
