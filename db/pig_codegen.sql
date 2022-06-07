DROP DATABASE IF EXISTS `pig_codegen`;

CREATE DATABASE  `pig_codegen` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

USE `pig_codegen`;

-- ----------------------------
-- Table structure for gen_datasource_conf
-- ----------------------------
DROP TABLE IF EXISTS `gen_datasource_conf`;
CREATE TABLE `gen_datasource_conf` (
                                       `id` bigint NOT NULL,
                                       `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据源名称',
                                       `url` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'jdbc-url',
                                       `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
                                       `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
                                       `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '删除标记',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `create_by` varchar(64) COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                                       `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                       `update_by` varchar(64) COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='数据源表';

-- ----------------------------
-- Records of gen_datasource_conf
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for gen_form_conf
-- ----------------------------
DROP TABLE IF EXISTS `gen_form_conf`;
CREATE TABLE `gen_form_conf` (
                                 `id` bigint NOT NULL,
                                 `table_name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '表名',
                                 `form_info` json NOT NULL COMMENT '表单信息',
                                 `del_flag` char(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '删除标记',
                                 `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                 `create_by` varchar(64) COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
                                 `update_time` datetime DEFAULT NULL COMMENT '修改时间',
                                 `update_by` varchar(64) COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 KEY `table_name` (`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='表单配置';

-- ----------------------------
-- Records of gen_form_conf
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
