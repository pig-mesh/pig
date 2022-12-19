use pigxx;

-- ----------------------------
-- Table structure for sys_tenant_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_menu`;
CREATE TABLE `sys_tenant_menu`  (
                                    `id` bigint(0) NOT NULL,
                                    `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '租户菜单名称',
                                    `menu_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单id集合',
                                    `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '租户菜单,9:冻结,0:正常',
                                    `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
                                    `create_by` varchar(64) CHARACTER SET utf8bm4 COLLATE utf8bm4_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
                                    `update_by` varchar(64) CHARACTER SET utf8bm4 COLLATE utf8bm4_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
                                    `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建',
                                    `update_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_tenant_menu
-- ----------------------------
INSERT INTO `sys_tenant_menu` VALUES (1601104338798153729, '1231', '1000,1100,1101,1102,1103,1104,1200,1201,1202,1203,1300,1301,1302,1303,1304,1305,1400,1401,1402,1403,1404,1500,1501,1502,1503,1600,1601,1602,1603,1604,1605,1601095373834067969,1601095530717814785,1601095569972305921,1601095611131011073', '0', '0', 'admin', ' ', '2022-12-09 14:39:56', '2022-12-09 06:39:56');




INSERT INTO `sys_route_conf` VALUES (14, 'APP服务', 'pigx-app-server', '[{\"args\": {\"_genkey_0\": \"/app/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-app-server-biz', 0, '{}', 'admin', ' ', '2022-12-07 10:53:44', NULL, '0');



INSERT INTO `sys_dict_item` VALUES (67, 17, 'mobile', 'app', 'grant_types', 'App登录', 5, 'admin', ' ', '2022-12-09 09:54:49', '2022-12-19 10:43:14', NULL, '0', 1);


INSERT INTO `sys_menu` VALUES (1600312638941048834, 'APP管理', NULL, '/app', -1, 'icon-bangzhushouji', '1', 999, '0', '0', 'admin', '2022-12-07 10:14:00', 'admin', '2022-12-11 10:32:23', '0', 1);
INSERT INTO `sys_menu` VALUES (1600313409464053762, '客户管理', NULL, '/app/appuser/index', 1600312638941048834, 'icon-yonghuguanli', '1', 1, '0', '0', 'admin', '2022-12-07 10:17:04', 'admin', '2022-12-11 10:32:34', '0', 1);
INSERT INTO `sys_menu` VALUES (1600323070510682114, '新增用户', 'app_appuser_add', NULL, 1600313409464053762, NULL, '1', 999, '0', '1', 'admin', '2022-12-07 10:55:27', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600323121857351681, '编辑用户', 'app_appuser_edit', NULL, 1600313409464053762, NULL, '1', 999, '0', '1', 'admin', '2022-12-07 10:55:39', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600323168514789378, '删除用户', 'app_appuser_del', NULL, 1600313409464053762, NULL, '1', 999, '0', '1', 'admin', '2022-12-07 10:55:51', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600323219744018434, '导出用户', 'app_appuser_export', NULL, 1600313409464053762, NULL, '1', 999, '0', '1', 'admin', '2022-12-07 10:56:03', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600376918277279745, '角色管理', NULL, '/app/approle/index', 1600312638941048834, 'icon-jiaoseguanli', '1', 2, '0', '0', 'admin', '2022-12-07 14:29:26', 'admin', '2022-12-11 10:32:40', '0', 1);
INSERT INTO `sys_menu` VALUES (1600377095700533249, '删除角色', 'app_approle_del', NULL, 1600376918277279745, NULL, '1', 999, '0', '1', 'admin', '2022-12-07 14:30:08', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600377182417768449, '编辑角色', 'app_approle_edit', NULL, 1600376918277279745, NULL, '1', 999, '0', '1', 'admin', '2022-12-07 14:30:29', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600377222876024833, '新增角色', 'app_approle_add', NULL, 1600376918277279745, NULL, '1', 999, '0', '1', 'admin', '2022-12-07 14:30:38', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600377308532101121, '到处角色', 'app_approle_export', NULL, 1600376918277279745, NULL, '1', 999, '0', '1', 'admin', '2022-12-07 14:30:59', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600652908694007810, '权限管理', NULL, '/app/appmenu/index', 1600312638941048834, 'icon-caidanguanli', '1', 3, '0', '0', 'admin', '2022-12-08 08:46:07', 'admin', '2022-12-11 10:32:46', '0', 1);
INSERT INTO `sys_menu` VALUES (1600653042983038977, '新增权限', 'app_appmenu_add', NULL, 1600652908694007810, NULL, '1', 999, '0', '1', 'admin', '2022-12-08 08:46:39', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600653088008892418, '修改权限', 'app_appmenu_edit', NULL, 1600652908694007810, NULL, '1', 999, '0', '1', 'admin', '2022-12-08 08:46:50', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600653127523430402, '删除权限', 'app_appmenu_del', NULL, 1600652908694007810, NULL, '1', 999, '0', '1', 'admin', '2022-12-08 08:46:59', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600720444353658882, '角色授权', 'app_approle_perm', NULL, 1600376918277279745, NULL, '1', 999, '0', '1', 'admin', '2022-12-08 13:14:29', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1601051690644447233, '秘钥管理', NULL, '/app/appsocial/index', 1600312638941048834, 'icon-denglvlingpai', '1', 999, '0', '0', 'admin', '2022-12-09 11:10:44', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1601051800774287362, '删除秘钥', 'app_social_details_del', NULL, 1601051690644447233, NULL, '1', 999, '0', '1', 'admin', '2022-12-09 11:11:10', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1601051844994834433, '修改秘钥', 'app_social_details_edit', NULL, 1601051690644447233, NULL, '1', 999, '0', '1', 'admin', '2022-12-09 11:11:21', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1601051895175487489, '保存秘钥', 'app_social_details_add', NULL, 1601051690644447233, NULL, '1', 999, '0', '1', 'admin', '2022-12-09 11:11:33', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1601095373834067969, '租户菜单', NULL, '/admin/tenantmenu/index', 1000, 'icon-caidanguanli', '0', 7, '0', '0', 'admin', '2022-12-09 14:04:19', 'admin', '2022-12-12 09:00:04', '1', 1);
INSERT INTO `sys_menu` VALUES (1601095530717814785, '租户菜单新增', 'admin_systenantmenu_add', NULL, 1602106377770541057, NULL, '1', 1, '0', '1', 'admin', '2022-12-09 14:04:19', 'admin', '2022-12-12 09:02:00', '0', 1);
INSERT INTO `sys_menu` VALUES (1601095569972305921, '租户菜单编辑', 'admin_systenantmenu_edit', NULL, 1602106377770541057, NULL, '1', 999, '0', '1', 'admin', '2022-12-09 14:04:19', 'admin', '2022-12-12 09:02:11', '0', 1);
INSERT INTO `sys_menu` VALUES (1601095611131011073, '租户菜单删除', 'admin_systenantmenu_del', NULL, 1602106377770541057, NULL, '1', 999, '0', '1', 'admin', '2022-12-09 14:04:19', 'admin', '2022-12-12 09:02:06', '0', 1);
INSERT INTO `sys_menu` VALUES (1602106377770541057, '租户菜单', 'admin_systenant_tenantmenu', NULL, 1500, NULL, '1', 999, '0', '1', 'admin', '2022-12-12 09:01:41', ' ', NULL, '0', 1);


ALTER TABLE `sys_tenant`
    ADD COLUMN `menu_id` bigint NULL COMMENT '租户菜单id' AFTER `update_time`;



use pigxx_bi;

ALTER TABLE jimu_report_data_source
    ADD COLUMN type  varchar(10) NULL COMMENT '类型(report:报表;drag:仪表盘)';
UPDATE jimu_report_data_source SET type= 'report';


ALTER TABLE  jimu_report_data_source
    ADD COLUMN tenant_id varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '多租户标识' AFTER connect_times;

ALTER TABLE jimu_dict
    ADD COLUMN tenant_id varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '多租户标识' AFTER type;

ALTER TABLE jimu_report
    ADD COLUMN tenant_id varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '多租户标识' AFTER js_str;




create database `pigxx_app` default character set utf8mb4 collate utf8mb4_general_ci;


USE pigxx_app;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app_menu
-- ----------------------------
DROP TABLE IF EXISTS `app_menu`;
CREATE TABLE `app_menu`  (
                             `menu_id` bigint(0) NOT NULL COMMENT '菜单ID',
                             `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `parent_id` bigint(0) NULL DEFAULT NULL COMMENT '父菜单ID',
                             `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '1' COMMENT '是否显示',
                             `sort_order` int(0) NULL DEFAULT 1 COMMENT '排序值',
                             `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
                             `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
                             `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                             `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
                             `tenant_id` bigint(0) UNSIGNED NULL DEFAULT NULL COMMENT '租户ID',
                             PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app_menu
-- ----------------------------
INSERT INTO `app_menu` VALUES (1600718710482857986, '主页', NULL, 'pages/sys/home/index', -1, '1', 999, '0', ' ', '2022-12-08 05:07:35', ' ', NULL, '0', 1);
INSERT INTO `app_menu` VALUES (1600718795732086785, '待办任务', 'pages_oa_todos', 'pages/oa/todos', 1600718710482857986, '1', 999, '0', '', '2022-12-08 05:07:55', '', '2022-12-09 07:34:40', '0', 1);
INSERT INTO `app_menu` VALUES (1600718867454685185, '发起任务', 'pages_oa_form', 'pages/oa/form', 1600718710482857986, '1', 999, '0', '', '2022-12-08 05:08:12', '', '2022-12-09 07:34:57', '0', 1);
INSERT INTO `app_menu` VALUES (1600718957435088898, '请假列表', 'pages_oa_history', 'pages/oa/history', 1600718710482857986, '1', 999, '0', '', '2022-12-08 05:08:34', '', '2022-12-09 07:35:10', '0', 1);
INSERT INTO `app_menu` VALUES (1600719297945464834, '用户管理', 'pages_sys_pigxUser', 'pages/sys/pigxUser/index', 1600718710482857986, '1', 999, '0', '', '2022-12-08 05:09:55', '', '2022-12-09 07:35:38', '0', 1);
INSERT INTO `app_menu` VALUES (1601118484814061570, '令牌管理', 'pages_sys_token', 'pages/sys/token/index', 1600718710482857986, '1', 999, '0', '', '2022-12-08 05:09:55', '', '2022-12-09 07:36:19', '0', 1);
INSERT INTO `app_menu` VALUES (1601118643937566721, '角色管理', 'pages_sys_pigxRole', 'pages/sys/pigxRole/index', 1600718710482857986, '1', 999, '0', ' ', '2022-12-08 05:09:55', ' ', NULL, '0', 1);
INSERT INTO `app_menu` VALUES (1601118838641352706, '岗位管理', 'pages_sys_pigxPost', 'pages/sys/pigxPost/index', 1600718710482857986, '1', 999, '0', ' ', '2022-12-08 05:09:55', ' ', NULL, '0', 1);
INSERT INTO `app_menu` VALUES (1601118961773535234, '租户管理', 'pages_sys_pigxTenant', 'pages/sys/pigxTenant/index', 1600718710482857986, '1', 999, '0', ' ', '2022-12-08 05:09:55', ' ', NULL, '0', 1);
INSERT INTO `app_menu` VALUES (1601119066337533954, '参数管理', 'pages_sys_pigxParam', 'pages/sys/pigxParam/index', 1600718710482857986, '1', 999, '0', ' ', '2022-12-08 05:09:55', ' ', NULL, '0', 1);

-- ----------------------------
-- Table structure for app_role
-- ----------------------------
DROP TABLE IF EXISTS `app_role`;
CREATE TABLE `app_role`  (
                             `role_id` bigint(0) NOT NULL,
                             `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
                             `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
                             `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
                             `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
                             `tenant_id` bigint(0) NULL DEFAULT NULL,
                             PRIMARY KEY (`role_id`) USING BTREE,
                             INDEX `role_idx1_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'app角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app_role
