import request from '../../utils/request'

const baseUrl = (process.env.NODE_ENV === "development" ? "/api" : "") + "/stationContract"


/**
 * 合同信息分页查询
 */
export function pageByParam(data) {
  return request({
    url: baseUrl + "/pageByParam",
    method: "post",
    data: data
  })
}


/**
 * 获取合同详细信息
 */

export function getDetail(id){
  return request({
    url:baseUrl+`/detail/${id}`,
    method:"get",
  })
}


/**
 * 合同信息保存
 */
export function save(data) {
  return request({
    url: baseUrl + "/save",
    method: "post",
    data: data
  })
}

/**
 *  合同信息删除
 */
export function del(id) {
  return request({
    url: baseUrl + `/delete/${id}`,
    method: "get",
  })
}
