#!/bin/bash
# 全项目冒烟测试：覆盖 19 个控制器全部只读/分页接口
BASE="http://master:8113"
PASS=0; FAIL=0; FAILED_LIST=()

req() {
  local method=$1 url=$2 body=$3 desc=$4
  if [ -n "$body" ]; then
    local code=$(curl -s -o /tmp/opencode/resp.json -w "%{http_code}" -X $method "$BASE$url" -H "Authorization: $TOKEN" -H "Content-Type: application/json" -d "$body")
  else
    local code=$(curl -s -o /tmp/opencode/resp.json -w "%{http_code}" "$BASE$url" -H "Authorization: $TOKEN")
  fi
  local ok=""
  if [ "$code" = "200" ]; then
    grep -q '"successful":true' /tmp/opencode/resp.json && ok=1
  fi
  if [ -n "$ok" ]; then PASS=$((PASS+1)); echo "PASS  $desc [$method $url]"
  else
    FAIL=$((FAIL+1)); FAILED_LIST+=("$desc [$method $url] code=$code resp=$(head -c 160 /tmp/opencode/resp.json)")
    echo "FAIL  $desc [$method $url] code=$code resp=$(head -c 160 /tmp/opencode/resp.json)"
  fi
}

echo "== 1.登录 =="
TOKEN=$(curl -s -D /tmp/opencode/hdr.txt -o /dev/null -X POST "$BASE/login" -H "Content-Type: application/json" -d '{"name":"admin","password":"123456"}'; awk -F': ' 'tolower($1)=="authorization"{print $2}' /tmp/opencode/hdr.txt | tr -d '\r')
if [ -z "$TOKEN" ]; then echo "!!! 登录失败，中止"; exit 1; fi
echo "token: ${TOKEN:0:20}..."
PASS=$((PASS+1)); echo "PASS  登录"

echo "== 2.用户/字典 =="
req GET /user/list "" "用户列表"
req GET /dictionary/province "" "省列表"
req GET /dictionary/city/1 "" "城市列表"
req GET /dictionary/area/1 "" "区县列表"

echo "== 3.电站管理 =="
req POST /station/pageByParam '{"page":1,"limit":10}' "电站分页"
req GET /station/detail/1 "" "电站详情"
req POST /stationSolarPrice/pageByParam '{"page":1,"limit":10}' "上网电价分页"
req POST /stationContract/pageByParam '{"page":1,"limit":10}' "售电合同分页"
req POST /statistics/station '{"type":1}' "电站统计"

echo "== 4.大屏 =="
req GET /screen/weather "" "大屏天气"
req GET /screen/stationLastThirtyDayPower/1 "" "近30天发电"
req GET /screen/stationDayAndYearPower/1 "" "日/年发电"
req GET /screen/stationMonthKWhStatistic/1 "" "月度发电统计"
req GET /screen/stationAllAndAverage "" "电站汇总与平均"
req GET /screen/stationMonthPower "" "月度发电(全部站)"
req GET /screen/stationTypePower "" "各类型电站发电"
req GET /screen/stationNextThirtyDayPower/1 "" "未来30天预测"
req GET /screen/stationFaultCount/1 "" "故障统计"
req POST /screen/ammeter '{}' "电表数据"

echo "== 5.设备管理 =="
req POST /device/pageByParam '{"page":1,"limit":10}' "设备分页"
req GET /device/deviceList "" "设备列表"
req GET /device/deviceByType/1 "" "按类型查设备"
req GET /device/listByType/1 "" "按类型查设备2"
req POST /factory/pageByParam '{"page":1,"limit":10}' "设备厂家分页"
req GET /factory/factoryList "" "厂家列表"

echo "== 6.实时监控 =="
req GET /monitor/inverter "" "逆变器实时"
req GET /monitor/combinerBox "" "汇流箱实时"
req GET /monitor/meter "" "电表实时"
req POST /monitor/historyData '{"page":1,"limit":10}' "历史/实时数据"
req POST /monitor/stationPower '{"page":1,"limit":10}' "电站天发电量"
req POST /threshold/pageByParam '{"page":1,"limit":10}' "阈值分页"

echo "== 7.巡检运维 =="
req GET /inspection/projectList "" "巡检项目列表"
req GET /inspection/itemList/1 "" "巡检项目详情"
req GET /inspection/manageList "" "巡检管理列表"
req POST /inspectionPlan/pageByParam '{"page":1,"limit":10}' "巡检计划分页"
req GET /inspectionPoint/pointList "" "巡检点列表"
req POST /inspectionPoint/point/pageByParam '{"page":1,"limit":10}' "巡检点分页"
req POST /workerOrder/pageByParam '{"page":1,"limit":10}' "工单分页"

echo "== 8.报表/查询 =="
req POST /powerDataReport/pageByParam '{"page":1,"limit":10}' "日报分页"
req POST /powerDataReport/generate '{"station":1,"start":"2026-09-30"}' "手动生成日报"
req POST /data/exceptionPage '{"page":1,"limit":10}' "异常/故障分页"
req POST /experience/pageByParam '{"page":1,"limit":10}' "经验分页"

echo ""
echo "=============================="
echo "PASS=$PASS  FAIL=$FAIL"
if [ ${#FAILED_LIST[@]} -gt 0 ]; then
  echo "--- 失败明细 ---"
  for f in "${FAILED_LIST[@]}"; do echo "  $f"; done
fi