-- ----------------------------
INSERT INTO `app_role` VALUES (1600378144758894594, 'app用户', 'APP_USER', 'app用户角色', ' ', ' ', '2022-12-07 06:34:18', NULL, '0', 1);

-- ----------------------------
-- Table structure for app_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `app_role_menu`;
CREATE TABLE `app_role_menu`  (
                                  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
                                  `menu_id` bigint(0) NOT NULL COMMENT '菜单ID',
                                  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app_role_menu
-- ----------------------------
INSERT INTO `app_role_menu` VALUES (1, 1600718710482857986);
INSERT INTO `app_role_menu` VALUES (1, 1600718795732086785);
INSERT INTO `app_role_menu` VALUES (1, 1600718867454685185);
INSERT INTO `app_role_menu` VALUES (1, 1600718957435088898);
INSERT INTO `app_role_menu` VALUES (1, 1600719297945464834);
INSERT INTO `app_role_menu` VALUES (2, 1600718710482857986);
INSERT INTO `app_role_menu` VALUES (2, 1600718957435088898);
INSERT INTO `app_role_menu` VALUES (1600378144758894594, 1600718710482857986);
INSERT INTO `app_role_menu` VALUES (1600378144758894594, 1600718957435088898);
INSERT INTO `app_role_menu` VALUES (1600378144758894594, 1600719297945464834);
INSERT INTO `app_role_menu` VALUES (1600378144758894594, 1601118484814061570);
INSERT INTO `app_role_menu` VALUES (1600378144758894594, 1601118643937566721);
INSERT INTO `app_role_menu` VALUES (1600378144758894594, 1601118838641352706);

-- ----------------------------
-- Table structure for app_social_details
-- ----------------------------
DROP TABLE IF EXISTS `app_social_details`;
CREATE TABLE `app_social_details`  (
                                       `id` bigint(0) NOT NULL COMMENT '主鍵',
                                       `type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                       `remark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                       `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                       `app_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                       `redirect_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                                       `ext` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拓展字段',
                                       `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
                                       `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
                                       `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                                       `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
                                       `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
                                       `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '所属租户',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统社交登录账号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app_social_details
-- ----------------------------
INSERT INTO `app_social_details` VALUES (1, 'MINI', '小程序登录', 'wx6832be859d0e1cf5', '', NULL, NULL, '', 'admin', '2022-12-09 01:44:42', '2022-12-09 11:12:29', '0', 1);

-- ----------------------------
-- Table structure for app_user
-- ----------------------------
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user`  (
                             `user_id` bigint(0) NOT NULL,
                             `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                             `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拓展字段:昵称',
                             `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拓展字段:姓名',
                             `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拓展字段:邮箱',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
                             `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
                             `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
                             `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
                             `tenant_id` bigint(0) NOT NULL DEFAULT 0 COMMENT '所属租户',
                             `last_modified_time` datetime(0) NULL DEFAULT NULL COMMENT '最后一次密码修改时间',
                             `lock_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '锁定状态',
                             `wx_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信登录openId',
                             PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'app用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app_user
-- ----------------------------
INSERT INTO `app_user` VALUES (1600324121922027521, 'aeizzz', '$2a$10$d5mw9aUzBhxDqknpxIWxdOJ2xdhkaPNConNu4cU8JNhTrruyjPYL2', NULL, '13054729089', NULL, 'aeizzz', '刘洪磊', 'aeizzz@foxmail.com', '', 'aeizzz', '2022-12-07 02:59:38', '2022-12-09 10:55:23', '0', 1, NULL, '0', 'oBxPy5EnbDiN-gGEaovCpp_IkrkQ');

-- ----------------------------
-- Table structure for app_user_role
-- ----------------------------
DROP TABLE IF EXISTS `app_user_role`;
CREATE TABLE `app_user_role`  (
                                  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                  `role_id` bigint(0) NOT NULL COMMENT '角色ID',
                                  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app_user_role
-- ----------------------------
INSERT INTO `app_user_role` VALUES (1600324121922027521, 1600378144758894594);

SET FOREIGN_KEY_CHECKS = 1;

