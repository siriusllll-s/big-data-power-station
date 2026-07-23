import request from '../../utils/request'
const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/screen'
const stationId = 1
export function getStationAllAndAverage () {
  return request({ url: baseUrl + '/stationAllAndAverage', method: 'get' })
}
export function getStationMonthPower () {
  return request({ url: baseUrl + '/stationMonthPower', method: 'get' })
}
export function getStationTypePower () {
  return request({ url: baseUrl + '/stationTypePower', method: 'get' })
}
export function getStationDayAndYearPower () {
  return request({ url: baseUrl + '/stationDayAndYearPower/' + stationId, method: 'get' })
}
export function getStationLastThirtyDayPower () {
  return request({ url: baseUrl + '/stationLastThirtyDayPower/' + stationId, method: 'get' })
}
export function getStationFaultCount () {
  return request({ url: baseUrl + '/stationFaultCount/' + stationId, method: 'get' })
}
export function getStationMonthKWhStatistic () {
  return request({ url: baseUrl + '/stationMonthKWhStatistic/' + stationId, method: 'get' })
}
export function getStationNextThirtyDayPower () {
  return request({ url: baseUrl + '/stationNextThirtyDayPower/' + stationId, method: 'get' })
}
export function getWeather () {
  return request({ url: baseUrl + '/weather', method: 'get' })
}
export function getAmmeterData (data) {
  return request({ url: baseUrl + '/ammeter', method: 'post', data: data })
}
