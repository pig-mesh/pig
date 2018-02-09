/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.0.111
 Source Server Type    : MySQL
 Source Server Version : 50721
 Source Host           : 192.168.0.111
 Source Database       : pig

 Target Server Type    : MySQL
 Target Server Version : 50721
 File Encoding         : utf-8

 Date: 02/05/2018 23:40:41 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `sys_UserConnection`
-- ----------------------------
DROP TABLE IF EXISTS `sys_UserConnection`;
CREATE TABLE `sys_UserConnection` (
  `userId` varchar(128) CHARACTER SET utf8mb4 NOT NULL,
  `providerId` varchar(128) CHARACTER SET utf8mb4 NOT NULL,
  `providerUserId` varchar(128) CHARACTER SET utf8mb4 NOT NULL,
  `rank` int(11) NOT NULL,
  `displayName` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL,
  `profileUrl` varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL,
  `imageUrl` varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL,
  `accessToken` varchar(512) CHARACTER SET utf8mb4 NOT NULL,
  `secret` varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL,
  `refreshToken` varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL,
  `expireTime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`userId`,`providerId`,`providerUserId`),
  UNIQUE KEY `UserConnectionRank` (`userId`,`providerId`,`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Records of `sys_UserConnection`
-- ----------------------------
BEGIN;
INSERT INTO `sys_UserConnection` VALUES ('B86F3FDF72CD481F4AA094A2345BF446', 'qq', 'B86F3FDF72CD481F4AA094A2345BF446', '1', '冷冷', null, 'http://q.qlogo.cn/qqapp/101322838/B86F3FDF72CD481F4AA094A2345BF446/40', 'E09B51404CDC276F463D05DF1C573514', null, 'B30A1C7C8601259F220807DD2EAA412E', '1524300218147');
COMMIT;

-- ----------------------------
--  Table structure for `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` int(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  `parent_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='部门管理';

-- ----------------------------
--  Records of `sys_dept`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept` VALUES ('1', '山东农信', null, '2018-01-22 19:00:23', '2018-01-23 12:40:46', '0', '0'), ('2', '沙县国际', null, '2018-01-22 19:00:38', '2018-01-23 12:42:04', '0', '0'), ('3', '潍坊农信', null, '2018-01-22 19:00:44', '2018-01-23 12:40:56', '0', '1'), ('4', '高新农信', null, '2018-01-22 19:00:52', '2018-01-23 12:41:11', '0', '3'), ('5', '院校农信', null, '2018-01-22 19:00:57', '2018-01-23 12:41:22', '0', '4'), ('6', '潍坊学院农信', null, '2018-01-22 19:01:06', '2018-01-23 12:41:45', '0', '5'), ('7', '山东沙县', null, '2018-01-22 19:01:57', '2018-01-23 12:42:15', '0', '2'), ('8', '潍坊沙县', null, '2018-01-22 19:02:03', '2018-01-23 12:42:23', '0', '7'), ('9', '高新沙县', null, '2018-01-22 19:02:14', '2018-01-23 12:42:33', '0', '8');
COMMIT;

-- ----------------------------
--  Table structure for `sys_dept_relation`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_relation`;
CREATE TABLE `sys_dept_relation` (
  `ancestor` int(11) NOT NULL COMMENT '祖先节点',
  `descendant` int(11) NOT NULL COMMENT '后代节点',
  PRIMARY KEY (`ancestor`,`descendant`),
  KEY `idx1` (`ancestor`),
  KEY `idx2` (`descendant`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Records of `sys_dept_relation`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dept_relation` VALUES ('1', '1'), ('1', '3'), ('1', '4'), ('1', '5'), ('1', '6'), ('2', '2'), ('2', '7'), ('2', '8'), ('2', '9'), ('3', '3'), ('3', '4'), ('3', '5'), ('3', '6'), ('4', '4'), ('4', '5'), ('4', '6'), ('5', '5'), ('5', '6'), ('6', '6'), ('7', '7'), ('7', '8'), ('7', '9'), ('8', '8'), ('8', '9'), ('9', '9');
COMMIT;

-- ----------------------------
--  Table structure for `sys_dict`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `value` varchar(100) NOT NULL COMMENT '数据值',
  `label` varchar(100) NOT NULL COMMENT '标签名',
  `type` varchar(100) NOT NULL COMMENT '类型',
  `description` varchar(100) NOT NULL COMMENT '描述',
  `sort` decimal(10,0) NOT NULL COMMENT '排序（升序）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `sys_dict_value` (`value`),
  KEY `sys_dict_label` (`label`),
  KEY `sys_dict_del_flag` (`del_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字典表';

-- ----------------------------
--  Records of `sys_dict`
-- ----------------------------
BEGIN;
INSERT INTO `sys_dict` VALUES ('1', '0', '正常', 'log_type', '日志正常', '0', '2017-12-28 17:00:23', '2017-12-28 17:00:23', null, '0'), ('2', '9', '异常', 'log_type', '日志异常', '1', '2017-12-28 13:06:39', '2018-01-06 10:54:41', null, '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_log_0`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_0`;
CREATE TABLE `sys_log_0` (
  `id` bigint(64) NOT NULL COMMENT '编号',
  `type` char(1) DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) DEFAULT '' COMMENT '日志标题',
  `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remote_addr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) DEFAULT NULL COMMENT '操作方式',
  `params` text COMMENT '操作提交的数据',
  `time` mediumtext COMMENT '执行时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记',
  `exception` text COMMENT '异常信息',
  PRIMARY KEY (`id`),
  KEY `sys_log_create_by` (`create_by`),
  KEY `sys_log_request_uri` (`request_uri`),
  KEY `sys_log_type` (`type`),
  KEY `sys_log_create_date` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='日志表';

-- ----------------------------
--  Table structure for `sys_log_1`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_1`;
CREATE TABLE `sys_log_1` (
  `id` bigint(64) NOT NULL COMMENT '编号',
  `type` char(1) DEFAULT '1' COMMENT '日志类型',
  `title` varchar(255) DEFAULT '' COMMENT '日志标题',
  `service_id` varchar(32) DEFAULT NULL COMMENT '服务ID',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remote_addr` varchar(255) DEFAULT NULL COMMENT '操作IP地址',
  `user_agent` varchar(255) DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(255) DEFAULT NULL COMMENT '请求URI',
  `method` varchar(10) DEFAULT NULL COMMENT '操作方式',
  `params` text COMMENT '操作提交的数据',
  `time` mediumtext COMMENT '执行时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标记',
  `exception` text COMMENT '异常信息',
  PRIMARY KEY (`id`),
  KEY `sys_log_create_by` (`create_by`),
  KEY `sys_log_request_uri` (`request_uri`),
  KEY `sys_log_type` (`type`),
  KEY `sys_log_create_date` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='日志表';

-- ----------------------------
--  Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `name` varchar(32) NOT NULL COMMENT '菜单名称',
  `permission` varchar(32) DEFAULT NULL COMMENT '菜单权限标识',
  `path` varchar(128) DEFAULT NULL COMMENT '前端URL',
  `url` varchar(128) DEFAULT NULL COMMENT '请求链接',
  `method` varchar(32) DEFAULT NULL COMMENT '请求方法',
  `parent_id` int(11) DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(32) DEFAULT NULL COMMENT '图标',
  `component` varchar(64) DEFAULT NULL COMMENT 'VUE页面',
  `sort` int(11) DEFAULT NULL COMMENT '排序值',
  `type` char(1) DEFAULT NULL COMMENT '菜单类型 （0菜单 1按钮）',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '0--正常 1--删除',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';

-- ----------------------------
--  Records of `sys_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_menu` VALUES ('1', '系统管理', null, '/admin', null, null, '-1', null, 'Layout', null, '0', '2017-11-07 20:56:00', '2017-11-14 14:26:03', '0'), ('2', '用户管理', null, 'user', null, null, '1', null, '_import(\'admin/user\')', null, '0', '2017-11-02 22:24:37', '2017-11-14 15:22:40', '0'), ('3', '菜单管理', null, 'menu', null, null, '1', null, '_import(\'admin/menu\')', null, '0', '2017-11-08 09:57:27', '2017-11-14 15:22:45', '0'), ('4', '角色管理', null, 'role', null, null, '1', null, '_import(\'admin/role\')', null, '0', '2017-11-08 10:13:37', '2017-11-14 15:22:51', '0'), ('5', '日志管理', null, 'log', null, null, '1', null, '_import(\'admin/log\')', null, '0', '2017-11-20 14:06:22', '2017-11-20 14:14:11', '0'), ('6', '字典管理', null, 'dict', null, null, '1', null, '_import(\'admin/dict\')', null, '0', '2017-11-29 11:30:52', '2017-11-29 11:31:15', '0'), ('7', '部门管理', null, null, null, null, '1', null, '_import(\'admin/dept\')', null, '0', '2018-01-20 13:17:19', null, '0'), ('8', '系统监控', null, null, null, null, '-1', null, '_import(\'admin/monitor\')', null, '1', '2018-01-22 12:30:41', '2018-01-23 10:51:46', '0'), ('9', '服务监控', null, null, null, null, '8', null, '_import(\'admin/monitor/service\')', null, null, '2018-01-23 10:53:33', null, '0'), ('10', 'zipkin监控', null, null, null, null, '8', null, '_import(\'admin/monitor/zipkin\')', null, '0', '2018-01-23 10:55:18', '2018-01-25 11:10:16', '0'), ('11', 'pinpoint监控', null, null, null, null, '8', null, '_import(\'admin/monitor/pinpoint\')', null, '0', '2018-01-25 11:08:52', '2018-01-25 11:11:01', '0'), ('12', '缓存状态', null, null, null, null, '8', null, '_import(\'admin/monitor/cache\')', null, '0', '2018-01-23 10:56:11', null, '0'), ('13', 'ELK状态', null, null, null, null, '8', null, '_import(\'admin/monitor/elk\')', null, '0', '2018-01-23 10:55:47', '2018-01-25 11:11:40', '0'), ('14', '接口文档', null, null, null, null, '8', null, '_import(\'admin/monitor/swagger\')', null, '0', '2018-01-23 10:56:43', '2018-01-25 11:11:45', '0'), ('21', '用户查看', '', null, '/admin/user/**', 'GET', '2', null, null, null, '1', '2017-11-07 20:58:05', '2018-02-04 14:28:49', '0'), ('22', '用户新增', 'sys_user_add', null, '/admin/user/*', 'POST', '2', null, null, null, '1', '2017-11-08 09:52:09', '2017-12-04 16:31:10', '0'), ('23', '用户修改', 'sys_user_upd', null, '/admin/user/**', 'PUT', '2', null, null, null, '1', '2017-11-08 09:52:48', '2018-01-17 17:40:01', '0'), ('24', '用户删除', 'sys_user_del', null, '/admin/user/*', 'DELETE', '2', null, null, null, '1', '2017-11-08 09:54:01', '2017-12-04 16:31:18', '0'), ('31', '菜单查看', null, null, '/admin/menu/**', 'GET', '3', null, null, null, '1', '2017-11-08 09:57:56', '2017-11-14 17:29:17', '0'), ('32', '菜单新增', 'sys_menu_add', null, '/admin/menu/*', 'POST', '3', null, null, null, '1', '2017-11-08 10:15:53', '2018-01-20 14:37:50', '0'), ('33', '菜单修改', 'sys_menu_edit', null, '/admin/menu/*', 'PUT', '3', null, null, null, '1', '2017-11-08 10:16:23', '2018-01-20 14:37:56', '0'), ('34', '菜单删除', 'sys_menu_del', null, '/admin/menu/*', 'DELETE', '3', null, null, null, '1', '2017-11-08 10:16:43', '2018-01-20 14:38:03', '0'), ('41', '角色查看', null, null, '/admin/role/**', 'GET', '4', null, null, null, '1', '2017-11-08 10:14:01', '2018-02-04 13:55:06', '0'), ('42', '角色新增', null, null, '/admin/role/*', 'POST', '4', null, null, null, '1', '2017-11-08 10:14:18', '2017-11-14 14:10:03', '0'), ('43', '角色修改', null, null, '/admin/role/*', 'PUT', '4', null, null, null, '1', '2017-11-08 10:14:41', '2017-11-08 20:35:33', '0'), ('44', '角色删除', null, null, '/admin/role/*', 'DELETE', '4', null, null, null, '1', '2017-11-08 10:14:59', '2017-11-08 20:35:35', '0'), ('51', '日志查看', null, null, '/admin/log/**', 'GET', '5', null, null, null, '1', '2017-11-20 14:07:25', '2018-02-04 14:28:53', '0'), ('52', '日志删除', 'sys_log_del', null, '/admin/log/*', 'DELETE', '5', null, null, null, '1', '2017-11-20 20:37:37', '2017-11-28 17:36:52', '0'), ('61', '字典查看', null, null, '/admin/dict/**', 'GET', '6', null, null, null, '1', '2017-11-19 22:04:24', '2017-11-29 11:31:24', '0'), ('62', '字典删除', 'sys_dict_del', null, '/admin/dict/**', 'DELETE', '6', null, null, null, '1', '2017-11-29 11:30:11', '2018-01-07 15:40:51', '0'), ('71', '部门查看', '', null, '/admin/dept/**', 'GET', '7', null, '', null, '1', '2018-01-20 13:17:19', '2018-01-20 14:55:24', '0'), ('72', '部门新增', 'sys_dept_add', null, '/admin/dept/**', 'POST', '7', null, null, null, '1', '2018-01-20 14:56:16', '2018-01-20 21:17:57', '0'), ('73', '部门修改', 'sys_dept_edit', null, '/admin/dept/**', 'PUT', '7', null, null, null, '1', '2018-01-20 14:56:59', '2018-01-20 21:17:59', '0'), ('74', '部门删除', 'sys_dept_del', null, '/admin/dept/**', 'DELETE', '7', null, null, null, '1', '2018-01-20 14:57:28', '2018-01-20 21:18:05', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `role_code` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `role_desc` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '删除标识（0-正常,1-删除）',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_idx1_role_code` (`role_code`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
--  Records of `sys_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role` VALUES ('1', 'admin', 'ROLE_ADMIN', '超级管理员', '2017-10-29 15:45:51', '2018-01-28 12:46:43', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `role_id` int(20) DEFAULT NULL COMMENT '角色ID',
  `dept_id` int(20) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色与部门对应关系';

-- ----------------------------
--  Records of `sys_role_dept`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_dept` VALUES ('1', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单表';

-- ----------------------------
--  Records of `sys_role_menu`
-- ----------------------------
BEGIN;
INSERT INTO `sys_role_menu` VALUES ('1', '1'), ('1', '2'), ('1', '3'), ('1', '4'), ('1', '5'), ('1', '6'), ('1', '7'), ('1', '8'), ('1', '9'), ('1', '10'), ('1', '11'), ('1', '12'), ('1', '13'), ('1', '14'), ('1', '21'), ('1', '22'), ('1', '23'), ('1', '24'), ('1', '31'), ('1', '32'), ('1', '33'), ('1', '34'), ('1', '41'), ('1', '42'), ('1', '43'), ('1', '44'), ('1', '51'), ('1', '52'), ('1', '61'), ('1', '62'), ('1', '71'), ('1', '72'), ('1', '73'), ('1', '74');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `salt` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '随机盐',
  `introduction` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '简介',
  `avatar` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门ID',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0-正常，1-删除',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_idx1_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
--  Records of `sys_user`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'admin', '$2a$10$gBejTEife2WvJzysrRDXL.MVNCcBsso3adMZWQDsVewCDUaIsIvSK', null, '17034642119', 'http://p0hpm86wj.bkt.clouddn.com/10bd0c7c-03c7-4ad1-8366-b24b4201b969.png', '1', '2017-10-29 15:45:13', '2018-02-03 13:29:28', '0');
COMMIT;

-- ----------------------------
--  Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
--  Records of `sys_user_role`
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_role` VALUES ('1', '1'), ('6', '2'), ('9', '9'), ('10', '2'), ('11', '1'), ('12', '11'), ('13', '1'), ('14', '5');
COMMIT;

-- ----------------------------
--  Table structure for `zipkin_annotations`
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_annotations`;
CREATE TABLE `zipkin_annotations` (
  `trace_id_high` bigint(20) NOT NULL DEFAULT '0' COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.trace_id',
  `span_id` bigint(20) NOT NULL COMMENT 'coincides with zipkin_spans.id',
  `a_key` varchar(255) NOT NULL COMMENT 'BinaryAnnotation.key or Annotation.value if type == -1',
  `a_value` blob COMMENT 'BinaryAnnotation.value(), which must be smaller than 64KB',
  `a_type` int(11) NOT NULL COMMENT 'BinaryAnnotation.type() or -1 if Annotation',
  `a_timestamp` bigint(20) DEFAULT NULL COMMENT 'Used to implement TTL; Annotation.timestamp or zipkin_spans.timestamp',
  `endpoint_ipv4` int(11) DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_ipv6` binary(16) DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null, or no IPv6 address',
  `endpoint_port` smallint(6) DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  `endpoint_service_name` varchar(255) DEFAULT NULL COMMENT 'Null when Binary/Annotation.endpoint is null',
  UNIQUE KEY `trace_id_high` (`trace_id_high`,`trace_id`,`span_id`,`a_key`,`a_timestamp`) COMMENT 'Ignore insert on duplicate',
  UNIQUE KEY `trace_id_high_4` (`trace_id_high`,`trace_id`,`span_id`,`a_key`,`a_timestamp`) COMMENT 'Ignore insert on duplicate',
  KEY `trace_id_high_2` (`trace_id_high`,`trace_id`,`span_id`) COMMENT 'for joining with zipkin_spans',
  KEY `trace_id_high_3` (`trace_id_high`,`trace_id`) COMMENT 'for getTraces/ByIds',
  KEY `endpoint_service_name` (`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames',
  KEY `a_type` (`a_type`) COMMENT 'for getTraces',
  KEY `a_key` (`a_key`) COMMENT 'for getTraces',
  KEY `trace_id` (`trace_id`,`span_id`,`a_key`) COMMENT 'for dependencies job',
  KEY `trace_id_high_5` (`trace_id_high`,`trace_id`,`span_id`) COMMENT 'for joining with zipkin_spans',
  KEY `trace_id_high_6` (`trace_id_high`,`trace_id`) COMMENT 'for getTraces/ByIds',
  KEY `endpoint_service_name_2` (`endpoint_service_name`) COMMENT 'for getTraces and getServiceNames',
  KEY `a_type_2` (`a_type`) COMMENT 'for getTraces',
  KEY `a_key_2` (`a_key`) COMMENT 'for getTraces',
  KEY `trace_id_2` (`trace_id`,`span_id`,`a_key`) COMMENT 'for dependencies job'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

-- ----------------------------
--  Table structure for `zipkin_dependencies`
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_dependencies`;
CREATE TABLE `zipkin_dependencies` (
  `day` date NOT NULL,
  `parent` varchar(255) NOT NULL,
  `child` varchar(255) NOT NULL,
  `call_count` bigint(20) DEFAULT NULL,
  UNIQUE KEY `day` (`day`,`parent`,`child`),
  UNIQUE KEY `day_2` (`day`,`parent`,`child`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

-- ----------------------------
--  Table structure for `zipkin_spans`
-- ----------------------------
DROP TABLE IF EXISTS `zipkin_spans`;
CREATE TABLE `zipkin_spans` (
  `trace_id_high` bigint(20) NOT NULL DEFAULT '0' COMMENT 'If non zero, this means the trace uses 128 bit traceIds instead of 64 bit',
  `trace_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `debug` bit(1) DEFAULT NULL,
  `start_ts` bigint(20) DEFAULT NULL COMMENT 'Span.timestamp(): epoch micros used for endTs query and to implement TTL',
  `duration` bigint(20) DEFAULT NULL COMMENT 'Span.duration(): micros used for minDuration and maxDuration query',
  UNIQUE KEY `trace_id_high` (`trace_id_high`,`trace_id`,`id`) COMMENT 'ignore insert on duplicate',
  UNIQUE KEY `trace_id_high_4` (`trace_id_high`,`trace_id`,`id`) COMMENT 'ignore insert on duplicate',
  KEY `trace_id_high_2` (`trace_id_high`,`trace_id`,`id`) COMMENT 'for joining with zipkin_annotations',
  KEY `trace_id_high_3` (`trace_id_high`,`trace_id`) COMMENT 'for getTracesByIds',
  KEY `name` (`name`) COMMENT 'for getTraces and getSpanNames',
  KEY `start_ts` (`start_ts`) COMMENT 'for getTraces ordering and range',
  KEY `trace_id_high_5` (`trace_id_high`,`trace_id`,`id`) COMMENT 'for joining with zipkin_annotations',
  KEY `trace_id_high_6` (`trace_id_high`,`trace_id`) COMMENT 'for getTracesByIds',
  KEY `name_2` (`name`) COMMENT 'for getTraces and getSpanNames',
  KEY `start_ts_2` (`start_ts`) COMMENT 'for getTraces ordering and range'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPRESSED;

SET FOREIGN_KEY_CHECKS = 1;
