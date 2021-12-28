SET FOREIGN_KEY_CHECKS=0;

-- 核心库
USE `pig`;

-- 修正老数据

update `sys_dept` set create_by='' where  create_by is null ;

update `sys_dept` set update_by='' where  update_by is null ;

update `sys_dept` set create_time=NOW() where  create_time is null ;

update `sys_dept` set update_time=NOW() where  update_time is null ;

update `sys_dict` set create_by='' where  create_by is null ;

update `sys_dict` set update_by='' where  update_by is null ;

update `sys_dict` set create_time=NOW() where  create_time is null ;

update `sys_dict` set update_time=NOW() where  update_time is null ;

UPDATE `sys_dict` SET `type` = 'dict_type', `description` = '字典类型', `remarks` = '', `system` = '1', `del_flag` = '0', `create_time` = '2019-05-16 14:16:20', `create_by` = '', `update_by` = 'admin', `update_time` = '2021-12-29 12:29:18' WHERE `id` = 1;

UPDATE `sys_dict` SET `type` = 'log_type', `description` = '日志类型', `remarks` = '', `system` = '1', `del_flag` = '0', `create_time` = '2020-03-13 14:21:01', `create_by` = '', `update_by` = 'admin', `update_time` = '2021-12-29 12:30:14' WHERE `id` = 2;

UPDATE `sys_dict` SET `type` = 'ds_type', `description` = '驱动类型', `remarks` = '', `system` = '1', `del_flag` = '0', `create_time` = '2021-10-15 16:24:35', `create_by` = '', `update_by` = 'admin', `update_time` = '2021-12-29 12:30:18' WHERE `id` = 3;

update `sys_menu` set create_by='' where  create_by is null ;

update `sys_menu` set update_by='' where  update_by is null ;

update `sys_menu` set create_time=NOW() where  create_time is null ;

update `sys_menu` set update_time=NOW() where  update_time is null ;

-- 变更表结构

ALTER TABLE `sys_dept` ADD COLUMN `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序' AFTER `name`;

ALTER TABLE `sys_dept` MODIFY COLUMN `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称' AFTER `dept_id`;

ALTER TABLE `sys_dept` MODIFY COLUMN `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常' AFTER `sort_order`;

ALTER TABLE `sys_dept` MODIFY COLUMN `parent_id` bigint NOT NULL COMMENT '父节点' AFTER `del_flag`;

ALTER TABLE `sys_dept` MODIFY COLUMN `create_time` datetime NOT NULL COMMENT '创建时间' AFTER `parent_id`;

ALTER TABLE `sys_dept` MODIFY COLUMN `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人' AFTER `create_time`;

ALTER TABLE `sys_dept` MODIFY COLUMN `update_time` datetime NOT NULL COMMENT '修改时间' AFTER `create_by`;

ALTER TABLE `sys_dept` MODIFY COLUMN `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人' AFTER `update_time`;

ALTER TABLE `sys_dept` DROP COLUMN `sort`;

ALTER TABLE `sys_dict` ADD COLUMN `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注' AFTER `description`;

ALTER TABLE `sys_dict` ADD COLUMN `system_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否是系统内置' AFTER `remark`;

ALTER TABLE `sys_dict` MODIFY COLUMN `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型' AFTER `id`;

ALTER TABLE `sys_dict` MODIFY COLUMN `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述' AFTER `type`;

ALTER TABLE `sys_dict` MODIFY COLUMN `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标记' AFTER `system_flag`;

ALTER TABLE `sys_dict` MODIFY COLUMN `create_time` datetime NOT NULL COMMENT '创建时间' AFTER `del_flag`;

ALTER TABLE `sys_dict` MODIFY COLUMN `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人' AFTER `create_time`;

ALTER TABLE `sys_dict` MODIFY COLUMN `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人' AFTER `create_by`;

ALTER TABLE `sys_dict` MODIFY COLUMN `update_time` datetime NOT NULL COMMENT '更新时间' AFTER `update_by`;

ALTER TABLE `sys_dict` DROP COLUMN `remarks`;

ALTER TABLE `sys_dict` DROP COLUMN `system`;

ALTER TABLE `sys_dict_item` ADD COLUMN `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序（升序）' AFTER `description`;

ALTER TABLE `sys_dict_item` ADD COLUMN `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注' AFTER `sort_order`;

ALTER TABLE `sys_dict_item` MODIFY COLUMN `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '值' AFTER `dict_id`;

ALTER TABLE `sys_dict_item` MODIFY COLUMN `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标签' AFTER `value`;

ALTER TABLE `sys_dict_item` MODIFY COLUMN `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型' AFTER `label`;

ALTER TABLE `sys_dict_item` MODIFY COLUMN `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '描述' AFTER `type`;

