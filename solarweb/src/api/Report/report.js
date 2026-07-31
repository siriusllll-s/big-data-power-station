import request from '../../utils/request'

const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/powerDataReport'

/**
 * 电站运行日报分页查询
 */
export function getDayReportPage (data) {
  return request({
    url: baseUrl + '/pageByParam',
    method: 'post',
    data: data
  })
}

/**
 * 电站运行日报详情
 */
export function getDayReportDetail (id) {
  return request({
    url: baseUrl + `/detail/${id}`,
    method: 'get'
  })
}

/**
 * 电站运行日报修改
 */
export function editDayReport (data) {
  return request({
    url: baseUrl + '/update',
    method: 'post',
    data: data
  })
}

/**
 * 手动生成日报（实验）
 */
export function generateDayReport (data) {
  return request({
    url: baseUrl + '/generate',
    method: 'post',
    data: data
  })
}
