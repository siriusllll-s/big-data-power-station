import request from '../../utils/request'
const baseUrl = (process.env.NODE_ENV === 'development' ? '/api' : '') + '/monitor'

export function getHistoryData (data) {
  return request({ url: baseUrl + '/historyData', method: 'post', data })
}
export function getStationPowerData (data) {
  return request({ url: baseUrl + '/stationPower', method: 'post', data })
}
export function getInverterData () {
  return request({ url: baseUrl + '/inverter', method: 'get' })
}
export function getMeterData () {
  return request({ url: baseUrl + '/meter', method: 'get' })
}
export function getCombinerBoxData () {
  return request({ url: baseUrl + '/combinerBox', method: 'get' })
}
