USE pigxx_codegen;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_datasource_conf
-- ----------------------------
DROP TABLE IF EXISTS `gen_datasource_conf`;
CREATE TABLE `gen_datasource_conf` (
                                       `id` bigint NOT NULL COMMENT '主键',
                                       `name` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '别名',
                                       `url` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'jdbcurl',
                                       `username` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户名',
                                       `password` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '密码',
                                       `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新',
                                       `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0' COMMENT '删除标记',
                                       `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
                                       `ds_type` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '数据库类型',
                                       `conf_type` char(1) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '配置类型',
                                       `ds_name` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '数据库名称',
                                       `instance` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '实例',
                                       `port` int DEFAULT NULL COMMENT '端口',
                                       `host` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '主机',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='数据源表';

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
                                 `id` bigint NOT NULL COMMENT 'ID',
                                 `table_name` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                 `form_info` json NOT NULL COMMENT '表单信息',
                                 `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
                                 `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                 `tenant_id` bigint DEFAULT NULL COMMENT '所属租户',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 KEY `table_name` (`table_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单配置';

-- ----------------------------
-- Records of gen_form_conf
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
