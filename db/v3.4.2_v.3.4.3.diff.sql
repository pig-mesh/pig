SET FOREIGN_KEY_CHECKS=0;

-- 核心库
USE `pig`;

-- 修正老数据

update `sys_dept` set create_by=' ' where  create_by is null ;

update `sys_dept` set update_by=' ' where  update_by is null ;

update `sys_dept` set create_time=NOW() where  create_time is null ;

update `sys_dept` set update_time=NOW() where  update_time is null ;

update `sys_dict` set create_by=' ' where  create_by is null ;

update `sys_dict` set update_by=' ' where  update_by is null ;

update `sys_dict` set create_time=NOW() where  create_time is null ;

update `sys_dict` set update_time=NOW() where  update_time is null ;

UPDATE `sys_dict` SET `type` = 'dict_type', `description` = '字典类型', `remarks` = '', `system` = '1', `del_flag` = '0', `create_time` = '2019-05-16 14:16:20', `create_by` = '', `update_by` = 'admin', `update_time` = '2021-12-29 12:29:18' WHERE `id` = 1;

UPDATE `sys_dict` SET `type` = 'log_type', `description` = '日志类型', `remarks` = '', `system` = '1', `del_flag` = '0', `create_time` = '2020-03-13 14:21:01', `create_by` = '', `update_by` = 'admin', `update_time` = '2021-12-29 12:30:14' WHERE `id` = 2;

UPDATE `sys_dict` SET `type` = 'ds_type', `description` = '驱动类型', `remarks` = '', `system` = '1', `del_flag` = '0', `create_time` = '2021-10-15 16:24:35', `create_by` = '', `update_by` = 'admin', `update_time` = '2021-12-29 12:30:18' WHERE `id` = 3;

update `sys_menu` set create_by=' ' where  create_by is null ;

update `sys_menu` set update_by=' ' where  update_by is null ;

update `sys_menu` set create_time=NOW() where  create_time is null ;

update `sys_menu` set update_time=NOW() where  update_time is null ;

-- 变更表结构

alter table sys_dept modify dept_id BIGINT;

alter table sys_dept modify parent_id BIGINT;


ALTER TABLE `sys_dept` ADD COLUMN `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序' AFTER `name`;

ALTER TABLE `sys_dept` DROP COLUMN `sort`;

alter table `sys_dept_relation` modify ancestor BIGINT;

alter table `sys_dept_relation` modify descendant BIGINT;

alter table `sys_dict` modify id BIGINT;

ALTER TABLE `sys_dict` ADD COLUMN `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '备注' AFTER `description`;

ALTER TABLE `sys_dict` ADD COLUMN `system_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否是系统内置' AFTER `remark`;

ALTER TABLE `sys_dict` DROP COLUMN `remarks`;
ALTER TABLE `sys_dict` DROP COLUMN `remarks`;

ALTER TABLE `sys_dict` DROP COLUMN `system`;

alter table `sys_dict_item` modify id BIGINT;

ALTER TABLE `sys_dict_item` ADD COLUMN `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序（升序）' AFTER `description`;

ALTER TABLE `sys_dict_item` DROP COLUMN `sort`;

ALTER TABLE `sys_dict_item` DROP COLUMN `remarks`;

alter table `sys_file` modify id BIGINT;

alter table `sys_log` modify id BIGINT;

ALTER TABLE `sys_log` MODIFY COLUMN `time` bigint COMMENT '执行时间' AFTER `params`;

alter table `sys_menu` modify menu_id BIGINT;

ALTER TABLE `sys_menu` ADD COLUMN `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序值' AFTER `icon`;

ALTER TABLE `sys_menu` DROP COLUMN `sort`;

ALTER TABLE `sys_menu` DROP COLUMN `component`;

alter table `sys_role` modify role_id BIGINT;

alter table `sys_role_menu` modify role_id BIGINT;

alter table `sys_role_menu` modify menu_id BIGINT;

alter table `sys_user` modify user_id BIGINT;

alter table `sys_user` modify dept_id BIGINT;

alter table `sys_user_role` modify user_id BIGINT;

alter table `sys_user_role` modify role_id BIGINT;

-- 代码生成器
USE `pig_codegen`;

alter table `gen_datasource_conf` modify id BIGINT;

alter table `gen_form_conf` modify id BIGINT;

SET FOREIGN_KEY_CHECKS=1;
