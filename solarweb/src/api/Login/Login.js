import request from '../../utils/request'

const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/login'

/**
 * 登录
 */
export function login (data) {
  return request({
    url: baseUrl,
    method: 'post',
    data: data
  })
}
