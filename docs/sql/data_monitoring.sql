-- 数据监控：阈值设置
CREATE TABLE IF NOT EXISTS threshold (
  id INT AUTO_INCREMENT PRIMARY KEY,
  classification INT COMMENT '0电站1汇流箱2逆变器3直流柜',
  type INT COMMENT '报警类型',
  level INT COMMENT '0低1中2高',
  cycle INT COMMENT '0=10m 1=30m 2=45m 3=1h',
  start_time INT COMMENT '0-23',
  end_time INT COMMENT '0-23',
  is_enable INT DEFAULT 0 COMMENT '0启用1禁用',
  memo VARCHAR(500),
  station INT DEFAULT 1,
  del_flag INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO threshold (classification, type, level, cycle, start_time, end_time, is_enable, memo, station, del_flag)
SELECT 0, 0, 1, 0, 8, 18, 0, '样例：电站效能比', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM threshold WHERE classification=0 AND type=0 LIMIT 1);

INSERT INTO threshold (classification, type, level, cycle, start_time, end_time, is_enable, memo, station, del_flag)
SELECT 2, 3, 2, 1, 6, 20, 0, '样例：逆变器效率', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM threshold WHERE classification=2 AND type=3 LIMIT 1);
