USE pigxx_report_init;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for aj_report_access
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_access`;
CREATE TABLE `aj_report_access` (
  `datetime` date NOT NULL,
  `access` int DEFAULT NULL COMMENT '访问量',
  `register` int DEFAULT NULL COMMENT '注册量',
  PRIMARY KEY (`datetime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of aj_report_access
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_access` VALUES ('2021-06-18', 1000, 12);
INSERT INTO `aj_report_access` VALUES ('2021-06-19', 1200, 20);
INSERT INTO `aj_report_access` VALUES ('2021-06-20', 1600, 40);
INSERT INTO `aj_report_access` VALUES ('2021-06-21', 2000, 100);
INSERT INTO `aj_report_access` VALUES ('2021-06-22', 800, 30);
COMMIT;

-- ----------------------------
-- Table structure for aj_report_barstack
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_barstack`;
CREATE TABLE `aj_report_barstack` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` date DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `nums` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aj_report_barstack
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_barstack` VALUES (1, '2021-07-27', 'A', 12);
INSERT INTO `aj_report_barstack` VALUES (2, '2021-07-27', 'B', 20);
INSERT INTO `aj_report_barstack` VALUES (3, '2021-07-27', 'C', 11);
INSERT INTO `aj_report_barstack` VALUES (4, '2021-07-26', 'A', 11);
INSERT INTO `aj_report_barstack` VALUES (5, '2021-07-26', 'B', 30);
INSERT INTO `aj_report_barstack` VALUES (6, '2021-07-25', 'B', 20);
INSERT INTO `aj_report_barstack` VALUES (7, '2021-07-25', 'C', 15);
COMMIT;

-- ----------------------------
-- Table structure for aj_report_common1
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_common1`;
CREATE TABLE `aj_report_common1` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `nums` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of aj_report_common1
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_common1` VALUES (1, '上海', 500);
INSERT INTO `aj_report_common1` VALUES (2, '北京', 600);
INSERT INTO `aj_report_common1` VALUES (3, '西安', 1000);
INSERT INTO `aj_report_common1` VALUES (4, '河南', 1200);
INSERT INTO `aj_report_common1` VALUES (5, '武汉', 2000);
COMMIT;

-- ----------------------------
-- Table structure for aj_report_common2
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_common2`;
CREATE TABLE `aj_report_common2` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `nums` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aj_report_common2
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_common2` VALUES (6, '上海', 200);
INSERT INTO `aj_report_common2` VALUES (7, '北京', 100);
INSERT INTO `aj_report_common2` VALUES (8, '西安', 70);
INSERT INTO `aj_report_common2` VALUES (9, '河南', 50);
INSERT INTO `aj_report_common2` VALUES (10, '武汉', 20);
COMMIT;

