import request from '../../utils/request'

const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/station'
const stationId = 1

/**
 * 获取电站信息（含经纬度）
 */
export function getStationDetail () {
  return request({
    url: baseUrl + '/detail/' + stationId,
    method: 'get'
  })
}

/**
 * 电站信息保存
 */
export function save (data) {
  return request({
    url: baseUrl + '/save',
    method: 'post',
    data: data
  })
}

/**
 * 上传文件
 */
export function upload (data) {
  return request({
    url: (process.env.NODE_ENV === 'development' ? '/api' : '') + '/minio/upload',
    method: 'post',
    data: data
  })
}

/**
 * 电站图片保存
 */
export function savePhoto (data) {
  return request({
    url: baseUrl + '/savePhoto',
    method: 'post',
    data: data
  })
}