ALTER TABLE `sys_dict_item` DROP COLUMN `sort`;

ALTER TABLE `sys_dict_item` DROP COLUMN `remarks`;

ALTER TABLE `sys_file` MODIFY COLUMN `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL AFTER `id`;

ALTER TABLE `sys_file` MODIFY COLUMN `bucket_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL AFTER `file_name`;

ALTER TABLE `sys_file` MODIFY COLUMN `original` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL AFTER `bucket_name`;

ALTER TABLE `sys_file` MODIFY COLUMN `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL AFTER `original`;

ALTER TABLE `sys_file` MODIFY COLUMN `file_size` bigint NOT NULL COMMENT '文件大小' AFTER `type`;

ALTER TABLE `sys_file` MODIFY COLUMN `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '0-正常，1-删除' AFTER `file_size`;

ALTER TABLE `sys_file` MODIFY COLUMN `create_time` datetime NOT NULL COMMENT '创建时间' AFTER `del_flag`;

ALTER TABLE `sys_file` MODIFY COLUMN `update_time` datetime NOT NULL COMMENT '修改时间' AFTER `create_time`;

ALTER TABLE `sys_file` MODIFY COLUMN `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建者' AFTER `update_time`;

ALTER TABLE `sys_file` MODIFY COLUMN `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人' AFTER `create_by`;

ALTER TABLE `sys_log` MODIFY COLUMN `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '日志类型' AFTER `id`;

ALTER TABLE `sys_log` MODIFY COLUMN `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '日志标题' AFTER `type`;

ALTER TABLE `sys_log` MODIFY COLUMN `service_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '服务ID' AFTER `title`;

ALTER TABLE `sys_log` MODIFY COLUMN `remote_addr` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作IP地址' AFTER `service_id`;

ALTER TABLE `sys_log` MODIFY COLUMN `user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '用户代理' AFTER `remote_addr`;

ALTER TABLE `sys_log` MODIFY COLUMN `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '请求URI' AFTER `user_agent`;

ALTER TABLE `sys_log` MODIFY COLUMN `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '操作方式' AFTER `request_uri`;

ALTER TABLE `sys_log` MODIFY COLUMN `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '操作提交的数据' AFTER `method`;

ALTER TABLE `sys_log` MODIFY COLUMN `time` bigint NOT NULL DEFAULT 0 COMMENT '执行时间' AFTER `params`;

ALTER TABLE `sys_log` MODIFY COLUMN `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标记' AFTER `time`;

ALTER TABLE `sys_log` MODIFY COLUMN `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '异常信息' AFTER `del_flag`;

ALTER TABLE `sys_log` MODIFY COLUMN `create_time` datetime NOT NULL COMMENT '创建时间' AFTER `exception`;

ALTER TABLE `sys_log` MODIFY COLUMN `update_time` datetime NOT NULL COMMENT '更新时间' AFTER `create_time`;

ALTER TABLE `sys_log` MODIFY COLUMN `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人' AFTER `update_time`;

ALTER TABLE `sys_log` MODIFY COLUMN `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人' AFTER `create_by`;

ALTER TABLE `sys_menu` ADD COLUMN `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序值' AFTER `icon`;

ALTER TABLE `sys_menu` MODIFY COLUMN `keep_alive` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '0-开启，1- 关闭' AFTER `sort`;

ALTER TABLE `sys_menu` MODIFY COLUMN `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单类型 （0菜单 1按钮）' AFTER `keep_alive`;

ALTER TABLE `sys_menu` MODIFY COLUMN `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '逻辑删除标记(0--正常 1--删除)' AFTER `type`;

ALTER TABLE `sys_menu` MODIFY COLUMN `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '创建人' AFTER `del_flag`;

ALTER TABLE `sys_menu` MODIFY COLUMN `create_time` datetime NOT NULL COMMENT '创建时间' AFTER `create_by`;

ALTER TABLE `sys_menu` MODIFY COLUMN `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '修改人' AFTER `create_time`;

ALTER TABLE `sys_menu` MODIFY COLUMN `update_time` datetime NOT NULL COMMENT '更新时间' AFTER `update_by`;

ALTER TABLE `sys_menu` DROP COLUMN `sort`;

ALTER TABLE `sys_menu` DROP COLUMN `component`;

-- 代码生成器
USE `pig_codegen`;

ALTER TABLE `pig_codegen_backup`.`gen_datasource_conf` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键' FIRST;

ALTER TABLE `pig_codegen_backup`.`gen_form_conf` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'ID' FIRST;

SET FOREIGN_KEY_CHECKS=1;