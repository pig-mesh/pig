DROP DATABASE IF EXISTS `pig`;

CREATE DATABASE  `pig` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE `pig`;


DROP TABLE IF EXISTS `sys_area`;
CREATE TABLE `sys_area` (
  `id` bigint unsigned NOT NULL COMMENT '主键ID',
  `pid` bigint unsigned NOT NULL DEFAULT '0' COMMENT '父ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '地区名称',
  `letter` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '地区字母',
  `adcode` bigint NOT NULL COMMENT '高德地区code',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '经纬度',
  `area_sort` bigint DEFAULT NULL COMMENT '排序值',
  `area_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '0:未生效，1:生效',
  `area_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '0:国家,1:省,2:城市,3:区县',
  `hot` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '0:非热门，1:热门',
  `city_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '城市编码',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '创建人',
  `create_time` datetime COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='行政区划表';

BEGIN;
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (1, 0, '全国', '', 100000, '', NULL, '1', '0', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', 'admin', '2024-02-17 12:45:08', '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (2, 100000, '北京市', '', 110000, '', NULL, '1', '1', '1', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', 'admin', '2024-02-17 12:45:12', '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (3, 100000, '天津市', '', 120000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (4, 100000, '河北省', '', 130000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (5, 100000, '山西省', '', 140000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (6, 100000, '内蒙古自治区', '', 150000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (7, 100000, '辽宁省', '', 210000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (8, 100000, '吉林省', '', 220000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', 'admin', '2024-02-16 23:16:23', '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (9, 100000, '黑龙江省', '', 230000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (10, 100000, '上海市', '', 310000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (11, 100000, '江苏省', '', 320000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (12, 100000, '浙江省', '', 330000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (13, 100000, '安徽省', '', 340000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (14, 100000, '福建省', '', 350000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (15, 100000, '江西省', '', 360000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (16, 100000, '山东省', '', 370000, '', 100, '1', '1', '1', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', 'admin', '2024-02-17 13:13:25', '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (17, 100000, '河南省', '', 410000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (18, 100000, '湖北省', '', 420000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (19, 100000, '湖南省', '', 430000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (20, 100000, '广东省', '', 440000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (21, 100000, '广西壮族自治区', '', 450000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (22, 100000, '海南省', '', 460000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (23, 100000, '重庆市', '', 500000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (24, 100000, '四川省', '', 510000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (25, 100000, '贵州省', '', 520000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (26, 100000, '云南省', '', 530000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (27, 100000, '西藏自治区', '', 540000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (28, 100000, '陕西省', '', 610000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (29, 100000, '甘肃省', '', 620000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (30, 100000, '青海省', '', 630000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
INSERT INTO `sys_area` (`id`, `pid`, `name`, `letter`, `adcode`, `location`, `area_sort`, `area_status`, `area_type`, `hot`, `city_code`, `create_by`, `create_time`, `update_by`, `update_time`, `del_flag`) VALUES (31, 100000, '宁夏回族自治区', '', 640000, '', NULL, '1', '1', '0', '', '2023-11-09 03:50:52', '2023-11-09 03:50:52', NULL, NULL, '0');
COMMIT;

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
  `create_time` datetime COMMENT '操作时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除标记',
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
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级部门ID',
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
  `id` bigint(20) NOT NULL COMMENT '编号',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '字典类型',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
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
INSERT INTO `sys_dict` VALUES (28, 'message_type', '消息类型', 'admin', ' ', '2023-10-27 10:29:48', NULL, NULL, '1', '0');
INSERT INTO `sys_dict` VALUES (29, 'sensitive_type', '敏感词类型', 'admin', ' ', '2023-10-27 10:29:48', NULL, NULL, '1', '0');
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
  `list_class` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标签类型',
  `sort_order` int(11) NOT NULL DEFAULT '0' COMMENT '排序（升序）',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
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
INSERT INTO `sys_dict_item` VALUES (1, 1, '9', '异常', 'log_type', '日志异常', NULL, 1, ' ', ' ', '2019-03-19 11:08:59', '2019-03-25 12:49:13', '', '0');
INSERT INTO `sys_dict_item` VALUES (2, 1, '0', '正常', 'log_type', '日志正常', NULL, 0, ' ', ' ', '2019-03-19 11:09:17', '2019-03-25 12:49:18', '', '0');
INSERT INTO `sys_dict_item` VALUES (3, 2, 'WX', '微信', 'social_type', '微信登录', NULL, 0, ' ', ' ', '2019-03-19 11:10:02', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (4, 2, 'QQ', 'QQ', 'social_type', 'QQ登录', NULL, 1, ' ', ' ', '2019-03-19 11:10:14', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (5, 3, '1', 'java类', 'job_type', 'java类', NULL, 1, ' ', ' ', '2019-03-19 11:22:37', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (6, 3, '2', 'spring bean', 'job_type', 'spring bean容器实例', NULL, 2, ' ', ' ', '2019-03-19 11:23:05', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (7, 3, '9', '其他', 'job_type', '其他类型', NULL, 9, ' ', ' ', '2019-03-19 11:23:31', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (8, 3, '3', 'Rest 调用', 'job_type', 'Rest 调用', NULL, 3, ' ', ' ', '2019-03-19 11:23:57', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (9, 3, '4', 'jar', 'job_type', 'jar类型', NULL, 4, ' ', ' ', '2019-03-19 11:24:20', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (10, 4, '1', '未发布', 'job_status', '未发布', NULL, 1, ' ', ' ', '2019-03-19 11:25:18', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (11, 4, '2', '运行中', 'job_status', '运行中', NULL, 2, ' ', ' ', '2019-03-19 11:25:31', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (12, 4, '3', '暂停', 'job_status', '暂停', NULL, 3, ' ', ' ', '2019-03-19 11:25:42', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (13, 5, '0', '正常', 'job_execute_status', '正常', NULL, 0, ' ', ' ', '2019-03-19 11:26:27', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (14, 5, '1', '异常', 'job_execute_status', '异常', NULL, 1, ' ', ' ', '2019-03-19 11:26:41', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (15, 6, '1', '错失周期立即执行', 'misfire_policy', '错失周期立即执行', NULL, 1, ' ', ' ', '2019-03-19 11:27:45', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (16, 6, '2', '错失周期执行一次', 'misfire_policy', '错失周期执行一次', NULL, 2, ' ', ' ', '2019-03-19 11:27:57', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (17, 6, '3', '下周期执行', 'misfire_policy', '下周期执行', NULL, 3, ' ', ' ', '2019-03-19 11:28:08', '2019-03-25 12:49:36', '', '0');
INSERT INTO `sys_dict_item` VALUES (18, 7, '1', '男', 'gender', '微信-男', NULL, 0, ' ', ' ', '2019-03-27 13:45:13', '2019-03-27 13:45:13', '微信-男', '0');
INSERT INTO `sys_dict_item` VALUES (19, 7, '2', '女', 'gender', '女-微信', NULL, 1, ' ', ' ', '2019-03-27 13:45:34', '2019-03-27 13:45:34', '女-微信', '0');
INSERT INTO `sys_dict_item` VALUES (20, 7, '0', '未知', 'gender', '性别未知', NULL, 3, ' ', ' ', '2019-03-27 13:45:57', '2019-03-27 13:45:57', '性别未知', '0');
INSERT INTO `sys_dict_item` VALUES (21, 8, '0', '未关注', 'subscribe', '公众号-未关注', NULL, 0, ' ', ' ', '2019-03-27 13:49:07', '2019-03-27 13:49:07', '公众号-未关注', '0');
INSERT INTO `sys_dict_item` VALUES (22, 8, '1', '已关注', 'subscribe', '公众号-已关注', NULL, 1, ' ', ' ', '2019-03-27 13:49:26', '2019-03-27 13:49:26', '公众号-已关注', '0');
INSERT INTO `sys_dict_item` VALUES (23, 9, '0', '未回复', 'response_type', '微信消息-未回复', NULL, 0, ' ', ' ', '2019-03-28 21:29:47', '2019-03-28 21:29:47', '微信消息-未回复', '0');
INSERT INTO `sys_dict_item` VALUES (24, 9, '1', '已回复', 'response_type', '微信消息-已回复', NULL, 1, ' ', ' ', '2019-03-28 21:30:08', '2019-03-28 21:30:08', '微信消息-已回复', '0');
INSERT INTO `sys_dict_item` VALUES (25, 10, '1', '检索', 'param_type', '检索', NULL, 0, ' ', ' ', '2019-04-29 18:22:17', '2019-04-29 18:22:17', '检索', '0');
INSERT INTO `sys_dict_item` VALUES (26, 10, '2', '原文', 'param_type', '原文', NULL, 0, ' ', ' ', '2019-04-29 18:22:27', '2019-04-29 18:22:27', '原文', '0');
INSERT INTO `sys_dict_item` VALUES (27, 10, '3', '报表', 'param_type', '报表', NULL, 0, ' ', ' ', '2019-04-29 18:22:36', '2019-04-29 18:22:36', '报表', '0');
INSERT INTO `sys_dict_item` VALUES (28, 10, '4', '安全', 'param_type', '安全', NULL, 0, ' ', ' ', '2019-04-29 18:22:46', '2019-04-29 18:22:46', '安全', '0');
INSERT INTO `sys_dict_item` VALUES (29, 10, '5', '文档', 'param_type', '文档', NULL, 0, ' ', ' ', '2019-04-29 18:22:56', '2019-04-29 18:22:56', '文档', '0');
INSERT INTO `sys_dict_item` VALUES (30, 10, '6', '消息', 'param_type', '消息', NULL, 0, ' ', ' ', '2019-04-29 18:23:05', '2019-04-29 18:23:05', '消息', '0');
INSERT INTO `sys_dict_item` VALUES (31, 10, '9', '其他', 'param_type', '其他', NULL, 0, ' ', ' ', '2019-04-29 18:23:16', '2019-04-29 18:23:16', '其他', '0');
INSERT INTO `sys_dict_item` VALUES (32, 10, '0', '默认', 'param_type', '默认', NULL, 0, ' ', ' ', '2019-04-29 18:23:30', '2019-04-29 18:23:30', '默认', '0');
INSERT INTO `sys_dict_item` VALUES (33, 11, '0', '正常', 'status_type', '状态正常', NULL, 0, ' ', ' ', '2019-05-15 16:31:34', '2019-05-16 22:30:46', '状态正常', '0');
INSERT INTO `sys_dict_item` VALUES (34, 11, '9', '冻结', 'status_type', '状态冻结', NULL, 1, ' ', ' ', '2019-05-15 16:31:56', '2019-05-16 22:30:50', '状态冻结', '0');
INSERT INTO `sys_dict_item` VALUES (35, 12, '1', '系统类', 'dict_type', '系统类字典', NULL, 0, ' ', ' ', '2019-05-16 14:20:40', '2019-05-16 14:20:40', '不能修改删除', '0');
INSERT INTO `sys_dict_item` VALUES (36, 12, '0', '业务类', 'dict_type', '业务类字典', NULL, 0, ' ', ' ', '2019-05-16 14:20:59', '2019-05-16 14:20:59', '可以修改', '0');
INSERT INTO `sys_dict_item` VALUES (37, 2, 'GITEE', '码云', 'social_type', '码云', NULL, 2, ' ', ' ', '2019-06-28 09:59:12', '2019-06-28 09:59:12', '码云', '0');
INSERT INTO `sys_dict_item` VALUES (38, 2, 'OSC', '开源中国', 'social_type', '开源中国登录', NULL, 2, ' ', ' ', '2019-06-28 10:04:32', '2019-06-28 10:04:32', '', '0');
INSERT INTO `sys_dict_item` VALUES (39, 14, 'password', '密码模式', 'grant_types', '支持oauth密码模式', NULL, 0, ' ', ' ', '2019-08-13 07:35:28', '2019-08-13 07:35:28', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (40, 14, 'authorization_code', '授权码模式', 'grant_types', 'oauth2 授权码模式', NULL, 1, ' ', ' ', '2019-08-13 07:36:07', '2019-08-13 07:36:07', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (41, 14, 'client_credentials', '客户端模式', 'grant_types', 'oauth2 客户端模式', NULL, 2, ' ', ' ', '2019-08-13 07:36:30', '2019-08-13 07:36:30', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (42, 14, 'refresh_token', '刷新模式', 'grant_types', 'oauth2 刷新token', NULL, 3, ' ', ' ', '2019-08-13 07:36:54', '2019-08-13 07:36:54', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (43, 14, 'implicit', '简化模式', 'grant_types', 'oauth2 简化模式', NULL, 4, ' ', ' ', '2019-08-13 07:39:32', '2019-08-13 07:39:32', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (44, 15, '0', 'Avue', 'style_type', 'Avue风格', NULL, 0, ' ', ' ', '2020-02-07 03:52:52', '2020-02-07 03:52:52', '', '0');
INSERT INTO `sys_dict_item` VALUES (45, 15, '1', 'element', 'style_type', 'element-ui', NULL, 1, ' ', ' ', '2020-02-07 03:53:12', '2020-02-07 03:53:12', '', '0');
INSERT INTO `sys_dict_item` VALUES (46, 16, '0', '关', 'captcha_flag_types', '不校验验证码', NULL, 0, ' ', ' ', '2020-11-18 06:53:58', '2020-11-18 06:53:58', '不校验验证码 -0', '0');
INSERT INTO `sys_dict_item` VALUES (47, 16, '1', '开', 'captcha_flag_types', '校验验证码', NULL, 1, ' ', ' ', '2020-11-18 06:54:15', '2020-11-18 06:54:15', '不校验验证码-1', '0');
INSERT INTO `sys_dict_item` VALUES (48, 17, '0', '否', 'enc_flag_types', '不加密', NULL, 0, ' ', ' ', '2020-11-18 06:55:31', '2020-11-18 06:55:31', '不加密-0', '0');
INSERT INTO `sys_dict_item` VALUES (49, 17, '1', '是', 'enc_flag_types', '加密', NULL, 1, ' ', ' ', '2020-11-18 06:55:51', '2020-11-18 06:55:51', '加密-1', '0');
INSERT INTO `sys_dict_item` VALUES (50, 13, 'MERGE_PAY', '聚合支付', 'channel_type', '聚合支付', NULL, 1, ' ', ' ', '2019-05-30 19:08:08', '2019-06-18 13:51:53', '聚合支付', '0');
INSERT INTO `sys_dict_item` VALUES (51, 2, 'CAS', 'CAS登录', 'social_type', 'CAS 单点登录系统', NULL, 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (52, 2, 'DINGTALK', '钉钉', 'social_type', '钉钉', NULL, 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (53, 2, 'WEIXIN_CP', '企业微信', 'social_type', '企业微信', NULL, 3, ' ', ' ', '2022-02-18 13:56:25', '2022-02-18 13:56:28', NULL, '0');
INSERT INTO `sys_dict_item` VALUES (54, 15, '2', 'APP', 'style_type', 'uview风格', NULL, 1, ' ', ' ', '2020-02-07 03:53:12', '2020-02-07 03:53:12', '', '0');
INSERT INTO `sys_dict_item` VALUES (55, 13, 'ALIPAY_WAP', '支付宝支付', 'channel_type', '支付宝支付', NULL, 1, ' ', ' ', '2019-05-30 19:08:08', '2019-06-18 13:51:53', '聚合支付', '0');
INSERT INTO `sys_dict_item` VALUES (56, 13, 'WEIXIN_MP', '微信支付', 'channel_type', '微信支付', NULL, 1, ' ', ' ', '2019-05-30 19:08:08', '2019-06-18 13:51:53', '聚合支付', '0');
INSERT INTO `sys_dict_item` VALUES (57, 14, 'mobile', 'mobile', 'grant_types', '移动端登录', NULL, 5, 'admin', ' ', '2023-01-29 17:21:42', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (58, 18, '0', '有效', 'lock_flag', '有效', NULL, 0, 'admin', ' ', '2023-02-01 16:56:00', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (59, 18, '9', '禁用', 'lock_flag', '禁用', NULL, 1, 'admin', ' ', '2023-02-01 16:56:09', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (60, 15, '4', 'vue3', 'style_type', 'element-plus', NULL, 4, 'admin', ' ', '2023-02-06 13:52:43', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (61, 19, '0', '主机', 'ds_config_type', '主机', NULL, 0, 'admin', ' ', '2023-02-06 18:37:23', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (62, 19, '1', 'JDBC', 'ds_config_type', 'jdbc', NULL, 2, 'admin', ' ', '2023-02-06 18:37:34', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (63, 20, 'false', '否', 'common_status', '否', NULL, 1, 'admin', ' ', '2023-02-09 11:02:39', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (64, 20, 'true', '是', 'common_status', '是', NULL, 2, 'admin', ' ', '2023-02-09 11:02:52', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (65, 21, 'MINI', '小程序', 'app_social_type', '小程序登录', NULL, 0, 'admin', ' ', '2023-02-10 11:11:41', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (66, 22, '0', '否', 'yes_no_type', '0', NULL, 0, 'admin', ' ', '2023-02-20 23:35:23', NULL, '0', '0');
INSERT INTO `sys_dict_item` VALUES (67, 22, '1', '是', 'yes_no_type', '1', NULL, 0, 'admin', ' ', '2023-02-20 23:35:37', NULL, '1', '0');
INSERT INTO `sys_dict_item` VALUES (69, 23, 'text', '文本', 'repType', '文本', NULL, 0, 'admin', ' ', '2023-02-24 15:08:45', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (70, 23, 'image', '图片', 'repType', '图片', NULL, 0, 'admin', ' ', '2023-02-24 15:08:56', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (71, 23, 'voice', '语音', 'repType', '语音', NULL, 0, 'admin', ' ', '2023-02-24 15:09:08', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (72, 23, 'video', '视频', 'repType', '视频', NULL, 0, 'admin', ' ', '2023-02-24 15:09:18', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (73, 23, 'shortvideo', '小视频', 'repType', '小视频', NULL, 0, 'admin', ' ', '2023-02-24 15:09:29', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (74, 23, 'location', '地理位置', 'repType', '地理位置', NULL, 0, 'admin', ' ', '2023-02-24 15:09:41', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (75, 23, 'link', '链接消息', 'repType', '链接消息', NULL, 0, 'admin', ' ', '2023-02-24 15:09:49', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (76, 23, 'event', '事件推送', 'repType', '事件推送', NULL, 0, 'admin', ' ', '2023-02-24 15:09:57', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (77, 24, '0', '未提交', 'leave_status', '未提交', NULL, 0, 'admin', ' ', '2023-03-02 22:50:45', NULL, '未提交', '0');
INSERT INTO `sys_dict_item` VALUES (78, 24, '1', '审批中', 'leave_status', '审批中', NULL, 0, 'admin', ' ', '2023-03-02 22:50:57', NULL, '审批中', '0');
INSERT INTO `sys_dict_item` VALUES (79, 24, '2', '完成', 'leave_status', '完成', NULL, 0, 'admin', ' ', '2023-03-02 22:51:06', NULL, '完成', '0');
INSERT INTO `sys_dict_item` VALUES (80, 24, '9', '驳回', 'leave_status', '驳回', NULL, 0, 'admin', ' ', '2023-03-02 22:51:20', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (81, 25, 'record', '日程记录', 'schedule_type', '日程记录', NULL, 0, 'admin', ' ', '2023-03-06 14:50:01', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (82, 25, 'plan', '计划', 'schedule_type', '计划类型', NULL, 0, 'admin', ' ', '2023-03-06 14:50:29', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (83, 26, '0', '计划中', 'schedule_status', '日程状态', NULL, 0, 'admin', ' ', '2023-03-06 14:53:18', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (84, 26, '1', '已开始', 'schedule_status', '已开始', NULL, 0, 'admin', ' ', '2023-03-06 14:53:33', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (85, 26, '3', '已结束', 'schedule_status', '已结束', NULL, 0, 'admin', ' ', '2023-03-06 14:53:41', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (86, 27, 'mysql', 'mysql', 'ds_type', 'mysql', NULL, 0, 'admin', ' ', '2023-03-12 09:58:11', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (87, 27, 'pg', 'pg', 'ds_type', 'pg', NULL, 1, 'admin', ' ', '2023-03-12 09:58:20', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (88, 27, 'oracle', 'oracle', 'ds_type', 'oracle', NULL, 2, 'admin', ' ', '2023-03-12 09:58:29', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (89, 27, 'mssql', 'mssql', 'ds_type', 'mssql', NULL, 3, 'admin', ' ', '2023-03-12 09:58:42', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (90, 27, 'db2', 'db2', 'ds_type', 'db2', NULL, 4, 'admin', ' ', '2023-03-12 09:58:53', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (91, 27, 'dm', '达梦', 'ds_type', '达梦', NULL, 5, 'admin', ' ', '2023-03-12 09:59:07', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (92, 27, 'highgo', '瀚高', 'ds_type', '瀚高数据库', NULL, 5, 'admin', ' ', '2023-03-12 09:59:07', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (93, 28, '0', '公告', 'message_type', '主页公告显示', NULL, 0, 'admin', ' ', '2023-10-27 10:30:14', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (94, 28, '1', '站内信', 'message_type', '右上角显示', NULL, 1, 'admin', ' ', '2023-10-27 10:30:47', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (95, 29, '0', '黑名单', 'sensitive_type', '失败', NULL, 3, 'admin', ' ', '2023-10-27 10:30:47', NULL, NULL, '0');
INSERT INTO `sys_dict_item` VALUES (96, 29, '1', '白名单', 'sensitive_type', '失败', NULL, 3, 'admin', ' ', '2023-10-27 10:30:47', NULL, NULL, '0');
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
  `hash` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件hash',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '上传时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
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
  `pid` bigint DEFAULT NULL COMMENT '父ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='文件分类表';

DROP TABLE IF EXISTS `sys_sensitive_word`;
CREATE TABLE `sys_sensitive_word` (
  `sensitive_id` bigint NOT NULL COMMENT '主键',
  `sensitive_word` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '敏感词',
  `sensitive_type` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类型',
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`sensitive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='敏感词';

-- ----------------------------
-- Table structure for sys_i18n
-- ----------------------------
DROP TABLE IF EXISTS `sys_i18n`;
CREATE TABLE `sys_i18n` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'name',
  `zh_cn` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '中文',
  `en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '英文',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
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
INSERT INTO `sys_i18n` VALUES (28, 'router.internationalizationManagement', '国际化管理', 'Internationalization Management', 'admin', '2023-02-24 10:36:59', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (29, 'router.auditLog', '审计日志', 'Audit Log', 'admin', '2023-02-24 10:36:59', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (30, 'router.systemMonitoring', '系统监控', 'System Monitoring', 'admin', '2023-02-24 10:36:59', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (31, 'router.generatePages', '生成页面', 'Generate Pages', 'admin', '2023-02-24 10:44:04', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (32, 'router.templateManagement', '模板管理', 'Template Management', 'admin', '2023-02-24 10:44:31', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (33, 'router.templateGroup', '模板分组', 'Template Group', 'admin', '2023-02-24 10:45:10', '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (34, 'router.fieldManagement', '字段管理', 'Field Management', 'admin', '2023-02-24 10:46:04', 'admin', '2023-03-07 14:27:48', '0');
INSERT INTO `sys_i18n` VALUES (50, 'router.logManagement', '日志管理', 'Log Management', 'admin', NULL, '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (62, 'router.cacheMonitoring', '缓存监控', 'Cache Monitoring', ' ', '2023-08-10 13:42:35', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (73, 'router.message', '信息推送', 'Message Push', ' ', '2023-08-10 13:46:37', ' ', '2023-08-10 13:47:09', '0');
INSERT INTO `sys_i18n` VALUES (74, 'router.sensitiveWords', '敏感词管理', 'Sensitive words', ' ', '2023-08-10 13:46:37', ' ', '2023-08-10 13:47:09', '0');
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
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
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
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组件',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单图标',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '1' COMMENT '是否可见，0隐藏，1显示',
  `sort_order` int(11) DEFAULT '1' COMMENT '排序值，越小越靠前',
  `keep_alive` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否缓存，0否，1是',
  `embedded` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '是否内嵌，0否，1是',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '菜单类型，0:菜单 1:按钮',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志，0未删除，1已删除',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES (1000,'权限管理',NULL,'/system',NULL,2000,'iconfont icon-quanxianguanli','1',0,'0','0','0','','2018-09-28 08:29:53','admin','2023-11-01 16:39:55','0');
INSERT INTO `sys_menu` VALUES (1100,'用户管理',NULL,'/admin/user/index',NULL,1000,'iconfont icon-yonghuguanli','1',1,'0','0','0','','2017-11-02 22:24:37','admin','2023-11-01 16:40:44','0');
INSERT INTO `sys_menu` VALUES (1101,'用户新增','sys_user_add',NULL,NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:52:09',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1102,'用户修改','sys_user_edit',NULL,NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:52:48',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1103,'用户删除','sys_user_del',NULL,NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:54:01',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1104,'导入导出','sys_user_export',NULL,NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:54:01',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1105,'用户查看','sys_user_view',NULL,NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:54:01',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1200,'菜单管理',NULL,'/admin/menu/index',NULL,1000,'iconfont icon-caidan','1',2,'0','0','0','','2017-11-08 09:57:27','admin','2023-11-01 16:40:39','0');
INSERT INTO `sys_menu` VALUES (1201,'菜单新增','sys_menu_add',NULL,NULL,1200,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:15:53',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1202,'菜单修改','sys_menu_edit',NULL,NULL,1200,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:16:23',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1203,'菜单删除','sys_menu_del',NULL,NULL,1200,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:16:43',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1300,'角色管理',NULL,'/admin/role/index',NULL,1000,'iconfont icon-jiaoseguanli','1',3,'0',NULL,'0','','2017-11-08 10:13:37','admin','2025-01-26 22:24:11','0');
INSERT INTO `sys_menu` VALUES (1301,'角色新增','sys_role_add',NULL,NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:14:18',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1302,'角色修改','sys_role_edit',NULL,NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:14:41',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1303,'角色删除','sys_role_del',NULL,NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:14:59',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1304,'分配权限','sys_role_perm',NULL,NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2018-04-20 07:22:55',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1305,'角色导入导出','sys_role_export',NULL,NULL,1300,NULL,'1',4,'0',NULL,'1',' ','2022-03-26 15:54:34',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (1306,'角色查看','sys_role_view',NULL,NULL,1300,NULL,'1',4,'0',NULL,'1',' ','2022-03-26 15:54:34',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (1400,'部门管理',NULL,'/admin/dept/index',NULL,1000,'iconfont icon-bumenguanli','1',4,'0',NULL,'0','','2018-01-20 13:17:19','admin','2023-11-01 16:40:30','0');
INSERT INTO `sys_menu` VALUES (1401,'部门新增','sys_dept_add',NULL,NULL,1400,NULL,'1',1,'0',NULL,'1',' ','2018-01-20 14:56:16',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1402,'部门修改','sys_dept_edit',NULL,NULL,1400,NULL,'1',1,'0',NULL,'1',' ','2018-01-20 14:56:59',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1403,'部门删除','sys_dept_del',NULL,NULL,1400,NULL,'1',1,'0',NULL,'1',' ','2018-01-20 14:57:28',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (1600,'岗位管理',NULL,'/admin/post/index',NULL,1000,'iconfont icon-gangweitubiao','1',5,'1','0','0','','2022-03-26 13:04:14','admin','2023-11-01 17:02:55','0');
INSERT INTO `sys_menu` VALUES (1601,'岗位信息查看','sys_post_view',NULL,NULL,1600,NULL,'1',0,'0',NULL,'1',' ','2022-03-26 13:05:34',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (1602,'岗位信息新增','sys_post_add',NULL,NULL,1600,NULL,'1',1,'0',NULL,'1',' ','2022-03-26 13:06:00',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (1603,'岗位信息修改','sys_post_edit',NULL,NULL,1600,NULL,'1',2,'0',NULL,'1',' ','2022-03-26 13:06:31',' ','2022-03-26 13:06:38','0');
INSERT INTO `sys_menu` VALUES (1604,'岗位信息删除','sys_post_del',NULL,NULL,1600,NULL,'1',3,'0',NULL,'1',' ','2022-03-26 13:06:31',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (1605,'岗位导入导出','sys_post_export',NULL,NULL,1600,NULL,'1',4,'0',NULL,'1',' ','2022-03-26 13:06:31',' ','2022-03-26 06:32:02','0');
INSERT INTO `sys_menu` VALUES (2000,'系统管理',NULL,'/admin',NULL,-1,'iconfont icon-xitongguanli','1',1,'0',NULL,'0','','2017-11-07 20:56:00','admin','2023-11-01 16:25:58','0');
INSERT INTO `sys_menu` VALUES (2001,'日志管理',NULL,'/admin/logs',NULL,2000,'iconfont icon-xitongrizhi','1',1,'0','0','0','admin','2023-03-02 12:26:42','admin','2023-11-01 16:22:08','0');
INSERT INTO `sys_menu` VALUES (2100,'操作日志',NULL,'/admin/log/index',NULL,2001,'iconfont icon-jinridaiban','1',2,'0','0','0','','2017-11-20 14:06:22','admin','2023-03-02 12:28:57','0');
INSERT INTO `sys_menu` VALUES (2101,'日志删除','sys_log_del',NULL,NULL,2100,NULL,'1',1,'0',NULL,'1',' ','2017-11-20 20:37:37',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (2102,'导入导出','sys_log_export',NULL,NULL,2100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:54:01',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (2103,'审计日志',NULL,'/admin/audit/index',NULL,2001,'iconfont icon-biaodan','1',1,'0','0','0','',NULL,'admin','2023-03-02 12:28:47','0');
INSERT INTO `sys_menu` VALUES (2104,'审计记录表删除','sys_audit_del',NULL,NULL,2103,'1','1',3,'0',NULL,'1','',NULL,'admin','2023-02-28 20:23:43','0');
INSERT INTO `sys_menu` VALUES (2105,'导入导出','sys_audit_export',NULL,NULL,2103,'1','1',3,'0',NULL,'1','',NULL,'admin','2023-02-28 20:23:51','0');
INSERT INTO `sys_menu` VALUES (2106,'敏感数据查看','no_mask',NULL,NULL,2103,'1','1',3,'0',NULL,'1','',NULL,'admin','2023-02-28 20:23:51','0');
INSERT INTO `sys_menu` VALUES (2107,'日志查看','sys_log_view',NULL,NULL,2001,'1','1',3,'0',NULL,'1','',NULL,'admin','2023-02-28 20:23:51','0');
INSERT INTO `sys_menu` VALUES (2108,'日志查看','sys_audit_view',NULL,NULL,2103,'1','1',3,'0',NULL,'1','',NULL,'admin','2023-02-28 20:23:51','0');
INSERT INTO `sys_menu` VALUES (2200,'字典管理',NULL,'/admin/dict/index',NULL,2000,'iconfont icon-zidianguanli','1',6,'0',NULL,'0','','2017-11-29 11:30:52','admin','2023-02-16 15:24:29','0');
INSERT INTO `sys_menu` VALUES (2201,'字典删除','sys_dict_del',NULL,NULL,2200,NULL,'1',1,'0',NULL,'1',' ','2017-11-29 11:30:11',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (2202,'字典新增','sys_dict_add',NULL,NULL,2200,NULL,'1',1,'0',NULL,'1',' ','2018-05-11 22:34:55',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (2203,'字典修改','sys_dict_edit',NULL,NULL,2200,NULL,'1',1,'0',NULL,'1',' ','2018-05-11 22:36:03',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (2204,'字典查看','sys_dict_view',NULL,NULL,2200,NULL,'1',1,'0',NULL,'1',' ','2018-05-11 22:36:03',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (2210,'参数管理',NULL,'/admin/param/index',NULL,2000,'iconfont icon-canshuguanli','1',7,'1',NULL,'0','','2019-04-29 22:16:50','admin','2023-02-16 15:24:51','0');
INSERT INTO `sys_menu` VALUES (2211,'参数新增','sys_syspublicparam_add',NULL,NULL,2210,NULL,'1',1,'0',NULL,'1',' ','2019-04-29 22:17:36',' ','2020-03-24 08:57:11','0');
INSERT INTO `sys_menu` VALUES (2212,'参数删除','sys_syspublicparam_del',NULL,NULL,2210,NULL,'1',1,'0',NULL,'1',' ','2019-04-29 22:17:55',' ','2020-03-24 08:57:12','0');
INSERT INTO `sys_menu` VALUES (2213,'参数编辑','sys_syspublicparam_edit',NULL,NULL,2210,NULL,'1',1,'0',NULL,'1',' ','2019-04-29 22:18:14',' ','2020-03-24 08:57:13','0');
INSERT INTO `sys_menu` VALUES (2214,'参数查看','sys_syspublicparam_view',NULL,NULL,2210,NULL,'1',1,'0',NULL,'1',' ','2019-04-29 22:18:14',' ','2020-03-24 08:57:13','0');
INSERT INTO `sys_menu` VALUES (2300,'代码生成',NULL,'/gen/table/index',NULL,9000,'iconfont icon-daimashengcheng','1',2,'0','0','0','','2018-01-20 13:17:19','admin','2023-02-20 13:54:35','0');
INSERT INTO `sys_menu` VALUES (2400,'终端管理',NULL,'/admin/client/index',NULL,2000,'iconfont icon-shouji','1',9,'1',NULL,'0','','2018-01-20 13:17:19','admin','2023-02-16 15:25:28','0');
INSERT INTO `sys_menu` VALUES (2401,'客户端新增','sys_client_add',NULL,NULL,2400,'1','1',1,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (2402,'客户端修改','sys_client_edit',NULL,NULL,2400,NULL,'1',1,'0',NULL,'1',' ','2018-05-15 21:37:06',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (2403,'客户端删除','sys_client_del',NULL,NULL,2400,NULL,'1',1,'0',NULL,'1',' ','2018-05-15 21:39:16',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (2404,'客户端查看','sys_client_view',NULL,NULL,2400,NULL,'1',1,'0',NULL,'1',' ','2018-05-15 21:39:16',' ','2021-05-25 03:12:55','0');
INSERT INTO `sys_menu` VALUES (2500,'密钥管理',NULL,'/admin/social/index',NULL,2000,'iconfont icon-miyueguanli','1',10,'0',NULL,'0','','2018-01-20 13:17:19','admin','2025-01-26 22:25:20','0');
INSERT INTO `sys_menu` VALUES (2501,'密钥新增','sys_social_details_add',NULL,NULL,2500,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:19','0');
INSERT INTO `sys_menu` VALUES (2502,'密钥修改','sys_social_details_edit',NULL,NULL,2500,'1','1',1,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:19','0');
INSERT INTO `sys_menu` VALUES (2503,'密钥删除','sys_social_details_del',NULL,NULL,2500,'1','1',2,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:23','0');
INSERT INTO `sys_menu` VALUES (2504,'密钥查看','sys_social_details_view',NULL,NULL,2500,'1','1',2,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:23','0');
INSERT INTO `sys_menu` VALUES (2600,'令牌管理',NULL,'/admin/token/index',NULL,2000,'iconfont icon-lingpai','1',11,'0',NULL,'0','','2018-09-04 05:58:41','admin','2023-02-16 15:28:28','0');
INSERT INTO `sys_menu` VALUES (2601,'令牌删除','sys_token_del',NULL,NULL,2600,NULL,'1',1,'0',NULL,'1',' ','2018-09-04 05:59:50',' ','2020-03-24 08:57:24','0');
INSERT INTO `sys_menu` VALUES (2800,'Quartz管理',NULL,'/daemon/job-manage/index',NULL,2000,'iconfont icon-quartz','1',4,'0',NULL,'0','','2018-01-20 13:17:19','admin','2023-11-27 14:52:53','0');
INSERT INTO `sys_menu` VALUES (2810,'任务新增','job_sys_job_add',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:26','0');
INSERT INTO `sys_menu` VALUES (2820,'任务修改','job_sys_job_edit',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:27','0');
INSERT INTO `sys_menu` VALUES (2830,'任务删除','job_sys_job_del',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:28','0');
INSERT INTO `sys_menu` VALUES (2840,'任务暂停','job_sys_job_shutdown_job',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:28','0');
INSERT INTO `sys_menu` VALUES (2850,'任务开始','job_sys_job_start_job',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:29','0');
INSERT INTO `sys_menu` VALUES (2860,'任务刷新','job_sys_job_refresh_job',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:30','0');
INSERT INTO `sys_menu` VALUES (2870,'执行任务','job_sys_job_run_job',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2019-08-08 15:35:18',' ','2020-03-24 08:57:31','0');
INSERT INTO `sys_menu` VALUES (2871,'导出','job_sys_job_export',NULL,NULL,2800,NULL,'1',0,'0','0','1','admin','2023-03-06 15:26:13',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (2900,'国际化管理',NULL,'/admin/i18n/index',NULL,2000,'iconfont icon-zhongyingwenqiehuan','1',8,'0',NULL,'0','',NULL,'admin','2023-02-16 15:25:18','0');
INSERT INTO `sys_menu` VALUES (2901,'系统表-国际化查看','sys_i18n_view',NULL,NULL,2900,'1','1',0,'0',NULL,'1',' ',NULL,' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (2902,'系统表-国际化新增','sys_i18n_add',NULL,NULL,2900,'1','1',1,'0',NULL,'1',' ',NULL,' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (2903,'系统表-国际化修改','sys_i18n_edit',NULL,NULL,2900,'1','1',2,'0',NULL,'1',' ',NULL,' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (2904,'系统表-国际化删除','sys_i18n_del',NULL,NULL,2900,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (2905,'导入导出','sys_i18n_export',NULL,NULL,2900,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (2906,'文件管理',NULL,'/admin/file/index',NULL,2000,'iconfont icon-wendangguanli','1',6,'0',NULL,'0','','2019-06-25 12:44:46','admin','2023-02-16 15:24:42','0');
INSERT INTO `sys_menu` VALUES (2907,'删除文件','sys_file_del',NULL,NULL,2906,NULL,'1',1,'0',NULL,'1',' ','2019-06-25 13:41:41',' ','2020-03-24 08:58:42','0');
INSERT INTO `sys_menu` VALUES (2908,'查看文件','sys_file_view',NULL,NULL,2906,NULL,'1',1,'0',NULL,'1',' ','2019-06-25 13:41:41',' ','2020-03-24 08:58:42','0');
INSERT INTO `sys_menu` VALUES (2910,'行政区划','','/admin/sysArea/index',NULL,2000,'iconfont icon-hangzhengquhuaguanli','1',99,'0',NULL,'0','',NULL,'admin','2024-02-16 22:11:03','0');
INSERT INTO `sys_menu` VALUES (2911,'行政区划表查看','sys_sysArea_view',NULL,NULL,2910,'1','1',0,'0',NULL,'1',' ',NULL,' ','2024-02-17 14:31:09','0');
INSERT INTO `sys_menu` VALUES (2912,'行政区划表新增','sys_sysArea_add',NULL,NULL,2910,'1','1',1,'0',NULL,'1',' ',NULL,' ','2024-02-17 14:31:16','0');
INSERT INTO `sys_menu` VALUES (2913,'行政区划表删除','sys_sysArea_del',NULL,NULL,2910,'1','1',3,'0',NULL,'1',' ',NULL,' ','2024-02-17 14:31:21','0');
INSERT INTO `sys_menu` VALUES (2914,'导入导出','sys_sysArea_export',NULL,NULL,2910,'1','1',3,'0',NULL,'1',' ',NULL,' ','2024-02-17 14:31:26','0');
INSERT INTO `sys_menu` VALUES (2915,'行政区划表修改','sys_sysArea_edit',NULL,NULL,2910,'1','1',2,'0',NULL,'1',' ',NULL,' ','2024-02-17 14:31:31','0');
INSERT INTO `sys_menu` VALUES (2920,'敏感词管理','','/admin/sensitive/index',NULL,2000,'iconfont icon-sensitiveword','1',12,'0',NULL,'0','',NULL,'admin','2024-07-07 15:09:27','0');
INSERT INTO `sys_menu` VALUES (2921,'敏感词查看','admin_sysSensitiveWord_view',NULL,NULL,2920,'1','1',0,'0',NULL,'1',' ',NULL,' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (2922,'敏感词新增','admin_sysSensitiveWord_add',NULL,NULL,2920,'1','1',1,'0',NULL,'1',' ',NULL,' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (2923,'敏感词修改','admin_sysSensitiveWord_edit',NULL,NULL,2920,'1','1',2,'0',NULL,'1',' ',NULL,' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (2924,'敏感词删除','admin_sysSensitiveWord_del',NULL,NULL,2920,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (2925,'导入导出','admin_sysSensitiveWord_export',NULL,NULL,2920,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (4002,'缓存监控',NULL,'/ext/cache',NULL,2000,'iconfont icon-huancunjiankong','1',1,'0','0','0','admin','2023-05-29 15:12:59','admin','2023-11-27 14:52:56','0');
INSERT INTO `sys_menu` VALUES (4010,'信息推送','','/admin/message/index',NULL,2000,'iconfont icon-xinxituisong','1',7,'0',NULL,'0','',NULL,'admin','2025-01-26 22:22:24','0');
INSERT INTO `sys_menu` VALUES (4011,'信息推送查看','sys_message_view',NULL,NULL,4010,'1','1',0,'0',NULL,'1','',NULL,'admin','2023-10-25 14:51:54','0');
INSERT INTO `sys_menu` VALUES (4012,'信息推送新增','sys_message_add',NULL,NULL,4010,'1','1',1,'0',NULL,'1','',NULL,'admin','2023-10-25 14:52:00','0');
INSERT INTO `sys_menu` VALUES (4013,'信息推送修改','sys_message_edit',NULL,NULL,4010,'1','1',2,'0',NULL,'1','',NULL,'admin','2023-10-25 14:52:04','0');
INSERT INTO `sys_menu` VALUES (4014,'信息推送删除','sys_message_del',NULL,NULL,4010,'1','1',3,'0',NULL,'1','',NULL,'admin','2023-10-25 14:52:09','0');
INSERT INTO `sys_menu` VALUES (9000,'开发平台',NULL,'/gen',NULL,-1,'iconfont icon-DevOps','1',9,'0','0','0','','2019-08-12 09:35:16','admin','2025-01-26 22:38:09','0');
INSERT INTO `sys_menu` VALUES (9005,'数据源管理',NULL,'/gen/datasource/index',NULL,9000,'iconfont icon-shujuyuanguanli','1',0,'0',NULL,'0','','2019-08-12 09:42:11','admin','2025-01-26 22:26:51','0');
INSERT INTO `sys_menu` VALUES (9006,'表单设计',NULL,'/gen/design/index',NULL,9000,'iconfont icon-AIshiyanshi','0',2,'0','0','0','','2019-08-16 10:08:56','admin','2023-02-23 14:06:50','0');
INSERT INTO `sys_menu` VALUES (9007,'生成页面',NULL,'/gen/gener/index',NULL,9000,'iconfont icon-tongzhi4','0',1,'0','0','0','admin','2023-02-20 09:58:23','admin','2023-02-20 14:41:43','0');
INSERT INTO `sys_menu` VALUES (9050,'元数据管理',NULL,'/gen/metadata',NULL,9000,'iconfont icon-yuanshujuguanli','1',9,'0','0','0','','2018-07-27 01:13:21','admin','2023-02-23 19:55:10','0');
INSERT INTO `sys_menu` VALUES (9051,'模板管理',NULL,'/gen/template/index',NULL,9050,'iconfont icon-mti-mobanguanli','1',5,'0','0','0','admin','2023-02-21 11:22:54','admin','2023-02-23 19:56:03','0');
INSERT INTO `sys_menu` VALUES (9052,'查询','codegen_template_view',NULL,NULL,9051,NULL,'0',0,'0','0','1','admin','2023-02-21 12:33:03','admin','2023-02-21 13:50:54','0');
INSERT INTO `sys_menu` VALUES (9053,'增加','codegen_template_add',NULL,NULL,9051,NULL,'1',0,'0','0','1','admin','2023-02-21 13:34:10','admin','2023-02-21 13:39:49','0');
INSERT INTO `sys_menu` VALUES (9054,'新增','codegen_template_add',NULL,NULL,9051,NULL,'0',1,'0','0','1','admin','2023-02-21 13:51:32',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (9055,'导出','codegen_template_export',NULL,NULL,9051,NULL,'0',2,'0','0','1','admin','2023-02-21 13:51:58',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (9056,'删除','codegen_template_del',NULL,NULL,9051,NULL,'0',3,'0','0','1','admin','2023-02-21 13:52:16',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (9057,'编辑','codegen_template_edit',NULL,NULL,9051,NULL,'0',4,'0','0','1','admin','2023-02-21 13:52:58',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (9059,'模板分组',NULL,'/gen/group/index',NULL,9050,'iconfont icon-shuxingtu','1',6,'0','0','0','admin','2023-02-21 15:06:50','admin','2023-02-23 19:55:25','0');
INSERT INTO `sys_menu` VALUES (9060,'查询','codegen_group_view',NULL,NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:08:07',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (9061,'新增','codegen_group_add',NULL,NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:08:28',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (9062,'修改','codegen_group_edit',NULL,NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:08:43',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (9063,'删除','codegen_group_del',NULL,NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:09:02',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (9064,'导出','codegen_group_export',NULL,NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:09:22',' ',NULL,'0');
INSERT INTO `sys_menu` VALUES (9065,'字段管理',NULL,'/gen/field-type/index',NULL,9050,'iconfont icon-field-manage','1',0,'0','0','0','admin','2023-02-23 20:05:09','admin','2025-01-26 22:31:28','0');
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
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
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
INSERT INTO `sys_oauth_client_details` VALUES (8, 'mini', NULL, 'mini', 'server', 'password,mobile', NULL, NULL, 160000000, 160000000, '{\"captcha_flag\":\"0\",\"enc_flag\":\"0\",\"online_quantity\":\"1\"}', 'true', '0', 'admin', 'admin', '2023-01-29 16:38:06', '2023-01-29 17:21:56');
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
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '更新人',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='岗位信息表';

-- ----------------------------
-- Records of sys_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_post` VALUES (1, 'TEAM_LEADER', '部门负责人', 0, 'LEADER', '0', '2022-03-26 13:48:17', '', '2023-03-08 16:03:35', 'admin');
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
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
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
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色编码',
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  PRIMARY KEY (`role_id`) USING BTREE,
  KEY `role_idx1_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', '管理员', '', 'edg134', '2017-10-29 15:45:51', '2023-04-06 14:03:28', '0');
INSERT INTO `sys_role` VALUES (2, '普通用户', 'GENERAL_USER', '普通用户', '', 'admin', '2022-03-31 17:03:15', '2023-04-03 02:28:51', '0');
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
INSERT INTO `sys_role_menu` VALUES (1, 1105);
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
INSERT INTO `sys_role_menu` VALUES (1, 1306);
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
INSERT INTO `sys_role_menu` VALUES (1, 2103);
INSERT INTO `sys_role_menu` VALUES (1, 2104);
INSERT INTO `sys_role_menu` VALUES (1, 2105);
INSERT INTO `sys_role_menu` VALUES (1, 2106);
INSERT INTO `sys_role_menu` VALUES (1, 2107);
INSERT INTO `sys_role_menu` VALUES (1, 2108);
INSERT INTO `sys_role_menu` VALUES (1, 2200);
INSERT INTO `sys_role_menu` VALUES (1, 2201);
INSERT INTO `sys_role_menu` VALUES (1, 2202);
INSERT INTO `sys_role_menu` VALUES (1, 2203);
INSERT INTO `sys_role_menu` VALUES (1, 2204);
INSERT INTO `sys_role_menu` VALUES (1, 2210);
INSERT INTO `sys_role_menu` VALUES (1, 2211);
INSERT INTO `sys_role_menu` VALUES (1, 2212);
INSERT INTO `sys_role_menu` VALUES (1, 2213);
INSERT INTO `sys_role_menu` VALUES (1, 2214);
INSERT INTO `sys_role_menu` VALUES (1, 2300);
INSERT INTO `sys_role_menu` VALUES (1, 2400);
INSERT INTO `sys_role_menu` VALUES (1, 2401);
INSERT INTO `sys_role_menu` VALUES (1, 2402);
INSERT INTO `sys_role_menu` VALUES (1, 2403);
INSERT INTO `sys_role_menu` VALUES (1, 2404);
INSERT INTO `sys_role_menu` VALUES (1, 2500);
INSERT INTO `sys_role_menu` VALUES (1, 2501);
INSERT INTO `sys_role_menu` VALUES (1, 2502);
INSERT INTO `sys_role_menu` VALUES (1, 2503);
INSERT INTO `sys_role_menu` VALUES (1, 2504);
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
INSERT INTO `sys_role_menu` VALUES (1, 2910);
INSERT INTO `sys_role_menu` VALUES (1, 2911);
INSERT INTO `sys_role_menu` VALUES (1, 2912);
INSERT INTO `sys_role_menu` VALUES (1, 2913);
INSERT INTO `sys_role_menu` VALUES (1, 2914);
INSERT INTO `sys_role_menu` VALUES (1, 2915);
INSERT INTO `sys_role_menu` VALUES (1, 2920);
INSERT INTO `sys_role_menu` VALUES (1, 2921);
INSERT INTO `sys_role_menu` VALUES (1, 2922);
INSERT INTO `sys_role_menu` VALUES (1, 2923);
INSERT INTO `sys_role_menu` VALUES (1, 2924);
INSERT INTO `sys_role_menu` VALUES (1, 2925);
INSERT INTO `sys_role_menu` VALUES (1, 4002);
INSERT INTO `sys_role_menu` VALUES (1, 4010);
INSERT INTO `sys_role_menu` VALUES (1, 4011);
INSERT INTO `sys_role_menu` VALUES (1, 4012);
INSERT INTO `sys_role_menu` VALUES (1, 4013);
INSERT INTO `sys_role_menu` VALUES (1, 4014);
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
-- Table structure for sys_schedule
-- ----------------------------
DROP TABLE IF EXISTS `sys_schedule`;
CREATE TABLE `sys_schedule` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标题',
  `schedule_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '日程类型',
  `schedule_state` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '状态',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '内容',
  `schedule_time` time DEFAULT NULL COMMENT '时间',
  `schedule_date` date DEFAULT NULL COMMENT '日期',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统日程管理表';

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
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统社交登录账号表';

-- ----------------------------
-- Records of sys_social_details
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
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
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `lock_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '锁定标记，0未锁定，9已锁定',
  `password_expire_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '密码是否过期，0未过期，9已过期',
  `password_modify_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  `wx_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信登录openId',
  `mini_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '小程序openId',
  `qq_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'QQ openId',
  `gitee_login` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '码云标识',
  `osc_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '开源中国标识',
  `wx_cp_userid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '企业微信唯一ID',
  `wx_ding_userid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '钉钉唯一ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '主部门ID',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$c/Ae0pRjJtMZg3BnvVpO.eIK6WYWVbKTzqgdy3afR7w.vd.xi3Mgy', '', '17338122125', '/admin/sys-file/local/2a14ae08150e483c93e12ac8934173e2.png', '管理员666777', '管理员', 'admin@pig4cloud.com', ' ', 'admin', '2018-04-20 07:15:18', '2023-04-03 14:00:06', '0', '0', NULL, '0', NULL, 'oBxPy5E-v82xWGsfzZVzkD3wEX64', NULL, 'log4j', NULL, NULL,NULL,1);
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


-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message` (
  `id` bigint NOT NULL COMMENT '主键',
  `category` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分类',
  `title` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标题',
  `content` text COLLATE utf8mb4_general_ci COMMENT '内容',
  `send_flag` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否推送',
  `all_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '全部接受',
  `sort` int unsigned NOT NULL DEFAULT '0' COMMENT '排序 （越大越在前）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='站内信';

-- ----------------------------
-- Records of sys_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_message_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_message_relation`;
CREATE TABLE `sys_message_relation` (
  `id` bigint NOT NULL COMMENT '主键',
  `msg_id` bigint DEFAULT NULL COMMENT '消息ID',
  `user_id` bigint DEFAULT NULL COMMENT '接收人ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '内容',
  `read_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '已读（0否，1是）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统消息推送记录';

DROP TABLE IF EXISTS `sys_system_config`;
CREATE TABLE `sys_system_config` (
  `id` bigint NOT NULL COMMENT '主键',
  `config_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '配置类型',
  `config_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '配置名称',
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '配置标识',
  `config_value` longtext COLLATE utf8mb4_general_ci COMMENT '配置值',
  `config_status` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '开启状态',
  `create_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统配置';


-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `dept_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户部门表';

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`) VALUES (1, 1);
COMMIT;

-- ----------------------------
SET FOREIGN_KEY_CHECKS = 1;


-- ============================================

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_job`;
CREATE TABLE `sys_job` (
                           `job_id` bigint NOT NULL COMMENT '任务id',
                           `job_name` varchar(64) NOT NULL COMMENT '任务名称',
                           `job_group` varchar(64) NOT NULL COMMENT '任务组名',
                           `job_order` char(1) DEFAULT '1' COMMENT '组内执行顺利，值越大执行优先级越高，最大值9，最小值1',
                           `job_type` char(1) NOT NULL DEFAULT '1' COMMENT '1、java类;2、spring bean名称;3、rest调用;4、jar调用;9其他',
                           `execute_path` varchar(500) DEFAULT NULL COMMENT 'job_type=3时，rest调用地址，仅支持rest get协议,需要增加String返回值，0成功，1失败;job_type=4时，jar路径;其它值为空',
                           `class_name` varchar(500) DEFAULT NULL COMMENT 'job_type=1时，类完整路径;job_type=2时，spring bean名称;其它值为空',
                           `method_name` varchar(500) DEFAULT NULL COMMENT '任务方法',
                           `method_params_value` varchar(2000) DEFAULT NULL COMMENT '参数值',
                           `cron_expression` varchar(255) DEFAULT NULL COMMENT 'cron执行表达式',
                           `misfire_policy` varchar(20) DEFAULT '3' COMMENT '错失执行策略（1错失周期立即执行 2错失周期执行一次 3下周期执行）',
                           `job_status` char(1) DEFAULT '0' COMMENT '状态（1、未发布;2、运行中;3、暂停;4、删除;）',
                           `job_execute_status` char(1) DEFAULT '0' COMMENT '状态（0正常 1异常）',
                           `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           `start_time` timestamp NULL DEFAULT NULL COMMENT '初次执行时间',
                           `previous_time` timestamp NULL DEFAULT NULL COMMENT '上次执行时间',
                           `next_time` timestamp NULL DEFAULT NULL COMMENT '下次执行时间',
                           `remark` varchar(500) DEFAULT '' COMMENT '备注信息',
                           PRIMARY KEY (`job_id`) USING BTREE,
                           UNIQUE KEY `job_name_group_idx` (`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB  COMMENT='定时任务调度表';

-- ----------------------------
DROP TABLE IF EXISTS `sys_job_log`;
CREATE TABLE `sys_job_log` (
                               `job_log_id` bigint NOT NULL COMMENT '任务日志ID',
                               `job_id` bigint NOT NULL COMMENT '任务id',
                               `job_name` varchar(64)  DEFAULT NULL COMMENT '任务名称',
                               `job_group` varchar(64)  DEFAULT NULL COMMENT '任务组名',
                               `job_order` char(1)  DEFAULT NULL COMMENT '组内执行顺利，值越大执行优先级越高，最大值9，最小值1',
                               `job_type` char(1)  NOT NULL DEFAULT '1' COMMENT '1、java类;2、spring bean名称;3、rest调用;4、jar调用;9其他',
                               `execute_path` varchar(500)  DEFAULT NULL COMMENT 'job_type=3时，rest调用地址，仅支持post协议;job_type=4时，jar路径;其它值为空',
                               `class_name` varchar(500)  DEFAULT NULL COMMENT 'job_type=1时，类完整路径;job_type=2时，spring bean名称;其它值为空',
                               `method_name` varchar(500)  DEFAULT NULL COMMENT '任务方法',
                               `method_params_value` varchar(2000)  DEFAULT NULL COMMENT '参数值',
                               `cron_expression` varchar(255)  DEFAULT NULL COMMENT 'cron执行表达式',
                               `job_message` varchar(500)  DEFAULT NULL COMMENT '日志信息',
                               `job_log_status` char(1)  DEFAULT '0' COMMENT '执行状态（0正常 1失败）',
                               `execute_time` varchar(30)  DEFAULT NULL COMMENT '执行时间',
                               `exception_info` varchar(2000)  DEFAULT '' COMMENT '异常信息',
                               `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                               PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE=InnoDB  COMMENT='定时任务执行日志表';


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

-- ----------------------------
-- Table structure for gen_datasource_conf
-- ----------------------------
DROP TABLE IF EXISTS `gen_datasource_conf`;
CREATE TABLE `gen_datasource_conf` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(64)  DEFAULT NULL COMMENT '别名',
  `url` text  NOT NULL COMMENT 'jdbcurl',
  `username` varchar(64)  DEFAULT NULL COMMENT '用户名',
  `password` varchar(64)  DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新',
  `del_flag` char(1)  DEFAULT '0' COMMENT '删除标记',
  `ds_type` varchar(64)  DEFAULT NULL COMMENT '数据库类型',
  `conf_type` char(1)  DEFAULT NULL COMMENT '配置类型',
  `ds_name` varchar(64)  DEFAULT NULL COMMENT '数据库名称',
  `instance` varchar(64)  DEFAULT NULL COMMENT '实例',
  `port` int DEFAULT NULL COMMENT '端口',
  `host` varchar(128)  DEFAULT NULL COMMENT '主机',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB  COMMENT='数据源表';

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
  `id` bigint NOT NULL COMMENT '主键',
  `column_type` varchar(200)  DEFAULT NULL COMMENT '字段类型',
  `attr_type` varchar(200)  DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200)  DEFAULT NULL COMMENT '属性包名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64)  DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(64)  DEFAULT NULL COMMENT '修改人',
  `del_flag` char(1)  DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `column_type` (`column_type`)
) ENGINE=InnoDB AUTO_INCREMENT=1634915190321451010  COMMENT='字段类型管理';

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
  `group_name` varchar(255)  DEFAULT NULL COMMENT '分组名称',
  `group_desc` varchar(255)  DEFAULT NULL COMMENT '分组描述',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改人',
  `del_flag` char(1)  DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='模板分组';


-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `id` bigint NOT NULL,
  `table_name` varchar(200)  DEFAULT NULL COMMENT '表名',
  `class_name` varchar(200)  DEFAULT NULL COMMENT '类名',
  `db_type` varchar(200)  DEFAULT NULL COMMENT '数据库类型',
  `table_comment` varchar(200)  DEFAULT NULL COMMENT '说明',
  `author` varchar(200)  DEFAULT NULL COMMENT '作者',
  `email` varchar(200)  DEFAULT NULL COMMENT '邮箱',
  `package_name` varchar(200)  DEFAULT NULL COMMENT '项目包名',
  `version` varchar(200)  DEFAULT NULL COMMENT '项目版本号',
  `i18n` char(1)  DEFAULT '0' COMMENT '是否生成带有i18n 0 不带有 1带有',
  `style`  bigint DEFAULT NULL COMMENT '代码风格',
  `sync_menu_id` bigint DEFAULT NULL COMMENT '所属菜单ID',
  `child_table_name` varchar(200)  DEFAULT NULL COMMENT '子表名称',
  `main_field` varchar(200)  DEFAULT NULL COMMENT '主表关联键',
  `child_field` varchar(200)  DEFAULT NULL COMMENT '子表关联键',
  `generator_type` char(1)  DEFAULT '0' COMMENT '生成方式  0：zip压缩包   1：自定义目录',
  `backend_path` varchar(500)  DEFAULT NULL COMMENT '后端生成路径',
  `frontend_path` varchar(500)  DEFAULT NULL COMMENT '前端生成路径',
  `module_name` varchar(200)  DEFAULT NULL COMMENT '模块名',
  `function_name` varchar(200)  DEFAULT NULL COMMENT '功能名',
  `form_layout` tinyint DEFAULT NULL COMMENT '表单布局  1：一列   2：两列',
  `ds_name` varchar(200)  DEFAULT NULL COMMENT '数据源ID',
  `baseclass_id` bigint DEFAULT NULL COMMENT '基类ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `table_name` (`table_name`,`ds_name`) USING BTREE
) ENGINE=InnoDB  COMMENT='代码生成表';

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
  `id` bigint NOT NULL,
  `ds_name` varchar(200)  DEFAULT NULL COMMENT '数据源名称',
  `table_name` varchar(200)  DEFAULT NULL COMMENT '表名称',
  `field_name` varchar(200)  DEFAULT NULL COMMENT '字段名称',
  `field_type` varchar(200)  DEFAULT NULL COMMENT '字段类型',
  `field_comment` varchar(200)  DEFAULT NULL COMMENT '字段说明',
  `attr_name` varchar(200)  DEFAULT NULL COMMENT '属性名',
  `attr_type` varchar(200)  DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200)  DEFAULT NULL COMMENT '属性包名',
  `sort` int DEFAULT NULL COMMENT '排序',
  `auto_fill` varchar(20)  DEFAULT NULL COMMENT '自动填充  DEFAULT、INSERT、UPDATE、INSERT_UPDATE',
  `primary_pk` char(1)  DEFAULT '0' COMMENT '主键 0：否  1：是',
  `base_field` char(1)  DEFAULT '0' COMMENT '基类字段 0：否  1：是',
  `form_item` char(1)  DEFAULT '0' COMMENT '表单项 0：否  1：是',
  `form_required` char(1)  DEFAULT '0' COMMENT '表单必填 0：否  1：是',
  `form_type` varchar(200)  DEFAULT NULL COMMENT '表单类型',
  `form_validator` varchar(200)  DEFAULT NULL COMMENT '表单效验',
  `grid_item` char(1)  DEFAULT '0' COMMENT '列表项 0：否  1：是',
  `grid_sort` char(1)  DEFAULT '0' COMMENT '列表排序 0：否  1：是',
  `query_item` char(1)  DEFAULT '0' COMMENT '查询项 0：否  1：是',
  `query_type` varchar(200)  DEFAULT NULL COMMENT '查询方式',
  `query_form_type` varchar(200)  DEFAULT NULL COMMENT '查询表单类型',
  `field_dict` varchar(200)  DEFAULT NULL COMMENT '字典类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='代码生成表字段';

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
  `template_name` varchar(255)  NOT NULL COMMENT '模板名称',
  `generator_path` varchar(255)  NOT NULL COMMENT '模板路径',
  `template_desc` varchar(255)  NOT NULL COMMENT '模板描述',
  `template_code` text  NOT NULL COMMENT '模板代码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新',
  `del_flag` char(1)  NOT NULL DEFAULT '0' COMMENT '删除标记',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  COMMENT='模板';


