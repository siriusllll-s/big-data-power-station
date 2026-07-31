import request from '../../utils/request'
const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/device'

export function getDeviceListByType (type) {
  return request({
    url: baseUrl + `/listByType/${type}`,
    method: 'get'
  })
}
