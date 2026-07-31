-- 电站运行日报表
-- mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/power_data_report.sql

CREATE TABLE IF NOT EXISTS `power_data_report` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `station` INT DEFAULT NULL COMMENT '电站id',
  `report_date` DATE DEFAULT NULL COMMENT '日报日期',
  `weather` VARCHAR(64) DEFAULT NULL COMMENT '天气',
  `kwh` DOUBLE DEFAULT NULL COMMENT '当日发电量kWh',
  `radiation` DOUBLE DEFAULT NULL COMMENT '当日辐照量kWh/m2',
  `power_ratio` DOUBLE DEFAULT NULL COMMENT '发电效率%',
  `summary` VARCHAR(1000) DEFAULT NULL COMMENT '运行总结',
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_station_date` (`station`, `report_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电站运行日报';

-- 样例（无 k_wh_station 时也可先看页面）
INSERT INTO `power_data_report` (
  `station`, `report_date`, `weather`, `kwh`, `radiation`, `power_ratio`, `summary`, `create_time`, `update_time`
)
SELECT 1, CURDATE(), '晴', 1250.5, 5.2, 82.3, CONCAT(DATE_FORMAT(CURDATE(), '%Y-%m-%d'), ' 样例日报'), NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (
  SELECT 1 FROM `power_data_report` WHERE station = 1 AND report_date = CURDATE()
);