-- ----------------------------
-- Table structure for gen_template_group
-- ----------------------------
DROP TABLE IF EXISTS `gen_template_group`;
CREATE TABLE `gen_template_group` (
  `group_id` bigint NOT NULL COMMENT '分组id',
  `template_id` bigint NOT NULL COMMENT '模板id',
  PRIMARY KEY (`group_id`,`template_id`)
) ENGINE=InnoDB  COMMENT='模板分组关联表';

-- ----------------------------
-- Table structure for gen_form_conf
-- ----------------------------
DROP TABLE IF EXISTS `gen_form_conf`;
CREATE TABLE `gen_form_conf` (
  `id` bigint NOT NULL COMMENT 'ID',
  `ds_name` varchar(64) DEFAULT NULL COMMENT '数据库名称',
  `table_name` varchar(64) DEFAULT NULL COMMENT '表名称',
  `form_info` text NOT NULL COMMENT '表单信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `table_name` (`table_name`) USING BTREE
) ENGINE=InnoDB COMMENT='表单配置';

-- ----------------------------
-- Table structure for gen_create_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_create_table`;
CREATE TABLE `gen_create_table` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `table_name` varchar(32) NOT NULL COMMENT '表名称',
  `ds_name` varchar(32) DEFAULT NULL COMMENT '数据源',
  `comments` varchar(512) DEFAULT NULL COMMENT '表注释',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  `column_info` text NOT NULL COMMENT '字段信息',
  `del_flag` char(1) DEFAULT NULL COMMENT '删除标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB COMMENT='自动创建表管理';

SET FOREIGN_KEY_CHECKS = 1;
