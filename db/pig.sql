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
INSERT INTO `sys_i18n` VALUES (75, 'router.clarityMonitoring', '站点统计', 'Site Analytics', 'admin', '2026-03-26 00:00:00', 'admin', NULL, '0');
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
INSERT INTO `sys_role_menu` VALUES (1, 4003);
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
-- Records of gen_group
-- ----------------------------
INSERT INTO `gen_group` (`id`, `group_name`, `group_desc`, `create_by`, `create_time`, `del_flag`) VALUES (1, '单表增删改查', NULL, 'admin', now(), '0');

-- ----------------------------
-- Records of gen_template
-- ----------------------------
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `del_flag`) VALUES (1, 'Controller', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java', 'Controller', 'package ${package}.${moduleName}.controller;\n\n#if($queryList)\nimport cn.hutool.core.util.StrUtil;\n#end\nimport cn.hutool.core.util.ArrayUtil;\nimport cn.hutool.core.collection.CollUtil;\nimport com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\nimport com.baomidou.mybatisplus.core.toolkit.Wrappers;\nimport com.baomidou.mybatisplus.extension.plugins.pagination.Page;\nimport ${package}.common.core.util.R;\nimport ${package}.common.log.annotation.SysLog;\n#if($opensource)\nimport com.pig4cloud.plugin.excel.annotation.ResponseExcel;\nimport com.pig4cloud.plugin.excel.annotation.RequestExcel;\n#else\nimport ${package}.common.excel.annotation.ResponseExcel;\nimport ${package}.common.excel.annotation.RequestExcel;\n#end\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport ${package}.${moduleName}.service.${ClassName}Service;\n\nimport io.swagger.v3.oas.annotations.security.SecurityRequirement;\n#if($isSpringBoot3)\nimport ${package}.common.security.annotation.HasPermission;\nimport org.springdoc.core.annotations.ParameterObject;\n#else\nimport org.springframework.security.access.prepost.PreAuthorize;\nimport org.springdoc.api.annotations.ParameterObject;\n#end\nimport org.springframework.http.HttpHeaders;\nimport io.swagger.v3.oas.annotations.tags.Tag;\nimport io.swagger.v3.oas.annotations.Operation;\nimport lombok.RequiredArgsConstructor;\nimport org.springframework.validation.BindingResult;\nimport org.springframework.web.bind.annotation.*;\n\nimport java.util.List;\nimport java.util.Objects;\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@RestController\n@RequiredArgsConstructor\n@RequestMapping("/${functionName}" )\n@Tag(description = "${functionName}" , name = "${tableComment}管理" )\n@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)\npublic class ${ClassName}Controller {\n\n    private final  ${ClassName}Service ${className}Service;\n\n    /**\n     * 分页查询\n     * @param page 分页对象\n     * @param ${className} ${tableComment}\n     * @return\n     */\n    @Operation(summary = "分页查询" , description = "分页查询" )\n    @GetMapping("/page" )\n    #if($isSpringBoot3)\n    @HasPermission("${moduleName}_${functionName}_view")\n    #else\n    @PreAuthorize("@pms.hasPermission(\\'${moduleName}_${functionName}_view\\')" )\n    #end\n    public R get${ClassName}Page(@ParameterObject Page page, @ParameterObject ${ClassName}Entity ${className}) {\n        LambdaQueryWrapper<${ClassName}Entity> wrapper = Wrappers.lambdaQuery();\n#foreach ($field in $queryList)\n#set($getAttrName=$str.getProperty($field.attrName))\n#set($var="${className}.$getAttrName()")\n#if($field.attrType == \\'String\\')\n#set($expression="StrUtil.isNotBlank")\n#else\n#set($expression="Objects.nonNull")\n#end\n#if($field.queryType == \\'=\\')\n\t\twrapper.eq($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \\'like\\' )\n\t\twrapper.like($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \\'!-\\' )\n\t\twrapper.ne($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \\'>\\' )\n\t\twrapper.gt($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \\'<\\' )\n\t\twrapper.lt($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \\'>=\\' )\n\t\twrapper.ge($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \\'<=\\' )\n\t\twrapper.le($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \\'left like\\' )\n\t\twrapper.likeLeft($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \\'right like\\' )\n\t\twrapper.likeRight($expression($var),${ClassName}Entity::$getAttrName,$var);\n#end\n#end\n        return R.ok(${className}Service.page(page, wrapper));\n    }\n\n\n    /**\n     * 通过条件查询${tableComment}\n     * @param ${className} 查询条件\n     * @return R  对象列表\n     */\n    @Operation(summary = "通过条件查询" , description = "通过条件查询对象" )\n    @GetMapping("/details" )\n    #if($isSpringBoot3)\n    @HasPermission("${moduleName}_${functionName}_view")\n    #else\n    @PreAuthorize("@pms.hasPermission(\\'${moduleName}_${functionName}_view\\')" )\n    #end\n    public R getDetails(@ParameterObject ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.list(Wrappers.query(${className})));\n    }\n\n    /**\n     * 新增${tableComment}\n     * @param ${className} ${tableComment}\n     * @return R\n     */\n    @Operation(summary = "新增${tableComment}" , description = "新增${tableComment}" )\n    @SysLog("新增${tableComment}" )\n    @PostMapping\n    #if($isSpringBoot3)\n    @HasPermission("${moduleName}_${functionName}_add")\n    #else\n    @PreAuthorize("@pms.hasPermission(\\'${moduleName}_${functionName}_add\\')" )\n    #end\n    public R save(@RequestBody ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.save(${className}));\n    }\n\n    /**\n     * 修改${tableComment}\n     * @param ${className} ${tableComment}\n     * @return R\n     */\n    @Operation(summary = "修改${tableComment}" , description = "修改${tableComment}" )\n    @SysLog("修改${tableComment}" )\n    @PutMapping\n    #if($isSpringBoot3)\n    @HasPermission("${moduleName}_${functionName}_edit")\n    #else\n    @PreAuthorize("@pms.hasPermission(\\'${moduleName}_${functionName}_edit\\')" )\n    #end\n    public R updateById(@RequestBody ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.updateById(${className}));\n    }\n\n    /**\n     * 通过id删除${tableComment}\n     * @param ids ${pk.attrName}列表\n     * @return R\n     */\n    @Operation(summary = "通过id删除${tableComment}" , description = "通过id删除${tableComment}" )\n    @SysLog("通过id删除${tableComment}" )\n    @DeleteMapping\n    #if($isSpringBoot3)\n    @HasPermission("${moduleName}_${functionName}_del")\n    #else\n    @PreAuthorize("@pms.hasPermission(\\'${moduleName}_${functionName}_del\\')" )\n    #end\n    public R removeById(@RequestBody ${pk.attrType}[] ids) {\n        return R.ok(${className}Service.removeBatchByIds(CollUtil.toList(ids)));\n    }\n\n\n    /**\n     * 导出excel 表格\n     * @param ${className} 查询条件\n   \t * @param ids 导出指定ID\n     * @return excel 文件流\n     */\n    @ResponseExcel\n    @GetMapping("/export")\n    #if($isSpringBoot3)\n    @HasPermission("${moduleName}_${functionName}_export")\n    #else\n    @PreAuthorize("@pms.hasPermission(\\'${moduleName}_${functionName}_export\\')" )\n    #end\n    public List<${ClassName}Entity> exportExcel(${ClassName}Entity ${className},${pk.attrType}[] ids) {\n        return ${className}Service.list(Wrappers.lambdaQuery(${className}).in(ArrayUtil.isNotEmpty(ids), ${ClassName}Entity::$str.getProperty($pk.attrName), ids));\n    }\n\n    /**\n     * 导入excel 表\n     * @param ${className}List 对象实体列表\n     * @param bindingResult 错误信息列表\n     * @return ok fail\n     */\n    @PostMapping("/import")\n    #if($isSpringBoot3)\n    @HasPermission("${moduleName}_${functionName}_export")\n    #else\n    @PreAuthorize("@pms.hasPermission(\\'${moduleName}_${functionName}_export\\')" )\n    #end\n    public R importExcel(@RequestExcel List<${ClassName}Entity> ${className}List, BindingResult bindingResult) {\n        return R.ok(${className}Service.saveBatch(${className}List));\n    }\n}\n', now(), '0');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `del_flag`) VALUES (2, 'Service', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/${ClassName}Service.java', 'Service', 'package ${package}.${moduleName}.service;\n\nimport com.baomidou.mybatisplus.extension.service.IService;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\n\npublic interface ${ClassName}Service extends IService<${ClassName}Entity> {\n\n}\n', now(), '0');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `del_flag`) VALUES (3, 'ServiceImpl', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/impl/${ClassName}ServiceImpl.java', 'ServiceImpl', 'package ${package}.${moduleName}.service.impl;\n\nimport com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport ${package}.${moduleName}.mapper.${ClassName}Mapper;\nimport ${package}.${moduleName}.service.${ClassName}Service;\nimport org.springframework.stereotype.Service;\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@Service\npublic class ${ClassName}ServiceImpl extends ServiceImpl<${ClassName}Mapper, ${ClassName}Entity> implements ${ClassName}Service {\n\n}\n', now(), '0');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `del_flag`) VALUES (4, '实体', '${backendPath}/src/main/java/${packagePath}/${moduleName}/entity/${ClassName}Entity.java', '实体', 'package ${package}.${moduleName}.entity;\n\nimport com.baomidou.mybatisplus.annotation.*;\nimport com.baomidou.mybatisplus.extension.activerecord.Model;\nimport io.swagger.v3.oas.annotations.media.Schema;\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\n#if($isTenant)\nimport ${package}.common.core.util.TenantTable;\n#end\n#foreach($import in $importList)\nimport $import;\n#end\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@Data\n#if($isTenant)\n@TenantTable\n#end\n@TableName("${tableName}")\n@EqualsAndHashCode(callSuper = true)\n@Schema(description = "${tableComment}")\npublic class ${ClassName}Entity extends Model<${ClassName}Entity> {\n\n#foreach ($field in $fieldList)\n#if(${field.fieldComment})#set($comment=${field.fieldComment})#else #set($comment=${field.attrName})#end\n\n\t/**\n\t* $comment\n\t*/\n#if($field.primaryPk == \\'1\\')\n    @TableId(type = IdType.ASSIGN_ID)\n#end\n#if($field.autoFill == \\'INSERT\\')\n\t@TableField(fill = FieldFill.INSERT)\n#elseif($field.autoFill == \\'INSERT_UPDATE\\')\n\t@TableField(fill = FieldFill.INSERT_UPDATE)\n#elseif($field.autoFill == \\'UPDATE\\')\n\t@TableField(fill = FieldFill.UPDATE)\n#end\n#if($field.fieldName == \\'del_flag\\')\n    @TableLogic\n\t@TableField(fill = FieldFill.INSERT)\n#end\n    @Schema(description="$comment"#if($field.hidden),hidden=$field.hidden#end)\n#if($field.formType == \\'checkbox\\')\n    private ${field.attrType}[] $field.attrName;\n#else\n    private $field.attrType $field.attrName;\n#end    \n#end\n}\n', now(), '0');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `del_flag`) VALUES (5, 'Mapper', '${backendPath}/src/main/java/${packagePath}/${moduleName}/mapper/${ClassName}Mapper.java', 'Mapper', 'package ${package}.${moduleName}.mapper;\n\n\nimport com.baomidou.mybatisplus.core.mapper.BaseMapper;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport org.apache.ibatis.annotations.Mapper;\n\n@Mapper\npublic interface ${ClassName}Mapper extends BaseMapper<${ClassName}Entity> {\n\n}\n', now(), '0');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `del_flag`) VALUES (6, 'Mapper.xml', '${backendPath}/src/main/resources/mapper/${ClassName}Mapper.xml', 'Mapper.xml', '<?xml version="1.0" encoding="UTF-8"?>\n<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">\n\n<mapper namespace="${package}.${moduleName}.mapper.${ClassName}Mapper">\n\n  <resultMap id="${className}Map" type="${package}.${moduleName}.entity.${ClassName}Entity">\n#foreach ($field in $fieldList)\n\t    #if($field.primaryPk == \\'1\\')\n        <id property="$field.attrName" column="$field.fieldName"/>\n      #else\n        <result property="$field.attrName" column="$field.fieldName"/>\n      #end\n#end\n  </resultMap>\n</mapper>\n', now(), '0');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `del_flag`) VALUES (7, '权限菜单', '${backendPath}/menu/${functionName}_menu.sql', '权限菜单', '-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 , #if(!$opensource)默认租户 1#end\n#set($menuId=${dateTool.getSystemTime()})\n\n-- 菜单SQL\ninsert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${menuId}, \\'-1\\', \\'/${moduleName}/${functionName}/index\\', \\'\\', \\'0\\', \\'icon-bangzhushouji\\', \\'0\\', null , \\'8\\', null , \\'${tableComment}管理\\'#if(!$opensource), 1#end);\n\n-- 菜单对应按钮SQL\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,1)},${menuId}, \\'${moduleName}_${functionName}_view\\', \\'1\\', null, \\'1\\',  \\'0\\', null, \\'0\\', null, \\'${tableComment}查看\\'#if(!$opensource), 1#end);\n\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,2)},${menuId}, \\'${moduleName}_${functionName}_add\\', \\'1\\', null, \\'1\\',  \\'0\\', null, \\'1\\', null, \\'${tableComment}新增\\'#if(!$opensource), 1#end);\n\ninsert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,3)},${menuId}, \\'${moduleName}_${functionName}_edit\\', \\'1\\', null, \\'1\\',  \\'0\\', null, \\'2\\', null, \\'${tableComment}修改\\'#if(!$opensource), 1#end);\n\ninsert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,4)},${menuId}, \\'${moduleName}_${functionName}_del\\', \\'1\\', null, \\'1\\',  \\'0\\', null, \\'3\\', null, \\'${tableComment}删除\\'#if(!$opensource), 1#end);\n\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,5)},${menuId}, \\'${moduleName}_${functionName}_export\\', \\'1\\', null, \\'1\\',  \\'0\\', null, \\'3\\', null, \\'导入导出\\'#if(!$opensource), 1#end);', now(), '0');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `del_flag`) VALUES (8, 'api.ts', '${frontendPath}/src/api/${moduleName}/${functionName}.ts', 'api.ts', 'import request from "/@/utils/request"\n\n// ========== 基础CRUD接口 ==========\n\n/**\n * 分页查询列表数据\n * @param query - 查询参数对象\n * @returns Promise<分页数据>\n */\nexport function fetchList(query?: Object) {\n  return request({\n    url: \\'/${moduleName}/${functionName}/page\\',\n    method: \\'get\\',\n    params: query\n  })\n}\n\n/**\n * 新增数据\n * @param obj - 要新增的数据对象\n * @returns Promise<boolean> - 操作结果\n */\nexport function addObj(obj?: Object) {\n  return request({\n    url: \\'/${moduleName}/${functionName}\\',\n    method: \\'post\\',\n    data: obj\n  })\n}\n\n/**\n * 获取详情数据\n * @param obj - 查询参数对象（包含ID等）\n * @returns Promise<数据详情>\n */\nexport function getObj(obj?: Object) {\n  return request({\n    url: \\'/${moduleName}/${functionName}/details\\',\n    method: \\'get\\',\n    params: obj\n  })\n}\n\n/**\n * 批量删除数据\n * @param ids - 要删除的ID数组\n * @returns Promise<操作结果>\n */\nexport function delObjs(ids?: Object) {\n  return request({\n    url: \\'/${moduleName}/${functionName}\\',\n    method: \\'delete\\',\n    data: ids\n  })\n}\n\n/**\n * 更新数据\n * @param obj - 要更新的数据对象\n * @returns Promise<操作结果>\n */\nexport function putObj(obj?: Object) {\n  return request({\n    url: \\'/${moduleName}/${functionName}\\',\n    method: \\'put\\',\n    data: obj\n  })\n}\n\n// ========== 工具函数 ==========\n\n/**\n * 验证字段值唯一性\n * @param rule - 验证规则对象\n * @param value - 要验证的值\n * @param callback - 验证回调函数\n * @param isEdit - 是否为编辑模式\n * \n * @example\n * // 在表单验证规则中使用\n * fieldName: [\n *   {\n *     validator: (rule, value, callback) => {\n *       validateExist(rule, value, callback, form.${pk.attrName} !== \\'\\');\n *     },\n *     trigger: \\'blur\\',\n *   },\n * ]\n */\nexport function validateExist(rule: any, value: any, callback: any, isEdit: boolean) {\n  // 编辑模式下跳过验证\n  if (isEdit) {\n    return callback();\n  }\n\n  // 查询是否存在相同值\n  getObj({ [rule.field]: value }).then((response) => {\n    const result = response.data;\n    if (result !== null && result.length > 0) {\n      callback(new Error(\\'数据已经存在\\'));\n    } else {\n      callback();\n    }\n  });\n}\n\n#if($ChildClassName)\n// ========== 子表相关接口 ==========\n\n/**\n * 删除子表数据\n * @param ids - 要删除的子表数据ID数组\n * @returns Promise<操作结果>\n */\nexport function delChildObj(ids?: Object) {\n  return request({\n    url: \\'/${moduleName}/${functionName}/child\\',\n    method: \\'delete\\',\n    data: ids\n  })\n}\n#end\n', now(), '0');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `del_flag`) VALUES (9, '表格', '${frontendPath}/src/views/${moduleName}/${functionName}/index.vue', '表格', '<template>\n  <div class="layout-padding">\n    <div class="layout-padding-auto layout-padding-view">\n#if($queryList)\n      <!-- 查询表单区域 -->\n      <el-row v-show="showSearch">\n        <el-form :model="state.queryForm" ref="queryRef" :inline="true" @keyup.enter="getDataList">\n#foreach($field in $queryList)\n#if($field.queryFormType == \\'select\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-select v-model="state.queryForm.${field.attrName}" placeholder="请选择#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end">\n#if($field.fieldDict)\n              <el-option \n                :label="item.label" \n                :value="item.value" \n                v-for="(item, index) in ${field.fieldDict}" \n                :key="index"\n              />\n#else\n              <el-option label="请选择" value="0" />\n#end\n            </el-select>\n          </el-form-item>\n#elseif($field.queryFormType == \\'date\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-date-picker \n              type="date" \n              placeholder="请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" \n              v-model="state.queryForm.${field.attrName}"\n              :value-format="dateStr"\n            />\n          </el-form-item>\n#elseif($field.queryFormType == \\'datetime\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-date-picker \n              type="datetime" \n              placeholder="请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" \n              v-model="state.queryForm.${field.attrName}"\n              :value-format="dateTimeStr"\n            />\n          </el-form-item>\n#elseif($field.formType == \\'radio\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-radio-group v-model="state.queryForm.${field.attrName}">\n#if($field.fieldDict)\n              <el-radio \n                :label="item.value" \n                v-for="(item, index) in ${field.fieldDict}" \n                border \n                :key="index"\n              >\n                {{ item.label }}\n              </el-radio>\n#else\n              <el-radio label="${field.fieldComment}" border>\n                ${field.fieldComment}\n              </el-radio>\n#end\n            </el-radio-group>\n          </el-form-item>\n#else\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-input \n              placeholder="请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" \n              v-model="state.queryForm.${field.attrName}" \n            />\n          </el-form-item>\n#end\n#end\n          <el-form-item>\n            <el-button icon="search" type="primary" @click="getDataList">\n              查询\n            </el-button>\n            <el-button icon="Refresh" @click="resetQuery">重置</el-button>\n          </el-form-item>\n        </el-form>\n      </el-row>\n#end\n\n      <!-- 操作按钮区域 -->\n      <el-row>\n        <div class="mb8" style="width: 100%">\n          <el-button \n            icon="folder-add" \n            type="primary" \n            class="ml10" \n            @click="formDialogRef.openDialog()"\n            v-auth="\\'${moduleName}_${functionName}_add\\'"\n          >\n            新增\n          </el-button>\n          <el-button \n            plain \n            icon="upload-filled" \n            type="primary" \n            class="ml10" \n            @click="excelUploadRef.show()" \n            v-auth="\\'${moduleName}_${functionName}_add\\'"\n          >\n            导入\n          </el-button>\n          <el-button \n            plain \n            :disabled="multiple" \n            icon="Delete" \n            type="primary"\n            v-auth="\\'${moduleName}_${functionName}_del\\'" \n            @click="handleDelete(selectObjs.value)"\n          >\n            删除\n          </el-button>\n          <right-toolbar \n            v-model:showSearch="showSearch" \n            :export="\\'${moduleName}_${functionName}_export\\'"\n            @exportExcel="exportExcel" \n            class="ml10 mr20" \n            style="float: right;"\n            @queryTable="getDataList"\n          />\n        </div>\n      </el-row>\n\n      <!-- 数据表格区域 -->\n      <el-table \n        :data="state.dataList" \n        v-loading="state.loading" \n        border \n        :cell-style="tableStyle.cellStyle" \n        :header-cell-style="tableStyle.headerCellStyle"\n        @selection-change="selectionChangHandle"\n        @sort-change="sortChangeHandle"\n      >\n        <el-table-column type="selection" width="40" align="center" />\n        <el-table-column type="index" label="#" width="40" />\n#foreach($field in $gridList)\n#if($field.fieldDict)\n        <el-table-column prop="${field.attrName}" label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" show-overflow-tooltip>\n          <template #default="scope">\n            <dict-tag :options="$field.fieldDict" :value="scope.row.${field.attrName}" />\n          </template>\n        </el-table-column>\n#else\n        <el-table-column \n          prop="${field.attrName}" \n          label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" \n#if(${field.gridSort} == \\'1\\')\n          sortable="custom" \n#end\n          show-overflow-tooltip\n        />\n#end\n#end\n        <el-table-column label="操作" width="150">\n          <template #default="scope">\n            <el-button \n              icon="edit-pen" \n              text \n              type="primary" \n              v-auth="\\'${moduleName}_${functionName}_edit\\'"\n              @click="formDialogRef.openDialog(scope.row.${pk.attrName})"\n            >\n              编辑\n            </el-button>\n            <el-button \n              icon="delete" \n              text \n              type="primary" \n              v-auth="\\'${moduleName}_${functionName}_del\\'" \n              @click="handleDelete([scope.row.${pk.attrName}])"\n            >\n              删除\n            </el-button>\n          </template>\n        </el-table-column>\n      </el-table>\n\n      <!-- 分页组件 -->\n      <pagination \n        @size-change="sizeChangeHandle" \n        @current-change="currentChangeHandle" \n        v-bind="state.pagination" \n      />\n    </div>\n\n    <!-- 编辑、新增弹窗 -->\n    <form-dialog ref="formDialogRef" @refresh="getDataList(false)" />\n\n    <!-- 导入excel弹窗 (需要在 upms-biz/resources/file 下维护模板) -->\n    <upload-excel\n      ref="excelUploadRef"\n      title="导入"\n      url="/${moduleName}/${functionName}/import"\n      temp-url="/admin/sys-file/local/file/${functionName}.xlsx"\n      @refreshDataList="getDataList"\n    />\n  </div>\n</template>\n\n<script setup lang="ts" name="system${ClassName}">\n// ========== 导入声明 ==========\nimport { BasicTableProps, useTable } from "/@/hooks/table";\nimport { fetchList, delObjs } from "/@/api/${moduleName}/${functionName}";\nimport { useMessage, useMessageBox } from "/@/hooks/message";\nimport { useDict } from \\'/@/hooks/dict\\';\n\n// ========== 组件声明 ==========\n// 异步加载表单弹窗组件\nconst FormDialog = defineAsyncComponent(() => import(\\'./form.vue\\'));\n\n// ========== 字典数据 ==========\n#set($fieldDict=[])\n#foreach($field in $queryList)\n#if($field.fieldDict)\n#set($void=$fieldDict.add($field.fieldDict))\n#end\n#end\n#foreach($field in $gridList)\n#if($field.fieldDict)\n#set($void=$fieldDict.add($field.fieldDict))\n#end\n#end\n#if($fieldDict)\n// 加载字典数据\nconst { $dict.format($fieldDict) } = useDict($dict.quotation($fieldDict));\n#end\n\n// ========== 组件引用 ==========\nconst formDialogRef = ref();          // 表单弹窗引用\nconst excelUploadRef = ref();         // Excel上传弹窗引用\nconst queryRef = ref();               // 查询表单引用\n\n// ========== 响应式数据 ==========\nconst showSearch = ref(true);         // 是否显示搜索区域\nconst selectObjs = ref([]) as any;    // 表格多选数据\nconst multiple = ref(true);           // 是否多选\n\n// ========== 表格状态 ==========\nconst state: BasicTableProps = reactive<BasicTableProps>({\n  queryForm: {},    // 查询参数\n  pageList: fetchList // 分页查询方法\n});\n\n// ========== Hook引用 ==========\n// 表格相关Hook\nconst {\n  getDataList,\n  currentChangeHandle,\n  sizeChangeHandle,\n  sortChangeHandle,\n  downBlobFile,\n  tableStyle\n} = useTable(state);\n\n// ========== 方法定义 ==========\n/**\n * 重置查询条件\n */\nconst resetQuery = () => {\n  // 清空搜索条件\n  queryRef.value?.resetFields();\n  // 清空多选\n  selectObjs.value = [];\n  // 重新查询\n  getDataList();\n};\n\n/**\n * 导出Excel文件\n */\nconst exportExcel = () => {\n  downBlobFile(\n    \\'/${moduleName}/${functionName}/export\\',\n    { ...state.queryForm, ids: selectObjs.value },\n    \\'${functionName}.xlsx\\'\n  );\n};\n\n/**\n * 表格多选事件处理\n * @param objs 选中的数据行\n */\nconst selectionChangHandle = (objs: { $pk.attrName: string }[]) => {\n  selectObjs.value = objs.map(({ $pk.attrName }) => $pk.attrName);\n  multiple.value = !objs.length;\n};\n\n/**\n * 删除数据处理\n * @param ids 要删除的数据ID数组\n */\nconst handleDelete = async (ids: string[]) => {\n  try {\n    await useMessageBox().confirm(\\'此操作将永久删除\\');\n  } catch {\n    return;\n  }\n\n  try {\n    await delObjs(ids);\n    getDataList();\n    useMessage().success(\\'删除成功\\');\n  } catch (err: any) {\n    useMessage().error(err.msg);\n  }\n};\n</script>\n', now(), '0');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `del_flag`) VALUES (10, '表单', '${frontendPath}/src/views/${moduleName}/${functionName}/form.vue', '表单', '<template>\n  <el-dialog :title="form.${pk.attrName} ? \\'编辑\\' : \\'新增\\'" v-model="visible"\n    :close-on-click-modal="false" draggable>\n    <el-form ref="dataFormRef" :model="form" :rules="dataRules" formDialogRef label-width="90px" v-loading="loading">\n      <el-row :gutter="24">\n#foreach($field in $formList)\n#if($field.attrName != ${pk.attrName})\n#if($formLayout == 1)\n        <el-col :span="24" class="mb20">\n#elseif($formLayout == 2)\n        <el-col :span="12" class="mb20">\n#end\n#if($field.formType == \\'text\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-input v-model="form.${field.attrName}" placeholder="请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end"/>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \\'textarea\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-input type="textarea" v-model="form.${field.attrName}" placeholder="请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end"/>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \\'select\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-select v-model="form.${field.attrName}" placeholder="请选择#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end">\n#if($field.fieldDict)\n              <el-option :value="item.value" :label="item.label" v-for="(item, index) in ${field.fieldDict}" :key="index"></el-option>\n#else\n              <el-option label="请选择" value="0"></el-option>\n#end\n            </el-select>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \\'radio\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-radio-group v-model="form.${field.attrName}">\n#if($field.fieldDict)\n              <el-radio :label="item.value" v-for="(item, index) in ${field.fieldDict}" border :key="index">{{ item.label }}</el-radio>\n#else\n              <el-radio label="${field.fieldComment}" border>${field.fieldComment}</el-radio>\n#end\n            </el-radio-group>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \\'checkbox\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-checkbox-group v-model="form.${field.attrName}">\n#if($field.fieldDict)\n              <el-checkbox :label="item.value" v-for="(item, index) in ${field.fieldDict}" :key="index">{{ item.label }}</el-checkbox>\n#else\n              <el-checkbox label="启用" name="type"></el-checkbox>\n              <el-checkbox label="禁用" name="type"></el-checkbox>\n#end\n            </el-checkbox-group>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \\'date\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-date-picker type="date" placeholder="请选择#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" v-model="form.${field.attrName}" :value-format="dateStr"></el-date-picker>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \\'datetime\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-date-picker type="datetime" placeholder="请选择#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" v-model="form.${field.attrName}" :value-format="dateTimeStr"></el-date-picker>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \\'number\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-input-number :min="1" :max="1000" v-model="form.${field.attrName}" placeholder="请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end"></el-input-number>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \\'upload-file\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <upload-file v-model="form.${field.attrName}"></upload-file>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \\'upload-img\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <upload-img v-model:imageUrl="form.${field.attrName}"></upload-img>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \\'editor\\')\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <editor v-if="visible" v-model:get-html="form.${field.attrName}" placeholder="请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end"></editor>\n          </el-form-item>\n        </el-col>\n#else\n          <el-form-item label="#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end" prop="${field.attrName}">\n            <el-input v-model="form.${field.attrName}" placeholder="请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end"/>\n          </el-form-item>\n        </el-col>\n#end\n#end\n#end\n      </el-row>\n    </el-form>\n    <template #footer>\n      <span class="dialog-footer">\n        <el-button @click="visible = false">取 消</el-button>\n        <el-button type="primary" @click="onSubmit" :disabled="loading">确 认</el-button>\n      </span>\n    </template>\n  </el-dialog>\n</template>\n\n<script setup lang="ts" name="${ClassName}Dialog">\n// ========== 1. 导入语句 ==========\nimport { useDict } from \\'/@/hooks/dict\\';\nimport { rule } from \\'/@/utils/validate\\';\nimport { useMessage } from "/@/hooks/message";\nimport { getObj, addObj, putObj, validateExist } from \\'/@/api/${moduleName}/${functionName}\\';\n\n// ========== 2. 组件定义 ==========\n// 定义组件事件\nconst emit = defineEmits([\\'refresh\\']);\n\n// ========== 3. 响应式数据定义 ==========\n// 基础响应式变量\nconst dataFormRef = ref(); // 表单引用\nconst visible = ref(false); // 弹窗显示状态\nconst loading = ref(false); // 加载状态\n\n// 表单数据对象\nconst form = reactive({\n#if(!$formList.contains(${pk.attrName}))\n  ${pk.attrName}: \\'\\', // 主键\n#end\n#foreach($field in $formList)\n#if($field.formType == \\'number\\')\n  ${field.attrName}: 0, // ${field.fieldComment}\n#elseif($field.formType == \\'checkbox\\')\n  ${field.attrName}: [], // ${field.fieldComment}\n#else\n  ${field.attrName}: \\'\\', // ${field.fieldComment}\n#end\n#end\n});\n\n// ========== 4. 字典数据处理 ==========\n#set($fieldDict=[])\n#foreach($field in $gridList)\n#if($field.fieldDict)\n#set($void=$fieldDict.add($field.fieldDict))\n#end\n#end\n#if($fieldDict && $fieldDict.size() > 0)\n// 加载字典数据\nconst { $dict.format($fieldDict) } = useDict($dict.quotation($fieldDict));\n#end\n\n// ========== 5. 表单校验规则 ==========\nconst dataRules = ref({\n#foreach($field in $formList)\n#if($field.formRequired == \\'1\\' && $field.formValidator == \\'duplicate\\')\n  ${field.attrName}: [\n    { required: true, message: \\'${field.fieldComment}不能为空\\', trigger: \\'blur\\' },\n    {\n      validator: (rule: any, value: any, callback: any) => {\n        // 重复性校验（编辑时跳过）\n        validateExist(rule, value, callback, form.${pk.attrName} !== \\'\\');\n      },\n      trigger: \\'blur\\',\n    }\n  ],\n#elseif($field.formRequired == \\'1\\' && $field.formValidator)\n  ${field.attrName}: [\n    { required: true, message: \\'${field.fieldComment}不能为空\\', trigger: \\'blur\\' },\n    { validator: rule.${field.formValidator}, trigger: \\'blur\\' }\n  ],\n#elseif($field.formRequired == \\'1\\')\n  ${field.attrName}: [\n    { required: true, message: \\'${field.fieldComment}不能为空\\', trigger: \\'blur\\' }\n  ],\n#elseif($field.formValidator)\n  ${field.attrName}: [\n    { validator: rule.${field.formValidator}, trigger: \\'blur\\' }\n  ],\n#end\n#end\n});\n\n// ========== 6. 方法定义 ==========\n// 获取详情数据\nconst get${ClassName}Data = async (id: string) => {\n  try {\n    loading.value = true;\n    const { data } = await getObj({ ${pk.attrName}: id });\n    // 直接将第一条数据赋值给表单\n    Object.assign(form, data[0]);\n  } catch (error) {\n    useMessage().error(\\'获取数据失败\\');\n  } finally {\n    loading.value = false;\n  }\n};\n\n// 打开弹窗方法\nconst openDialog = (id: string) => {\n  visible.value = true;\n  form.${pk.attrName} = \\'\\';\n\n  // 重置表单数据\n  nextTick(() => {\n    dataFormRef.value?.resetFields();\n  });\n\n  // 获取${ClassName}信息\n  if (id) {\n    form.${pk.attrName} = id;\n    get${ClassName}Data(id);\n  }\n};\n\n// 提交表单方法\nconst onSubmit = async () => {\n  loading.value = true; // 防止重复提交\n  \n  // 表单校验\n  const valid = await dataFormRef.value.validate().catch(() => {});\n  if (!valid) {\n    loading.value = false;\n    return false;\n  }\n\n  try {\n    // 根据是否有ID判断是新增还是修改\n    form.${pk.attrName} ? await putObj(form) : await addObj(form);\n    useMessage().success(form.${pk.attrName} ? \\'修改成功\\' : \\'添加成功\\');\n    visible.value = false;\n    emit(\\'refresh\\'); // 通知父组件刷新列表\n  } catch (err: any) {\n    useMessage().error(err.msg);\n  } finally {\n    loading.value = false;\n  }\n};\n\n// ========== 7. 对外暴露 ==========\n// 暴露方法给父组件\ndefineExpose({\n  openDialog\n});\n</script> ', now(), '0');

-- ----------------------------
-- Records of gen_template_group
-- ----------------------------
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 1);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 2);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 3);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 4);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 5);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 6);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 7);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 8);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 9);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 10);

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

