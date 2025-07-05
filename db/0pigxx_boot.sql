-- pigx 单体模式的数据库【可选】
create database `pigxx_boot` default character set utf8mb4 collate utf8mb4_general_ci;

use pigxx_boot;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统配置';

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
  `city_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '城市编码',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
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
INSERT INTO `sys_dict` VALUES (28, 'message_type', '消息类型', 'admin', ' ', '2023-10-27 10:29:48', NULL, NULL, '1', '0', 1);
INSERT INTO `sys_dict` VALUES (29, 'sensitive_type', '敏感词类型', 'admin', ' ', '2023-10-27 10:29:48', NULL, NULL, '1', '0', 1);
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
INSERT INTO `sys_dict_item` VALUES (20, 7, '0', '未知', 'gender', '性别未知', 3, ' ', ' ', '2019-03-27 13:45:57', '2019-03-27 13:45:57', '性别未知', '0', 1);
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
INSERT INTO `sys_dict_item` VALUES (93, 28, '0', '公告', 'message_type', '主页公告显示', 0, 'admin', ' ', '2023-10-27 10:30:14', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (94, 28, '1', '站内信', 'message_type', '右上角显示', 1, 'admin', ' ', '2023-10-27 10:30:47', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (95, 29, '0', '黑名单', 'sensitive_type', '失败', 3, 'admin', ' ', '2023-10-27 10:30:47', NULL, NULL, '0', 1);
INSERT INTO `sys_dict_item` VALUES (96, 29, '1', '白名单', 'sensitive_type', '失败', 3, 'admin', ' ', '2023-10-27 10:30:47', NULL, NULL, '0', 1);
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
INSERT INTO `sys_i18n` VALUES (56, 'router.todoTask', '待办任务', 'Todo Task', 'admin', NULL, '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (57, 'router.tagManagement', '标签管理', 'Tag Management', 'admin', NULL, '', NULL, '0');
INSERT INTO `sys_i18n` VALUES (58, 'router.articleInformation', '文章资讯', 'Article Information', ' ', '2023-08-10 13:40:09', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (59, 'router.articleCategory', '文章分类', 'Article Category', ' ', '2023-08-10 13:40:48', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (60, 'router.interfaceSettings', '界面设置', 'Interface Settings', ' ', '2023-08-10 13:41:21', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (61, 'router.bottomNavigation', '底部导航', 'Bottom Navigation', ' ', '2023-08-10 13:41:54', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (62, 'router.cacheMonitoring', '缓存监控', 'Cache Monitoring', ' ', '2023-08-10 13:42:35', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (63, 'rotuer. initiateProcess', '发起流程', 'Initiate Process', ' ', '2023-08-10 13:44:23', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (64, 'router.taskManagement', '任务管理', 'Task Management', ' ', '2023-08-10 13:44:53', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (65, 'router.myInitiations', '我的发起', 'My Initiations', ' ', '2023-08-10 13:45:17', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (66, 'router.copiedtoMe', '抄送给我', 'Copied to Me', ' ', '2023-08-10 13:45:46', ' ', NULL, '0');
INSERT INTO `sys_i18n` VALUES (67, 'router.completedTasks', '我的已办', 'Completed Tasks', ' ', '2023-08-10 13:46:37', ' ', '2023-08-10 13:47:09', '0');
INSERT INTO `sys_i18n` VALUES (68, 'router.bizPlatform', '业务平台', 'Biz Platform', ' ', '2023-08-10 13:46:37', ' ', '2023-08-10 13:47:09', '0');
INSERT INTO `sys_i18n` VALUES (69, 'router.baseTools', '基础工具', 'Base Tools', ' ', '2023-08-10 13:46:37', ' ', '2023-08-10 13:47:09', '0');
INSERT INTO `sys_i18n` VALUES (70, 'router.route', '路由管理', 'Route Management', ' ', '2023-08-10 13:46:37', ' ', '2023-08-10 13:47:09', '0');
INSERT INTO `sys_i18n` VALUES (71, 'router.datav', '大屏看板', 'Data Visual', ' ', '2023-08-10 13:46:37', ' ', '2023-08-10 13:47:09', '0');
INSERT INTO `sys_i18n` VALUES (72, 'router.bi', '数据报表', 'Bi Report', ' ', '2023-08-10 13:46:37', ' ', '2023-08-10 13:47:09', '0');
INSERT INTO `sys_i18n` VALUES (73, 'router.message', '信息推送', 'Message', ' ', '2023-08-10 13:46:37', ' ', '2023-08-10 13:47:09', '0');
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
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remote_addr` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '远程地址',
  `user_agent` text COLLATE utf8mb4_general_ci COMMENT '浏览器标识',
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
-- Table structure for sys_sensitive_word
-- ----------------------------
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
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`sensitive_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='敏感词';


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
INSERT INTO `sys_menu` VALUES (1000,'权限管理',NULL,'/system',NULL,2000,'iconfont icon-quanxianguanli','1',0,'0','0','0','','2018-09-28 08:29:53','admin','2023-11-01 16:39:55','0',1);
INSERT INTO `sys_menu` VALUES (1100,'用户管理',NULL,'/admin/system/user/index',NULL,1000,'iconfont icon-yonghuguanli','1',1,'0','0','0','','2017-11-02 22:24:37','admin','2023-11-01 16:40:44','0',1);
INSERT INTO `sys_menu` VALUES (1101,'用户新增','sys_user_add',NULL,NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:52:09',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1102,'用户修改','sys_user_edit',NULL,NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:52:48',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1103,'用户删除','sys_user_del',NULL,NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:54:01',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1104,'导入导出','sys_user_export',NULL,NULL,1100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:54:01',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1200,'菜单管理',NULL,'/admin/system/menu/index',NULL,1000,'iconfont icon-caidan','1',2,'0','0','0','','2017-11-08 09:57:27','admin','2023-11-01 16:40:39','0',1);
INSERT INTO `sys_menu` VALUES (1201,'菜单新增','sys_menu_add',NULL,NULL,1200,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:15:53',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1202,'菜单修改','sys_menu_edit',NULL,NULL,1200,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:16:23',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1203,'菜单删除','sys_menu_del',NULL,NULL,1200,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:16:43',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1300,'角色管理',NULL,'/admin/system/role/index',NULL,1000,'iconfont icon-jiaoseguanli','1',3,'0',NULL,'0','','2017-11-08 10:13:37','admin','2025-01-26 22:24:11','0',1);
INSERT INTO `sys_menu` VALUES (1301,'角色新增','sys_role_add',NULL,NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:14:18',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1302,'角色修改','sys_role_edit',NULL,NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:14:41',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1303,'角色删除','sys_role_del',NULL,NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 10:14:59',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1304,'分配权限','sys_role_perm',NULL,NULL,1300,NULL,'1',1,'0',NULL,'1',' ','2018-04-20 07:22:55',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1305,'角色导入导出','sys_role_export',NULL,NULL,1300,NULL,'1',4,'0',NULL,'1',' ','2022-03-26 15:54:34',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (1400,'部门管理',NULL,'/admin/system/dept/index',NULL,1000,'iconfont icon-bumenguanli','1',4,'0',NULL,'0','','2018-01-20 13:17:19','admin','2023-11-01 16:40:30','0',1);
INSERT INTO `sys_menu` VALUES (1401,'部门新增','sys_dept_add',NULL,NULL,1400,NULL,'1',1,'0',NULL,'1',' ','2018-01-20 14:56:16',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1402,'部门修改','sys_dept_edit',NULL,NULL,1400,NULL,'1',1,'0',NULL,'1',' ','2018-01-20 14:56:59',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1403,'部门删除','sys_dept_del',NULL,NULL,1400,NULL,'1',1,'0',NULL,'1',' ','2018-01-20 14:57:28',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1404,'开放互联','sys_connect_sync',NULL,NULL,1400,NULL,'1',1,'0',NULL,'1',' ','2018-01-20 14:57:28',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (1500,'租户管理',NULL,'/admin/system/tenant/index',NULL,1000,'iconfont icon-tenant','1',9,'0','0','0','','2018-01-20 13:17:19','admin','2023-11-01 16:40:26','0',1);
INSERT INTO `sys_menu` VALUES (1501,'租户新增','sys_systenant_add',NULL,NULL,1500,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:56:52','0',1);
INSERT INTO `sys_menu` VALUES (1502,'租户修改','sys_systenant_edit',NULL,NULL,1500,'1','1',1,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:56:53','0',1);
INSERT INTO `sys_menu` VALUES (1503,'租户删除','sys_systenant_del',NULL,NULL,1500,'1','1',2,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:56:54','0',1);
INSERT INTO `sys_menu` VALUES (1504,'租户套餐','sys_systenant_tenantmenu',NULL,NULL,1500,'1','1',1,'0',NULL,'1','admin','2022-12-12 09:01:41',' ','2023-01-11 05:52:51','0',1);
INSERT INTO `sys_menu` VALUES (1505,'租户套餐删除','sys_systenantmenu_del',NULL,NULL,1500,'1','1',1,'0',NULL,'1','admin','2022-12-09 14:04:19','admin','2023-01-11 05:52:51','0',1);
INSERT INTO `sys_menu` VALUES (1506,'租户套餐编辑','sys_systenantmenu_edit',NULL,NULL,1500,'1','1',1,'0',NULL,'1','admin','2022-12-09 14:04:19','admin','2023-01-11 05:52:51','0',1);
INSERT INTO `sys_menu` VALUES (1507,'租户套餐新增','sys_systenantmenu_add',NULL,NULL,1500,'1','1',1,'0',NULL,'1','admin','2022-12-09 14:04:19','admin','2022-12-12 09:02:00','0',1);
INSERT INTO `sys_menu` VALUES (1508,'租户套餐导出','sys_systenant_export',NULL,NULL,1500,NULL,'1',0,'0','0','1','admin','2023-03-06 16:28:24',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (1600,'岗位管理',NULL,'/admin/system/post/index',NULL,1000,'iconfont icon-gangweitubiao','1',5,'1','0','0','','2022-03-26 13:04:14','admin','2023-11-01 17:02:55','0',1);
INSERT INTO `sys_menu` VALUES (1601,'岗位信息查看','sys_post_view',NULL,NULL,1600,NULL,'1',0,'0',NULL,'1',' ','2022-03-26 13:05:34',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (1602,'岗位信息新增','sys_post_add',NULL,NULL,1600,NULL,'1',1,'0',NULL,'1',' ','2022-03-26 13:06:00',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (1603,'岗位信息修改','sys_post_edit',NULL,NULL,1600,NULL,'1',2,'0',NULL,'1',' ','2022-03-26 13:06:31',' ','2022-03-26 13:06:38','0',1);
INSERT INTO `sys_menu` VALUES (1604,'岗位信息删除','sys_post_del',NULL,NULL,1600,NULL,'1',3,'0',NULL,'1',' ','2022-03-26 13:06:31',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (1605,'岗位导入导出','sys_post_export',NULL,NULL,1600,NULL,'1',4,'0',NULL,'1',' ','2022-03-26 13:06:31',' ','2022-03-26 06:32:02','0',1);
INSERT INTO `sys_menu` VALUES (2000,'系统管理',NULL,'/admin',NULL,-1,'iconfont icon-xitongguanli','1',1,'0',NULL,'0','','2017-11-07 20:56:00','admin','2023-11-01 16:25:58','0',1);
INSERT INTO `sys_menu` VALUES (2001,'日志管理',NULL,'/admin/logs',NULL,2000,'iconfont icon-xitongrizhi','1',1,'0','0','0','admin','2023-03-02 12:26:42','admin','2023-11-01 16:22:08','0',1);
INSERT INTO `sys_menu` VALUES (2100,'操作日志',NULL,'/admin/log/index',NULL,2001,'iconfont icon-jinridaiban','1',2,'0','0','0','','2017-11-20 14:06:22','admin','2023-03-02 12:28:57','0',1);
INSERT INTO `sys_menu` VALUES (2101,'日志删除','sys_log_del',NULL,NULL,2100,NULL,'1',1,'0',NULL,'1',' ','2017-11-20 20:37:37',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (2102,'导入导出','sys_log_export',NULL,NULL,2100,NULL,'1',1,'0',NULL,'1',' ','2017-11-08 09:54:01',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (2103,'审计日志',NULL,'/admin/audit/index',NULL,2001,'iconfont icon-biaodan','1',1,'0','0','0','',NULL,'admin','2023-03-02 12:28:47','0',1);
INSERT INTO `sys_menu` VALUES (2104,'审计记录表删除','sys_audit_del',NULL,NULL,2103,'1','1',3,'0',NULL,'1','',NULL,'admin','2023-02-28 20:23:43','0',1);
INSERT INTO `sys_menu` VALUES (2105,'导入导出','sys_audit_export',NULL,NULL,2103,'1','1',3,'0',NULL,'1','',NULL,'admin','2023-02-28 20:23:51','0',1);
INSERT INTO `sys_menu` VALUES (2106,'敏感数据查看','no_mask',NULL,NULL,2103,'1','1',3,'0',NULL,'1','',NULL,'admin','2023-02-28 20:23:51','0',1);
INSERT INTO `sys_menu` VALUES (2200,'字典管理',NULL,'/admin/dict/index',NULL,2000,'iconfont icon-zidianguanli','1',6,'0',NULL,'0','','2017-11-29 11:30:52','admin','2023-02-16 15:24:29','0',1);
INSERT INTO `sys_menu` VALUES (2201,'字典删除','sys_dict_del',NULL,NULL,2200,NULL,'1',1,'0',NULL,'1',' ','2017-11-29 11:30:11',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (2202,'字典新增','sys_dict_add',NULL,NULL,2200,NULL,'1',1,'0',NULL,'1',' ','2018-05-11 22:34:55',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (2203,'字典修改','sys_dict_edit',NULL,NULL,2200,NULL,'1',1,'0',NULL,'1',' ','2018-05-11 22:36:03',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (2210,'参数管理',NULL,'/admin/param/index',NULL,2000,'iconfont icon-canshuguanli','1',7,'1',NULL,'0','','2019-04-29 22:16:50','admin','2023-02-16 15:24:51','0',1);
INSERT INTO `sys_menu` VALUES (2211,'参数新增','sys_syspublicparam_add',NULL,NULL,2210,NULL,'1',1,'0',NULL,'1',' ','2019-04-29 22:17:36',' ','2020-03-24 08:57:11','0',1);
INSERT INTO `sys_menu` VALUES (2212,'参数删除','sys_syspublicparam_del',NULL,NULL,2210,NULL,'1',1,'0',NULL,'1',' ','2019-04-29 22:17:55',' ','2020-03-24 08:57:12','0',1);
INSERT INTO `sys_menu` VALUES (2213,'参数编辑','sys_syspublicparam_edit',NULL,NULL,2210,NULL,'1',1,'0',NULL,'1',' ','2019-04-29 22:18:14',' ','2020-03-24 08:57:13','0',1);
INSERT INTO `sys_menu` VALUES (2300,'代码生成',NULL,'/gen/table/index',NULL,9000,'iconfont icon-daimashengcheng','1',2,'0','0','0','','2018-01-20 13:17:19','admin','2023-02-20 13:54:35','0',1);
INSERT INTO `sys_menu` VALUES (2400,'终端管理',NULL,'/admin/client/index',NULL,2000,'iconfont icon-shouji','1',9,'1',NULL,'0','','2018-01-20 13:17:19','admin','2023-02-16 15:25:28','0',1);
INSERT INTO `sys_menu` VALUES (2401,'客户端新增','sys_client_add',NULL,NULL,2400,'1','1',1,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (2402,'客户端修改','sys_client_edit',NULL,NULL,2400,NULL,'1',1,'0',NULL,'1',' ','2018-05-15 21:37:06',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (2403,'客户端删除','sys_client_del',NULL,NULL,2400,NULL,'1',1,'0',NULL,'1',' ','2018-05-15 21:39:16',' ','2021-05-25 03:12:55','0',1);
INSERT INTO `sys_menu` VALUES (2500,'密钥管理',NULL,'/admin/social/index',NULL,2000,'iconfont icon-miyueguanli','1',10,'0',NULL,'0','','2018-01-20 13:17:19','admin','2025-01-26 22:25:20','0',1);
INSERT INTO `sys_menu` VALUES (2501,'密钥新增','sys_social_details_add',NULL,NULL,2500,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:19','0',1);
INSERT INTO `sys_menu` VALUES (2502,'密钥修改','sys_social_details_edit',NULL,NULL,2500,'1','1',1,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:19','0',1);
INSERT INTO `sys_menu` VALUES (2503,'密钥删除','sys_social_details_del',NULL,NULL,2500,'1','1',2,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:23','0',1);
INSERT INTO `sys_menu` VALUES (2600,'令牌管理',NULL,'/admin/token/index',NULL,2000,'iconfont icon-lingpai','1',11,'0',NULL,'0','','2018-09-04 05:58:41','admin','2023-02-16 15:28:28','0',1);
INSERT INTO `sys_menu` VALUES (2601,'令牌删除','sys_token_del',NULL,NULL,2600,NULL,'1',1,'0',NULL,'1',' ','2018-09-04 05:59:50',' ','2020-03-24 08:57:24','0',1);
INSERT INTO `sys_menu` VALUES (2800,'Quartz管理',NULL,'/tools/job-manage/index',NULL,9910,'iconfont icon-quartz','1',4,'0',NULL,'0','','2018-01-20 13:17:19','admin','2023-11-27 14:52:53','0',1);
INSERT INTO `sys_menu` VALUES (2810,'任务新增','job_sys_job_add',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:26','0',1);
INSERT INTO `sys_menu` VALUES (2820,'任务修改','job_sys_job_edit',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:27','0',1);
INSERT INTO `sys_menu` VALUES (2830,'任务删除','job_sys_job_del',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:28','0',1);
INSERT INTO `sys_menu` VALUES (2840,'任务暂停','job_sys_job_shutdown_job',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:28','0',1);
INSERT INTO `sys_menu` VALUES (2850,'任务开始','job_sys_job_start_job',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:29','0',1);
INSERT INTO `sys_menu` VALUES (2860,'任务刷新','job_sys_job_refresh_job',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2018-05-15 21:35:18',' ','2020-03-24 08:57:30','0',1);
INSERT INTO `sys_menu` VALUES (2870,'执行任务','job_sys_job_run_job',NULL,NULL,2800,'1','1',0,'0',NULL,'1',' ','2019-08-08 15:35:18',' ','2020-03-24 08:57:31','0',1);
INSERT INTO `sys_menu` VALUES (2871,'导出','job_sys_job_export',NULL,NULL,2800,NULL,'1',0,'0','0','1','admin','2023-03-06 15:26:13',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (2900,'国际化管理',NULL,'/admin/i18n/index',NULL,2000,'iconfont icon-zhongyingwenqiehuan','1',8,'0',NULL,'0','',NULL,'admin','2023-02-16 15:25:18','0',1);
INSERT INTO `sys_menu` VALUES (2901,'系统表-国际化查看','sys_i18n_view',NULL,NULL,2900,'1','1',0,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (2902,'系统表-国际化新增','sys_i18n_add',NULL,NULL,2900,'1','1',1,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (2903,'系统表-国际化修改','sys_i18n_edit',NULL,NULL,2900,'1','1',2,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (2904,'系统表-国际化删除','sys_i18n_del',NULL,NULL,2900,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (2905,'导入导出','sys_i18n_export',NULL,NULL,2900,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (2906,'文件管理',NULL,'/admin/file/index',NULL,2000,'iconfont icon-wendangguanli','1',6,'0',NULL,'0','','2019-06-25 12:44:46','admin','2023-02-16 15:24:42','0',1);
INSERT INTO `sys_menu` VALUES (2907,'删除文件','sys_file_del',NULL,NULL,2906,NULL,'1',1,'0',NULL,'1',' ','2019-06-25 13:41:41',' ','2020-03-24 08:58:42','0',1);
INSERT INTO `sys_menu` VALUES (2910,'行政区划','','/admin/sysArea/index',NULL,2000,'iconfont icon-hangzhengquhuaguanli','1',99,'0',NULL,'0','',NULL,'admin','2024-02-16 22:11:03','0',1);
INSERT INTO `sys_menu` VALUES (2911,'行政区划表查看','sys_sysArea_view',NULL,NULL,2910,'1','1',0,'0',NULL,'1',' ',NULL,' ','2024-02-17 14:31:09','0',1);
INSERT INTO `sys_menu` VALUES (2912,'行政区划表新增','sys_sysArea_add',NULL,NULL,2910,'1','1',1,'0',NULL,'1',' ',NULL,' ','2024-02-17 14:31:16','0',1);
INSERT INTO `sys_menu` VALUES (2913,'行政区划表删除','sys_sysArea_del',NULL,NULL,2910,'1','1',3,'0',NULL,'1',' ',NULL,' ','2024-02-17 14:31:21','0',1);
INSERT INTO `sys_menu` VALUES (2914,'导入导出','sys_sysArea_export',NULL,NULL,2910,'1','1',3,'0',NULL,'1',' ',NULL,' ','2024-02-17 14:31:26','0',1);
INSERT INTO `sys_menu` VALUES (2915,'行政区划表修改','sys_sysArea_edit',NULL,NULL,2910,'1','1',2,'0',NULL,'1',' ',NULL,' ','2024-02-17 14:31:31','0',1);
INSERT INTO `sys_menu` VALUES (2920,'敏感词管理','','/admin/sensitive/index',NULL,2000,'iconfont icon-sensitiveword','1',12,'0',NULL,'0','',NULL,'admin','2024-07-07 15:09:27','0',1);
INSERT INTO `sys_menu` VALUES (2921,'敏感词查看','admin_sysSensitiveWord_view',NULL,NULL,2920,'1','1',0,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (2922,'敏感词新增','admin_sysSensitiveWord_add',NULL,NULL,2920,'1','1',1,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (2923,'敏感词修改','admin_sysSensitiveWord_edit',NULL,NULL,2920,'1','1',2,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (2924,'敏感词删除','admin_sysSensitiveWord_del',NULL,NULL,2920,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (2925,'导入导出','admin_sysSensitiveWord_export',NULL,NULL,2920,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3000,'公众号平台',NULL,'/mp',NULL,9900,'iconfont icon-weixingongzhonghao','1',3,'0','0','0','admin','2023-02-24 10:40:44','admin','2023-11-27 14:52:28','0',1);
INSERT INTO `sys_menu` VALUES (3001,'账号管理',NULL,'/biz/mp/wx-account/index',NULL,3000,'iconfont icon-zhanghaoguanli','1',0,'0','0','0','admin','2023-02-24 10:43:03',' ','2023-11-01 17:28:07','0',1);
INSERT INTO `sys_menu` VALUES (3002,'菜单设置',NULL,'/biz/mp/wx-menu/index',NULL,3000,'iconfont icon-caidanshezhi','1',1,'0','0','0','admin','2023-02-24 11:16:32','admin','2023-11-01 17:28:11','0',1);
INSERT INTO `sys_menu` VALUES (3003,'删除','mp_wxaccount_del',NULL,NULL,3001,NULL,'0',0,'0','0','1','admin','2023-02-24 13:12:53',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3004,'新增','mp_wxaccount_add',NULL,NULL,3001,NULL,'0',0,'0','0','1','admin','2023-02-24 13:13:04',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3005,'编辑','mp_wxaccount_edit',NULL,NULL,3001,NULL,'0',0,'0','0','1','admin','2023-02-24 13:13:15',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3006,'粉丝管理',NULL,'/biz/mp/wx-account-fans/index',NULL,3000,'iconfont icon-fans','1',2,'0','0','0','admin','2023-02-24 13:28:24','admin','2023-11-01 17:28:15','0',1);
INSERT INTO `sys_menu` VALUES (3007,'同步粉丝','mp_wxaccountfans_sync',NULL,NULL,3006,NULL,'0',0,'0','0','1','admin','2023-02-24 14:03:03',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3008,'消息管理',NULL,'/biz/mp/wx-fans-msg/index',NULL,3000,'iconfont icon-xiaoxiguanli','1',6,'0','0','0','admin','2023-02-24 15:24:35','admin','2023-11-01 17:28:21','0',1);
INSERT INTO `sys_menu` VALUES (3009,'修改微信消息','mp_wxmsg_edit',NULL,NULL,3008,NULL,'1',0,'0','0','1','admin','2023-02-24 15:41:55','admin','2023-11-01 17:28:28','0',1);
INSERT INTO `sys_menu` VALUES (3010,'标签管理',NULL,'/biz/mp/wx-account-tag/index',NULL,3000,'iconfont icon-biaoqian','1',3,'0','0','0','admin','2023-03-03 09:49:07','admin','2023-11-01 17:28:32','0',1);
INSERT INTO `sys_menu` VALUES (3011,'新增标签','mp_wx_account_tag_add',NULL,NULL,3010,NULL,'1',0,'0','0','1','admin','2023-03-03 09:49:26','admin','2023-03-11 16:29:44','0',1);
INSERT INTO `sys_menu` VALUES (3012,'编辑标签','mp_wx_account_tag_edit',NULL,NULL,3010,NULL,'1',0,'0','0','1','admin','2023-03-03 09:49:35','admin','2023-03-11 16:29:50','0',1);
INSERT INTO `sys_menu` VALUES (3013,'标签删除','mp_wx_account_tag_del',NULL,NULL,3010,NULL,'1',0,'0','0','1','admin','2023-03-03 09:49:45','admin','2023-03-11 16:29:53','0',1);
INSERT INTO `sys_menu` VALUES (3014,'同步标签','mp_wx_account_tag_sync',NULL,NULL,3010,NULL,'1',0,'0','0','1','admin','2023-03-03 09:49:55','admin','2023-03-11 16:29:56','0',1);
INSERT INTO `sys_menu` VALUES (3015,'素材管理',NULL,'/biz/mp/wx-material/index',NULL,3000,'iconfont icon-sucaiguanli','1',5,'0','0','0','admin','2023-02-27 14:13:47','admin','2023-11-01 17:28:35','0',1);
INSERT INTO `sys_menu` VALUES (3016,'素材维护','mp_wxmaterial_add',NULL,NULL,3015,NULL,'0',0,'0','0','1','admin','2023-02-27 14:14:07',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3017,'素材删除','mp_wxmaterial_del',NULL,NULL,3015,NULL,'0',0,'0','0','1','admin','2023-02-27 14:14:18',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3018,'自动回复',NULL,'/biz/mp/wx-auto-reply/index',NULL,3000,'iconfont icon-zidonghuifu','1',4,'0','0','0','admin','2023-03-01 10:56:10','admin','2023-11-01 17:28:40','0',1);
INSERT INTO `sys_menu` VALUES (3019,'新增回复','mp_wxautoreply_add',NULL,NULL,3018,NULL,'0',0,'0','0','1','admin','2023-03-01 10:56:28',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3020,'编辑回复','mp_wxautoreply_edit',NULL,NULL,3018,NULL,'0',0,'0','0','1','admin','2023-03-01 10:56:42',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3021,'删除回复','mp_wxautoreply_del',NULL,NULL,3018,NULL,'0',0,'0','0','1','admin','2023-03-01 10:56:53',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3022,'运营数据',NULL,'/biz/mp/wx-statistics/index',NULL,3000,'iconfont icon-yunyingshuju','1',8,'0','0','0','admin','2023-03-01 11:15:58','admin','2023-11-01 17:28:54','0',1);
INSERT INTO `sys_menu` VALUES (3023,'新增消息','mp_wxmsg_add',NULL,NULL,3008,NULL,'0',0,'0','0','1','admin','2023-03-01 17:12:02',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3024,'新增粉丝','mp_wxaccountfans_add','mp_wxaccountfans_add',NULL,3006,NULL,'0',0,'0','0','1','admin','2023-03-02 10:57:41',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3025,'粉丝编辑','mp_wxaccountfans_edit','mp_wxaccountfans_add',NULL,3006,NULL,'0',0,'0','0','1','admin','2023-03-02 10:57:52',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3026,'粉丝删除','mp_wxaccountfans_del','mp_wxaccountfans_add',NULL,3006,NULL,'0',0,'0','0','1','admin','2023-03-02 10:58:02',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3027,'新增菜单','mp_wxmenu_add',NULL,NULL,3002,NULL,'0',0,'0','0','1','admin','2023-02-27 20:54:34',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3028,'发布菜单','mp_wxmenu_push',NULL,NULL,3002,NULL,'0',0,'0','0','1','admin','2023-02-27 20:54:48',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (3029,'删除菜单','mp_wxmenu_del',NULL,NULL,3002,NULL,'0',0,'0','0','1','admin','2023-02-27 20:54:57',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (4000,'系统监控',NULL,'/daemon',NULL,-1,'iconfont icon-shuju','1',3,'0','0','0','admin','2023-02-06 20:20:47','admin','2023-11-01 17:12:31','1',1);
INSERT INTO `sys_menu` VALUES (4001,'文档扩展',NULL,'http://pigx-gateway:9999/admin/doc.html',NULL,9910,'iconfont icon-swagger','1',2,'0','1','0','','2018-06-26 10:50:32','admin','2023-11-27 14:52:54','0',1);
INSERT INTO `sys_menu` VALUES (4002,'缓存监控',NULL,'/tools/data/cache',NULL,9910,'iconfont icon-huancunjiankong','1',1,'0','0','0','admin','2023-05-29 15:12:59','admin','2023-11-27 14:52:56','0',1);
INSERT INTO `sys_menu` VALUES (4010,'信息推送','','/tools/message/index',NULL,9910,'iconfont icon-xinxituisong','1',7,'0',NULL,'0','',NULL,'admin','2025-01-26 22:22:24','0',1);
INSERT INTO `sys_menu` VALUES (4011,'信息推送查看','sys_message_view',NULL,NULL,4010,'1','1',0,'0',NULL,'1','',NULL,'admin','2023-10-25 14:51:54','0',1);
INSERT INTO `sys_menu` VALUES (4012,'信息推送新增','sys_message_add',NULL,NULL,4010,'1','1',1,'0',NULL,'1','',NULL,'admin','2023-10-25 14:52:00','0',1);
INSERT INTO `sys_menu` VALUES (4013,'信息推送修改','sys_message_edit',NULL,NULL,4010,'1','1',2,'0',NULL,'1','',NULL,'admin','2023-10-25 14:52:04','0',1);
INSERT INTO `sys_menu` VALUES (4014,'信息推送删除','sys_message_del',NULL,NULL,4010,'1','1',3,'0',NULL,'1','',NULL,'admin','2023-10-25 14:52:09','0',1);
INSERT INTO `sys_menu` VALUES (5000,'支付系统',NULL,'/pay',NULL,9900,'iconfont icon-duoqudaozhifuxitongguanli','1',1,'0','0','0','admin','2023-02-27 10:57:14','admin','2023-11-27 14:52:11','0',1);
INSERT INTO `sys_menu` VALUES (5001,'收银台',NULL,'/biz/pay/cd/index',NULL,5000,'iconfont icon-shouyintai','1',0,'0','0','0','admin','2023-02-27 10:58:13','admin','2023-11-01 17:28:57','0',1);
INSERT INTO `sys_menu` VALUES (5002,'支付渠道',NULL,'/biz/pay/channel/index',NULL,5000,'iconfont icon-zhifuqudao','1',1,'0','0','0','admin','2023-02-27 19:36:55','admin','2023-11-01 17:29:01','0',1);
INSERT INTO `sys_menu` VALUES (5003,'查询','pay_channel_view',NULL,NULL,5002,NULL,'0',0,'0','0','1','admin','2023-02-27 19:41:44',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5004,'新增','pay_channel_add',NULL,NULL,5002,NULL,'0',0,'0','0','1','admin','2023-02-27 19:42:05',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5005,'编辑','pay_channel_edit',NULL,NULL,5002,NULL,'0',0,'0','0','1','admin','2023-02-27 19:42:23',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5006,'删除','pay_channel_del',NULL,NULL,5002,NULL,'0',0,'0','0','1','admin','2023-02-27 19:42:40',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5007,'导出','pay_channel_export',NULL,NULL,5002,NULL,'0',0,'0','0','1','admin','2023-02-27 19:42:57',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5008,'商品订单',NULL,'/biz/pay/order/index',NULL,5000,'iconfont icon-shangpindingdan','1',2,'0','0','0','admin','2023-02-28 09:56:22',' ','2023-11-01 17:29:05','0',1);
INSERT INTO `sys_menu` VALUES (5009,'新增','pay_order_add',NULL,NULL,5008,NULL,'0',0,'0','0','1','admin','2023-02-28 09:58:25',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5010,'删除','pay_order_del',NULL,NULL,5008,NULL,'0',0,'0','0','1','admin','2023-02-28 09:58:40',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5011,'修改','pay_order_edit',NULL,NULL,5008,NULL,'0',0,'0','0','1','admin','2023-02-28 09:59:11',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5012,'查找','pay_order_view',NULL,NULL,5008,NULL,'0',0,'0','0','1','admin','2023-02-28 09:59:37',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5013,'导出','pay_order_export',NULL,NULL,5008,NULL,'0',0,'0','0','1','admin','2023-02-28 09:59:54',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5014,'通知记录',NULL,'/biz/pay/record/index',NULL,5000,'iconfont icon-shangpindingdan','1',5,'0','0','0','admin','2023-02-28 11:01:37','admin','2023-11-01 17:29:08','0',1);
INSERT INTO `sys_menu` VALUES (5015,'新增','pay_record_add',NULL,NULL,5014,NULL,'1',0,'0','0','1','admin','2023-02-28 11:04:40',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5016,'修改','pay_record_edit',NULL,NULL,5014,NULL,'1',0,'0','0','1','admin','2023-02-28 11:05:00',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5017,'删除','pay_record_del',NULL,NULL,5014,NULL,'1',0,'0','0','1','admin','2023-02-28 11:05:15',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5018,'导出','pay_record_export',NULL,NULL,5014,NULL,'1',0,'0','0','1','admin','2023-02-28 11:05:41',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5019,'查询','pay_record_view',NULL,NULL,5014,NULL,'0',0,'0','0','1','admin','2023-02-28 11:12:53',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5020,'退款订单',NULL,'/biz/pay/refund/index',NULL,5000,'iconfont icon-tuikuandingdan','1',4,'0','0','0','admin','2023-02-28 13:59:04','admin','2023-11-01 17:29:11','0',1);
INSERT INTO `sys_menu` VALUES (5021,'查询','pay_refund_view',NULL,NULL,5020,NULL,'0',0,'0','0','1','admin','2023-02-28 13:59:31',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5022,'新增','pay_refund_add',NULL,NULL,5020,NULL,'0',0,'0','0','1','admin','2023-02-28 13:59:48',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5023,'修改','pay_refund_edit',NULL,NULL,5020,NULL,'0',0,'0','0','1','admin','2023-02-28 14:00:05',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5024,'删除','pay_refund_del',NULL,NULL,5020,NULL,'0',0,'0','0','1','admin','2023-02-28 14:00:23',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5025,'导出','pay_refund_export',NULL,NULL,5020,NULL,'0',0,'0','0','1','admin','2023-02-28 14:00:35','admin','2023-02-28 14:04:15','0',1);
INSERT INTO `sys_menu` VALUES (5026,'支付订单',NULL,'/biz/pay/trade/index',NULL,5000,'iconfont icon-biaodan','1',3,'0','0','0','admin','2023-02-28 14:44:59','admin','2023-11-01 17:29:16','0',1);
INSERT INTO `sys_menu` VALUES (5027,'查询','pay_trade_view',NULL,NULL,5026,NULL,'0',0,'0','0','1','admin','2023-02-28 14:45:50',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5028,'新增','pay_trade_add',NULL,NULL,5026,NULL,'0',0,'0','0','1','admin','2023-02-28 14:46:08',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5029,'修改','pay_trade_edit',NULL,NULL,5026,NULL,'0',0,'0','0','1','admin','2023-02-28 14:46:22',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5030,'删除','pay_trade_del',NULL,NULL,5026,NULL,'0',0,'0','0','1','admin','2023-02-28 14:46:36',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (5031,'导出','pay_trade_export',NULL,NULL,5026,NULL,'0',0,'0','0','1','admin','2023-02-28 14:46:49',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (6000,'协同办公',NULL,'/flow',NULL,-1,'iconfont icon-OAxitong','1',4,'0','0','0','admin','2023-03-02 16:36:49','admin','2023-11-01 17:09:28','0',1);
INSERT INTO `sys_menu` VALUES (6001,'流程管理',NULL,'/flow/group/index',NULL,6000,'iconfont icon-liuchengguanli','1',3,'0','0','0','admin','2023-03-02 16:37:55','admin','2023-11-01 17:10:20','0',1);
INSERT INTO `sys_menu` VALUES (6002,'创建流程',NULL,'/flow/create/all',NULL,6000,'iconfont icon-faqiliucheng','0',2,'0',NULL,'0','','2023-07-27 13:14:56','admin','2023-07-27 13:32:32','0',1);
INSERT INTO `sys_menu` VALUES (6003,'发起流程',NULL,'/flow/list/index',NULL,6000,'iconfont icon-faqiliucheng','1',1,'0','0','0','admin','2023-03-02 18:18:10','admin','2025-01-26 22:21:53','0',1);
INSERT INTO `sys_menu` VALUES (6004,'任务管理',NULL,'/task',NULL,6000,'iconfont icon-renwuguanli','1',0,'0','0','0','admin','2023-03-02 22:13:29','admin','2023-11-01 17:10:13','0',1);
INSERT INTO `sys_menu` VALUES (6005,'待办任务',NULL,'/flow/task/pending',NULL,6004,'iconfont icon-jinridaiban','1',0,'0','0','0','admin','2023-03-02 22:59:35','admin','2023-11-01 17:36:55','0',1);
INSERT INTO `sys_menu` VALUES (6006,'我的已办',NULL,'/flow/task/completed',NULL,6004,'iconfont icon-document-record','1',3,'0','0','0','admin','2023-03-02 23:23:13','admin','2023-11-01 17:36:57','0',1);
INSERT INTO `sys_menu` VALUES (6007,'我的发起',NULL,'/flow/task/started',NULL,6004,'iconfont icon-wodefaqi','1',1,'0',NULL,'0','','2023-07-27 13:14:51','admin','2023-11-01 17:36:59','0',1);
INSERT INTO `sys_menu` VALUES (6008,'抄送给我',NULL,'/flow/task/cc',NULL,6004,'iconfont icon-chaosonggeiwo','1',2,'0',NULL,'0','','2023-07-27 13:14:56','admin','2023-11-01 17:37:01','0',1);
INSERT INTO `sys_menu` VALUES (7000,'APP管理',NULL,'/app',NULL,9900,'iconfont icon-menhukehuduan','1',2,'0','0','0','admin',NULL,'admin','2023-11-27 14:52:31','0',1);
INSERT INTO `sys_menu` VALUES (7100,'客户管理',NULL,'/biz/app/appuser/index',NULL,7000,'iconfont icon-gerenzhongxin','1',1,'1',NULL,'0','admin',NULL,'admin','2023-11-01 17:29:36','0',1);
INSERT INTO `sys_menu` VALUES (7101,'新增用户','app_appuser_add',NULL,NULL,7100,NULL,'1',1,'0',NULL,'1','admin',NULL,'admin','2023-01-29 07:01:00','0',1);
INSERT INTO `sys_menu` VALUES (7102,'编辑用户','app_appuser_edit',NULL,NULL,7100,NULL,'1',1,'0',NULL,'1','admin',NULL,'admin','2023-01-29 07:01:00','0',1);
INSERT INTO `sys_menu` VALUES (7103,'删除用户','app_appuser_del',NULL,NULL,7100,NULL,'1',1,'0',NULL,'1','admin',NULL,'admin','2023-01-29 07:01:00','0',1);
INSERT INTO `sys_menu` VALUES (7104,'导出用户','app_appuser_export',NULL,NULL,7100,NULL,'1',1,'0',NULL,'1','admin',NULL,'admin','2023-01-29 07:01:00','0',1);
INSERT INTO `sys_menu` VALUES (7200,'APP角色',NULL,'/biz/app/approle/index',NULL,7000,'iconfont icon-app-juese','1',2,'0','0','0','admin',NULL,'admin','2025-01-26 22:34:02','0',1);
INSERT INTO `sys_menu` VALUES (7201,'删除角色','app_approle_del',NULL,NULL,7200,NULL,'1',1,'0',NULL,'1','admin',NULL,'admin','2023-01-29 07:01:01','0',1);
INSERT INTO `sys_menu` VALUES (7202,'编辑角色','app_approle_edit',NULL,NULL,7200,NULL,'1',1,'0',NULL,'1','admin',NULL,'admin','2023-01-29 07:01:01','0',1);
INSERT INTO `sys_menu` VALUES (7203,'新增角色','app_approle_add',NULL,NULL,7200,NULL,'1',1,'0',NULL,'1','admin',NULL,'admin','2023-01-29 07:01:01','0',1);
INSERT INTO `sys_menu` VALUES (7204,'导出角色','app_approle_export',NULL,NULL,7200,NULL,'1',1,'0',NULL,'1','admin',NULL,'admin','2023-01-29 07:01:01','0',1);
INSERT INTO `sys_menu` VALUES (7300,'APP秘钥',NULL,'/biz/app/appsocial/index',NULL,7000,'iconfont icon-miyueguanli','1',3,'0','0','0','admin',NULL,'admin','2025-01-26 22:28:14','0',1);
INSERT INTO `sys_menu` VALUES (7301,'删除秘钥','app_social_details_del',NULL,NULL,7300,NULL,'1',1,'0',NULL,'1','admin',NULL,'admin','2023-01-29 07:01:02','0',1);
INSERT INTO `sys_menu` VALUES (7302,'修改秘钥','app_social_details_edit',NULL,NULL,7300,NULL,'1',1,'0',NULL,'1','admin',NULL,'admin','2023-01-29 07:01:02','0',1);
INSERT INTO `sys_menu` VALUES (7303,'保存秘钥','app_social_details_add',NULL,NULL,7300,NULL,'1',1,'0',NULL,'1','admin',NULL,'admin','2023-01-29 07:01:02','0',1);
INSERT INTO `sys_menu` VALUES (7400,'文章资讯','','/biz/app/appArticle/index',NULL,7000,'iconfont icon-wenzhangzixun','1',4,'0',NULL,'0','',NULL,'admin','2025-01-26 22:33:43','0',1);
INSERT INTO `sys_menu` VALUES (7401,'文章资讯表查看','app_appArticle_view',NULL,NULL,7400,'1','1',0,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (7402,'文章资讯表新增','app_appArticle_add',NULL,NULL,7400,'1','1',1,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (7403,'文章资讯表修改','app_appArticle_edit',NULL,NULL,7400,'1','1',2,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (7404,'文章资讯表删除','app_appArticle_del',NULL,NULL,7400,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (7405,'导入导出','app_appArticle_export',NULL,NULL,7400,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (7500,'文章分类','','/biz/app/appArticleCategory/index',NULL,7000,'iconfont icon-biaoqian','1',5,'0',NULL,'0','',NULL,'admin','2025-01-26 22:28:53','0',1);
INSERT INTO `sys_menu` VALUES (7501,'文章分类表查看','app_appArticleCategory_view',NULL,NULL,7500,'1','1',0,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (7502,'文章分类表新增','app_appArticleCategory_add',NULL,NULL,7500,'1','1',1,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (7503,'文章分类表修改','app_appArticleCategory_edit',NULL,NULL,7500,'1','1',2,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (7504,'文章分类表删除','app_appArticleCategory_del',NULL,NULL,7500,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (7505,'导入导出','app_appArticleCategory_export',NULL,NULL,7500,'1','1',3,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (7600,'文章发布',NULL,'/biz/app/appArticle/form',NULL,7000,'iconfont icon-refresh','0',4,'0','0','0','admin','2023-06-07 17:05:32','admin','2023-11-01 17:29:52','0',1);
INSERT INTO `sys_menu` VALUES (7700,'界面设置','','/biz/app/page/index',NULL,7000,'iconfont icon-diannao1','1',8,'0',NULL,'0','',NULL,'admin','2023-11-01 17:29:55','0',1);
INSERT INTO `sys_menu` VALUES (7701,'底部导航',NULL,'/biz/app/tabbar/index',NULL,7000,'iconfont icon-dibudaohang','1',9,'0','0','0','admin','2023-06-14 14:36:08','admin','2025-01-26 22:27:17','0',1);
INSERT INTO `sys_menu` VALUES (9000,'开发平台',NULL,'/gen',NULL,-1,'iconfont icon-DevOps','1',9,'0','0','0','','2019-08-12 09:35:16','admin','2025-01-26 22:38:09','0',1);
INSERT INTO `sys_menu` VALUES (9005,'数据源管理',NULL,'/gen/datasource/index',NULL,9000,'iconfont icon-shujuyuanguanli','1',0,'0',NULL,'0','','2019-08-12 09:42:11','admin','2025-01-26 22:26:51','0',1);
INSERT INTO `sys_menu` VALUES (9006,'表单设计',NULL,'/gen/design/index',NULL,9000,'iconfont icon-AIshiyanshi','0',2,'0','0','0','','2019-08-16 10:08:56','admin','2023-02-23 14:06:50','0',1);
INSERT INTO `sys_menu` VALUES (9007,'生成页面',NULL,'/gen/gener/index',NULL,9000,'iconfont icon-tongzhi4','0',1,'0','0','0','admin','2023-02-20 09:58:23','admin','2023-02-20 14:41:43','0',1);
INSERT INTO `sys_menu` VALUES (9050,'元数据管理',NULL,'/gen/metadata',NULL,9000,'iconfont icon-yuanshujuguanli','1',9,'0','0','0','','2018-07-27 01:13:21','admin','2023-02-23 19:55:10','0',1);
INSERT INTO `sys_menu` VALUES (9051,'模板管理',NULL,'/gen/template/index',NULL,9050,'iconfont icon-mti-mobanguanli','1',5,'0','0','0','admin','2023-02-21 11:22:54','admin','2023-02-23 19:56:03','0',1);
INSERT INTO `sys_menu` VALUES (9052,'查询','codegen_template_view',NULL,NULL,9051,NULL,'0',0,'0','0','1','admin','2023-02-21 12:33:03','admin','2023-02-21 13:50:54','0',1);
INSERT INTO `sys_menu` VALUES (9053,'增加','codegen_template_add',NULL,NULL,9051,NULL,'1',0,'0','0','1','admin','2023-02-21 13:34:10','admin','2023-02-21 13:39:49','0',1);
INSERT INTO `sys_menu` VALUES (9054,'新增','codegen_template_add',NULL,NULL,9051,NULL,'0',1,'0','0','1','admin','2023-02-21 13:51:32',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (9055,'导出','codegen_template_export',NULL,NULL,9051,NULL,'0',2,'0','0','1','admin','2023-02-21 13:51:58',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (9056,'删除','codegen_template_del',NULL,NULL,9051,NULL,'0',3,'0','0','1','admin','2023-02-21 13:52:16',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (9057,'编辑','codegen_template_edit',NULL,NULL,9051,NULL,'0',4,'0','0','1','admin','2023-02-21 13:52:58',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (9059,'模板分组',NULL,'/gen/group/index',NULL,9050,'iconfont icon-shuxingtu','1',6,'0','0','0','admin','2023-02-21 15:06:50','admin','2023-02-23 19:55:25','0',1);
INSERT INTO `sys_menu` VALUES (9060,'查询','codegen_group_view',NULL,NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:08:07',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (9061,'新增','codegen_group_add',NULL,NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:08:28',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (9062,'修改','codegen_group_edit',NULL,NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:08:43',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (9063,'删除','codegen_group_del',NULL,NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:09:02',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (9064,'导出','codegen_group_export',NULL,NULL,9059,NULL,'0',0,'0','0','1','admin','2023-02-21 15:09:22',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (9065,'字段管理',NULL,'/gen/field-type/index',NULL,9050,'iconfont icon-field-manage','1',0,'0','0','0','admin','2023-02-23 20:05:09','admin','2025-01-26 22:31:28','0',1);
INSERT INTO `sys_menu` VALUES (9070,'数据表管理',NULL,'/gen/create-table/index',NULL,9000,'iconfont icon-shujubiaoguanli1','1',1,'0','0','0','admin','2024-02-19 11:41:12',' ','2025-01-26 22:31:50','0',1);
INSERT INTO `sys_menu` VALUES (9071,'新增','codegen_table_add',NULL,NULL,9070,NULL,'0',0,'0','0','1','admin','2023-02-21 15:08:28',' ',NULL,'0',1);
INSERT INTO `sys_menu` VALUES (9900,'业务平台',NULL,'/biz',NULL,-1,'iconfont icon-yewupingtai','1',2,'0','0','0','admin','2023-11-01 17:07:23','admin','2025-01-26 22:17:13','0',1);
INSERT INTO `sys_menu` VALUES (9910,'基础工具',NULL,'/tools',NULL,-1,'iconfont icon-gongju','1',3,'0','0','0','admin','2023-11-01 17:12:02',' ','2023-11-27 14:53:13','0',1);
INSERT INTO `sys_menu` VALUES (9911,'路由管理',NULL,'/tools/route/index',NULL,9910,'iconfont icon-apigateway','1',3,'0','0','0','admin','2023-11-01 17:13:09','admin','2023-11-27 14:53:33','0',1);
INSERT INTO `sys_menu` VALUES (9912,'大屏看板',NULL,'/tools/data/report',NULL,9910,'iconfont icon-shuju','1',5,'0','0','0','admin','2023-11-01 17:19:38','admin','2023-11-27 14:53:38','0',1);
INSERT INTO `sys_menu` VALUES (9913,'数据报表',NULL,'/tools/data/jimu',NULL,9910,'iconfont icon-shujubaobiao','1',6,'0','0','0','admin','2023-11-01 17:20:06','admin','2023-11-27 14:53:43','0',1);
INSERT INTO `sys_menu` VALUES (9914,'路由配置','sys_route_manage',NULL,NULL,9911,'1','1',0,'0',NULL,'1',' ',NULL,' ',NULL,'0',1);
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
INSERT INTO `sys_oauth_client_details` VALUES (8, 'mini', NULL, 'mini', 'server', 'password,mobile', NULL, NULL, 160000000, 160000000, '{\"captcha_flag\":\"0\",\"enc_flag\":\"0\",\"online_quantity\":\"1\"}', 'true', '0', 'admin', 'admin', '2023-01-29 16:38:06', '2023-01-29 17:21:56', 1);
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
INSERT INTO `sys_role_menu` VALUES (1, 2106);
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
INSERT INTO `sys_role_menu` VALUES (1, 4010);
INSERT INTO `sys_role_menu` VALUES (1, 4011);
INSERT INTO `sys_role_menu` VALUES (1, 4012);
INSERT INTO `sys_role_menu` VALUES (1, 4013);
INSERT INTO `sys_role_menu` VALUES (1, 4014);
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
INSERT INTO `sys_role_menu` VALUES (1, 7400);
INSERT INTO `sys_role_menu` VALUES (1, 7401);
INSERT INTO `sys_role_menu` VALUES (1, 7402);
INSERT INTO `sys_role_menu` VALUES (1, 7403);
INSERT INTO `sys_role_menu` VALUES (1, 7404);
INSERT INTO `sys_role_menu` VALUES (1, 7405);
INSERT INTO `sys_role_menu` VALUES (1, 7500);
INSERT INTO `sys_role_menu` VALUES (1, 7501);
INSERT INTO `sys_role_menu` VALUES (1, 7502);
INSERT INTO `sys_role_menu` VALUES (1, 7503);
INSERT INTO `sys_role_menu` VALUES (1, 7504);
INSERT INTO `sys_role_menu` VALUES (1, 7505);
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
INSERT INTO `sys_role_menu` VALUES (1, 9070);
INSERT INTO `sys_role_menu` VALUES (1, 9071);
INSERT INTO `sys_role_menu` VALUES (1, 9900);
INSERT INTO `sys_role_menu` VALUES (1, 9910);
INSERT INTO `sys_role_menu` VALUES (1, 9911);
INSERT INTO `sys_role_menu` VALUES (1, 9912);
INSERT INTO `sys_role_menu` VALUES (1, 9913);
INSERT INTO `sys_role_menu` VALUES (1, 9914);
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
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (1, '工作流管理模块', 'pigx-oa-platform', '[{\"args\": {\"_genkey_0\": \"/act/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-oa-platform', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:36:56', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (2, '认证中心', 'pigx-auth', '[{\"args\": {\"_genkey_0\": \"/auth/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-auth', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2024-04-22 12:58:03', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (3, '代码生成模块', 'pigx-codegen', '[{\"args\": {\"_genkey_0\": \"/gen/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-codegen', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:36:58', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (4, 'elastic-job定时任务模块', 'pigx-daemon-elastic-job', '[{\"args\": {\"_genkey_0\": \"/daemon/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-daemon-elastic-job', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:36:59', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (5, 'quartz定时任务模块', 'pigx-daemon-quartz', '[{\"args\": {\"_genkey_0\": \"/job/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-daemon-quartz', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:02', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (6, '分布式事务模块', 'pigx-tx-manager', '[{\"args\": {\"_genkey_0\": \"/tx/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-tx-manager', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:04', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (7, '通用权限模块', 'pigx-upms-biz', '[{\"args\": {\"_genkey_0\": \"/admin/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-upms-biz', 0, '{\"response-timeout\": \"30000\"}', ' ', ' ', '2019-10-16 16:44:41', '2024-04-22 12:58:07', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (8, '工作流长链接支持', 'pigx-oa-platform-ws', '[{\"args\": {\"_genkey_0\": \"/act/ws/**\"}, \"name\": \"Path\"}]', '[]', 'lb:ws://pigx-oa-platform', 100, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:09', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (9, '微信公众号管理', 'pigx-mp-platform', '[{\"args\": {\"_genkey_0\": \"/mp/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-mp-platform', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:12', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (10, '支付管理', 'pigx-pay-platform', '[{\"args\": {\"_genkey_0\": \"/pay/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-pay-platform', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:13', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (11, '监控管理', 'pigx-monitor', '[{\"args\": {\"_genkey_0\": \"/monitor/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-monitor', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:17', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (12, '积木报表', 'pigx-jimu-platform\n', '[{\"args\": {\"_genkey_0\": \"/jimu/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-jimu-platform', 0, NULL, ' ', ' ', '2019-10-16 16:44:41', '2019-11-05 22:37:17', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (13, '大屏设计', 'pigx-report-platform', '[{\"args\": {\"_genkey_0\": \"/gv/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-report-platform', 0, '{}', ' ', ' ', '2022-08-27 02:38:43', '2023-04-05 07:52:27', '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (14, 'APP服务', 'pigx-app-server', '[{\"args\": {\"_genkey_0\": \"/app/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-app-server-biz', 0, '{}', 'admin', ' ', '2022-12-07 10:53:44', NULL, '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (15, '工作流引擎', 'pigx-flow-task-biz', '[{\"args\": {\"_genkey_0\": \"/task/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-flow-task-biz', 0, '{}', ' ', ' ', '2023-07-28 16:50:26', NULL, '0');
INSERT INTO `sys_route_conf` (`id`, `route_name`, `route_id`, `predicates`, `filters`, `uri`, `sort_order`, `metadata`, `create_by`, `update_by`, `create_time`, `update_time`, `del_flag`) VALUES (16, '知识库', 'pigx-knowledge', '[{\"args\": {\"_genkey_0\": \"/knowledge/**\"}, \"name\": \"Path\"}]', '[]', 'lb://pigx-knowledge', 0, '{}', ' ', ' ', '2023-07-28 16:50:26', NULL, '0');
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
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  `tenant_id` bigint(20) unsigned DEFAULT NULL COMMENT '租户ID',
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
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
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
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户ID',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `user_unique_phone` (`phone`) USING BTREE,
  UNIQUE KEY `user_unique_username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$c/Ae0pRjJtMZg3BnvVpO.eIK6WYWVbKTzqgdy3afR7w.vd.xi3Mgy', '', '13054729089', '/admin/sys-file/local/2a14ae08150e483c93e12ac8934173e2.png', '管理员666777', '管理员', 'sw@mail.pigxl.vip', ' ', 'admin', '2018-04-20 07:15:18', '2023-04-03 14:00:06', '0', '0', NULL, '0', NULL, 'oBxPy5E-v82xWGsfzZVzkD3wEX64', NULL, 'log4j', NULL, NULL,NULL,1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户与岗位关联表';

