-- 设备与厂商
CREATE TABLE IF NOT EXISTS device_factory (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  address VARCHAR(255),
  person VARCHAR(64),
  person_tel VARCHAR(32),
  memo VARCHAR(500),
  del_flag INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS device (
  id INT AUTO_INCREMENT PRIMARY KEY,
  no VARCHAR(64),
  name VARCHAR(128),
  type INT,
  factory INT,
  device_address VARCHAR(255),
  specifications VARCHAR(128),
  model VARCHAR(128),
  dai_id VARCHAR(64),
  install_time DATE,
  end_time DATE,
  memo VARCHAR(500),
  station INT DEFAULT 1,
  del_flag INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO device_factory (id, name, address, person, person_tel, memo, del_flag)
SELECT 1, '华为数字能源', '深圳', '张工', '13800000001', '样例厂商', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM device_factory WHERE id=1);
INSERT INTO device_factory (id, name, address, person, person_tel, memo, del_flag)
SELECT 2, '阳光电源', '合肥', '李工', '13800000002', '样例厂商', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM device_factory WHERE id=2);

INSERT INTO device (id, no, name, type, factory, device_address, specifications, model, dai_id, install_time, end_time, memo, station, del_flag)
SELECT 1, 'INV-01', '01号逆变器', 0, 1, '1号箱变侧', '100kW', 'SUN2000', '1001', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '样例', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM device WHERE id=1);
INSERT INTO device (id, no, name, type, factory, device_address, specifications, model, dai_id, install_time, end_time, memo, station, del_flag)
SELECT 2, 'AMM-01', '01号电表', 4, 2, '关口表位', '三相', 'DTSD', '2001', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 5 YEAR), '样例', 1, 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM device WHERE id=2);