-- ----------------------------
-- Clarity 监控数据缓存表
-- ----------------------------
DROP TABLE IF EXISTS `sys_clarity_data`;
CREATE TABLE `sys_clarity_data` (
  `id`                bigint(20)     NOT NULL                        COMMENT '主键（ASSIGN_ID）',
  `data_date`         date           NOT NULL                        COMMENT '数据日期',
  `total_sessions`    int(11)        DEFAULT NULL                    COMMENT '总会话数',
  `distinct_users`    int(11)        DEFAULT NULL                    COMMENT '独立访客数 UV',
  `pages_per_session` decimal(10,2)  DEFAULT NULL                    COMMENT '每会话页面数',
  `scroll_depth`      decimal(10,2)  DEFAULT NULL                    COMMENT '平均滚动深度 %',
  `dead_click_rate`   decimal(10,2)  DEFAULT NULL                    COMMENT '死点击率 %',
  `rage_click_rate`   decimal(10,2)  DEFAULT NULL                    COMMENT '激怒点击率 %',
  `device_data`       text           DEFAULT NULL                    COMMENT '设备分布 JSON',
  `top_urls`          text           DEFAULT NULL                    COMMENT '热门页面 Top10 JSON',
  `referrer_url_data` text           DEFAULT NULL                    COMMENT '来源页面 Top5 JSON',
  `page_title_data`   text           DEFAULT NULL                    COMMENT '页面标题 Top5 JSON',
  `browser_data`      text           DEFAULT NULL                    COMMENT '浏览器分布 JSON',
  `num_of_days`       int(11)        DEFAULT NULL                    COMMENT 'numOfDays 参数：1=24h,2=48h,3=72h',
  `fetch_status`      varchar(10)    NOT NULL DEFAULT 'pending'      COMMENT '拉取状态：pending/success/failed',
  `create_by`         varchar(64)    DEFAULT NULL                    COMMENT '创建人',
  `update_by`         varchar(64)    DEFAULT NULL                    COMMENT '修改人',
  `del_flag`          char(1)        DEFAULT '0'                     COMMENT '删除标记',
  `create_time`       datetime       DEFAULT NULL                    COMMENT '创建时间',
  `update_time`       datetime       DEFAULT NULL                    COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_date_days` (`data_date`, `num_of_days`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='Clarity 监控数据缓存表';

-- 菜单：站点统计
INSERT INTO `sys_menu` VALUES (4003,'站点统计',NULL,'/tools/data/clarity',NULL,9910,'iconfont icon-shuju','1',2,'0','0','0','admin','2026-03-26 00:00:00','admin','2026-03-26 00:00:00','0',1);
INSERT INTO `sys_role_menu` VALUES (1, 4003);
