CREATE TABLE IF NOT EXISTS experience (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200),
  device_type VARCHAR(64),
  content TEXT,
  create_time DATETIME,
  update_time DATETIME,
  del_flag INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS fault (
  id INT AUTO_INCREMENT PRIMARY KEY,
  station INT,
  device_name VARCHAR(128),
  device_type VARCHAR(64),
  fault_desc VARCHAR(1000),
  fault_level INT,
  fault_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO experience (title, device_type, content, create_time, update_time, del_flag)
SELECT '逆变器过温处理', '逆变器', '检查散热风扇与滤网，必要时降容运行。', NOW(), NOW(), 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM experience WHERE title='逆变器过温处理');

INSERT INTO fault (station, device_name, device_type, fault_desc, fault_level, fault_time)
SELECT 1, '01号逆变器', 'ammeter', '样例异常：输出波动', 1, NOW()
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM fault LIMIT 1);
