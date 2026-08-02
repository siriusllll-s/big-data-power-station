#!/usr/bin/env python
# -*- coding: utf-8 -*-
# 用法: jsonval.py <expr>  (expr 如 ['successful'] 或 ['resultValue']['name'])
# 从 stdin 读取 JSON，expr 为基于 d 的表达式
import sys, json
expr = sys.argv[1]
try:
    d = json.load(sys.stdin)
except Exception:
    sys.exit(1)
try:
    v = eval('d' + expr)
    if isinstance(v, bool):
        s = 'true' if v else 'false'
    elif v is None:
        s = ''
    elif isinstance(v, unicode):
        sys.stdout.write(v.encode('utf-8') + '\n')
        sys.exit(0)
    else:
        s = str(v)
    sys.stdout.write(s.encode('utf-8') + '\n')
except Exception:
    sys.exit(1)
