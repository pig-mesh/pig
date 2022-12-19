
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
