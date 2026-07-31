import request from '../../utils/request'

const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/dictionary'

/**
 * 省份列表
 */
export function getProvinceList () {
  return request({
    url: baseUrl + '/province',
    method: 'get'
  })
}

/**
 * 城市列表
 * @param {number} provinceId 省份 id
 */
export function getCityList (provinceId) {
  return request({
    url: baseUrl + '/city/' + provinceId,
    method: 'get'
  })
}

/**
 * 区县列表
 * @param {number} cityId 城市 id
 */
export function getAreaList (cityId) {
  return request({
    url: baseUrl + '/area/' + cityId,
    method: 'get'
  })
}
