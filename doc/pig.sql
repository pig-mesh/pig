/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : pig

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2017-11-17 13:42:21
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '系统管理', null, '/admin', null, null, '-1', null, 'Layout', null, '0', '2017-11-07 20:56:00', '2017-11-14 14:26:03', '0');
INSERT INTO `sys_menu` VALUES ('2', '用户管理', null, 'user', null, null, '1', null, '_import(\'admin/user\')', null, '0', '2017-11-02 22:24:37', '2017-11-14 15:22:40', '0');
INSERT INTO `sys_menu` VALUES ('3', '菜单管理', null, 'menu', null, null, '1', null, '_import(\'admin/menu\')', null, '0', '2017-11-08 09:57:27', '2017-11-14 15:22:45', '0');
INSERT INTO `sys_menu` VALUES ('4', '角色管理', null, 'role', null, null, '1', null, '_import(\'admin/role\')', null, '0', '2017-11-08 10:13:37', '2017-11-14 15:22:51', '0');
INSERT INTO `sys_menu` VALUES ('21', '用户查看', 'sss', null, '/admin/user/*', 'GET', '2', null, null, null, '1', '2017-11-07 20:58:05', '2017-11-14 14:10:16', '0');
INSERT INTO `sys_menu` VALUES ('22', '用户新增', 'userAdd', null, '/admin/user/*', 'POST', '2', null, null, null, '1', '2017-11-08 09:52:09', '2017-11-14 14:10:15', '0');
INSERT INTO `sys_menu` VALUES ('23', '用户修改', 'userUpd', null, '/admin/user/*', 'PUT', '2', null, null, null, '1', '2017-11-08 09:52:48', '2017-11-14 14:10:13', '0');
INSERT INTO `sys_menu` VALUES ('24', '用户删除', 'userDel', null, '/admin/user/*', 'DELETE', '2', null, null, null, '1', '2017-11-08 09:54:01', '2017-11-14 14:10:12', '0');
INSERT INTO `sys_menu` VALUES ('31', '菜单查看', null, null, '/admin/menu/**', 'GET', '3', null, null, null, '1', '2017-11-08 09:57:56', '2017-11-14 17:29:17', '0');
INSERT INTO `sys_menu` VALUES ('32', '菜单新增', null, null, '/admin/menu/*', 'POST', '3', null, null, null, '1', '2017-11-08 10:15:53', '2017-11-14 14:10:10', '0');
INSERT INTO `sys_menu` VALUES ('33', '菜单修改', null, null, '/admin/menu/*', 'PUT', '3', null, null, null, '1', '2017-11-08 10:16:23', '2017-11-14 14:10:08', '0');
INSERT INTO `sys_menu` VALUES ('34', '菜单删除', null, null, '/admin/menu/*', 'DELETE', '3', null, null, null, '1', '2017-11-08 10:16:43', '2017-11-14 14:10:07', '0');
INSERT INTO `sys_menu` VALUES ('41', '角色查看', null, null, '/admin/role/*', 'GET', '4', null, null, null, '1', '2017-11-08 10:14:01', '2017-11-14 14:10:05', '0');
INSERT INTO `sys_menu` VALUES ('42', '角色新增', null, null, '/admin/role/*', 'POST', '4', null, null, null, '1', '2017-11-08 10:14:18', '2017-11-14 14:10:03', '0');
INSERT INTO `sys_menu` VALUES ('43', '角色修改', null, null, '/admin/role/*', 'PUT', '4', null, null, null, '1', '2017-11-08 10:14:41', '2017-11-08 20:35:33', '0');
INSERT INTO `sys_menu` VALUES ('44', '角色删除', null, null, '/admin/role/*', 'DELETE', '4', null, null, null, '1', '2017-11-08 10:14:59', '2017-11-08 20:35:35', '0');

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
INSERT INTO `sys_role` VALUES ('1', 'admin', 'admins', '多撒DSA', '2017-10-29 15:45:51', '2017-11-14 10:59:14', '0');
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
INSERT INTO `sys_role_menu` VALUES ('1', '4');
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
  `introduction` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '简介',
  `avatar` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `del_flag` char(1) COLLATE utf8mb4_bin DEFAULT '0' COMMENT '0-正常，1-删除',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_idx1_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '123456', null, 'sdiuasdsaf', 'https://static.oschina.net/uploads/user/1151/2303656_100.jpg?t=1471420284000', '2017-10-29 15:45:13', '2017-11-11 14:56:21', '0');

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
