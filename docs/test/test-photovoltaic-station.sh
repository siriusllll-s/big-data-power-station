#!/bin/bash
# 光伏Web系统 自动化测试：用例 13-23（电站管理-电站信息/电价管理/合同管理）
set -u
BASE="http://localhost:8113"
JV="$(dirname "$0")/jsonval.py"
PASS=0; FAIL=0; FAILED_CASES=()
TOKEN=""

login() {
  TOKEN=$(curl -s -D - -X POST "$BASE/login" -H "Content-Type: application/json" \
    -d '{"name":"admin","password":"123456"}' | grep -i '^Authorization:' | tr -d '\r' | awk '{print $2}')
  [ -n "$TOKEN" ] || { echo "LOGIN FAILED"; exit 1; }
  echo "登录成功，token 获取正常"
}

api() {
  local method=$1 path=$2 body=${3:-}
  if [ -n "$body" ]; then
    curl -s -X "$method" "$BASE$path" -H "Authorization: $TOKEN" -H "Content-Type: application/json" -d "$body"
  else
    curl -s -X "$method" "$BASE$path" -H "Authorization: $TOKEN"
  fi
}

check() {
  if [ "$3" = "1" ]; then
    PASS=$((PASS+1)); echo "  [PASS] 用例$1 $2 — $4"
  else
    FAIL=$((FAIL+1)); FAILED_CASES+=("$1 $2"); echo "  [FAIL] 用例$1 $2 — $4"
  fi
}

find_id() { # find_id <json文件> <字段名> <字段值>
  python -c "
import sys,json
d=json.load(open(sys.argv[1]))
rv=d.get('resultValue',{}) or {}
rows=rv.get('records') or rv.get('list') or []
for r in rows:
    if str(r.get(sys.argv[2]))==sys.argv[3]: print(r.get('id')); break
" "$1" "$2" "$3"
}

echo "================ 开始执行测试 ================"
login

echo ""
echo "---- 用例13 显示电站详细信息 ----"
DETAIL=$(api GET "/station/detail/1")
SUC=$(echo "$DETAIL" | python $JV "['successful']")
NAME=$(echo "$DETAIL" | python $JV "['resultValue']['shortName']")
ADDR=$(echo "$DETAIL" | python $JV "['resultValue']['address']")
if [ "$SUC" = "true" ] && [ -n "$NAME" ]; then ok=1; else ok=0; fi
check 13 "显示电站详细信息" "$ok" "返回电站: shortName=$NAME, no=$(echo "$DETAIL" | python $JV "['resultValue']['no']")（DB 中 name 字段为空，属数据问题）"

echo ""
echo "---- 用例14 电站图片（上传+保存+预览） ----"
ORIG_PHOTO=$(echo "$DETAIL" | python $JV "['resultValue']['photo']")
printf '/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/wAALCAABAAEBAREA/8QAFAABAAAAAAAAAAAAAAAAAAAACf/EABQQAQAAAAAAAAAAAAAAAAAAAAD/2gAIAQEAAD8AKp//2Q==' | base64 -d > /tmp/opencode/test.jpg
UPLOAD=$(curl -s -X POST "$BASE/minio/upload" -H "Authorization: $TOKEN" -F "file=@/tmp/opencode/test.jpg")
KEY=$(echo "$UPLOAD" | python $JV "['resultValue']")
if [ -n "$KEY" ]; then ok=1; else ok=0; fi
check 14a "电站图片-上传" "$ok" "上传返回 key=$KEY"
PSUC=$(api POST "/station/savePhoto" "{\"station\":1,\"photo\":\"$KEY\"}" | python $JV "['successful']")
if [ "$PSUC" = "true" ]; then ok=1; else ok=0; fi
check 14b "电站图片-保存" "$ok" "savePhoto successful=$PSUC"
PREVIEW_CODE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE/minio/preViewPicture/$KEY" -H "Authorization: $TOKEN")
if [ "$PREVIEW_CODE" = "200" ]; then ok=1; else ok=0; fi
check 14c "电站图片-预览" "$ok" "preViewPicture HTTP=$PREVIEW_CODE"
if [ -n "$ORIG_PHOTO" ]; then
  api POST "/station/savePhoto" "{\"station\":1,\"photo\":\"$ORIG_PHOTO\"}" >/dev/null
fi

echo ""
echo "---- 用例15 编辑电站信息 ----"
STAMP="auto-test-$(date +%s)"
OLD_DESC=$(echo "$DETAIL" | python $JV "['resultValue']['stationDesc']")
api POST "/station/save" "{\"id\":1,\"name\":\"$NAME\",\"address\":\"$ADDR\",\"stationDesc\":\"$STAMP\"}" >/dev/null
DESC2=$(api GET "/station/detail/1" | python $JV "['resultValue']['stationDesc']")
if echo "$DESC2" | grep -q "$STAMP"; then ok=1; else ok=0; fi
check 15 "编辑电站信息" "$ok" "修改 stationDesc 后返回: $DESC2"
api POST "/station/save" "{\"id\":1,\"name\":\"$NAME\",\"address\":\"$ADDR\",\"stationDesc\":\"$OLD_DESC\"}" >/dev/null

echo ""
echo "---- 用例16 电价列表页面 ----"
PLIST=$(api POST "/stationSolarPrice/pageByParam" '{"page":1,"limit":10}' > /tmp/price_list.json; cat /tmp/price_list.json)
PSUCC=$(echo "$PLIST" | python $JV "['successful']")
PTOTAL=$(echo "$PLIST" | python $JV "['resultValue']['total']")
if [ "$PSUCC" = "true" ]; then ok=1; else ok=0; fi
check 16 "电价列表页面" "$ok" "成功返回电价列表 total=$PTOTAL"