-- ----------------------------
-- Records of sys_user_post
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_post` VALUES (1, 1,1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept` (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `dept_id` bigint NOT NULL COMMENT '角色ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`user_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户部门表';

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_dept` (`user_id`, `dept_id`, `tenant_id`) VALUES (1, 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_user`;
CREATE TABLE `sys_tenant_user` (
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`tenant_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='租户用户表';

-- ----------------------------
-- Records of sys_tenant_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant_user` (`tenant_id`, `user_id`) VALUES (1, 1);
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
  `tenant_id` bigint DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='站内信息';

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
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除时间',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统消息推送记录';


SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for process
-- ----------------------------
DROP TABLE IF EXISTS `process`;
CREATE TABLE `process` (
  `id` bigint NOT NULL COMMENT '用户id',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '表单ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单名称',
  `logo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图标配置',
  `settings` json DEFAULT NULL COMMENT '设置项',
  `group_id` bigint NOT NULL COMMENT '分组ID',
  `form_items` json NOT NULL COMMENT '表单设置内容',
  `process` json NOT NULL COMMENT '流程设置内容',
  `remark` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `sort` int NOT NULL,
  `is_hidden` tinyint(1) NOT NULL COMMENT '0 正常 1=隐藏',
  `is_stop` tinyint(1) NOT NULL COMMENT '0 正常 1=停用 ',
  `admin_id` bigint DEFAULT NULL COMMENT '流程管理员',
  `unique_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '唯一性id',
  `admin_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '管理员',
  `range_show` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '范围描述显示',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_form_id` (`flow_id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=182 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='流程定义数据';


-- ----------------------------
-- Table structure for process_copy
-- ----------------------------
DROP TABLE IF EXISTS `process_copy`;
CREATE TABLE `process_copy` (
  `id` bigint NOT NULL COMMENT '用户id',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `start_time` datetime NOT NULL COMMENT ' 流程发起时间',
  `node_time` datetime NOT NULL COMMENT '当前节点时间',
  `start_user_id` bigint NOT NULL COMMENT '发起人',
  `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
  `process_instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例id',
  `node_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id',
  `group_id` bigint NOT NULL COMMENT '分组id',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名称',
  `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名称',
  `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点 名称',
  `form_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单数据',
  `user_id` bigint NOT NULL COMMENT '抄送人id',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='流程抄送数据';


-- ----------------------------
-- Table structure for process_group
-- ----------------------------
DROP TABLE IF EXISTS `process_group`;
CREATE TABLE `process_group` (
  `id` bigint NOT NULL COMMENT '用户id',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='流程分组';



-- ----------------------------
-- Table structure for process_instance_record
-- ----------------------------
DROP TABLE IF EXISTS `process_instance_record`;
CREATE TABLE `process_instance_record` (
  `id` bigint NOT NULL COMMENT '用户id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名字',
  `logo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程id',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程实例id',
  `form_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '表单数据',
  `group_id` bigint DEFAULT NULL COMMENT '组id',
  `group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组名称',
  `status` int DEFAULT '1' COMMENT '状态',
  `finish_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '结束原因',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `parent_process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上级流程实例id',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE,
  KEY `idx_dep_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=366 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='流程记录';


-- ----------------------------
-- Table structure for process_node_data
-- ----------------------------
DROP TABLE IF EXISTS `process_node_data`;
CREATE TABLE `process_node_data` (
  `id` bigint NOT NULL COMMENT '用户id',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
  `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单数据',
  `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1195 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='流程节点数据';


-- ----------------------------
-- Table structure for process_node_record
-- ----------------------------
DROP TABLE IF EXISTS `process_node_record`;
CREATE TABLE `process_node_record` (
  `id` bigint NOT NULL COMMENT '用户id',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
  `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '表单数据',
  `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `node_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '节点类型',
  `node_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点名字',
  `status` int NOT NULL COMMENT '节点状态',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `execution_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行id',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1435 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='流程节点记录';


-- ----------------------------
-- Table structure for process_node_record_assign_user
-- ----------------------------
DROP TABLE IF EXISTS `process_node_record_assign_user`;
CREATE TABLE `process_node_record_assign_user` (
  `id` bigint NOT NULL COMMENT '用户id',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
  `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '表单数据',
  `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ' 用户id',
  `status` int NOT NULL COMMENT '节点状态',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `execution_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行id',
  `task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT ' 任务id',
  `approve_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '审批意见',
  `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT ' 节点名称',
  `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务类型',
  `local_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '表单本地数据',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=597 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='流程节点记录-执行人';


-- ----------------------------
-- Table structure for process_starter
-- ----------------------------
DROP TABLE IF EXISTS `process_starter`;
CREATE TABLE `process_starter` (
  `id` bigint NOT NULL COMMENT '用户id',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `type_id` bigint NOT NULL COMMENT '用户id或者部门id',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ' 类型 user dept',
  `process_id` bigint NOT NULL COMMENT '流程id',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=217 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='流程发起人';

SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
                                      `sched_name` varchar(120) CHARACTER SET utf8  NOT NULL,
                                      `trigger_name` varchar(200) CHARACTER SET utf8  NOT NULL,
                                      `trigger_group` varchar(200) CHARACTER SET utf8  NOT NULL,
                                      `blob_data` blob,
                                      PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
                                      CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
                                  `sched_name` varchar(120) CHARACTER SET utf8  NOT NULL,
                                  `calendar_name` varchar(200) CHARACTER SET utf8  NOT NULL,
                                  `calendar` blob NOT NULL,
                                  PRIMARY KEY (`sched_name`,`calendar_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
                                      `sched_name` varchar(120) CHARACTER SET utf8  NOT NULL,
                                      `trigger_name` varchar(200) CHARACTER SET utf8  NOT NULL,
                                      `trigger_group` varchar(200) CHARACTER SET utf8  NOT NULL,
                                      `cron_expression` varchar(200) CHARACTER SET utf8  NOT NULL,
                                      `time_zone_id` varchar(80) CHARACTER SET utf8  DEFAULT NULL,
                                      PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
                                      CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
                                       `sched_name` varchar(120) CHARACTER SET utf8  NOT NULL,
                                       `entry_id` varchar(95) CHARACTER SET utf8  NOT NULL,
                                       `trigger_name` varchar(200) CHARACTER SET utf8  NOT NULL,
                                       `trigger_group` varchar(200) CHARACTER SET utf8  NOT NULL,
                                       `instance_name` varchar(200) CHARACTER SET utf8  NOT NULL,
                                       `fired_time` bigint NOT NULL,
                                       `sched_time` bigint NOT NULL,
                                       `priority` int NOT NULL,
                                       `state` varchar(16) CHARACTER SET utf8  NOT NULL,
                                       `job_name` varchar(200) CHARACTER SET utf8  DEFAULT NULL,
                                       `job_group` varchar(200) CHARACTER SET utf8  DEFAULT NULL,
                                       `is_nonconcurrent` varchar(1) CHARACTER SET utf8  DEFAULT NULL,
                                       `requests_recovery` varchar(1) CHARACTER SET utf8  DEFAULT NULL,
                                       PRIMARY KEY (`sched_name`,`entry_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
                                    `sched_name` varchar(120) CHARACTER SET utf8  NOT NULL,
                                    `job_name` varchar(200) CHARACTER SET utf8  NOT NULL,
                                    `job_group` varchar(200) CHARACTER SET utf8  NOT NULL,
                                    `description` varchar(250) CHARACTER SET utf8  DEFAULT NULL,
                                    `job_class_name` varchar(250) CHARACTER SET utf8  NOT NULL,
                                    `is_durable` varchar(1) CHARACTER SET utf8  NOT NULL,
                                    `is_nonconcurrent` varchar(1) CHARACTER SET utf8  NOT NULL,
                                    `is_update_data` varchar(1) CHARACTER SET utf8  NOT NULL,
                                    `requests_recovery` varchar(1) CHARACTER SET utf8  NOT NULL,
                                    `job_data` blob,
                                    PRIMARY KEY (`sched_name`,`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
                              `sched_name` varchar(120) CHARACTER SET utf8  NOT NULL,
                              `lock_name` varchar(40) CHARACTER SET utf8  NOT NULL,
                              PRIMARY KEY (`sched_name`,`lock_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
                                            `sched_name` varchar(120) CHARACTER SET utf8  NOT NULL,
                                            `trigger_group` varchar(200) CHARACTER SET utf8  NOT NULL,
                                            PRIMARY KEY (`sched_name`,`trigger_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
                                        `sched_name` varchar(120) CHARACTER SET utf8  NOT NULL,
                                        `instance_name` varchar(200) CHARACTER SET utf8  NOT NULL,
                                        `last_checkin_time` bigint NOT NULL,
                                        `checkin_interval` bigint NOT NULL,
                                        PRIMARY KEY (`sched_name`,`instance_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
                                        `sched_name` varchar(120) CHARACTER SET utf8  NOT NULL,
                                        `trigger_name` varchar(200) CHARACTER SET utf8  NOT NULL,
                                        `trigger_group` varchar(200) CHARACTER SET utf8  NOT NULL,
                                        `repeat_count` bigint NOT NULL,
                                        `repeat_interval` bigint NOT NULL,
                                        `times_triggered` bigint NOT NULL,
                                        PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
                                        CONSTRAINT `QRTZ_SIMPLE_TRIGGERS_IBFK_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
                                         `sched_name` varchar(120) CHARACTER SET utf8  NOT NULL,
                                         `trigger_name` varchar(200) CHARACTER SET utf8  NOT NULL,
                                         `trigger_group` varchar(200) CHARACTER SET utf8  NOT NULL,
                                         `str_prop_1` varchar(512) CHARACTER SET utf8  DEFAULT NULL,
                                         `str_prop_2` varchar(512) CHARACTER SET utf8  DEFAULT NULL,
                                         `str_prop_3` varchar(512) CHARACTER SET utf8  DEFAULT NULL,
                                         `int_prop_1` int DEFAULT NULL,
                                         `int_prop_2` int DEFAULT NULL,
                                         `long_prop_1` bigint DEFAULT NULL,
                                         `long_prop_2` bigint DEFAULT NULL,
                                         `dec_prop_1` decimal(13,4) DEFAULT NULL,
                                         `dec_prop_2` decimal(13,4) DEFAULT NULL,
                                         `bool_prop_1` varchar(1) CHARACTER SET utf8  DEFAULT NULL,
                                         `bool_prop_2` varchar(1) CHARACTER SET utf8  DEFAULT NULL,
                                         PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
                                         CONSTRAINT `QRTZ_SIMPROP_TRIGGERS_IBFK_1` FOREIGN KEY (`sched_name`, `trigger_name`, `trigger_group`) REFERENCES `qrtz_triggers` (`sched_name`, `trigger_name`, `trigger_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
                                 `sched_name` varchar(120) CHARACTER SET utf8  NOT NULL,
                                 `trigger_name` varchar(200) CHARACTER SET utf8  NOT NULL,
                                 `trigger_group` varchar(200) CHARACTER SET utf8  NOT NULL,
                                 `job_name` varchar(200) CHARACTER SET utf8  NOT NULL,
                                 `job_group` varchar(200) CHARACTER SET utf8  NOT NULL,
                                 `description` varchar(250) CHARACTER SET utf8  DEFAULT NULL,
                                 `next_fire_time` bigint DEFAULT NULL,
                                 `prev_fire_time` bigint DEFAULT NULL,
                                 `priority` int DEFAULT NULL,
                                 `trigger_state` varchar(16) CHARACTER SET utf8  NOT NULL,
                                 `trigger_type` varchar(8) CHARACTER SET utf8  NOT NULL,
                                 `start_time` bigint NOT NULL,
                                 `end_time` bigint DEFAULT NULL,
                                 `calendar_name` varchar(200) CHARACTER SET utf8  DEFAULT NULL,
                                 `misfire_instr` smallint DEFAULT NULL,
                                 `job_data` blob,
                                 PRIMARY KEY (`sched_name`,`trigger_name`,`trigger_group`) USING BTREE,
                                 KEY `sched_name` (`sched_name`,`job_name`,`job_group`) USING BTREE,
                                 CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`sched_name`, `job_name`, `job_group`) REFERENCES `qrtz_job_details` (`sched_name`, `job_name`, `job_group`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_job
-- ----------------------------
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
                           `tenant_id` bigint DEFAULT '1' COMMENT '租户',
                           `remark` varchar(500) CHARACTER SET utf8mb4 DEFAULT '' COMMENT '备注信息',
                           PRIMARY KEY (`job_id`,`job_name`,`job_group`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='定时任务调度表';

-- ----------------------------
-- Records of sys_job
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_job_log
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
                               `tenant_id` bigint NOT NULL DEFAULT '1' COMMENT '租户id',
                               PRIMARY KEY (`job_log_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='定时任务执行日志表';


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wx_account
-- ----------------------------
DROP TABLE IF EXISTS `wx_account`;
CREATE TABLE `wx_account` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `account` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '账号',
  `appid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用ID',
  `appsecret` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用秘钥',
  `url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'URL地址',
  `token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'Token令牌',
  `aeskey` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息加解密密钥',
  `qr_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '二维码URL地址',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公众号账户表';

-- ----------------------------
-- Records of wx_account
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wx_account_fans
-- ----------------------------
DROP TABLE IF EXISTS `wx_account_fans`;
CREATE TABLE `wx_account_fans` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `openid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '粉丝openid',
  `subscribe_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '订阅状态，0未订阅，1已订阅',
  `subscribe_time` datetime DEFAULT NULL COMMENT '订阅时间',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `gender` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '性别',
  `language` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '语言',
  `country` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '国家',
  `province` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '省份',
  `city` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '城市',
  `tag_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组ID',
  `headimg_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像URL地址',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `wx_account_id` bigint(20) DEFAULT NULL COMMENT '微信公众号ID',
  `wx_account_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信公众号名称',
  `wx_account_appid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信公众号AppID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `is_black` int(255) DEFAULT NULL COMMENT '是否在黑名单',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_1` (`openid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='微信公众号粉丝表';

-- ----------------------------
-- Records of wx_account_fans
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wx_account_tag
-- ----------------------------
DROP TABLE IF EXISTS `wx_account_tag`;
CREATE TABLE `wx_account_tag` (
  `id` bigint(20) NOT NULL COMMENT '主键ID',
  `tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '删除标记，0未删除，1已删除',
  `tenant_id` bigint(20) NOT NULL COMMENT '租户ID',
  `wx_account_id` bigint(20) NOT NULL COMMENT '微信公众号ID',
  `wx_account_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信公众号名称',
  `wx_account_appid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信公众号AppID',
  `tag_id` bigint(20) NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='微信公众号标签表';

-- ----------------------------
-- Records of wx_account_tag
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wx_auto_reply
-- ----------------------------
DROP TABLE IF EXISTS `wx_auto_reply`;
CREATE TABLE `wx_auto_reply` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `type` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类型（1、关注时回复；2、消息回复；3、关键词回复）',
  `req_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '关键词',
  `req_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求消息类型（text：文本；image：图片；voice：语音；video：视频；shortvideo：小视频；location：地理位置）',
  `rep_type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回复消息类型（text：文本；image：图片；voice：语音；video：视频；music：音乐；news：图文）',
  `rep_mate` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回复类型文本匹配类型（1、全匹配，2、半匹配）',
  `rep_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '回复类型文本保存文字',
  `rep_media_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回复类型imge、voice、news、video的mediaID或音乐缩略图的媒体id',
  `rep_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回复的素材名、视频和音乐的标题',
  `rep_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '视频和音乐的描述',
  `rep_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接',
  `rep_hq_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '高质量链接',
  `rep_thumb_media_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '缩略图的媒体id',
  `rep_thumb_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '缩略图url',
  `content` json DEFAULT NULL COMMENT '图文消息的内容',
  `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公众号ID',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='微信自动回复';

-- ----------------------------
-- Records of wx_auto_reply
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wx_mp_menu
-- ----------------------------
DROP TABLE IF EXISTS `wx_mp_menu`;
CREATE TABLE `wx_mp_menu` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `menu` json DEFAULT NULL COMMENT '菜单',
  `wx_account_id` bigint(20) DEFAULT NULL COMMENT '公众号ID',
  `wx_account_appid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公众号APPID',
  `wx_account_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公众号名称',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  `pub_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '发布标志',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='微信菜单表';

-- ----------------------------
-- Records of wx_mp_menu
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for wx_msg
-- ----------------------------
DROP TABLE IF EXISTS `wx_msg`;
CREATE TABLE `wx_msg` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `app_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公众号名称',
  `app_logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公众号logo',
  `wx_user_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '微信用户ID',
  `nick_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信用户昵称',
  `headimg_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信用户头像',
  `type` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息分类（1、用户发给公众号；2、公众号发给用户；）',
  `rep_type` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息类型（text：文本；image：图片；voice：语音；video：视频；shortvideo：小视频；location：地理位置；music：音乐；news：图文；event：推送事件）',
  `rep_event` char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '事件类型（subscribe：关注；unsubscribe：取关；CLICK、VIEW：菜单事件）',
  `rep_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '回复类型文本保存文字、地理位置信息',
  `rep_media_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回复类型imge、voice、news、video的mediaID或音乐缩略图的媒体id',
  `rep_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '回复的素材名、视频和音乐的标题',
  `rep_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '视频和音乐的描述',
  `rep_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接',
  `rep_hq_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '高质量链接',
  `content` json DEFAULT NULL COMMENT '图文消息的内容',
  `rep_thumb_media_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '缩略图的媒体id',
  `rep_thumb_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '缩略图url',
  `rep_location_x` double DEFAULT NULL COMMENT '地理位置维度',
  `rep_location_y` double DEFAULT NULL COMMENT '地理位置经度',
  `rep_scale` double DEFAULT NULL COMMENT '地图缩放大小',
  `read_flag` char(2) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '1' COMMENT '已读标记（1：是；0：否）',
  `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '公众号ID',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信唯一标识',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '逻辑删除标记（0：显示；1：隐藏）',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='微信消息';

-- ----------------------------
-- Records of wx_msg
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_channel
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel`;
CREATE TABLE `pay_channel` (
  `id` bigint(20) NOT NULL COMMENT '渠道主键ID',
  `mch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户ID',
  `channel_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道ID',
  `channel_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道名称',
  `channel_mch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道商户ID',
  `return_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '前端回调地址',
  `notify_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '后端回调地址',
  `state` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '状态',
  `param` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '参数',
  `remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付渠道表';

-- ----------------------------
-- Records of pay_channel
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pay_goods_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_goods_order`;
CREATE TABLE `pay_goods_order` (
  `goods_order_id` bigint(20) NOT NULL COMMENT '商品订单ID',
  `goods_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品ID',
  `goods_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商品名称',
  `amount` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '金额',
  `user_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ID',
  `status` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '订单状态：订单生成(0)、支付成功(1)、处理完成(2)、处理失败(-1)',
  `pay_order_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付订单ID',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`goods_order_id`) USING BTREE,
  UNIQUE KEY `IDX_PayOrderId` (`pay_order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品订单表';

-- ----------------------------
-- Records of pay_goods_order
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pay_notify_record
-- ----------------------------
DROP TABLE IF EXISTS `pay_notify_record`;
CREATE TABLE `pay_notify_record` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `notify_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '通知ID',
  `request` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '请求内容',
  `response` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '响应内容',
  `order_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '订单号',
  `http_status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'http状态',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='通知记录日志表';

-- ----------------------------
-- Records of pay_notify_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pay_refund_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_refund_order`;
CREATE TABLE `pay_refund_order` (
  `refund_order_id` bigint(20) NOT NULL COMMENT '退款订单ID',
  `pay_order_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付订单号',
  `channel_pay_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道支付订单号',
  `mch_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户号',
  `mch_refund_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户退款单号',
  `channel_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道ID',
  `pay_amount` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付金额',
  `refund_amount` bigint(20) NOT NULL COMMENT '退款金额,单位分',
  `currency` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '币种',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败,4-业务处理完成',
  `result` tinyint(4) NOT NULL DEFAULT '0' COMMENT '退款结果:0-不确认结果,1-等待手动处理,2-确认成功,3-确认失败',
  `client_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端IP',
  `device` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备信息',
  `remark` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注信息',
  `channel_user` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道用户标识',
  `username` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `channel_mch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道商户号',
  `channel_order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道订单号',
  `channel_err_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道错误码',
  `channel_err_msg` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道错误信息',
  `extra` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '附加信息',
  `notify_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '通知URL',
  `param1` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '扩展参数1',
  `param2` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '扩展参数2',
  `expire_time` datetime DEFAULT NULL COMMENT '订单失效时间',
  `refund_succ_time` datetime DEFAULT NULL COMMENT '订单退款成功时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`refund_order_id`) USING BTREE,
  UNIQUE KEY `IDX_MchId_MchOrderNo` (`mch_id`,`mch_refund_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='退款订单表';

-- ----------------------------
-- Records of pay_refund_order
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pay_trade_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_trade_order`;
CREATE TABLE `pay_trade_order` (
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `channel_id` varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道ID',
  `amount` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付金额',
  `currency` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '币种',
  `status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '支付状态:0-订单生成,1-支付中(目前未使用),2-支付成功,3-业务处理完成',
  `client_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户端IP',
  `device` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '设备信息',
  `subject` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '标题',
  `body` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '内容',
  `extra` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '附加信息',
  `channel_mch_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道商户号',
  `channel_order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '渠道订单号',
  `err_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '错误码',
  `err_msg` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '错误信息',
  `param1` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '扩展参数1',
  `param2` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '扩展参数2',
  `notify_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '通知URL',
  `notify_count` tinyint(4) DEFAULT '0' COMMENT '通知次数',
  `last_notify_time` bigint(20) DEFAULT NULL COMMENT '最后一次通知时间',
  `expire_time` bigint(20) DEFAULT NULL COMMENT '订单失效时间',
  `pay_succ_time` datetime DEFAULT NULL COMMENT '订单支付成功时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付订单表';

-- ----------------------------
-- Records of pay_trade_order
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_datasource_conf
-- ----------------------------
DROP TABLE IF EXISTS `gen_datasource_conf`;
CREATE TABLE `gen_datasource_conf` (
  `id` bigint NOT NULL COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '别名',
  `url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'jdbcurl',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
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
  `id` bigint NOT NULL COMMENT 'id',
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
-- Table structure for gen_form_conf
-- ----------------------------
DROP TABLE IF EXISTS `gen_form_conf`;
CREATE TABLE `gen_form_conf` (
  `id` bigint NOT NULL COMMENT 'ID',
  `ds_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库名称',
  `table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表名称',
  `form_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0',
  `tenant_id` bigint DEFAULT NULL COMMENT '所属租户',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `table_name` (`table_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单配置';

-- ----------------------------
-- Records of gen_form_conf
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gen_group
-- ----------------------------
DROP TABLE IF EXISTS `gen_group`;
CREATE TABLE `gen_group` (
  `id` bigint NOT NULL,
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组名称',
  `group_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分组描述',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
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
INSERT INTO `gen_group` VALUES (1, '单表增删改查', '单表增删改查', 1, ' ', ' ', NULL, NULL, '0');
INSERT INTO `gen_group` VALUES (2, '主子表表增删改查', '主子表表增删改查', 1, ' ', ' ', NULL, NULL, '0');
COMMIT;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table` (
  `id` bigint NOT NULL COMMENT 'id',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '表名',
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '类名',
  `db_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '数据库类型',
  `table_comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '说明',
  `author` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作者',
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目包名',
  `version` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '项目版本号',
  `i18n` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否生成带有i18n 0 不带有 1带有',
  `style` bigint DEFAULT NULL COMMENT '代码风格',
  `sync_menu_id` bigint DEFAULT NULL COMMENT '所属菜单ID',
  `sync_route` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否自动同步路由',
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
  `id` bigint NOT NULL COMMENT 'id',
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
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `create_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='模板';

-- ----------------------------
-- Records of gen_template
-- ----------------------------
BEGIN;
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079920092708866, 'ControllerV2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java', 'ControllerV2025001\n', 'package ${package}.${moduleName}.controller;\n\n#if($queryList)\nimport cn.hutool.core.util.StrUtil;\n#end\nimport cn.hutool.core.util.ArrayUtil;\nimport cn.hutool.core.collection.CollUtil;\nimport com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\nimport com.baomidou.mybatisplus.core.toolkit.Wrappers;\nimport com.baomidou.mybatisplus.extension.plugins.pagination.Page;\nimport ${package}.common.core.util.R;\nimport ${package}.common.log.annotation.SysLog;\n#if($opensource)\nimport com.pig4cloud.plugin.excel.annotation.ResponseExcel;\nimport com.pig4cloud.plugin.excel.annotation.RequestExcel;\n#else\nimport ${package}.common.excel.annotation.ResponseExcel;\nimport ${package}.common.excel.annotation.RequestExcel;\n#end\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport ${package}.${moduleName}.service.${ClassName}Service;\n\nimport io.swagger.v3.oas.annotations.security.SecurityRequirement;\n#if($isSpringBoot3)\nimport ${package}.common.security.annotation.HasPermission;\nimport org.springdoc.core.annotations.ParameterObject;\n#else\nimport org.springframework.security.access.prepost.PreAuthorize;\nimport org.springdoc.api.annotations.ParameterObject;\n#end\nimport org.springframework.http.HttpHeaders;\nimport io.swagger.v3.oas.annotations.tags.Tag;\nimport io.swagger.v3.oas.annotations.Operation;\nimport lombok.RequiredArgsConstructor;\nimport org.springframework.validation.BindingResult;\nimport org.springframework.web.bind.annotation.*;\n\nimport java.util.List;\nimport java.util.Objects;\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@RestController\n@RequiredArgsConstructor\n@RequestMapping(\"/${functionName}\" )\n@Tag(description = \"${functionName}\" , name = \"${tableComment}管理\" )\n@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)\npublic class ${ClassName}Controller {\n\n    private final  ${ClassName}Service ${className}Service;\n\n    /**\n     * 分页查询\n     * @param page 分页对象\n     * @param ${className} ${tableComment}\n     * @return\n     */\n    @Operation(summary = \"分页查询\" , description = \"分页查询\" )\n    @GetMapping(\"/page\" )\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_view\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_view\')\" )\n    #end\n    public R get${ClassName}Page(@ParameterObject Page page, @ParameterObject ${ClassName}Entity ${className}) {\n        LambdaQueryWrapper<${ClassName}Entity> wrapper = Wrappers.lambdaQuery();\n#foreach ($field in $queryList)\n#set($getAttrName=$str.getProperty($field.attrName))\n#set($var=\"${className}.$getAttrName()\")\n#if($field.attrType == \'String\')\n#set($expression=\"StrUtil.isNotBlank\")\n#else\n#set($expression=\"Objects.nonNull\")\n#end\n#if($field.queryType == \'=\')\n		wrapper.eq($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'like\' )\n		wrapper.like($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'!-\' )\n		wrapper.ne($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'>\' )\n		wrapper.gt($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'<\' )\n		wrapper.lt($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'>=\' )\n		wrapper.ge($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'<=\' )\n		wrapper.le($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'left like\' )\n		wrapper.likeLeft($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'right like\' )\n		wrapper.likeRight($expression($var),${ClassName}Entity::$getAttrName,$var);\n#end\n#end\n        return R.ok(${className}Service.page(page, wrapper));\n    }\n\n\n    /**\n     * 通过条件查询${tableComment}\n     * @param ${className} 查询条件\n     * @return R  对象列表\n     */\n    @Operation(summary = \"通过条件查询\" , description = \"通过条件查询对象\" )\n    @GetMapping(\"/details\" )\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_view\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_view\')\" )\n    #end\n    public R getDetails(@ParameterObject ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.list(Wrappers.query(${className})));\n    }\n\n    /**\n     * 新增${tableComment}\n     * @param ${className} ${tableComment}\n     * @return R\n     */\n    @Operation(summary = \"新增${tableComment}\" , description = \"新增${tableComment}\" )\n    @SysLog(\"新增${tableComment}\" )\n    @PostMapping\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_add\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_add\')\" )\n    #end\n    public R save(@RequestBody ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.save(${className}));\n    }\n\n    /**\n     * 修改${tableComment}\n     * @param ${className} ${tableComment}\n     * @return R\n     */\n    @Operation(summary = \"修改${tableComment}\" , description = \"修改${tableComment}\" )\n    @SysLog(\"修改${tableComment}\" )\n    @PutMapping\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_edit\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_edit\')\" )\n    #end\n    public R updateById(@RequestBody ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.updateById(${className}));\n    }\n\n    /**\n     * 通过id删除${tableComment}\n     * @param ids ${pk.attrName}列表\n     * @return R\n     */\n    @Operation(summary = \"通过id删除${tableComment}\" , description = \"通过id删除${tableComment}\" )\n    @SysLog(\"通过id删除${tableComment}\" )\n    @DeleteMapping\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_del\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_del\')\" )\n    #end\n    public R removeById(@RequestBody ${pk.attrType}[] ids) {\n        return R.ok(${className}Service.removeBatchByIds(CollUtil.toList(ids)));\n    }\n\n\n    /**\n     * 导出excel 表格\n     * @param ${className} 查询条件\n   	 * @param ids 导出指定ID\n     * @return excel 文件流\n     */\n    @ResponseExcel\n    @GetMapping(\"/export\")\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_export\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_export\')\" )\n    #end\n    public List<${ClassName}Entity> exportExcel(${ClassName}Entity ${className},${pk.attrType}[] ids) {\n        return ${className}Service.list(Wrappers.lambdaQuery(${className}).in(ArrayUtil.isNotEmpty(ids), ${ClassName}Entity::$str.getProperty($pk.attrName), ids));\n    }\n\n    /**\n     * 导入excel 表\n     * @param ${className}List 对象实体列表\n     * @param bindingResult 错误信息列表\n     * @return ok fail\n     */\n    @PostMapping(\"/import\")\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_export\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_export\')\" )\n    #end\n    public R importExcel(@RequestExcel List<${ClassName}Entity> ${className}List, BindingResult bindingResult) {\n        return R.ok(${className}Service.saveBatch(${className}List));\n    }\n}\n', '2025-06-15 10:46:01', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079920327589889, 'ServiceV2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/${ClassName}Service.java', 'ServiceV2025001\n', 'package ${package}.${moduleName}.service;\n\nimport com.baomidou.mybatisplus.extension.service.IService;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\n\npublic interface ${ClassName}Service extends IService<${ClassName}Entity> {\n\n}\n', '2025-06-15 10:46:01', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079920554082306, 'ServiceImplV2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/impl/${ClassName}ServiceImpl.java', 'ServiceImplV2025001\n', 'package ${package}.${moduleName}.service.impl;\n\nimport com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport ${package}.${moduleName}.mapper.${ClassName}Mapper;\nimport ${package}.${moduleName}.service.${ClassName}Service;\nimport org.springframework.stereotype.Service;\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@Service\npublic class ${ClassName}ServiceImpl extends ServiceImpl<${ClassName}Mapper, ${ClassName}Entity> implements ${ClassName}Service {\n\n}\n', '2025-06-15 10:46:01', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079920784769026, '实体V2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/entity/${ClassName}Entity.java', '实体V2025001\n', 'package ${package}.${moduleName}.entity;\n\nimport com.baomidou.mybatisplus.annotation.*;\nimport com.baomidou.mybatisplus.extension.activerecord.Model;\nimport io.swagger.v3.oas.annotations.media.Schema;\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\n#if($isTenant)\nimport ${package}.common.core.util.TenantTable;\n#end\n#foreach($import in $importList)\nimport $import;\n#end\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@Data\n#if($isTenant)\n@TenantTable\n#end\n@TableName(\"${tableName}\")\n@EqualsAndHashCode(callSuper = true)\n@Schema(description = \"${tableComment}\")\npublic class ${ClassName}Entity extends Model<${ClassName}Entity> {\n\n#foreach ($field in $fieldList)\n#if(${field.fieldComment})#set($comment=${field.fieldComment})#else #set($comment=${field.attrName})#end\n\n	/**\n	* $comment\n	*/\n#if($field.primaryPk == \'1\')\n    @TableId(type = IdType.ASSIGN_ID)\n#end\n#if($field.autoFill == \'INSERT\')\n	@TableField(fill = FieldFill.INSERT)\n#elseif($field.autoFill == \'INSERT_UPDATE\')\n	@TableField(fill = FieldFill.INSERT_UPDATE)\n#elseif($field.autoFill == \'UPDATE\')\n	@TableField(fill = FieldFill.UPDATE)\n#end\n#if($field.fieldName == \'del_flag\')\n    @TableLogic\n	@TableField(fill = FieldFill.INSERT)\n#end\n    @Schema(description=\"$comment\"#if($field.hidden),hidden=$field.hidden#end)\n#if($field.formType == \'checkbox\')\n    private ${field.attrType}[] $field.attrName;\n#else\n    private $field.attrType $field.attrName;\n#end    \n#end\n}\n', '2025-06-15 10:46:01', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079920998678529, 'MapperV2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/mapper/${ClassName}Mapper.java', 'MapperV2025001\n', 'package ${package}.${moduleName}.mapper;\n\n\nimport com.baomidou.mybatisplus.core.mapper.BaseMapper;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport org.apache.ibatis.annotations.Mapper;\n\n@Mapper\npublic interface ${ClassName}Mapper extends BaseMapper<${ClassName}Entity> {\n\n}\n', '2025-06-15 10:46:01', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079921220976642, 'Mapper.xmlV2025001\n', '${backendPath}/src/main/resources/mapper/${ClassName}Mapper.xml', 'Mapper.xmlV2025001\n', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n<mapper namespace=\"${package}.${moduleName}.mapper.${ClassName}Mapper\">\n\n  <resultMap id=\"${className}Map\" type=\"${package}.${moduleName}.entity.${ClassName}Entity\">\n#foreach ($field in $fieldList)\n	    #if($field.primaryPk == \'1\')\n        <id property=\"$field.attrName\" column=\"$field.fieldName\"/>\n      #else\n        <result property=\"$field.attrName\" column=\"$field.fieldName\"/>\n      #end\n#end\n  </resultMap>\n</mapper>\n', '2025-06-15 10:46:01', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079921443274753, '权限菜单V2025001\n', '${backendPath}/menu/${functionName}_menu.sql', '权限菜单V2025001\n', '-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 , #if(!$opensource)默认租户 1#end\n#set($menuId=${dateTool.getSystemTime()})\n\n-- 菜单SQL\ninsert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${menuId}, \'-1\', \'/${moduleName}/${functionName}/index\', \'\', \'0\', \'icon-bangzhushouji\', \'0\', null , \'8\', null , \'${tableComment}管理\'#if(!$opensource), 1#end);\n\n-- 菜单对应按钮SQL\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,1)},${menuId}, \'${moduleName}_${functionName}_view\', \'1\', null, \'1\',  \'0\', null, \'0\', null, \'${tableComment}查看\'#if(!$opensource), 1#end);\n\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,2)},${menuId}, \'${moduleName}_${functionName}_add\', \'1\', null, \'1\',  \'0\', null, \'1\', null, \'${tableComment}新增\'#if(!$opensource), 1#end);\n\ninsert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,3)},${menuId}, \'${moduleName}_${functionName}_edit\', \'1\', null, \'1\',  \'0\', null, \'2\', null, \'${tableComment}修改\'#if(!$opensource), 1#end);\n\ninsert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,4)},${menuId}, \'${moduleName}_${functionName}_del\', \'1\', null, \'1\',  \'0\', null, \'3\', null, \'${tableComment}删除\'#if(!$opensource), 1#end);\n\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,5)},${menuId}, \'${moduleName}_${functionName}_export\', \'1\', null, \'1\',  \'0\', null, \'3\', null, \'导入导出\'#if(!$opensource), 1#end);', '2025-06-15 10:46:01', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079921673961473, 'api.tsV2025001\n', '${frontendPath}/src/api/${moduleName}/${functionName}.ts', 'api.tsV2025001\n', 'import request from \"/@/utils/request\"\n\n// ========== 基础CRUD接口 ==========\n\n/**\n * 分页查询列表数据\n * @param query - 查询参数对象\n * @returns Promise<分页数据>\n */\nexport function fetchList(query?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}/page\',\n    method: \'get\',\n    params: query\n  })\n}\n\n/**\n * 新增数据\n * @param obj - 要新增的数据对象\n * @returns Promise<boolean> - 操作结果\n */\nexport function addObj(obj?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'post\',\n    data: obj\n  })\n}\n\n/**\n * 获取详情数据\n * @param obj - 查询参数对象（包含ID等）\n * @returns Promise<数据详情>\n */\nexport function getObj(obj?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}/details\',\n    method: \'get\',\n    params: obj\n  })\n}\n\n/**\n * 批量删除数据\n * @param ids - 要删除的ID数组\n * @returns Promise<操作结果>\n */\nexport function delObjs(ids?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'delete\',\n    data: ids\n  })\n}\n\n/**\n * 更新数据\n * @param obj - 要更新的数据对象\n * @returns Promise<操作结果>\n */\nexport function putObj(obj?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'put\',\n    data: obj\n  })\n}\n\n// ========== 工具函数 ==========\n\n/**\n * 验证字段值唯一性\n * @param rule - 验证规则对象\n * @param value - 要验证的值\n * @param callback - 验证回调函数\n * @param isEdit - 是否为编辑模式\n * \n * @example\n * // 在表单验证规则中使用\n * fieldName: [\n *   {\n *     validator: (rule, value, callback) => {\n *       validateExist(rule, value, callback, form.${pk.attrName} !== \'\');\n *     },\n *     trigger: \'blur\',\n *   },\n * ]\n */\nexport function validateExist(rule: any, value: any, callback: any, isEdit: boolean) {\n  // 编辑模式下跳过验证\n  if (isEdit) {\n    return callback();\n  }\n\n  // 查询是否存在相同值\n  getObj({ [rule.field]: value }).then((response) => {\n    const result = response.data;\n    if (result !== null && result.length > 0) {\n      callback(new Error(\'数据已经存在\'));\n    } else {\n      callback();\n    }\n  });\n}\n\n#if($ChildClassName)\n// ========== 子表相关接口 ==========\n\n/**\n * 删除子表数据\n * @param ids - 要删除的子表数据ID数组\n * @returns Promise<操作结果>\n */\nexport function delChildObj(ids?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}/child\',\n    method: \'delete\',\n    data: ids\n  })\n}\n#end\n', '2025-06-15 10:46:01', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079921900453889, '表格V2025001\n', '${frontendPath}/src/views/${moduleName}/${functionName}/index.vue', '表格V2025001\n', '<template>\n  <div class=\"layout-padding\">\n    <div class=\"layout-padding-auto layout-padding-view\">\n#if($queryList)\n      <!-- 查询表单区域 -->\n      <el-row v-show=\"showSearch\">\n        <el-form :model=\"state.queryForm\" ref=\"queryRef\" :inline=\"true\" @keyup.enter=\"getDataList\">\n#foreach($field in $queryList)\n#if($field.queryFormType == \'select\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-select v-model=\"state.queryForm.${field.attrName}\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\">\n#if($field.fieldDict)\n              <el-option \n                :label=\"item.label\" \n                :value=\"item.value\" \n                v-for=\"(item, index) in ${field.fieldDict}\" \n                :key=\"index\"\n              />\n#else\n              <el-option label=\"请选择\" value=\"0\" />\n#end\n            </el-select>\n          </el-form-item>\n#elseif($field.queryFormType == \'date\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-date-picker \n              type=\"date\" \n              placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" \n              v-model=\"state.queryForm.${field.attrName}\"\n            />\n          </el-form-item>\n#elseif($field.queryFormType == \'datetime\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-date-picker \n              type=\"datetime\" \n              placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" \n              v-model=\"state.queryForm.${field.attrName}\"\n            />\n          </el-form-item>\n#elseif($field.formType == \'radio\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-radio-group v-model=\"state.queryForm.${field.attrName}\">\n#if($field.fieldDict)\n              <el-radio \n                :label=\"item.value\" \n                v-for=\"(item, index) in ${field.fieldDict}\" \n                border \n                :key=\"index\"\n              >\n                {{ item.label }}\n              </el-radio>\n#else\n              <el-radio label=\"${field.fieldComment}\" border>\n                ${field.fieldComment}\n              </el-radio>\n#end\n            </el-radio-group>\n          </el-form-item>\n#else\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-input \n              placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" \n              v-model=\"state.queryForm.${field.attrName}\" \n            />\n          </el-form-item>\n#end\n#end\n          <el-form-item>\n            <el-button icon=\"search\" type=\"primary\" @click=\"getDataList\">\n              查询\n            </el-button>\n            <el-button icon=\"Refresh\" @click=\"resetQuery\">重置</el-button>\n          </el-form-item>\n        </el-form>\n      </el-row>\n#end\n\n      <!-- 操作按钮区域 -->\n      <el-row>\n        <div class=\"mb8\" style=\"width: 100%\">\n          <el-button \n            icon=\"folder-add\" \n            type=\"primary\" \n            class=\"ml10\" \n            @click=\"formDialogRef.openDialog()\"\n            v-auth=\"\'${moduleName}_${functionName}_add\'\"\n          >\n            新增\n          </el-button>\n          <el-button \n            plain \n            icon=\"upload-filled\" \n            type=\"primary\" \n            class=\"ml10\" \n            @click=\"excelUploadRef.show()\" \n            v-auth=\"\'${moduleName}_${functionName}_add\'\"\n          >\n            导入\n          </el-button>\n          <el-button \n            plain \n            :disabled=\"multiple\" \n            icon=\"Delete\" \n            type=\"primary\"\n            v-auth=\"\'${moduleName}_${functionName}_del\'\" \n            @click=\"handleDelete(selectObjs)\"\n          >\n            删除\n          </el-button>\n          <right-toolbar \n            v-model:showSearch=\"showSearch\" \n            :export=\"\'${moduleName}_${functionName}_export\'\"\n            @exportExcel=\"exportExcel\" \n            class=\"ml10 mr20\" \n            style=\"float: right;\"\n            @queryTable=\"getDataList\"\n          />\n        </div>\n      </el-row>\n\n      <!-- 数据表格区域 -->\n      <el-table \n        :data=\"state.dataList\" \n        v-loading=\"state.loading\" \n        border \n        :cell-style=\"tableStyle.cellStyle\" \n        :header-cell-style=\"tableStyle.headerCellStyle\"\n        @selection-change=\"selectionChangHandle\"\n        @sort-change=\"sortChangeHandle\"\n      >\n        <el-table-column type=\"selection\" width=\"40\" align=\"center\" />\n        <el-table-column type=\"index\" label=\"#\" width=\"40\" />\n#foreach($field in $gridList)\n#if($field.fieldDict)\n        <el-table-column prop=\"${field.attrName}\" label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" show-overflow-tooltip>\n          <template #default=\"scope\">\n            <dict-tag :options=\"$field.fieldDict\" :value=\"scope.row.${field.attrName}\" />\n          </template>\n        </el-table-column>\n#else\n        <el-table-column \n          prop=\"${field.attrName}\" \n          label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" \n#if(${field.gridSort} == \'1\')\n          sortable=\"custom\" \n#end\n          show-overflow-tooltip\n        />\n#end\n#end\n        <el-table-column label=\"操作\" width=\"150\">\n          <template #default=\"scope\">\n            <el-button \n              icon=\"edit-pen\" \n              text \n              type=\"primary\" \n              v-auth=\"\'${moduleName}_${functionName}_edit\'\"\n              @click=\"formDialogRef.openDialog(scope.row.${pk.attrName})\"\n            >\n              编辑\n            </el-button>\n            <el-button \n              icon=\"delete\" \n              text \n              type=\"primary\" \n              v-auth=\"\'${moduleName}_${functionName}_del\'\" \n              @click=\"handleDelete([scope.row.${pk.attrName}])\"\n            >\n              删除\n            </el-button>\n          </template>\n        </el-table-column>\n      </el-table>\n\n      <!-- 分页组件 -->\n      <pagination \n        @size-change=\"sizeChangeHandle\" \n        @current-change=\"currentChangeHandle\" \n        v-bind=\"state.pagination\" \n      />\n    </div>\n\n    <!-- 编辑、新增弹窗 -->\n    <form-dialog ref=\"formDialogRef\" @refresh=\"getDataList(false)\" />\n\n    <!-- 导入excel弹窗 (需要在 upms-biz/resources/file 下维护模板) -->\n    <upload-excel\n      ref=\"excelUploadRef\"\n      title=\"导入\"\n      url=\"/${moduleName}/${functionName}/import\"\n      temp-url=\"/admin/sys-file/local/file/${functionName}.xlsx\"\n      @refreshDataList=\"getDataList\"\n    />\n  </div>\n</template>\n\n<script setup lang=\"ts\" name=\"system${ClassName}\">\n// ========== 导入声明 ==========\nimport { BasicTableProps, useTable } from \"/@/hooks/table\";\nimport { fetchList, delObjs } from \"/@/api/${moduleName}/${functionName}\";\nimport { useMessage, useMessageBox } from \"/@/hooks/message\";\nimport { useDict } from \'/@/hooks/dict\';\n\n// ========== 组件声明 ==========\n// 异步加载表单弹窗组件\nconst FormDialog = defineAsyncComponent(() => import(\'./form.vue\'));\n\n// ========== 字典数据 ==========\n#set($fieldDict=[])\n#foreach($field in $queryList)\n#if($field.fieldDict)\n#set($void=$fieldDict.add($field.fieldDict))\n#end\n#end\n#foreach($field in $gridList)\n#if($field.fieldDict)\n#set($void=$fieldDict.add($field.fieldDict))\n#end\n#end\n#if($fieldDict)\n// 加载字典数据\nconst { $dict.format($fieldDict) } = useDict($dict.quotation($fieldDict));\n#end\n\n// ========== 组件引用 ==========\nconst formDialogRef = ref();          // 表单弹窗引用\nconst excelUploadRef = ref();         // Excel上传弹窗引用\nconst queryRef = ref();               // 查询表单引用\n\n// ========== 响应式数据 ==========\nconst showSearch = ref(true);         // 是否显示搜索区域\nconst selectObjs = ref([]) as any;    // 表格多选数据\nconst multiple = ref(true);           // 是否多选\n\n// ========== 表格状态 ==========\nconst state: BasicTableProps = reactive<BasicTableProps>({\n  queryForm: {},    // 查询参数\n  pageList: fetchList // 分页查询方法\n});\n\n// ========== Hook引用 ==========\n// 表格相关Hook\nconst {\n  getDataList,\n  currentChangeHandle,\n  sizeChangeHandle,\n  sortChangeHandle,\n  downBlobFile,\n  tableStyle\n} = useTable(state);\n\n// ========== 方法定义 ==========\n/**\n * 重置查询条件\n */\nconst resetQuery = () => {\n  // 清空搜索条件\n  queryRef.value?.resetFields();\n  // 清空多选\n  selectObjs.value = [];\n  // 重新查询\n  getDataList();\n};\n\n/**\n * 导出Excel文件\n */\nconst exportExcel = () => {\n  downBlobFile(\n    \'/${moduleName}/${functionName}/export\',\n    Object.assign(state.queryForm, { ids: selectObjs }),\n    \'${functionName}.xlsx\'\n  );\n};\n\n/**\n * 表格多选事件处理\n * @param objs 选中的数据行\n */\nconst selectionChangHandle = (objs: { $pk.attrName: string }[]) => {\n  selectObjs.value = objs.map(({ $pk.attrName }) => $pk.attrName);\n  multiple.value = !objs.length;\n};\n\n/**\n * 删除数据处理\n * @param ids 要删除的数据ID数组\n */\nconst handleDelete = async (ids: string[]) => {\n  try {\n    await useMessageBox().confirm(\'此操作将永久删除\');\n  } catch {\n    return;\n  }\n\n  try {\n    await delObjs(ids);\n    getDataList();\n    useMessage().success(\'删除成功\');\n  } catch (err: any) {\n    useMessage().error(err.msg);\n  }\n};\n</script>\n', '2025-06-15 10:46:02', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079922147917825, '表单V2025001\n', '${frontendPath}/src/views/${moduleName}/${functionName}/form.vue', '表单V2025001\n', '<template>\n  <el-dialog :title=\"form.${pk.attrName} ? \'编辑\' : \'新增\'\" v-model=\"visible\"\n    :close-on-click-modal=\"false\" draggable>\n    <el-form ref=\"dataFormRef\" :model=\"form\" :rules=\"dataRules\" formDialogRef label-width=\"90px\" v-loading=\"loading\">\n      <el-row :gutter=\"24\">\n#foreach($field in $formList)\n#if($field.attrName != ${pk.attrName})\n#if($formLayout == 1)\n        <el-col :span=\"24\" class=\"mb20\">\n#elseif($formLayout == 2)\n        <el-col :span=\"12\" class=\"mb20\">\n#end\n#if($field.formType == \'text\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-input v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\"/>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'textarea\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-input type=\"textarea\" v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\"/>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'select\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-select v-model=\"form.${field.attrName}\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\">\n#if($field.fieldDict)\n              <el-option :value=\"item.value\" :label=\"item.label\" v-for=\"(item, index) in ${field.fieldDict}\" :key=\"index\"></el-option>\n#else\n              <el-option label=\"请选择\" value=\"0\"></el-option>\n#end\n            </el-select>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'radio\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-radio-group v-model=\"form.${field.attrName}\">\n#if($field.fieldDict)\n              <el-radio :label=\"item.value\" v-for=\"(item, index) in ${field.fieldDict}\" border :key=\"index\">{{ item.label }}</el-radio>\n#else\n              <el-radio label=\"${field.fieldComment}\" border>${field.fieldComment}</el-radio>\n#end\n            </el-radio-group>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'checkbox\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-checkbox-group v-model=\"form.${field.attrName}\">\n#if($field.fieldDict)\n              <el-checkbox :label=\"item.value\" v-for=\"(item, index) in ${field.fieldDict}\" :key=\"index\">{{ item.label }}</el-checkbox>\n#else\n              <el-checkbox label=\"启用\" name=\"type\"></el-checkbox>\n              <el-checkbox label=\"禁用\" name=\"type\"></el-checkbox>\n#end\n            </el-checkbox-group>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'date\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-date-picker type=\"date\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" v-model=\"form.${field.attrName}\" :value-format=\"dateStr\"></el-date-picker>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'datetime\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-date-picker type=\"datetime\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" v-model=\"form.${field.attrName}\" :value-format=\"dateTimeStr\"></el-date-picker>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'number\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-input-number :min=\"1\" :max=\"1000\" v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\"></el-input-number>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'upload-file\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <upload-file v-model=\"form.${field.attrName}\"></upload-file>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'upload-img\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <upload-img v-model:imageUrl=\"form.${field.attrName}\"></upload-img>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'editor\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <editor v-if=\"visible\" v-model:get-html=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\"></editor>\n          </el-form-item>\n        </el-col>\n#else\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-input v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\"/>\n          </el-form-item>\n        </el-col>\n#end\n#end\n#end\n      </el-row>\n    </el-form>\n    <template #footer>\n      <span class=\"dialog-footer\">\n        <el-button @click=\"visible = false\">取 消</el-button>\n        <el-button type=\"primary\" @click=\"onSubmit\" :disabled=\"loading\">确 认</el-button>\n      </span>\n    </template>\n  </el-dialog>\n</template>\n\n<script setup lang=\"ts\" name=\"${ClassName}Dialog\">\n// ========== 1. 导入语句 ==========\nimport { useDict } from \'/@/hooks/dict\';\nimport { rule } from \'/@/utils/validate\';\nimport { useMessage } from \"/@/hooks/message\";\nimport { getObj, addObj, putObj, validateExist } from \'/@/api/${moduleName}/${functionName}\';\n\n// ========== 2. 组件定义 ==========\n// 定义组件事件\nconst emit = defineEmits([\'refresh\']);\n\n// ========== 3. 响应式数据定义 ==========\n// 基础响应式变量\nconst dataFormRef = ref(); // 表单引用\nconst visible = ref(false); // 弹窗显示状态\nconst loading = ref(false); // 加载状态\n\n// 表单数据对象\nconst form = reactive({\n#if(!$formList.contains(${pk.attrName}))\n  ${pk.attrName}: \'\', // 主键\n#end\n#foreach($field in $formList)\n#if($field.formType == \'number\')\n  ${field.attrName}: 0, // ${field.fieldComment}\n#elseif($field.formType == \'checkbox\')\n  ${field.attrName}: [], // ${field.fieldComment}\n#else\n  ${field.attrName}: \'\', // ${field.fieldComment}\n#end\n#end\n});\n\n// ========== 4. 字典数据处理 ==========\n#set($fieldDict=[])\n#foreach($field in $gridList)\n#if($field.fieldDict)\n#set($void=$fieldDict.add($field.fieldDict))\n#end\n#end\n#if($fieldDict && $fieldDict.size() > 0)\n// 加载字典数据\nconst { $dict.format($fieldDict) } = useDict($dict.quotation($fieldDict));\n#end\n\n// ========== 5. 表单校验规则 ==========\nconst dataRules = ref({\n#foreach($field in $formList)\n#if($field.formRequired == \'1\' && $field.formValidator == \'duplicate\')\n  ${field.attrName}: [\n    { required: true, message: \'${field.fieldComment}不能为空\', trigger: \'blur\' },\n    {\n      validator: (rule: any, value: any, callback: any) => {\n        // 重复性校验（编辑时跳过）\n        validateExist(rule, value, callback, form.${pk.attrName} !== \'\');\n      },\n      trigger: \'blur\',\n    }\n  ],\n#elseif($field.formRequired == \'1\' && $field.formValidator)\n  ${field.attrName}: [\n    { required: true, message: \'${field.fieldComment}不能为空\', trigger: \'blur\' },\n    { validator: rule.${field.formValidator}, trigger: \'blur\' }\n  ],\n#elseif($field.formRequired == \'1\')\n  ${field.attrName}: [\n    { required: true, message: \'${field.fieldComment}不能为空\', trigger: \'blur\' }\n  ],\n#elseif($field.formValidator)\n  ${field.attrName}: [\n    { validator: rule.${field.formValidator}, trigger: \'blur\' }\n  ],\n#end\n#end\n});\n\n// ========== 6. 方法定义 ==========\n// 获取详情数据\nconst get${ClassName}Data = async (id: string) => {\n  try {\n    loading.value = true;\n    const { data } = await getObj({ ${pk.attrName}: id });\n    // 直接将第一条数据赋值给表单\n    Object.assign(form, data[0]);\n  } catch (error) {\n    useMessage().error(\'获取数据失败\');\n  } finally {\n    loading.value = false;\n  }\n};\n\n// 打开弹窗方法\nconst openDialog = (id: string) => {\n  visible.value = true;\n  form.${pk.attrName} = \'\';\n\n  // 重置表单数据\n  nextTick(() => {\n    dataFormRef.value?.resetFields();\n  });\n\n  // 获取${ClassName}信息\n  if (id) {\n    form.${pk.attrName} = id;\n    get${ClassName}Data(id);\n  }\n};\n\n// 提交表单方法\nconst onSubmit = async () => {\n  loading.value = true; // 防止重复提交\n  \n  // 表单校验\n  const valid = await dataFormRef.value.validate().catch(() => {});\n  if (!valid) {\n    loading.value = false;\n    return false;\n  }\n\n  try {\n    // 根据是否有ID判断是新增还是修改\n    form.${pk.attrName} ? await putObj(form) : await addObj(form);\n    useMessage().success(form.${pk.attrName} ? \'修改成功\' : \'添加成功\');\n    visible.value = false;\n    emit(\'refresh\'); // 通知父组件刷新列表\n  } catch (err: any) {\n    useMessage().error(err.msg);\n  } finally {\n    loading.value = false;\n  }\n};\n\n// ========== 7. 对外暴露 ==========\n// 暴露方法给父组件\ndefineExpose({\n  openDialog\n});\n</script> ', '2025-06-15 10:46:02', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079922437324801, 'ServiceV2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/${ClassName}Service.java', 'ServiceV2025001\n', 'package ${package}.${moduleName}.service;\n\nimport com.github.yulichang.extension.mapping.base.MPJDeepService;\nimport ${package}.${moduleName}.entity.${ChildClassName}Entity;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\n\npublic interface ${ClassName}Service extends MPJDeepService<${ClassName}Entity> {\n    Boolean saveDeep(${ClassName}Entity ${className});\n\n    Boolean updateDeep(${ClassName}Entity ${className});\n\n    Boolean removeDeep(Long[] ids);\n\n    Boolean removeChild(Long[] ids);\n}\n', '2025-06-15 10:46:02', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079922659622913, 'ServiceImplV2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/impl/${ClassName}ServiceImpl.java', 'ServiceImplV2025001\n', 'package ${package}.${moduleName}.service.impl;\n\nimport com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport ${package}.${moduleName}.mapper.${ClassName}Mapper;\nimport ${package}.${moduleName}.service.${ClassName}Service;\nimport org.springframework.stereotype.Service;\nimport cn.hutool.core.collection.CollUtil;\nimport com.baomidou.mybatisplus.core.toolkit.Wrappers;\nimport ${package}.${moduleName}.entity.${ChildClassName}Entity;\nimport ${package}.${moduleName}.mapper.${ChildClassName}Mapper;\nimport org.springframework.transaction.annotation.Transactional;\nimport lombok.RequiredArgsConstructor;\nimport java.util.Objects;\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@Service\n@RequiredArgsConstructor\npublic class ${ClassName}ServiceImpl extends ServiceImpl<${ClassName}Mapper, ${ClassName}Entity> implements ${ClassName}Service {\n\n  private final ${ChildClassName}Mapper ${childClassName}Mapper;\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean saveDeep(${ClassName}Entity ${className}) {\n        baseMapper.insert(${className});\n        for (${ChildClassName}Entity  ${childClassName} : ${className}.get${ChildClassName}List()) {\n            ${childClassName}.$str.setProperty($childField)(${className}.$str.getProperty($mainField)());\n            ${childClassName}Mapper.insert( ${childClassName});\n        }\n\n        return Boolean.TRUE;\n    }\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean updateDeep(${ClassName}Entity ${className}) {\n        baseMapper.updateById(${className});\n        for (${ChildClassName}Entity  ${childClassName} : ${className}.get${ChildClassName}List()) {\n#set($getChildPkName=$str.getProperty(${pk.attrName}))\n            if (Objects.isNull(${childClassName}.$getChildPkName())) {\n                ${childClassName}.$str.setProperty($childField)(${className}.$str.getProperty($mainField)());\n                ${childClassName}Mapper.insert(${childClassName});\n            } else {\n                ${childClassName}Mapper.updateById(${childClassName});\n            }\n        }\n        return Boolean.TRUE;\n    }\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean removeDeep(Long[] ids) {\n        baseMapper.deleteBatchIds(CollUtil.toList(ids));\n        ${childClassName}Mapper.delete(Wrappers.<${ChildClassName}Entity>lambdaQuery().in(${ChildClassName}Entity::$str.getProperty($childField), ids));\n        return Boolean.TRUE;\n    }\n\n    @Override\n    @Transactional(rollbackFor = Exception.class)\n    public Boolean removeChild(Long[] ids) {\n        ${childClassName}Mapper.deleteBatchIds(CollUtil.toList(ids));\n        return Boolean.TRUE;\n    }\n}\n', '2025-06-15 10:46:02', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079922873532418, '主实体V2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/entity/${ClassName}Entity.java', '主实体V2025001\n', 'package ${package}.${moduleName}.entity;\n\nimport com.baomidou.mybatisplus.annotation.*;\nimport com.baomidou.mybatisplus.extension.activerecord.Model;\nimport io.swagger.v3.oas.annotations.media.Schema;\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\n#if($isTenant)\nimport ${package}.common.core.util.TenantTable;\n#end\n#foreach($import in $importList)\nimport $import;\n#end\nimport cn.idev.excel.annotation.ExcelIgnore;\nimport com.github.yulichang.annotation.EntityMapping;\nimport java.util.List;\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@Data\n#if($isTenant)\n@TenantTable\n#end\n@TableName(\"${tableName}\")\n@EqualsAndHashCode(callSuper = true)\n@Schema(description = \"${tableComment}\")\npublic class ${ClassName}Entity extends Model<${ClassName}Entity> {\n\n#foreach ($field in $fieldList)\n#if(${field.fieldComment})#set($comment=${field.fieldComment})#else #set($comment=${field.attrName})#end\n\n	/**\n	* $comment\n	*/\n#if($field.primaryPk == \'1\')\n    @TableId(type = IdType.ASSIGN_ID)\n#end\n#if($field.autoFill == \'INSERT\')\n	@TableField(fill = FieldFill.INSERT)\n#elseif($field.autoFill == \'INSERT_UPDATE\')\n	@TableField(fill = FieldFill.INSERT_UPDATE)\n#elseif($field.autoFill == \'UPDATE\')\n	@TableField(fill = FieldFill.UPDATE)\n#end\n#if($field.fieldName == \'del_flag\')\n    @TableLogic\n	@TableField(fill = FieldFill.INSERT)\n#end\n    @Schema(description=\"$comment\"#if($field.hidden),hidden=$field.hidden#end)\n#if($field.formType == \'checkbox\')\n    private ${field.attrType}[] $field.attrName;\n#else\n    private $field.attrType $field.attrName;\n#end    \n#end\n    @ExcelIgnore\n    @TableField(exist = false)\n    @EntityMapping(thisField = \"$mainField\", joinField = \"$childField\")\n    private List<${ChildClassName}Entity> ${childClassName}List;\n}\n', '2025-06-15 10:46:02', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079923095830529, 'MapperV2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/mapper/${ClassName}Mapper.java', 'MapperV2025001\n', 'package ${package}.${moduleName}.mapper;\n\n\nimport com.baomidou.mybatisplus.core.mapper.BaseMapper;\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport org.apache.ibatis.annotations.Mapper;\n\n@Mapper\npublic interface ${ClassName}Mapper extends BaseMapper<${ClassName}Entity> {\n\n}\n', '2025-06-15 10:46:02', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079923313934337, 'Mapper.xmlV2025001\n', '${backendPath}/src/main/resources/mapper/${ClassName}Mapper.xml', 'Mapper.xmlV2025001\n', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n\n<mapper namespace=\"${package}.${moduleName}.mapper.${ClassName}Mapper\">\n\n  <resultMap id=\"${className}Map\" type=\"${package}.${moduleName}.entity.${ClassName}Entity\">\n#foreach ($field in $fieldList)\n	    #if($field.primaryPk == \'1\')\n        <id property=\"$field.attrName\" column=\"$field.fieldName\"/>\n      #else\n        <result property=\"$field.attrName\" column=\"$field.fieldName\"/>\n      #end\n#end\n  </resultMap>\n</mapper>\n', '2025-06-15 10:46:02', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079923536232449, '权限菜单V2025001\n', '${backendPath}/menu/${functionName}_menu.sql', '权限菜单V2025001\n', '-- 该脚本不要直接执行， 注意维护菜单的父节点ID 默认 父节点-1 , #if(!$opensource)默认租户 1#end\n#set($menuId=${dateTool.getSystemTime()})\n\n-- 菜单SQL\ninsert into sys_menu ( menu_id,parent_id, path, permission, menu_type, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${menuId}, \'-1\', \'/${moduleName}/${functionName}/index\', \'\', \'0\', \'icon-bangzhushouji\', \'0\', null , \'8\', null , \'${tableComment}管理\'#if(!$opensource), 1#end);\n\n-- 菜单对应按钮SQL\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,1)},${menuId}, \'${moduleName}_${functionName}_view\', \'1\', null, \'1\',  \'0\', null, \'0\', null, \'${tableComment}查看\'#if(!$opensource), 1#end);\n\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,2)},${menuId}, \'${moduleName}_${functionName}_add\', \'1\', null, \'1\',  \'0\', null, \'1\', null, \'${tableComment}新增\'#if(!$opensource), 1#end);\n\ninsert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon,  del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,3)},${menuId}, \'${moduleName}_${functionName}_edit\', \'1\', null, \'1\',  \'0\', null, \'2\', null, \'${tableComment}修改\'#if(!$opensource), 1#end);\n\ninsert into sys_menu (menu_id, parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,4)},${menuId}, \'${moduleName}_${functionName}_del\', \'1\', null, \'1\',  \'0\', null, \'3\', null, \'${tableComment}删除\'#if(!$opensource), 1#end);\n\ninsert into sys_menu ( menu_id,parent_id, permission, menu_type, path, icon, del_flag, create_time, sort_order, update_time, name#if(!$opensource), tenant_id#end)\nvalues (${math.add($menuId,5)},${menuId}, \'${moduleName}_${functionName}_export\', \'1\', null, \'1\',  \'0\', null, \'3\', null, \'导入导出\'#if(!$opensource), 1#end);', '2025-06-15 10:46:02', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079923766919169, 'api.tsV2025001\n', '${frontendPath}/src/api/${moduleName}/${functionName}.ts', 'api.tsV2025001\n', 'import request from \"/@/utils/request\"\n\n// ========== 基础CRUD接口 ==========\n\n/**\n * 分页查询列表数据\n * @param query - 查询参数对象\n * @returns Promise<分页数据>\n */\nexport function fetchList(query?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}/page\',\n    method: \'get\',\n    params: query\n  })\n}\n\n/**\n * 新增数据\n * @param obj - 要新增的数据对象\n * @returns Promise<boolean> - 操作结果\n */\nexport function addObj(obj?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'post\',\n    data: obj\n  })\n}\n\n/**\n * 获取详情数据\n * @param obj - 查询参数对象（包含ID等）\n * @returns Promise<数据详情>\n */\nexport function getObj(obj?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}/details\',\n    method: \'get\',\n    params: obj\n  })\n}\n\n/**\n * 批量删除数据\n * @param ids - 要删除的ID数组\n * @returns Promise<操作结果>\n */\nexport function delObjs(ids?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'delete\',\n    data: ids\n  })\n}\n\n/**\n * 更新数据\n * @param obj - 要更新的数据对象\n * @returns Promise<操作结果>\n */\nexport function putObj(obj?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}\',\n    method: \'put\',\n    data: obj\n  })\n}\n\n// ========== 工具函数 ==========\n\n/**\n * 验证字段值唯一性\n * @param rule - 验证规则对象\n * @param value - 要验证的值\n * @param callback - 验证回调函数\n * @param isEdit - 是否为编辑模式\n * \n * @example\n * // 在表单验证规则中使用\n * fieldName: [\n *   {\n *     validator: (rule, value, callback) => {\n *       validateExist(rule, value, callback, form.${pk.attrName} !== \'\');\n *     },\n *     trigger: \'blur\',\n *   },\n * ]\n */\nexport function validateExist(rule: any, value: any, callback: any, isEdit: boolean) {\n  // 编辑模式下跳过验证\n  if (isEdit) {\n    return callback();\n  }\n\n  // 查询是否存在相同值\n  getObj({ [rule.field]: value }).then((response) => {\n    const result = response.data;\n    if (result !== null && result.length > 0) {\n      callback(new Error(\'数据已经存在\'));\n    } else {\n      callback();\n    }\n  });\n}\n\n#if($ChildClassName)\n// ========== 子表相关接口 ==========\n\n/**\n * 删除子表数据\n * @param ids - 要删除的子表数据ID数组\n * @returns Promise<操作结果>\n */\nexport function delChildObj(ids?: Object) {\n  return request({\n    url: \'/${moduleName}/${functionName}/child\',\n    method: \'delete\',\n    data: ids\n  })\n}\n#end\n', '2025-06-15 10:46:02', '2025-06-15 10:46:01', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079923989217282, '子实体V2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/entity/${ChildClassName}Entity.java', '子实体V2025001\n', 'package ${package}.${moduleName}.entity;\n\nimport com.baomidou.mybatisplus.annotation.*;\nimport com.baomidou.mybatisplus.extension.activerecord.Model;\nimport io.swagger.v3.oas.annotations.media.Schema;\n#if($isChildTenant)\nimport ${package}.common.core.util.TenantTable;\n#end\nimport lombok.Data;\nimport lombok.EqualsAndHashCode;\n#foreach($import in $importList)\nimport $import;\n#end\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@Data\n#if($isChildTenant)\n@TenantTable\n#end\n@TableName(\"${childTableName}\")\n@EqualsAndHashCode(callSuper = true)\n@Schema(description = \"${childTableName}\")\npublic class ${ChildClassName}Entity extends Model<${ChildClassName}Entity> {\n\n#foreach ($field in $childFieldList)\n#if(${field.fieldComment})#set($comment=${field.fieldComment})#else #set($comment=${field.attrName})#end\n	/**\n	* $comment\n	*/\n#if($field.primaryPk == \'1\')\n	@TableId(type = IdType.ASSIGN_ID)\n#end\n#if($field.autoFill == \'INSERT\')\n	@TableField(fill = FieldFill.INSERT)\n#elseif($field.autoFill == \'INSERT_UPDATE\')\n	@TableField(fill = FieldFill.INSERT_UPDATE)\n#elseif($field.autoFill == \'UPDATE\')\n	@TableField(fill = FieldFill.UPDATE)\n#end\n#if($field.fieldName == \'del_flag\')\n  @TableLogic\n	@TableField(fill = FieldFill.INSERT)\n#end\n	@Schema(description=\"$comment\"#if($field.hidden),hidden=$field.hidden#end)\n#if($field.formType == \'checkbox\')\n   private ${field.attrType}[] $field.attrName;\n#else\n   private $field.attrType $field.attrName;\n#end \n#end\n}\n', '2025-06-15 10:46:02', '2025-06-15 10:46:02', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079924215709698, '主子ContollerV2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java', '主子ContollerV2025001\n', 'package ${package}.${moduleName}.controller;\n\n#if($queryList)\nimport cn.hutool.core.util.StrUtil;\n#end\nimport cn.hutool.core.util.ArrayUtil;\nimport com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\nimport com.baomidou.mybatisplus.core.toolkit.Wrappers;\nimport com.baomidou.mybatisplus.extension.plugins.pagination.Page;\nimport ${package}.common.core.util.R;\nimport ${package}.common.log.annotation.SysLog;\n#if($opensource)\nimport com.pig4cloud.plugin.excel.annotation.ResponseExcel;\nimport com.pig4cloud.plugin.excel.annotation.RequestExcel;\n#else\nimport ${package}.common.excel.annotation.ResponseExcel;\nimport ${package}.common.excel.annotation.RequestExcel;\n#end\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\nimport ${package}.${moduleName}.entity.${ChildClassName}Entity;\nimport ${package}.${moduleName}.service.${ClassName}Service;\nimport io.swagger.v3.oas.annotations.security.SecurityRequirement;\n#if($isSpringBoot3)\nimport ${package}.common.security.annotation.HasPermission;\nimport org.springdoc.core.annotations.ParameterObject;\n#else\nimport org.springframework.security.access.prepost.PreAuthorize;\nimport org.springdoc.api.annotations.ParameterObject;\n#end\nimport org.springframework.http.HttpHeaders;\nimport io.swagger.v3.oas.annotations.tags.Tag;\nimport io.swagger.v3.oas.annotations.Operation;\nimport lombok.RequiredArgsConstructor;\nimport org.springframework.validation.BindingResult;\nimport org.springframework.web.bind.annotation.*;\n\nimport java.util.List;\nimport java.util.Objects;\n\n/**\n * ${tableComment}\n *\n * @author ${author}\n * @date ${datetime}\n */\n@RestController\n@RequiredArgsConstructor\n@RequestMapping(\"/${functionName}\" )\n@Tag(description = \"${functionName}\" , name = \"${tableComment}管理\" )\n@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)\npublic class ${ClassName}Controller {\n\n    private final  ${ClassName}Service ${className}Service;\n\n    /**\n     * 分页查询\n     * @param page 分页对象\n     * @param ${className} ${tableComment}\n     * @return\n     */\n    @Operation(summary = \"分页查询\" , description = \"分页查询\" )\n    @GetMapping(\"/page\" )\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_view\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_view\')\" )\n    #end\n    public R get${ClassName}Page(@ParameterObject Page page, @ParameterObject ${ClassName}Entity ${className}) {\n        LambdaQueryWrapper<${ClassName}Entity> wrapper = Wrappers.lambdaQuery();\n#foreach ($field in $queryList)\n#set($getAttrName=$str.getProperty($field.attrName))\n#set($var=\"${className}.$getAttrName()\")\n#if($field.attrType == \'String\')\n#set($expression=\"StrUtil.isNotBlank\")\n#else\n#set($expression=\"Objects.nonNull\")\n#end\n#if($field.queryType == \'=\')\n		wrapper.eq($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'like\' )\n		wrapper.like($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'!-\' )\n		wrapper.ne($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'>\' )\n		wrapper.gt($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'<\' )\n		wrapper.lt($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'>=\' )\n		wrapper.ge($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'<=\' )\n		wrapper.le($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'left like\' )\n		wrapper.likeLeft($expression($var),${ClassName}Entity::$getAttrName,$var);\n#elseif( $field.queryType == \'right like\' )\n		wrapper.likeRight($expression($var),${ClassName}Entity::$getAttrName,$var);\n#end\n#end\n        return R.ok(${className}Service.page(page, wrapper));\n    }\n\n\n    /**\n     * 通过条件查询${tableComment}\n     * @param ${className} 查询条件\n     * @return R  对象列表\n     */\n    @Operation(summary = \"通过条件查询\" , description = \"通过条件查询对象\" )\n    @GetMapping(\"/details\" )\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_view\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_view\')\" )\n    #end\n    public R getDetails(@ParameterObject ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.listDeep(Wrappers.query(${className})));\n    }\n\n    /**\n     * 新增${tableComment}\n     * @param ${className} ${tableComment}\n     * @return R\n     */\n    @Operation(summary = \"新增${tableComment}\" , description = \"新增${tableComment}\" )\n    @SysLog(\"新增${tableComment}\" )\n    @PostMapping\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_add\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_add\')\" )\n    #end\n    public R save(@RequestBody ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.saveDeep(${className}));\n    }\n\n    /**\n     * 修改${tableComment}\n     * @param ${className} ${tableComment}\n     * @return R\n     */\n    @Operation(summary = \"修改${tableComment}\" , description = \"修改${tableComment}\" )\n    @SysLog(\"修改${tableComment}\" )\n    @PutMapping\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_edit\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_edit\')\" )\n    #end\n    public R updateById(@RequestBody ${ClassName}Entity ${className}) {\n        return R.ok(${className}Service.updateDeep(${className}));\n    }\n\n    /**\n     * 通过id删除${tableComment}\n     * @param ids ${pk.attrName}列表\n     * @return R\n     */\n    @Operation(summary = \"通过id删除${tableComment}\" , description = \"通过id删除${tableComment}\" )\n    @SysLog(\"通过id删除${tableComment}\" )\n    @DeleteMapping\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_del\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_del\')\" )\n    #end\n    public R removeById(@RequestBody ${pk.attrType}[] ids) {\n        return R.ok(${className}Service.removeDeep(ids));\n    }\n\n    /**\n     * 通过id删除${tableComment}子表数据\n     * @param ids ${pk.attrName}列表\n     * @return R\n     */\n    @Operation(summary = \"通过id删除${tableComment}子表数据\" , description = \"通过id删除${tableComment}子表数据\" )\n    @SysLog(\"通过id删除${tableComment}子表数据\" )\n    @DeleteMapping(\"/child\")\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_del\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_del\')\" )\n    #end\n    public R removeChild(@RequestBody ${pk.attrType}[] ids) {\n        return R.ok(${className}Service.removeChild(ids));\n    }\n\n    /**\n     * 导出excel 表格\n     * @param ${className} 查询条件\n   	 * @param ids 导出指定ID\n     * @return excel 文件流\n     */\n    @ResponseExcel\n    @GetMapping(\"/export\")\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_export\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_export\')\" )\n    #end\n    public List<${ClassName}Entity> exportExcel(${ClassName}Entity ${className},${pk.attrType}[] ids) {\n        return ${className}Service.list(Wrappers.lambdaQuery(${className}).in(ArrayUtil.isNotEmpty(ids), ${ClassName}Entity::$str.getProperty($pk.attrName), ids));\n    }\n\n    /**\n     * 导入excel 表\n     * @param ${className}List 对象实体列表\n     * @param bindingResult 错误信息列表\n     * @return ok fail\n     */\n    @PostMapping(\"/import\")\n    #if($isSpringBoot3)\n    @HasPermission(\"${moduleName}_${functionName}_export\")\n    #else\n    @PreAuthorize(\"@pms.hasPermission(\'${moduleName}_${functionName}_export\')\" )\n    #end\n    public R importExcel(@RequestExcel List<${ClassName}Entity> ${className}List, BindingResult bindingResult) {\n        return R.ok(${className}Service.saveBatch(${className}List));\n    }\n}\n', '2025-06-15 10:46:02', '2025-06-15 10:46:02', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079924450590722, '主子表单V2025001\n', '${frontendPath}/src/views/${moduleName}/${functionName}/form.vue', '主子表单V2025001\n', '<template>\n  <el-drawer :title=\"form.${pk.attrName} ? (detail ? \'详情\' : \'编辑\') : \'添加\'\" v-model=\"visible\" size=\"50%\">\n    <el-form ref=\"dataFormRef\" :model=\"form\" :rules=\"dataRules\" :disabled=\"detail\" v-loading=\"loading\">\n      <el-row :gutter=\"24\">\n#foreach($field in $formList)\n#if($field.attrName != ${pk.attrName})\n#if($formLayout == 1)\n        <el-col :span=\"24\" class=\"mb20\">\n#elseif($formLayout == 2)\n        <el-col :span=\"12\" class=\"mb20\">\n#end\n#if($field.formType == \'text\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <el-input v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\"/>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'textarea\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <el-input type=\"textarea\" v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\"/>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'select\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <el-select v-model=\"form.${field.attrName}\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\">\n#if($field.fieldDict)\n              <el-option :value=\"item.value\" :label=\"item.label\" v-for=\"(item, index) in ${field.fieldDict}\" :key=\"index\"></el-option>\n#else\n              <el-option label=\"请选择\" value=\"0\"></el-option>\n#end\n            </el-select>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'radio\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <el-radio-group v-model=\"form.${field.attrName}\">\n#if($field.fieldDict)\n              <el-radio :label=\"item.value\" v-for=\"(item, index) in ${field.fieldDict}\" border :key=\"index\">{{ item.label }}</el-radio>\n#else\n              <el-radio label=\"${field.fieldComment}\" border>${field.fieldComment}</el-radio>\n#end\n            </el-radio-group>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'checkbox\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <el-checkbox-group v-model=\"form.${field.attrName}\">\n#if($field.fieldDict)\n              <el-checkbox :label=\"item.value\" v-for=\"(item, index) in ${field.fieldDict}\" :key=\"index\">{{ item.label }}</el-checkbox>\n#else\n              <el-checkbox label=\"启用\" name=\"type\"></el-checkbox>\n              <el-checkbox label=\"禁用\" name=\"type\"></el-checkbox>\n#end\n            </el-checkbox-group>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'date\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <el-date-picker type=\"date\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" v-model=\"form.${field.attrName}\" :value-format=\"dateStr\"></el-date-picker>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'datetime\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <el-date-picker type=\"datetime\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" v-model=\"form.${field.attrName}\" :value-format=\"dateTimeStr\"></el-date-picker>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'number\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <el-input-number :min=\"1\" :max=\"1000\" v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\"></el-input-number>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'upload-file\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <upload-file v-model=\"form.${field.attrName}\"></upload-file>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'upload-img\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <upload-img v-model:imageUrl=\"form.${field.attrName}\"></upload-img>\n          </el-form-item>\n        </el-col>\n#elseif($field.formType == \'editor\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <editor v-model:get-html=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\"></editor>\n          </el-form-item>\n        </el-col>\n#else\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <el-input v-model=\"form.${field.attrName}\" placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\"/>\n          </el-form-item>\n        </el-col>\n#end\n#end\n#end\n      </el-row>\n      <el-row :gutter=\"24\">\n        <sc-form-table\n          v-model=\"form.${childClassName}List\"\n          :addTemplate=\"childTemp\"\n          @delete=\"deleteChild\"\n          placeholder=\"暂无数据\"\n        >\n#set($ignoreColumnList = [\"create_by\",\"create_time\",\"update_by\",\"update_time\",\"del_flag\",\"tenant_id\"])\n#foreach($field in $childFieldList)\n#if($field.primaryPk == \'1\')\n#elseif($ignoreColumnList.contains(${field.fieldName}))\n#elseif($field.attrName == $childField)\n#else  \n          <el-table-column label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.fieldName}#end\" prop=\"${field.attrName}\">\n            <template #default=\"{ row, $index }\">\n              <el-form-item :prop=\"`${childClassName}List.${$index}.${field.attrName}`\" :rules=\"[{ required: true, trigger: \'blur\' }]\">\n                <el-input v-model=\"row.${field.attrName}\"/>\n              </el-form-item>\n            </template>\n          </el-table-column>\n#end\n#end\n        </sc-form-table>\n      </el-row>\n    </el-form>\n    <template #footer>\n      <span class=\"dialog-footer\">\n        <el-button @click=\"visible = false\">取 消</el-button>\n        <el-button type=\"primary\" @click=\"onSubmit\" :disabled=\"loading\">确 认</el-button>\n      </span>\n    </template>\n  </el-drawer>\n</template>\n\n<script setup lang=\"ts\" name=\"${ClassName}Dialog\">\n// ========== 1. 导入语句 ==========\nimport { useDict } from \'/@/hooks/dict\';\nimport { rule } from \'/@/utils/validate\';\nimport { useMessage } from \"/@/hooks/message\";\nimport { getObj, addObj, putObj, delChildObj, validateExist } from \'/@/api/${moduleName}/${functionName}\';\n\n// ========== 2. 组件定义 ==========\n// 异步加载表格组件\nconst scFormTable = defineAsyncComponent(() => import(\'/@/components/FormTable/index.vue\'));\n\n// 定义组件事件\nconst emit = defineEmits([\'refresh\']);\n\n// ========== 3. 响应式数据定义 ==========\n// 基础响应式变量\nconst dataFormRef = ref(); // 表单引用\nconst visible = ref(false); // 抽屉显示状态\nconst loading = ref(false); // 加载状态\nconst detail = ref(false); // 是否为详情模式\n\n// 表单数据对象\nconst form = reactive({\n#if(!$formList.contains(${pk.attrName}))\n  ${pk.attrName}: \'\', // 主键\n#end\n#foreach($field in $formList)\n#if($field.formType == \'number\')\n  ${field.attrName}: 0, // ${field.fieldComment}\n#elseif($field.formType == \'checkbox\')\n  ${field.attrName}: [], // ${field.fieldComment}\n#else\n  ${field.attrName}: \'\', // ${field.fieldComment}\n#end\n#end\n  ${childClassName}List: [], // 子表数据列表\n});\n\n// 子表数据模板\nconst childTemp = reactive({\n#foreach($field in $childFieldList)\n  ${field.attrName}: \'\', // ${field.fieldComment}\n#end\n});\n\n// ========== 4. 字典数据处理 ==========\n#set($fieldDict=[])\n#foreach($field in $gridList)\n#if($field.fieldDict)\n#set($void=$fieldDict.add($field.fieldDict))\n#end\n#end\n#if($fieldDict && $fieldDict.size() > 0)\n// 加载字典数据\nconst { $dict.format($fieldDict) } = useDict($dict.quotation($fieldDict));\n#end\n\n// ========== 5. 表单校验规则 ==========\nconst dataRules = ref({\n#foreach($field in $formList)\n#if($field.formRequired == \'1\' && $field.formValidator == \'duplicate\')\n  ${field.attrName}: [\n    { required: true, message: \'${field.fieldComment}不能为空\', trigger: \'blur\' },\n    {\n      validator: (rule: any, value: any, callback: any) => {\n        // 重复性校验（编辑时跳过）\n        validateExist(rule, value, callback, form.${pk.attrName} !== \'\');\n      },\n      trigger: \'blur\',\n    }\n  ],\n#elseif($field.formRequired == \'1\' && $field.formValidator)\n  ${field.attrName}: [\n    { required: true, message: \'${field.fieldComment}不能为空\', trigger: \'blur\' },\n    { validator: rule.${field.formValidator}, trigger: \'blur\' }\n  ],\n#elseif($field.formRequired == \'1\')\n  ${field.attrName}: [\n    { required: true, message: \'${field.fieldComment}不能为空\', trigger: \'blur\' }\n  ],\n#elseif($field.formValidator)\n  ${field.attrName}: [\n    { validator: rule.${field.formValidator}, trigger: \'blur\' }\n  ],\n#end\n#end\n});\n\n// ========== 6. 方法定义 ==========\n// 获取主子表详情数据\nconst get${ClassName}Data = async (id: string) => {\n  try {\n    loading.value = true;\n    const { data } = await getObj({ ${pk.attrName}: id });\n    // 直接将第一条数据赋值给表单\n    Object.assign(form, data[0]);\n  } catch (error) {\n    useMessage().error(\'获取数据失败\');\n  } finally {\n    loading.value = false;\n  }\n};\n\n// 打开抽屉方法\nconst openDialog = (id: string, isDetail: boolean) => {\n  visible.value = true;\n  detail.value = isDetail;\n  form.${pk.attrName} = \'\';\n\n  // 重置表单数据\n  nextTick(() => {\n    dataFormRef.value?.resetFields();\n    form.${childClassName}List = [];\n  });\n\n  // 获取${ClassName}信息\n  if (id) {\n    form.${pk.attrName} = id;\n    get${ClassName}Data(id);\n  }\n};\n\n// 提交表单方法\nconst onSubmit = async () => {\n  loading.value = true; // 防止重复提交\n  \n  // 表单校验\n  const valid = await dataFormRef.value.validate().catch(() => {});\n  if (!valid) {\n    loading.value = false;\n    return false;\n  }\n\n  try {\n    // 根据是否有ID判断是新增还是修改\n    form.${pk.attrName} ? await putObj(form) : await addObj(form);\n    useMessage().success(form.${pk.attrName} ? \'修改成功\' : \'添加成功\');\n    visible.value = false;\n    emit(\'refresh\'); // 通知父组件刷新列表\n  } catch (err: any) {\n    useMessage().error(err.msg);\n  } finally {\n    loading.value = false;\n  }\n};\n\n#foreach ($field in $childFieldList)\n#if($field.primaryPk == \'1\')\n#set($childPkName=$field.attrName)\n#end\n#end\n// 删除子表数据方法\nconst deleteChild = async (obj: { $childPkName: string }) => {\n  if (obj.$childPkName) {\n    try {\n      await delChildObj([obj.$childPkName]);\n      useMessage().success(\'删除成功\');\n    } catch (err: any) {\n      useMessage().error(err.msg);\n    }\n  }\n};\n\n// ========== 7. 对外暴露 ==========\n// 暴露方法给父组件\ndefineExpose({\n  openDialog\n});\n</script>\n', '2025-06-15 10:46:02', '2025-06-15 10:46:02', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079924681277442, '主子表格V2025001\n', '${frontendPath}/src/views/${moduleName}/${functionName}/index.vue', '主子表格V2025001\n', '<template>\n  <div class=\"layout-padding\">\n    <div class=\"layout-padding-auto layout-padding-view\">\n#if($queryList)\n      <el-row v-show=\"showSearch\">\n        <el-form :model=\"state.queryForm\" ref=\"queryRef\" :inline=\"true\" @keyup.enter=\"getDataList\">\n#foreach($field in $queryList)\n#if($field.queryFormType == \'select\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-select v-model=\"state.queryForm.${field.attrName}\" placeholder=\"请选择#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\">\n#if($field.fieldDict)\n              <el-option \n                :label=\"item.label\" \n                :value=\"item.value\" \n                v-for=\"(item, index) in ${field.fieldDict}\" \n                :key=\"index\">\n              </el-option>\n#else\n              <el-option label=\"请选择\">0</el-option>\n#end\n            </el-select>\n          </el-form-item>\n#elseif($field.queryFormType == \'date\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-date-picker \n              type=\"date\" \n              placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" \n              v-model=\"state.queryForm.${field.attrName}\">\n            </el-date-picker>\n          </el-form-item>\n#elseif($field.queryFormType == \'datetime\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-date-picker \n              type=\"datetime\" \n              placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" \n              v-model=\"state.queryForm.${field.attrName}\">\n            </el-date-picker>\n          </el-form-item>\n#elseif($field.formType == \'radio\')\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-radio-group v-model=\"state.queryForm.${field.attrName}\">\n#if($field.fieldDict)\n              <el-radio \n                :label=\"item.value\" \n                v-for=\"(item, index) in ${field.fieldDict}\" \n                border \n                :key=\"index\">\n                {{ item.label }}\n              </el-radio>\n#else\n              <el-radio label=\"${field.fieldComment}\" border>${field.fieldComment}</el-radio>\n#end\n            </el-radio-group>\n          </el-form-item>\n#else\n          <el-form-item label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" prop=\"${field.attrName}\">\n            <el-input \n              placeholder=\"请输入#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" \n              v-model=\"state.queryForm.${field.attrName}\"\n              style=\"max-width: 180px\" \n            />\n          </el-form-item>\n#end\n#end\n          <el-form-item>\n            <el-button icon=\"search\" type=\"primary\" @click=\"getDataList\">\n              查询\n            </el-button>\n            <el-button icon=\"Refresh\" @click=\"resetQuery\">重置</el-button>\n          </el-form-item>\n        </el-form>\n      </el-row>\n#end\n      <el-row>\n        <div class=\"mb8\" style=\"width: 100%\">\n          <el-button \n            icon=\"folder-add\" \n            type=\"primary\" \n            class=\"ml10\" \n            @click=\"formDialogRef.openDialog()\"\n            v-auth=\"\'${moduleName}_${functionName}_add\'\">\n            新增\n          </el-button>\n          <el-button \n            plain \n            :disabled=\"multiple\" \n            icon=\"Delete\" \n            type=\"primary\"\n            v-auth=\"\'${moduleName}_${functionName}_del\'\" \n            @click=\"handleDelete(selectObjs)\">\n            删除\n          </el-button>\n          <right-toolbar \n            v-model:showSearch=\"showSearch\" \n            :export=\"\'${moduleName}_${functionName}_export\'\"\n            @exportExcel=\"exportExcel\" \n            class=\"ml10 mr20\" \n            style=\"float: right;\"\n            @queryTable=\"getDataList\">\n          </right-toolbar>\n        </div>\n      </el-row>\n      <el-table \n        :data=\"state.dataList\" \n        v-loading=\"state.loading\" \n        border \n        :cell-style=\"tableStyle.cellStyle\" \n        :header-cell-style=\"tableStyle.headerCellStyle\"\n        @selection-change=\"selectionChangeHandle\" \n        @sort-change=\"sortChangeHandle\">\n        <el-table-column type=\"selection\" width=\"40\" align=\"center\" />\n        <el-table-column type=\"index\" label=\"#\" width=\"40\" />\n#foreach($field in $gridList)\n#if($field.fieldDict)\n        <el-table-column prop=\"${field.attrName}\" label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" show-overflow-tooltip>\n          <template #default=\"scope\">\n            <dict-tag :options=\"$field.fieldDict\" :value=\"scope.row.${field.attrName}\"></dict-tag>\n          </template>\n        </el-table-column>\n#else\n        <el-table-column \n          prop=\"${field.attrName}\" \n          label=\"#if(${field.fieldComment})${field.fieldComment}#else${field.attrName}#end\" \n#if(${field.gridSort} == \'1\')\n          sortable=\"custom\" \n#end\n          show-overflow-tooltip\n        />\n#end\n#end\n        <el-table-column label=\"操作\" width=\"200\">\n          <template #default=\"scope\">\n#if($ChildClassName)\n            <el-button \n              text \n              type=\"primary\" \n              icon=\"view\" \n              v-auth=\"\'${moduleName}_${functionName}_view\'\" \n              @click=\"formDialogRef.openDialog(scope.row.${pk.attrName}, true)\">\n              详情\n            </el-button>\n#end\n            <el-button \n              icon=\"edit-pen\" \n              text \n              type=\"primary\" \n              v-auth=\"\'${moduleName}_${functionName}_edit\'\"\n              @click=\"formDialogRef.openDialog(scope.row.${pk.attrName})\">\n              编辑\n            </el-button>\n            <el-button \n              icon=\"delete\" \n              text \n              type=\"primary\" \n              v-auth=\"\'${moduleName}_${functionName}_del\'\" \n              @click=\"handleDelete([scope.row.${pk.attrName}])\">\n              删除\n            </el-button>\n          </template>\n        </el-table-column>\n      </el-table>\n      <pagination \n        @size-change=\"sizeChangeHandle\" \n        @current-change=\"currentChangeHandle\" \n        v-bind=\"state.pagination\" \n      />\n    </div>\n\n    <!-- 编辑、新增 -->\n    <form-dialog ref=\"formDialogRef\" @refresh=\"getDataList(false)\" />\n  </div>\n</template>\n\n<script setup lang=\"ts\" name=\"system${ClassName}\">\nimport { BasicTableProps, useTable } from \"/@/hooks/table\";\nimport { fetchList, delObjs } from \"/@/api/${moduleName}/${functionName}\";\nimport { useMessage, useMessageBox } from \"/@/hooks/message\";\nimport { useDict } from \'/@/hooks/dict\';\n\n// 引入组件\nconst FormDialog = defineAsyncComponent(() => import(\'./form.vue\'));\n\n// 定义查询字典\n#set($fieldDict=[])\n#foreach($field in $queryList)\n#if($field.fieldDict)\n#set($void=$fieldDict.add($field.fieldDict))\n#end\n#end\n#foreach($field in $gridList)\n#if($field.fieldDict)\n#set($void=$fieldDict.add($field.fieldDict))\n#end\n#end\n#if($fieldDict)\nconst { $dict.format($fieldDict) } = useDict($dict.quotation($fieldDict));\n#end\n\n// 定义变量内容\nconst formDialogRef = ref();\n// 搜索变量\nconst queryRef = ref();\nconst showSearch = ref(true);\n// 多选变量\nconst selectObjs = ref([]) as any;\nconst multiple = ref(true);\n\nconst state: BasicTableProps = reactive<BasicTableProps>({\n  queryForm: {},\n  pageList: fetchList\n});\n\n// table hook\nconst {\n  getDataList,\n  currentChangeHandle,\n  sizeChangeHandle,\n  sortChangeHandle,\n  downBlobFile,\n  tableStyle\n} = useTable(state);\n\n// 清空搜索条件\nconst resetQuery = () => {\n  // 清空搜索条件\n  queryRef.value?.resetFields();\n  // 清空多选\n  selectObjs.value = [];\n  getDataList();\n};\n\n// 导出excel\nconst exportExcel = () => {\n  downBlobFile(\'/${moduleName}/${functionName}/export\', Object.assign(state.queryForm, { ids: selectObjs }), \'${functionName}.xlsx\');\n};\n\n// 多选事件\nconst selectionChangeHandle = (objs: { $pk.attrName: string }[]) => {\n  selectObjs.value = objs.map(({ $pk.attrName }) => $pk.attrName);\n  multiple.value = !objs.length;\n};\n\n// 删除操作\nconst handleDelete = async (ids: string[]) => {\n  try {\n    await useMessageBox().confirm(\'此操作将永久删除\');\n  } catch {\n    return;\n  }\n\n  try {\n    await delObjs(ids);\n    getDataList();\n    useMessage().success(\'删除成功\');\n  } catch (err: any) {\n    useMessage().error(err.msg);\n  }\n};\n</script>\n', '2025-06-15 10:46:02', '2025-06-15 10:46:02', '0', 1, 'admin', ' ');
INSERT INTO `gen_template` (`id`, `template_name`, `generator_path`, `template_desc`, `template_code`, `create_time`, `update_time`, `del_flag`, `tenant_id`, `create_by`, `update_by`) VALUES (1934079924903575554, '子MapperV2025001\n', '${backendPath}/src/main/java/${packagePath}/${moduleName}/mapper/${ChildClassName}Mapper.java', '子MapperV2025001\n', 'package ${package}.${moduleName}.mapper;\n\nimport ${package}.common.data.datascope.PigxBaseMapper;\nimport ${package}.${moduleName}.entity.${ChildClassName}Entity;\nimport org.apache.ibatis.annotations.Mapper;\n\n@Mapper\npublic interface ${ChildClassName}Mapper extends PigxBaseMapper<${ChildClassName}Entity> {\n\n}\n', '2025-06-15 10:46:02', '2025-06-15 10:46:02', '0', 1, 'admin', ' ');
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
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 1934079920092708866);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 1934079920327589889);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 1934079920554082306);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 1934079920784769026);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 1934079920998678529);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 1934079921220976642);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 1934079921443274753);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 1934079921673961473);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 1934079921900453889);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (1, 1934079922147917825);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079922437324801);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079922659622913);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079922873532418);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079923095830529);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079923313934337);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079923536232449);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079923766919169);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079923989217282);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079924215709698);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079924450590722);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079924681277442);
INSERT INTO `gen_template_group` (`group_id`, `template_id`) VALUES (2, 1934079924903575554);
COMMIT;

