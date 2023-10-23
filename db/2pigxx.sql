USE pigxx;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_audit_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_audit_log`;
CREATE TABLE `sys_audit_log` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `audit_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审计名称',
  `audit_field` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段名称',
  `before_val` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '变更前值',
  `after_val` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '变更后值',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人',
  `create_time` datetime NOT NULL COMMENT '操作时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标记',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='审计记录表';

-- ----------------------------
-- Records of sys_audit_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门名称',
  `sort_order` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级部门ID',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES (1, '总裁办', 1, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:07:49', '0', 0, 1);
INSERT INTO `sys_dept` VALUES (2, '技术部', 2, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1, 1);
INSERT INTO `sys_dept` VALUES (3, '市场部', 3, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1, 1);
INSERT INTO `sys_dept` VALUES (4, '销售部', 4, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1, 1);
INSERT INTO `sys_dept` VALUES (5, '财务部', 5, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 1, 1);
INSERT INTO `sys_dept` VALUES (6, '人事行政部', 6, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:53:36', '1', 1, 1);
INSERT INTO `sys_dept` VALUES (7, '研发部', 7, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 2, 1);
INSERT INTO `sys_dept` VALUES (8, 'UI设计部', 11, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 7, 1);
INSERT INTO `sys_dept` VALUES (9, '产品部', 12, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 2, 1);
INSERT INTO `sys_dept` VALUES (10, '渠道部', 13, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 3, 1);
INSERT INTO `sys_dept` VALUES (11, '推广部', 14, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 3, 1);
INSERT INTO `sys_dept` VALUES (12, '客服部', 15, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 4, 1);
INSERT INTO `sys_dept` VALUES (13, '财务会计部', 16, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 13:04:47', '0', 5, 1);
INSERT INTO `sys_dept` VALUES (14, '审计风控部', 17, 'admin', 'admin', '2023-04-03 13:04:47', '2023-04-03 14:06:57', '0', 5, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL COMMENT '编号',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `system_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '系统标志',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_dict_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES (1, 'log_type', '日志类型', ' ', ' ', '2019-03-19 11:06:44', '2019-03-19 11:06:44', '异常、正常', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (2, 'social_type', '社交登录', ' ', ' ', '2019-03-19 11:09:44', '2019-03-19 11:09:44', '微信、QQ', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (3, 'job_type', '定时任务类型', ' ', ' ', '2019-03-19 11:22:21', '2019-03-19 11:22:21', 'quartz', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (4, 'job_status', '定时任务状态', ' ', ' ', '2019-03-19 11:24:57', '2019-03-19 11:24:57', '发布状态、运行状态', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (5, 'job_execute_status', '定时任务执行状态', ' ', ' ', '2019-03-19 11:26:15', '2019-03-19 11:26:15', '正常、异常', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (6, 'misfire_policy', '定时任务错失执行策略', ' ', ' ', '2019-03-19 11:27:19', '2019-03-19 11:27:19', '周期', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (7, 'gender', '性别', ' ', ' ', '2019-03-27 13:44:06', '2019-03-27 13:44:06', '微信用户性别', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (8, 'subscribe', '订阅状态', ' ', ' ', '2019-03-27 13:48:33', '2019-03-27 13:48:33', '公众号订阅状态', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (9, 'response_type', '回复', ' ', ' ', '2019-03-28 21:29:21', '2019-03-28 21:29:21', '微信消息是否已回复', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (10, 'param_type', '参数配置', ' ', ' ', '2019-04-29 18:20:47', '2019-04-29 18:20:47', '检索、原文、报表、安全、文档、消息、其他', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (11, 'status_type', '租户状态', ' ', ' ', '2019-05-15 16:31:08', '2019-05-15 16:31:08', '租户状态', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (12, 'dict_type', '字典类型', ' ', ' ', '2019-05-16 14:16:20', '2019-05-16 14:20:16', '系统类不能修改', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (13, 'channel_type', '支付类型', ' ', ' ', '2019-05-16 14:16:20', '2019-05-16 14:20:16', '系统类不能修改', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (14, 'grant_types', '授权类型', ' ', ' ', '2019-08-13 07:34:10', '2019-08-13 07:34:10', NULL, '1', '0', 1);
INSERT INTO `sys_dict` VALUES (15, 'style_type', '前端风格', ' ', ' ', '2020-02-07 03:49:28', '2020-02-07 03:50:40', '0-Avue 1-element', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (16, 'captcha_flag_types', '验证码开关', ' ', ' ', '2020-11-18 06:53:25', '2020-11-18 06:53:25', '是否校验验证码', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (17, 'enc_flag_types', '前端密码加密', ' ', ' ', '2020-11-18 06:54:44', '2020-11-18 06:54:44', '前端密码是否加密传输', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (18, 'lock_flag', '用户状态', 'admin', ' ', '2023-02-01 16:55:31', NULL, NULL, '1', '0', 1);
INSERT INTO `sys_dict` VALUES (19, 'ds_config_type', '数据连接类型', 'admin', ' ', '2023-02-06 18:36:59', NULL, NULL, '1', '0', 1);
INSERT INTO `sys_dict` VALUES (20, 'common_status', '通用状态', 'admin', ' ', '2023-02-09 11:02:08', NULL, NULL, '1', '0', 1);
INSERT INTO `sys_dict` VALUES (21, 'app_social_type', 'app社交登录', 'admin', ' ', '2023-02-10 11:11:06', NULL, 'app社交登录', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (22, 'yes_no_type', '是否', 'admin', ' ', '2023-02-20 23:25:04', NULL, NULL, '1', '0', 1);
INSERT INTO `sys_dict` VALUES (23, 'repType', '微信消息类型', 'admin', ' ', '2023-02-24 15:08:25', NULL, NULL, '0', '0', 1);
INSERT INTO `sys_dict` VALUES (24, 'leave_status', '请假状态', 'admin', ' ', '2023-03-02 22:50:15', NULL, NULL, '0', '0', 1);
INSERT INTO `sys_dict` VALUES (25, 'schedule_type', '日程类型', 'admin', ' ', '2023-03-06 14:49:18', NULL, NULL, '0', '0', 1);
INSERT INTO `sys_dict` VALUES (26, 'schedule_status', '日程状态', 'admin', ' ', '2023-03-06 14:52:57', NULL, NULL, '0', '0', 1);
INSERT INTO `sys_dict` VALUES (27, 'ds_type', '代码生成器支持的数据库类型', 'admin', ' ', '2023-03-12 09:57:59', NULL, NULL, '1', '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` bigint(20) NOT NULL COMMENT '编号',
  `dict_id` bigint(20) NOT NULL COMMENT '字典ID',
  `item_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典项值',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典项名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典项描述',
  `sort_order` int(11) NOT NULL DEFAULT '0' COMMENT '排序（升序）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_dict_value` (`item_value`) USING BTREE,
  KEY `sys_dict_label` (`label`) USING BTREE,
  KEY `sys_dict_item_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典项';

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict_item` VALUES (1, 1, '9', '异常', 'log_type', '日志异常', 1, ' ', ' ', '2019-03-19 11:08:59', '2019-03-25 12:49:13', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (2, 1, '0', '正常', 'log_type', '日志正常', 0, ' ', ' ', '2019-03-19 11:09:17', '2019-03-25 12:49:18', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (3, 2, 'WX', '微信', 'social_type', '微信登录', 0, ' ', ' ', '2019-03-19 11:10:02', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (4, 2, 'QQ', 'QQ', 'social_type', 'QQ登录', 1, ' ', ' ', '2019-03-19 11:10:14', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (5, 3, '1', 'java类', 'job_type', 'java类', 1, ' ', ' ', '2019-03-19 11:22:37', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (6, 3, '2', 'spring bean', 'job_type', 'spring bean容器实例', 2, ' ', ' ', '2019-03-19 11:23:05', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (7, 3, '9', '其他', 'job_type', '其他类型', 9, ' ', ' ', '2019-03-19 11:23:31', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (8, 3, '3', 'Rest 调用', 'job_type', 'Rest 调用', 3, ' ', ' ', '2019-03-19 11:23:57', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (9, 3, '4', 'jar', 'job_type', 'jar类型', 4, ' ', ' ', '2019-03-19 11:24:20', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (10, 4, '1', '未发布', 'job_status', '未发布', 1, ' ', ' ', '2019-03-19 11:25:18', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (11, 4, '2', '运行中', 'job_status', '运行中', 2, ' ', ' ', '2019-03-19 11:25:31', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (12, 4, '3', '暂停', 'job_status', '暂停', 3, ' ', ' ', '2019-03-19 11:25:42', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (13, 5, '0', '正常', 'job_execute_status', '正常', 0, ' ', ' ', '2019-03-19 11:26:27', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (14, 5, '1', '异常', 'job_execute_status', '异常', 1, ' ', ' ', '2019-03-19 11:26:41', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (15, 6, '1', '错失周期立即执行', 'misfire_policy', '错失周期立即执行', 1, ' ', ' ', '2019-03-19 11:27:45', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (16, 6, '2', '错失周期执行一次', 'misfire_policy', '错失周期执行一次', 2, ' ', ' ', '2019-03-19 11:27:57', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (17, 6, '3', '下周期执行', 'misfire_policy', '下周期执行', 3, ' ', ' ', '2019-03-19 11:28:08', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (18, 7, '1', '男', 'gender', '微信-男', 0, ' ', ' ', '2019-03-27 13:45:13', '2019-03-27 13:45:13', '微信-男', '0', 1);
INSERT INTO `sys_dict_item` VALUES (19, 7, '2', '女', 'gender', '女-微信', 1, ' ', ' ', '2019-03-27 13:45:34', '2019-03-27 13:45:34', '女-微信', '0', 1);
INSERT INTO `sys_dict_item` VALUES (20, 7, '0', '未知', 'gender', 'x性别未知', 3, ' ', ' ', '2019-03-27 13:45:57', '2019-03-27 13:45:57', 'x性别未知', '0', 1);
INSERT INTO `sys_dict_item` VALUES (21, 8, '0', '未关注', 'subscribe', '公众号-未关注', 0, ' ', ' ', '2019-03-27 13:49:07', '2019-03-27 13:49:07', '公众号-未关注', '0', 1);
INSERT INTO `sys_dict_item` VALUES (22, 8, '1', '已关注', 'subscribe', '公众号-已关注', 1, ' ', ' ', '2019-03-27 13:49:26', '2019-03-27 13:49:26', '公众号-已关注', '0', 1);
INSERT INTO `sys_dict_item` VALUES (23, 9, '0', '未回复', 'response_type', '微信消息-未回复', 0, ' ', ' ', '2019-03-28 21:29:47', '2019-03-28 21:29:47', '微信消息-未回复', '0', 1);
INSERT INTO `sys_dict_item` VALUES (24, 9, '1', '已回复', 'response_type', '微信消息-已回复', 1, ' ', ' ', '2019-03-28 21:30:08', '2019-03-28 21:30:08', '微信消息-已回复', '0', 1);
INSERT INTO `sys_dict_item` VALUES (25, 10, '1', '检索', 'param_type', '检索', 0, ' ', ' ', '2019-04-29 18:22:17', '2019-04-29 18:22:17', '检索', '0', 1);
INSERT INTO `sys_dict_item` VALUES (26, 10, '2', '原文', 'param_type', '原文', 0, ' ', ' ', '2019-04-29 18:22:27', '2019-04-29 18:22:27', '原文', '0', 1);
INSERT INTO `sys_dict_item` VALUES (27, 10, '3', '报表', 'param_type', '报表', 0, ' ', ' ', '2019-04-29 18:22:36', '2019-04-29 18:22:36', '报表', '0', 1);
INSERT INTO `sys_dict_item` VALUES (28, 10, '4', '安全', 'param_type', '安全', 0, ' ', ' ', '2019-04-29 18:22:46', '2019-04-29 18:22:46', '安全', '0', 1);
INSERT INTO `sys_dict_item` VALUES (29, 10, '5', '文档', 'param_type', '文档', 0, ' ', ' ', '2019-04-29 18:22:56', '2019-04-29 18:22:56', '文档', '0', 1);
INSERT INTO `sys_dict_item` VALUES (30, 10, '6', '消息', 'param_type', '消息', 0, ' ', ' ', '2019-04-29 18:23:05', '2019-04-29 18:23:05', '消息', '0', 1);
INSERT INTO `sys_dict_item` VALUES (31, 10, '9', '其他', 'param_type', '其他', 0, ' ', ' ', '2019-04-29 18:23:16', '2019-04-29 18:23:16', '其他', '0', 1);
INSERT INTO `sys_dict_item` VALUES (32, 10, '0', '默认', 'param_type', '默认', 0, ' ', ' ', '2019-04-29 18:23:30', '2019-04-29 18:23:30', '默认', '0', 1);
INSERT INTO `sys_dict_item` VALUES (33, 11, '0', '正常', 'status_type', '状态正常', 0, ' ', ' ', '2019-05-15 16:31:34', '2019-05-16 22:30:46', '状态正常', '0', 1);
INSERT INTO `sys_dict_item` VALUES (34, 11, '9', '冻结', 'status_type', '状态冻结', 1, ' ', ' ', '2019-05-15 16:31:56', '2019-05-16 22:30:50', '状态冻结', '0', 1);
INSERT INTO `sys_dict_item` VALUES (35, 12, '1', '系统类', 'dict_type', '系统类字典', 0, ' ', ' ', '2019-05-16 14:20:40', '2019-05-16 14:20:40', '不能修改删除', '0', 1);
INSERT INTO `sys_dict_item` VALUES (36, 12, '0', '业务类', 'dict_type', '业务类字典', 0, ' ', ' ', '2019-05-16 14:20:59', '2019-05-16 14:20:59', '可以修改', '0', 1);
INSERT INTO `sys_dict_item` VALUES (37, 2, 'GITEE', '码云', 'social_type', '码云', 2, ' ', ' ', '2019-06-28 09:59:12', '2019-06-28 09:59:12', '码云', '0', 1);
INSERT INTO `sys_dict_item` VALUES (38, 2, 'OSC', '开源中国', 'social_type', '开源中国登录', 2, ' ', ' ', '2019-06-28 10:04:32', '2019-06-28 10:04:32', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (39, 14, 'password', '密码模式', 'grant_types', '支持oauth密码模式', 0, ' ', ' ', '2019-08-13 07:35:28', '2019-08-13 07:35:28', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (40, 14, 'authorization_code', '授权码模式', 'grant_types', 'oauth2 授权码模式', 1, ' ', ' ', '2019-08-13 07:36:07', '2019-08-13 07:36:07', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (41, 14, 'client_credentials', '客户端模式', 'grant_types', 'oauth2 客户端模式', 2, ' ', ' ', '2019-08-13 07:36:30', '2019-08-13 07:36:30', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (42, 14, 'refresh_token', '刷新模式', 'grant_types', 'oauth2 刷新token', 3, ' ', ' ', '2019-08-13 07:36:54', '2019-08-13 07:36:54', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (43, 14, 'implicit', '简化模式', 'grant_types', 'oauth2 简化模式', 4, ' ', ' ', '2019-08-13 07:39:32', '2019-08-13 07:39:32', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (44, 15, '0', 'Avue', 'style_type', 'Avue风格', 0, ' ', ' ', '2020-02-07 03:52:52', '2020-02-07 03:52:52', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (45, 15, '1', 'element', 'style_type', 'element-ui', 1, ' ', ' ', '2020-02-07 03:53:12', '2020-02-07 03:53:12', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (46, 16, '0', '关', 'captcha_flag_types', '不校验验证码', 0, ' ', ' ', '2020-11-18 06:53:58', '2020-11-18 06:53:58', '不校验验证码 -0', '0', 1);
INSERT INTO `sys_dict_item` VALUES (47, 16, '1', '开', 'captcha_flag_types', '校验验证码', 1, ' ', ' ', '2020-11-18 06:54:15', '2020-11-18 06:54:15', '不校验验证码-1', '0', 1);
INSERT INTO `sys_dict_item` VALUES (48, 17, '0', '否', 'enc_flag_types', '不加密', 0, ' ', ' ', '2020-11-18 06:55:31', '2020-11-18 06:55:31', '不加密-0', '0', 1);
INSERT INTO `sys_dict_item` VALUES (49, 17, '1', '是', 'enc_flag_types', '加密', 1, ' ', ' ', '2020-11-18 06:55:51', '2020-11-18 06:55:51', '加密-1', '0', 1);
INSERT INTO `sys_dict_item` VALUES (50, 13, 'MERGE_PAY', '聚合支付', 'channel_type', '聚合支付', 1, ' ', ' ', '2019-05-30 19:08:08', '2019-06-18 13:51:53', '聚合支付', '0', 1);
INSERT INTO `sys_dict_item` VALUES (51, 2, 'CAS', 'CAS登录', 'social_type', 'CAS 单点登录系统', 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (52, 2, 'DINGTALK', '钉钉', 'social_type', '钉钉', 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (53, 2, 'WEIXIN_CP', '企业微信', 'social_type', '企业微信', 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (54, 15, '2', 'APP', 'style_type', 'uview风格', 1, ' ', ' ', '2020-02-07 03:53:12', '2020-02-07 03:53:12', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (55, 13, 'ALIPAY_WAP', '支付宝支付', 'channel_type', '支付宝支付', 1, ' ', ' ', '2019-05-30 19:08:08', '2019-06-18 13:51:53', '聚合支付', '0', 1);
INSERT INTO `sys_dict_item` VALUES (56, 13, 'WEIXIN_MP', '微信支付', 'channel_type', '微信支付', 1, ' ', ' ', '2019-05-30 19:08:08', '2019-06-18 13:51:53', '聚合支付', '0', 1);
INSERT INTO `sys_dict_item` VALUES (57, 14, 'mobile', 'mobile', 'grant_types', '移动端登录', 5, 'admin', ' ', '2023-01-29 17:21:42', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (58, 18, '0', '有效', 'lock_flag', '有效', 0, 'admin', ' ', '2023-02-01 16:56:00', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (59, 18, '9', '禁用', 'lock_flag', '禁用', 1, 'admin', ' ', '2023-02-01 16:56:09', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (60, 15, '4', 'vue3', 'style_type', 'element-plus', 4, 'admin', ' ', '2023-02-06 13:52:43', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (61, 19, '0', '主机', 'ds_config_type', '主机', 0, 'admin', ' ', '2023-02-06 18:37:23', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (62, 19, '1', 'JDBC', 'ds_config_type', 'jdbc', 2, 'admin', ' ', '2023-02-06 18:37:34', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (63, 20, 'false', '否', 'common_status', '否', 1, 'admin', ' ', '2023-02-09 11:02:39', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (64, 20, 'true', '是', 'common_status', '是', 2, 'admin', ' ', '2023-02-09 11:02:52', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (65, 21, 'MINI', '小程序', 'app_social_type', '小程序登录', 0, 'admin', ' ', '2023-02-10 11:11:41', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (66, 22, '0', '否', 'yes_no_type', '0', 0, 'admin', ' ', '2023-02-20 23:35:23', NULL, '0', '0', 1);
INSERT INTO `sys_dict_item` VALUES (67, 22, '1', '是', 'yes_no_type', '1', 0, 'admin', ' ', '2023-02-20 23:35:37', NULL, '1', '0', 1);
INSERT INTO `sys_dict_item` VALUES (69, 23, 'text', '文本', 'repType', '文本', 0, 'admin', ' ', '2023-02-24 15:08:45', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (70, 23, 'image', '图片', 'repType', '图片', 0, 'admin', ' ', '2023-02-24 15:08:56', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (71, 23, 'voice', '语音', 'repType', '语音', 0, 'admin', ' ', '2023-02-24 15:09:08', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (72, 23, 'video', '视频', 'repType', '视频', 0, 'admin', ' ', '2023-02-24 15:09:18', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (73, 23, 'shortvideo', '小视频', 'repType', '小视频', 0, 'admin', ' ', '2023-02-24 15:09:29', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (74, 23, 'location', '地理位置', 'repType', '地理位置', 0, 'admin', ' ', '2023-02-24 15:09:41', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (75, 23, 'link', '链接消息', 'repType', '链接消息', 0, 'admin', ' ', '2023-02-24 15:09:49', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (76, 23, 'event', '事件推送', 'repType', '事件推送', 0, 'admin', ' ', '2023-02-24 15:09:57', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (77, 24, '0', '未提交', 'leave_status', '未提交', 0, 'admin', ' ', '2023-03-02 22:50:45', NULL, '未提交', '0', 1);
INSERT INTO `sys_dict_item` VALUES (78, 24, '1', '审批中', 'leave_status', '审批中', 0, 'admin', ' ', '2023-03-02 22:50:57', NULL, '审批中', '0', 1);
INSERT INTO `sys_dict_item` VALUES (79, 24, '2', '完成', 'leave_status', '完成', 0, 'admin', ' ', '2023-03-02 22:51:06', NULL, '完成', '0', 1);
INSERT INTO `sys_dict_item` VALUES (80, 24, '9', '驳回', 'leave_status', '驳回', 0, 'admin', ' ', '2023-03-02 22:51:20', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (81, 25, 'record', '日程记录', 'schedule_type', '日程记录', 0, 'admin', ' ', '2023-03-06 14:50:01', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (82, 25, 'plan', '计划', 'schedule_type', '计划类型', 0, 'admin', ' ', '2023-03-06 14:50:29', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (83, 26, '0', '计划中', 'schedule_status', '日程状态', 0, 'admin', ' ', '2023-03-06 14:53:18', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (84, 26, '1', '已开始', 'schedule_status', '已开始', 0, 'admin', ' ', '2023-03-06 14:53:33', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (85, 26, '3', '已结束', 'schedule_status', '已结束', 0, 'admin', ' ', '2023-03-06 14:53:41', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (86, 27, 'mysql', 'mysql', 'ds_type', 'mysql', 0, 'admin', ' ', '2023-03-12 09:58:11', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (87, 27, 'pg', 'pg', 'ds_type', 'pg', 1, 'admin', ' ', '2023-03-12 09:58:20', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (88, 27, 'oracle', 'oracle', 'ds_type', 'oracle', 2, 'admin', ' ', '2023-03-12 09:58:29', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (89, 27, 'mssql', 'mssql', 'ds_type', 'mssql', 3, 'admin', ' ', '2023-03-12 09:58:42', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (90, 27, 'db2', 'db2', 'ds_type', 'db2', 4, 'admin', ' ', '2023-03-12 09:58:53', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (91, 27, 'dm', '达梦', 'ds_type', '达梦', 5, 'admin', ' ', '2023-03-12 09:59:07', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (92, 27, 'highgo', '瀚高', 'ds_type', '瀚高数据库', 5, 'admin', ' ', '2023-03-12 09:59:07', NULL, NULL, '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` bigint(20) NOT NULL COMMENT '编号',
  `group_id` bigint DEFAULT NULL COMMENT '文件组',
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件名',
  `bucket_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件存储桶名称',
  `dir` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件夹名称',
  `original` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '原始文件名',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件类型',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '上传时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '所属租户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='文件管理表';

DROP TABLE IF EXISTS `sys_file_group`;
CREATE TABLE `sys_file_group` (
  `id` bigint unsigned NOT NULL COMMENT '主键ID',
  `type` tinyint unsigned DEFAULT '10' COMMENT '类型: [10=图片, 20=视频]',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '分类名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户',
  `pid` bigint DEFAULT NULL COMMENT '父ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='文件分类表';

-- ----------------------------
-- Table structure for sys_i18n
-- ----------------------------
DROP TABLE IF EXISTS `sys_i18n`;
CREATE TABLE `sys_i18n` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'name',
  `zh_cn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '中文',
  `en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '英文',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统表-国际化';

-- ----------------------------
-- Records of sys_i18n
-- ----------------------------
BEGIN;
INSERT INTO `sys_i18n` VALUES (1, 'router.permissionManagement', '权限管理', 'Permission Management', '', '2023-02-14 02:03:59', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (2, 'router.userManagement', '用户管理', 'User Management', 'admin', '2023-02-14 10:39:08', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (3, 'router.menuManagement', '菜单管理', 'Menu Management', 'admin', '2023-02-15 23:14:39', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (4, 'router.roleManagement', '角色管理', 'Role Management', 'admin', '2023-02-15 23:15:51', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (5, 'router.departmentManagement', '部门管理', 'Department Management', 'admin', '2023-02-15 23:16:52', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (6, 'router.tenantManagement', '租户管理', 'Tenant Management', 'admin', '2023-02-24 10:08:29', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (7, 'router.postManagement', '岗位管理', 'Post Management', 'admin', '2023-02-24 10:12:58', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (8, 'router.systemManagement', '系统管理', 'System Management', 'admin', '2023-02-24 10:13:34', 'admin', '2023-02-24 10:58:30', '0');
INSERT INTO `sys_i18n` VALUES (9, 'router.operationLog', '操作日志', 'Operation Log', 'admin', '2023-02-24 10:14:47', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (10, 'router.dictManagement', '字典管理', 'Dictionary Management', 'admin', '2023-02-24 10:16:21', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (11, 'router.parameterManagement', '参数管理', 'Parameter Management', 'admin', '2023-02-24 10:17:04', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (12, 'router.codeGeneration', '代码生成', 'Code Generation', 'admin', '2023-02-24 10:19:16', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (13, 'router.terminalManagement', '终端管理', 'Terminal Management', 'admin', '2023-02-24 10:21:45', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (14, 'router.keyManagement', '密钥管理', 'Key Management', 'admin', '2023-02-24 10:22:52', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (15, 'router.tokenManagement', '令牌管理', 'Token Management', 'admin', '2023-02-24 10:23:22', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (16, 'router.quartzManagement', 'Quartz管理', 'Quartz Management', 'admin', '2023-02-24 10:24:32', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (17, 'router.metadataManagement', '元数据管理', 'Metadata Management', 'admin', '2023-02-24 10:25:11', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (18, 'router.documentExtension', '文档扩展', 'Document Extension', 'admin', '2023-02-24 10:27:23', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (19, 'router.fileManagement', '文件管理', 'File Management', 'admin', '2023-02-24 10:28:44', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (20, 'router.platformDevelopment', '开发平台', 'Platform Development', 'admin', '2023-02-24 10:29:28', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (21, 'router.dataSourceManagement', '数据源管理', 'Data Source Management', 'admin', '2023-02-24 10:30:33', 'admin', '2023-03-06 14:33:20', '0');
INSERT INTO `sys_i18n` VALUES (22, 'router.formDesign', '表单设计', 'Form Design', 'admin', '2023-02-24 10:31:33', 'admin', '2023-03-06 14:33:28', '0');
INSERT INTO `sys_i18n` VALUES (23, 'router.appManagement', 'APP管理', 'App Management', 'admin', '2023-02-24 10:33:22', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (24, 'router.customerManagement', '客户管理', 'Customer Management', 'admin', '2023-02-24 10:35:30', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (25, 'router.appRole', 'APP角色', 'App Role', 'admin', '2023-02-24 10:36:17', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (26, 'router.appPermission', 'APP权限', 'App Permission', 'admin', '2023-02-24 10:36:59', 'admin', '2023-02-24 10:37:47', '0');
INSERT INTO `sys_i18n` VALUES (27, 'router.appKey', 'APP秘钥', 'App Key', 'admin', '2023-02-24 10:36:59', 'admin', '2023-02-24 10:40:27', '0');
INSERT INTO `sys_i18n` VALUES (28, 'router.internationalizationManagement', '国际化管理', 'Internationalization Management', 'admin', '2023-02-24 10:36:59', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (29, 'router.auditLog', '审计日志', 'Audit Log', 'admin', '2023-02-24 10:36:59', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (30, 'router.systemMonitoring', '系统监控', 'System Monitoring', 'admin', '2023-02-24 10:36:59', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (31, 'router.generatePages', '生成页面', 'Generate Pages', 'admin', '2023-02-24 10:44:04', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (32, 'router.templateManagement', '模板管理', 'Template Management', 'admin', '2023-02-24 10:44:31', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (33, 'router.templateGroup', '模板分组', 'Template Group', 'admin', '2023-02-24 10:45:10', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (34, 'router.fieldManagement', '字段管理', 'Field Management', 'admin', '2023-02-24 10:46:04', 'admin', '2023-03-07 14:27:48', '0');
INSERT INTO `sys_i18n` VALUES (35, 'router.wechatPlatform', '公众号平台', 'WeChat Platform', 'admin', '2023-02-24 10:48:51', 'admin', '2023-02-24 11:03:41', '0');
INSERT INTO `sys_i18n` VALUES (36, 'router.accountManagement', '账号管理', 'Account Management', 'admin', '2023-02-24 10:13:34', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (37, 'router.menuSettings', '菜单设置', 'Menu Settings', 'admin', '2023-02-24 14:02:22', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (38, 'router.fanManagement', '粉丝管理', 'Fan Management', 'admin', '2023-02-24 14:03:44', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (39, 'router.messageManagement', '消息管理', 'Message Management', 'admin', '2023-02-24 14:03:45', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (40, 'router.paymentSystem', '支付系统', 'Payment System', 'admin', '2023-02-24 14:03:46', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (41, 'router.checkoutCounter', '收银台', 'Checkout Counter', 'admin', '2023-02-24 14:03:47', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (42, 'router.mediaManagement', '素材管理', 'Media Management', 'admin', '2023-02-24 14:03:48', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (43, 'router.paymentChannel', '支付渠道', 'Payment Channel', 'admin', '2023-02-24 14:03:49', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (44, 'router.productOrder', '商品订单', 'Product Order', 'admin', '2023-02-24 14:03:50', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (45, 'router.notificationRecord', '通知记录', 'Notification Record', 'admin', '2023-02-24 14:03:51', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (46, 'router.refundOrder', '退款订单', 'Refund Order', 'admin', '2023-02-24 14:03:52', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (47, 'router.paymentOrder', '支付订单', 'Payment Order', 'admin', '2023-02-24 14:03:53', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (48, 'router.autoReply', '自动回复', 'Auto Reply', 'admin', '2023-02-24 14:03:54', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (49, 'router.operationalData', '运营数据', 'Operational Data', 'admin', '2023-02-24 14:03:55', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (50, 'router.logManagement', '日志管理', 'Log Management', 'admin', NULL, '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (51, 'router.collaborativeOffice', '协同办公', 'Collaborative Office', 'admin', NULL, '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (52, 'router.modelManagement', '模型管理', 'Model Management', 'admin', NULL, '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (53, 'router.modelDiagramView', '模型图查看', 'Model Diagram View', 'admin', NULL, '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (54, 'router.processManagement', '流程管理', 'Process Management', 'admin', NULL, '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (55, 'router.leaveWorkOrder', '请假工单', 'Leave Work Order', 'admin', NULL, '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (56, 'router.todoTask', '代办任务', 'Todo Task', 'admin', NULL, '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (57, 'router.tagManagement', '标签管理', 'Tag Management', 'admin', NULL, '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (58, 'router.articleInformation\n', '文章资讯', 'Article Information', ' ', '2023-08-10 13:40:09', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (59, 'router.articleCategory', '文章分类', 'Article Category', ' ', '2023-08-10 13:40:48', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (60, 'router.interfaceSettings', '界面设置', 'Interface Settings', ' ', '2023-08-10 13:41:21', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (61, 'router.bottomNavigation\n', '底部导航', 'Bottom Navigation', ' ', '2023-08-10 13:41:54', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (62, 'router.cacheMonitoring\n', '缓存监控', 'Cache Monitoring', ' ', '2023-08-10 13:42:35', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (63, 'rotuer. initiateProcess', '发起流程', 'Initiate Process\n', ' ', '2023-08-10 13:44:23', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (64, 'router.taskManagement\n', '任务管理', 'Task Management', ' ', '2023-08-10 13:44:53', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (65, 'router.myInitiations', '我的发起', 'My Initiations', ' ', '2023-08-10 13:45:17', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (66, 'router.copiedtoMe\n', '抄送给我', 'Copied to Me', ' ', '2023-08-10 13:45:46', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (67, 'router.completedTasks\n', '我的已办', 'Completed Tasks', ' ', '2023-08-10 13:46:37', ' ', '2023-08-10 13:47:09', '0');
INSERT INTO `sys_i18n` VALUES (68, 'router.createFlow', '创建流程', 'Create Flow', ' ', '2023-08-10 13:46:37', ' ', '2023-08-10 13:47:09', '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL COMMENT '编号',
  `log_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '日志类型',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日志标题',
  `service_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '服务ID',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remote_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '远程地址',
  `user_agent` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求方法',
  `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '请求参数',
  `time` bigint(20) DEFAULT NULL COMMENT '执行时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '异常信息',
  `tenant_id` bigint(20) DEFAULT '0' COMMENT '所属租户',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `sys_log_request_uri` (`request_uri`) USING BTREE,
  KEY `sys_log_type` (`log_type`) USING BTREE,
  KEY `sys_log_create_date` (`create_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='日志表';

-- ----------------------------
-- Records of sys_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单名称',
  `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限标识',
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '路由路径',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单图标',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '是否可见，0隐藏，1显示',
  `sort_order` int(11) DEFAULT '1' COMMENT '排序值，越小越靠前',
  `keep_alive` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否缓存，0否，1是',
  `embedded` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否内嵌，0否，1是',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '菜单类型，0目录，1菜单，2按钮',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志，0未删除，1已删除',
  `tenant_id` bigint(20) unsigned DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1000, '权限管理', NULL, '/admin', -1, 'iconfont icon-icon-', '1', 0, '0', '0', '0', '', '2018-09-28 08:29:53', 'admin', '2023-03-12 22:32:52', '0', 1);
INSERT INTO `sys_menu` VALUES (1100, '用户管理', NULL, '/admin/user/index', 1000, 'ele-User', '1', 1, '0', '0', '0', '', '2017-11-02 22:24:37', 'admin', '2023-03-12 22:32:59', '0', 1);
INSERT INTO `sys_menu` VALUES (1101, '用户新增', 'sys_user_add', NULL, 1100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 09:52:09', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1102, '用户修改', 'sys_user_edit', NULL, 1100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 09:52:48', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1103, '用户删除', 'sys_user_del', NULL, 1100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1104, '导入导出', 'sys_user_export', NULL, 1100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1200, '菜单管理', NULL, '/admin/menu/index', 1000, 'iconfont icon-caidan', '1', 2, '0', '0', '0', '', '2017-11-08 09:57:27', 'admin', '2023-03-29 19:15:25', '0', 1);
INSERT INTO `sys_menu` VALUES (1201, '菜单新增', 'sys_menu_add', NULL, 1200, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:15:53', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1202, '菜单修改', 'sys_menu_edit', NULL, 1200, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:16:23', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1203, '菜单删除', 'sys_menu_del', NULL, 1200, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:16:43', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1300, '角色管理', NULL, '/admin/role/index', 1000, 'iconfont icon-gerenzhongxin', '1', 3, '0', NULL, '0', '', '2017-11-08 10:13:37', 'admin', '2023-02-16 15:21:31', '0', 1);
INSERT INTO `sys_menu` VALUES (1301, '角色新增', 'sys_role_add', NULL, 1300, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:14:18', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1302, '角色修改', 'sys_role_edit', NULL, 1300, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:14:41', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1303, '角色删除', 'sys_role_del', NULL, 1300, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 10:14:59', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1304, '分配权限', 'sys_role_perm', NULL, 1300, NULL, '1', 1, '0', NULL, '1', ' ', '2018-04-20 07:22:55', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1305, '角色导入导出', 'sys_role_export', NULL, 1300, NULL, '1', 4, '0', NULL, '1', ' ', '2022-03-26 15:54:34', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1400, '部门管理', NULL, '/admin/dept/index', 1000, 'iconfont icon-zidingyibuju', '1', 4, '0', NULL, '0', '', '2018-01-20 13:17:19', 'admin', '2023-02-16 15:21:45', '0', 1);
INSERT INTO `sys_menu` VALUES (1401, '部门新增', 'sys_dept_add', NULL, 1400, NULL, '1', 1, '0', NULL, '1', ' ', '2018-01-20 14:56:16', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1402, '部门修改', 'sys_dept_edit', NULL, 1400, NULL, '1', 1, '0', NULL, '1', ' ', '2018-01-20 14:56:59', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1403, '部门删除', 'sys_dept_del', NULL, 1400, NULL, '1', 1, '0', NULL, '1', ' ', '2018-01-20 14:57:28', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1404, '开放互联', 'sys_connect_sync', NULL, 1400, NULL, '1', 1, '0', NULL, '1', ' ', '2018-01-20 14:57:28', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1500, '租户管理', NULL, '/admin/tenant/index', 1000, 'iconfont icon-shuxingtu', '1', 9, '0', '0', '0', '', '2018-01-20 13:17:19', 'admin', '2023-03-07 13:32:15', '0', 1);
INSERT INTO `sys_menu` VALUES (1501, '租户新增', 'sys_systenant_add', NULL, 1500, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:56:52', '0', 1);
INSERT INTO `sys_menu` VALUES (1502, '租户修改', 'sys_systenant_edit', NULL, 1500, '1', '1', 1, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:56:53', '0', 1);
INSERT INTO `sys_menu` VALUES (1503, '租户删除', 'sys_systenant_del', NULL, 1500, '1', '1', 2, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:56:54', '0', 1);
INSERT INTO `sys_menu` VALUES (1504, '租户套餐', 'sys_systenant_tenantmenu', NULL, 1500, '1', '1', 1, '0', NULL, '1', 'admin', '2022-12-12 09:01:41', ' ', '2023-01-11 05:52:51', '0', 1);
INSERT INTO `sys_menu` VALUES (1505, '租户套餐删除', 'sys_systenantmenu_del', NULL, 1500, '1', '1', 1, '0', NULL, '1', 'admin', '2022-12-09 14:04:19', 'admin', '2023-01-11 05:52:51', '0', 1);
INSERT INTO `sys_menu` VALUES (1506, '租户套餐编辑', 'sys_systenantmenu_edit', NULL, 1500, '1', '1', 1, '0', NULL, '1', 'admin', '2022-12-09 14:04:19', 'admin', '2023-01-11 05:52:51', '0', 1);
INSERT INTO `sys_menu` VALUES (1507, '租户套餐新增', 'sys_systenantmenu_add', NULL, 1500, '1', '1', 1, '0', NULL, '1', 'admin', '2022-12-09 14:04:19', 'admin', '2022-12-12 09:02:00', '0', 1);
INSERT INTO `sys_menu` VALUES (1508, '租户套餐导出', 'sys_systenant_export', NULL, 1500, NULL, '1', 0, '0', '0', '1', 'admin', '2023-03-06 16:28:24', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1600, '岗位管理', NULL, '/admin/post/index', 1000, 'iconfont icon--chaifenhang', '1', 5, '1', '0', '0', '', '2022-03-26 13:04:14', 'admin', '2023-03-07 13:32:25', '0', 1);
INSERT INTO `sys_menu` VALUES (1601, '岗位信息查看', 'sys_post_view', NULL, 1600, NULL, '1', 0, '0', NULL, '1', ' ', '2022-03-26 13:05:34', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1602, '岗位信息新增', 'sys_post_add', NULL, 1600, NULL, '1', 1, '0', NULL, '1', ' ', '2022-03-26 13:06:00', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1603, '岗位信息修改', 'sys_post_edit', NULL, 1600, NULL, '1', 2, '0', NULL, '1', ' ', '2022-03-26 13:06:31', ' ', '2022-03-26 13:06:38', '0', 1);
INSERT INTO `sys_menu` VALUES (1604, '岗位信息删除', 'sys_post_del', NULL, 1600, NULL, '1', 3, '0', NULL, '1', ' ', '2022-03-26 13:06:31', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1605, '岗位导入导出', 'sys_post_export', NULL, 1600, NULL, '1', 4, '0', NULL, '1', ' ', '2022-03-26 13:06:31', ' ', '2022-03-26 06:32:02', '0', 1);
INSERT INTO `sys_menu` VALUES (2000, '系统管理', NULL, '/system', -1, 'iconfont icon-quanjushezhi_o', '1', 1, '0', NULL, '0', '', '2017-11-07 20:56:00', 'admin', '2023-02-16 15:15:56', '0', 1);
INSERT INTO `sys_menu` VALUES (2001, '日志管理', NULL, '/admin/logs', 2000, 'ele-Cloudy', '1', 0, '0', '0', '0', 'admin', '2023-03-02 12:26:42', 'admin', '2023-03-02 12:28:35', '0', 1);
INSERT INTO `sys_menu` VALUES (2100, '操作日志', NULL, '/admin/log/index', 2001, 'iconfont icon-jinridaiban', '1', 2, '0', '0', '0', '', '2017-11-20 14:06:22', 'admin', '2023-03-02 12:28:57', '0', 1);
INSERT INTO `sys_menu` VALUES (2101, '日志删除', 'sys_log_del', NULL, 2100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-20 20:37:37', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2102, '导入导出', 'sys_log_export', NULL, 2100, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2103, '审计日志', NULL, '/admin/audit/index', 2001, 'iconfont icon-biaodan', '1', 1, '0', '0', '0', '', NULL, 'admin', '2023-03-02 12:28:47', '0', 1);
INSERT INTO `sys_menu` VALUES (2104, '审计记录表删除', 'sys_audit_del', NULL, 2103, '1', '1', 3, '0', NULL, '1', '', NULL, 'admin', '2023-02-28 20:23:43', '0', 1);
INSERT INTO `sys_menu` VALUES (2105, '导入导出', 'sys_audit_export', NULL, 2103, '1', '1', 3, '0', NULL, '1', '', NULL, 'admin', '2023-02-28 20:23:51', '0', 1);
INSERT INTO `sys_menu` VALUES (2200, '字典管理', NULL, '/admin/dict/index', 2000, 'iconfont icon-zhongduancanshuchaxun', '1', 6, '0', NULL, '0', '', '2017-11-29 11:30:52', 'admin', '2023-02-16 15:24:29', '0', 1);
INSERT INTO `sys_menu` VALUES (2201, '字典删除', 'sys_dict_del', NULL, 2200, NULL, '1', 1, '0', NULL, '1', ' ', '2017-11-29 11:30:11', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2202, '字典新增', 'sys_dict_add', NULL, 2200, NULL, '1', 1, '0', NULL, '1', ' ', '2018-05-11 22:34:55', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2203, '字典修改', 'sys_dict_edit', NULL, 2200, NULL, '1', 1, '0', NULL, '1', ' ', '2018-05-11 22:36:03', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2210, '参数管理', NULL, '/admin/param/index', 2000, 'iconfont icon-wenducanshu-05', '1', 7, '1', NULL, '0', '', '2019-04-29 22:16:50', 'admin', '2023-02-16 15:24:51', '0', 1);
INSERT INTO `sys_menu` VALUES (2211, '参数新增', 'sys_syspublicparam_add', NULL, 2210, NULL, '1', 1, '0', NULL, '1', ' ', '2019-04-29 22:17:36', ' ', '2020-03-24 08:57:11', '0', 1);
INSERT INTO `sys_menu` VALUES (2212, '参数删除', 'sys_syspublicparam_del', NULL, 2210, NULL, '1', 1, '0', NULL, '1', ' ', '2019-04-29 22:17:55', ' ', '2020-03-24 08:57:12', '0', 1);
INSERT INTO `sys_menu` VALUES (2213, '参数编辑', 'sys_syspublicparam_edit', NULL, 2210, NULL, '1', 1, '0', NULL, '1', ' ', '2019-04-29 22:18:14', ' ', '2020-03-24 08:57:13', '0', 1);
INSERT INTO `sys_menu` VALUES (2300, '代码生成', NULL, '/gen/table/index', 9000, 'iconfont icon-zhongduancanshu', '1', 1, '0', '0', '0', '', '2018-01-20 13:17:19', 'admin', '2023-02-20 13:54:35', '0', 1);
INSERT INTO `sys_menu` VALUES (2400, '终端管理', NULL, '/admin/client/index', 2000, 'iconfont icon-gongju', '1', 9, '1', NULL, '0', '', '2018-01-20 13:17:19', 'admin', '2023-02-16 15:25:28', '0', 1);
INSERT INTO `sys_menu` VALUES (2401, '客户端新增', 'sys_client_add', NULL, 2400, '1', '1', 1, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2402, '客户端修改', 'sys_client_edit', NULL, 2400, NULL, '1', 1, '0', NULL, '1', ' ', '2018-05-15 21:37:06', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2403, '客户端删除', 'sys_client_del', NULL, 2400, NULL, '1', 1, '0', NULL, '1', ' ', '2018-05-15 21:39:16', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2500, '密钥管理', NULL, '/admin/social/index', 2000, 'iconfont icon-quanxian', '1', 10, '0', NULL, '0', '', '2018-01-20 13:17:19', 'admin', '2023-02-16 15:26:16', '0', 1);
INSERT INTO `sys_menu` VALUES (2501, '密钥新增', 'sys_social_details_add', NULL, 2500, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:19', '0', 1);
INSERT INTO `sys_menu` VALUES (2502, '密钥修改', 'sys_social_details_edit', NULL, 2500, '1', '1', 1, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:19', '0', 1);
INSERT INTO `sys_menu` VALUES (2503, '密钥删除', 'sys_social_details_del', NULL, 2500, '1', '1', 2, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:23', '0', 1);
INSERT INTO `sys_menu` VALUES (2600, '令牌管理', NULL, '/admin/token/index', 2000, 'ele-Key', '1', 11, '0', NULL, '0', '', '2018-09-04 05:58:41', 'admin', '2023-02-16 15:28:28', '0', 1);
INSERT INTO `sys_menu` VALUES (2601, '令牌删除', 'sys_token_del', NULL, 2600, NULL, '1', 1, '0', NULL, '1', ' ', '2018-09-04 05:59:50', ' ', '2020-03-24 08:57:24', '0', 1);
INSERT INTO `sys_menu` VALUES (2800, 'Quartz管理', NULL, '/daemon/job-manage/index', 2000, 'ele-AlarmClock', '1', 8, '0', NULL, '0', '', '2018-01-20 13:17:19', 'admin', '2023-02-16 15:25:06', '0', 1);
INSERT INTO `sys_menu` VALUES (2810, '任务新增', 'job_sys_job_add', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:26', '0', 1);
INSERT INTO `sys_menu` VALUES (2820, '任务修改', 'job_sys_job_edit', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:27', '0', 1);
INSERT INTO `sys_menu` VALUES (2830, '任务删除', 'job_sys_job_del', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:28', '0', 1);
INSERT INTO `sys_menu` VALUES (2840, '任务暂停', 'job_sys_job_shutdown_job', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:28', '0', 1);
INSERT INTO `sys_menu` VALUES (2850, '任务开始', 'job_sys_job_start_job', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:29', '0', 1);
INSERT INTO `sys_menu` VALUES (2860, '任务刷新', 'job_sys_job_refresh_job', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:30', '0', 1);
INSERT INTO `sys_menu` VALUES (2870, '执行任务', 'job_sys_job_run_job', NULL, 2800, '1', '1', 0, '0', NULL, '1', ' ', '2019-08-08 15:35:18', ' ', '2020-03-24 08:57:31', '0', 1);
INSERT INTO `sys_menu` VALUES (2871, '导出', 'job_sys_job_export', NULL, 2800, NULL, '1', 0, '0', '0', '1', 'admin', '2023-03-06 15:26:13', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (2900, '国际化管理', NULL, '/admin/i18n/index', 2000, 'iconfont icon-zhongyingzhuanhuan', '1', 8, '0', NULL, '0', '', NULL, 'admin', '2023-02-16 15:25:18', '0', 1);
INSERT INTO `sys_menu` VALUES (2901, '系统表-国际化查看', 'sys_i18n_view', NULL, 2900, '1', '1', 0, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (2902, '系统表-国际化新增', 'sys_i18n_add', NULL, 2900, '1', '1', 1, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (2903, '系统表-国际化修改', 'sys_i18n_edit', NULL, 2900, '1', '1', 2, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (2904, '系统表-国际化删除', 'sys_i18n_del', NULL, 2900, '1', '1', 3, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (2905, '导入导出', 'sys_i18n_export', NULL, 2900, '1', '1', 3, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (2906, '文件管理', NULL, '/admin/file/index', 2000, 'ele-Files', '1', 6, '0', NULL, '0', '', '2019-06-25 12:44:46', 'admin', '2023-02-16 15:24:42', '0', 1);
INSERT INTO `sys_menu` VALUES (2907, '删除文件', 'sys_file_del', NULL, 2906, NULL, '1', 1, '0', NULL, '1', ' ', '2019-06-25 13:41:41', ' ', '2020-03-24 08:58:42', '0', 1);
INSERT INTO `sys_menu` VALUES (2908, '路由管理', NULL, '/ext/route', 2000, 'iconfont icon-diqiu1', '1', 12, '0', NULL, '0', '', '2019-06-25 12:44:46', 'admin', '2023-02-16 15:24:42', '0', 1);
INSERT INTO `sys_menu` VALUES (2909, '路由配置', 'sys_route_manage', NULL, 2908, NULL, '1', 1, '0', NULL, '1', ' ', '2019-06-25 13:41:41', ' ', '2020-03-24 08:58:42', '0', 1);
INSERT INTO `sys_menu` VALUES (3000, '公众号平台', NULL, '/mp', -1, 'iconfont icon-zhongyingzhuanhuan', '1', 10, '0', '0', '0', 'admin', '2023-02-24 10:40:44', 'admin', '2023-03-06 10:05:21', '0', 1);
INSERT INTO `sys_menu` VALUES (3001, '账号管理', NULL, '/mp/wx-account/index', 3000, 'iconfont icon-putong', '1', 0, '0', '0', '0', 'admin', '2023-02-24 10:43:03', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3002, '菜单设置', NULL, '/mp/wx-menu/index', 3000, 'iconfont icon--chaifenlie', '1', 1, '0', '0', '0', 'admin', '2023-02-24 11:16:32', 'admin', '2023-03-11 16:28:56', '0', 1);
INSERT INTO `sys_menu` VALUES (3003, '删除', 'mp_wxaccount_del', NULL, 3001, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-24 13:12:53', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3004, '新增', 'mp_wxaccount_add', NULL, 3001, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-24 13:13:04', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3005, '编辑', 'mp_wxaccount_edit', NULL, 3001, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-24 13:13:15', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3006, '粉丝管理', NULL, '/mp/wx-account-fans/index', 3000, 'iconfont icon-tongzhi3', '1', 2, '0', '0', '0', 'admin', '2023-02-24 13:28:24', 'admin', '2023-03-11 16:29:03', '0', 1);
INSERT INTO `sys_menu` VALUES (3007, '同步粉丝', 'mp_wxaccountfans_sync', NULL, 3006, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-24 14:03:03', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3008, '消息管理', NULL, '/mp/wx-fans-msg/index', 3000, 'iconfont icon-tongzhi3', '1', 6, '0', '0', '0', 'admin', '2023-02-24 15:24:35', 'admin', '2023-03-11 16:32:38', '0', 1);
INSERT INTO `sys_menu` VALUES (3009, '修改微信消息', 'mp_wxmsg_edit', '/mp/wx-account-tag/index', 3008, NULL, '1', 0, '0', '0', '1', 'admin', '2023-02-24 15:41:55', 'admin', '2023-03-01 17:12:44', '0', 1);
INSERT INTO `sys_menu` VALUES (3010, '标签管理', NULL, '/mp/wx-account-tag/index', 3000, 'iconfont icon-zidingyibuju', '1', 3, '0', '0', '0', 'admin', '2023-03-03 09:49:07', 'admin', '2023-03-11 08:32:00', '0', 1);
INSERT INTO `sys_menu` VALUES (3011, '新增标签', 'mp_wx_account_tag_add', NULL, 3010, NULL, '1', 0, '0', '0', '1', 'admin', '2023-03-03 09:49:26', 'admin', '2023-04-07 02:10:27', '0', 1);
INSERT INTO `sys_menu` VALUES (3012, '编辑标签', 'mp_wx_account_tag_edit', NULL, 3010, NULL, '1', 0, '0', '0', '1', 'admin', '2023-03-03 09:49:35', 'admin', '2023-04-07 02:10:25', '0', 1);
INSERT INTO `sys_menu` VALUES (3013, '标签删除', 'mp_wx_account_tag_del', NULL, 3010, NULL, '1', 0, '0', '0', '1', 'admin', '2023-03-03 09:49:45', 'admin', '2023-04-07 02:10:22', '0', 1);
INSERT INTO `sys_menu` VALUES (3014, '同步标签', 'mp_wx_account_tag_sync', NULL, 3010, NULL, '1', 0, '0', '0', '1', 'admin', '2023-03-03 09:49:55', 'admin', '2023-04-07 02:10:20', '0', 1);
INSERT INTO `sys_menu` VALUES (3015, '素材管理', NULL, '/mp/wx-material/index', 3000, 'iconfont icon-tongzhi3', '1', 5, '0', '0', '0', 'admin', '2023-02-27 14:13:47', 'admin', '2023-03-11 16:32:30', '0', 1);
INSERT INTO `sys_menu` VALUES (3016, '素材维护', 'mp_wxmaterial_add', NULL, 3015, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-27 14:14:07', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3017, '素材删除', 'mp_wxmaterial_del', NULL, 3015, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-27 14:14:18', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3018, '自动回复', NULL, '/mp/wx-auto-reply/index', 3000, 'iconfont icon-putong', '1', 4, '0', '0', '0', 'admin', '2023-03-01 10:56:10', 'admin', '2023-03-11 16:32:23', '0', 1);
INSERT INTO `sys_menu` VALUES (3019, '新增回复', 'mp_wxautoreply_add', NULL, 3018, NULL, '0', 0, '0', '0', '1', 'admin', '2023-03-01 10:56:28', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3020, '编辑回复', 'mp_wxautoreply_edit', NULL, 3018, NULL, '0', 0, '0', '0', '1', 'admin', '2023-03-01 10:56:42', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3021, '删除回复', 'mp_wxautoreply_del', NULL, 3018, NULL, '0', 0, '0', '0', '1', 'admin', '2023-03-01 10:56:53', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3022, '运营数据', NULL, '/mp/wx-statistics/index', 3000, 'iconfont icon-shuxing', '1', 8, '0', '0', '0', 'admin', '2023-03-01 11:15:58', 'admin', '2023-03-11 16:32:43', '0', 1);
INSERT INTO `sys_menu` VALUES (3023, '新增消息', 'mp_wxmsg_add', NULL, 3008, NULL, '0', 0, '0', '0', '1', 'admin', '2023-03-01 17:12:02', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3024, '新增粉丝', 'mp_wxaccountfans_add', 'mp_wxaccountfans_add', 3006, NULL, '0', 0, '0', '0', '1', 'admin', '2023-03-02 10:57:41', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3025, '粉丝编辑', 'mp_wxaccountfans_edit', 'mp_wxaccountfans_add', 3006, NULL, '0', 0, '0', '0', '1', 'admin', '2023-03-02 10:57:52', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3026, '粉丝删除', 'mp_wxaccountfans_del', 'mp_wxaccountfans_add', 3006, NULL, '0', 0, '0', '0', '1', 'admin', '2023-03-02 10:58:02', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3027, '新增菜单', 'mp_wxmenu_add', NULL, 3002, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-27 20:54:34', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3028, '发布菜单', 'mp_wxmenu_push', NULL, 3002, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-27 20:54:48', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (3029, '删除菜单', 'mp_wxmenu_del', NULL, 3002, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-27 20:54:57', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (4000, '系统监控', NULL, '/daemon', -1, 'iconfont icon-shuju', '1', 3, '0', '0', '0', 'admin', '2023-02-06 20:20:47', 'admin', '2023-02-23 20:01:07', '0', 1);
INSERT INTO `sys_menu` VALUES (4001, '文档扩展', NULL, 'http://pigx-gateway:9999/admin/doc.html', 4000, 'iconfont icon-biaodan', '1', 2, '0', '1', '0', '', '2018-06-26 10:50:32', 'admin', '2023-02-23 20:01:29', '0', 1);
INSERT INTO `sys_menu` VALUES (4002, '大屏设计', NULL, '/ext/report', -1, 'iconfont icon-putong', '1', 6, '0', '0', '0', 'admin', '2023-04-07 11:04:01', ' ', '2023-04-07 06:22:16', '0', 1);
INSERT INTO `sys_menu` VALUES (4003, '报表设计', NULL, '/ext/jimu', -1, 'iconfont icon-shuju', '1', 7, '0', '0', '0', 'admin', '2023-04-07 13:15:56', 'admin', '2023-04-07 06:22:19', '0', 1);
INSERT INTO `sys_menu` VALUES (4004, '缓存监控', NULL, '/ext/cache', 4000, 'iconfont icon-shuju', '1', 1, '0', '0', '0', 'admin', '2023-05-29 15:12:59', 'admin', '2023-06-06 19:31:18', '0', 1);
INSERT INTO `sys_menu` VALUES (5000, '支付系统', NULL, '/pay', -1, 'iconfont icon-neiqianshujuchucun', '1', 4, '0', '0', '0', 'admin', '2023-02-27 10:57:14', 'admin', '2023-02-27 11:00:47', '0', 1);
INSERT INTO `sys_menu` VALUES (5001, '收银台', NULL, '/pay/cd/index', 5000, 'iconfont icon-diqiu1', '1', 0, '0', '0', '0', 'admin', '2023-02-27 10:58:13', 'admin', '2023-02-27 11:00:14', '0', 1);
INSERT INTO `sys_menu` VALUES (5002, '支付渠道', NULL, '/pay/channel/index', 5000, 'iconfont icon-crew_feature', '1', 1, '0', '0', '0', 'admin', '2023-02-27 19:36:55', 'admin', '2023-02-28 20:02:53', '0', 1);
INSERT INTO `sys_menu` VALUES (5003, '查询', 'pay_channel_view', NULL, 5002, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-27 19:41:44', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5004, '新增', 'pay_channel_add', NULL, 5002, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-27 19:42:05', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5005, '编辑', 'pay_channel_edit', NULL, 5002, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-27 19:42:23', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5006, '删除', 'pay_channel_del', NULL, 5002, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-27 19:42:40', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5007, '导出', 'pay_channel_export', NULL, 5002, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-27 19:42:57', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5008, '商品订单', NULL, '/pay/order/index', 5000, 'iconfont icon-fuwenbenkuang', '1', 2, '0', '0', '0', 'admin', '2023-02-28 09:56:22', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5009, '新增', 'pay_order_add', NULL, 5008, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 09:58:25', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5010, '删除', 'pay_order_del', NULL, 5008, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 09:58:40', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5011, '修改', 'pay_order_edit', NULL, 5008, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 09:59:11', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5012, '查找', 'pay_order_view', NULL, 5008, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 09:59:37', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5013, '导出', 'pay_order_export', NULL, 5008, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 09:59:54', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5014, '通知记录', NULL, '/pay/record/index', 5000, 'iconfont icon-fuwenbenkuang', '1', 5, '0', '0', '0', 'admin', '2023-02-28 11:01:37', 'admin', '2023-02-28 20:06:24', '0', 1);
INSERT INTO `sys_menu` VALUES (5015, '新增', 'pay_record_add', NULL, 5014, NULL, '1', 0, '0', '0', '1', 'admin', '2023-02-28 11:04:40', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5016, '修改', 'pay_record_edit', NULL, 5014, NULL, '1', 0, '0', '0', '1', 'admin', '2023-02-28 11:05:00', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5017, '删除', 'pay_record_del', NULL, 5014, NULL, '1', 0, '0', '0', '1', 'admin', '2023-02-28 11:05:15', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5018, '导出', 'pay_record_export', NULL, 5014, NULL, '1', 0, '0', '0', '1', 'admin', '2023-02-28 11:05:41', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5019, '查询', 'pay_record_view', NULL, 5014, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 11:12:53', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5020, '退款订单', NULL, '/pay/refund/index', 5000, 'iconfont icon-fuwenbenkuang', '1', 4, '0', '0', '0', 'admin', '2023-02-28 13:59:04', 'admin', '2023-02-28 20:03:13', '0', 1);
INSERT INTO `sys_menu` VALUES (5021, '查询', 'pay_refund_view', NULL, 5020, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 13:59:31', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5022, '新增', 'pay_refund_add', NULL, 5020, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 13:59:48', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5023, '修改', 'pay_refund_edit', NULL, 5020, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 14:00:05', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5024, '删除', 'pay_refund_del', NULL, 5020, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 14:00:23', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5025, '导出', 'pay_refund_export', NULL, 5020, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 14:00:35', 'admin', '2023-02-28 14:04:15', '0', 1);
INSERT INTO `sys_menu` VALUES (5026, '支付订单', NULL, '/pay/trade/index', 5000, 'iconfont icon-biaodan', '1', 3, '0', '0', '0', 'admin', '2023-02-28 14:44:59', 'admin', '2023-02-28 20:03:09', '0', 1);
INSERT INTO `sys_menu` VALUES (5027, '查询', 'pay_trade_view', NULL, 5026, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 14:45:50', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5028, '新增', 'pay_trade_add', NULL, 5026, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 14:46:08', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5029, '修改', 'pay_trade_edit', NULL, 5026, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 14:46:22', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5030, '删除', 'pay_trade_del', NULL, 5026, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 14:46:36', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (5031, '导出', 'pay_trade_export', NULL, 5026, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-28 14:46:49', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (6000, '协同办公', NULL, '/flow', -1, 'ele-Present', '1', 5, '0', '0', '0', 'admin', '2023-03-02 16:36:49', 'admin', '2023-07-27 13:12:32', '0', 1);
INSERT INTO `sys_menu` VALUES (6001, '流程管理', NULL, '/flow/group/index', 6000, 'iconfont icon-gongju', '1', 0, '0', '0', '0', 'admin', '2023-03-02 16:37:55', 'admin', '2023-07-27 13:12:42', '0', 1);
INSERT INTO `sys_menu` VALUES (6002, '创建流程', NULL, '/flow/create/all', 6000, 'fa fa-arrow-circle-right', '0', 2, '0', NULL, '0', '', '2023-07-27 13:14:56', 'admin', '2023-07-27 13:32:32', '0', 1);
INSERT INTO `sys_menu` VALUES (6003, '发起流程', NULL, '/flow/list/index', 6000, 'fa fa-play', '1', 1, '0', '0', '0', 'admin', '2023-03-02 18:18:10', 'admin', '2023-07-27 13:29:00', '0', 1);
INSERT INTO `sys_menu` VALUES (6004, '任务管理', NULL, '/task', 6000, 'fa fa-th', '1', 2, '0', '0', '0', 'admin', '2023-03-02 22:13:29', 'admin', '2023-07-27 13:29:17', '0', 1);
INSERT INTO `sys_menu` VALUES (6005, '代办任务', NULL, '/task/pending', 6004, 'fa fa-flag-checkered', '1', 0, '0', '0', '0', 'admin', '2023-03-02 22:59:35', 'admin', '2023-07-27 13:32:18', '0', 1);
INSERT INTO `sys_menu` VALUES (6006, '我的已办', NULL, '/task/completed', 6004, 'fa fa-hand-o-right', '1', 3, '0', '0', '0', 'admin', '2023-03-02 23:23:13', 'admin', '2023-07-27 13:30:51', '0', 1);
INSERT INTO `sys_menu` VALUES (6007, '我的发起', NULL, '/task/started', 6004, 'fa fa-plane', '1', 1, '0', NULL, '0', '', '2023-07-27 13:14:51', 'admin', '2023-07-27 13:29:47', '0', 1);
INSERT INTO `sys_menu` VALUES (6008, '抄送给我', NULL, '/task/cc', 6004, 'fa fa-arrow-circle-right', '1', 2, '0', NULL, '0', '', '2023-07-27 13:14:56', 'admin', '2023-07-27 13:32:32', '0', 1);
INSERT INTO `sys_menu` VALUES (7000, 'APP管理', NULL, '/app', -1, 'ele-Cellphone', '1', 2, '0', '0', '0', 'admin', NULL, 'admin', '2023-02-23 19:54:05', '0', 1);
INSERT INTO `sys_menu` VALUES (7100, '客户管理', NULL, '/app/appuser/index', 7000, 'ele-UserFilled', '1', 1, '1', NULL, '0', 'admin', NULL, 'admin', '2023-02-16 15:29:08', '0', 1);
INSERT INTO `sys_menu` VALUES (7101, '新增用户', 'app_appuser_add', NULL, 7100, NULL, '1', 1, '0', NULL, '1', 'admin', NULL, 'admin', '2023-01-29 07:01:00', '0', 1);
INSERT INTO `sys_menu` VALUES (7102, '编辑用户', 'app_appuser_edit', NULL, 7100, NULL, '1', 1, '0', NULL, '1', 'admin', NULL, 'admin', '2023-01-29 07:01:00', '0', 1);
INSERT INTO `sys_menu` VALUES (7103, '删除用户', 'app_appuser_del', NULL, 7100, NULL, '1', 1, '0', NULL, '1', 'admin', NULL, 'admin', '2023-01-29 07:01:00', '0', 1);
INSERT INTO `sys_menu` VALUES (7104, '导出用户', 'app_appuser_export', NULL, 7100, NULL, '1', 1, '0', NULL, '1', 'admin', NULL, 'admin', '2023-01-29 07:01:00', '0', 1);
INSERT INTO `sys_menu` VALUES (7200, 'APP角色', NULL, '/app/approle/index', 7000, 'ele-Stamp', '1', 2, '0', '0', '0', 'admin', NULL, 'admin', '2023-03-07 14:18:30', '0', 1);
INSERT INTO `sys_menu` VALUES (7201, '删除角色', 'app_approle_del', NULL, 7200, NULL, '1', 1, '0', NULL, '1', 'admin', NULL, 'admin', '2023-01-29 07:01:01', '0', 1);
INSERT INTO `sys_menu` VALUES (7202, '编辑角色', 'app_approle_edit', NULL, 7200, NULL, '1', 1, '0', NULL, '1', 'admin', NULL, 'admin', '2023-01-29 07:01:01', '0', 1);
INSERT INTO `sys_menu` VALUES (7203, '新增角色', 'app_approle_add', NULL, 7200, NULL, '1', 1, '0', NULL, '1', 'admin', NULL, 'admin', '2023-01-29 07:01:01', '0', 1);
INSERT INTO `sys_menu` VALUES (7204, '导出角色', 'app_approle_export', NULL, 7200, NULL, '1', 1, '0', NULL, '1', 'admin', NULL, 'admin', '2023-01-29 07:01:01', '0', 1);
INSERT INTO `sys_menu` VALUES (7300, 'APP秘钥', NULL, '/app/appsocial/index', 7000, 'iconfont icon-quanxian', '1', 3, '0', '0', '0', 'admin', NULL, 'admin', '2023-03-07 14:18:44', '0', 1);
INSERT INTO `sys_menu` VALUES (7301, '删除秘钥', 'app_social_details_del', NULL, 7300, NULL, '1', 1, '0', NULL, '1', 'admin', NULL, 'admin', '2023-01-29 07:01:02', '0', 1);
INSERT INTO `sys_menu` VALUES (7302, '修改秘钥', 'app_social_details_edit', NULL, 7300, NULL, '1', 1, '0', NULL, '1', 'admin', NULL, 'admin', '2023-01-29 07:01:02', '0', 1);
INSERT INTO `sys_menu` VALUES (7303, '保存秘钥', 'app_social_details_add', NULL, 7300, NULL, '1', 1, '0', NULL, '1', 'admin', NULL, 'admin', '2023-01-29 07:01:02', '0', 1);
INSERT INTO `sys_menu` VALUES (7400, '文章资讯', '', '/app/appArticle/index', 7000, 'ele-CollectionTag', '1', 4, '0', NULL, '0', '', NULL, 'admin', '2023-06-07 17:17:20', '0', 1);
INSERT INTO `sys_menu` VALUES (7401, '文章资讯表查看', 'app_appArticle_view', NULL, 7400, '1', '1', 0, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (7402, '文章资讯表新增', 'app_appArticle_add', NULL, 7400, '1', '1', 1, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (7403, '文章资讯表修改', 'app_appArticle_edit', NULL, 7400, '1', '1', 2, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (7404, '文章资讯表删除', 'app_appArticle_del', NULL, 7400, '1', '1', 3, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (7405, '导入导出', 'app_appArticle_export', NULL, 7400, '1', '1', 3, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (7500, '文章分类', '', '/app/appArticleCategory/index', 7000, 'iconfont icon-caidan', '1', 5, '0', NULL, '0', '', NULL, 'admin', '2023-06-07 16:22:53', '0', 1);
INSERT INTO `sys_menu` VALUES (7501, '文章分类表查看', 'app_appArticleCategory_view', NULL, 7500, '1', '1', 0, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (7502, '文章分类表新增', 'app_appArticleCategory_add', NULL, 7500, '1', '1', 1, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (7503, '文章分类表修改', 'app_appArticleCategory_edit', NULL, 7500, '1', '1', 2, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (7504, '文章分类表删除', 'app_appArticleCategory_del', NULL, 7500, '1', '1', 3, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (7505, '导入导出', 'app_appArticleCategory_export', NULL, 7500, '1', '1', 3, '0', NULL, '1', ' ', NULL, ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (7600, '文章发布', NULL, '/app/appArticle/form', 7000, 'iconfont icon-shuaxin', '0', 4, '0', '0', '0', 'admin', '2023-06-07 17:05:32', 'admin', '2023-06-07 17:17:49', '0', 1);
INSERT INTO `sys_menu` VALUES (7700, '界面设置', '', '/app/page/index', 7000, 'iconfont icon-diannao1', '1', 8, '0', NULL, '0', '', NULL, 'admin', '2023-06-20 14:02:01', '0', 1);
INSERT INTO `sys_menu` VALUES (7701, '底部导航', NULL, '/app/tabbar/index', 7000, 'iconfont icon-neiqianshujuchucun', '1', 9, '0', '0', '0', 'admin', '2023-06-14 14:36:08', 'admin', '2023-06-20 14:02:18', '0', 1);
INSERT INTO `sys_menu` VALUES (9000, '开发平台', NULL, '/gen', -1, 'iconfont icon-shuxingtu', '1', 9, '0', '0', '0', '', '2019-08-12 09:35:16', 'admin', '2023-02-23 20:02:24', '0', 1);
INSERT INTO `sys_menu` VALUES (9005, '数据源管理', NULL, '/gen/datasource/index', 9000, 'ele-Coin', '1', 0, '0', NULL, '0', '', '2019-08-12 09:42:11', 'admin', '2023-02-16 15:31:37', '0', 1);
INSERT INTO `sys_menu` VALUES (9006, '表单设计', NULL, '/gen/design/index', 9000, 'iconfont icon-AIshiyanshi', '0', 2, '0', '0', '0', '', '2019-08-16 10:08:56', 'admin', '2023-02-23 14:06:50', '0', 1);
INSERT INTO `sys_menu` VALUES (9007, '生成页面', NULL, '/gen/gener/index', 9000, 'iconfont icon-tongzhi4', '0', 0, '0', '0', '0', 'admin', '2023-02-20 09:58:23', 'admin', '2023-02-20 14:41:43', '0', 1);
INSERT INTO `sys_menu` VALUES (9050, '元数据管理', NULL, '/gen/metadata', 9000, 'iconfont icon--chaifenhang', '1', 9, '0', '0', '0', '', '2018-07-27 01:13:21', 'admin', '2023-02-23 19:55:10', '0', 1);
INSERT INTO `sys_menu` VALUES (9051, '模板管理', NULL, '/gen/template/index', 9050, 'iconfont icon--chaifenhang', '1', 5, '0', '0', '0', 'admin', '2023-02-21 11:22:54', 'admin', '2023-02-23 19:56:03', '0', 1);
INSERT INTO `sys_menu` VALUES (9052, '查询', 'codegen_template_view', NULL, 9051, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 12:33:03', 'admin', '2023-02-21 13:50:54', '0', 1);
INSERT INTO `sys_menu` VALUES (9053, '增加', 'codegen_template_add', NULL, 9051, NULL, '1', 0, '0', '0', '1', 'admin', '2023-02-21 13:34:10', 'admin', '2023-02-21 13:39:49', '0', 1);
INSERT INTO `sys_menu` VALUES (9054, '新增', 'codegen_template_add', NULL, 9051, NULL, '0', 1, '0', '0', '1', 'admin', '2023-02-21 13:51:32', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (9055, '导出', 'codegen_template_export', NULL, 9051, NULL, '0', 2, '0', '0', '1', 'admin', '2023-02-21 13:51:58', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (9056, '删除', 'codegen_template_del', NULL, 9051, NULL, '0', 3, '0', '0', '1', 'admin', '2023-02-21 13:52:16', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (9057, '编辑', 'codegen_template_edit', NULL, 9051, NULL, '0', 4, '0', '0', '1', 'admin', '2023-02-21 13:52:58', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (9059, '模板分组', NULL, '/gen/group/index', 9050, 'iconfont icon-shuxingtu', '1', 6, '0', '0', '0', 'admin', '2023-02-21 15:06:50', 'admin', '2023-02-23 19:55:25', '0', 1);
INSERT INTO `sys_menu` VALUES (9060, '查询', 'codegen_group_view', NULL, 9059, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 15:08:07', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (9061, '新增', 'codegen_group_add', NULL, 9059, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 15:08:28', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (9062, '修改', 'codegen_group_edit', NULL, 9059, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 15:08:43', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (9063, '删除', 'codegen_group_del', NULL, 9059, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 15:09:02', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (9064, '导出', 'codegen_group_export', NULL, 9059, NULL, '0', 0, '0', '0', '1', 'admin', '2023-02-21 15:09:22', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (9065, '字段管理', NULL, '/gen/field-type/index', 9050, 'iconfont icon-fuwenben', '1', 0, '0', '0', '0', 'admin', '2023-02-23 20:05:09', 'admin', '2023-02-23 20:05:45', '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `client_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端ID',
  `resource_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资源ID集合',
  `client_secret` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端秘钥',
  `scope` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权范围',
  `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '授权类型',
  `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回调地址',
  `authorities` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '权限集合',
  `access_token_validity` int(11) DEFAULT NULL COMMENT '访问令牌有效期（秒）',
  `refresh_token_validity` int(11) DEFAULT NULL COMMENT '刷新令牌有效期（秒）',
  `additional_information` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '附加信息',
  `autoapprove` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '自动授权',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='终端信息表';

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client_details` VALUES (1, 'app', NULL, 'app', 'server', 'password,refresh_token,authorization_code,client_credentials,mobile', 'http://localhost:4040/sso1/login,http://localhost:4041/sso1/login,http://localhost:8080/renren-admin/sys/oauth2-sso,http://localhost:8090/sys/oauth2-sso', NULL, 43200, 2592001, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\",\"online_quantity\":\"1\"}', 'true', '0', '', 'admin', NULL, '2023-02-09 13:54:54', 1);
INSERT INTO `sys_oauth_client_details` VALUES (2, 'daemon', NULL, 'daemon', 'server', 'password,refresh_token', NULL, NULL, 43200, 2592001, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\"}', 'true', '0', ' ', ' ', NULL, NULL, 1);
INSERT INTO `sys_oauth_client_details` VALUES (3, 'gen', NULL, 'gen', 'server', 'password,refresh_token', NULL, NULL, 43200, 2592001, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\"}', 'true', '0', ' ', ' ', NULL, NULL, 1);
INSERT INTO `sys_oauth_client_details` VALUES (4, 'mp', NULL, 'mp', 'server', 'password,refresh_token', NULL, NULL, 43200, 2592001, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\"}', 'true', '0', ' ', ' ', NULL, NULL, 1);
INSERT INTO `sys_oauth_client_details` VALUES (5, 'pig', NULL, 'pig', 'server', 'password,refresh_token,authorization_code,client_credentials,mobile', 'http://localhost:4040/sso1/login,http://localhost:4041/sso1/login,http://localhost:8080/renren-admin/sys/oauth2-sso,http://localhost:8090/sys/oauth2-sso', NULL, 43200, 2592001, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\",\"online_quantity\":\"1\"}', 'false', '0', '', 'admin', NULL, '2023-03-08 11:32:41', 1);
INSERT INTO `sys_oauth_client_details` VALUES (6, 'test', NULL, 'test', 'server', 'password,refresh_token', NULL, NULL, 43200, 2592001, '{ \"enc_flag\":\"1\",\"captcha_flag\":\"0\"}', 'true', '0', ' ', ' ', NULL, NULL, 1);
INSERT INTO `sys_oauth_client_details` VALUES (7, 'social', NULL, 'social', 'server', 'password,refresh_token,mobile', NULL, NULL, 43200, 2592001, '{ \"enc_flag\":\"0\",\"captcha_flag\":\"0\"}', 'true', '0', ' ', ' ', NULL, NULL, 1);
INSERT INTO `sys_oauth_client_details` VALUES (1619615858091982850, 'mini', NULL, 'mini', 'server', 'password,mobile', NULL, NULL, 160000000, 160000000, '{\"captcha_flag\":\"0\",\"enc_flag\":\"1\",\"online_quantity\":\"1\"}', 'true', '0', 'admin', 'admin', '2023-01-29 16:38:06', '2023-01-29 17:21:56', 1);
INSERT INTO `sys_oauth_client_details` VALUES (1632018490988806145, 'xzxzccxz', NULL, 'xczcxzxzcxzcxz', 'server', 'password,authorization_code,client_credentials,refresh_token,mobile,implicit', 'https://1237.0.0.1/XX', NULL, 3600, 2592001, '{\"captcha_flag\":\"0\",\"enc_flag\":\"0\",\"online_quantity\":\"1\"}', 'true', '1', 'admin', 'admin', '2023-03-04 22:01:44', '2023-03-09 09:58:12', 1);
INSERT INTO `sys_oauth_client_details` VALUES (1635201365770797057, 'sddsa', NULL, 'sadsad', 'serverxxx', 'password,authorization_code,client_credentials,refresh_token', 'sadds', NULL, 43200, 2592001, '{\"captcha_flag\":\"1\",\"enc_flag\":\"1\",\"online_quantity\":\"1\"}', 'true', '1', 'admin', 'admin', '2023-03-13 16:49:21', '2023-03-13 16:49:31', 1);
INSERT INTO `sys_oauth_client_details` VALUES (1636620644923621377, 'nov', NULL, 'nov', 'server', 'client_credentials', NULL, NULL, 43200, 2592001, '{\"captcha_flag\":\"0\",\"enc_flag\":\"1\",\"online_quantity\":\"1\"}', 'true', '0', 'admin', 'admin', '2023-03-17 14:49:03', '2023-03-17 14:49:46', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  `post_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '岗位名称',
  `post_sort` int(11) NOT NULL COMMENT '岗位排序',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '岗位描述',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '更新人',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_post` VALUES (1, 'TEAM_LEADER', '部门负责人', 0, 'LEADER', '0', '2022-03-26 13:48:17', '', '2023-03-08 16:03:35', 'admin', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_public_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_public_param`;
CREATE TABLE `sys_public_param` (
  `public_id` bigint(20) NOT NULL COMMENT '编号',
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
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`public_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公共参数配置表';

-- ----------------------------
-- Records of sys_public_param
-- ----------------------------
BEGIN;
INSERT INTO `sys_public_param` VALUES (1, '租户默认来源', 'TENANT_DEFAULT_ID', '1', '0', '', ' ', ' ', '2020-05-12 04:03:46', '2020-06-20 08:56:30', '2', '0', '1', 1);
INSERT INTO `sys_public_param` VALUES (2, '租户默认部门名称', 'TENANT_DEFAULT_DEPTNAME', '租户默认部门', '0', '', ' ', ' ', '2020-05-12 03:36:32', NULL, '2', '1', '0', 1);
INSERT INTO `sys_public_param` VALUES (3, '租户默认账户', 'TENANT_DEFAULT_USERNAME', 'admin', '0', '', ' ', ' ', '2020-05-12 04:05:04', NULL, '2', '1', '0', 1);
INSERT INTO `sys_public_param` VALUES (4, '租户默认密码', 'TENANT_DEFAULT_PASSWORD', '123456', '0', '', ' ', ' ', '2020-05-12 04:05:24', NULL, '2', '1', '0', 1);
INSERT INTO `sys_public_param` VALUES (5, '租户默认角色编码', 'TENANT_DEFAULT_ROLECODE', 'ROLE_ADMIN', '0', '', ' ', ' ', '2020-05-12 04:05:57', NULL, '2', '1', '0', 1);
INSERT INTO `sys_public_param` VALUES (6, '租户默认角色名称', 'TENANT_DEFAULT_ROLENAME', '租户默认角色', '0', '', ' ', ' ', '2020-05-12 04:06:19', NULL, '2', '1', '0', 1);
INSERT INTO `sys_public_param` VALUES (7, '表前缀', 'GEN_TABLE_PREFIX', 'tb_', '0', '', ' ', ' ', '2020-05-12 04:23:04', NULL, '9', '1', '0', 1);
INSERT INTO `sys_public_param` VALUES (8, '接口文档不显示的字段', 'GEN_HIDDEN_COLUMNS', 'tenant_id', '0', '', ' ', ' ', '2020-05-12 04:25:19', NULL, '9', '1', '0', 1);
INSERT INTO `sys_public_param` VALUES (9, '注册用户默认角色', 'USER_DEFAULT_ROLE', 'GENERAL_USER', '0', NULL, ' ', ' ', '2022-03-31 16:52:24', NULL, '2', '1', '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色编码',
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
  `ds_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '2' COMMENT '数据权限类型，0全部，1自定义，2本部门及以下，3本部门，4仅本人',
  `ds_scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据权限范围',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`role_id`) USING BTREE,
  KEY `role_idx1_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', '管理员', '0', '', '', 'edg134', '2017-10-29 15:45:51', '2023-04-06 14:03:28', '0', 1);
INSERT INTO `sys_role` VALUES (2, '普通用户', 'GENERAL_USER', '普通用户', '0', '', '', 'admin', '2022-03-31 17:03:15', '2023-04-03 02:28:51', '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
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
INSERT INTO `sys_role_menu` VALUES (1, 1404);
INSERT INTO `sys_role_menu` VALUES (1, 1500);
INSERT INTO `sys_role_menu` VALUES (1, 1501);
INSERT INTO `sys_role_menu` VALUES (1, 1502);
INSERT INTO `sys_role_menu` VALUES (1, 1503);
INSERT INTO `sys_role_menu` VALUES (1, 1504);
INSERT INTO `sys_role_menu` VALUES (1, 1505);
INSERT INTO `sys_role_menu` VALUES (1, 1506);
INSERT INTO `sys_role_menu` VALUES (1, 1507);
INSERT INTO `sys_role_menu` VALUES (1, 1508);
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
INSERT INTO `sys_role_menu` VALUES (1, 2103);
INSERT INTO `sys_role_menu` VALUES (1, 2104);
INSERT INTO `sys_role_menu` VALUES (1, 2105);
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
INSERT INTO `sys_role_menu` VALUES (1, 2500);
INSERT INTO `sys_role_menu` VALUES (1, 2501);
INSERT INTO `sys_role_menu` VALUES (1, 2502);
INSERT INTO `sys_role_menu` VALUES (1, 2503);
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
INSERT INTO `sys_role_menu` VALUES (1, 2900);
INSERT INTO `sys_role_menu` VALUES (1, 2901);
INSERT INTO `sys_role_menu` VALUES (1, 2902);
INSERT INTO `sys_role_menu` VALUES (1, 2903);
INSERT INTO `sys_role_menu` VALUES (1, 2904);
INSERT INTO `sys_role_menu` VALUES (1, 2905);
INSERT INTO `sys_role_menu` VALUES (1, 2906);
INSERT INTO `sys_role_menu` VALUES (1, 2907);
INSERT INTO `sys_role_menu` VALUES (1, 2908);
INSERT INTO `sys_role_menu` VALUES (1, 3000);
INSERT INTO `sys_role_menu` VALUES (1, 3001);
INSERT INTO `sys_role_menu` VALUES (1, 3002);
INSERT INTO `sys_role_menu` VALUES (1, 3003);
INSERT INTO `sys_role_menu` VALUES (1, 3004);
INSERT INTO `sys_role_menu` VALUES (1, 3005);
INSERT INTO `sys_role_menu` VALUES (1, 3006);
INSERT INTO `sys_role_menu` VALUES (1, 3007);
INSERT INTO `sys_role_menu` VALUES (1, 3008);
INSERT INTO `sys_role_menu` VALUES (1, 3009);
INSERT INTO `sys_role_menu` VALUES (1, 3010);
INSERT INTO `sys_role_menu` VALUES (1, 3011);
INSERT INTO `sys_role_menu` VALUES (1, 3012);
INSERT INTO `sys_role_menu` VALUES (1, 3013);
INSERT INTO `sys_role_menu` VALUES (1, 3014);
INSERT INTO `sys_role_menu` VALUES (1, 3015);
INSERT INTO `sys_role_menu` VALUES (1, 3016);
INSERT INTO `sys_role_menu` VALUES (1, 3017);
INSERT INTO `sys_role_menu` VALUES (1, 3018);
INSERT INTO `sys_role_menu` VALUES (1, 3019);
INSERT INTO `sys_role_menu` VALUES (1, 3020);
INSERT INTO `sys_role_menu` VALUES (1, 3021);
INSERT INTO `sys_role_menu` VALUES (1, 3022);
INSERT INTO `sys_role_menu` VALUES (1, 3023);
INSERT INTO `sys_role_menu` VALUES (1, 3024);
INSERT INTO `sys_role_menu` VALUES (1, 3025);
INSERT INTO `sys_role_menu` VALUES (1, 3026);
INSERT INTO `sys_role_menu` VALUES (1, 3027);
INSERT INTO `sys_role_menu` VALUES (1, 3028);
INSERT INTO `sys_role_menu` VALUES (1, 3029);
INSERT INTO `sys_role_menu` VALUES (1, 4000);
INSERT INTO `sys_role_menu` VALUES (1, 4001);
INSERT INTO `sys_role_menu` VALUES (1, 4002);
INSERT INTO `sys_role_menu` VALUES (1, 4003);
INSERT INTO `sys_role_menu` VALUES (1, 4004);
INSERT INTO `sys_role_menu` VALUES (1, 5000);
INSERT INTO `sys_role_menu` VALUES (1, 5001);
INSERT INTO `sys_role_menu` VALUES (1, 5002);
INSERT INTO `sys_role_menu` VALUES (1, 5003);
INSERT INTO `sys_role_menu` VALUES (1, 5004);
INSERT INTO `sys_role_menu` VALUES (1, 5005);
INSERT INTO `sys_role_menu` VALUES (1, 5006);
INSERT INTO `sys_role_menu` VALUES (1, 5007);
INSERT INTO `sys_role_menu` VALUES (1, 5008);
INSERT INTO `sys_role_menu` VALUES (1, 5009);
INSERT INTO `sys_role_menu` VALUES (1, 5010);
INSERT INTO `sys_role_menu` VALUES (1, 5011);
INSERT INTO `sys_role_menu` VALUES (1, 5012);
INSERT INTO `sys_role_menu` VALUES (1, 5013);
INSERT INTO `sys_role_menu` VALUES (1, 5014);
INSERT INTO `sys_role_menu` VALUES (1, 5015);
INSERT INTO `sys_role_menu` VALUES (1, 5016);
INSERT INTO `sys_role_menu` VALUES (1, 5017);
INSERT INTO `sys_role_menu` VALUES (1, 5018);
INSERT INTO `sys_role_menu` VALUES (1, 5019);
INSERT INTO `sys_role_menu` VALUES (1, 5020);
INSERT INTO `sys_role_menu` VALUES (1, 5021);
INSERT INTO `sys_role_menu` VALUES (1, 5022);
INSERT INTO `sys_role_menu` VALUES (1, 5023);
INSERT INTO `sys_role_menu` VALUES (1, 5024);
INSERT INTO `sys_role_menu` VALUES (1, 5025);
INSERT INTO `sys_role_menu` VALUES (1, 5026);
INSERT INTO `sys_role_menu` VALUES (1, 5027);
INSERT INTO `sys_role_menu` VALUES (1, 5028);
INSERT INTO `sys_role_menu` VALUES (1, 5029);
INSERT INTO `sys_role_menu` VALUES (1, 5030);
INSERT INTO `sys_role_menu` VALUES (1, 5031);
INSERT INTO `sys_role_menu` VALUES (1, 6000);
INSERT INTO `sys_role_menu` VALUES (1, 6001);
INSERT INTO `sys_role_menu` VALUES (1, 6002);
INSERT INTO `sys_role_menu` VALUES (1, 6003);
INSERT INTO `sys_role_menu` VALUES (1, 6004);
INSERT INTO `sys_role_menu` VALUES (1, 6005);
INSERT INTO `sys_role_menu` VALUES (1, 6006);
INSERT INTO `sys_role_menu` VALUES (1, 6007);
INSERT INTO `sys_role_menu` VALUES (1, 6008);
INSERT INTO `sys_role_menu` VALUES (1, 7000);
INSERT INTO `sys_role_menu` VALUES (1, 7100);
INSERT INTO `sys_role_menu` VALUES (1, 7101);
INSERT INTO `sys_role_menu` VALUES (1, 7102);
INSERT INTO `sys_role_menu` VALUES (1, 7103);
INSERT INTO `sys_role_menu` VALUES (1, 7104);
INSERT INTO `sys_role_menu` VALUES (1, 7200);
INSERT INTO `sys_role_menu` VALUES (1, 7201);
INSERT INTO `sys_role_menu` VALUES (1, 7202);
INSERT INTO `sys_role_menu` VALUES (1, 7203);
INSERT INTO `sys_role_menu` VALUES (1, 7204);
INSERT INTO `sys_role_menu` VALUES (1, 7300);
INSERT INTO `sys_role_menu` VALUES (1, 7301);
INSERT INTO `sys_role_menu` VALUES (1, 7302);
INSERT INTO `sys_role_menu` VALUES (1, 7303);
INSERT INTO `sys_role_menu` VALUES (1, 7304);
INSERT INTO `sys_role_menu` VALUES (1, 7400);
INSERT INTO `sys_role_menu` VALUES (1, 7401);
INSERT INTO `sys_role_menu` VALUES (1, 7402);
INSERT INTO `sys_role_menu` VALUES (1, 7403);
INSERT INTO `sys_role_menu` VALUES (1, 7404);
INSERT INTO `sys_role_menu` VALUES (1, 7500);
INSERT INTO `sys_role_menu` VALUES (1, 7501);
INSERT INTO `sys_role_menu` VALUES (1, 7502);
INSERT INTO `sys_role_menu` VALUES (1, 7503);
INSERT INTO `sys_role_menu` VALUES (1, 7504);
INSERT INTO `sys_role_menu` VALUES (1, 7600);
INSERT INTO `sys_role_menu` VALUES (1, 7700);
INSERT INTO `sys_role_menu` VALUES (1, 7701);
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
COMMIT;

-- ----------------------------
-- Table structure for sys_route_conf
-- ----------------------------
DROP TABLE IF EXISTS `sys_route_conf`;
CREATE TABLE `sys_route_conf` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `route_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `route_id` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `predicates` json DEFAULT NULL COMMENT '断言',
  `filters` json DEFAULT NULL COMMENT '过滤器',
  `uri` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `metadata` json DEFAULT NULL COMMENT '路由元信息',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='路由配置表';

-- ----------------------------
-- Records of sys_route_conf
-- ----------------------------
BEGIN;
INSERT INTO `sys_route_conf` VALUES (1, '工作流管理模块', 'pigx-oa-platform', '[{\"args\": {\"_genkey_0\": \"/act/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-oa-platform', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:36:56', '0');
INSERT INTO `sys_route_conf` VALUES (2, '认证中心', 'pigx-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', '[{\"args\": {}, \"name\": \"ValidateCodeGatewayFilter\"}, {\"args\": {}, \"name\": \"PasswordDecoderFilter\"}]', 'lb://pigx-auth', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:36:57', '0');
INSERT INTO `sys_route_conf` VALUES (3, '代码生成模块', 'pigx-codegen', '[{\"args\": {\"_genkey_0\": \"/gen/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-codegen', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:36:58', '0');
INSERT INTO `sys_route_conf` VALUES (4, 'elastic-job定时任务模块', 'pigx-daemon-elastic-job', '[{\"args\": {\"_genkey_0\": \"/daemon/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-daemon-elastic-job', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:36:59', '0');
INSERT INTO `sys_route_conf` VALUES (5, 'quartz定时任务模块', 'pigx-daemon-quartz', '[{\"args\": {\"_genkey_0\": \"/job/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-daemon-quartz', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:02', '0');
INSERT INTO `sys_route_conf` VALUES (6, '分布式事务模块', 'pigx-tx-manager', '[{\"args\": {\"_genkey_0\": \"/tx/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-tx-manager', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:04', '0');
INSERT INTO `sys_route_conf` VALUES (7, '通用权限模块', 'pigx-upms-biz', '[{\"args\": {\"_genkey_0\": \"/admin/**\"}, \"name\": \"Path\"}]', '[{\"args\": {\"key-resolver\": \"#{@remoteAddrKeyResolver}\", \"redis-rate-limiter.burstCapacity\": \"1000\", \"redis-rate-limiter.replenishRate\": \"1000\"}, \"name\": \"RequestRateLimiter\"}]', 'lb://pigx-upms-biz', 0, '{\"response-timeout\": \"30000\"}', ' ', ' ', '2019-10-16 16:44:41', '2021-12-14 13:24:55', '0');
INSERT INTO `sys_route_conf` VALUES (8, '工作流长链接支持', 'pigx-oa-platform-ws', '[{\"args\": {\"_genkey_0\": \"/act/ws/**\"}, \"name\": \"Path\"}]', '[]', 'lb:ws://pigx-oa-platform', 100, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:09', '0');
INSERT INTO `sys_route_conf` VALUES (9, '微信公众号管理', 'pigx-mp-platform', '[{\"args\": {\"_genkey_0\": \"/mp/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-mp-platform', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:12', '0');
INSERT INTO `sys_route_conf` VALUES (10, '支付管理', 'pigx-pay-platform', '[{\"args\": {\"_genkey_0\": \"/pay/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-pay-platform', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:13', '0');
INSERT INTO `sys_route_conf` VALUES (11, '监控管理', 'pigx-monitor', '[{\"args\": {\"_genkey_0\": \"/monitor/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-monitor', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:17', '0');
INSERT INTO `sys_route_conf` VALUES (12, '积木报表', 'pigx-jimu-platform\n', '[{\"args\": {\"_genkey_0\": \"/jimu/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-jimu-platform', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:17', '0');
INSERT INTO `sys_route_conf` VALUES (13, '大屏设计', 'pigx-report-platform', '[{\"args\": {\"_genkey_0\": \"/gv/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-report-platform', 0, '{}', ' ', ' ', '2022-08-27 02:38:43', '2023-04-05 07:52:27', '0');
INSERT INTO `sys_route_conf` VALUES (14, 'APP服务', 'pigx-app-server', '[{\"args\": {\"_genkey_0\": \"/app/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-app-server-biz', 0, '{}', 'admin', ' ', '2022-12-07 10:53:44', NULL, '0');
INSERT INTO `sys_route_conf` VALUES (15, '工作流引擎', 'pigx-flow-task-biz', '[{\"args\": {\"_genkey_0\": \"/task/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-flow-task-biz', 0, '{}', ' ', ' ', '2023-07-28 16:50:26', NULL, '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_schedule
-- ----------------------------
DROP TABLE IF EXISTS `sys_schedule`;
CREATE TABLE `sys_schedule` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标题',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日程类型',
  `state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '状态',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '内容',
  `time` time DEFAULT NULL COMMENT '时间',
  `date` date DEFAULT NULL COMMENT '日期',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  `tenant_id` bigint(20) unsigned DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of sys_schedule
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_social_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_social_details`;
CREATE TABLE `sys_social_details` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '社交登录类型',
  `remark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用ID',
  `app_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用密钥',
  `redirect_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回调地址',
  `ext` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '拓展字段',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统社交登录账号表';

-- ----------------------------
-- Records of sys_social_details
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
  `id` bigint(20) NOT NULL COMMENT '租户ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户名称',
  `code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户编码',
  `tenant_domain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租户域名',
  `website_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '网站名称',
  `mini_qr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '移动端二维码',
  `background` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录页背景图',
  `footer` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '页脚信息',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'logo',
  `start_time` datetime DEFAULT NULL COMMENT '租户开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '租户结束时间',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '租户状态，0正常，1停用',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `menu_id` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '租户菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='租户表';

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant` VALUES (1, '北京分公司', '1', '', NULL, NULL, NULL, NULL, NULL, '2019-05-15 00:00:00', '2029-05-15 00:00:00', '0', '0', '', 'admin', '2019-05-15 15:44:57', '2023-07-30 14:52:57', 1642752536722997250);
COMMIT;


-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '盐值',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '电话号码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '姓名',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱地址',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '所属部门ID',
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
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户ID',
  PRIMARY KEY (`user_id`) USING BTREE,
  KEY `user_wx_openid` (`wx_openid`) USING BTREE,
  KEY `user_qq_openid` (`qq_openid`) USING BTREE,
  KEY `user_idx1_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$c/Ae0pRjJtMZg3BnvVpO.eIK6WYWVbKTzqgdy3afR7w.vd.xi3Mgy', '', '17034642999', '/admin/sys-file/local/46811160fd824c7998303a73fb368076.png', '管理员666777', '管理员', 'sw@mail.pigxl.vip', 4, ' ', 'admin', '2018-04-20 07:15:18', '2023-04-06 16:04:23', '0', '0', NULL, 'oBxPy5E-v82xWGsfzZVzkD3wEX64', NULL, 'log4j', NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
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
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