-- ----------------------------
-- Table structure for aj_report_common3
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_common3`;
CREATE TABLE `aj_report_common3` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` varchar(255) DEFAULT NULL,
  `collect` int DEFAULT NULL,
  `start` int DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aj_report_common3
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_common3` VALUES (13, '1月', 10, 8);
INSERT INTO `aj_report_common3` VALUES (14, '2月', 15, 12);
INSERT INTO `aj_report_common3` VALUES (15, '3月', 20, 22);
INSERT INTO `aj_report_common3` VALUES (16, '4月', 30, 28);
INSERT INTO `aj_report_common3` VALUES (17, '5月', 28, 35);
INSERT INTO `aj_report_common3` VALUES (18, '6月', 40, 38);
INSERT INTO `aj_report_common3` VALUES (19, '7月', 80, 100);
INSERT INTO `aj_report_common3` VALUES (20, '8月', 90, 120);
INSERT INTO `aj_report_common3` VALUES (21, '9月', 65, 89);
INSERT INTO `aj_report_common3` VALUES (22, '10月', 50, 50);
INSERT INTO `aj_report_common3` VALUES (23, '11月', 35, 34);
INSERT INTO `aj_report_common3` VALUES (24, '12月', 27, 23);
COMMIT;

-- ----------------------------
-- Table structure for aj_report_comparestack
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_comparestack`;
CREATE TABLE `aj_report_comparestack` (
  `id` int NOT NULL AUTO_INCREMENT,
  `time` date DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `nums` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aj_report_comparestack
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_comparestack` VALUES (1, '2021-08-23', '成功', 12);
INSERT INTO `aj_report_comparestack` VALUES (2, '2021-08-23', '失败', 1);
INSERT INTO `aj_report_comparestack` VALUES (3, '2021-08-24', '成功', 24);
INSERT INTO `aj_report_comparestack` VALUES (4, '2021-08-24', '失败', 5);
INSERT INTO `aj_report_comparestack` VALUES (5, '2021-08-25', '成功', 13);
INSERT INTO `aj_report_comparestack` VALUES (6, '2021-08-25', '失败', 8);
INSERT INTO `aj_report_comparestack` VALUES (7, '2021-08-26', '成功', 19);
INSERT INTO `aj_report_comparestack` VALUES (8, '2021-08-26', '失败', 3);
INSERT INTO `aj_report_comparestack` VALUES (9, '2021-08-27', '成功', 9);
INSERT INTO `aj_report_comparestack` VALUES (10, '2021-08-27', '失败', 15);
COMMIT;

-- ----------------------------
-- Table structure for aj_report_devices
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_devices`;
CREATE TABLE `aj_report_devices` (
  `device_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '设备编号',
  `device_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '设备名称',
  `device_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '设备类型',
  `device_type_code` int DEFAULT NULL COMMENT '设备类型编号',
  `online_time` datetime DEFAULT NULL COMMENT '上线日期',
  `device_state` int DEFAULT NULL COMMENT '1上线，0下线',
  PRIMARY KEY (`device_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of aj_report_devices
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_devices` VALUES ('f00001', '11楼防火墙服务器f01A', '防火墙', 1, '2021-06-23 10:24:16', 1);
INSERT INTO `aj_report_devices` VALUES ('f00002', '11楼防火墙服务器f01B', '防火墙', 1, '2021-06-23 10:24:16', 1);
INSERT INTO `aj_report_devices` VALUES ('j00001', '1楼总交换机j102A', '交换机', 1, '2021-06-23 10:05:10', 1);
INSERT INTO `aj_report_devices` VALUES ('j00002', '1楼分交换机j104B', '交换机', 1, '2021-06-23 10:05:44', 1);
INSERT INTO `aj_report_devices` VALUES ('j00003', '1楼分交换机j106C', '交换机', 1, '2021-06-23 10:06:12', 1);
INSERT INTO `aj_report_devices` VALUES ('j00004', '1楼分交换机j109D', '交换机', 1, '2021-06-23 10:07:21', 1);
INSERT INTO `aj_report_devices` VALUES ('j00005', '2楼总交换机j202A', '交换机', 1, '2021-06-23 10:15:38', 1);
INSERT INTO `aj_report_devices` VALUES ('j00006', '2楼分交换机j204B', '交换机', 1, '2021-06-23 10:15:38', 1);
INSERT INTO `aj_report_devices` VALUES ('j00007', '2楼分交换机j206C', '交换机', 1, '2021-06-23 10:15:38', 1);
INSERT INTO `aj_report_devices` VALUES ('j00008', '2楼分交换机j209D', '交换机', 1, '2021-06-23 10:15:38', 1);
INSERT INTO `aj_report_devices` VALUES ('j00009', '3楼总交换机j302A', '交换机', 1, '2021-06-23 10:15:38', 1);
INSERT INTO `aj_report_devices` VALUES ('j00010', '3楼分交换机j304B', '交换机', 1, '2021-06-23 10:15:38', 1);
INSERT INTO `aj_report_devices` VALUES ('j00011', '4楼总交换机j402A', '交换机', 1, '2021-06-23 10:15:38', 1);
INSERT INTO `aj_report_devices` VALUES ('j00012', '4楼分交换机j409B', '交换机', 1, '2021-06-23 10:15:38', 1);
INSERT INTO `aj_report_devices` VALUES ('s00001', '1楼服务器s101A', '服务器', 1, '2021-06-23 09:55:35', 1);
INSERT INTO `aj_report_devices` VALUES ('s00002', '2楼服务器s201A', '服务器', 1, '2021-06-23 09:59:39', 1);
INSERT INTO `aj_report_devices` VALUES ('s00003', '3楼服务器s301A', '服务器', 1, '2021-06-23 10:00:02', 1);
INSERT INTO `aj_report_devices` VALUES ('s00004', '4楼服务器s401A', '服务器', 1, '2021-06-23 10:00:23', 1);
INSERT INTO `aj_report_devices` VALUES ('s00005', '4楼服务器s401B', '服务器', 1, '2021-06-23 10:01:10', 1);
INSERT INTO `aj_report_devices` VALUES ('s00006', '11楼服务器1101A', '服务器', 1, '2021-06-23 10:09:26', 1);
INSERT INTO `aj_report_devices` VALUES ('s00007', '11楼服务器1101B', '服务器', 1, '2021-06-23 10:09:26', 1);
INSERT INTO `aj_report_devices` VALUES ('s00008', '11楼服务器1101C', '服务器', 1, '2021-06-23 10:09:26', 1);
INSERT INTO `aj_report_devices` VALUES ('s00009', '11楼服务器1101D', '服务器', 1, '2021-06-23 10:09:26', 1);
INSERT INTO `aj_report_devices` VALUES ('s00010', '11楼服务器1101E', '服务器', 1, '2021-06-23 10:09:26', 1);
INSERT INTO `aj_report_devices` VALUES ('w00001', '1楼路由器', '路由器', 1, '2021-06-23 10:21:14', 1);
INSERT INTO `aj_report_devices` VALUES ('w00002', '2楼路由器', '路由器', 1, '2021-06-23 10:21:14', 1);
INSERT INTO `aj_report_devices` VALUES ('w00003', '3楼路由器', '路由器', 1, '2021-06-23 10:21:14', 1);
INSERT INTO `aj_report_devices` VALUES ('w00004', '4楼路由器', '路由器', 1, '2021-06-23 10:21:14', 1);
COMMIT;

-- ----------------------------
-- Table structure for aj_report_exper
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_exper`;
CREATE TABLE `aj_report_exper` (
  `datetime` date NOT NULL,
  `rt` double(11,2) DEFAULT NULL,
  `qps` bigint DEFAULT NULL,
  `error` int DEFAULT NULL,
  PRIMARY KEY (`datetime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of aj_report_exper
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_exper` VALUES ('2021-06-18', 90.92, 9000, 2);
INSERT INTO `aj_report_exper` VALUES ('2021-06-19', 100.02, 10000, 3);
INSERT INTO `aj_report_exper` VALUES ('2021-06-20', 98.89, 9560, 1);
INSERT INTO `aj_report_exper` VALUES ('2021-06-21', 110.99, 13456, 9);
INSERT INTO `aj_report_exper` VALUES ('2021-06-22', 89.78, 8990, 3);
COMMIT;

-- ----------------------------
-- Table structure for aj_report_fireacl
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_fireacl`;
CREATE TABLE `aj_report_fireacl` (
  `id` int NOT NULL AUTO_INCREMENT,
  `acl_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'acl类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of aj_report_fireacl
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_fireacl` VALUES (1, '网页浏览443', '2021-06-23 10:50:41');
INSERT INTO `aj_report_fireacl` VALUES (2, '网页浏览443', '2021-06-23 10:51:11');
INSERT INTO `aj_report_fireacl` VALUES (3, '网页浏览443', '2021-06-23 10:51:47');
INSERT INTO `aj_report_fireacl` VALUES (4, '网页浏览443', '2021-06-23 10:51:49');
INSERT INTO `aj_report_fireacl` VALUES (5, '网页浏览443', '2021-06-23 10:51:52');
INSERT INTO `aj_report_fireacl` VALUES (6, '网页浏览443', '2021-06-23 10:51:55');
INSERT INTO `aj_report_fireacl` VALUES (7, '网页浏览443', '2021-06-23 10:51:57');
INSERT INTO `aj_report_fireacl` VALUES (8, '网页浏览443', '2021-06-23 10:52:28');
INSERT INTO `aj_report_fireacl` VALUES (9, '网页浏览443', '2021-06-23 10:52:31');
INSERT INTO `aj_report_fireacl` VALUES (10, '网页浏览443', '2021-06-23 10:52:33');
INSERT INTO `aj_report_fireacl` VALUES (11, '网页浏览443', '2021-06-23 10:52:36');
INSERT INTO `aj_report_fireacl` VALUES (12, '网页浏览443', '2021-06-23 10:52:38');
INSERT INTO `aj_report_fireacl` VALUES (13, '网页浏览443', '2021-06-23 10:52:39');
INSERT INTO `aj_report_fireacl` VALUES (14, '网页浏览443', '2021-06-23 10:52:42');
INSERT INTO `aj_report_fireacl` VALUES (15, '网页浏览443', '2021-06-23 10:52:44');
INSERT INTO `aj_report_fireacl` VALUES (16, '网页浏览443', '2021-06-23 10:52:46');
INSERT INTO `aj_report_fireacl` VALUES (17, '网页浏览443', '2021-06-23 10:52:48');
INSERT INTO `aj_report_fireacl` VALUES (18, '网页浏览443', '2021-06-23 10:54:33');
INSERT INTO `aj_report_fireacl` VALUES (19, '网页浏览443', '2021-06-23 10:54:41');
INSERT INTO `aj_report_fireacl` VALUES (20, '网页浏览443', '2021-06-23 10:54:45');
INSERT INTO `aj_report_fireacl` VALUES (21, '网页浏览443', '2021-06-23 10:54:46');
INSERT INTO `aj_report_fireacl` VALUES (22, '网页浏览443', '2021-06-23 10:54:46');
INSERT INTO `aj_report_fireacl` VALUES (23, '网页浏览443', '2021-06-23 10:54:47');
INSERT INTO `aj_report_fireacl` VALUES (24, '网页浏览443', '2021-06-23 10:54:48');
INSERT INTO `aj_report_fireacl` VALUES (25, '网页浏览443', '2021-06-23 10:54:51');
INSERT INTO `aj_report_fireacl` VALUES (26, '网页浏览443', '2021-06-23 10:54:54');
INSERT INTO `aj_report_fireacl` VALUES (27, '共享文件445', '2021-06-23 10:57:14');
INSERT INTO `aj_report_fireacl` VALUES (28, 'LDAP389', '2021-06-23 10:57:14');
INSERT INTO `aj_report_fireacl` VALUES (29, '共享文件445', '2021-06-23 10:57:20');
INSERT INTO `aj_report_fireacl` VALUES (30, 'LDAP389', '2021-06-23 10:57:20');
INSERT INTO `aj_report_fireacl` VALUES (31, 'LDAP389', '2021-06-23 10:57:24');
INSERT INTO `aj_report_fireacl` VALUES (32, '网页浏览443', '2021-06-23 10:57:27');
INSERT INTO `aj_report_fireacl` VALUES (33, '共享文件445', '2021-06-23 10:57:27');
INSERT INTO `aj_report_fireacl` VALUES (34, 'LDAP389', '2021-06-23 10:57:27');
INSERT INTO `aj_report_fireacl` VALUES (35, 'LDAP389', '2021-06-23 10:57:30');
INSERT INTO `aj_report_fireacl` VALUES (36, '网页浏览443', '2021-06-23 10:57:53');
INSERT INTO `aj_report_fireacl` VALUES (37, '共享文件445', '2021-06-23 10:57:53');
INSERT INTO `aj_report_fireacl` VALUES (38, 'LDAP389', '2021-06-23 10:57:53');
INSERT INTO `aj_report_fireacl` VALUES (39, 'Server1433', '2021-06-23 10:57:53');
INSERT INTO `aj_report_fireacl` VALUES (40, '存储514', '2021-06-23 10:57:53');
INSERT INTO `aj_report_fireacl` VALUES (41, '网页浏览443', '2021-06-23 10:57:56');
INSERT INTO `aj_report_fireacl` VALUES (42, '共享文件445', '2021-06-23 10:57:56');
INSERT INTO `aj_report_fireacl` VALUES (43, 'LDAP389', '2021-06-23 10:57:56');
INSERT INTO `aj_report_fireacl` VALUES (44, 'Server1433', '2021-06-23 10:57:56');
INSERT INTO `aj_report_fireacl` VALUES (45, '存储514', '2021-06-23 10:57:56');
INSERT INTO `aj_report_fireacl` VALUES (46, '远程桌面3389', '2021-06-23 10:57:56');
INSERT INTO `aj_report_fireacl` VALUES (47, '远程登陆23', '2021-06-23 10:57:56');
INSERT INTO `aj_report_fireacl` VALUES (48, 'LDAP389', '2021-06-23 10:58:29');
INSERT INTO `aj_report_fireacl` VALUES (49, 'Server1433', '2021-06-23 10:58:29');
INSERT INTO `aj_report_fireacl` VALUES (50, '存储514', '2021-06-23 10:58:29');
INSERT INTO `aj_report_fireacl` VALUES (51, '远程桌面3389', '2021-06-23 10:58:32');
INSERT INTO `aj_report_fireacl` VALUES (52, '远程登陆23', '2021-06-23 10:58:32');
INSERT INTO `aj_report_fireacl` VALUES (53, 'LDAP389', '2021-06-23 10:58:51');
INSERT INTO `aj_report_fireacl` VALUES (54, 'Server1433', '2021-06-23 10:58:51');
INSERT INTO `aj_report_fireacl` VALUES (55, '存储514', '2021-06-23 10:58:51');
INSERT INTO `aj_report_fireacl` VALUES (56, '远程桌面3389', '2021-06-23 10:58:51');
INSERT INTO `aj_report_fireacl` VALUES (57, '网页浏览443', '2021-06-23 10:58:54');
INSERT INTO `aj_report_fireacl` VALUES (58, '共享文件445', '2021-06-23 10:58:54');
INSERT INTO `aj_report_fireacl` VALUES (59, 'LDAP389', '2021-06-23 10:58:54');
INSERT INTO `aj_report_fireacl` VALUES (60, 'Server1433', '2021-06-23 10:58:54');
INSERT INTO `aj_report_fireacl` VALUES (61, '存储514', '2021-06-23 10:58:57');
INSERT INTO `aj_report_fireacl` VALUES (62, '远程桌面3389', '2021-06-23 10:58:57');
INSERT INTO `aj_report_fireacl` VALUES (63, '远程登陆23', '2021-06-23 10:58:57');
INSERT INTO `aj_report_fireacl` VALUES (64, 'LDAP389', '2021-06-23 10:59:01');
INSERT INTO `aj_report_fireacl` VALUES (65, 'Server1433', '2021-06-23 10:59:01');
INSERT INTO `aj_report_fireacl` VALUES (66, '存储514', '2021-06-23 10:59:01');
INSERT INTO `aj_report_fireacl` VALUES (67, '网页浏览443', '2021-06-23 10:59:05');
INSERT INTO `aj_report_fireacl` VALUES (68, '共享文件445', '2021-06-23 10:59:05');
INSERT INTO `aj_report_fireacl` VALUES (69, 'LDAP389', '2021-06-23 10:59:05');
INSERT INTO `aj_report_fireacl` VALUES (70, 'Server1433', '2021-06-23 10:59:05');
INSERT INTO `aj_report_fireacl` VALUES (71, '存储514', '2021-06-23 10:59:05');
INSERT INTO `aj_report_fireacl` VALUES (72, '远程桌面3389', '2021-06-23 10:59:05');
INSERT INTO `aj_report_fireacl` VALUES (73, '远程桌面3389', '2021-06-23 10:59:08');
INSERT INTO `aj_report_fireacl` VALUES (74, '远程登陆23', '2021-06-23 10:59:08');
INSERT INTO `aj_report_fireacl` VALUES (75, '远程桌面3389', '2021-06-23 10:59:08');
INSERT INTO `aj_report_fireacl` VALUES (76, '远程登陆23', '2021-06-23 10:59:08');
INSERT INTO `aj_report_fireacl` VALUES (77, '远程桌面3389', '2021-06-23 10:59:09');
INSERT INTO `aj_report_fireacl` VALUES (78, '远程登陆23', '2021-06-23 10:59:09');
INSERT INTO `aj_report_fireacl` VALUES (79, '远程桌面3389', '2021-06-23 10:59:09');
INSERT INTO `aj_report_fireacl` VALUES (80, '远程登陆23', '2021-06-23 10:59:09');
INSERT INTO `aj_report_fireacl` VALUES (81, '远程桌面3389', '2021-06-23 10:59:09');
INSERT INTO `aj_report_fireacl` VALUES (82, '远程登陆23', '2021-06-23 10:59:09');
INSERT INTO `aj_report_fireacl` VALUES (83, '远程桌面3389', '2021-06-23 10:59:09');
INSERT INTO `aj_report_fireacl` VALUES (84, '远程登陆23', '2021-06-23 10:59:09');
INSERT INTO `aj_report_fireacl` VALUES (85, '远程桌面3389', '2021-06-23 10:59:10');
INSERT INTO `aj_report_fireacl` VALUES (86, '远程登陆23', '2021-06-23 10:59:10');
INSERT INTO `aj_report_fireacl` VALUES (87, '远程桌面3389', '2021-06-23 10:59:10');
INSERT INTO `aj_report_fireacl` VALUES (88, '远程登陆23', '2021-06-23 10:59:10');
INSERT INTO `aj_report_fireacl` VALUES (89, '远程桌面3389', '2021-06-23 10:59:10');
INSERT INTO `aj_report_fireacl` VALUES (90, '远程登陆23', '2021-06-23 10:59:10');
INSERT INTO `aj_report_fireacl` VALUES (91, '远程桌面3389', '2021-06-23 10:59:10');
INSERT INTO `aj_report_fireacl` VALUES (92, '远程登陆23', '2021-06-23 10:59:10');
INSERT INTO `aj_report_fireacl` VALUES (93, '远程桌面3389', '2021-06-23 10:59:11');
INSERT INTO `aj_report_fireacl` VALUES (94, '远程登陆23', '2021-06-23 10:59:11');
INSERT INTO `aj_report_fireacl` VALUES (95, '远程桌面3389', '2021-06-23 10:59:11');
INSERT INTO `aj_report_fireacl` VALUES (96, '远程登陆23', '2021-06-23 10:59:11');
INSERT INTO `aj_report_fireacl` VALUES (97, '远程桌面3389', '2021-06-23 10:59:11');
INSERT INTO `aj_report_fireacl` VALUES (98, '远程登陆23', '2021-06-23 10:59:11');
INSERT INTO `aj_report_fireacl` VALUES (99, '远程桌面3389', '2021-06-23 10:59:11');
INSERT INTO `aj_report_fireacl` VALUES (100, '远程登陆23', '2021-06-23 10:59:11');
INSERT INTO `aj_report_fireacl` VALUES (101, '远程桌面3389', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (102, '远程登陆23', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (103, '远程桌面3389', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (104, '远程登陆23', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (105, '远程桌面3389', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (106, '远程登陆23', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (107, '远程桌面3389', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (108, '远程登陆23', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (109, '远程桌面3389', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (110, '远程登陆23', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (111, '远程桌面3389', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (112, '远程登陆23', '2021-06-23 10:59:12');
INSERT INTO `aj_report_fireacl` VALUES (113, '远程桌面3389', '2021-06-23 10:59:13');
INSERT INTO `aj_report_fireacl` VALUES (114, '远程登陆23', '2021-06-23 10:59:13');
INSERT INTO `aj_report_fireacl` VALUES (115, '远程桌面3389', '2021-06-23 10:59:13');
INSERT INTO `aj_report_fireacl` VALUES (116, '远程登陆23', '2021-06-23 10:59:13');
INSERT INTO `aj_report_fireacl` VALUES (117, '远程桌面3389', '2021-06-23 10:59:13');
INSERT INTO `aj_report_fireacl` VALUES (118, '远程登陆23', '2021-06-23 10:59:13');
INSERT INTO `aj_report_fireacl` VALUES (119, '远程桌面3389', '2021-06-23 10:59:14');
INSERT INTO `aj_report_fireacl` VALUES (120, '远程登陆23', '2021-06-23 10:59:14');
INSERT INTO `aj_report_fireacl` VALUES (121, '远程桌面3389', '2021-06-23 10:59:14');
INSERT INTO `aj_report_fireacl` VALUES (122, '远程登陆23', '2021-06-23 10:59:14');
INSERT INTO `aj_report_fireacl` VALUES (123, '远程桌面3389', '2021-06-23 10:59:14');
INSERT INTO `aj_report_fireacl` VALUES (124, '远程登陆23', '2021-06-23 10:59:14');
INSERT INTO `aj_report_fireacl` VALUES (125, '远程桌面3389', '2021-06-23 10:59:14');
INSERT INTO `aj_report_fireacl` VALUES (126, '远程登陆23', '2021-06-23 10:59:14');
INSERT INTO `aj_report_fireacl` VALUES (127, '远程桌面3389', '2021-06-23 10:59:15');
INSERT INTO `aj_report_fireacl` VALUES (128, '远程登陆23', '2021-06-23 10:59:15');
INSERT INTO `aj_report_fireacl` VALUES (129, '远程桌面3389', '2021-06-23 10:59:15');
INSERT INTO `aj_report_fireacl` VALUES (130, '远程登陆23', '2021-06-23 10:59:15');
INSERT INTO `aj_report_fireacl` VALUES (131, '远程桌面3389', '2021-06-23 10:59:15');
INSERT INTO `aj_report_fireacl` VALUES (132, '远程登陆23', '2021-06-23 10:59:15');
INSERT INTO `aj_report_fireacl` VALUES (133, '远程桌面3389', '2021-06-23 10:59:16');
INSERT INTO `aj_report_fireacl` VALUES (134, '远程登陆23', '2021-06-23 10:59:16');
INSERT INTO `aj_report_fireacl` VALUES (135, '远程桌面3389', '2021-06-23 10:59:16');
INSERT INTO `aj_report_fireacl` VALUES (136, '远程登陆23', '2021-06-23 10:59:16');
INSERT INTO `aj_report_fireacl` VALUES (137, '远程桌面3389', '2021-06-23 10:59:17');
INSERT INTO `aj_report_fireacl` VALUES (138, '远程登陆23', '2021-06-23 10:59:17');
INSERT INTO `aj_report_fireacl` VALUES (139, '远程桌面3389', '2021-06-23 10:59:17');
INSERT INTO `aj_report_fireacl` VALUES (140, '远程登陆23', '2021-06-23 10:59:17');
INSERT INTO `aj_report_fireacl` VALUES (141, '远程桌面3389', '2021-06-23 10:59:17');
INSERT INTO `aj_report_fireacl` VALUES (142, '远程登陆23', '2021-06-23 10:59:17');
INSERT INTO `aj_report_fireacl` VALUES (143, '远程桌面3389', '2021-06-23 10:59:18');
INSERT INTO `aj_report_fireacl` VALUES (144, '远程登陆23', '2021-06-23 10:59:18');
INSERT INTO `aj_report_fireacl` VALUES (145, '远程桌面3389', '2021-06-23 10:59:18');
INSERT INTO `aj_report_fireacl` VALUES (146, '远程登陆23', '2021-06-23 10:59:18');
INSERT INTO `aj_report_fireacl` VALUES (147, '远程桌面3389', '2021-06-23 10:59:18');
INSERT INTO `aj_report_fireacl` VALUES (148, '远程登陆23', '2021-06-23 10:59:18');
INSERT INTO `aj_report_fireacl` VALUES (149, '远程桌面3389', '2021-06-23 10:59:18');
INSERT INTO `aj_report_fireacl` VALUES (150, '远程登陆23', '2021-06-23 10:59:18');
INSERT INTO `aj_report_fireacl` VALUES (151, '远程桌面3389', '2021-06-23 10:59:19');
INSERT INTO `aj_report_fireacl` VALUES (152, '远程登陆23', '2021-06-23 10:59:19');
INSERT INTO `aj_report_fireacl` VALUES (153, '远程桌面3389', '2021-06-23 10:59:19');
INSERT INTO `aj_report_fireacl` VALUES (154, '远程登陆23', '2021-06-23 10:59:19');
INSERT INTO `aj_report_fireacl` VALUES (155, '远程桌面3389', '2021-06-23 10:59:19');
INSERT INTO `aj_report_fireacl` VALUES (156, '远程登陆23', '2021-06-23 10:59:19');
INSERT INTO `aj_report_fireacl` VALUES (157, '远程桌面3389', '2021-06-23 10:59:19');
INSERT INTO `aj_report_fireacl` VALUES (158, '远程登陆23', '2021-06-23 10:59:19');
INSERT INTO `aj_report_fireacl` VALUES (159, '远程桌面3389', '2021-06-23 10:59:19');
INSERT INTO `aj_report_fireacl` VALUES (160, '远程登陆23', '2021-06-23 10:59:19');
INSERT INTO `aj_report_fireacl` VALUES (161, '存储514', '2021-06-23 10:59:22');
INSERT INTO `aj_report_fireacl` VALUES (162, '远程桌面3389', '2021-06-23 10:59:22');
INSERT INTO `aj_report_fireacl` VALUES (163, '远程登陆23', '2021-06-23 10:59:22');
INSERT INTO `aj_report_fireacl` VALUES (164, 'Server1433', '2021-06-23 10:59:24');
INSERT INTO `aj_report_fireacl` VALUES (165, '存储514', '2021-06-23 10:59:24');
INSERT INTO `aj_report_fireacl` VALUES (166, '远程桌面3389', '2021-06-23 10:59:24');
INSERT INTO `aj_report_fireacl` VALUES (167, '远程登陆23', '2021-06-23 10:59:28');
INSERT INTO `aj_report_fireacl` VALUES (168, '远程登陆23', '2021-06-23 10:59:29');
INSERT INTO `aj_report_fireacl` VALUES (169, '远程登陆23', '2021-06-23 10:59:29');
INSERT INTO `aj_report_fireacl` VALUES (170, '远程登陆23', '2021-06-23 10:59:29');
INSERT INTO `aj_report_fireacl` VALUES (171, '远程登陆23', '2021-06-23 10:59:29');
INSERT INTO `aj_report_fireacl` VALUES (172, '远程登陆23', '2021-06-23 10:59:29');
INSERT INTO `aj_report_fireacl` VALUES (173, '远程登陆23', '2021-06-23 10:59:30');
INSERT INTO `aj_report_fireacl` VALUES (174, '远程登陆23', '2021-06-23 10:59:30');
INSERT INTO `aj_report_fireacl` VALUES (175, '存储514', '2021-06-23 10:59:33');
INSERT INTO `aj_report_fireacl` VALUES (176, '远程桌面3389', '2021-06-23 10:59:33');
INSERT INTO `aj_report_fireacl` VALUES (177, 'LDAP389', '2021-06-23 10:59:36');
INSERT INTO `aj_report_fireacl` VALUES (178, 'Server1433', '2021-06-23 10:59:36');
INSERT INTO `aj_report_fireacl` VALUES (179, '存储514', '2021-06-23 10:59:39');
INSERT INTO `aj_report_fireacl` VALUES (180, '远程桌面3389', '2021-06-23 10:59:39');
INSERT INTO `aj_report_fireacl` VALUES (181, '远程登陆23', '2021-06-23 10:59:39');
INSERT INTO `aj_report_fireacl` VALUES (182, '存储514', '2021-06-23 10:59:40');
INSERT INTO `aj_report_fireacl` VALUES (183, '远程桌面3389', '2021-06-23 10:59:40');
INSERT INTO `aj_report_fireacl` VALUES (184, '远程登陆23', '2021-06-23 10:59:40');
INSERT INTO `aj_report_fireacl` VALUES (185, '存储514', '2021-06-23 10:59:40');
INSERT INTO `aj_report_fireacl` VALUES (186, '远程桌面3389', '2021-06-23 10:59:40');
INSERT INTO `aj_report_fireacl` VALUES (187, '远程登陆23', '2021-06-23 10:59:40');
INSERT INTO `aj_report_fireacl` VALUES (188, '存储514', '2021-06-23 10:59:40');
INSERT INTO `aj_report_fireacl` VALUES (189, '远程桌面3389', '2021-06-23 10:59:40');
INSERT INTO `aj_report_fireacl` VALUES (190, '远程登陆23', '2021-06-23 10:59:40');
INSERT INTO `aj_report_fireacl` VALUES (191, '远程桌面3389', '2021-06-23 10:59:43');
INSERT INTO `aj_report_fireacl` VALUES (192, '远程登陆23', '2021-06-23 10:59:43');
INSERT INTO `aj_report_fireacl` VALUES (193, '远程桌面3389', '2021-06-23 10:59:43');
INSERT INTO `aj_report_fireacl` VALUES (194, '远程登陆23', '2021-06-23 10:59:43');
INSERT INTO `aj_report_fireacl` VALUES (195, '远程桌面3389', '2021-06-23 10:59:44');
INSERT INTO `aj_report_fireacl` VALUES (196, '远程登陆23', '2021-06-23 10:59:44');
INSERT INTO `aj_report_fireacl` VALUES (197, '远程桌面3389', '2021-06-23 10:59:44');
INSERT INTO `aj_report_fireacl` VALUES (198, '远程登陆23', '2021-06-23 10:59:44');
INSERT INTO `aj_report_fireacl` VALUES (199, '远程桌面3389', '2021-06-23 10:59:44');
INSERT INTO `aj_report_fireacl` VALUES (200, '远程登陆23', '2021-06-23 10:59:44');
INSERT INTO `aj_report_fireacl` VALUES (201, '远程桌面3389', '2021-06-23 10:59:44');
INSERT INTO `aj_report_fireacl` VALUES (202, '远程登陆23', '2021-06-23 10:59:44');
COMMIT;

-- ----------------------------
-- Table structure for aj_report_fireattack
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_fireattack`;
CREATE TABLE `aj_report_fireattack` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '事件编号',
  `attack_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '攻击类型',
  `create_time` datetime DEFAULT NULL COMMENT '事件创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of aj_report_fireattack
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_fireattack` VALUES (1, '僵尸网络', '2021-06-23 13:15:37');
INSERT INTO `aj_report_fireattack` VALUES (2, '僵尸网络', '2021-06-23 13:34:14');
INSERT INTO `aj_report_fireattack` VALUES (3, '超大ICMP', '2021-06-23 13:34:14');
INSERT INTO `aj_report_fireattack` VALUES (4, '端口扫描', '2021-06-23 13:34:14');
INSERT INTO `aj_report_fireattack` VALUES (5, '网站扫描', '2021-06-23 13:34:14');
INSERT INTO `aj_report_fireattack` VALUES (6, 'UDP', '2021-06-23 13:34:14');
INSERT INTO `aj_report_fireattack` VALUES (7, 'WEB', '2021-06-23 13:34:14');
INSERT INTO `aj_report_fireattack` VALUES (8, 'SQL注入', '2021-06-23 13:34:14');
INSERT INTO `aj_report_fireattack` VALUES (9, '弱口令', '2021-06-23 13:34:14');
INSERT INTO `aj_report_fireattack` VALUES (10, '僵尸网络', '2021-06-23 13:39:27');
INSERT INTO `aj_report_fireattack` VALUES (11, '超大ICMP', '2021-06-23 13:39:27');
INSERT INTO `aj_report_fireattack` VALUES (12, 'UDP', '2021-06-23 13:39:33');
INSERT INTO `aj_report_fireattack` VALUES (13, 'WEB', '2021-06-23 13:39:33');
INSERT INTO `aj_report_fireattack` VALUES (14, 'SQL注入', '2021-06-23 13:39:33');
INSERT INTO `aj_report_fireattack` VALUES (15, '弱口令', '2021-06-23 13:39:33');
INSERT INTO `aj_report_fireattack` VALUES (16, '僵尸网络', '2021-06-23 13:39:38');
INSERT INTO `aj_report_fireattack` VALUES (17, '超大ICMP', '2021-06-23 13:39:38');
INSERT INTO `aj_report_fireattack` VALUES (18, '端口扫描', '2021-06-23 13:39:38');
INSERT INTO `aj_report_fireattack` VALUES (19, '网站扫描', '2021-06-23 13:39:38');
INSERT INTO `aj_report_fireattack` VALUES (20, 'UDP', '2021-06-23 13:39:38');
INSERT INTO `aj_report_fireattack` VALUES (21, 'WEB', '2021-06-23 13:39:38');
INSERT INTO `aj_report_fireattack` VALUES (22, 'SQL注入', '2021-06-23 13:39:38');
INSERT INTO `aj_report_fireattack` VALUES (23, '僵尸网络', '2021-06-23 13:39:41');
INSERT INTO `aj_report_fireattack` VALUES (24, '超大ICMP', '2021-06-23 13:39:41');
INSERT INTO `aj_report_fireattack` VALUES (25, '端口扫描', '2021-06-23 13:39:41');
INSERT INTO `aj_report_fireattack` VALUES (26, '僵尸网络', '2021-06-23 13:39:42');
INSERT INTO `aj_report_fireattack` VALUES (27, '超大ICMP', '2021-06-23 13:39:42');
INSERT INTO `aj_report_fireattack` VALUES (28, '端口扫描', '2021-06-23 13:39:42');
INSERT INTO `aj_report_fireattack` VALUES (29, '僵尸网络', '2021-06-23 13:39:42');
INSERT INTO `aj_report_fireattack` VALUES (30, '超大ICMP', '2021-06-23 13:39:42');
INSERT INTO `aj_report_fireattack` VALUES (31, '端口扫描', '2021-06-23 13:39:42');
INSERT INTO `aj_report_fireattack` VALUES (32, '僵尸网络', '2021-06-23 13:39:43');
INSERT INTO `aj_report_fireattack` VALUES (33, '超大ICMP', '2021-06-23 13:39:43');
INSERT INTO `aj_report_fireattack` VALUES (34, '端口扫描', '2021-06-23 13:39:43');
INSERT INTO `aj_report_fireattack` VALUES (35, '僵尸网络', '2021-06-23 13:39:43');
INSERT INTO `aj_report_fireattack` VALUES (36, '超大ICMP', '2021-06-23 13:39:43');
INSERT INTO `aj_report_fireattack` VALUES (37, '端口扫描', '2021-06-23 13:39:43');
INSERT INTO `aj_report_fireattack` VALUES (38, '弱口令', '2021-06-23 13:39:46');
INSERT INTO `aj_report_fireattack` VALUES (39, 'WEB', '2021-06-23 13:39:49');
INSERT INTO `aj_report_fireattack` VALUES (40, 'SQL注入', '2021-06-23 13:39:49');
INSERT INTO `aj_report_fireattack` VALUES (41, '端口扫描', '2021-06-23 13:39:55');
INSERT INTO `aj_report_fireattack` VALUES (42, '网站扫描', '2021-06-23 13:39:55');
INSERT INTO `aj_report_fireattack` VALUES (43, 'UDP', '2021-06-23 13:39:55');
INSERT INTO `aj_report_fireattack` VALUES (44, '超大ICMP', '2021-06-23 13:39:58');
INSERT INTO `aj_report_fireattack` VALUES (45, '端口扫描', '2021-06-23 13:39:58');
INSERT INTO `aj_report_fireattack` VALUES (46, '僵尸网络', '2021-06-23 13:40:00');
INSERT INTO `aj_report_fireattack` VALUES (47, '僵尸网络', '2021-06-23 13:40:01');
INSERT INTO `aj_report_fireattack` VALUES (48, '僵尸网络', '2021-06-23 13:40:01');
INSERT INTO `aj_report_fireattack` VALUES (49, '僵尸网络', '2021-06-23 13:40:01');
INSERT INTO `aj_report_fireattack` VALUES (50, '弱口令', '2021-06-23 13:40:05');
INSERT INTO `aj_report_fireattack` VALUES (51, 'SQL注入', '2021-06-23 13:40:07');
INSERT INTO `aj_report_fireattack` VALUES (52, '网站扫描', '2021-06-23 13:40:13');
INSERT INTO `aj_report_fireattack` VALUES (53, '端口扫描', '2021-06-23 13:40:17');
INSERT INTO `aj_report_fireattack` VALUES (54, '僵尸网络', '2021-06-23 13:40:19');
INSERT INTO `aj_report_fireattack` VALUES (55, '僵尸网络', '2021-06-23 13:40:24');
INSERT INTO `aj_report_fireattack` VALUES (56, '超大ICMP', '2021-06-23 13:40:24');
INSERT INTO `aj_report_fireattack` VALUES (57, '端口扫描', '2021-06-23 13:40:24');
INSERT INTO `aj_report_fireattack` VALUES (58, '网站扫描', '2021-06-23 13:40:24');
INSERT INTO `aj_report_fireattack` VALUES (59, 'UDP', '2021-06-23 13:40:24');
INSERT INTO `aj_report_fireattack` VALUES (60, 'WEB', '2021-06-23 13:40:24');
INSERT INTO `aj_report_fireattack` VALUES (61, 'SQL注入', '2021-06-23 13:40:24');
INSERT INTO `aj_report_fireattack` VALUES (62, '弱口令', '2021-06-23 13:40:24');
INSERT INTO `aj_report_fireattack` VALUES (63, '僵尸网络', '2021-06-23 13:40:25');
INSERT INTO `aj_report_fireattack` VALUES (64, '超大ICMP', '2021-06-23 13:40:25');
INSERT INTO `aj_report_fireattack` VALUES (65, '端口扫描', '2021-06-23 13:40:25');
INSERT INTO `aj_report_fireattack` VALUES (66, '网站扫描', '2021-06-23 13:40:25');
INSERT INTO `aj_report_fireattack` VALUES (67, 'UDP', '2021-06-23 13:40:25');
INSERT INTO `aj_report_fireattack` VALUES (68, 'WEB', '2021-06-23 13:40:25');
INSERT INTO `aj_report_fireattack` VALUES (69, 'SQL注入', '2021-06-23 13:40:25');
INSERT INTO `aj_report_fireattack` VALUES (70, '弱口令', '2021-06-23 13:40:25');
INSERT INTO `aj_report_fireattack` VALUES (71, '网站扫描', '2021-06-23 13:40:29');
INSERT INTO `aj_report_fireattack` VALUES (72, 'UDP', '2021-06-23 13:40:29');
INSERT INTO `aj_report_fireattack` VALUES (73, 'WEB', '2021-06-23 13:40:29');
INSERT INTO `aj_report_fireattack` VALUES (74, 'SQL注入', '2021-06-23 13:40:29');
INSERT INTO `aj_report_fireattack` VALUES (75, '僵尸网络', '2021-06-23 13:40:32');
INSERT INTO `aj_report_fireattack` VALUES (76, '超大ICMP', '2021-06-23 13:40:32');
INSERT INTO `aj_report_fireattack` VALUES (77, '端口扫描', '2021-06-23 13:40:32');
INSERT INTO `aj_report_fireattack` VALUES (78, '僵尸网络', '2021-06-23 13:40:32');
INSERT INTO `aj_report_fireattack` VALUES (79, '超大ICMP', '2021-06-23 13:40:32');
INSERT INTO `aj_report_fireattack` VALUES (80, '端口扫描', '2021-06-23 13:40:32');
INSERT INTO `aj_report_fireattack` VALUES (81, '僵尸网络', '2021-06-23 13:40:32');
INSERT INTO `aj_report_fireattack` VALUES (82, '超大ICMP', '2021-06-23 13:40:32');
INSERT INTO `aj_report_fireattack` VALUES (83, '端口扫描', '2021-06-23 13:40:32');
INSERT INTO `aj_report_fireattack` VALUES (84, '僵尸网络', '2021-06-23 13:40:33');
INSERT INTO `aj_report_fireattack` VALUES (85, '超大ICMP', '2021-06-23 13:40:33');
INSERT INTO `aj_report_fireattack` VALUES (86, '端口扫描', '2021-06-23 13:40:33');
INSERT INTO `aj_report_fireattack` VALUES (87, '僵尸网络', '2021-06-23 13:40:36');
INSERT INTO `aj_report_fireattack` VALUES (88, '超大ICMP', '2021-06-23 13:40:36');
INSERT INTO `aj_report_fireattack` VALUES (89, '僵尸网络', '2021-06-23 13:40:36');
INSERT INTO `aj_report_fireattack` VALUES (90, '超大ICMP', '2021-06-23 13:40:36');
INSERT INTO `aj_report_fireattack` VALUES (91, '僵尸网络', '2021-06-23 13:40:36');
INSERT INTO `aj_report_fireattack` VALUES (92, '超大ICMP', '2021-06-23 13:40:36');
INSERT INTO `aj_report_fireattack` VALUES (93, '僵尸网络', '2021-06-23 13:40:37');
INSERT INTO `aj_report_fireattack` VALUES (94, '超大ICMP', '2021-06-23 13:40:37');
INSERT INTO `aj_report_fireattack` VALUES (95, '网站扫描', '2021-06-23 13:42:06');
INSERT INTO `aj_report_fireattack` VALUES (96, 'UDP', '2021-06-23 13:42:26');
INSERT INTO `aj_report_fireattack` VALUES (97, 'SQL注入', '2021-06-23 13:42:42');
INSERT INTO `aj_report_fireattack` VALUES (98, 'UDP', '2021-06-23 13:42:47');
INSERT INTO `aj_report_fireattack` VALUES (99, 'UDP', '2021-06-23 13:42:47');
INSERT INTO `aj_report_fireattack` VALUES (100, 'UDP', '2021-06-23 13:42:48');
COMMIT;

-- ----------------------------
-- Table structure for aj_report_mail
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_mail`;
CREATE TABLE `aj_report_mail` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '事件id',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '登陆用户',
  `status` int DEFAULT NULL COMMENT '1成功，0失败',
  `create_time` datetime DEFAULT NULL COMMENT '事件时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=245 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of aj_report_mail
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_mail` VALUES (1, 'zhangsi', 0, '2021-06-21 14:14:22');
INSERT INTO `aj_report_mail` VALUES (2, 'zhangsi', 0, '2021-06-21 14:18:01');
INSERT INTO `aj_report_mail` VALUES (3, 'zhangsi', 0, '2021-06-21 14:18:01');
INSERT INTO `aj_report_mail` VALUES (4, 'zhangsi', 0, '2021-06-22 14:18:02');
INSERT INTO `aj_report_mail` VALUES (5, 'zhangsi', 0, '2021-06-22 14:18:02');
INSERT INTO `aj_report_mail` VALUES (6, 'zhangsi', 0, '2021-06-22 14:18:02');
INSERT INTO `aj_report_mail` VALUES (7, 'zhangsi', 0, '2021-06-23 14:18:02');
INSERT INTO `aj_report_mail` VALUES (8, 'zhangsi', 0, '2021-06-23 14:18:03');
INSERT INTO `aj_report_mail` VALUES (9, 'zhangsi', 0, '2021-06-23 14:18:03');
INSERT INTO `aj_report_mail` VALUES (10, 'zhangsi', 0, '2021-06-23 14:18:03');
INSERT INTO `aj_report_mail` VALUES (11, 'zhangsi', 0, '2021-06-23 14:18:03');
INSERT INTO `aj_report_mail` VALUES (12, 'zhangsi', 0, '2021-06-23 14:18:04');
INSERT INTO `aj_report_mail` VALUES (13, 'zhangsi', 0, '2021-06-23 14:18:04');
INSERT INTO `aj_report_mail` VALUES (14, 'zhangsi', 0, '2021-06-23 14:18:09');
INSERT INTO `aj_report_mail` VALUES (15, 'zhangsi', 0, '2021-06-23 14:18:10');
INSERT INTO `aj_report_mail` VALUES (16, 'zhangsi', 0, '2021-06-23 14:18:10');
INSERT INTO `aj_report_mail` VALUES (17, 'zhangsi', 0, '2021-06-23 14:18:10');
INSERT INTO `aj_report_mail` VALUES (18, 'zhangsi', 0, '2021-06-23 14:18:10');
INSERT INTO `aj_report_mail` VALUES (19, 'zhangsi', 0, '2021-06-23 14:18:10');
INSERT INTO `aj_report_mail` VALUES (20, 'wangwu', 0, '2021-06-21 14:18:23');
INSERT INTO `aj_report_mail` VALUES (21, 'wangwu', 0, '2021-06-21 14:18:24');
INSERT INTO `aj_report_mail` VALUES (22, 'wangwu', 0, '2021-06-21 14:18:24');
INSERT INTO `aj_report_mail` VALUES (23, 'wangwu', 0, '2021-06-21 14:18:24');
INSERT INTO `aj_report_mail` VALUES (24, 'wangwu', 0, '2021-06-21 14:18:24');
INSERT INTO `aj_report_mail` VALUES (25, 'wangwu', 0, '2021-06-23 14:18:24');
INSERT INTO `aj_report_mail` VALUES (26, 'wangwu', 0, '2021-06-23 14:18:24');
INSERT INTO `aj_report_mail` VALUES (27, 'wangwu', 0, '2021-06-23 14:18:25');
INSERT INTO `aj_report_mail` VALUES (28, 'wangwu', 0, '2021-06-23 14:18:25');
INSERT INTO `aj_report_mail` VALUES (29, 'zhangsi', 0, '2021-06-23 14:18:31');
INSERT INTO `aj_report_mail` VALUES (30, 'wangwu', 0, '2021-06-23 14:18:31');
INSERT INTO `aj_report_mail` VALUES (31, 'zhangsi', 0, '2021-06-23 14:18:31');
INSERT INTO `aj_report_mail` VALUES (32, 'wangwu', 0, '2021-06-23 14:18:31');
INSERT INTO `aj_report_mail` VALUES (33, 'zhangsi', 0, '2021-06-23 14:18:31');
INSERT INTO `aj_report_mail` VALUES (34, 'wangwu', 0, '2021-06-23 14:18:31');
INSERT INTO `aj_report_mail` VALUES (35, 'zhangsi', 0, '2021-06-23 14:18:31');
INSERT INTO `aj_report_mail` VALUES (36, 'wangwu', 0, '2021-06-23 14:18:31');
INSERT INTO `aj_report_mail` VALUES (37, 'zhangsi', 0, '2021-06-23 14:18:32');
INSERT INTO `aj_report_mail` VALUES (38, 'wangwu', 0, '2021-06-23 14:18:32');
INSERT INTO `aj_report_mail` VALUES (39, 'zhangsi', 0, '2021-06-23 14:18:41');
INSERT INTO `aj_report_mail` VALUES (40, 'wangwu', 0, '2021-06-23 14:18:41');
INSERT INTO `aj_report_mail` VALUES (41, 'liliu', 0, '2021-06-23 14:18:41');
INSERT INTO `aj_report_mail` VALUES (42, 'zhangsi', 0, '2021-06-23 14:18:41');
INSERT INTO `aj_report_mail` VALUES (43, 'wangwu', 0, '2021-06-23 14:18:41');
INSERT INTO `aj_report_mail` VALUES (44, 'liliu', 0, '2021-06-23 14:18:41');
INSERT INTO `aj_report_mail` VALUES (45, 'liliu', 0, '2021-06-23 14:18:43');
INSERT INTO `aj_report_mail` VALUES (46, 'liliu', 0, '2021-06-21 14:18:43');
INSERT INTO `aj_report_mail` VALUES (47, 'liliu', 0, '2021-06-21 14:18:44');
INSERT INTO `aj_report_mail` VALUES (48, 'liliu', 0, '2021-06-23 14:18:44');
INSERT INTO `aj_report_mail` VALUES (49, 'IT1', 0, '2021-06-23 14:18:53');
INSERT INTO `aj_report_mail` VALUES (50, 'IT1', 0, '2021-06-23 14:18:53');
INSERT INTO `aj_report_mail` VALUES (51, 'IT1', 0, '2021-06-23 14:18:54');
INSERT INTO `aj_report_mail` VALUES (52, 'IT1', 0, '2021-06-21 14:18:54');
INSERT INTO `aj_report_mail` VALUES (53, 'zhangsi', 0, '2021-06-21 14:18:58');
INSERT INTO `aj_report_mail` VALUES (54, 'wangwu', 0, '2021-06-23 14:18:58');
INSERT INTO `aj_report_mail` VALUES (55, 'liliu', 0, '2021-06-23 14:18:58');
INSERT INTO `aj_report_mail` VALUES (56, 'IT1', 0, '2021-06-23 14:18:58');
INSERT INTO `aj_report_mail` VALUES (57, 'IT2', 0, '2021-06-23 14:19:07');
INSERT INTO `aj_report_mail` VALUES (58, 'IT1', 0, '2021-06-23 14:19:12');
INSERT INTO `aj_report_mail` VALUES (59, 'IT2', 0, '2021-06-23 14:19:12');
INSERT INTO `aj_report_mail` VALUES (60, 'IT1', 0, '2021-06-23 14:19:12');
INSERT INTO `aj_report_mail` VALUES (61, 'IT2', 0, '2021-06-22 14:19:12');
INSERT INTO `aj_report_mail` VALUES (62, 'IT1', 0, '2021-06-22 14:19:12');
INSERT INTO `aj_report_mail` VALUES (63, 'IT2', 0, '2021-06-22 14:19:12');
INSERT INTO `aj_report_mail` VALUES (64, 'IT2', 0, '2021-06-23 14:19:26');
INSERT INTO `aj_report_mail` VALUES (65, 'jiayi', 0, '2021-06-23 14:19:26');
INSERT INTO `aj_report_mail` VALUES (66, 'IT2', 0, '2021-06-23 14:19:27');
INSERT INTO `aj_report_mail` VALUES (67, 'jiayi', 0, '2021-06-23 14:19:27');
INSERT INTO `aj_report_mail` VALUES (68, 'IT2', 0, '2021-06-23 14:19:27');
INSERT INTO `aj_report_mail` VALUES (69, 'jiayi', 0, '2021-06-23 14:19:27');
INSERT INTO `aj_report_mail` VALUES (70, 'IT1', 0, '2021-06-23 14:19:30');
INSERT INTO `aj_report_mail` VALUES (71, 'IT2', 0, '2021-06-23 14:19:30');
INSERT INTO `aj_report_mail` VALUES (72, 'IT1', 0, '2021-06-23 14:19:30');
INSERT INTO `aj_report_mail` VALUES (73, 'IT2', 0, '2021-06-22 14:19:30');
INSERT INTO `aj_report_mail` VALUES (74, 'zhangsi', 0, '2021-06-22 14:19:33');
INSERT INTO `aj_report_mail` VALUES (75, 'wangwu', 0, '2021-06-22 14:19:33');
INSERT INTO `aj_report_mail` VALUES (76, 'liliu', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (77, 'IT1', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (78, 'IT2', 0, '2021-06-22 14:19:33');
INSERT INTO `aj_report_mail` VALUES (79, 'jiayi', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (80, 'zhangsi', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (81, 'wangwu', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (82, 'liliu', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (83, 'IT1', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (84, 'IT2', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (85, 'jiayi', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (86, 'zhangsi', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (87, 'wangwu', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (88, 'liliu', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (89, 'IT1', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (90, 'IT2', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (91, 'jiayi', 0, '2021-06-23 14:19:33');
INSERT INTO `aj_report_mail` VALUES (92, 'zhangsi', 0, '2021-06-21 14:19:34');
INSERT INTO `aj_report_mail` VALUES (93, 'wangwu', 0, '2021-06-21 14:19:34');
INSERT INTO `aj_report_mail` VALUES (94, 'liliu', 0, '2021-06-21 14:19:34');
INSERT INTO `aj_report_mail` VALUES (95, 'IT1', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (96, 'IT2', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (97, 'jiayi', 0, '2021-06-21 14:19:34');
INSERT INTO `aj_report_mail` VALUES (98, 'zhangsi', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (99, 'wangwu', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (100, 'liliu', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (101, 'IT1', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (102, 'IT2', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (103, 'jiayi', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (104, 'zhangsi', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (105, 'wangwu', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (106, 'liliu', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (107, 'IT1', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (108, 'IT2', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (109, 'jiayi', 0, '2021-06-23 14:19:34');
INSERT INTO `aj_report_mail` VALUES (110, 'zhangsi', 0, '2021-06-24 09:39:28');
INSERT INTO `aj_report_mail` VALUES (111, 'wangwu', 0, '2021-06-24 09:39:28');
INSERT INTO `aj_report_mail` VALUES (112, 'liliu', 0, '2021-06-24 09:39:28');
INSERT INTO `aj_report_mail` VALUES (113, 'IT1', 0, '2021-06-24 09:39:28');
INSERT INTO `aj_report_mail` VALUES (114, 'IT2', 0, '2021-06-24 09:39:28');
INSERT INTO `aj_report_mail` VALUES (115, 'jiayi', 0, '2021-06-24 09:39:28');
INSERT INTO `aj_report_mail` VALUES (116, 'zhangsi', 0, '2021-06-24 09:39:35');
INSERT INTO `aj_report_mail` VALUES (117, 'wangwu', 0, '2021-06-24 09:39:35');
INSERT INTO `aj_report_mail` VALUES (118, 'liliu', 0, '2021-06-24 09:39:35');
INSERT INTO `aj_report_mail` VALUES (119, 'IT1', 0, '2021-06-24 09:39:35');
INSERT INTO `aj_report_mail` VALUES (120, 'IT2', 0, '2021-06-24 09:39:35');
INSERT INTO `aj_report_mail` VALUES (121, 'jiayi', 0, '2021-06-24 09:39:35');
INSERT INTO `aj_report_mail` VALUES (122, 'zhangsi', 0, '2021-06-24 09:39:36');
INSERT INTO `aj_report_mail` VALUES (123, 'wangwu', 0, '2021-06-24 09:39:36');
INSERT INTO `aj_report_mail` VALUES (124, 'liliu', 0, '2021-06-24 09:39:36');
INSERT INTO `aj_report_mail` VALUES (125, 'IT1', 0, '2021-06-24 09:39:36');
INSERT INTO `aj_report_mail` VALUES (126, 'IT2', 0, '2021-06-24 09:39:36');
INSERT INTO `aj_report_mail` VALUES (127, 'jiayi', 0, '2021-06-24 09:39:36');
INSERT INTO `aj_report_mail` VALUES (128, 'liliu', 0, '2021-06-24 09:39:39');
INSERT INTO `aj_report_mail` VALUES (129, 'IT1', 0, '2021-06-24 09:39:39');
INSERT INTO `aj_report_mail` VALUES (130, 'IT2', 0, '2021-06-24 09:39:39');
INSERT INTO `aj_report_mail` VALUES (131, 'zhangsi', 0, '2021-06-24 09:39:42');
INSERT INTO `aj_report_mail` VALUES (132, 'wangwu', 0, '2021-06-24 09:39:42');
INSERT INTO `aj_report_mail` VALUES (133, 'liliu', 0, '2021-06-24 09:39:42');
INSERT INTO `aj_report_mail` VALUES (134, 'IT1', 0, '2021-06-24 09:39:44');
INSERT INTO `aj_report_mail` VALUES (135, 'IT1', 0, '2021-06-24 09:39:44');
INSERT INTO `aj_report_mail` VALUES (136, 'IT1', 0, '2021-06-24 09:39:45');
INSERT INTO `aj_report_mail` VALUES (137, 'zhangsi', 0, '2021-06-24 09:39:47');
INSERT INTO `aj_report_mail` VALUES (138, 'zhangsi', 0, '2021-06-24 09:39:47');
INSERT INTO `aj_report_mail` VALUES (139, 'wangwu', 0, '2021-06-24 09:39:50');
INSERT INTO `aj_report_mail` VALUES (140, 'liliu', 0, '2021-06-24 09:39:50');
INSERT INTO `aj_report_mail` VALUES (141, 'wangwu', 0, '2021-06-24 09:39:50');
INSERT INTO `aj_report_mail` VALUES (142, 'liliu', 0, '2021-06-23 09:39:50');
INSERT INTO `aj_report_mail` VALUES (143, 'zhangsi', 0, '2021-06-23 09:39:53');
INSERT INTO `aj_report_mail` VALUES (144, 'wangwu', 0, '2021-06-24 09:39:53');
INSERT INTO `aj_report_mail` VALUES (145, 'liliu', 0, '2021-06-24 09:39:53');
INSERT INTO `aj_report_mail` VALUES (146, 'IT1', 0, '2021-06-24 09:39:53');
INSERT INTO `aj_report_mail` VALUES (147, 'IT2', 0, '2021-06-24 09:39:53');
INSERT INTO `aj_report_mail` VALUES (148, 'zhangsi', 0, '2021-06-24 09:39:54');
INSERT INTO `aj_report_mail` VALUES (149, 'wangwu', 0, '2021-06-23 09:39:54');
INSERT INTO `aj_report_mail` VALUES (150, 'liliu', 0, '2021-06-24 09:39:54');
INSERT INTO `aj_report_mail` VALUES (151, 'IT1', 0, '2021-06-24 09:39:54');
INSERT INTO `aj_report_mail` VALUES (152, 'IT2', 0, '2021-06-24 09:39:54');
INSERT INTO `aj_report_mail` VALUES (153, 'zhangsi', 0, '2021-06-24 09:39:56');
INSERT INTO `aj_report_mail` VALUES (154, 'wangwu', 0, '2021-06-24 09:39:56');
INSERT INTO `aj_report_mail` VALUES (155, 'zhangsi', 0, '2021-06-24 09:39:57');
INSERT INTO `aj_report_mail` VALUES (156, 'wangwu', 0, '2021-06-24 09:39:57');
INSERT INTO `aj_report_mail` VALUES (157, 'zhangsi', 0, '2021-06-24 09:39:57');
INSERT INTO `aj_report_mail` VALUES (158, 'wangwu', 0, '2021-06-24 09:39:57');
INSERT INTO `aj_report_mail` VALUES (159, 'zhangsi', 0, '2021-06-24 09:40:00');
INSERT INTO `aj_report_mail` VALUES (160, 'wangwu', 0, '2021-06-24 09:40:01');
INSERT INTO `aj_report_mail` VALUES (161, 'liliu', 0, '2021-06-24 09:40:01');
INSERT INTO `aj_report_mail` VALUES (162, 'zhangsi', 0, '2021-06-23 09:40:01');
INSERT INTO `aj_report_mail` VALUES (163, 'wangwu', 0, '2021-06-23 09:40:01');
INSERT INTO `aj_report_mail` VALUES (164, 'liliu', 0, '2021-06-24 09:40:01');
INSERT INTO `aj_report_mail` VALUES (165, 'liliu', 0, '2021-06-23 09:40:05');
INSERT INTO `aj_report_mail` VALUES (166, 'IT1', 0, '2021-06-24 09:40:05');
INSERT INTO `aj_report_mail` VALUES (167, 'IT2', 0, '2021-06-24 09:40:07');
INSERT INTO `aj_report_mail` VALUES (168, 'jiayi', 0, '2021-06-24 09:40:07');
INSERT INTO `aj_report_mail` VALUES (169, 'wangwu', 0, '2021-06-24 09:40:11');
INSERT INTO `aj_report_mail` VALUES (170, 'liliu', 0, '2021-06-24 09:40:11');
INSERT INTO `aj_report_mail` VALUES (171, 'zhangsi', 0, '2021-06-24 09:40:14');
INSERT INTO `aj_report_mail` VALUES (172, 'zhangsi', 0, '2021-06-24 09:40:14');
INSERT INTO `aj_report_mail` VALUES (173, 'zhangsi', 0, '2021-06-24 09:40:15');
INSERT INTO `aj_report_mail` VALUES (174, 'IT1', 0, '2021-06-24 09:40:18');
INSERT INTO `aj_report_mail` VALUES (175, 'liliu', 0, '2021-06-24 09:40:20');
INSERT INTO `aj_report_mail` VALUES (176, 'jiayi', 0, '2021-06-24 09:40:22');
INSERT INTO `aj_report_mail` VALUES (177, 'zhangsi', 0, '2021-06-25 09:55:00');
INSERT INTO `aj_report_mail` VALUES (178, 'wangwu', 0, '2021-06-25 09:55:00');
INSERT INTO `aj_report_mail` VALUES (179, 'liliu', 0, '2021-06-25 09:55:00');
INSERT INTO `aj_report_mail` VALUES (180, 'IT1', 0, '2021-06-25 09:55:00');
INSERT INTO `aj_report_mail` VALUES (181, 'IT2', 0, '2021-06-25 09:55:00');
INSERT INTO `aj_report_mail` VALUES (182, 'jiayi', 0, '2021-06-25 09:55:00');
INSERT INTO `aj_report_mail` VALUES (183, 'wangwu', 0, '2021-06-25 09:55:05');
INSERT INTO `aj_report_mail` VALUES (184, 'liliu', 0, '2021-06-25 09:55:05');
INSERT INTO `aj_report_mail` VALUES (185, 'zhangsi', 0, '2021-06-25 09:55:09');
INSERT INTO `aj_report_mail` VALUES (186, 'IT2', 0, '2021-06-25 09:55:12');
INSERT INTO `aj_report_mail` VALUES (187, 'liliu', 0, '2021-06-25 09:55:15');
INSERT INTO `aj_report_mail` VALUES (188, 'IT1', 0, '2021-06-25 09:55:15');
INSERT INTO `aj_report_mail` VALUES (189, 'IT2', 0, '2021-06-25 09:55:15');
INSERT INTO `aj_report_mail` VALUES (190, 'wangwu', 0, '2021-06-22 09:55:17');
INSERT INTO `aj_report_mail` VALUES (191, 'liliu', 0, '2021-06-25 09:55:17');
INSERT INTO `aj_report_mail` VALUES (192, 'jiayi', 0, '2021-06-25 09:55:20');
INSERT INTO `aj_report_mail` VALUES (193, 'liliu', 0, '2021-06-25 09:55:22');
INSERT INTO `aj_report_mail` VALUES (194, 'wangwu', 0, '2021-06-22 09:55:25');
INSERT INTO `aj_report_mail` VALUES (195, 'zhangsi', 0, '2021-06-22 09:55:27');
INSERT INTO `aj_report_mail` VALUES (196, 'IT1', 0, '2021-06-25 09:55:30');
INSERT INTO `aj_report_mail` VALUES (197, 'IT2', 0, '2021-06-21 09:55:33');
INSERT INTO `aj_report_mail` VALUES (198, 'zhangsi', 0, '2021-06-21 09:55:38');
INSERT INTO `aj_report_mail` VALUES (199, 'wangwu', 0, '2021-06-21 09:55:38');
INSERT INTO `aj_report_mail` VALUES (200, 'zhangsi', 0, '2021-06-25 09:55:38');
INSERT INTO `aj_report_mail` VALUES (201, 'wangwu', 0, '2021-06-25 09:55:38');
INSERT INTO `aj_report_mail` VALUES (202, 'zhangsi', 0, '2021-06-25 09:55:39');
INSERT INTO `aj_report_mail` VALUES (203, 'wangwu', 0, '2021-06-25 09:55:39');
INSERT INTO `aj_report_mail` VALUES (204, 'zhangsi', 0, '2021-06-25 09:55:39');
INSERT INTO `aj_report_mail` VALUES (205, 'wangwu', 0, '2021-06-25 09:55:39');
INSERT INTO `aj_report_mail` VALUES (206, 'zhangsi', 0, '2021-06-25 09:55:42');
INSERT INTO `aj_report_mail` VALUES (207, 'wangwu', 0, '2021-06-25 09:55:42');
INSERT INTO `aj_report_mail` VALUES (208, 'liliu', 0, '2021-06-25 09:55:42');
INSERT INTO `aj_report_mail` VALUES (209, 'IT1', 0, '2021-06-25 09:55:42');
INSERT INTO `aj_report_mail` VALUES (210, 'IT2', 0, '2021-06-25 09:55:42');
INSERT INTO `aj_report_mail` VALUES (211, 'zhangsi', 0, '2021-06-25 09:55:45');
INSERT INTO `aj_report_mail` VALUES (212, 'wangwu', 0, '2021-06-25 09:55:45');
INSERT INTO `aj_report_mail` VALUES (213, 'liliu', 0, '2021-06-25 09:55:45');
INSERT INTO `aj_report_mail` VALUES (214, 'IT1', 0, '2021-06-25 09:55:45');
INSERT INTO `aj_report_mail` VALUES (215, 'IT2', 0, '2021-06-25 09:55:45');
INSERT INTO `aj_report_mail` VALUES (216, 'jiayi', 0, '2021-06-25 09:55:45');
INSERT INTO `aj_report_mail` VALUES (217, 'IT1', 0, '2021-06-25 09:55:48');
INSERT INTO `aj_report_mail` VALUES (218, 'IT2', 0, '2021-06-21 09:55:48');
INSERT INTO `aj_report_mail` VALUES (219, 'zhangsi', 0, '2021-06-25 10:00:00');
INSERT INTO `aj_report_mail` VALUES (220, 'wangwu', 0, '2021-06-25 10:00:00');
INSERT INTO `aj_report_mail` VALUES (221, 'liliu', 0, '2021-06-25 10:00:00');
INSERT INTO `aj_report_mail` VALUES (222, 'IT1', 0, '2021-06-25 10:00:00');
INSERT INTO `aj_report_mail` VALUES (223, 'IT2', 0, '2021-06-22 10:00:00');
INSERT INTO `aj_report_mail` VALUES (224, 'jiayi', 0, '2021-06-25 10:00:00');
INSERT INTO `aj_report_mail` VALUES (225, 'jiayi', 0, '2021-06-25 10:00:03');
INSERT INTO `aj_report_mail` VALUES (226, 'jiayi', 0, '2021-06-25 10:00:03');
INSERT INTO `aj_report_mail` VALUES (227, 'jiayi', 0, '2021-06-21 10:00:04');
INSERT INTO `aj_report_mail` VALUES (228, 'IT1', 0, '2021-06-22 10:00:06');
INSERT INTO `aj_report_mail` VALUES (229, 'zhangsi', 0, '2021-06-22 10:00:14');
INSERT INTO `aj_report_mail` VALUES (230, 'wangwu', 0, '2021-06-25 10:00:14');
INSERT INTO `aj_report_mail` VALUES (231, 'liliu', 0, '2021-06-25 10:00:14');
INSERT INTO `aj_report_mail` VALUES (232, 'IT1', 0, '2021-06-21 10:00:14');
INSERT INTO `aj_report_mail` VALUES (233, 'zhangsi', 0, '2021-06-25 10:00:14');
INSERT INTO `aj_report_mail` VALUES (234, 'wangwu', 0, '2021-06-25 10:00:14');
INSERT INTO `aj_report_mail` VALUES (235, 'liliu', 0, '2021-06-25 10:00:14');
INSERT INTO `aj_report_mail` VALUES (236, 'IT1', 0, '2021-06-21 10:00:14');
INSERT INTO `aj_report_mail` VALUES (237, 'zhangsi', 0, '2021-06-25 10:00:14');
INSERT INTO `aj_report_mail` VALUES (238, 'wangwu', 0, '2021-06-25 10:00:14');
INSERT INTO `aj_report_mail` VALUES (239, 'liliu', 0, '2021-06-21 10:00:14');
INSERT INTO `aj_report_mail` VALUES (240, 'IT1', 0, '2021-06-21 10:00:14');
INSERT INTO `aj_report_mail` VALUES (241, 'zhangsi', 0, '2021-06-25 10:00:15');
INSERT INTO `aj_report_mail` VALUES (242, 'wangwu', 0, '2021-06-25 10:00:15');
INSERT INTO `aj_report_mail` VALUES (243, 'liliu', 0, '2021-06-25 10:00:15');
INSERT INTO `aj_report_mail` VALUES (244, 'IT1', 0, '2021-06-25 10:00:15');
COMMIT;

-- ----------------------------
-- Table structure for aj_report_manus
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_manus`;
CREATE TABLE `aj_report_manus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `datetime` date DEFAULT NULL COMMENT '日期',
  `brand` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '系列',
  `manus` int DEFAULT NULL COMMENT '生产量',
  `sales` int DEFAULT NULL COMMENT '销售量',
  `unsales` int DEFAULT NULL COMMENT '滞销量',
  `rework` int DEFAULT NULL COMMENT '返修量',
  `return` int DEFAULT NULL COMMENT '退货量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of aj_report_manus
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_manus` VALUES (1, '2021-06-18', 'A-100', 12, 11, 1, 0, 0);
INSERT INTO `aj_report_manus` VALUES (2, '2021-06-18', 'A-110', 20, 15, 5, 1, 1);
INSERT INTO `aj_report_manus` VALUES (3, '2021-06-18', 'B-100', 20, 20, 0, 0, 0);
INSERT INTO `aj_report_manus` VALUES (4, '2021-06-18', 'B-110', 30, 25, 5, 2, 1);
INSERT INTO `aj_report_manus` VALUES (5, '2021-06-18', 'C-50', 60, 50, 10, 5, 3);
INSERT INTO `aj_report_manus` VALUES (6, '2021-06-18', 'D-40', 65, 65, 0, 10, 0);
INSERT INTO `aj_report_manus` VALUES (7, '2021-06-18', 'E-30', 45, 45, 0, 20, 2);
INSERT INTO `aj_report_manus` VALUES (8, '2021-06-19', 'A-100', 7, 7, 0, 1, 0);
INSERT INTO `aj_report_manus` VALUES (9, '2021-06-19', 'A-110', 10, 9, 1, 1, 0);
INSERT INTO `aj_report_manus` VALUES (10, '2021-06-19', 'B-100', 11, 10, 1, 1, 0);
INSERT INTO `aj_report_manus` VALUES (11, '2021-06-19', 'B-110', 15, 12, 3, 0, 0);
INSERT INTO `aj_report_manus` VALUES (12, '2021-06-19', 'C-50', 40, 40, 0, 5, 2);
INSERT INTO `aj_report_manus` VALUES (13, '2021-06-19', 'D-40', 35, 33, 2, 4, 2);
INSERT INTO `aj_report_manus` VALUES (14, '2021-06-19', 'E-30', 15, 29, 1, 10, 10);
INSERT INTO `aj_report_manus` VALUES (15, '2021-06-20', 'A-100', 17, 13, 4, 1, 0);
INSERT INTO `aj_report_manus` VALUES (16, '2021-06-20', 'A-110', 9, 8, 1, 0, 0);
INSERT INTO `aj_report_manus` VALUES (17, '2021-06-20', 'B-100', 18, 17, 1, 1, 0);
INSERT INTO `aj_report_manus` VALUES (18, '2021-06-20', 'B-110', 5, 5, 0, 0, 0);
INSERT INTO `aj_report_manus` VALUES (19, '2021-06-20', 'C-50', 60, 50, 10, 5, 2);
INSERT INTO `aj_report_manus` VALUES (20, '2021-06-20', 'D-40', 35, 33, 2, 7, 2);
INSERT INTO `aj_report_manus` VALUES (21, '2021-06-20', 'E-30', 45, 44, 1, 12, 3);
INSERT INTO `aj_report_manus` VALUES (22, '2021-06-21', 'A-100', 27, 20, 7, 1, 0);
INSERT INTO `aj_report_manus` VALUES (23, '2021-06-21', 'A-110', 39, 30, 9, 2, 0);
INSERT INTO `aj_report_manus` VALUES (24, '2021-06-21', 'B-100', 28, 25, 3, 1, 0);
INSERT INTO `aj_report_manus` VALUES (25, '2021-06-21', 'B-110', 15, 12, 3, 2, 0);
INSERT INTO `aj_report_manus` VALUES (26, '2021-06-21', 'C-50', 30, 30, 0, 5, 2);
INSERT INTO `aj_report_manus` VALUES (27, '2021-06-21', 'D-40', 25, 24, 1, 3, 1);
INSERT INTO `aj_report_manus` VALUES (28, '2021-06-21', 'E-30', 55, 50, 5, 7, 3);
INSERT INTO `aj_report_manus` VALUES (29, '2021-06-22', 'A-100', 37, 30, 7, 2, 0);
INSERT INTO `aj_report_manus` VALUES (30, '2021-06-22', 'A-110', 19, 15, 4, 1, 1);
INSERT INTO `aj_report_manus` VALUES (31, '2021-06-22', 'B-100', 8, 8, 0, 1, 0);
INSERT INTO `aj_report_manus` VALUES (32, '2021-06-22', 'B-110', 5, 5, 0, 1, 0);
INSERT INTO `aj_report_manus` VALUES (33, '2021-06-22', 'C-50', 20, 19, 1, 2, 1);
INSERT INTO `aj_report_manus` VALUES (34, '2021-06-22', 'D-40', 15, 14, 1, 3, 1);
INSERT INTO `aj_report_manus` VALUES (35, '2021-06-22', 'E-30', 55, 53, 2, 9, 6);
COMMIT;

-- ----------------------------
-- Table structure for aj_report_nums
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_nums`;
CREATE TABLE `aj_report_nums` (
  `id` int NOT NULL AUTO_INCREMENT,
  `inter` int DEFAULT NULL,
  `doub` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aj_report_nums
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_nums` VALUES (9, 18, NULL);
INSERT INTO `aj_report_nums` VALUES (10, NULL, 55.33);
COMMIT;

-- ----------------------------
-- Table structure for aj_report_table
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_table`;
CREATE TABLE `aj_report_table` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of aj_report_table
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_table` VALUES (8, '2021-05-01', '上汽安吉', 'A00000001', '这是一条测试表格事件1');
INSERT INTO `aj_report_table` VALUES (9, '2021-05-02', '上汽大通', 'A00000002', '这是一条测试表格事件2');
INSERT INTO `aj_report_table` VALUES (10, '2021-05-03', '上汽智行', 'A00000003', '这是一条测试表格事件3');
INSERT INTO `aj_report_table` VALUES (11, '2021-05-04', '上汽国际', 'A00000004', '这是一条测试表格事件4');
INSERT INTO `aj_report_table` VALUES (12, '2021-05-05', '上汽国内', 'A00000005', '这是一条测试表格事件5');
INSERT INTO `aj_report_table` VALUES (13, '2021-05-06', '上汽运输', 'A00000006', '这是一条测试表格事件6');
INSERT INTO `aj_report_table` VALUES (14, '2021-05-07', '上汽大众', 'A00000007', '这是一条测试表格事件7');
COMMIT;

-- ----------------------------
-- Table structure for aj_report_wifiamount
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_wifiamount`;
CREATE TABLE `aj_report_wifiamount` (
  `datetime` date NOT NULL,
  `success` int DEFAULT NULL COMMENT '成功次数',
  `fail` int DEFAULT NULL COMMENT '失败次数',
  PRIMARY KEY (`datetime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of aj_report_wifiamount
-- ----------------------------
BEGIN;
INSERT INTO `aj_report_wifiamount` VALUES ('2021-06-17', 210, 15);
INSERT INTO `aj_report_wifiamount` VALUES ('2021-06-18', 234, 43);
INSERT INTO `aj_report_wifiamount` VALUES ('2021-06-19', 199, 28);
INSERT INTO `aj_report_wifiamount` VALUES ('2021-06-20', 260, 80);
INSERT INTO `aj_report_wifiamount` VALUES ('2021-06-21', 245, 45);
INSERT INTO `aj_report_wifiamount` VALUES ('2021-06-22', 216, 26);
INSERT INTO `aj_report_wifiamount` VALUES ('2021-06-23', 150, 10);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
