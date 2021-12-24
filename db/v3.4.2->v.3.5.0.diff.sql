SET FOREIGN_KEY_CHECKS=0;

-- 核心库
USE `pig`;

ALTER TABLE `sys_dept` MODIFY COLUMN `dept_id` bigint NOT NULL FIRST;

ALTER TABLE `sys_dept` MODIFY COLUMN `parent_id` bigint NULL DEFAULT NULL COMMENT '父节点' AFTER `del_flag`;

ALTER TABLE `sys_dept_relation` MODIFY COLUMN `ancestor` bigint NOT NULL COMMENT '祖先节点' FIRST;

ALTER TABLE `sys_dept_relation` MODIFY COLUMN `descendant` bigint NOT NULL COMMENT '后代节点' AFTER `ancestor`;

ALTER TABLE `sys_dict` MODIFY COLUMN `id` bigint NOT NULL COMMENT '编号' FIRST;

ALTER TABLE `sys_dict_item` MODIFY COLUMN `id` bigint NOT NULL COMMENT '编号' FIRST;

ALTER TABLE `sys_dict_item` MODIFY COLUMN `dict_id` bigint NOT NULL COMMENT '字典ID' AFTER `id`;

ALTER TABLE `sys_file` MODIFY COLUMN `id` bigint NOT NULL COMMENT '编号' FIRST;

ALTER TABLE `sys_log` MODIFY COLUMN `id` bigint NOT NULL COMMENT '编号' FIRST;

ALTER TABLE `sys_menu` MODIFY COLUMN `menu_id` bigint NOT NULL COMMENT '菜单ID' FIRST;

ALTER TABLE `sys_role` MODIFY COLUMN `role_id` bigint NOT NULL FIRST;

ALTER TABLE `sys_role_menu` MODIFY COLUMN `role_id` bigint NOT NULL COMMENT '角色ID' FIRST;

ALTER TABLE `sys_role_menu` MODIFY COLUMN `menu_id` bigint NOT NULL COMMENT '菜单ID' AFTER `role_id`;

ALTER TABLE `sys_user` MODIFY COLUMN `user_id` bigint NOT NULL COMMENT '主键ID' FIRST;

ALTER TABLE `sys_user` MODIFY COLUMN `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID' AFTER `avatar`;

ALTER TABLE `sys_user_role` MODIFY COLUMN `user_id` bigint NOT NULL COMMENT '用户ID' FIRST;

ALTER TABLE `sys_user_role` MODIFY COLUMN `role_id` bigint NOT NULL COMMENT '角色ID' AFTER `user_id`;

-- 代码生成器
USE `pig_codegen`;

ALTER TABLE `pig_codegen_backup`.`gen_datasource_conf` MODIFY COLUMN `id` bigint NOT NULL COMMENT '主键' FIRST;

ALTER TABLE `pig_codegen_backup`.`gen_form_conf` MODIFY COLUMN `id` bigint NOT NULL COMMENT 'ID' FIRST;

SET FOREIGN_KEY_CHECKS=1;