/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : pig

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-11-08 21:34:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  `name` varchar(32) NOT NULL COMMENT '菜单名称',
  `permission` varchar(32) DEFAULT NULL COMMENT '菜单权限标识',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '系统管理', null, null, null, '-1', null, null, null, '0', '2017-11-07 20:56:00', '2017-11-08 20:35:21', '0');
INSERT INTO `sys_menu` VALUES ('2', '用户管理', null, '', null, '1', null, null, null, '0', '2017-11-02 22:24:37', '2017-11-08 20:35:22', '0');
INSERT INTO `sys_menu` VALUES ('3', '菜单管理', null, null, null, '1', null, null, null, '0', '2017-11-08 09:57:27', '2017-11-08 20:35:23', '0');
INSERT INTO `sys_menu` VALUES ('4', '角色管理', null, null, null, '1', null, null, null, '0', '2017-11-08 10:13:37', '2017-11-08 20:35:24', '0');
INSERT INTO `sys_menu` VALUES ('21', '用户查看', null, '/admin/user/*', 'GET', '2', null, null, null, '0', '2017-11-07 20:58:05', '2017-11-08 20:35:26', '0');
INSERT INTO `sys_menu` VALUES ('22', '用户新增', null, '/admin/user/*', 'POST', '2', null, null, null, '1', '2017-11-08 09:52:09', '2017-11-08 20:35:27', '0');
INSERT INTO `sys_menu` VALUES ('23', '用户修改', null, '/admin/user/*', 'PUT', '2', null, null, null, '1', '2017-11-08 09:52:48', '2017-11-08 20:35:28', '0');
INSERT INTO `sys_menu` VALUES ('24', '用户删除', null, '/admin/user/*', 'DELETE', '2', null, null, null, '1', '2017-11-08 09:54:01', '2017-11-08 20:35:29', '0');
INSERT INTO `sys_menu` VALUES ('31', '菜单查看', null, '/admin/menu/*', 'GET', '3', null, null, null, '1', '2017-11-08 09:57:56', '2017-11-08 20:35:29', '0');
INSERT INTO `sys_menu` VALUES ('32', '菜单新增', null, '/admin/menu/*', 'POST', '3', null, null, null, '1', '2017-11-08 10:15:53', '2017-11-08 20:35:30', '0');
INSERT INTO `sys_menu` VALUES ('33', '菜单修改', null, '/admin/menu/*', 'PUT', '3', null, null, null, '1', '2017-11-08 10:16:23', '2017-11-08 20:35:31', '0');
INSERT INTO `sys_menu` VALUES ('34', '菜单删除', null, '/admin/menu/*', 'DELETE', '3', null, null, null, '1', '2017-11-08 10:16:43', '2017-11-08 20:35:31', '0');
INSERT INTO `sys_menu` VALUES ('41', '角色查看', null, '/admin/role/*', 'GET', '4', null, null, null, '1', '2017-11-08 10:14:01', '2017-11-08 20:35:32', '0');
INSERT INTO `sys_menu` VALUES ('42', '角色新增', null, '/admin/role/*', 'POST', '4', null, null, null, '1', '2017-11-08 10:14:18', '2017-11-08 20:35:33', '0');
INSERT INTO `sys_menu` VALUES ('43', '角色修改', null, '/admin/role/*', 'PUT', '4', null, null, null, '1', '2017-11-08 10:14:41', '2017-11-08 20:35:33', '0');
INSERT INTO `sys_menu` VALUES ('44', '角色删除', null, '/admin/role/*', 'DELETE', '4', null, null, null, '1', '2017-11-08 10:14:59', '2017-11-08 20:35:35', '0');

-- ----------------------------
-- Table structure for sys_role
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'admin', 'admin', '多撒DSA', '2017-10-29 15:45:51', '2017-11-06 22:09:10', '0');
INSERT INTO `sys_role` VALUES ('2', 'user', 'user', '普通用户角色', '2017-11-06 22:02:21', '2017-11-06 22:19:53', '1');
INSERT INTO `sys_role` VALUES ('4', 'test', 'test', '测试角色！！！', '2017-11-06 22:03:54', '2017-11-06 22:19:51', '1');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色菜单表';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '1');
INSERT INTO `sys_role_menu` VALUES ('1', '2');
INSERT INTO `sys_role_menu` VALUES ('1', '3');
INSERT INTO `sys_role_menu` VALUES ('1', '21');
INSERT INTO `sys_role_menu` VALUES ('1', '22');
INSERT INTO `sys_role_menu` VALUES ('1', '23');
INSERT INTO `sys_role_menu` VALUES ('1', '24');
INSERT INTO `sys_role_menu` VALUES ('1', '31');
INSERT INTO `sys_role_menu` VALUES ('1', '32');
INSERT INTO `sys_role_menu` VALUES ('1', '33');
INSERT INTO `sys_role_menu` VALUES ('1', '34');
INSERT INTO `sys_role_menu` VALUES ('1', '41');
INSERT INTO `sys_role_menu` VALUES ('1', '42');
INSERT INTO `sys_role_menu` VALUES ('1', '43');
INSERT INTO `sys_role_menu` VALUES ('1', '44');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `salt` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '随机盐',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0-正常，1-删除',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_idx1_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '123456', null, '2017-10-29 15:45:13', '2017-11-06 00:15:05', '0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1');
INSERT INTO `sys_user_role` VALUES ('19', '1');
INSERT INTO `sys_user_role` VALUES ('20', '1');
