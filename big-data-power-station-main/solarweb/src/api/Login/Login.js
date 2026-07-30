import request from '../../utils/request'
import { modulesToMenu } from '@/config/modules'

const AUTH_PREFIX = '/api'
// 后端未就绪时本地联调；后端可用后改为 false
const USE_MOCK = true

function mockLogin (data) {
  const name = (data && (data.name || data.username)) || 'admin'
  const password = (data && data.password) || ''
  if (!name || !password) {
    return Promise.reject(new Error('用户名或密码不能为空'))
  }
  if (!(name === 'admin' && password === '123456') && password.length < 4) {
    return Promise.reject(new Error('账号或密码错误（测试账号 admin/123456）'))
  }
  const token = 'mock-token-' + Date.now()
  return Promise.resolve({
    successful: true,
    resultHint: 'ok',
    resultValue: {
      id: 1,
      name: name,
      trueName: name === 'admin' ? '管理员' : name,
      token: token,
      menuList: modulesToMenu(),
      authList: [{ code: 'ALL' }]
    }
  })
}

export function login (data) {
  if (USE_MOCK) return mockLogin(data)
  return request({
    url: AUTH_PREFIX + '/login',
    method: 'post',
    data
  }).catch(function () { return mockLogin(data) })
}

export function register (data) {
  if (USE_MOCK) {
    return mockLogin({ name: data.name, password: data.password })
  }
  return request({
    url: AUTH_PREFIX + '/register',
    method: 'post',
    data
  })
}
