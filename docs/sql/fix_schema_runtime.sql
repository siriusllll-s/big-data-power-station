-- 全项目运行测试发现的表结构补齐脚本（在 slave2 的 photovoltaic 库执行）
-- 用途：代码实体/查询期望的列与旧建表 SQL 不一致，逐一补齐

ALTER TABLE fault_count ADD COLUMN device_name varchar(50) DEFAULT NULL, ADD COLUMN fault_count int DEFAULT NULL;
ALTER TABLE station_forecast ADD COLUMN power_ratio double DEFAULT NULL;
ALTER TABLE device ADD COLUMN station int DEFAULT 1, ADD COLUMN del_flag int DEFAULT 0;
ALTER TABLE threshold ADD COLUMN del_flag int DEFAULT 0;
ALTER TABLE inspection_content ADD COLUMN project_id int DEFAULT NULL, ADD COLUMN name varchar(200) DEFAULT NULL;
ALTER TABLE inspection_item ADD COLUMN project_id int DEFAULT NULL;
ALTER TABLE inspection_manage ADD COLUMN plan_id int DEFAULT NULL, ADD COLUMN point_id int DEFAULT NULL, ADD COLUMN plan_date datetime DEFAULT NULL;
ALTER TABLE inspection_point ADD COLUMN project_id int DEFAULT NULL;
ALTER TABLE work_order ADD COLUMN description varchar(200) DEFAULT NULL, ADD COLUMN create_time datetime DEFAULT NULL, ADD COLUMN update_time datetime DEFAULT NULL;
ALTER TABLE experience ADD COLUMN device_type varchar(50) DEFAULT NULL, ADD COLUMN update_time datetime DEFAULT NULL, ADD COLUMN del_flag int DEFAULT 0;
ALTER TABLE work_order_device ADD COLUMN order_id int DEFAULT NULL, ADD COLUMN device_type int DEFAULT NULL, ADD COLUMN device_name varchar(50) DEFAULT NULL;
ALTER TABLE work_order_user ADD COLUMN order_id int DEFAULT NULL, ADD COLUMN user_id int DEFAULT NULL, ADD COLUMN user_name varchar(50) DEFAULT NULL;
ALTER TABLE work_order_history ADD COLUMN order_id int DEFAULT NULL, ADD COLUMN status int DEFAULT NULL, ADD COLUMN handle_desc varchar(500) DEFAULT NULL, ADD COLUMN handle_user varchar(50) DEFAULT NULL, ADD COLUMN handle_time datetime DEFAULT NULL;
