import request from '../../utils/request'

const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/statistics'

/**
 * 电站发电量统计查询
 */
export function getStationKWhPage (data) {
  return request({
    url: baseUrl + '/station',
    method: 'post',
    data: data
  })
}

/**
 * 设备发电量统计查询
 */
export function getDeviceKWhPage (data) {
  return request({
    url: baseUrl + '/device',
    method: 'post',
    data: data
  })
}

/**
 * 损失发电量统计查询
 */
export function getLossKWhPage (data) {
  return request({
    url: baseUrl + '/lossKwh',
    method: 'post',
    data: data
  })
}
