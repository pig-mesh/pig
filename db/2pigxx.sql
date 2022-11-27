USE pigxx;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
                            `dept_id` bigint NOT NULL,
                            `name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序',
                            `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                            `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                            `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                            `parent_id` bigint DEFAULT NULL,
                            `tenant_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES (1, '山东', 1, ' ', ' ', '2018-01-22 19:00:23', '2019-05-18 14:56:06', '0', 0, 1);
INSERT INTO `sys_dept` VALUES (2, '沙县国际', 2, ' ', ' ', '2018-01-22 19:00:38', '2019-05-18 14:12:07', '0', 0, 1);
INSERT INTO `sys_dept` VALUES (3, '潍坊', 3, ' ', ' ', '2018-01-22 19:00:44', '2019-05-18 14:56:11', '0', 1, 1);
INSERT INTO `sys_dept` VALUES (4, '高新', 4, ' ', ' ', '2018-01-22 19:00:52', '2019-05-18 14:56:09', '0', 3, 1);
INSERT INTO `sys_dept` VALUES (5, '院校', 5, ' ', ' ', '2018-01-22 19:00:57', '2019-05-18 14:56:13', '0', 4, 1);
INSERT INTO `sys_dept` VALUES (6, '潍院', 6, ' ', ' ', '2018-01-22 19:01:06', '2019-05-18 14:56:16', '1', 5, 1);
INSERT INTO `sys_dept` VALUES (7, '山东沙县', 7, ' ', ' ', '2018-01-22 19:01:57', '2019-05-18 14:12:17', '0', 2, 1);
INSERT INTO `sys_dept` VALUES (8, '潍坊沙县', 8, ' ', ' ', '2018-01-22 19:02:03', '2019-05-18 14:12:19', '0', 7, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_dept_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_relation`;
CREATE TABLE `sys_dept_relation` (
                                     `ancestor` bigint NOT NULL COMMENT '祖先节点',
                                     `descendant` bigint NOT NULL COMMENT '后代节点',
                                     PRIMARY KEY (`ancestor`,`descendant`) USING BTREE,
                                     KEY `idx1` (`ancestor`) USING BTREE,
                                     KEY `idx2` (`descendant`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='部门关系表';

-- ----------------------------
-- Records of sys_dept_relation
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept_relation` VALUES (1, 1);
INSERT INTO `sys_dept_relation` VALUES (1, 3);
INSERT INTO `sys_dept_relation` VALUES (1, 4);
INSERT INTO `sys_dept_relation` VALUES (1, 5);
INSERT INTO `sys_dept_relation` VALUES (2, 2);
INSERT INTO `sys_dept_relation` VALUES (2, 7);
INSERT INTO `sys_dept_relation` VALUES (2, 8);
INSERT INTO `sys_dept_relation` VALUES (2, 11);
INSERT INTO `sys_dept_relation` VALUES (2, 12);
INSERT INTO `sys_dept_relation` VALUES (3, 3);
INSERT INTO `sys_dept_relation` VALUES (3, 4);
INSERT INTO `sys_dept_relation` VALUES (3, 5);
INSERT INTO `sys_dept_relation` VALUES (4, 4);
INSERT INTO `sys_dept_relation` VALUES (4, 5);
INSERT INTO `sys_dept_relation` VALUES (5, 5);
INSERT INTO `sys_dept_relation` VALUES (7, 7);
INSERT INTO `sys_dept_relation` VALUES (7, 8);
INSERT INTO `sys_dept_relation` VALUES (7, 11);
INSERT INTO `sys_dept_relation` VALUES (7, 12);
INSERT INTO `sys_dept_relation` VALUES (8, 8);
COMMIT;

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
                             `post_id` bigint NOT NULL COMMENT '岗位ID',
                             `post_code` varchar(64) CHARACTER SET utf8mb4 NOT NULL COMMENT '岗位编码',
                             `post_name` varchar(50) CHARACTER SET utf8mb4 NOT NULL COMMENT '岗位名称',
                             `post_sort` int(0) NOT NULL COMMENT '岗位排序',
                             `remark` varchar(500) CHARACTER SET utf8mb4 NULL DEFAULT NULL COMMENT '岗位描述',
                             `del_flag` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
                             `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
                             `create_by` varchar(64) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '创建人',
                             `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
                             `update_by` varchar(64) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '更新人',
                             `tenant_id` bigint(0) NULL DEFAULT NULL COMMENT '租户ID',
                             PRIMARY KEY (`post_id`) USING BTREE
) ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_post` VALUES (1, 'CTO', 'CTO', 0, 'CTO', '0', '2022-03-26 13:48:17', '', '2022-03-26 13:50:14', '', 1);
INSERT INTO `sys_post` VALUES (2, 'CEO', 'CEO', 1, 'CEO', '0', '2022-03-26 13:48:27', '', '2022-03-26 13:48:38', '', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
                                  `user_id` bigint(0) NOT NULL COMMENT '用户ID',
                                  `post_id` bigint(0) NOT NULL COMMENT '岗位ID',
                                  PRIMARY KEY (`user_id`, `post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT = '用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_post` VALUES (1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
                            `id` bigint NOT NULL COMMENT '编号',
                            `dict_type` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `description` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                            `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                            `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `remarks` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `system_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                            `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                            `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '所属租户',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `sys_dict_del_flag` (`del_flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='字典表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES (1, 'log_type', '日志类型', ' ', ' ', '2019-03-19 11:06:44', '2019-03-19 11:06:44', '异常、正常', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (2, 'social_type', '社交登录', ' ', ' ', '2019-03-19 11:09:44', '2019-03-19 11:09:44', '微信、QQ', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (3, 'leave_status', '请假状态', ' ', ' ', '2019-03-19 11:09:44', '2019-03-19 11:09:44', '未提交、审批中、完成、驳回', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (4, 'job_type', '定时任务类型', ' ', ' ', '2019-03-19 11:22:21', '2019-03-19 11:22:21', 'quartz', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (5, 'job_status', '定时任务状态', ' ', ' ', '2019-03-19 11:24:57', '2019-03-19 11:24:57', '发布状态、运行状态', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (6, 'job_execute_status', '定时任务执行状态', ' ', ' ', '2019-03-19 11:26:15', '2019-03-19 11:26:15', '正常、异常', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (7, 'misfire_policy', '定时任务错失执行策略', ' ', ' ', '2019-03-19 11:27:19', '2019-03-19 11:27:19', '周期', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (8, 'gender', '性别', ' ', ' ', '2019-03-27 13:44:06', '2019-03-27 13:44:06', '微信用户性别', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (9, 'subscribe', '订阅状态', ' ', ' ', '2019-03-27 13:48:33', '2019-03-27 13:48:33', '公众号订阅状态', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (10, 'response_type', '回复', ' ', ' ', '2019-03-28 21:29:21', '2019-03-28 21:29:21', '微信消息是否已回复', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (11, 'param_type', '参数配置', ' ', ' ', '2019-04-29 18:20:47', '2019-04-29 18:20:47', '检索、原文、报表、安全、文档、消息、其他', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (12, 'status_type', '租户状态', ' ', ' ', '2019-05-15 16:31:08', '2019-05-15 16:31:08', '租户状态', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (13, 'dict_type', '字典类型', ' ', ' ', '2019-05-16 14:16:20', '2019-05-16 14:20:16', '系统类不能修改', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (14, 'channel_status', '支付渠道状态', ' ', ' ', '2019-05-30 16:14:43', '2019-05-30 16:14:43', '支付渠道状态（0-正常，1-冻结）', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (15, 'channel_id', '渠道编码ID', ' ', ' ', '2019-05-30 18:59:12', '2019-05-30 18:59:12', '不同的支付方式', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (16, 'order_status', '订单状态', ' ', ' ', '2019-06-27 08:17:40', '2019-06-27 08:17:40', '支付订单状态', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (17, 'grant_types', '授权类型', ' ', ' ', '2019-08-13 07:34:10', '2019-08-13 07:34:10', NULL, '1', '0', 1);
INSERT INTO `sys_dict` VALUES (18, 'style_type', '前端风格', ' ', ' ', '2020-02-07 03:49:28', '2020-02-07 03:50:40', '0-Avue 1-element', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (19, 'captcha_flag_types', '验证码开关', ' ', ' ', '2020-11-18 06:53:25', '2020-11-18 06:53:25', '是否校验验证码', '1', '0', 1);
INSERT INTO `sys_dict` VALUES (20, 'enc_flag_types', '前端密码加密', ' ', ' ', '2020-11-18 06:54:44', '2020-11-18 06:54:44', '前端密码是否加密传输', '1', '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
                                 `id` bigint NOT NULL COMMENT '编号',
                                 `dict_id` bigint NOT NULL,
                                 `item_value` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
                                 `label` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
                                 `dict_type` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
                                 `description` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
                                 `sort_order` int NOT NULL DEFAULT '0' COMMENT '排序（升序）',
                                 `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                                 `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                 `remarks` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                                 `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                 `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '所属租户',
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
INSERT INTO `sys_dict_item` VALUES (5, 3, '0', '未提交', 'leave_status', '未提交', 0, ' ', ' ', '2019-03-19 11:18:34', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (6, 3, '1', '审批中', 'leave_status', '审批中', 1, ' ', ' ', '2019-03-19 11:18:45', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (7, 3, '2', '完成', 'leave_status', '完成', 2, ' ', ' ', '2019-03-19 11:19:02', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (8, 3, '9', '驳回', 'leave_status', '驳回', 9, ' ', ' ', '2019-03-19 11:19:20', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (9, 4, '1', 'java类', 'job_type', 'java类', 1, ' ', ' ', '2019-03-19 11:22:37', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (10, 4, '2', 'spring bean', 'job_type', 'spring bean容器实例', 2, ' ', ' ', '2019-03-19 11:23:05', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (11, 4, '9', '其他', 'job_type', '其他类型', 9, ' ', ' ', '2019-03-19 11:23:31', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (12, 4, '3', 'Rest 调用', 'job_type', 'Rest 调用', 3, ' ', ' ', '2019-03-19 11:23:57', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (13, 4, '4', 'jar', 'job_type', 'jar类型', 4, ' ', ' ', '2019-03-19 11:24:20', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (14, 5, '1', '未发布', 'job_status', '未发布', 1, ' ', ' ', '2019-03-19 11:25:18', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (15, 5, '2', '运行中', 'job_status', '运行中', 2, ' ', ' ', '2019-03-19 11:25:31', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (16, 5, '3', '暂停', 'job_status', '暂停', 3, ' ', ' ', '2019-03-19 11:25:42', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (17, 6, '0', '正常', 'job_execute_status', '正常', 0, ' ', ' ', '2019-03-19 11:26:27', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (18, 6, '1', '异常', 'job_execute_status', '异常', 1, ' ', ' ', '2019-03-19 11:26:41', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (19, 7, '1', '错失周期立即执行', 'misfire_policy', '错失周期立即执行', 1, ' ', ' ', '2019-03-19 11:27:45', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (20, 7, '2', '错失周期执行一次', 'misfire_policy', '错失周期执行一次', 2, ' ', ' ', '2019-03-19 11:27:57', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (21, 7, '3', '下周期执行', 'misfire_policy', '下周期执行', 3, ' ', ' ', '2019-03-19 11:28:08', '2019-03-25 12:49:36', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (22, 8, '1', '男', 'gender', '微信-男', 0, ' ', ' ', '2019-03-27 13:45:13', '2019-03-27 13:45:13', '微信-男', '0', 1);
INSERT INTO `sys_dict_item` VALUES (23, 8, '2', '女', 'gender', '女-微信', 1, ' ', ' ', '2019-03-27 13:45:34', '2019-03-27 13:45:34', '女-微信', '0', 1);
INSERT INTO `sys_dict_item` VALUES (24, 8, '0', '未知', 'gender', 'x性别未知', 3, ' ', ' ', '2019-03-27 13:45:57', '2019-03-27 13:45:57', 'x性别未知', '0', 1);
INSERT INTO `sys_dict_item` VALUES (25, 9, '0', '未关注', 'subscribe', '公众号-未关注', 0, ' ', ' ', '2019-03-27 13:49:07', '2019-03-27 13:49:07', '公众号-未关注', '0', 1);
INSERT INTO `sys_dict_item` VALUES (26, 9, '1', '已关注', 'subscribe', '公众号-已关注', 1, ' ', ' ', '2019-03-27 13:49:26', '2019-03-27 13:49:26', '公众号-已关注', '0', 1);
INSERT INTO `sys_dict_item` VALUES (27, 10, '0', '未回复', 'response_type', '微信消息-未回复', 0, ' ', ' ', '2019-03-28 21:29:47', '2019-03-28 21:29:47', '微信消息-未回复', '0', 1);
INSERT INTO `sys_dict_item` VALUES (28, 10, '1', '已回复', 'response_type', '微信消息-已回复', 1, ' ', ' ', '2019-03-28 21:30:08', '2019-03-28 21:30:08', '微信消息-已回复', '0', 1);
INSERT INTO `sys_dict_item` VALUES (29, 11, '1', '检索', 'param_type', '检索', 0, ' ', ' ', '2019-04-29 18:22:17', '2019-04-29 18:22:17', '检索', '0', 1);
INSERT INTO `sys_dict_item` VALUES (30, 11, '2', '原文', 'param_type', '原文', 0, ' ', ' ', '2019-04-29 18:22:27', '2019-04-29 18:22:27', '原文', '0', 1);
INSERT INTO `sys_dict_item` VALUES (31, 11, '3', '报表', 'param_type', '报表', 0, ' ', ' ', '2019-04-29 18:22:36', '2019-04-29 18:22:36', '报表', '0', 1);
INSERT INTO `sys_dict_item` VALUES (32, 11, '4', '安全', 'param_type', '安全', 0, ' ', ' ', '2019-04-29 18:22:46', '2019-04-29 18:22:46', '安全', '0', 1);
INSERT INTO `sys_dict_item` VALUES (33, 11, '5', '文档', 'param_type', '文档', 0, ' ', ' ', '2019-04-29 18:22:56', '2019-04-29 18:22:56', '文档', '0', 1);
INSERT INTO `sys_dict_item` VALUES (34, 11, '6', '消息', 'param_type', '消息', 0, ' ', ' ', '2019-04-29 18:23:05', '2019-04-29 18:23:05', '消息', '0', 1);
INSERT INTO `sys_dict_item` VALUES (35, 11, '9', '其他', 'param_type', '其他', 0, ' ', ' ', '2019-04-29 18:23:16', '2019-04-29 18:23:16', '其他', '0', 1);
INSERT INTO `sys_dict_item` VALUES (36, 11, '0', '默认', 'param_type', '默认', 0, ' ', ' ', '2019-04-29 18:23:30', '2019-04-29 18:23:30', '默认', '0', 1);
INSERT INTO `sys_dict_item` VALUES (37, 12, '0', '正常', 'status_type', '状态正常', 0, ' ', ' ', '2019-05-15 16:31:34', '2019-05-16 22:30:46', '状态正常', '0', 1);
INSERT INTO `sys_dict_item` VALUES (38, 12, '9', '冻结', 'status_type', '状态冻结', 1, ' ', ' ', '2019-05-15 16:31:56', '2019-05-16 22:30:50', '状态冻结', '0', 1);
INSERT INTO `sys_dict_item` VALUES (39, 13, '1', '系统类', 'dict_type', '系统类字典', 0, ' ', ' ', '2019-05-16 14:20:40', '2019-05-16 14:20:40', '不能修改删除', '0', 1);
INSERT INTO `sys_dict_item` VALUES (40, 13, '0', '业务类', 'dict_type', '业务类字典', 0, ' ', ' ', '2019-05-16 14:20:59', '2019-05-16 14:20:59', '可以修改', '0', 1);
INSERT INTO `sys_dict_item` VALUES (41, 14, '0', '正常', 'channel_status', '支付渠道状态正常', 0, ' ', ' ', '2019-05-30 16:16:51', '2019-05-30 16:16:51', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (42, 14, '1', '冻结', 'channel_status', '支付渠道冻结', 0, ' ', ' ', '2019-05-30 16:17:08', '2019-05-30 16:17:08', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (43, 15, 'ALIPAY_WAP', '支付宝wap支付', 'channel_id', '支付宝扫码支付', 0, ' ', ' ', '2019-05-30 19:03:16', '2019-06-18 13:51:42', '支付宝wap支付', '0', 1);
INSERT INTO `sys_dict_item` VALUES (44, 15, 'WEIXIN_MP', '微信公众号支付', 'channel_id', '微信公众号支付', 1, ' ', ' ', '2019-05-30 19:08:08', '2019-06-18 13:51:53', '微信公众号支付', '0', 1);
INSERT INTO `sys_dict_item` VALUES (45, 16, '1', '支付成功', 'order_status', '支付成功', 1, ' ', ' ', '2019-06-27 08:18:26', '2019-06-27 08:18:26', '订单支付成功', '0', 1);
INSERT INTO `sys_dict_item` VALUES (46, 16, '2', '支付完成', 'order_status', '订单支付完成', 2, ' ', ' ', '2019-06-27 08:18:44', '2019-06-27 08:18:44', '订单支付完成', '0', 1);
INSERT INTO `sys_dict_item` VALUES (47, 16, '0', '待支付', 'order_status', '订单待支付', 0, ' ', ' ', '2019-06-27 08:19:02', '2019-06-27 08:19:02', '订单待支付', '0', 1);
INSERT INTO `sys_dict_item` VALUES (48, 16, '-1', '支付失败', 'order_status', '订单支付失败', 3, ' ', ' ', '2019-06-27 08:19:37', '2019-06-27 08:19:37', '订单支付失败', '0', 1);
INSERT INTO `sys_dict_item` VALUES (49, 2, 'GITEE', '码云', 'social_type', '码云', 2, ' ', ' ', '2019-06-28 09:59:12', '2019-06-28 09:59:12', '码云', '0', 1);
INSERT INTO `sys_dict_item` VALUES (50, 2, 'OSC', '开源中国', 'social_type', '开源中国登录', 2, ' ', ' ', '2019-06-28 10:04:32', '2019-06-28 10:04:32', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (51, 17, 'password', '密码模式', 'grant_types', '支持oauth密码模式', 0, ' ', ' ', '2019-08-13 07:35:28', '2019-08-13 07:35:28', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (52, 17, 'authorization_code', '授权码模式', 'grant_types', 'oauth2 授权码模式', 1, ' ', ' ', '2019-08-13 07:36:07', '2019-08-13 07:36:07', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (53, 17, 'client_credentials', '客户端模式', 'grant_types', 'oauth2 客户端模式', 2, ' ', ' ', '2019-08-13 07:36:30', '2019-08-13 07:36:30', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (54, 17, 'refresh_token', '刷新模式', 'grant_types', 'oauth2 刷新token', 3, ' ', ' ', '2019-08-13 07:36:54', '2019-08-13 07:36:54', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (55, 17, 'implicit', '简化模式', 'grant_types', 'oauth2 简化模式', 4, ' ', ' ', '2019-08-13 07:39:32', '2019-08-13 07:39:32', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (56, 18, '0', 'Avue', 'style_type', 'Avue风格', 0, ' ', ' ', '2020-02-07 03:52:52', '2020-02-07 03:52:52', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (57, 18, '1', 'element', 'style_type', 'element-ui', 1, ' ', ' ', '2020-02-07 03:53:12', '2020-02-07 03:53:12', '', '0', 1);
INSERT INTO `sys_dict_item` VALUES (58, 19, '0', '关', 'captcha_flag_types', '不校验验证码', 0, ' ', ' ', '2020-11-18 06:53:58', '2020-11-18 06:53:58', '不校验验证码 -0', '0', 1);
INSERT INTO `sys_dict_item` VALUES (59, 19, '1', '开', 'captcha_flag_types', '校验验证码', 1, ' ', ' ', '2020-11-18 06:54:15', '2020-11-18 06:54:15', '不校验验证码-1', '0', 1);
INSERT INTO `sys_dict_item` VALUES (60, 20, '0', '否', 'enc_flag_types', '不加密', 0, ' ', ' ', '2020-11-18 06:55:31', '2020-11-18 06:55:31', '不加密-0', '0', 1);
INSERT INTO `sys_dict_item` VALUES (61, 20, '1', '是', 'enc_flag_types', '加密', 1, ' ', ' ', '2020-11-18 06:55:51', '2020-11-18 06:55:51', '加密-1', '0', 1);
INSERT INTO `sys_dict_item` VALUES (62, 15, 'MERGE_PAY', '聚合支付', 'channel_id', '聚合支付', 1, ' ', ' ', '2019-05-30 19:08:08', '2019-06-18 13:51:53', '聚合支付', '0', 1);
INSERT INTO `sys_dict_item` VALUES (63, 2, 'CAS', 'CAS登录', 'social_type', 'CAS 单点登录系统', 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (64, 2, 'DINGTALK', '钉钉', 'social_type', '钉钉', 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (65, 2, 'WEIXIN_CP', '企业微信', 'social_type', '企业微信', 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (66, 18, '2', 'APP', 'style_type', 'uview风格', 1, ' ', ' ', '2020-02-07 03:53:12', '2020-02-07 03:53:12', '', '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
                            `id` bigint NOT NULL COMMENT '编号',
                            `file_name` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `bucket_name` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `original` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `type` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `file_size` bigint DEFAULT NULL COMMENT '文件大小',
                            `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                            `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                            `create_time` datetime DEFAULT NULL COMMENT '上传时间',
                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                            `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                            `tenant_id` bigint DEFAULT NULL COMMENT '所属租户',
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
                           `log_type` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                           `title` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                           `service_id` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
                           `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                           `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           `remote_addr` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                           `user_agent` varchar(1000) CHARACTER SET utf8mb4 DEFAULT NULL,
                           `request_uri` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                           `method` varchar(10) CHARACTER SET utf8mb4 DEFAULT NULL,
                           `params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                           `time` bigint DEFAULT NULL COMMENT '执行时间',
                           `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                           `exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                           `tenant_id` bigint DEFAULT '0' COMMENT '所属租户',
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
                            `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `parent_id` bigint DEFAULT NULL COMMENT '父菜单ID',
                            `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `visible` char(1) COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '是否显示',
                            `sort_order` int DEFAULT '1' COMMENT '排序值',
                            `keep_alive` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0',
                            `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0',
                            `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
                            `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0',
                            `tenant_id` bigint unsigned DEFAULT NULL COMMENT '租户ID',
                            PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1000, '权限管理', NULL, '/admin', -1, 'icon-quanxianguanli', '1', 0, '0', '0', ' ', '2018-09-28 08:29:53', ' ', '2022-01-25 17:24:21', '0', 1);
INSERT INTO `sys_menu` VALUES (1100, '用户管理', NULL, '/admin/user/index', 1000, 'icon-yonghuguanli', '1', 1, '0', '0', ' ', '2017-11-02 22:24:37', ' ', '2022-01-27 11:58:48', '0', 1);
INSERT INTO `sys_menu` VALUES (1101, '用户新增', 'sys_user_add', NULL, 1100, NULL, '1', 1, '0', '1', ' ', '2017-11-08 09:52:09', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1102, '用户修改', 'sys_user_edit', NULL, 1100, NULL, '1', 1, '0', '1', ' ', '2017-11-08 09:52:48', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1103, '用户删除', 'sys_user_del', NULL, 1100, NULL, '1', 1, '0', '1', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1104, '导入导出', 'sys_user_export', NULL, 1100, NULL, '1', 1, '0', '1', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1200, '菜单管理', NULL, '/admin/menu/index', 1000, 'icon-caidanguanli', '1', 2, '0', '0', ' ', '2017-11-08 09:57:27', ' ', '2022-01-27 11:58:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1201, '菜单新增', 'sys_menu_add', NULL, 1200, NULL, '1', 1, '0', '1', ' ', '2017-11-08 10:15:53', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1202, '菜单修改', 'sys_menu_edit', NULL, 1200, NULL, '1', 1, '0', '1', ' ', '2017-11-08 10:16:23', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1203, '菜单删除', 'sys_menu_del', NULL, 1200, NULL, '1', 1, '0', '1', ' ', '2017-11-08 10:16:43', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1300, '角色管理', NULL, '/admin/role/index', 1000, 'icon-jiaoseguanli', '1', 3, '0', '0', ' ', '2017-11-08 10:13:37', ' ', '2022-01-27 11:59:01', '0', 1);
INSERT INTO `sys_menu` VALUES (1301, '角色新增', 'sys_role_add', NULL, 1300, NULL, '1', 1, '0', '1', ' ', '2017-11-08 10:14:18', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1302, '角色修改', 'sys_role_edit', NULL, 1300, NULL, '1', 1, '0', '1', ' ', '2017-11-08 10:14:41', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1303, '角色删除', 'sys_role_del', NULL, 1300, NULL, '1', 1, '0', '1', ' ', '2017-11-08 10:14:59', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1304, '分配权限', 'sys_role_perm', NULL, 1300, NULL, '1', 1, '0', '1', ' ', '2018-04-20 07:22:55', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1305, '角色导入导出', 'sys_role_export', NULL, 1300, NULL, '1', 4, '0', '1', ' ', '2022-03-26 15:54:34', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1400, '部门管理', NULL, '/admin/dept/index', 1000, 'icon-web-icon-', '1', 4, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2022-01-27 11:59:06', '0', 1);
INSERT INTO `sys_menu` VALUES (1401, '部门新增', 'sys_dept_add', NULL, 1400, NULL, '1', 1, '0', '1', ' ', '2018-01-20 14:56:16', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1402, '部门修改', 'sys_dept_edit', NULL, 1400, NULL, '1', 1, '0', '1', ' ', '2018-01-20 14:56:59', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1403, '部门删除', 'sys_dept_del', NULL, 1400, NULL, '1', 1, '0', '1', ' ', '2018-01-20 14:57:28', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1404, '开放互联', 'sys_connect_sync', NULL, 1400, NULL, '1', 1, '0', '1', ' ', '2018-01-20 14:57:28', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (1500, '租户管理', '', '/admin/tenant/index', 1000, 'icon-erji-zuhushouye', '1', 5, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2022-01-27 11:59:14', '0', 1);
INSERT INTO `sys_menu` VALUES (1501, '租户新增', 'admin_systenant_add', NULL, 1500, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:56:52', '0', 1);
INSERT INTO `sys_menu` VALUES (1502, '租户修改', 'admin_systenant_edit', NULL, 1500, '1', '1', 1, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:56:53', '0', 1);
INSERT INTO `sys_menu` VALUES (1503, '租户删除', 'admin_systenant_del', NULL, 1500, '1', '1', 2, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:56:54', '0', 1);
INSERT INTO `sys_menu` VALUES (1600, '岗位管理', NULL, '/admin/post/index', 1000, 'icon-record', '1', 6, '0', '0', ' ', '2022-03-26 13:04:14', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1601, '岗位信息查看', 'sys_post_view', NULL, 1600, NULL, '1', 0, '0', '1', ' ', '2022-03-26 13:05:34', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1602, '岗位信息新增', 'sys_post_add', NULL, 1600, NULL, '1', 1, '0', '1', ' ', '2022-03-26 13:06:00', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1603, '岗位信息修改', 'sys_post_edit', NULL, 1600, NULL, '1', 2, '0', '1', ' ', '2022-03-26 13:06:31', ' ', '2022-03-26 13:06:38', '0', 1);
INSERT INTO `sys_menu` VALUES (1604, '岗位信息删除', 'sys_post_del', NULL, 1600, NULL, '1', 3, '0', '1', ' ', '2022-03-26 13:06:31', ' ', NULL, '0', 1);
INSERT INTO `sys_menu` VALUES (1605, '岗位导入导出', 'sys_post_export', NULL, 1600, NULL, '1', 4, '0', '1', ' ', '2022-03-26 13:06:31', ' ', '2022-03-26 06:32:02', '0', 1);
INSERT INTO `sys_menu` VALUES (2000, '系统管理', NULL, '/system', -1, 'icon-xitongpeizhi', '1', 1, '0', '0', ' ', '2017-11-07 20:56:00', ' ', '2022-02-05 16:15:14', '0', 1);
INSERT INTO `sys_menu` VALUES (2100, '日志管理', NULL, '/admin/log/index', 2000, 'icon-rizhi', '1', 5, '0', '0', ' ', '2017-11-20 14:06:22', ' ', '2020-03-24 08:56:56', '0', 1);
INSERT INTO `sys_menu` VALUES (2101, '日志删除', 'sys_log_del', NULL, 2100, NULL, '1', 1, '0', '1', ' ', '2017-11-20 20:37:37', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2102, '导入导出', 'sys_log_export', NULL, 2100, NULL, '1', 1, '0', '1', ' ', '2017-11-08 09:54:01', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2200, '字典管理', NULL, '/admin/dict/index', 2000, 'icon-navicon-zdgl', '1', 6, '0', '0', ' ', '2017-11-29 11:30:52', ' ', '2020-03-24 08:56:58', '0', 1);
INSERT INTO `sys_menu` VALUES (2201, '字典删除', 'sys_dict_del', NULL, 2200, NULL, '1', 1, '0', '1', ' ', '2017-11-29 11:30:11', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2202, '字典新增', 'sys_dict_add', NULL, 2200, NULL, '1', 1, '0', '1', ' ', '2018-05-11 22:34:55', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2203, '字典修改', 'sys_dict_edit', NULL, 2200, NULL, '1', 1, '0', '1', ' ', '2018-05-11 22:36:03', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2210, '参数管理', NULL, '/admin/param/index', 2000, 'icon-canshu', '1', 7, '1', '0', ' ', '2019-04-29 22:16:50', ' ', '2020-03-24 08:57:10', '0', 1);
INSERT INTO `sys_menu` VALUES (2211, '参数新增', 'admin_syspublicparam_add', NULL, 2210, NULL, '1', 1, '0', '1', ' ', '2019-04-29 22:17:36', ' ', '2020-03-24 08:57:11', '0', 1);
INSERT INTO `sys_menu` VALUES (2212, '参数删除', 'admin_syspublicparam_del', NULL, 2210, NULL, '1', 1, '0', '1', ' ', '2019-04-29 22:17:55', ' ', '2020-03-24 08:57:12', '0', 1);
INSERT INTO `sys_menu` VALUES (2213, '参数编辑', 'admin_syspublicparam_edit', NULL, 2210, NULL, '1', 1, '0', '1', ' ', '2019-04-29 22:18:14', ' ', '2020-03-24 08:57:13', '0', 1);
INSERT INTO `sys_menu` VALUES (2300, '代码生成', '', '/gen/index', 9000, 'icon-weibiaoti46', '1', 1, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-24 08:57:14', '0', 1);
INSERT INTO `sys_menu` VALUES (2400, '终端管理', '', '/admin/client/index', 2000, 'icon-bangzhushouji', '1', 9, '1', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-24 08:57:15', '0', 1);
INSERT INTO `sys_menu` VALUES (2401, '客户端新增', 'sys_client_add', NULL, 2400, '1', '1', 1, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2402, '客户端修改', 'sys_client_edit', NULL, 2400, NULL, '1', 1, '0', '1', ' ', '2018-05-15 21:37:06', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2403, '客户端删除', 'sys_client_del', NULL, 2400, NULL, '1', 1, '0', '1', ' ', '2018-05-15 21:39:16', ' ', '2021-05-25 03:12:55', '0', 1);
INSERT INTO `sys_menu` VALUES (2500, '密钥管理', '', '/admin/social/index', 2000, 'icon-miyue', '1', 10, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-24 08:57:18', '0', 1);
INSERT INTO `sys_menu` VALUES (2501, '密钥新增', 'sys_social_details_add', NULL, 2500, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:19', '0', 1);
INSERT INTO `sys_menu` VALUES (2502, '密钥修改', 'sys_social_details_edit', NULL, 2500, '1', '1', 1, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:19', '0', 1);
INSERT INTO `sys_menu` VALUES (2503, '密钥删除', 'sys_social_details_del', NULL, 2500, '1', '1', 2, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:23', '0', 1);
INSERT INTO `sys_menu` VALUES (2600, '令牌管理', NULL, '/admin/token/index', 2000, 'icon-denglvlingpai', '1', 11, '0', '0', ' ', '2018-09-04 05:58:41', ' ', '2020-03-24 08:57:24', '0', 1);
INSERT INTO `sys_menu` VALUES (2601, '令牌删除', 'sys_token_del', NULL, 2600, NULL, '1', 1, '0', '1', ' ', '2018-09-04 05:59:50', ' ', '2020-03-24 08:57:24', '0', 1);
INSERT INTO `sys_menu` VALUES (2700, '动态路由', NULL, '/admin/route/index', 2000, 'icon-luyou', '1', 12, '0', '0', ' ', '2018-09-04 05:58:41', ' ', '2020-03-24 08:57:25', '0', 1);
INSERT INTO `sys_menu` VALUES (2800, 'Quartz管理', '', '/daemon/job-manage/index', 2000, 'icon-guanwangfangwen', '1', 8, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-24 08:57:26', '0', 1);
INSERT INTO `sys_menu` VALUES (2810, '任务新增', 'job_sys_job_add', NULL, 2800, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:26', '0', 1);
INSERT INTO `sys_menu` VALUES (2820, '任务修改', 'job_sys_job_edit', NULL, 2800, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:27', '0', 1);
INSERT INTO `sys_menu` VALUES (2830, '任务删除', 'job_sys_job_del', NULL, 2800, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:28', '0', 1);
INSERT INTO `sys_menu` VALUES (2840, '任务暂停', 'job_sys_job_shutdown_job', NULL, 2800, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:28', '0', 1);
INSERT INTO `sys_menu` VALUES (2850, '任务开始', 'job_sys_job_start_job', NULL, 2800, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:29', '0', 1);
INSERT INTO `sys_menu` VALUES (2860, '任务刷新', 'job_sys_job_refresh_job', NULL, 2800, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:30', '0', 1);
INSERT INTO `sys_menu` VALUES (2870, '执行任务', 'job_sys_job_run_job', NULL, 2800, '1', '1', 0, '0', '1', ' ', '2019-08-08 15:35:18', ' ', '2020-03-24 08:57:31', '0', 1);
INSERT INTO `sys_menu` VALUES (3000, '系统监控', NULL, '/daemon', -1, 'icon-msnui-supervise', '1', 2, '0', '2', ' ', '2018-07-27 01:13:21', ' ', '2020-03-24 08:57:31', '0', 1);
INSERT INTO `sys_menu` VALUES (3100, '服务监控', NULL, 'http://127.0.0.1:5001', 3000, 'icon-server', '1', 0, '0', '0', ' ', '2018-06-26 10:50:32', ' ', '2020-03-24 08:57:32', '0', 1);
INSERT INTO `sys_menu` VALUES (3101, '流量监控', NULL, 'http://127.0.0.1:5020', 3000, 'icon-liuliang', '1', 0, '0', '0', ' ', '2018-06-26 10:50:32', ' ', '2020-03-24 08:57:32', '0', 1);
INSERT INTO `sys_menu` VALUES (3110, '缓存监控', NULL, '/monitor/redis/index', 3000, 'icon-qingchuhuancun', '1', 1, '0', '0', ' ', '2019-05-08 23:51:27', ' ', '2020-03-24 08:57:33', '0', 1);
INSERT INTO `sys_menu` VALUES (3200, '接口文档', NULL, 'http://127.0.0.1:9999/swagger-ui/index.html', 3000, 'icon-wendang', '1', 1, '0', '0', ' ', '2018-06-26 10:50:32', ' ', '2020-03-24 08:57:34', '0', 1);
INSERT INTO `sys_menu` VALUES (3500, '文档扩展', NULL, 'http://127.0.0.1:9999/doc.html', 3000, 'icon-wendang', '1', 2, '0', '0', ' ', '2018-06-26 10:50:32', ' ', '2020-03-24 08:57:36', '0', 1);
INSERT INTO `sys_menu` VALUES (3600, 'Quartz日志', '', '/daemon/job-log/index', 3000, 'icon-gtsquanjushiwufuwuGTS', '1', 8, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-24 08:57:37', '0', 1);
INSERT INTO `sys_menu` VALUES (3700, '注册配置', NULL, '', 3000, 'icon-line', '1', 10, '0', '0', ' ', '2018-01-25 11:08:52', ' ', '2020-03-24 08:57:37', '1', 1);
INSERT INTO `sys_menu` VALUES (4000, '协同管理', NULL, '/activti', -1, 'icon-kuaisugongzuoliu_o', '1', 3, '0', '0', ' ', '2018-09-26 01:38:13', ' ', '2020-03-24 08:57:39', '0', 1);
INSERT INTO `sys_menu` VALUES (4100, '模型管理', NULL, '/activiti/index', 4000, 'icon-weibiaoti13', '1', 1, '0', '0', ' ', '2018-09-26 01:39:07', ' ', '2020-03-24 08:57:40', '0', 1);
INSERT INTO `sys_menu` VALUES (4101, '模型管理', 'act_model_manage', NULL, 4100, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:41', '0', 1);
INSERT INTO `sys_menu` VALUES (4200, '流程管理', '/activiti/process', '/activiti/process', 4000, 'icon-liucheng', '1', 2, '0', '0', ' ', '2018-09-26 06:41:05', ' ', '2020-03-24 08:57:42', '0', 1);
INSERT INTO `sys_menu` VALUES (4201, '流程管理', 'act_process_manage', NULL, 4200, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:42', '0', 1);
INSERT INTO `sys_menu` VALUES (4300, '请假管理', '/activiti/leave', '/activiti/leave', 4000, 'icon-qingjia', '1', 3, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-24 08:57:43', '0', 1);
INSERT INTO `sys_menu` VALUES (4301, '请假新增', 'act_leavebill_add', NULL, 4300, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:44', '0', 1);
INSERT INTO `sys_menu` VALUES (4302, '请假修改', 'act_leavebill_edit', NULL, 4300, '1', '1', 1, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:45', '0', 1);
INSERT INTO `sys_menu` VALUES (4303, '请假删除', 'act_leavebill_del', NULL, 4300, '1', '1', 2, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:46', '0', 1);
INSERT INTO `sys_menu` VALUES (4400, '待办任务', '/activiti/task', '/activiti/task', 4000, 'icon-renwu', '1', 4, '0', '0', ' ', '2018-09-27 09:52:31', ' ', '2020-03-24 08:57:48', '0', 1);
INSERT INTO `sys_menu` VALUES (4401, '流程管理', 'act_task_manage', NULL, 4400, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:57:50', '0', 1);
INSERT INTO `sys_menu` VALUES (5000, '支付管理', NULL, '/pay', -1, 'icon-pay6zhifu', '1', 4, '0', '0', ' ', '2019-05-30 15:28:03', ' ', '2020-03-24 08:57:51', '0', 1);
INSERT INTO `sys_menu` VALUES (5100, '渠道管理', NULL, '/pay/paychannel/index', 5000, 'icon-zhifuqudaoguanli', '1', 1, '0', '0', ' ', '2019-05-30 15:32:17', ' ', '2020-03-24 08:57:52', '0', 1);
INSERT INTO `sys_menu` VALUES (5110, '增加渠道', 'pay_paychannel_add', NULL, 5100, NULL, '1', 1, '0', '1', ' ', '2019-05-30 15:46:14', ' ', '2020-03-24 08:58:07', '0', 1);
INSERT INTO `sys_menu` VALUES (5120, '编辑渠道', 'pay_paychannel_edit', NULL, 5100, NULL, '1', 1, '0', '1', ' ', '2019-05-30 15:46:35', ' ', '2020-03-24 08:58:08', '0', 1);
INSERT INTO `sys_menu` VALUES (5130, '删除渠道', 'pay_paychannel_del', NULL, 5100, NULL, '1', 1, '0', '1', ' ', '2019-05-30 15:47:08', ' ', '2020-03-24 08:58:09', '0', 1);
INSERT INTO `sys_menu` VALUES (5200, '收银台', NULL, '/pay/cd/index', 5000, 'icon-shouyintai', '1', 0, '0', '0', ' ', '2019-05-30 19:44:00', ' ', '2020-03-24 08:58:09', '0', 1);
INSERT INTO `sys_menu` VALUES (5300, '商品订单', '', '/pay/goods/index', 5000, 'icon-dingdan', '1', 2, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-24 08:58:10', '0', 1);
INSERT INTO `sys_menu` VALUES (5310, '商品订单新增', 'generator_paygoodsorder_add', NULL, 5300, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:11', '0', 1);
INSERT INTO `sys_menu` VALUES (5320, '商品订单修改', 'generator_paygoodsorder_edit', NULL, 5300, '1', '1', 1, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:13', '0', 1);
INSERT INTO `sys_menu` VALUES (5330, '商品订单删除', 'generator_paygoodsorder_del', NULL, 5300, '1', '1', 2, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:14', '0', 1);
INSERT INTO `sys_menu` VALUES (5400, '支付订单', '', '/pay/orders/index', 5000, 'icon-zhifu', '1', 3, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-24 08:58:14', '0', 1);
INSERT INTO `sys_menu` VALUES (5410, '支付订单新增', 'generator_paytradeorder_add', NULL, 5400, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:15', '0', 1);
INSERT INTO `sys_menu` VALUES (5420, '支付订单修改', 'generator_paytradeorder_edit', NULL, 5400, '1', '1', 1, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:16', '0', 1);
INSERT INTO `sys_menu` VALUES (5430, '支付订单删除', 'generator_paytradeorder_del', NULL, 5400, '1', '1', 2, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:18', '0', 1);
INSERT INTO `sys_menu` VALUES (5500, '回调记录', '', '/pay/notify/index', 5000, 'icon-huitiao', '1', 4, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-24 08:58:19', '0', 1);
INSERT INTO `sys_menu` VALUES (5510, '记录新增', 'generator_paynotifyrecord_add', NULL, 5500, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:19', '0', 1);
INSERT INTO `sys_menu` VALUES (5520, '记录修改', 'generator_paynotifyrecord_edit', NULL, 5500, '1', '1', 1, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:20', '0', 1);
INSERT INTO `sys_menu` VALUES (5530, '记录删除', 'generator_paynotifyrecord_del', NULL, 5500, '1', '1', 2, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:21', '0', 1);
INSERT INTO `sys_menu` VALUES (6000, '微信管理', NULL, '/mp', -1, 'icon-gongzhonghao', '1', 4, '0', '0', ' ', '2018-09-26 01:38:13', ' ', '2020-03-24 08:58:21', '0', 1);
INSERT INTO `sys_menu` VALUES (6100, '账号管理', '', '/mp/wx-account/index', 6000, 'icon-weixincaidan', '1', 8, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2021-12-31 04:46:40', '0', 1);
INSERT INTO `sys_menu` VALUES (6101, '公众号新增', 'mp_wxaccount_add', '', 6100, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:23', '0', 1);
INSERT INTO `sys_menu` VALUES (6102, '公众号修改', 'mp_wxaccount_edit', NULL, 6100, '1', '1', 1, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:23', '0', 1);
INSERT INTO `sys_menu` VALUES (6103, '公众号删除', 'mp_wxaccount_del', NULL, 6100, '1', '1', 2, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:25', '0', 1);
INSERT INTO `sys_menu` VALUES (6200, '粉丝管理', '', '/mp/wx-account-fans/index', 6000, 'icon-fensiguanli', '1', 8, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2021-12-31 04:46:40', '0', 1);
INSERT INTO `sys_menu` VALUES (6201, '粉丝新增', 'mp_wxaccountfans_add', NULL, 6200, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:27', '0', 1);
INSERT INTO `sys_menu` VALUES (6202, '粉丝修改', 'mp_wxaccountfans_edit', NULL, 6200, '1', '1', 1, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:30', '0', 1);
INSERT INTO `sys_menu` VALUES (6203, '粉丝删除', 'mp_wxaccountfans_del', NULL, 6200, '1', '1', 2, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:31', '0', 1);
INSERT INTO `sys_menu` VALUES (6204, '粉丝同步', 'mp_wxaccountfans_sync', NULL, 6200, '1', '1', 3, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:31', '0', 1);
INSERT INTO `sys_menu` VALUES (6300, '消息管理', '', '/mp/wx-fans-msg/index', 6000, 'icon-xiaoxiguanli', '1', 8, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2021-12-31 04:46:40', '0', 1);
INSERT INTO `sys_menu` VALUES (6301, '消息新增', 'mp_wxmsg_add', NULL, 6300, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:32', '0', 1);
INSERT INTO `sys_menu` VALUES (6302, '消息修改', 'mp_wxmsg_edit', NULL, 6300, '1', '1', 1, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:33', '0', 1);
INSERT INTO `sys_menu` VALUES (6303, '消息删除', 'mp_wxmsg_del', NULL, 6300, '1', '1', 2, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:33', '0', 1);
INSERT INTO `sys_menu` VALUES (6400, '菜单设置', NULL, '/mp/wx-menu/index', 6000, 'icon-anniu_weixincaidanlianjie', '1', 6, '0', '0', ' ', '2019-03-29 15:20:12', ' ', '2021-12-31 04:46:40', '0', 1);
INSERT INTO `sys_menu` VALUES (6401, '保存', 'mp_wxmenu_add', NULL, 6400, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:22', ' ', '2020-03-24 08:58:38', '0', 1);
INSERT INTO `sys_menu` VALUES (6402, '发布', 'mp_wxmenu_push', NULL, 6400, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:54', ' ', '2020-03-24 08:58:39', '0', 1);
INSERT INTO `sys_menu` VALUES (6403, '删除', 'mp_wxmenu_del', NULL, 6400, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:54', ' ', '2020-03-24 08:58:39', '0', 1);
INSERT INTO `sys_menu` VALUES (6500, '运营数据', NULL, '/mp/wx-statistics/index', 6000, 'icon-zhexiantu', '1', 7, '0', '0', ' ', '2019-04-14 00:15:35', ' ', '2021-12-31 04:46:40', '0', 1);
INSERT INTO `sys_menu` VALUES (6600, '素材管理', NULL, '/mp/wx-material/index', 6000, 'icon-sucaisads', '1', 999, '0', '0', ' ', '2020-04-27 15:25:17', ' ', '2021-12-31 04:46:40', '0', 1);
INSERT INTO `sys_menu` VALUES (6601, '素材维护', 'mp_wxmaterial_add', NULL, 6600, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:54', ' ', '2020-03-24 08:58:39', '0', 1);
INSERT INTO `sys_menu` VALUES (6602, '素材删除', 'mp_wxmaterial_del', NULL, 6600, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:54', ' ', '2020-03-24 08:58:39', '0', 1);
INSERT INTO `sys_menu` VALUES (6700, '自动回复', NULL, '/mp/wx-auto-reply/index', 6000, 'icon-huifu', '1', 998, '0', '0', ' ', '2020-04-27 15:25:17', ' ', '2021-12-31 04:46:40', '0', 1);
INSERT INTO `sys_menu` VALUES (6701, '新增回复', 'mp_wxautoreply_add', NULL, 6700, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:54', ' ', '2020-03-24 08:58:39', '0', 1);
INSERT INTO `sys_menu` VALUES (6702, '编辑回复', 'mp_wxautoreply_edit', NULL, 6700, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:54', ' ', '2020-03-24 08:58:39', '0', 1);
INSERT INTO `sys_menu` VALUES (6703, '删除回复', 'mp_wxautoreply_del', NULL, 6700, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:54', ' ', '2020-03-24 08:58:39', '0', 1);
INSERT INTO `sys_menu` VALUES (6800, '标签管理', NULL, '/mp/wx-account-tag/index', 6000, 'icon-huifu', '1', 998, '0', '0', ' ', '2020-04-27 15:25:17', ' ', '2020-05-09 03:16:16', '0', 1);
INSERT INTO `sys_menu` VALUES (6801, '新增标签', 'mp_wx_account_tag_add', NULL, 6800, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:54', ' ', '2020-03-24 08:58:39', '0', 1);
INSERT INTO `sys_menu` VALUES (6802, '编辑标签', 'mp_wx_account_tag_edit', NULL, 6800, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:54', ' ', '2020-03-24 08:58:39', '0', 1);
INSERT INTO `sys_menu` VALUES (6803, '删除标签', 'mp_wx_account_tag_del', NULL, 6800, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:54', ' ', '2020-03-24 08:58:39', '0', 1);
INSERT INTO `sys_menu` VALUES (6804, '同步标签', 'mp_wx_account_tag_sync', NULL, 6800, NULL, '1', 1, '0', '1', ' ', '2019-03-29 15:43:54', ' ', '2022-02-17 15:44:24', '0', 1);
INSERT INTO `sys_menu` VALUES (7000, '报表设计', NULL, '/report/bi/index', -1, 'icon-icon-p_mrpbaobiao', '1', 9, '0', '0', '', '2019-08-12 09:35:16', 'admin', '2022-11-18 18:48:17', '0', 1);
INSERT INTO `sys_menu` VALUES (8000, '文件管理', NULL, '/admin/file/index', 2000, 'icon-wenjianguanli', '1', 6, '0', '0', ' ', '2019-06-25 12:44:46', ' ', '2020-03-24 08:58:41', '0', 1);
INSERT INTO `sys_menu` VALUES (8001, '删除文件', 'sys_file_del', NULL, 8000, NULL, '1', 1, '0', '1', ' ', '2019-06-25 13:41:41', ' ', '2020-03-24 08:58:42', '0', 1);
INSERT INTO `sys_menu` VALUES (9000, '开发平台', NULL, '/gen', -1, 'icon-shejiyukaifa-', '1', 9, '0', '0', ' ', '2019-08-12 09:35:16', ' ', '2020-03-24 08:58:48', '0', 1);
INSERT INTO `sys_menu` VALUES (9001, '表单管理', '', '/gen/form', 9000, 'icon-record', '1', 3, '0', '0', ' ', '2018-01-20 13:17:19', ' ', '2020-03-24 08:58:44', '0', 1);
INSERT INTO `sys_menu` VALUES (9002, '表单新增', 'gen_form_add', NULL, 9001, '1', '1', 0, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:45', '0', 1);
INSERT INTO `sys_menu` VALUES (9003, '表单修改', 'gen_form_edit', NULL, 9001, '1', '1', 1, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:46', '0', 1);
INSERT INTO `sys_menu` VALUES (9004, '表单删除', 'gen_form_del', NULL, 9001, '1', '1', 2, '0', '1', ' ', '2018-05-15 21:35:18', ' ', '2020-03-24 08:58:47', '0', 1);
INSERT INTO `sys_menu` VALUES (9005, '数据源管理', NULL, '/gen/datasource', 9000, 'icon-mysql', '1', 0, '0', '0', ' ', '2019-08-12 09:42:11', ' ', '2020-03-24 08:58:49', '0', 1);
INSERT INTO `sys_menu` VALUES (9006, '表单设计', NULL, '/gen/design', 9000, 'icon-biaodanbiaoqian', '1', 2, '0', '0', ' ', '2019-08-16 10:08:56', ' ', '2020-03-24 08:58:53', '0', 1);
INSERT INTO `sys_menu` VALUES (9007, '低代码', 'gen_api_designer', NULL, 9001, '1', '1', 3, '0', '1', ' ', '2019-08-16 10:08:56', ' ', '2020-03-24 08:58:53', '0', 1);
INSERT INTO `sys_menu` VALUES (10000, '大屏设计', NULL, '/report/screen/index', -1, 'icon-shuju', '1', 10, '0', '0', '', '2019-08-16 10:08:56', 'admin', '2022-11-04 15:21:29', '0', 1);
COMMIT;


-- ----------------------------
-- Table structure for sys_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_details`;
CREATE TABLE `sys_oauth_client_details` (
                                            `id` bigint NOT NULL COMMENT 'ID',
                                            `client_id` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
                                            `resource_ids` varchar(256) CHARACTER SET utf8mb4 DEFAULT NULL,
                                            `client_secret` varchar(256) CHARACTER SET utf8mb4 DEFAULT NULL,
                                            `scope` varchar(256) CHARACTER SET utf8mb4 DEFAULT NULL,
                                            `authorized_grant_types` varchar(256) CHARACTER SET utf8mb4 DEFAULT NULL,
                                            `web_server_redirect_uri` varchar(256) CHARACTER SET utf8mb4 DEFAULT NULL,
                                            `authorities` varchar(256) CHARACTER SET utf8mb4 DEFAULT NULL,
                                            `access_token_validity` int DEFAULT NULL,
                                            `refresh_token_validity` int DEFAULT NULL,
                                            `additional_information` varchar(4096) CHARACTER SET utf8mb4 DEFAULT NULL,
                                            `autoapprove` varchar(256) CHARACTER SET utf8mb4 DEFAULT NULL,
                                            `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                            `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                                            `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                                            `create_time` datetime DEFAULT NULL COMMENT '上传时间',
                                            `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                            `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '所属租户',
                                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='终端信息表';

-- ----------------------------
-- Records of sys_oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `sys_oauth_client_details` VALUES (1, 'app', NULL, 'app', 'server', 'password,refresh_token,authorization_code,client_credentials,mobile', NULL, NULL, 43200, 2592001, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\"}', 'true', '0', ' ', ' ', NULL, NULL, 1);
INSERT INTO `sys_oauth_client_details` VALUES (2, 'daemon', NULL, 'daemon', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\"}', 'true', '0', ' ', ' ', NULL, NULL, 1);
INSERT INTO `sys_oauth_client_details` VALUES (3, 'gen', NULL, 'gen', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\"}', 'true', '0', ' ', ' ', NULL, NULL, 1);
INSERT INTO `sys_oauth_client_details` VALUES (4, 'mp', NULL, 'mp', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\"}', 'true', '0', ' ', ' ', NULL, NULL, 1);
INSERT INTO `sys_oauth_client_details` VALUES (5, 'pig', NULL, 'pig', 'server', 'password,refresh_token,authorization_code,client_credentials,mobile', 'http://localhost:4040/sso1/login,http://localhost:4041/sso1/login,http://localhost:8080/renren-admin/sys/oauth2-sso,http://localhost:8090/sys/oauth2-sso', NULL, 0, 0, '{\"enc_flag\":\"1\",\"captcha_flag\":\"1\"}', 'false', '0', ' ', ' ', NULL, NULL, 1);
INSERT INTO `sys_oauth_client_details` VALUES (6, 'test', NULL, 'test', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, '{ \"enc_flag\":\"1\",\"captcha_flag\":\"0\"}', 'true', '0', ' ', ' ', NULL, NULL, 1);
INSERT INTO `sys_oauth_client_details` VALUES (7, 'social', NULL, 'social', 'server', 'password,refresh_token,mobile', NULL, NULL, NULL, NULL, '{ \"enc_flag\":\"1\",\"captcha_flag\":\"0\"}', 'true', '0', ' ', ' ', NULL, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_public_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_public_param`;
CREATE TABLE `sys_public_param` (
                                    `public_id` bigint NOT NULL COMMENT '编号',
                                    `public_name` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `public_key` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `public_value` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `status` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                    `validate_code` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                                    `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                    `public_type` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                    `system_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                    `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                    `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
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
                            `role_id` bigint NOT NULL,
                            `role_name` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `role_code` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `role_desc` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `ds_type` char(1) CHARACTER SET utf8mb4 DEFAULT '2',
                            `ds_scope` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                            `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                            `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                            `tenant_id` bigint DEFAULT NULL,
                            PRIMARY KEY (`role_id`) USING BTREE,
                            KEY `role_idx1_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', '管理员', '3', '2', ' ', ' ', '2017-10-29 15:45:51', '2022-01-25 18:21:23', '0', 1);
INSERT INTO `sys_role` VALUES (2, '普通用户', 'GENERAL_USER', '普通用户', '3', NULL, ' ', ' ', '2022-03-31 17:03:15', NULL, '0', 1);
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
INSERT INTO `sys_role_menu` VALUES (1, 1404);
INSERT INTO `sys_role_menu` VALUES (1, 1500);
INSERT INTO `sys_role_menu` VALUES (1, 1501);
INSERT INTO `sys_role_menu` VALUES (1, 1502);
INSERT INTO `sys_role_menu` VALUES (1, 1503);
INSERT INTO `sys_role_menu` VALUES (1, 1600);
INSERT INTO `sys_role_menu` VALUES (1, 1601);
INSERT INTO `sys_role_menu` VALUES (1, 1602);
INSERT INTO `sys_role_menu` VALUES (1, 1603);
INSERT INTO `sys_role_menu` VALUES (1, 1604);
INSERT INTO `sys_role_menu` VALUES (1, 1605);
INSERT INTO `sys_role_menu` VALUES (1, 2000);
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
INSERT INTO `sys_role_menu` VALUES (1, 2500);
INSERT INTO `sys_role_menu` VALUES (1, 2501);
INSERT INTO `sys_role_menu` VALUES (1, 2502);
INSERT INTO `sys_role_menu` VALUES (1, 2503);
INSERT INTO `sys_role_menu` VALUES (1, 2600);
INSERT INTO `sys_role_menu` VALUES (1, 2601);
INSERT INTO `sys_role_menu` VALUES (1, 2700);
INSERT INTO `sys_role_menu` VALUES (1, 2800);
INSERT INTO `sys_role_menu` VALUES (1, 2810);
INSERT INTO `sys_role_menu` VALUES (1, 2820);
INSERT INTO `sys_role_menu` VALUES (1, 2830);
INSERT INTO `sys_role_menu` VALUES (1, 2840);
INSERT INTO `sys_role_menu` VALUES (1, 2850);
INSERT INTO `sys_role_menu` VALUES (1, 2860);
INSERT INTO `sys_role_menu` VALUES (1, 2870);
INSERT INTO `sys_role_menu` VALUES (1, 3000);
INSERT INTO `sys_role_menu` VALUES (1, 3100);
INSERT INTO `sys_role_menu` VALUES (1, 3101);
INSERT INTO `sys_role_menu` VALUES (1, 3110);
INSERT INTO `sys_role_menu` VALUES (1, 3200);
INSERT INTO `sys_role_menu` VALUES (1, 3500);
INSERT INTO `sys_role_menu` VALUES (1, 3600);
INSERT INTO `sys_role_menu` VALUES (1, 4000);
INSERT INTO `sys_role_menu` VALUES (1, 4100);
INSERT INTO `sys_role_menu` VALUES (1, 4101);
INSERT INTO `sys_role_menu` VALUES (1, 4200);
INSERT INTO `sys_role_menu` VALUES (1, 4201);
INSERT INTO `sys_role_menu` VALUES (1, 4300);
INSERT INTO `sys_role_menu` VALUES (1, 4301);
INSERT INTO `sys_role_menu` VALUES (1, 4302);
INSERT INTO `sys_role_menu` VALUES (1, 4303);
INSERT INTO `sys_role_menu` VALUES (1, 4400);
INSERT INTO `sys_role_menu` VALUES (1, 4401);
INSERT INTO `sys_role_menu` VALUES (1, 5000);
INSERT INTO `sys_role_menu` VALUES (1, 5100);
INSERT INTO `sys_role_menu` VALUES (1, 5110);
INSERT INTO `sys_role_menu` VALUES (1, 5120);
INSERT INTO `sys_role_menu` VALUES (1, 5130);
INSERT INTO `sys_role_menu` VALUES (1, 5200);
INSERT INTO `sys_role_menu` VALUES (1, 5300);
INSERT INTO `sys_role_menu` VALUES (1, 5310);
INSERT INTO `sys_role_menu` VALUES (1, 5320);
INSERT INTO `sys_role_menu` VALUES (1, 5330);
INSERT INTO `sys_role_menu` VALUES (1, 5400);
INSERT INTO `sys_role_menu` VALUES (1, 5410);
INSERT INTO `sys_role_menu` VALUES (1, 5420);
INSERT INTO `sys_role_menu` VALUES (1, 5430);
INSERT INTO `sys_role_menu` VALUES (1, 5500);
INSERT INTO `sys_role_menu` VALUES (1, 5510);
INSERT INTO `sys_role_menu` VALUES (1, 5520);
INSERT INTO `sys_role_menu` VALUES (1, 5530);
INSERT INTO `sys_role_menu` VALUES (1, 6000);
INSERT INTO `sys_role_menu` VALUES (1, 6100);
INSERT INTO `sys_role_menu` VALUES (1, 6101);
INSERT INTO `sys_role_menu` VALUES (1, 6102);
INSERT INTO `sys_role_menu` VALUES (1, 6103);
INSERT INTO `sys_role_menu` VALUES (1, 6200);
INSERT INTO `sys_role_menu` VALUES (1, 6201);
INSERT INTO `sys_role_menu` VALUES (1, 6202);
INSERT INTO `sys_role_menu` VALUES (1, 6203);
INSERT INTO `sys_role_menu` VALUES (1, 6204);
INSERT INTO `sys_role_menu` VALUES (1, 6300);
INSERT INTO `sys_role_menu` VALUES (1, 6301);
INSERT INTO `sys_role_menu` VALUES (1, 6302);
INSERT INTO `sys_role_menu` VALUES (1, 6303);
INSERT INTO `sys_role_menu` VALUES (1, 6304);
INSERT INTO `sys_role_menu` VALUES (1, 6305);
INSERT INTO `sys_role_menu` VALUES (1, 6400);
INSERT INTO `sys_role_menu` VALUES (1, 6401);
INSERT INTO `sys_role_menu` VALUES (1, 6402);
INSERT INTO `sys_role_menu` VALUES (1, 6500);
INSERT INTO `sys_role_menu` VALUES (1, 6600);
INSERT INTO `sys_role_menu` VALUES (1, 6601);
INSERT INTO `sys_role_menu` VALUES (1, 6602);
INSERT INTO `sys_role_menu` VALUES (1, 6700);
INSERT INTO `sys_role_menu` VALUES (1, 6701);
INSERT INTO `sys_role_menu` VALUES (1, 6702);
INSERT INTO `sys_role_menu` VALUES (1, 6703);
INSERT INTO `sys_role_menu` VALUES (1, 6800);
INSERT INTO `sys_role_menu` VALUES (1, 6801);
INSERT INTO `sys_role_menu` VALUES (1, 6802);
INSERT INTO `sys_role_menu` VALUES (1, 6803);
INSERT INTO `sys_role_menu` VALUES (1, 6804);
INSERT INTO `sys_role_menu` VALUES (1, 7000);
INSERT INTO `sys_role_menu` VALUES (1, 8000);
INSERT INTO `sys_role_menu` VALUES (1, 8001);
INSERT INTO `sys_role_menu` VALUES (1, 9000);
INSERT INTO `sys_role_menu` VALUES (1, 9001);
INSERT INTO `sys_role_menu` VALUES (1, 9002);
INSERT INTO `sys_role_menu` VALUES (1, 9003);
INSERT INTO `sys_role_menu` VALUES (1, 9004);
INSERT INTO `sys_role_menu` VALUES (1, 9005);
INSERT INTO `sys_role_menu` VALUES (1, 9006);
INSERT INTO `sys_role_menu` VALUES (1, 9007);
INSERT INTO `sys_role_menu` VALUES (1, 10000);
COMMIT;

-- ----------------------------
-- Table structure for sys_route_conf
-- ----------------------------
DROP TABLE IF EXISTS `sys_route_conf`;
CREATE TABLE `sys_route_conf` (
                                  `id` bigint NOT NULL COMMENT '主键',
                                  `route_name` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL,
                                  `route_id` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL,
                                  `predicates` json DEFAULT NULL COMMENT '断言',
                                  `filters` json DEFAULT NULL COMMENT '过滤器',
                                  `uri` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
                                  `sort_order` int DEFAULT '0' COMMENT '排序',
                                  `metadata` json DEFAULT NULL COMMENT '路由元信息',
                                  `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                                  `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                                  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                  `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
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
INSERT INTO `sys_route_conf` VALUES (13, '大屏设计', 'pigx-report-platform', '[{\"args\": {\"_genkey_0\": \"/aj/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-report-platform', 0, '{}', ' ', ' ', '2022-08-27 02:38:43', NULL, '0');
COMMIT;

-- ----------------------------
-- Table structure for sys_social_details
-- ----------------------------
DROP TABLE IF EXISTS `sys_social_details`;
CREATE TABLE `sys_social_details` (
                                      `id` bigint NOT NULL COMMENT '主鍵',
                                      `type` varchar(16) CHARACTER SET utf8mb4 DEFAULT NULL,
                                      `remark` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                      `app_id` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                      `app_secret` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                      `redirect_url` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL,
                                      `ext` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '拓展字段',
                                      `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                                      `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                                      `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                      `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '所属租户',
                                      PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统社交登录账号表';


-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant` (
                              `id` bigint NOT NULL COMMENT '租户id',
                              `name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                              `code` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                              `tenant_domain` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                              `start_time` datetime DEFAULT NULL COMMENT '开始时间',
                              `end_time` datetime DEFAULT NULL COMMENT '结束时间',
                              `status` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                              `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                              `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                              `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建',
                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='租户表';

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant` VALUES (1, '北京分公司', '1', '','2019-05-15 00:00:00', '2029-05-15 00:00:00', '0', '0', ' ', ' ', '2019-05-15 15:44:57', '2022-02-10 05:38:48');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
                            `user_id` bigint NOT NULL COMMENT '主键ID',
                            `username` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `password` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `salt` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `phone` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `avatar` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
                            `nickname` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '拓展字段:昵称',
                            `name` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '拓展字段:姓名',
                            `email` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '拓展字段:邮箱',
                            `dept_id` bigint DEFAULT NULL COMMENT '部门ID',
                            `create_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '创建人',
                            `update_by` varchar(64) CHARACTER SET utf8  NOT NULL DEFAULT ' ' COMMENT '修改人',
                            `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                            `lock_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                            `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                            `wx_openid` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '微信登录openId',
                            `mini_openid` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '小程序openId',
                            `qq_openid` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'QQ openId',
                            `gitee_login` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '码云 标识',
                            `osc_id` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '开源中国 标识',
                            `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '所属租户',
                            PRIMARY KEY (`user_id`) USING BTREE,
                            KEY `user_wx_openid` (`wx_openid`) USING BTREE,
                            KEY `user_qq_openid` (`qq_openid`) USING BTREE,
                            KEY `user_idx1_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$cE02oZ1N4mkdA6JHJUP7/uAJ3TQdVgO3kLRvLoe5KvvMU99W5r5hG', '', '17034642888', '/admin/sys-file/lengleng/c5a85e0770cd4fe78bc14b63b3bd05ae.jpg', '管理员', '管理员', 'admin@mail.com', 1, ' ', ' ', '2018-04-20 07:15:18', '2022-02-18 14:02:09', '0', '0', 'o_0FT0uyg_H1vVy2H0JpSwlVGhWQ', 'oBxPy5E-v82xWGsfzZVzkD3wEX64', NULL, 'log4j', '2303656', 1);
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
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