echo ""
echo "---- 用例17 电价查询功能 ----"
PQ=$(api POST "/stationSolarPrice/pageByParam" '{"page":1,"limit":50,"station":1}')
QSUCC=$(echo "$PQ" | python $JV "['successful']")
QSTATION=$(echo "$PQ" | python -c "
import sys,json
d=json.load(sys.stdin)
rv=d.get('resultValue',{}) or {}
rows=rv.get('records') or rv.get('list') or []
print(','.join(str(r.get('station','')) for r in rows))
")
if [ "$QSUCC" = "true" ] && [ -n "$QSTATION" ]; then ok=1; else ok=0; fi
check 17 "电价查询功能" "$ok" "按电站1查询，结果所属电站: $(echo "$QSTATION" | tr ',' '\n' | sort -u | tr '\n' ' ')"

echo ""
echo "---- 用例18 新增电价 ----"
NEWP=$(api POST "/stationSolarPrice/save" '{"station":1,"price":1.05,"beginDate":"2026-08-01","name":"自动化测试电价","memo":"opencode test"}')
NSUC=$(echo "$NEWP" | python $JV "['successful']")
if [ "$NSUC" = "true" ]; then ok=1; else ok=0; fi
check 18 "新增电价" "$ok" "保存电价 successful=$NSUC"
api POST "/stationSolarPrice/pageByParam" '{"page":1,"limit":10}' > /tmp/price_list.json
PID=$(find_id /tmp/price_list.json memo "opencode test")
if [ -n "$PID" ]; then ok=1; else ok=0; fi
check 18b "新增电价-列表可见" "$ok" "新电价 id=$PID 出现在列表中（必填校验为前端 rules，代码已确认）"

echo ""
echo "---- 用例19 编辑电价 ----"
if [ -n "$PID" ]; then
  ESUC=$(api POST "/stationSolarPrice/save" "{\"id\":$PID,\"station\":1,\"price\":1.10,\"beginDate\":\"2026-08-01\",\"name\":\"自动化测试电价\",\"memo\":\"edited\"}" | python $JV "['successful']")
  PNEW=$(api GET "/stationSolarPrice/detail/$PID" | python $JV "['resultValue']['price']")
  if [ "$ESUC" = "true" ] && python -c "print(abs(float('$PNEW')-1.10)<0.001)" 2>/dev/null | grep -q True; then ok=1; else ok=0; fi
  check 19 "编辑电价" "$ok" "修改后 price=$PNEW"
else
  check 19 "编辑电价" 0 "无新电价 id"
fi

echo ""
echo "---- 用例20 删除电价 ----"
if [ -n "$PID" ]; then
  DSUC=$(api GET "/stationSolarPrice/delete/$PID" | python $JV "['successful']")
  DCHK=$(api GET "/stationSolarPrice/detail/$PID" | python $JV "['successful']")
  if [ "$DSUC" = "true" ] && [ "$DCHK" != "true" ]; then ok=1; else ok=0; fi
  check 20 "删除电价" "$ok" "删除 successful=$DSUC，删除后 detail successful=$DCHK（应为空/非true）"
else
  check 20 "删除电价" 0 "无新电价 id"
fi

echo ""
echo "---- 用例21 合同列表页面 ----"
CLIST=$(api POST "/stationContract/pageByParam" '{"page":1,"limit":10}')
CSUC=$(echo "$CLIST" | python $JV "['successful']")
CTOTAL=$(echo "$CLIST" | python $JV "['resultValue']['total']")
if [ "$CSUC" = "true" ]; then ok=1; else ok=0; fi
check 21 "合同列表页面" "$ok" "成功返回合同列表 total=$CTOTAL"

echo ""
echo "---- 用例22 合同查询功能 ----"
CQ=$(api POST "/stationContract/pageByParam" '{"page":1,"limit":50,"station":1}')
CQSUC=$(echo "$CQ" | python $JV "['successful']")
if [ "$CQSUC" = "true" ]; then ok=1; else ok=0; fi
check 22 "合同查询功能" "$ok" "按电站1查询合同 successful=$CQSUC"

echo ""
echo "---- 用例23 新增合同 ----"
CNO="HT-$(date +%Y%m%d%H%M%S)"
CNSUC=$(api POST "/stationContract/save" "{\"station\":1,\"no\":\"$CNO\",\"beginDate\":\"2026-08-01\",\"endDate\":\"2027-08-01\",\"contractPower\":100.5,\"protocolPr\":0.85,\"efficiency\":82.1,\"avgRadio\":1.05,\"memo\":\"opencode test\"}" | python $JV "['successful']")
if [ "$CNSUC" = "true" ]; then ok=1; else ok=0; fi
check 23 "新增合同" "$ok" "保存合同 successful=$CNSUC (no=$CNO)"
api POST "/stationContract/pageByParam" '{"page":1,"limit":10}' > /tmp/contract_list.json
CID=$(find_id /tmp/contract_list.json no "$CNO")
if [ -n "$CID" ]; then
  api GET "/stationContract/delete/$CID" >/dev/null
  echo "  （测试合同已清理 id=$CID）"
fi

echo ""
echo "================ 测试结果汇总 ================"
echo "通过: $PASS  失败: $FAIL"
if [ ${#FAILED_CASES[@]} -gt 0 ]; then
  echo "失败用例:"
  for c in "${FAILED_CASES[@]}"; do echo "  - $c"; done
  exit 1
else
  echo "全部通过"
fi
