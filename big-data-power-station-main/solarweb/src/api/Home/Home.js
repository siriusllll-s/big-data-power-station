import request from '../../utils/request'

const baseUrl = (process.env.NODE_ENV === "development" ? "/api" : "") + "/screen"
const stationId = 1

/**
 * 电站综合及平均发电相关统计
 */
export function getStationAllAndAverage(){
  return request({
    url:baseUrl+"/stationAllAndAverage",
    method:"get",
  })
}

/**
 * 电站本月发电量和本月发电效率
 */
export function getStationMonthPower(){
  return request({
    url:baseUrl+"/stationMonthPower",
    method:"get",
  })
}

/**
 * 各类电站分布数和日发电效率
 */
export function getStationTypePower(){
  return request({
    url:baseUrl+"/stationTypePower",
    method:"get",
  })
}

/**
 * 今日发电量和今年发电量，节能指标
 */

export function getStationDayAndYearPower(){
  return request({
    url:baseUrl+`/stationDayAndYearPower/`+stationId,
    method:"get",
  })
}

/**
 * 电站本月发电量和本月发电效率
 */

export function getStationLastThirtyDayPower(){
  return request({
    url:baseUrl+`/stationLastThirtyDayPower/`+stationId,
    method:"get",
  })
}

/**
 * 电站设备故障统计
 */

export function getStationFaultCount(){
  return request({
    url:baseUrl+`/stationFaultCount/`+stationId,
    method:"get",
  })
}

/**
 * 过去12个月发电量
 */

export function getStationMonthKWhStatistic(){
  return request({
    url:baseUrl+`/stationMonthKWhStatistic/`+stationId,
    method:"get",
  })
}

/**
 * 预测未来30天发电量
 */

export function getStationNextThirtyDayPower(){
  return request({
    url:baseUrl+`/stationNextThirtyDayPower/`+stationId,
    method:"get",
  })
}

/**
 * 天气信息
 */

export function getWeather(){
  return request({
    url:baseUrl+`/weather`,
    method:"get",
  })
}


/**
 * 电表读数查询
 */
export function getAmmeterData(data) {
  return request({
    url: baseUrl + "/ammeter",
    method: "post",
    data: data
  })
}
