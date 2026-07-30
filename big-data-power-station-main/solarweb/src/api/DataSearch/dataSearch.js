import request from '../../utils/request'

const baseUrl = (process.env.NODE_ENV === "development" ? "/api" : "") + "/data"

/**
 * 异常列表分页查询
 */
export function getExceptionPage(data) {
  return request({
    url: baseUrl + "/exceptionPage",
    method: "post",
    data: data
  })
}
