import request from '../../utils/request'

/**
 * 登录/注册固定走 /api 前缀：
 * - 开发环境：vue.config.js 将 /api/login、/api/register 代理到云服务器
 * - 其它 /api/* 代理到集群本地后端
 */
const AUTH_PREFIX = '/api'

export function login (data) {
  return request({
    url: AUTH_PREFIX + '/login',
    method: 'post',
    data
  })
}

export function register (data) {
  return request({
    url: AUTH_PREFIX + '/register',
    method: 'post',
    data
  })
}
