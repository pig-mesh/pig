DROP DATABASE IF EXISTS `pig`;

CREATE DATABASE  `pig` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `pig`;


-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门名称',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `parent_id` bigint DEFAULT NULL COMMENT '父级部门ID',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES (1, '总裁办', 1, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:07:49', '0', 0);
INSERT INTO `sys_dept` VALUES (2, '技术部', 2, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1);
INSERT INTO `sys_dept` VALUES (3, '市场部', 3, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1);
INSERT INTO `sys_dept` VALUES (4, '销售部', 4, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1);
INSERT INTO `sys_dept` VALUES (5, '财务部', 5, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1);
INSERT INTO `sys_dept` VALUES (6, '人事行政部', 6, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:53:36', '1', 1);
INSERT INTO `sys_dept` VALUES (7, '研发部', 7, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 2);
INSERT INTO `sys_dept` VALUES (8, 'UI设计部', 11, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 7);
INSERT INTO `sys_dept` VALUES (9, '产品部', 12, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 2);
INSERT INTO `sys_dept` VALUES (10, '渠道部', 13, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 3);
INSERT INTO `sys_dept` VALUES (11, '推广部', 14, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 3);
INSERT INTO `sys_dept` VALUES (12, '客服部', 15, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 4);
INSERT INTO `sys_dept` VALUES (13, '财务会计部', 16, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 5);
INSERT INTO `sys_dept` VALUES (14, '审计风控部', 17, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 14:06:57', '0', 5);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL COMMENT '编号',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `system_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '系统标志',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_dict_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES (1, 'log_type', '日志类型', ' ', ' ', '2019-03-19 11:06:44', '2019-03-19 11:06:44', '异常、正常', '1', '0');
INSERT INTO `sys_dict` VALUES (2, 'social_type', '社交登录', ' ', ' ', '2019-03-19 11:09:44', '2019-03-19 11:09:44', '微信、QQ', '1', '0');
INSERT INTO `sys_dict` VALUES (3, 'job_type', '定时任务类型', ' ', ' ', '2019-03-19 11:22:21', '2019-03-19 11:22:21', 'quartz', '1', '0');
INSERT INTO `sys_dict` VALUES (4, 'job_status', '定时任务状态', ' ', ' ', '2019-03-19 11:24:57', '2019-03-19 11:24:57', '发布状态、运行状态', '1', '0');
INSERT INTO `sys_dict` VALUES (5, 'job_execute_status', '定时任务执行状态', ' ', ' ', '2019-03-19 11:26:15', '2019-03-19 11:26:15', '正常、异常', '1', '0');
INSERT INTO `sys_dict` VALUES (6, 'misfire_policy', '定时任务错失执行策略', ' ', ' ', '2019-03-19 11:27:19', '2019-03-19 11:27:19', '周期', '1', '0');
INSERT INTO `sys_dict` VALUES (7, 'gender', '性别', ' ', ' ', '2019-03-27 13:44:06', '2019-03-27 13:44:06', '微信用户性别', '1', '0');
INSERT INTO `sys_dict` VALUES (8, 'subscribe', '订阅状态', ' ', ' ', '2019-03-27 13:48:33', '2019-03-27 13:48:33', '公众号订阅状态', '1', '0');
INSERT INTO `sys_dict` VALUES (9, 'response_type', '回复', ' ', ' ', '2019-03-28 21:29:21', '2019-03-28 21:29:21', '微信消息是否已回复', '1', '0');
INSERT INTO `sys_dict` VALUES (10, 'param_type', '参数配置', ' ', ' ', '2019-04-29 18:20:47', '2019-04-29 18:20:47', '检索、原文、报表、安全、文档、消息、其他', '1', '0');
INSERT INTO `sys_dict` VALUES (11, 'status_type', '租户状态', ' ', ' ', '2019-05-15 16:31:08', '2019-05-15 16:31:08', '租户状态', '1', '0');
INSERT INTO `sys_dict` VALUES (12, 'dict_type', '字典类型', ' ', ' ', '2019-05-16 14:16:20', '2019-05-16 14:20:16', '系统类不能修改', '1', '0');
INSERT INTO `sys_dict` VALUES (13, 'channel_type', '支付类型', ' ', ' ', '2019-05-16 14:16:20', '2019-05-16 14:20:16', '系统类不能修改', '1', '0');
INSERT INTO `sys_dict` VALUES (14, 'grant_types', '授权类型', ' ', ' ', '2019-08-13 07:34:10', '2019-08-13 07:34:10', NULL, '1', '0');
INSERT INTO `sys_dict` VALUES (15, 'style_type', '前端风格', ' ', ' ', '2020-02-07 03:49:28', '2020-02-07 03:50:40', '0-Avue 1-element', '1', '0');
INSERT INTO `sys_dict` VALUES (16, 'captcha_flag_types', '验证码开关', ' ', ' ', '2020-11-18 06:53:25', '2020-11-18 06:53:25', '是否校验验证码', '1', '0');
INSERT INTO `sys_dict` VALUES (17, 'enc_flag_types', '前端密码加密', ' ', ' ', '2020-11-18 06:54:44', '2020-11-18 06:54:44', '前端密码是否加密传输', '1', '0');
INSERT INTO `sys_dict` VALUES (18, 'lock_flag', '用户状态', 'admin', ' ', '2023-02-01 16:55:31', NULL, NULL, '1', '0');
INSERT INTO `sys_dict` VALUES (19, 'ds_config_type', '数据连接类型', 'admin', ' ', '2023-02-06 18:36:59', NULL, NULL, '1', '0');
INSERT INTO `sys_dict` VALUES (20, 'common_status', '通用状态', 'admin', ' ', '2023-02-09 11:02:08', NULL, NULL, '1', '0');
INSERT INTO `sys_dict` VALUES (21, 'app_social_type', 'app社交登录', 'admin', ' ', '2023-02-10 11:11:06', NULL, 'app社交登录', '1', '0');
INSERT INTO `sys_dict` VALUES (22, 'yes_no_type', '是否', 'admin', ' ', '2023-02-20 23:25:04', NULL, NULL, '1', '0');
INSERT INTO `sys_dict` VALUES (23, 'repType', '微信消息类型', 'admin', ' ', '2023-02-24 15:08:25', NULL, NULL, '0', '0');
INSERT INTO `sys_dict` VALUES (24, 'leave_status', '请假状态', 'admin', ' ', '2023-03-02 22:50:15', NULL, NULL, '0', '0');
INSERT INTO `sys_dict` VALUES (25, 'schedule_type', '日程类型', 'admin', ' ', '2023-03-06 14:49:18', NULL, NULL, '0', '0');
INSERT INTO `sys_dict` VALUES (26, 'schedule_status', '日程状态', 'admin', ' ', '2023-03-06 14:52:57', NULL, NULL, '0', '0');
INSERT INTO `sys_dict` VALUES (27, 'ds_type', '代码生成器支持的数据库类型', 'admin', ' ', '2023-03-12 09:57:59', NULL, NULL, '1', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` bigint NOT NULL COMMENT '编号',
  `dict_id` bigint NOT NULL COMMENT '字典ID',
  `item_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典项值',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典项名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典项描述',
  `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序（升序）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_dict_value` (`item_value`) USING BTREE,
  KEY `sys_dict_label` (`label`) USING BTREE,
  KEY `sys_dict_item_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典项';

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_item` VALUES (1, 1, '9', '异常', 'log_type', '日志异常', 1, ' ', ' ', '2019-03-19 11:08:59', '2019-03-25 12:49:13', '', '0');
INSERT INTO `sys_dict_item` VALUES (2, 1, '0', '正常', 'log_type', '日志正常', 0, ' ', ' ', '2019-03-19 11:09:17', '2019-03-25 12:49:18', '', '0');
INSERT INTO `sys_dict_item` VALUES (3, 2, 'WX', '微信', 'social_type', '微信登录', 0, ' ', ' ', '2019-03-19 11:10:02', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (4, 2, 'QQ', 'QQ', 'social_type', 'QQ登录', 1, ' ', ' ', '2019-03-19 11:10:14', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (5, 3, '1', 'java类', 'job_type', 'java类', 1, ' ', ' ', '2019-03-19 11:22:37', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (6, 3, '2', 'spring bean', 'job_type', 'spring bean容器实例', 2, ' ', ' ', '2019-03-19 11:23:05', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (7, 3, '9', '其他', 'job_type', '其他类型', 9, ' ', ' ', '2019-03-19 11:23:31', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (8, 3, '3', 'Rest 调用', 'job_type', 'Rest 调用', 3, ' ', ' ', '2019-03-19 11:23:57', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (9, 3, '4', 'jar', 'job_type', 'jar类型', 4, ' ', ' ', '2019-03-19 11:24:20', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (10, 4, '1', '未发布', 'job_status', '未发布', 1, ' ', ' ', '2019-03-19 11:25:18', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (11, 4, '2', '运行中', 'job_status', '运行中', 2, ' ', ' ', '2019-03-19 11:25:31', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (12, 4, '3', '暂停', 'job_status', '暂停', 3, ' ', ' ', '2019-03-19 11:25:42', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (13, 5, '0', '正常', 'job_execute_status', '正常', 0, ' ', ' ', '2019-03-19 11:26:27', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (14, 5, '1', '异常', 'job_execute_status', '异常', 1, ' ', ' ', '2019-03-19 11:26:41', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (15, 6, '1', '错失周期立即执行', 'misfire_policy', '错失周期立即执行', 1, ' ', ' ', '2019-03-19 11:27:45', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (16, 6, '2', '错失周期执行一次', 'misfire_policy', '错失周期执行一次', 2, ' ', ' ', '2019-03-19 11:27:57', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (17, 6, '3', '下周期执行', 'misfire_policy', '下周期执行', 3, ' ', ' ', '2019-03-19 11:28:08', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (18, 7, '1', '男', 'gender', '微信-男', 0, ' ', ' ', '2019-03-27 13:45:13', '2019-03-27 13:45:13', '微信-男', '0');
INSERT INTO `sys_dict_item` VALUES (19, 7, '2', '女', 'gender', '女-微信', 1, ' ', ' ', '2019-03-27 13:45:34', '2019-03-27 13:45:34', '女-微信', '0');
INSERT INTO `sys_dict_item` VALUES (20, 7, '0', '未知', 'gender', 'x性别未知', 3, ' ', ' ', '2019-03-27 13:45:57', '2019-03-27 13:45:57', 'x性别未知', '0');
INSERT INTO `sys_dict_item` VALUES (21, 8, '0', '未关注', 'subscribe', '公众号-未关注', 0, ' ', ' ', '2019-03-27 13:49:07', '2019-03-27 13:49:07', '公众号-未关注', '0');
INSERT INTO `sys_dict_item` VALUES (22, 8, '1', '已关注', 'subscribe', '公众号-已关注', 1, ' ', ' ', '2019-03-27 13:49:26', '2019-03-27 13:49:26', '公众号-已关注', '0');
INSERT INTO `sys_dict_item` VALUES (23, 9, '0', '未回复', 'response_type', '微信消息-未回复', 0, ' ', ' ', '2019-03-28 21:29:47', '2019-03-28 21:29:47', '微信消息-未回复', '0');
INSERT INTO `sys_dict_item` VALUES (24, 9, '1', '已回复', 'response_type', '微信消息-已回复', 1, ' ', ' ', '2019-03-28 21:30:08', '2019-03-28 21:30:08', '微信消息-已回复', '0');
INSERT INTO `sys_dict_item` VALUES (25, 10, '1', '检索', 'param_type', '检索', 0, ' ', ' ', '2019-04-29 18:22:17', '2019-04-29 18:22:17', '检索', '0');
INSERT INTO `sys_dict_item` VALUES (26, 10, '2', '原文', 'param_type', '原文', 0, ' ', ' ', '2019-04-29 18:22:27', '2019-04-29 18:22:27', '原文', '0');
INSERT INTO `sys_dict_item` VALUES (27, 10, '3', '报表', 'param_type', '报表', 0, ' ', ' ', '2019-04-29 18:22:36', '2019-04-29 18:22:36', '报表', '0');
INSERT INTO `sys_dict_item` VALUES (28, 10, '4', '安全', 'param_type', '安全', 0, ' ', ' ', '2019-04-29 18:22:46', '2019-04-29 18:22:46', '安全', '0');
INSERT INTO `sys_dict_item` VALUES (29, 10, '5', '文档', 'param_type', '文档', 0, ' ', ' ', '2019-04-29 18:22:56', '2019-04-29 18:22:56', '文档', '0');
INSERT INTO `sys_dict_item` VALUES (30, 10, '6', '消息', 'param_type', '消息', 0, ' ', ' ', '2019-04-29 18:23:05', '2019-04-29 18:23:05', '消息', '0');
INSERT INTO `sys_dict_item` VALUES (31, 10, '9', '其他', 'param_type', '其他', 0, ' ', ' ', '2019-04-29 18:23:16', '2019-04-29 18:23:16', '其他', '0');
INSERT INTO `sys_dict_item` VALUES (32, 10, '0', '默认', 'param_type', '默认', 0, ' ', ' ', '2019-04-29 18:23:30', '2019-04-29 18:23:30', '默认', '0');
INSERT INTO `sys_dict_item` VALUES (33, 11, '0', '正常', 'status_type', '状态正常', 0, ' ', ' ', '2019-05-15 16:31:34', '2019-05-16 22:30:46', '状态正常', '0');
INSERT INTO `sys_dict_item` VALUES (34, 11, '9', '冻结', 'status_type', '状态冻结', 1, ' ', ' ', '2019-05-15 16:31:56', '2019-05-16 22:30:50', '状态冻结', '0');
INSERT INTO `sys_dict_item` VALUES (35, 12, '1', '系统类', 'dict_type', '系统类字典', 0, ' ', ' ', '2019-05-16 14:20:40', '2019-05-16 14:20:40', '不能修改删除', '0');
INSERT INTO `sys_dict_item` VALUES (36, 12, '0', '业务类', 'dict_type', '业务类字典', 0, ' ', ' ', '2019-05-16 14:20:59', '2019-05-16 14:20:59', '可以修改', '0');
INSERT INTO `sys_dict_item` VALUES (37, 2, 'GITEE', '码云', 'social_type', '码云', 2, ' ', ' ', '2019-06-28 09:59:12', '2019-06-28 09:59:12', '码云', '0');
INSERT INTO `sys_dict_item` VALUES (38, 2, 'OSC', '开源中国', 'social_type', '开源中国登录', 2, ' ', ' ', '2019-06-28 10:04:32', '2019-06-28 10:04:32', '', '0');
INSERT INTO `sys_dict_item` VALUES (39, 14, 'password', '密码模式', 'grant_types', '支持oauth密码模式', 0, ' ', ' ', '2019-08-13 07:35:28', '2019-08-13 07:35:28', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (40, 14, 'authorization_code', '授权码模式', 'grant_types', 'oauth2 授权码模式', 1, ' ', ' ', '2019-08-13 07:36:07', '2019-08-13 07:36:07', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (41, 14, 'client_credentials', '客户端模式', 'grant_types', 'oauth2 客户端模式', 2, ' ', ' ', '2019-08-13 07:36:30', '2019-08-13 07:36:30', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (42, 14, 'refresh_token', '刷新模式', 'grant_types', 'oauth2 刷新token', 3, ' ', ' ', '2019-08-13 07:36:54', '2019-08-13 07:36:54', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (43, 14, 'implicit', '简化模式', 'grant_types', 'oauth2 简化模式', 4, ' ', ' ', '2019-08-13 07:39:32', '2019-08-13 07:39:32', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (44, 15, '0', 'Avue', 'style_type', 'Avue风格', 0, ' ', ' ', '2020-02-07 03:52:52', '2020-02-07 03:52:52', '', '0');
INSERT INTO `sys_dict_item` VALUES (45, 15, '1', 'element', 'style_type', 'element-ui', 1, ' ', ' ', '2020-02-07 03:53:12', '2020-02-07 03:53:12', '', '0');
INSERT INTO `sys_dict_item` VALUES (46, 16, '0', '关', 'captcha_flag_types', '不校验验证码', 0, ' ', ' ', '2020-11-18 06:53:58', '2020-11-18 06:53:58', '不校验验证码 -0', '0');
INSERT INTO `sys_dict_item` VALUES (47, 16, '1', '开', 'captcha_flag_types', '校验验证码', 1, ' ', ' ', '2020-11-18 06:54:15', '2020-11-18 06:54:15', '不校验验证码-1', '0');
INSERT INTO `sys_dict_item` VALUES (48, 17, '0', '否', 'enc_flag_types', '不加密', 0, ' ', ' ', '2020-11-18 06:55:31', '2020-11-18 06:55:31', '不加密-0', '0');
INSERT INTO `sys_dict_item` VALUES (49, 17, '1', '是', 'enc_flag_types', '加密', 1, ' ', ' ', '2020-11-18 06:55:51', '2020-11-18 06:55:51', '加密-1', '0');
INSERT INTO `sys_dict_item` VALUES (50, 13, 'MERGE_PAY', '聚合支付', 'channel_type', '聚合支付', 1, ' ', ' ', '2019-05-30 19:08:08', '2019-06-18 13:51:53', '聚合支付', '0');
INSERT INTO `sys_dict_item` VALUES (51, 2, 'CAS', 'CAS登录', 'social_type', 'CAS 单点登录系统', 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (52, 2, 'DINGTALK', '钉钉', 'social_type', '钉钉', 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (53, 2, 'WEIXIN_CP', '企业微信', 'social_type', '企业微信', 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (54, 15, '2', 'APP', 'style_type', 'uview风格', 1, ' ', ' ', '2020-02-07 03:53:12', '2020-02-07 03:53:12', '', '0');
INSERT INTO `sys_dict_item` VALUES (55, 13, 'ALIPAY_WAP', '支付宝支付', 'channel_type', '支付宝支付', 1, ' ', ' ', '2019-05-30 19:08:08', '2019-06-18 13:51:53', '聚合支付', '0');
INSERT INTO `sys_dict_item` VALUES (56, 13, 'WEIXIN_MP', '微信支付', 'channel_type', '微信支付', 1, ' ', ' ', '2019-05-30 19:08:08', '2019-06-18 13:51:53', '聚合支付', '0');
INSERT INTO `sys_dict_item` VALUES (57, 14, 'mobile', 'mobile', 'grant_types', '移动端登录', 5, 'admin', ' ', '2023-01-29 17:21:42', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (58, 18, '0', '有效', 'lock_flag', '有效', 0, 'admin', ' ', '2023-02-01 16:56:00', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (59, 18, '9', '禁用', 'lock_flag', '禁用', 1, 'admin', ' ', '2023-02-01 16:56:09', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (60, 15, '4', 'vue3', 'style_type', 'element-plus', 4, 'admin', ' ', '2023-02-06 13:52:43', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (61, 19, '0', '主机', 'ds_config_type', '主机', 0, 'admin', ' ', '2023-02-06 18:37:23', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (62, 19, '1', 'JDBC', 'ds_config_type', 'jdbc', 2, 'admin', ' ', '2023-02-06 18:37:34', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (63, 20, 'false', '否', 'common_status', '否', 1, 'admin', ' ', '2023-02-09 11:02:39', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (64, 20, 'true', '是', 'common_status', '是', 2, 'admin', ' ', '2023-02-09 11:02:52', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (65, 21, 'MINI', '小程序', 'app_social_type', '小程序登录', 0, 'admin', ' ', '2023-02-10 11:11:41', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (66, 22, '0', '否', 'yes_no_type', '0', 0, 'admin', ' ', '2023-02-20 23:35:23', NULL, '0', '0');
INSERT INTO `sys_dict_item` VALUES (67, 22, '1', '是', 'yes_no_type', '1', 0, 'admin', ' ', '2023-02-20 23:35:37', NULL, '1', '0');
INSERT INTO `sys_dict_item` VALUES (69, 23, 'text', '文本', 'repType', '文本', 0, 'admin', ' ', '2023-02-24 15:08:45', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (70, 23, 'image', '图片', 'repType', '图片', 0, 'admin', ' ', '2023-02-24 15:08:56', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (71, 23, 'voice', '语音', 'repType', '语音', 0, 'admin', ' ', '2023-02-24 15:09:08', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (72, 23, 'video', '视频', 'repType', '视频', 0, 'admin', ' ', '2023-02-24 15:09:18', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (73, 23, 'shortvideo', '小视频', 'repType', '小视频', 0, 'admin', ' ', '2023-02-24 15:09:29', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (74, 23, 'location', '地理位置', 'repType', '地理位置', 0, 'admin', ' ', '2023-02-24 15:09:41', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (75, 23, 'link', '链接消息', 'repType', '链接消息', 0, 'admin', ' ', '2023-02-24 15:09:49', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (76, 23, 'event', '事件推送', 'repType', '事件推送', 0, 'admin', ' ', '2023-02-24 15:09:57', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (77, 24, '0', '未提交', 'leave_status', '未提交', 0, 'admin', ' ', '2023-03-02 22:50:45', NULL, '未提交', '0');
INSERT INTO `sys_dict_item` VALUES (78, 24, '1', '审批中', 'leave_status', '审批中', 0, 'admin', ' ', '2023-03-02 22:50:57', NULL, '审批中', '0');
INSERT INTO `sys_dict_item` VALUES (79, 24, '2', '完成', 'leave_status', '完成', 0, 'admin', ' ', '2023-03-02 22:51:06', NULL, '完成', '0');
INSERT INTO `sys_dict_item` VALUES (80, 24, '9', '驳回', 'leave_status', '驳回', 0, 'admin', ' ', '2023-03-02 22:51:20', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (81, 25, 'record', '日程记录', 'schedule_type', '日程记录', 0, 'admin', ' ', '2023-03-06 14:50:01', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (82, 25, 'plan', '计划', 'schedule_type', '计划类型', 0, 'admin', ' ', '2023-03-06 14:50:29', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (83, 26, '0', '计划中', 'schedule_status', '日程状态', 0, 'admin', ' ', '2023-03-06 14:53:18', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (84, 26, '1', '已开始', 'schedule_status', '已开始', 0, 'admin', ' ', '2023-03-06 14:53:33', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (85, 26, '3', '已结束', 'schedule_status', '已结束', 0, 'admin', ' ', '2023-03-06 14:53:41', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (86, 27, 'mysql', 'mysql', 'ds_type', 'mysql', 0, 'admin', ' ', '2023-03-12 09:58:11', NULL, NULL, '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` bigint NOT NULL COMMENT '编号',
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件名',
  `bucket_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件存储桶名称',
  `original` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原始文件名',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件类型',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '上传时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文件管理表';

-- ----------------------------
-- Records of sys_file
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint NOT NULL COMMENT '编号',
  `log_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '日志类型',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志标题',
  `service_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务ID',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remote_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '远程地址',
  `user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方法',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求参数',
  `time` bigint DEFAULT NULL COMMENT '执行时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '异常信息',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_log_request_uri` (`request_uri`) USING BTREE,
  KEY `sys_log_type` (`log_type`) USING BTREE,
  KEY `sys_log_create_date` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日志表';


-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单名称',
  `en_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '英文名称',
  `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限标识',
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由路径',
  `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单图标',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '是否可见，0隐藏，1显示',
  `sort_order` int DEFAULT '1' COMMENT '排序值，越小越靠前',
  `keep_alive` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否缓存，0否，1是',
  `embedded` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否内嵌，0否，1是',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '菜单类型，0目录，1菜单，2按钮',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志，0未删除，1已删除',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1000, '权限管理', 'authorization', NULL, '/admin', -1, 'iconfont icon-icon-', '1', 0, '0', '0', '0', '', '2018-09-28 08:29:53', 'admin', '2023-03-12 22:32:52', '0');
INSERT INTO `sys_menu` VALUES (1100, '用户管理', 'user', NULL, '/admin/user/index', 1000, 'ele-User', '1', 1, '0', '0', '0', '', '2017-11-02 22:24:37', 'admin', '2023-07-05 10:28:22', '0');
INSERT INTO `sys_menu` VALUES (1101, '用户新增', NULL, 'sys_user_add', NULL, 1100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 09:52:09', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1102, '用户修改', NULL, 'sys_user_edit', NULL, 1100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 09:52:48', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1103, '用户删除', NULL, 'sys_user_del', NULL, 1100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1104, '导入导出', NULL, 'sys_user_export', NULL, 1100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1200, '菜单管理', 'menu', NULL, '/admin/menu/index', 1000, 'iconfont icon-caidan', '1', 2, '0', '0', '0', '', '2017-11-08 09:57:27', 'admin', '2023-07-05 10:28:17', '0');
INSERT INTO `sys_menu` VALUES (1201, '菜单新增', NULL, 'sys_menu_add', NULL, 1200, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:15:53', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1202, '菜单修改', NULL, 'sys_menu_edit', NULL, 1200, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:16:23', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1203, '菜单删除', NULL, 'sys_menu_del', NULL, 1200, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:16:43', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1300, '角色管理', 'role', NULL, '/admin/role/index', 1000, 'iconfont icon-gerenzhongxin', '1', 3, '0', NULL, '0', '', '2017-11-08 10:13:37', 'admin', '2023-07-05 10:28:13', '0');
INSERT INTO `sys_menu` VALUES (1301, '角色新增', NULL, 'sys_role_add', NULL, 1300, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:14:18', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1302, '角色修改', NULL, 'sys_role_edit', NULL, 1300, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:14:41', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1303, '角色删除', NULL, 'sys_role_del', NULL, 1300, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:14:59', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1304, '分配权限', NULL, 'sys_role_perm', NULL, 1300, NULL, '1', 1, '0', NULL, '1', ' ', '2018-04-20 07:22:55', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1305, '角色导入导出', NULL, 'sys_role_export', NULL, 1300, NULL, '1', 4, '0', NULL, '1', ' ', '2022-03-26 15:54:34', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (1400, '部门管理', 'dept', NULL, '/admin/dept/index', 1000, 'iconfont icon-zidingyibuju', '1', 4, '0', NULL, '0', '', '2018-01-20 13:17:19', 'admin', '2023-07-05 10:28:07', '0');
INSERT INTO `sys_menu` VALUES (1401, '部门新增', NULL, 'sys_dept_add', NULL, 1400, NULL, '1', 1, '0', NULL, '1', ' ', '2018-01-20 14:56:16', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1402, '部门修改', NULL, 'sys_dept_edit', NULL, 1400, NULL, '1', 1, '0', NULL, '1', ' ', '2018-01-20 14:56:59', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1403, '部门删除', NULL, 'sys_dept_del', NULL, 1400, NULL, '1', 1, '0', NULL, '1', ' ', '2018-01-20 14:57:28', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (1600, '岗位管理', 'post', NULL, '/admin/post/index', 1000, 'iconfont icon--chaifenhang', '1', 5, '1', '0', '0', '', '2022-03-26 13:04:14', 'admin', '2023-07-05 10:28:03', '0');
INSERT INTO `sys_menu` VALUES (1601, '岗位信息查看', NULL, 'sys_post_view', NULL, 1600, NULL, '1', 0, '0', NULL, '1', ' ', '2022-03-26 13:05:34', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (1602, '岗位信息新增', NULL, 'sys_post_add', NULL, 1600, NULL, '1', 1, '0', NULL, '1', ' ', '2022-03-26 13:06:00', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (1603, '岗位信息修改', NULL, 'sys_post_edit', NULL, 1600, NULL, '1', 2, '0', NULL, '1', ' ', '2022-03-26 13:06:31', ' ', '2022-03-26 13:06:38', '0');
INSERT INTO `sys_menu` VALUES (1604, '岗位信息删除', NULL, 'sys_post_del', NULL, 1600, NULL, '1', 3, '0', NULL, '1', ' ', '2022-03-26 13:06:31', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (1605, '岗位导入导出', NULL, 'sys_post_export', NULL, 1600, NULL, '1', 4, '0', NULL, '1', ' ', '2022-03-26 13:06:31', ' ', '2022-03-26 06:32:02', '0');
INSERT INTO `sys_menu` VALUES (2000, '系统管理', 'system', NULL, '/system', -1, 'iconfont icon-quanjushezhi_o', '1', 1, '0', NULL, '0', '', '2017-11-07 20:56:00', 'admin', '2023-07-05 10:27:58', '0');
INSERT INTO `sys_menu` VALUES (2001, '日志管理', 'log', NULL, '/admin/logs', 2000, 'ele-Cloudy', '1', 0, '0', '0', '0', 'admin', '2023-03-02 12:26:42', 'admin', '2023-07-05 10:27:53', '0');
INSERT INTO `sys_menu` VALUES (2100, '操作日志', 'operation', NULL, '/admin/log/index', 2001, 'iconfont icon-jinridaiban', '1', 2, '0', '0', '0', '', '2017-11-20 14:06:22', 'admin', '2023-07-05 10:27:49', '0');
INSERT INTO `sys_menu` VALUES (2101, '日志删除', NULL, 'sys_log_del', NULL, 2100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-20 20:37:37', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (2102, '导入导出', NULL, 'sys_log_export', NULL, 2100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (2200, '字典管理', 'dict', NULL, '/admin/dict/index', 2000, 'iconfont icon-zhongduancanshuchaxun', '1', 6, '0', NULL, '0', '', '2017-11-29 11:30:52', 'admin', '2023-07-05 10:27:37', '0');
INSERT INTO `sys_menu` VALUES (2201, '字典删除', NULL, 'sys_dict_del', NULL, 2200, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-29 11:30:11', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (2202, '字典新增', NULL, 'sys_dict_add', NULL, 2200, NULL, '1', 1, '0', NULL, '1', ' ', '2018-05-11 22:34:55', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (2203, '字典修改', NULL, 'sys_dict_edit', NULL, 2200, NULL, '1', 1, '0', NULL, '1', ' ', '2018-05-11 22:36:03', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (2210, '参数管理', 'parameter', NULL, '/admin/param/index', 2000, 'iconfont icon-wenducanshu-05', '1', 7, '1', NULL, '0', '', '2019-04-29 22:16:50', 'admin', '2023-02-16 15:24:51', '0');
INSERT INTO `sys_menu` VALUES (2211, '参数新增', NULL, 'sys_syspublicparam_add', NULL, 2210, NULL, '1', 1, '0', NULL, '1', ' ', '2019-04-29 22:17:36', ' ', '2020-03-24 08:57:11', '0');
INSERT INTO `sys_menu` VALUES (2212, '参数删除', NULL, 'sys_syspublicparam_del', NULL, 2210, NULL, '1', 1, '0', NULL, '1', ' ', '2019-04-29 22:17:55', ' ', '2020-03-24 08:57:12', '0');
INSERT INTO `sys_menu` VALUES (2213, '参数编辑', NULL, 'sys_syspublicparam_edit', NULL, 2210, NULL, '1', 1, '0', NULL, '1', ' ', '2019-04-29 22:18:14', ' ', '2020-03-24 08:57:13', '0');
INSERT INTO `sys_menu` VALUES (2300, '代码生成', 'code', NULL, '/gen/table/index', 9000, 'iconfont icon-zhongduancanshu', '1', 1, '0', '0', '0', '', '2018-01-20 13:17:19', 'admin', '2023-02-20 13:54:35', '0');
INSERT INTO `sys_menu` VALUES (2400, '终端管理', 'client', NULL, '/admin/client/index', 2000, 'iconfont icon-gongju', '1', 9, '1', NULL, '0', '', '2018-01-20 13:17:19', 'admin', '2023-02-16 15:25:28', '0');
INSERT INTO `sys_menu` VALUES (2401, '客户端新增', NULL, 'sys_client_add', NULL, 2400, '1', '1', 1, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (2402, '客户端修改', NULL, 'sys_client_edit', NULL, 2400, NULL, '1', 1, '0', NULL, '1', ' ', '2018-05-15 21:37:06', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (2403, '客户端删除', NULL, 'sys_client_del', NULL, 2400, NULL, '1', 1, '0', NULL, '1', ' ', '2018-05-15 21:39:16', ' ', '2021-05-25 03:12:55', '0');
INSERT INTO `sys_menu` VALUES (2600, '令牌管理', 'token', NULL, '/admin/token/index', 2000, 'ele-Key', '1', 11, '0', NULL, '0', '', '2018-09-04 05:58:41', 'admin', '2023-02-16 15:28:28', '0');
INSERT INTO `sys_menu` VALUES (2601, '令牌删除', NULL, 'sys_token_del', NULL, 2600, NULL, '1', 1, '0', NULL, '1', ' ', '2018-09-04 05:59:50', ' ', '2020-03-24 08:57:24', '0');
INSERT INTO `sys_menu` VALUES (2800, 'Quartz管理', 'quartz', NULL, '/daemon/job-manage/index', 2000, 'ele-AlarmClock', '1', 8, '0', NULL, '0', '', '2018-01-20 13:17:19', 'admin', '2023-02-16 15:25:06', '0');
INSERT INTO `sys_menu` VALUES (2810, '任务新增', NULL, 'job_sys_job_add', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:26', '0');
INSERT INTO `sys_menu` VALUES (2820, '任务修改', NULL, 'job_sys_job_edit', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:27', '0');
INSERT INTO `sys_menu` VALUES (2830, '任务删除', NULL, 'job_sys_job_del', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:28', '0');
INSERT INTO `sys_menu` VALUES (2840, '任务暂停', NULL, 'job_sys_job_shutdown_job', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:28', '0');
INSERT INTO `sys_menu` VALUES (2850, '任务开始', NULL, 'job_sys_job_start_job', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:29', '0');
INSERT INTO `sys_menu` VALUES (2860, '任务刷新', NULL, 'job_sys_job_refresh_job', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:30', '0');
INSERT INTO `sys_menu` VALUES (2870, '执行任务', NULL, 'job_sys_job_run_job', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2019-08-08 15:35:18', ' ', '2020-03-24 08:57:31', '0');
INSERT INTO `sys_menu` VALUES (2871, '导出', NULL, 'job_sys_job_export', NULL, 2800, NULL, '1', 0, '0', '0', '1', 'admin', '2023-03-06 15:26:13', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (2906, '文件管理', 'file', NULL, '/admin/file/index', 2000, 'ele-Files', '1', 6, '0', NULL, '0', '', '2019-06-25 12:44:46', 'admin', '2023-02-16 15:24:42', '0');
INSERT INTO `sys_menu` VALUES (2907, '删除文件', NULL, 'sys_file_del', NULL, 2906, NULL, '1', 1, '0', NULL, '1', ' ', '2019-06-25 13:41:41', ' ', '2020-03-24 08:58:42', '0');
INSERT INTO `sys_menu` VALUES (4000, '系统监控', 'monitor', NULL, '/daemon', -1, 'iconfont icon-shuju', '1', 3, '0', '0', '0', 'admin', '2023-02-06 20:20:47', 'admin', '2023-02-23 20:01:07', '0');
INSERT INTO `sys_menu` VALUES (4001, '文档扩展', 'doc', NULL, 'http://pig-gateway:9999/swagger-ui.html', 4000, 'iconfont icon-biaodan', '1', 2, '0', '1', '0', '', '2018-06-26 10:50:32', 'admin', '2023-02-23 20:01:29', '0');
INSERT INTO `sys_menu` VALUES (4002, '缓存监控', 'cache', NULL, '/ext/cache', 4000, 'iconfont icon-shuju', '1', 1, '0', '0', '0', 'admin', '2023-05-29 15:12:59', 'admin', '2023-06-06 11:58:41', '0');
INSERT INTO `sys_menu` VALUES (9000, '开发平台', 'develop', NULL, '/gen', -1, 'iconfont icon-shuxingtu', '1', 9, '0', '0', '0', '', '2019-08-12 09:35:16', 'admin', '2023-07-05 10:25:27', '0');
INSERT INTO `sys_menu` VALUES (9005, '数据源管理', 'datasource', NULL, '/gen/datasource/index', 9000, 'ele-Coin', '1', 0, '0', NULL, '0', '', '2019-08-12 09:42:11', 'admin', '2023-07-05 10:26:56', '0');
INSERT INTO `sys_menu` VALUES (9006, '表单设计', 'Form Design', NULL, '/gen/design/index', 9000, 'iconfont icon-AIshiyanshi', '0', 2, '0', '0', '0', '', '2019-08-16 10:08:56', 'admin', '2023-02-23 14:06:50', '0');
INSERT INTO `sys_menu` VALUES (9007, '生成页面', 'generation', NULL, '/gen/gener/index', 9000, 'iconfont icon-tongzhi4', '0', 0, '0', '0', '0', 'admin', '2023-02-20 09:58:23', 'admin', '2023-07-05 10:27:06', '0');
INSERT INTO `sys_menu` VALUES (9050, '元数据管理', 'metadata', NULL, '/gen/metadata', 9000, 'iconfont icon--chaifenhang', '1', 9, '0', '0', '0', '', '2018-07-27 01:13:21', 'admin', '2023-07-05 10:27:13', '0');
INSERT INTO `sys_menu` VALUES (9051, '模板管理', 'template', NULL, '/gen/template/index', 9050, 'iconfont icon--chaifenhang', '1', 5, '0', '0', '0', 'admin', '2023-02-21 11:22:54', 'admin', '2023-07-05 10:27:18', '0');
INSERT INTO `sys_menu` VALUES (9052, '查询', NULL, 'codegen_template_view', NULL, 9051, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 12:33:03', 'admin', '2023-02-21 13:50:54', '0');
INSERT INTO `sys_menu` VALUES (9053, '增加', NULL, 'codegen_template_add', NULL, 9051, NULL, '1', 0, '0', '0', '1', 'admin', '2023-02-21 13:34:10', 'admin', '2023-02-21 13:39:49', '0');
INSERT INTO `sys_menu` VALUES (9054, '新增', NULL, 'codegen_template_add', NULL, 9051, NULL, '0', 1, '0', '0', '1', 'admin', '2023-02-21 13:51:32', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (9055, '导出', NULL, 'codegen_template_export', NULL, 9051, NULL, '0', 2, '0', '0', '1', 'admin', '2023-02-21 13:51:58', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (9056, '删除', NULL, 'codegen_template_del', NULL, 9051, NULL, '0', 3, '0', '0', '1', 'admin', '2023-02-21 13:52:16', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (9057, '编辑', NULL, 'codegen_template_edit', NULL, 9051, NULL, '0', 4, '0', '0', '1', 'admin', '2023-02-21 13:52:58', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (9059, '模板分组', 'group', NULL, '/gen/group/index', 9050, 'iconfont icon-shuxingtu', '1', 6, '0', '0', '0', 'admin', '2023-02-21 15:06:50', 'admin', '2023-07-05 10:27:22', '0');
INSERT INTO `sys_menu` VALUES (9060, '查询', NULL, 'codegen_group_view', NULL, 9059, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 15:08:07', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (9061, '新增', NULL, 'codegen_group_add', NULL, 9059, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 15:08:28', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (9062, '修改', NULL, 'codegen_group_edit', NULL, 9059, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 15:08:43', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (9063, '删除', NULL, 'codegen_group_del', NULL, 9059, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 15:09:02', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (9064, '导出', NULL, 'codegen_group_export', NULL, 9059, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 15:09:22', ' ', NULL, '0');
INSERT INTO `sys_menu` VALUES (9065, '字段管理', 'field', NULL, '/gen/field-type/index', 9050, 'iconfont icon-fuwenben', '1', 0, '0', '0', '0', 'admin', '2023-02-23 20:05:09', 'admin', '2023-07-05 10:27:31', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details` (
  `id` bigint NOT NULL COMMENT 'ID',
  `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端ID',
  `resource_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源ID集合',
  `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端秘钥',
  `scope` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权范围',
  `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权类型',
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回调地址',
  `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限集合',
  `access_token_validity` int DEFAULT NULL COMMENT '访问令牌有效期（秒）',
  `refresh_token_validity` int DEFAULT NULL COMMENT '刷新令牌有效期（秒）',
  `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '附加信息',
  `autoapprove` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '自动授权',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='终端信息表';

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client_details` VALUES (1, 'app', NULL, 'app', 'server', 'password,refresh_token,authorization_code,client_credentials,mobile', 'http://localhost:4040/sso1/login,http://localhost:4041/sso1/login,http://localhost:8080/renren-admin/sys/oauth2-sso,http://localhost:8090/sys/oauth2-sso', NULL, 43200, 2592001, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\",\"online_quantity\":\"1\"}', 'true', '0', '', 'admin', NULL, '2023-02-09 13:54:54');
INSERT INTO `sys_oauth_client_details` VALUES (2, 'daemon', NULL, 'daemon', 'server', 'password,refresh_token', NULL, NULL, 43200, 2592001, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\"}', 'true', '0', ' ', ' ', NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES (3, 'gen', NULL, 'gen', 'server', 'password,refresh_token', NULL, NULL, 43200, 2592001, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\"}', 'true', '0', ' ', ' ', NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES (4, 'mp', NULL, 'mp', 'server', 'password,refresh_token', NULL, NULL, 43200, 2592001, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\"}', 'true', '0', ' ', ' ', NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES (5, 'pig', NULL, 'pig', 'server', 'password,refresh_token,authorization_code,client_credentials,mobile', 'http://localhost:4040/sso1/login,http://localhost:4041/sso1/login,http://localhost:8080/renren-admin/sys/oauth2-sso,http://localhost:8090/sys/oauth2-sso', NULL, 43200, 2592001, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\",\"online_quantity\":\"1\"}', 'false', '0', '', 'admin', NULL, '2023-03-08 11:32:41');
INSERT INTO `sys_oauth_client_details` VALUES (6, 'test', NULL, 'test', 'server', 'password,refresh_token', NULL, NULL, 43200, 2592001, '{ \"enc_flag\":\"1\",\"captcha_flag\":\"0\"}', 'true', '0', ' ', ' ', NULL, NULL);
INSERT INTO `sys_oauth_client_details` VALUES (7, 'social', NULL, 'social', 'server', 'password,refresh_token,mobile', NULL, NULL, 43200, 2592001, '{ \"enc_flag\":\"0\",\"captcha_flag\":\"0\"}', 'true', '0', ' ', ' ', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int NOT NULL COMMENT '岗位排序',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '岗位描述',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_post` VALUES (1, 'CTO', 'CTO', 0, 'CTOOO', '0', '2022-03-26 13:48:17', '', '2023-03-08 16:03:35', 'admin');
COMMIT;

-- ----------------------------
-- Table structure for sys_public_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_public_param`;
CREATE TABLE `sys_public_param` (
  `public_id` bigint NOT NULL COMMENT '编号',
  `public_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `public_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '键',
  `public_value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '值',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '状态，0禁用，1启用',
  `validate_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '校验码',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `public_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '类型，0未知，1系统，2业务',
  `system_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '系统标识，0非系统，1系统',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  PRIMARY KEY (`public_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公共参数配置表';

-- ----------------------------
-- Records of sys_public_param
-- ----------------------------
BEGIN;
INSERT INTO `sys_public_param` VALUES (1, '租户默认来源', 'TENANT_DEFAULT_ID', '1', '0', '', ' ', ' ', '2020-05-12 04:03:46', '2020-06-20 08:56:30', '2', '0', '1');
INSERT INTO `sys_public_param` VALUES (2, '租户默认部门名称', 'TENANT_DEFAULT_DEPTNAME', '租户默认部门', '0', '', ' ', ' ', '2020-05-12 03:36:32', NULL, '2', '1', '0');
INSERT INTO `sys_public_param` VALUES (3, '租户默认账户', 'TENANT_DEFAULT_USERNAME', 'admin', '0', '', ' ', ' ', '2020-05-12 04:05:04', NULL, '2', '1', '0');
INSERT INTO `sys_public_param` VALUES (4, '租户默认密码', 'TENANT_DEFAULT_PASSWORD', '123456', '0', '', ' ', ' ', '2020-05-12 04:05:24', NULL, '2', '1', '0');
INSERT INTO `sys_public_param` VALUES (5, '租户默认角色编码', 'TENANT_DEFAULT_ROLECODE', 'ROLE_ADMIN', '0', '', ' ', ' ', '2020-05-12 04:05:57', NULL, '2', '1', '0');
INSERT INTO `sys_public_param` VALUES (6, '租户默认角色名称', 'TENANT_DEFAULT_ROLENAME', '租户默认角色', '0', '', ' ', ' ', '2020-05-12 04:06:19', NULL, '2', '1', '0');
INSERT INTO `sys_public_param` VALUES (7, '表前缀', 'GEN_TABLE_PREFIX', 'tb_', '0', '', ' ', ' ', '2020-05-12 04:23:04', NULL, '9', '1', '0');
INSERT INTO `sys_public_param` VALUES (8, '接口文档不显示的字段', 'GEN_HIDDEN_COLUMNS', 'tenant_id', '0', '', ' ', ' ', '2020-05-12 04:25:19', NULL, '9', '1', '0');
INSERT INTO `sys_public_param` VALUES (9, '注册用户默认角色', 'USER_DEFAULT_ROLE', 'GENERAL_USER', '0', NULL, ' ', ' ', '2022-03-31 16:52:24', NULL, '2', '1', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色编码',
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  PRIMARY KEY (`role_id`) USING BTREE,
  KEY `role_idx1_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', '管理员', '', 'admin', '2017-10-29 15:45:51', '2023-07-07 14:55:07', '0');
INSERT INTO `sys_role` VALUES (2, '普通用户', 'GENERAL_USER', '普通用户', '', 'admin', '2022-03-31 17:03:15', '2023-04-03 02:28:51', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色菜单表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES (1, 1000);
INSERT INTO `sys_role_menu` VALUES (1, 1100);
INSERT INTO `sys_role_menu` VALUES (1, 1101);
INSERT INTO `sys_role_menu` VALUES (1, 1102);
INSERT INTO `sys_role_menu` VALUES (1, 1103);
INSERT INTO `sys_role_menu` VALUES (1, 1104);
INSERT INTO `sys_role_menu` VALUES (1, 1200);
INSERT INTO `sys_role_menu` VALUES (1, 1201);
INSERT INTO `sys_role_menu` VALUES (1, 1202);
INSERT INTO `sys_role_menu` VALUES (1, 1203);
INSERT INTO `sys_role_menu` VALUES (1, 1300);
INSERT INTO `sys_role_menu` VALUES (1, 1301);
INSERT INTO `sys_role_menu` VALUES (1, 1302);
INSERT INTO `sys_role_menu` VALUES (1, 1303);
INSERT INTO `sys_role_menu` VALUES (1, 1304);
INSERT INTO `sys_role_menu` VALUES (1, 1305);
INSERT INTO `sys_role_menu` VALUES (1, 1400);
INSERT INTO `sys_role_menu` VALUES (1, 1401);
INSERT INTO `sys_role_menu` VALUES (1, 1402);
INSERT INTO `sys_role_menu` VALUES (1, 1403);
INSERT INTO `sys_role_menu` VALUES (1, 1600);
INSERT INTO `sys_role_menu` VALUES (1, 1601);
INSERT INTO `sys_role_menu` VALUES (1, 1602);
INSERT INTO `sys_role_menu` VALUES (1, 1603);
INSERT INTO `sys_role_menu` VALUES (1, 1604);
INSERT INTO `sys_role_menu` VALUES (1, 1605);
INSERT INTO `sys_role_menu` VALUES (1, 2000);
INSERT INTO `sys_role_menu` VALUES (1, 2001);
INSERT INTO `sys_role_menu` VALUES (1, 2100);
INSERT INTO `sys_role_menu` VALUES (1, 2101);
INSERT INTO `sys_role_menu` VALUES (1, 2102);
INSERT INTO `sys_role_menu` VALUES (1, 2200);
INSERT INTO `sys_role_menu` VALUES (1, 2201);
INSERT INTO `sys_role_menu` VALUES (1, 2202);
INSERT INTO `sys_role_menu` VALUES (1, 2203);
INSERT INTO `sys_role_menu` VALUES (1, 2210);
INSERT INTO `sys_role_menu` VALUES (1, 2211);
INSERT INTO `sys_role_menu` VALUES (1, 2212);
INSERT INTO `sys_role_menu` VALUES (1, 2213);
INSERT INTO `sys_role_menu` VALUES (1, 2300);
INSERT INTO `sys_role_menu` VALUES (1, 2400);
INSERT INTO `sys_role_menu` VALUES (1, 2401);
INSERT INTO `sys_role_menu` VALUES (1, 2402);
INSERT INTO `sys_role_menu` VALUES (1, 2403);
INSERT INTO `sys_role_menu` VALUES (1, 2600);
INSERT INTO `sys_role_menu` VALUES (1, 2601);
INSERT INTO `sys_role_menu` VALUES (1, 2800);
INSERT INTO `sys_role_menu` VALUES (1, 2810);
INSERT INTO `sys_role_menu` VALUES (1, 2820);
INSERT INTO `sys_role_menu` VALUES (1, 2830);
INSERT INTO `sys_role_menu` VALUES (1, 2840);
INSERT INTO `sys_role_menu` VALUES (1, 2850);
INSERT INTO `sys_role_menu` VALUES (1, 2860);
INSERT INTO `sys_role_menu` VALUES (1, 2870);
INSERT INTO `sys_role_menu` VALUES (1, 2871);
INSERT INTO `sys_role_menu` VALUES (1, 2906);
INSERT INTO `sys_role_menu` VALUES (1, 2907);
INSERT INTO `sys_role_menu` VALUES (1, 4000);
INSERT INTO `sys_role_menu` VALUES (1, 4001);
INSERT INTO `sys_role_menu` VALUES (1, 4002);
INSERT INTO `sys_role_menu` VALUES (1, 9000);
INSERT INTO `sys_role_menu` VALUES (1, 9005);
INSERT INTO `sys_role_menu` VALUES (1, 9006);
INSERT INTO `sys_role_menu` VALUES (1, 9007);
INSERT INTO `sys_role_menu` VALUES (1, 9050);
INSERT INTO `sys_role_menu` VALUES (1, 9051);
INSERT INTO `sys_role_menu` VALUES (1, 9052);
INSERT INTO `sys_role_menu` VALUES (1, 9053);
INSERT INTO `sys_role_menu` VALUES (1, 9054);
INSERT INTO `sys_role_menu` VALUES (1, 9055);
INSERT INTO `sys_role_menu` VALUES (1, 9056);
INSERT INTO `sys_role_menu` VALUES (1, 9057);
INSERT INTO `sys_role_menu` VALUES (1, 9059);
INSERT INTO `sys_role_menu` VALUES (1, 9060);
INSERT INTO `sys_role_menu` VALUES (1, 9061);
INSERT INTO `sys_role_menu` VALUES (1, 9062);
INSERT INTO `sys_role_menu` VALUES (1, 9063);
INSERT INTO `sys_role_menu` VALUES (1, 9064);
INSERT INTO `sys_role_menu` VALUES (1, 9065);
INSERT INTO `sys_role_menu` VALUES (2, 4000);
INSERT INTO `sys_role_menu` VALUES (2, 4001);
INSERT INTO `sys_role_menu` VALUES (2, 4002);
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '盐值',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '电话号码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱地址',
  `dept_id` bigint DEFAULT NULL COMMENT '所属部门ID',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `lock_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '锁定标记，0未锁定，9已锁定',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  `wx_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信登录openId',
  `mini_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小程序openId',
  `qq_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'QQ openId',
  `gitee_login` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '码云标识',
  `osc_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '开源中国标识',
  PRIMARY KEY (`user_id`) USING BTREE,
  KEY `user_wx_openid` (`wx_openid`) USING BTREE,
  KEY `user_qq_openid` (`qq_openid`) USING BTREE,
  KEY `user_idx1_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$c/Ae0pRjJtMZg3BnvVpO.eIK6WYWVbKTzqgdy3afR7w.vd.xi3Mgy', '', '17034642999', '/admin/sys-file/s3demo/7ff4ca6b7bf446f3a5a13ac016dc21af.png', '管理员', '管理员', 'pig4cloud@qq.com', 4, ' ', 'admin', '2018-04-20 07:15:18', '2023-07-07 14:55:40', '0', '0', NULL, 'oBxPy5E-v82xWGsfzZVzkD3wEX64', NULL, 'log4j', NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `post_id` bigint NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_post` VALUES (1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (1676492190299299842, 2);
COMMIT;

DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
                           `job_id` bigint NOT NULL COMMENT '任务id',
                           `job_name` varchar(64) CHARACTER SET utf8mb4 NOT NULL COMMENT '任务名称',
                           `job_group` varchar(64) CHARACTER SET utf8mb4 NOT NULL COMMENT '任务组名',
                           `job_order` char(1) CHARACTER SET utf8mb4 DEFAULT '1' COMMENT '组内执行顺利，值越大执行优先级越高，最大值9，最小值1',
                           `job_type` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '1' COMMENT '1、java类;2、spring bean名称;3、rest调用;4、jar调用;9其他',
                           `execute_path` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'job_type=3时，rest调用地址，仅支持rest get协议,需要增加String返回值，0成功，1失败;job_type=4时，jar路径;其它值为空',
                           `class_name` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'job_type=1时，类完整路径;job_type=2时，spring bean名称;其它值为空',
                           `method_name` varchar(500) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '任务方法',
                           `method_params_value` varchar(2000) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '参数值',
                           `cron_expression` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'cron执行表达式',
                           `misfire_policy` varchar(20) CHARACTER SET utf8mb4 DEFAULT '3' COMMENT '错失执行策略（1错失周期立即执行 2错失周期执行一次 3下周期执行）',
                           `job_tenant_type` char(1) CHARACTER SET utf8mb4 DEFAULT '1' COMMENT '1、多租户任务;2、非多租户任务',
                           `job_status` char(1) CHARACTER SET utf8mb4 DEFAULT '0' COMMENT '状态（1、未发布;2、运行中;3、暂停;4、删除;）',
                           `job_execute_status` char(1) CHARACTER SET utf8mb4 DEFAULT '0' COMMENT '状态（0正常 1异常）',
                           `create_by` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建者',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_by` varchar(64) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '更新者',
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                           `start_time` timestamp NULL DEFAULT NULL COMMENT '初次执行时间',
                           `previous_time` timestamp NULL DEFAULT NULL COMMENT '上次执行时间',
                           `next_time` timestamp NULL DEFAULT NULL COMMENT '下次执行时间',
                           `remark` varchar(255) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '备注信息',
                            PRIMARY KEY (`job_id`) USING BTREE,
                           UNIQUE KEY `job_name_group_idx` (`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='定时任务调度表';

-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log` (
                               `job_log_id` bigint NOT NULL COMMENT '任务日志ID',
                               `job_id` bigint NOT NULL COMMENT '任务id',
                               `job_name` varchar(64) CHARACTER SET utf8  DEFAULT NULL COMMENT '任务名称',
                               `job_group` varchar(64) CHARACTER SET utf8  DEFAULT NULL COMMENT '任务组名',
                               `job_order` char(1) CHARACTER SET utf8  DEFAULT NULL COMMENT '组内执行顺利，值越大执行优先级越高，最大值9，最小值1',
                               `job_type` char(1) CHARACTER SET utf8  NOT NULL DEFAULT '1' COMMENT '1、java类;2、spring bean名称;3、rest调用;4、jar调用;9其他',
                               `execute_path` varchar(500) CHARACTER SET utf8  DEFAULT NULL COMMENT 'job_type=3时，rest调用地址，仅支持post协议;job_type=4时，jar路径;其它值为空',
                               `class_name` varchar(500) CHARACTER SET utf8  DEFAULT NULL COMMENT 'job_type=1时，类完整路径;job_type=2时，spring bean名称;其它值为空',
                               `method_name` varchar(500) CHARACTER SET utf8  DEFAULT NULL COMMENT '任务方法',
                               `method_params_value` varchar(2000) CHARACTER SET utf8  DEFAULT NULL COMMENT '参数值',
                               `cron_expression` varchar(255) CHARACTER SET utf8  DEFAULT NULL COMMENT 'cron执行表达式',
                               `job_message` varchar(500) CHARACTER SET utf8  DEFAULT NULL COMMENT '日志信息',
                               `job_log_status` char(1) CHARACTER SET utf8  DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
                               `execute_time` varchar(30) CHARACTER SET utf8  DEFAULT NULL COMMENT '执行时间',
                               `exception_info` varchar(2000) CHARACTER SET utf8  DEFAULT '' COMMENT '异常信息',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='定时任务执行日志表';

-- ----------------------------
-- Table structure for gen_datasource_conf
-- ----------------------------
DROP TABLE IF EXISTS `gen_datasource_conf`;
CREATE TABLE `gen_datasource_conf` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '别名',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'jdbcurl',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  `ds_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库类型',
  `conf_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '配置类型',
  `ds_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库名称',
  `instance` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '实例',
  `port` int DEFAULT NULL COMMENT '端口',
  `host` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主机',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据源表';

-- ----------------------------
-- Records of gen_datasource_conf
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gen_field_type
-- ----------------------------
DROP TABLE IF EXISTS `gen_field_type`;
CREATE TABLE `gen_field_type` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `column_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段类型',
  `attr_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性包名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `column_type` (`column_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1634915190321451010 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字段类型管理';

-- ----------------------------
-- Records of gen_field_type
-- ----------------------------
BEGIN;
INSERT INTO `gen_field_type` VALUES (1, 'datetime', 'LocalDateTime', 'java.time.LocalDateTime', '2023-02-06 08:45:10', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (2, 'date', 'LocalDate', 'java.time.LocalDate', '2023-02-06 08:45:10', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (3, 'tinyint', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (4, 'smallint', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (5, 'mediumint', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (6, 'int', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (7, 'integer', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (8, 'bigint', 'Long', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (9, 'float', 'Float', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (10, 'double', 'Double', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (11, 'decimal', 'BigDecimal', 'java.math.BigDecimal', '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (12, 'bit', 'Boolean', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (13, 'char', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (14, 'varchar', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (15, 'tinytext', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (16, 'text', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (17, 'mediumtext', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (18, 'longtext', 'String', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (19, 'timestamp', 'LocalDateTime', 'java.time.LocalDateTime', '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (20, 'NUMBER', 'Integer', NULL, '2023-02-06 08:45:11', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (21, 'BINARY_INTEGER', 'Integer', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (22, 'BINARY_FLOAT', 'Float', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (23, 'BINARY_DOUBLE', 'Double', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (24, 'VARCHAR2', 'String', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (25, 'NVARCHAR', 'String', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (26, 'NVARCHAR2', 'String', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (27, 'CLOB', 'String', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (28, 'int8', 'Long', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (29, 'int4', 'Integer', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (30, 'int2', 'Integer', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (31, 'numeric', 'BigDecimal', 'java.math.BigDecimal', '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
INSERT INTO `gen_field_type` VALUES (32, 'json', 'String', NULL, '2023-02-06 08:45:12', NULL, NULL, NULL, '0');
COMMIT;

-- ----------------------------
-- Table structure for gen_group
-- ----------------------------
DROP TABLE IF EXISTS `gen_group`;
CREATE TABLE `gen_group` (
  `id` bigint NOT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组名称',
  `group_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组描述',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='模板分组';

-- ----------------------------
-- Records of gen_group
-- ----------------------------
BEGIN;
INSERT INTO `gen_group` VALUES (1, '增删改查', '单表增删改查', ' ', ' ', NULL, NULL, '0');
COMMIT;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表名',
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `db_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库类型',
  `table_comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '说明',
  `author` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作者',
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目包名',
  `version` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目版本号',
  `i18n` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否生成带有i18n 0 不带有 1带有',
  `style` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '代码风格',
  `child_table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '子表名称',
  `main_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '主表关联键',
  `child_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '子表关联键',
  `generator_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '生成方式  0：zip压缩包   1：自定义目录',
  `backend_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '后端生成路径',
  `frontend_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '前端生成路径',
  `module_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模块名',
  `function_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '功能名',
  `form_layout` tinyint DEFAULT NULL COMMENT '表单布局  1：一列   2：两列',
  `ds_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据源ID',
  `baseclass_id` bigint DEFAULT NULL COMMENT '基类ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_name` (`table_name`,`ds_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成表';

-- ----------------------------
-- Records of gen_table
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `ds_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据源名称',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表名称',
  `field_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段名称',
  `field_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段类型',
  `field_comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字段说明',
  `attr_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性名',
  `attr_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '属性包名',
  `sort` int DEFAULT NULL COMMENT '排序',
  `auto_fill` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '自动填充  DEFAULT、INSERT、UPDATE、INSERT_UPDATE',
  `primary_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '主键 0：否  1：是',
  `base_field` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '基类字段 0：否  1：是',
  `form_item` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '表单项 0：否  1：是',
  `form_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '表单必填 0：否  1：是',
  `form_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表单类型',
  `form_validator` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表单效验',
  `grid_item` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '列表项 0：否  1：是',
  `grid_sort` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '列表排序 0：否  1：是',
  `query_item` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '查询项 0：否  1：是',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '查询方式',
  `query_form_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '查询表单类型',
  `field_dict` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='代码生成表字段';

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gen_template
-- ----------------------------
DROP TABLE IF EXISTS `gen_template`;
CREATE TABLE `gen_template` (
  `id` bigint NOT NULL COMMENT '主键',
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板名称',
  `generator_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板路径',
  `template_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板描述',
  `template_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板代码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='模板';

-- ----------------------------
-- Records of gen_template
-- ----------------------------
BEGIN;
INSERT INTO `gen_template` VALUES (1, 'Controller', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java', '后台Controller', 'package ${package}.${moduleName}.controller;\n\n#if($queryList)\nimport cn.hutool.core.util.StrUtil;\n#end\nimport cn.hutool.core.util.ArrayUtil;\nimport cn.hutool.core.collection.CollUtil;\nimport com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\nimport com.baomidou.mybatisplus.core.toolkit.Wrappers;\nimport com.baomidou.mybatisplus.extension.plugins.pagination.Page;\nimport com.pig4cloud.pig.common.core.util.R;\nimport com.pig4cloud.pig.common.log.annotation.SysLog;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport ${package}.${moduleName}.service.${ClassName}Service;\nimport org.springframework.security.access.prepost.PreAuthorize;\nimport com.pig4cloud.plugin.excel.annotation.ResponseExcel;\nimport io.swagger.v3.oas.annotations.security.SecurityRequirement;\nimport org.springdoc.api.annotations.ParameterObject;\nimport org.springframework.http.HttpHeaders;\nimport io.swagger.v3.oas.annotations.tags.Tag;\nimport io.swagger.v3.oas.annotations.Operation;\nimport lombok.RequiredArgsConstructor;\nimport org.springframework.web.bind.annotation.*;\n\nimport java.util.List;\nimport java.util.Objects;\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@RestController\n@RequiredArgsConstructor\n@RequestMapping(\"/${functionName}\" )\n@Tag(description = \"${functionName}\" , name = \"${tableComment}管理\" )\n@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)\npublic class ${ClassName}Controller {\n\n    private final  ${ClassName}Service ${className}Service;\n\n    /**\n     * 分页查询\n     * @param page 分页对象\n     * @param ${className} ${tableComment}\n     * @return\n     */\n    @Operation(summary = \"分页查询\" , description = \"分页查询\" )\n    @GetMapping(\"/page\" )\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_view\')\" )\n    public R get${ClassName}Page(@ParameterObject Page page, @ParameterObject ${ClassName}Entity ${className}) {\n        LambdaQueryWrapper<${ClassName}Entity> wrapper = Wrappers.lambdaQuery();\n#foreach ($field in $queryList)\n#set($getAttrName=$str.getProperty($field.attrName))\n#set($var=\"${className}.$getAttrName()\")\n#if($field.attrType == \'String\')\n#set($expression=\"StrUtil.isNotBlank\")\n#else\n#set($expression=\"Objects.nonNull\")\n#end\n#if($field.queryType == \'=\')\n		wrapper.eq($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'like\' )\n		wrapper.like($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'!-\' )\n		wrapper.ne($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'>\' )\n		wrapper.gt($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'<\' )\n		wrapper.lt($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'>=\' )\n		wrapper.ge($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'<=\' )\n		wrapper.le($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'left like\' )\n		wrapper.likeLeft($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'right like\' )\n		wrapper.likeRight($expression($var),${ClassName}Entity::$getAttrName,$var);\n#end\n#end\n        return R.ok(${className}Service.page(page, wrapper));\n    }\n\n\n    /**\n     * 通过id查询${tableComment}\n     * @param ${pk.attrName} id\n     * @return R\n     */\n    @Operation(summary = \"通过id查询\" , description = \"通过id查询\" )\n    @GetMapping(\"/{${pk.attrName}}\" )\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_view\')\" )\n    public R getById(@PathVariable(\"${pk.attrName}\" ) ${pk.attrType} ${pk.attrName}) {\n        return R.ok(${className}Service.getById(${pk.attrName}));\n    }\n\n    /**\n     * 新增${tableComment}\n     * @param ${className} ${tableComment}\n     * @return R\n     */\n    @Operation(summary = \"新增${tableComment}\" , description = \"新增${tableComment}\" )\n    @SysLog(\"新增${tableComment}\" )\n    @PostMapping\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_add\')\" )\n    public R save(@RequestBody ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.save(${className}));\n    }\n\n    /**\n     * 修改${tableComment}\n     * @param ${className} ${tableComment}\n     * @return R\n     */\n    @Operation(summary = \"修改${tableComment}\" , description = \"修改${tableComment}\" )\n    @SysLog(\"修改${tableComment}\" )\n    @PutMapping\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_edit\')\" )\n    public R updateById(@RequestBody ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.updateById(${className}));\n    }\n\n    /**\n     * 通过id删除${tableComment}\n     * @param ids ${pk.attrName}列表\n     * @return R\n     */\n    @Operation(summary = \"通过id删除${tableComment}\" , description = \"通过id删除${tableComment}\" )\n    @SysLog(\"通过id删除${tableComment}\" )\n    @DeleteMapping\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_del\')\" )\n    public R removeById(@RequestBody ${pk.attrType}[] ids) {\n        return R.ok(${className}Service.removeBatchByIds(CollUtil.toList(ids)));\n    }\n\n\n    /**\n     * 导出excel 表格\n     * @param ${className} 查询条件\n   	 * @param ids 导出指定ID\n     * @return excel 文件流\n     */\n    @ResponseExcel\n    @GetMapping(\"/export\")\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_export\')\" )\n    public List<${ClassName}Entity> export(${ClassName}Entity ${className},${pk.attrType}[] ids) {\n        return ${className}Service.list(Wrappers.lambdaQuery(${className}).in(ArrayUtil.isNotEmpty(ids), ${ClassName}Entity::$str.getProperty($pk.attrName), ids));\n    }\n}', '2023-02-23 01:16:17', '2023-08-28 22:48:06', '0', '', 'admin');
INSERT INTO `gen_template` VALUES (2, 'Service', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/${ClassName}Service.java', 'Service', 'package ${package}.${moduleName}.service;\n\n#if($ChildClassName)\nimport com.github.yulichang.extension.mapping.base.MPJDeepService;\nimport ${package}.${moduleName}.entity.${ChildClassName}Entity;\n#else\nimport com.baomidou.mybatisplus.extension.service.IService;\n#end\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\n\n#if($ChildClassName)\npublic interface ${ClassName}Service extends MPJDeepService<${ClassName}Entity> {\n    Boolean saveDeep(${ClassName}Entity ${className});\n\n    Boolean updateDeep(${ClassName}Entity ${className});\n\n    Boolean removeDeep(Long[] ids);\n\n    Boolean removeChild(Long[] ids);\n#else\npublic interface ${ClassName}Service extends IService<${ClassName}Entity> {\n#end\n\n}', '2023-02-23 01:16:53', '2023-06-04 10:35:25', '0', ' ', ' ');
INSERT INTO `gen_template` VALUES (3, 'ServiceImpl', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/impl/${ClassName}ServiceImpl.java', 'ServiceImpl', 'package ${package}.${moduleName}.service.impl;\n\nimport com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport ${package}.${moduleName}.mapper.${ClassName}Mapper;\nimport ${package}.${moduleName}.service.${ClassName}Service;\nimport org.springframework.stereotype.Service;\n#if($ChildClassName)\nimport cn.hutool.core.collection.CollUtil;\nimport com.baomidou.mybatisplus.core.toolkit.Wrappers;\nimport ${package}.${moduleName}.entity.${ChildClassName}Entity;\nimport ${package}.${moduleName}.mapper.${ChildClassName}Mapper;\nimport org.springframework.transaction.annotation.Transactional;\nimport lombok.RequiredArgsConstructor;\nimport java.util.Objects;\n#end\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@Service\n#if($ChildClassName)\n@RequiredArgsConstructor\n#end\npublic class ${ClassName}ServiceImpl extends ServiceImpl<${ClassName}Mapper, ${ClassName}Entity> implements ${ClassName}Service {\n#if($ChildClassName)\n  private final ${ChildClassName}Mapper ${childClassName}Mapper;\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean saveDeep(${ClassName}Entity ${className}) {\n        baseMapper.insert(${className});\n        for (${ChildClassName}Entity  ${childClassName} : ${className}.get${ChildClassName}List()) {\n            ${childClassName}.$str.setProperty($childField)(${className}.$str.getProperty($mainField)());\n            ${childClassName}Mapper.insert( ${childClassName});\n        }\n\n        return Boolean.TRUE;\n    }\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean updateDeep(${ClassName}Entity ${className}) {\n        baseMapper.updateById(${className});\n        for (${ChildClassName}Entity  ${childClassName} : ${className}.get${ChildClassName}List()) {\n#set($getChildPkName=$str.getProperty(${pk.attrName}))\n            if (Objects.isNull(${childClassName}.$getChildPkName())) {\n                ${childClassName}.$str.setProperty($childField)(${className}.getId());\n                ${childClassName}Mapper.insert(${childClassName});\n            } else {\n                ${childClassName}Mapper.updateById(${childClassName});\n            }\n        }\n        return Boolean.TRUE;\n    }\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean removeDeep(Long[] ids) {\n        baseMapper.deleteBatchIds(CollUtil.toList(ids));\n        ${childClassName}Mapper.delete(Wrappers.<${ChildClassName}Entity>lambdaQuery().in(${ChildClassName}Entity::$str.getProperty($childField), ids));\n        return Boolean.TRUE;\n    }\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean removeChild(Long[] ids) {\n        ${childClassName}Mapper.deleteBatchIds(CollUtil.toList(ids));\n        return Boolean.TRUE;\n    }\n#end\n}', '2023-02-23 01:17:36', '2023-08-27 23:29:58', '0', ' ', ' ');
INSERT INTO `gen_template` VALUES (4, '实体', '${backendPath}/src/main/java/${packagePath}/${moduleName}/entity/${ClassName}Entity.java', 'Entity', 'package ${package}.${moduleName}.entity;\n\nimport com.baomidou.mybatisplus.annotation.*;\nimport com.baomidou.mybatisplus.extension.activerecord.Model;\nimport io.swagger.v3.oas.annotations.media.Schema;\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\n#foreach($import in $importList)\nimport $import;\n#end\n#if($ChildClassName)\nimport com.alibaba.excel.annotation.ExcelIgnore;\nimport com.github.yulichang.annotation.EntityMapping;\nimport java.util.List;\n#end\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@Data\n@TableName(\"${tableName}\")\n@EqualsAndHashCode(callSuper = true)\n@Schema(description = \"${tableComment}\")\npublic class ${ClassName}Entity extends Model<${ClassName}Entity> {\n\n#foreach ($field in $fieldList)\n#if(${field.fieldComment})#set($comment=${field.fieldComment})#else #set($comment=${field.attrName})#end\n\n	/**\n	* $comment\n	*/\n#if($field.primaryPk == \'1\')\n    @TableId(type = IdType.ASSIGN_ID)\n#end\n#if($field.autoFill == \'INSERT\')\n	@TableField(fill = FieldFill.INSERT)\n#elseif($field.autoFill == \'INSERT_UPDATE\')\n	@TableField(fill = FieldFill.INSERT_UPDATE)\n#elseif($field.autoFill == \'UPDATE\')\n	@TableField(fill = FieldFill.UPDATE)\n#end\n#if($field.fieldName == \'del_flag\')\n    @TableLogic\n		@TableField(fill = FieldFill.INSERT)\n#end\n    @Schema(description=\"$comment\"#if($field.hidden),hidden=$field.hidden#end)\n    private $field.attrType $field.attrName;\n#end\n#if($ChildClassName)\n    @ExcelIgnore\n    @TableField(exist = false)\n    @EntityMapping(thisField = \"$mainField\", joinField = \"$childField\")\n    private List<${ChildClassName}Entity> ${childClassName}List;\n#end\n}', '2023-02-23 01:17:53', '2023-08-27 22:26:06', '0', ' ', ' ');
INSERT INTO `gen_template` VALUES (5, 'Mapper', '${backendPath}/src/main/java/${packagePath}/${moduleName}/mapper/${ClassName}Mapper.java', 'Mapper', 'package ${package}.${moduleName}.mapper;\n\nimport com.baomidou.mybatisplus.core.mapper.BaseMapper;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport org.apache.ibatis.annotations.Mapper;\n\n@Mapper\npublic interface ${ClassName}Mapper extends BaseMapper<${ClassName}Entity> {\n\n\n}', '2023-02-23 01:18:18', '2023-08-28 22:47:02', '0', ' ', 'admin');
INSERT INTO `gen_template` VALUES (6, 'Mapper.xml', '${backendPath}/src/main/resources/mapper/${ClassName}Mapper.xml', 'Mapper.xml', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n<mapper namespace=\"${package}.${moduleName}.mapper.${ClassName}Mapper\">\n\n  <resultMap id=\"${className}Map\" type=\"${package}.${moduleName}.entity.${ClassName}Entity\">\n#foreach ($field in $fieldList)\n	  #if($field.primaryPk)\n        <id property=\"$field.attrName\" column=\"$field.fieldName\"/>\n      #end\n      #if(!$field.primaryPk)\n        <result property=\"$field.attrName\" column=\"$field.fieldName\"/>\n      #end\n#end\n  </resultMap>\n</mapper>', '2023-02-23 01:18:35', '2023-06-04 10:34:56', '0', ' ', ' ');
INSERT INTO `gen_template` VALUES (7, '权限菜单', '${backendPath}/menu/${functionName}_menu.sql', 'menu.sql', '-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 \n#set($menuId=${dateTool.getSystemTime()})\n\n-- 菜单SQL\ninsert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name)\nvalues (${menuId}, \'-1\', \'/${moduleName}/${functionName}/index\', \'\', \'0\', \'icon-bangzhushouji\', \'0\', null , \'8\', null , \'${tableComment}管理\');\n\n-- 菜单对应按钮SQL\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)\nvalues (${math.add($menuId,1)},${menuId}, \'${moduleName}_${functionName}_view\', \'1\', null, \'1\',  \'0\', null, \'0\', null, \'${tableComment}查看\');\n\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)\nvalues (${math.add($menuId,2)},${menuId}, \'${moduleName}_${functionName}_add\', \'1\', null, \'1\',  \'0\', null, \'1\', null, \'${tableComment}新增\');\n\ninsert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name)\nvalues (${math.add($menuId,3)},${menuId}, \'${moduleName}_${functionName}_edit\', \'1\', null, \'1\',  \'0\', null, \'2\', null, \'${tableComment}修改\');\n\ninsert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)\nvalues (${math.add($menuId,4)},${menuId}, \'${moduleName}_${functionName}_del\', \'1\', null, \'1\',  \'0\', null, \'3\', null, \'${tableComment}删除\');\n\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name)\nvalues (${math.add($menuId,5)},${menuId}, \'${moduleName}_${functionName}_export\', \'1\', null, \'1\',  \'0\', null, \'3\', null, \'导入导出\');', '2023-02-23 01:19:08', '2023-08-28 22:06:16', '0', ' ', ' ');
INSERT INTO `gen_template` VALUES (8, 'api.ts', '${frontendPath}/src/api/${moduleName}/${functionName}.ts', 'api.ts', 'import request from \"/@/utils/request\"\n\nexport function fetchList(query?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}/page\',\n    method: \'get\',\n    params: query\n  })\n}\n\nexport function addObj(obj?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'post\',\n    data: obj\n  })\n}\n\nexport function getObj(id?: string) {\n  return request({\n    url: \'/${moduleName}/${functionName}/\' + id,\n    method: \'get\'\n  })\n}\n\nexport function delObjs(ids?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'delete\',\n    data: ids\n  })\n}\n\nexport function putObj(obj?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'put\',\n    data: obj\n  })\n}\n\n#if($ChildClassName)\nexport function delChildObj(ids?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}/child\',\n    method: \'delete\',\n    data: ids\n  })\n}\n#end', '2023-02-23 01:19:23', '2023-06-04 10:34:17', '0', ' ', ' ');
INSERT INTO `gen_template` VALUES (9, '表格', '${frontendPath}/src/views/${moduleName}/${functionName}/index.vue', '表格不含i18n', '<template>\n  <div class=\"layout-padding\">\n    <div class=\"layout-padding-auto layout-padding-view\">\n#if($queryList)\n      <el-row v-show=\"showSearch\">\n        <el-form :model=\"state.queryForm\" ref=\"queryRef\" :inline=\"true\" @keyup.enter=\"getDataList\">\n#foreach($field in $queryList)\n#if($field.queryFormType == \'select\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-select v-model=\"state.queryForm.${field.attrName}\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\">\n       #if($field.fieldDict)\n              <el-option :label=\"item.label\" :value=\"item.value\" v-for=\"(item, index) in ${field.fieldDict}\" :key=\"index\"></el-option>\n         #else\n              <el-option label=\"请选择\">0</el-option>\n         #end\n            </el-select>\n      </el-form-item>\n#elseif($field.queryFormType == \'date\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n      <el-date-picker type=\"date\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" v-model=\"state.queryForm.${field.attrName}\"></el-date-picker>\n      </el-form-item>\n#elseif($field.queryFormType == \'datetime\')\n      <el-col>\n        <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\" >\n            <el-date-picker type=\"datetime\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" v-model=\"state.queryForm.${field.attrName}\"></el-date-picker>\n        </el-form-item>\n      </el-col>\n#else\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\" >\n        <el-input placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" v-model=\"state.queryForm.${field.attrName}\" />\n      </el-form-item>\n#end\n#end\n          <el-form-item>\n            <el-button icon=\"search\" type=\"primary\" @click=\"getDataList\">\n              查询\n            </el-button>\n            <el-button icon=\"Refresh\" @click=\"resetQuery\">重置</el-button>\n          </el-form-item>\n        </el-form>\n      </el-row>\n#end\n      <el-row>\n        <div class=\"mb8\" style=\"width: 100%\">\n          <el-button icon=\"folder-add\" type=\"primary\" class=\"ml10\" @click=\"formDialogRef.openDialog()\"\n            v-auth=\"\'${moduleName}_${functionName}_add\'\">\n            新 增\n          </el-button>\n          <el-button plain :disabled=\"multiple\" icon=\"Delete\" type=\"primary\"\n            v-auth=\"\'${moduleName}_${functionName}_del\'\" @click=\"handleDelete(selectObjs)\">\n            删除\n          </el-button>\n          <right-toolbar v-model:showSearch=\"showSearch\" :export=\"\'${moduleName}_${functionName}_export\'\"\n                @exportExcel=\"exportExcel\" class=\"ml10 mr20\" style=\"float: right;\"\n            @queryTable=\"getDataList\"></right-toolbar>\n        </div>\n      </el-row>\n      <el-table :data=\"state.dataList\" v-loading=\"state.loading\" border \n        :cell-style=\"tableStyle.cellStyle\" :header-cell-style=\"tableStyle.headerCellStyle\"\n				@selection-change=\"selectionChangHandle\"\n        @sort-change=\"sortChangeHandle\">\n        <el-table-column type=\"selection\" width=\"40\" align=\"center\" />\n        <el-table-column type=\"index\" label=\"#\" width=\"40\" />\n      #foreach($field in $gridList)\n        #if($field.fieldDict)\n          <el-table-column prop=\"${field.attrName}\" label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" show-overflow-tooltip>\n      <template #default=\"scope\">\n                <dict-tag :options=\"$field.fieldDict\" :value=\"scope.row.${field.attrName}\"></dict-tag>\n            </template>\n          </el-table-column>\n        #else\n          <el-table-column prop=\"${field.attrName}\" label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" #if(${field.gridSort} == \'1\')sortable=\"custom\"#end show-overflow-tooltip/>\n        #end\n     #end\n        <el-table-column label=\"操作\" width=\"150\">\n          <template #default=\"scope\">\n            <el-button icon=\"edit-pen\" text type=\"primary\" v-auth=\"\'${moduleName}_${functionName}_edit\'\"\n              @click=\"formDialogRef.openDialog(scope.row.${pk.attrName})\">编辑</el-button>\n            <el-button icon=\"delete\" text type=\"primary\" v-auth=\"\'${moduleName}_${functionName}_del\'\" @click=\"handleDelete([scope.row.${pk.attrName}])\">删除</el-button>\n          </template>\n        </el-table-column>\n      </el-table>\n      <pagination @size-change=\"sizeChangeHandle\" @current-change=\"currentChangeHandle\" v-bind=\"state.pagination\" />\n    </div>\n\n    <!-- 编辑、新增  -->\n    <form-dialog ref=\"formDialogRef\" @refresh=\"getDataList(false)\" />\n\n  </div>\n</template>\n\n<script setup lang=\"ts\" name=\"system${ClassName}\">\nimport { BasicTableProps, useTable } from \"/@/hooks/table\";\nimport { fetchList, delObjs } from \"/@/api/${moduleName}/${functionName}\";\nimport { useMessage, useMessageBox } from \"/@/hooks/message\";\nimport { useDict } from \'/@/hooks/dict\';\n\n// 引入组件\nconst FormDialog = defineAsyncComponent(() => import(\'./form.vue\'));\n// 定义查询字典\n#set($fieldDict=[])\n#foreach($field in $queryList)\n  #if($field.fieldDict)\n    #set($void=$fieldDict.add($field.fieldDict))\n  #end\n#end\n\n#foreach($field in $gridList)\n  #if($field.fieldDict)\n    #set($void=$fieldDict.add($field.fieldDict))\n  #end\n#end\n#if($fieldDict)\nconst { $dict.format($fieldDict) } = useDict($dict.quotation($fieldDict))\n#end\n// 定义变量内容\nconst formDialogRef = ref()\n// 搜索变量\nconst queryRef = ref()\nconst showSearch = ref(true)\n// 多选变量\nconst selectObjs = ref([]) as any\nconst multiple = ref(true)\n\nconst state: BasicTableProps = reactive<BasicTableProps>({\n  queryForm: {},\n  pageList: fetchList\n})\n\n//  table hook\nconst {\n  getDataList,\n  currentChangeHandle,\n  sizeChangeHandle,\n  sortChangeHandle,\n  downBlobFile,\n	tableStyle\n} = useTable(state)\n\n// 清空搜索条件\nconst resetQuery = () => {\n  // 清空搜索条件\n  queryRef.value?.resetFields()\n  // 清空多选\n  selectObjs.value = []\n  getDataList()\n}\n\n// 导出excel\nconst exportExcel = () => {\n  downBlobFile(\'/${moduleName}/${functionName}/export\',Object.assign(state.queryForm, { ids: selectObjs }), \'${functionName}.xlsx\')\n}\n\n// 多选事件\nconst selectionChangHandle = (objs: { $pk.attrName: string }[]) => {\n  selectObjs.value = objs.map(({ $pk.attrName }) => $pk.attrName);\n  multiple.value = !objs.length;\n};\n\n// 删除操作\nconst handleDelete = async (ids: string[]) => {\n  try {\n    await useMessageBox().confirm(\'此操作将永久删除\');\n  } catch {\n    return;\n  }\n\n  try {\n    await delObjs(ids);\n    getDataList();\n    useMessage().success(\'删除成功\');\n  } catch (err: any) {\n    useMessage().error(err.msg);\n  }\n};\n</script>', '2023-02-23 01:19:35', '2023-08-28 22:53:55', '0', '', 'admin');
INSERT INTO `gen_template` VALUES (10, '表单', '${frontendPath}/src/views/${moduleName}/${functionName}/form.vue', '表单不含i18n', '<template>\n    <el-dialog :title=\"form.${pk.attrName} ? \'编辑\' : \'新增\'\" v-model=\"visible\"\n      :close-on-click-modal=\"false\" draggable>\n      <el-form ref=\"dataFormRef\" :model=\"form\" :rules=\"dataRules\" formDialogRef label-width=\"90px\" v-loading=\"loading\">\n       <el-row :gutter=\"24\">\n#foreach($field in $formList)\n#if($field.attrName != ${pk.attrName})\n#if($formLayout == 1)\n    <el-col :span=\"24\" class=\"mb20\">\n#elseif($formLayout == 2)\n    <el-col :span=\"12\" class=\"mb20\">\n#end\n#if($field.formType == \'text\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n        <el-input v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\"/>\n      </el-form-item>\n      </el-col>\n#elseif($field.formType == \'textarea\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n        <el-input type=\"textarea\" v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\"/>\n      </el-form-item>\n      </el-col>\n#elseif($field.formType == \'select\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n          <el-select v-model=\"form.${field.attrName}\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\">\n     #if($field.fieldDict)\n            <el-option :value=\"item.value\" :label=\"item.label\" v-for=\"(item, index) in ${field.fieldDict}\" :key=\"index\"></el-option>\n       #end\n     #if(!$field.fieldDict)\n            <el-option label=\"请选择\">0</el-option>\n       #end\n          </el-select>\n        </el-form-item>\n      </el-col>\n#elseif($field.formType == \'radio\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-radio-group v-model=\"form.${field.attrName}\">\n     #if($field.fieldDict)\n             <el-radio :label=\"item.value\" v-for=\"(item, index) in ${field.fieldDict}\" border :key=\"index\">{{ item.label }}\n            </el-radio>\n       #else\n           <el-radio label=\"${field.fieldComment}\" border>${field.fieldComment}</el-radio>\n       #end\n            </el-radio-group>\n        </el-form-item>\n      </el-col>\n#elseif($field.formType == \'checkbox\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-checkbox-group v-model=\"form.${field.attrName}\">\n     #if($field.fieldDict)\n                <el-checkbox :label=\"item.value\" :name=\"item.label\" v-for=\"(item, index) in ${field.fieldDict}\" :key=\"index\"></el-checkbox>\n       #end\n     #if(!$field.fieldDict)\n                <el-checkbox label=\"启用\" name=\"type\"></el-checkbox>\n                <el-checkbox label=\"禁用\" name=\"type\"></el-checkbox>\n       #end\n            </<el-checkbox-group>\n        </el-form-item>\n      </el-col>\n#elseif($field.formType == \'date\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n      <el-date-picker type=\"date\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" v-model=\"form.${field.attrName}\" :value-format=\"dateStr\"></el-date-picker>\n      </el-form-item>\n      </el-col>\n#elseif($field.formType == \'datetime\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-date-picker type=\"datetime\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" v-model=\"form.${field.attrName}\" :value-format=\"dateTimeStr\"></el-date-picker>\n      </el-form-item>\n      </el-col>\n\n#elseif($field.formType == \'number\')\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n        <el-input-number :min=\"1\" :max=\"1000\" v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\"></el-input-number>\n      </el-form-item>\n    </el-col>\n#elseif($field.formType == \'upload-file\')\n  <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n    <upload-file v-model:imageUrl=\"form.${field.attrName}\"></upload-file>\n  </el-form-item>\n  </el-col>\n#elseif($field.formType == \'upload-img\')\n  <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n    <upload-img v-model=\"form.${field.attrName}\"></upload-img>\n  </el-form-item>\n  </el-col>\n#elseif($field.formType == \'editor\')\n  <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${field.attrName}\">\n    <editor v-if=\"visible\" v-model:get-html=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\"></editor>\n  </el-form-item>\n  </el-col>\n#end\n\n#if(!$field.formType)\n      <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\" prop=\"${column.attrName}\">\n        <el-input v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\"/>\n      </el-form-item>\n    </el-col>\n#end\n#end\n#end\n			</el-row>\n      </el-form>\n      <template #footer>\n        <span class=\"dialog-footer\">\n          <el-button @click=\"visible = false\">取消</el-button>\n          <el-button type=\"primary\" @click=\"onSubmit\" :disabled=\"loading\">确认</el-button>\n        </span>\n      </template>\n    </el-dialog>\n</template>\n\n<script setup lang=\"ts\" name=\"${ClassName}Dialog\">\nimport { useDict } from \'/@/hooks/dict\';\nimport { useMessage } from \"/@/hooks/message\";\nimport { getObj, addObj, putObj } from \'/@/api/${moduleName}/${functionName}\'\nimport { rule } from \'/@/utils/validate\';\nconst emit = defineEmits([\'refresh\']);\n\n// 定义变量内容\nconst dataFormRef = ref();\nconst visible = ref(false)\nconst loading = ref(false)\n// 定义字典\n#set($fieldDict=[])\n#foreach($field in $gridList)\n	#if($field.fieldDict)\n		#set($void=$fieldDict.add($field.fieldDict))\n	#end\n#end\n#if($fieldDict)\nconst { $dict.format($fieldDict) } = useDict($dict.quotation($fieldDict))\n#end\n\n// 提交表单数据\nconst form = reactive({\n#if(!$formList.contains(${pk.attrName}))\n		${pk.attrName}:\'\',\n#end\n#foreach($field in $formList)\n#if($field.formType == \'number\')\n		${field.attrName}: 0,\n#else\n	  ${field.attrName}: \'\',\n#end\n#end\n});\n\n// 定义校验规则\nconst dataRules = ref({\n#foreach($field in $formList)\n#if($field.formRequired == \'1\' && $field.formValidator)\n    ${field.attrName}: [{required: true, message: \'${field.fieldComment}不能为空\', trigger: \'blur\'}, { validator: rule.${field.formValidator}, trigger: \'blur\' }],\n#elseif($field.formRequired == \'1\')\n    ${field.attrName}: [{required: true, message: \'${field.fieldComment}不能为空\', trigger: \'blur\'}],\n#elseif($field.formValidator)\n    ${field.attrName}: [{ validator: rule.${field.formValidator}, trigger: \'blur\' }],\n#end\n#end\n})\n\n// 打开弹窗\nconst openDialog = (id: string) => {\n  visible.value = true\n  form.${pk.attrName} = \'\'\n\n  // 重置表单数据\n	nextTick(() => {\n		dataFormRef.value?.resetFields();\n	});\n\n  // 获取${className}信息\n  if (id) {\n    form.${pk.attrName} = id\n    get${className}Data(id)\n  }\n};\n\n// 提交\nconst onSubmit = async () => {\n	const valid = await dataFormRef.value.validate().catch(() => {});\n	if (!valid) return false;\n\n	try {\n    loading.value = true;\n		form.${pk.attrName} ? await putObj(form) : await addObj(form);\n		useMessage().success(form.${pk.attrName} ? \'修改成功\' : \'添加成功\');\n		visible.value = false;\n		emit(\'refresh\');\n	} catch (err: any) {\n		useMessage().error(err.msg);\n	} finally {\n    loading.value = false;\n  }\n};\n\n\n// 初始化表单数据\nconst get${className}Data = (id: string) => {\n  // 获取数据\n  loading.value = true\n  getObj(id).then((res: any) => {\n    Object.assign(form, res.data)\n  }).finally(() => {\n    loading.value = false\n  })\n};\n\n// 暴露变量\ndefineExpose({\n  openDialog\n});\n</script>', '2023-02-23 01:19:48', '2023-08-27 22:23:19', '0', '', 'admin');
INSERT INTO `gen_template` VALUES (11, 'i18n英文模板', '${frontendPath}/src/views/${moduleName}/${functionName}/i18n/en.ts', 'i18n英文模板', 'export default {\n   ${functionName}: {\n        index: \'#\',\n        import${className}Tip: \'import ${ClassName}\',\n#foreach($field in $fieldList)\n        ${field.attrName}: \'${field.attrName}\',\n#end\n#foreach($field in $fieldList)\n        input$str.pascalCase(${field.attrName})Tip: \'input ${field.attrName}\',\n#end\n    }\n}', '2023-02-23 01:20:25', '2023-06-04 10:49:25', '0', '', 'admin');
INSERT INTO `gen_template` VALUES (12, 'i18n中文模板', '${frontendPath}/src/views/${moduleName}/${functionName}/i18n/zh-cn.ts', 'i18n中文模板', 'export default {\n   ${functionName}: {\n        index: \'#\',\n        import${className}Tip: \'导入${tableComment}\',\n#foreach($field in $fieldList)\n        ${field.attrName}: \'#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\',\n#end\n#foreach($field in $fieldList)\n        input$str.pascalCase(${field.attrName})Tip: \'请输入#if(${field.fieldComment})${field.fieldComment}#else ${field.attrName}#end\',\n#end\n    }\n}', '2023-02-23 01:20:40', '2023-06-04 10:49:28', '0', '', 'admin');
COMMIT;

-- ----------------------------
-- Table structure for gen_template_group
-- ----------------------------
DROP TABLE IF EXISTS `gen_template_group`;
CREATE TABLE `gen_template_group` (
  `group_id` bigint NOT NULL COMMENT '分组id',
  `template_id` bigint NOT NULL COMMENT '模板id',
  PRIMARY KEY (`group_id`,`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='模板分组关联表';

-- ----------------------------
-- Records of gen_template_group
-- ----------------------------
BEGIN;
INSERT INTO `gen_template_group` VALUES (1, 1);
INSERT INTO `gen_template_group` VALUES (1, 2);
INSERT INTO `gen_template_group` VALUES (1, 3);
INSERT INTO `gen_template_group` VALUES (1, 4);
INSERT INTO `gen_template_group` VALUES (1, 5);
INSERT INTO `gen_template_group` VALUES (1, 6);
INSERT INTO `gen_template_group` VALUES (1, 7);
INSERT INTO `gen_template_group` VALUES (1, 8);
INSERT INTO `gen_template_group` VALUES (1, 9);
INSERT INTO `gen_template_group` VALUES (1, 10);
INSERT INTO `gen_template_group` VALUES (1, 11);
INSERT INTO `gen_template_group` VALUES (1, 12);
COMMIT;

#
# Quartz seems to work best with the driver mm.mysql-2.0.7-bin.jar
#
# PLEASE consider using mysql with innodb tables to avoid locking issues
#
# In your Quartz properties file, you'll need to set
# org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
#

DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;


CREATE TABLE QRTZ_JOB_DETAILS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    JOB_CLASS_NAME   VARCHAR(250) NOT NULL,
    IS_DURABLE VARCHAR(1) NOT NULL,
    IS_NONCONCURRENT VARCHAR(1) NOT NULL,
    IS_UPDATE_DATA VARCHAR(1) NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    JOB_NAME  VARCHAR(200) NOT NULL,
    JOB_GROUP VARCHAR(200) NOT NULL,
    DESCRIPTION VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13) NULL,
    PREV_FIRE_TIME BIGINT(13) NULL,
    PRIORITY INTEGER NULL,
    TRIGGER_STATE VARCHAR(16) NOT NULL,
    TRIGGER_TYPE VARCHAR(8) NOT NULL,
    START_TIME BIGINT(13) NOT NULL,
    END_TIME BIGINT(13) NULL,
    CALENDAR_NAME VARCHAR(200) NULL,
    MISFIRE_INSTR SMALLINT(2) NULL,
    JOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP)
);

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    REPEAT_COUNT BIGINT(7) NOT NULL,
    REPEAT_INTERVAL BIGINT(12) NOT NULL,
    TIMES_TRIGGERED BIGINT(10) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CRON_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(200) NOT NULL,
    TIME_ZONE_ID VARCHAR(80),
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
    REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_BLOB_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA BLOB NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_CALENDARS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    CALENDAR_NAME  VARCHAR(200) NOT NULL,
    CALENDAR BLOB NOT NULL,
    PRIMARY KEY (SCHED_NAME,CALENDAR_NAME)
);

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP)
);

CREATE TABLE QRTZ_FIRED_TRIGGERS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    ENTRY_ID VARCHAR(95) NOT NULL,
    TRIGGER_NAME VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    FIRED_TIME BIGINT(13) NOT NULL,
    SCHED_TIME BIGINT(13) NOT NULL,
    PRIORITY INTEGER NOT NULL,
    STATE VARCHAR(16) NOT NULL,
    JOB_NAME VARCHAR(200) NULL,
    JOB_GROUP VARCHAR(200) NULL,
    IS_NONCONCURRENT VARCHAR(1) NULL,
    REQUESTS_RECOVERY VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,ENTRY_ID)
);

CREATE TABLE QRTZ_SCHEDULER_STATE
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    INSTANCE_NAME VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
    CHECKIN_INTERVAL BIGINT(13) NOT NULL,
    PRIMARY KEY (SCHED_NAME,INSTANCE_NAME)
);

CREATE TABLE QRTZ_LOCKS
  (
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40) NOT NULL,
    PRIMARY KEY (SCHED_NAME,LOCK_NAME)
);

SET FOREIGN_KEY_CHECKS = 1;
