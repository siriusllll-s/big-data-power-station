import request from '../../utils/request'

const baseUrl = (process.env.NODE_ENV === "development" ? "/api" : "") + "/stationSolarPrice"


/**
 * 电价信息分页查询
 */
export function pageByParam(data) {
  return request({
    url: baseUrl + "/pageByParam",
    method: "post",
    data: data
  })
}


/**
 * 获取电价详细信息
 */

export function getPriceDetail(id){
  return request({
    url:baseUrl+`/detail/${id}`,
    method:"get",
  })
}


/**
 * 电价信息保存
 */
export function save(data) {
  return request({
    url: baseUrl + "/save",
    method: "post",
    data: data
  })
}

/**
 *  电价信息删除
 */
export function delPrice(id) {
  return request({
    url: baseUrl + `/delete/${id}`,
    method: "get",
  })
}
