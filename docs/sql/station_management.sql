-- 电站管理：电价表 + 合同表（MySQL / 库 photovoltaic）
-- 在 slave2 或本地执行：mysql -uroot -p123456 photovoltaic < docs/sql/station_management.sql

CREATE TABLE IF NOT EXISTS `station_solar_price` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `station` INT DEFAULT NULL COMMENT '电站id',
  `price` DOUBLE DEFAULT NULL COMMENT '电价(元)',
  `begin_date` DATE DEFAULT NULL COMMENT '实施日期',
  `memo` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `del_flag` INT DEFAULT 0 COMMENT '0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_station_date` (`station`, `begin_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电站电价';

CREATE TABLE IF NOT EXISTS `station_contract` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `station` INT DEFAULT NULL COMMENT '电站id',
  `no` VARCHAR(64) DEFAULT NULL COMMENT '合同编号',
  `begin_date` DATE DEFAULT NULL COMMENT '合同开始',
  `end_date` DATE DEFAULT NULL COMMENT '合同结束',
  `contract_power` DOUBLE DEFAULT NULL COMMENT '合同发电量kWh',
  `protocol_pr` DOUBLE DEFAULT NULL COMMENT '协议效能比%',
  `efficiency` DOUBLE DEFAULT NULL COMMENT '模拟发电效率%',
  `avg_radio` DOUBLE DEFAULT NULL COMMENT '年均辐照Wh/m2',
  `memo` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `del_flag` INT DEFAULT 0 COMMENT '0正常 1删除',
  PRIMARY KEY (`id`),
  KEY `idx_station_date` (`station`, `begin_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电站合同';

-- 样例数据（电站 id=1）
INSERT INTO `station_solar_price` (`station`, `price`, `begin_date`, `memo`, `del_flag`)
SELECT 1, 0.42, CURDATE(), '样例电价', 0
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `station_solar_price` WHERE station = 1 AND del_flag = 0 LIMIT 1);

INSERT INTO `station_contract` (
  `station`, `no`, `begin_date`, `end_date`,
  `contract_power`, `protocol_pr`, `efficiency`, `avg_radio`, `memo`, `del_flag`
)
SELECT 1, 'HT-2026-001', DATE_FORMAT(CURDATE(), '%Y-01-01'), DATE_FORMAT(CURDATE(), '%Y-12-31'),
       1200000, 80, 85, 1450, '样例合同', 0
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM `station_contract` WHERE station = 1 AND del_flag = 0 LIMIT 1);
