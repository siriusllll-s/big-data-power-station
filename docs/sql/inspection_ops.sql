-- 运维管理：巡检 + 工单
-- mysql -h slave2 -uroot -p123456 photovoltaic < docs/sql/inspection_ops.sql

CREATE TABLE IF NOT EXISTS inspection_project (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  del_flag INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inspection_content (
  id INT AUTO_INCREMENT PRIMARY KEY,
  project_id INT,
  name VARCHAR(128),
  del_flag INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inspection_item (
  id INT AUTO_INCREMENT PRIMARY KEY,
  content_id INT,
  name VARCHAR(128),
  del_flag INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inspection_point (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  project_id INT,
  station INT DEFAULT 1,
  memo VARCHAR(500),
  del_flag INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inspection_point_item (
  id INT AUTO_INCREMENT PRIMARY KEY,
  point_id INT,
  item_id INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inspection_point_device (
  id INT AUTO_INCREMENT PRIMARY KEY,
  point_id INT,
  device_type INT,
  device_name VARCHAR(128)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inspection_plan (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(128) NOT NULL,
  begin_date DATE,
  end_date DATE,
  station INT DEFAULT 1,
  memo VARCHAR(500),
  del_flag INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inspection_plan_point (
  id INT AUTO_INCREMENT PRIMARY KEY,
  plan_id INT,
  point_id INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inspection_plan_user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  plan_id INT,
  user_id INT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS inspection_manage (
  id INT AUTO_INCREMENT PRIMARY KEY,
  plan_id INT,
  point_id INT,
  plan_date DATE,
  status INT DEFAULT 0 COMMENT '0未巡检1巡检中2已巡检3未完成',
  name VARCHAR(128)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS work_order (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200),
  station INT DEFAULT 1,
  status INT DEFAULT 1 COMMENT '1新建2处理中3已解决4关闭',
  type INT DEFAULT 1 COMMENT '0自动1手动',
  device_type INT,
  exception_time DATETIME,
  forecast_time DATETIME,
  description VARCHAR(1000),
  create_time DATETIME,
  update_time DATETIME,
  del_flag INT DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS work_order_device (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT,
  device_type INT,
  device_name VARCHAR(128)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS work_order_user (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT,
  user_id INT,
  user_name VARCHAR(64)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS work_order_history (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT,
  status INT,
  handle_desc VARCHAR(1000),
  handle_user VARCHAR(64),
  handle_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- seed projects
INSERT INTO inspection_project (id, name, del_flag)
SELECT 1, '逆变器巡检', 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_project WHERE id=1);
INSERT INTO inspection_project (id, name, del_flag)
SELECT 2, '汇流箱巡检', 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_project WHERE id=2);
INSERT INTO inspection_project (id, name, del_flag)
SELECT 3, '电表巡检', 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_project WHERE id=3);

INSERT INTO inspection_content (id, project_id, name, del_flag)
SELECT 1, 1, '外观检查', 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_content WHERE id=1);
INSERT INTO inspection_content (id, project_id, name, del_flag)
SELECT 2, 1, '运行参数', 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_content WHERE id=2);
INSERT INTO inspection_content (id, project_id, name, del_flag)
SELECT 3, 2, '接线检查', 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_content WHERE id=3);

INSERT INTO inspection_item (id, content_id, name, del_flag)
SELECT 1, 1, '外壳有无破损', 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_item WHERE id=1);
INSERT INTO inspection_item (id, content_id, name, del_flag)
SELECT 2, 1, '指示灯是否正常', 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_item WHERE id=2);
INSERT INTO inspection_item (id, content_id, name, del_flag)
SELECT 3, 2, '输出功率', 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_item WHERE id=3);
INSERT INTO inspection_item (id, content_id, name, del_flag)
SELECT 4, 3, '端子紧固', 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_item WHERE id=4);

INSERT INTO inspection_point (id, name, project_id, station, memo, del_flag)
SELECT 1, '1号逆变器巡检点', 1, 1, '样例', 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_point WHERE id=1);
INSERT INTO inspection_point_item (point_id, item_id)
SELECT 1, 1 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_point_item WHERE point_id=1 AND item_id=1);
INSERT INTO inspection_point_device (point_id, device_type, device_name)
SELECT 1, 0, '01号逆变器' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_point_device WHERE point_id=1 LIMIT 1);

INSERT INTO inspection_plan (id, name, begin_date, end_date, station, memo, del_flag)
SELECT 1, '本月日常巡检', DATE_FORMAT(CURDATE(),'%Y-%m-01'), LAST_DAY(CURDATE()), 1, '样例计划', 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_plan WHERE id=1);
INSERT INTO inspection_plan_point (plan_id, point_id)
SELECT 1, 1 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_plan_point WHERE plan_id=1 AND point_id=1);
INSERT INTO inspection_plan_user (plan_id, user_id)
SELECT 1, 1 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_plan_user WHERE plan_id=1 AND user_id=1);

INSERT INTO inspection_manage (plan_id, point_id, plan_date, status, name)
SELECT 1, 1, CURDATE(), 0, '1号逆变器巡检点'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM inspection_manage WHERE plan_id=1 AND plan_date=CURDATE() AND point_id=1);

INSERT INTO work_order (id, title, station, status, type, device_type, exception_time, forecast_time, description, create_time, update_time, del_flag)
SELECT 1, '样例故障工单-逆变器告警', 1, 1, 1, 0, NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), '实验样例', NOW(), NOW(), 0
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM work_order WHERE id=1);
INSERT INTO work_order_device (order_id, device_type, device_name)
SELECT 1, 0, '01号逆变器' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM work_order_device WHERE order_id=1 LIMIT 1);
INSERT INTO work_order_user (order_id, user_id, user_name)
SELECT 1, 1, 'admin' FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM work_order_user WHERE order_id=1 LIMIT 1);