-- ----------------------------
-- Table structure for gen_create_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_create_table`;
CREATE TABLE `gen_create_table` (
  `id` bigint NOT NULL COMMENT '主键ID',
  `table_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表名称',
  `ds_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '数据源',
  `comments` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '表注释',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  `column_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字段信息',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '删除标记',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='自动创建表管理';

-- ----------------------------
-- Table structure for app_article
-- ----------------------------
DROP TABLE IF EXISTS `app_article`;
CREATE TABLE `app_article` (
  `id` bigint NOT NULL COMMENT '主键',
  `cid` bigint NOT NULL COMMENT '分类',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `intro` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '简介',
  `summary` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '' COMMENT '摘要',
  `image` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '封面',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '内容',
  `author` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '作者',
  `visit` int unsigned NOT NULL DEFAULT '0' COMMENT '浏览',
  `sort` int unsigned NOT NULL DEFAULT '50' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '删除时间',
    `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `cid_idx` (`cid`) USING BTREE COMMENT '分类索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='文章资讯表';

-- ----------------------------
-- Records of app_article
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for app_article_category
-- ----------------------------
DROP TABLE IF EXISTS `app_article_category`;
CREATE TABLE `app_article_category` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `name` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `sort` smallint unsigned NOT NULL DEFAULT '50' COMMENT '排序',
  `is_show` tinyint unsigned NOT NULL DEFAULT '1' COMMENT '是否显示: 0=否, 1=是',
  `del_flag` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否删除: 0=否, 1=是',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '创建人',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='文章分类表';

-- ----------------------------
-- Records of app_article_category
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for app_article_collect
-- ----------------------------
DROP TABLE IF EXISTS `app_article_collect`;
CREATE TABLE `app_article_collect` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `user_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `article_id` bigint unsigned NOT NULL DEFAULT '0' COMMENT '文章ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '0' COMMENT '是否删除',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
    `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='文章收藏表';

-- ----------------------------
-- Records of app_article_collect
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for app_page
-- ----------------------------
DROP TABLE IF EXISTS `app_page`;
CREATE TABLE `app_page` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `page_type` tinyint unsigned NOT NULL DEFAULT '10' COMMENT '页面类型',
  `page_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '页面名称',
  `page_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '页面数据',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '删除标记',
    `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='页面装修表';

-- ----------------------------
-- Records of app_page
-- ----------------------------
BEGIN;
INSERT INTO `app_page` VALUES (1, 1, '商城首页', '[{\"title\":\"搜索\",\"name\":\"search\",\"disabled\":1,\"content\":{},\"styles\":{}},{\"title\":\"首页轮播图\",\"name\":\"banner\",\"content\":{\"enabled\":1,\"data\":[{\"image\":\"/api/static/banner01.png\",\"name\":\"\",\"link\":{\"path\":\"/pages/index/index\",\"name\":\"商城首页\",\"type\":\"shop\"}}]},\"styles\":{}},{\"title\":\"导航菜单\",\"name\":\"nav\",\"content\":{\"enabled\":1,\"data\":[{\"image\":\"https://minio.pigx.top/oss/app/nav01.png\",\"name\":\"资讯中心\",\"link\":{\"path\":\"/pages/news/news\",\"name\":\"文章资讯\",\"type\":\"shop\"}},{\"image\":\"https://minio.pigx.top/oss/app/nav02.png\",\"name\":\"我的收藏\",\"link\":{\"path\":\"/pages/collection/collection\",\"name\":\"我的收藏\",\"type\":\"shop\"}},{\"image\":\"https://minio.pigx.top/oss/app/nav03.png\",\"name\":\"个人设置\",\"link\":{\"path\":\"/pages/user_set/user_set\",\"name\":\"个人设置\",\"type\":\"shop\"}},{\"image\":\"https://minio.pigx.top/oss/app/nav04.png\",\"name\":\"联系客服\",\"link\":{\"path\":\"/pages/customer_service/customer_service\",\"name\":\"联系客服\",\"type\":\"shop\"}},{\"image\":\"https://minio.pigx.top/oss/app/nav05.png\",\"name\":\"关于我们\",\"link\":{\"path\":\"/pages/as_us/as_us\",\"name\":\"关于我们\",\"type\":\"shop\"}}]},\"styles\":{}},{\"id\":\"l84almsk2uhyf\",\"title\":\"资讯\",\"name\":\"news\",\"disabled\":1,\"content\":{},\"styles\":{}}]', NULL, 'admin', NULL, '2023-06-15 09:18:02', '0',1);
INSERT INTO `app_page` VALUES (2, 2, '个人中心', '[{\"title\":\"用户信息\",\"name\":\"user-info\",\"disabled\":1,\"content\":{},\"styles\":{}},{\"title\":\"我的服务\",\"name\":\"my-service\",\"content\":{\"style\":2,\"title\":\"服务中心\",\"data\":[{\"image\":\"https://minio.pigx.top/oss/app/user_collect.png\",\"name\":\"我的收藏\",\"link\":{\"path\":\"/pages/collection/collection\",\"name\":\"我的收藏\",\"type\":\"shop\"}},{\"image\":\"https://minio.pigx.top/oss/app/user_setting.png\",\"name\":\"个人设置\",\"link\":{\"path\":\"/pages/user_set/user_set\",\"name\":\"个人设置\",\"type\":\"shop\"}},{\"image\":\"https://minio.pigx.top/oss/app/user_kefu.png\",\"name\":\"联系客服\",\"link\":{\"path\":\"/pages/customer_service/customer_service\",\"name\":\"联系客服\",\"type\":\"shop\"}}]},\"styles\":{}},{\"title\":\"个人中心广告图\",\"name\":\"user-banner\",\"content\":{\"enabled\":1,\"data\":[{\"image\":\"\",\"name\":\"sdds\",\"link\":{\"path\":\"/pages/user/user\",\"name\":\"个人中心\",\"type\":\"shop\"}}]},\"styles\":{}}]', NULL, 'admin', NULL, '2023-06-18 17:00:05', '0',1);
INSERT INTO `app_page` VALUES (3, 3, '客服设置', '[{\"title\":\"客服设置\",\"name\":\"customer-service\",\"content\":{\"title\":\"添加客服二维码\",\"time\":\"早上 9:00 - 22:00\",\"mobile\":\"13800138000\",\"qrcode\":\"/admin/sys-file/local/adc5061f99e9440abcd9b22572909c88.jpg\"},\"styles\":{}}]', NULL, 'admin', NULL, '2023-06-14 13:12:19', '0',1);
COMMIT;

-- ----------------------------
-- Table structure for app_tabbar
-- ----------------------------
DROP TABLE IF EXISTS `app_tabbar`;
CREATE TABLE `app_tabbar` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '导航名称',
  `selected` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '未选图标',
  `unselected` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '已选图标',
  `link` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '链接地址',
  `sort_order` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `del_flag` char(1) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '删除标记',
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='底部装修表';

-- ----------------------------
-- Records of app_tabbar
-- ----------------------------
BEGIN;
INSERT INTO `app_tabbar` VALUES (1, '首页', 'https://minio.pigx.top/oss/app/tabbar0.png', 'https://minio.pigx.top/oss/app/tabbar0_0.png', '{\"path\":\"/pages/index/index\",\"name\":\"商城首页\",\"type\":\"shop\"}', 0, NULL, '2023-06-15 09:16:25', NULL, 'admin', '0',1);
INSERT INTO `app_tabbar` VALUES (2, '资讯', 'https://minio.pigx.top/oss/app/tabbar1.png', 'https://minio.pigx.top/oss/app/tabbar1_1.png', '{\"path\":\"/pages/news/news\",\"name\":\"文章资讯\",\"type\":\"shop\"}', 1, NULL, '2023-06-15 09:16:25', NULL, 'admin', '0',1);
INSERT INTO `app_tabbar` VALUES (3, '我的', 'https://minio.pigx.top/oss/app/tabbar3.png', 'https://minio.pigx.top/oss/app/tabbar3_3.png', '{\"path\":\"/pages/user/user\",\"name\":\"个人中心\",\"type\":\"shop\"}',2, NULL, '2023-06-15 09:16:25', NULL, 'admin', '0',1);
COMMIT;

-- ----------------------------
-- Table structure for app_role
-- ----------------------------
DROP TABLE IF EXISTS `app_role`;
CREATE TABLE `app_role` (
  `role_id` bigint(20) NOT NULL,
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0',
  `tenant_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`role_id`) USING BTREE,
  KEY `role_idx1_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='app角色表';

-- ----------------------------
-- Records of app_role
-- ----------------------------
BEGIN;
INSERT INTO `app_role` VALUES (1, 'app用户', 'APP_USER', 'app用户角色', '', '', '2022-12-07 06:34:18', '2023-03-09 06:34:42', '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for app_social_details
-- ----------------------------
DROP TABLE IF EXISTS `app_social_details`;
CREATE TABLE `app_social_details` (
  `id` bigint(20) NOT NULL COMMENT '主键', -- 主键
  `type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '社交类型', -- 社交类型
  `remark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注', -- 备注
  `app_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用ID', -- 应用ID
  `app_secret` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '应用密钥', -- 应用密钥
  `redirect_url` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '重定向URL', -- 重定向URL
  `ext` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '拓展字段', -- 拓展字段
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '创建人', -- 创建人
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人', -- 修改人
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', -- 创建时间
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间', -- 更新时间
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志', -- 删除标志
PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC
COMMENT='系统社交登录账号表'; -- 系统社交登录账号表

-- ----------------------------
-- Records of app_social_details
-- ----------------------------
BEGIN;
INSERT INTO `app_social_details` VALUES (1, 'MINI', '小程序登录', 'app_id', 'app_secret', 'http://www.baidu.com123', NULL, '', 'admin', '2022-12-09 01:44:42', '2023-04-03 06:12:30', '0');
COMMIT;

-- ----------------------------
-- Table structure for app_user
-- ----------------------------
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '盐值',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像图片链接',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '拓展字段:昵称',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '拓展字段:姓名',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '拓展字段:邮箱',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT ' ' COMMENT '修改人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标志',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户id',
  `last_modified_time` datetime DEFAULT NULL COMMENT '最后一次密码修改时间',
  `lock_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '锁定状态',
  `wx_openid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信登录openId',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='app用户表';

-- ----------------------------
-- Records of app_user
-- ----------------------------
BEGIN;
INSERT INTO `app_user` VALUES (1, 'appuser', '$2a$10$XQu3TmORLqDWayFspQN.U.LigJ5TWPTdXPIn/6SxGHKED3PVpuMH6', NULL, '13054729089', NULL, 'aeizzz', '刘洪磊', 'aeizzz@foxmail.com', '', 'appuser', '2022-12-07 02:59:38', '2023-03-09 15:14:44', '0', 1, NULL, '0', 'oBxPy5EnbDiN-gGEaovCpp_IkrkQ');
COMMIT;

-- ----------------------------
-- Table structure for app_user_role
-- ----------------------------
DROP TABLE IF EXISTS `app_user_role`;
CREATE TABLE `app_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
-- Records of app_user_role
-- ----------------------------
BEGIN;
INSERT INTO `app_user_role` VALUES (1, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
