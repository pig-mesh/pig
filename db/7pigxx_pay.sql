USE pigxx_pay;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_channel
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel`;
CREATE TABLE `pay_channel` (
                               `id` bigint NOT NULL COMMENT '渠道主键ID',
                               `mch_id` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
                               `channel_id` varchar(24) CHARACTER SET utf8mb4 DEFAULT NULL,
                               `channel_name` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL,
                               `channel_mch_id` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
                               `return_url` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '前端回调地址',
                               `notify_url` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '后端回调地址',
                               `state` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                               `param` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                               `remark` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL,
                               `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
                               `app_id` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
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
                                   `goods_order_id` bigint NOT NULL COMMENT '商品订单ID',
                                   `goods_id` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `goods_name` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `amount` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `user_id` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `status` tinyint NOT NULL DEFAULT '0' COMMENT '订单状态,订单生成(0),支付成功(1),处理完成(2),处理失败(-1)',
                                   `pay_order_id` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
                                   PRIMARY KEY (`goods_order_id`) USING BTREE,
                                   UNIQUE KEY `IDX_PayOrderId` (`pay_order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商品订单表';

-- ----------------------------
-- Records of pay_goods_order
-- ----------------------------
BEGIN;
INSERT INTO `pay_goods_order` VALUES (1, '10001', '测试产品', '1', 'oiOkqw2NON9hO9FwazKRBr3zfysI', 1, '2021101500001', '0', '2021-10-15 21:40:31', '2021-10-15 21:40:31', 1);
COMMIT;

-- ----------------------------
-- Table structure for pay_notify_record
-- ----------------------------
DROP TABLE IF EXISTS `pay_notify_record`;
CREATE TABLE `pay_notify_record` (
                                     `id` bigint NOT NULL COMMENT 'ID',
                                     `notify_id` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
                                     `request` varchar(2000) CHARACTER SET utf8mb4 DEFAULT NULL,
                                     `response` varchar(2000) CHARACTER SET utf8mb4 DEFAULT NULL,
                                     `order_no` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
                                     `http_status` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL,
                                     `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
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
                                    `refund_order_id` bigint NOT NULL,
                                    `pay_order_id` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `channel_pay_order_no` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `mch_id` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `mch_refund_no` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `channel_id` varchar(24) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `pay_amount` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `refund_amount` bigint NOT NULL COMMENT '退款金额,单位分',
                                    `currency` varchar(3) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `status` tinyint NOT NULL DEFAULT '0' COMMENT '退款状态:0-订单生成,1-退款中,2-退款成功,3-退款失败,4-业务处理完成',
                                    `result` tinyint NOT NULL DEFAULT '0' COMMENT '退款结果:0-不确认结果,1-等待手动处理,2-确认成功,3-确认失败',
                                    `client_ip` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `device` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `remark` varchar(256) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `channel_user` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `username` varchar(24) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `channel_mch_id` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `channel_order_no` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `channel_err_code` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `channel_err_msg` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `extra` varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `notifyUrl` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `param1` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `param2` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                    `expire_time` datetime DEFAULT NULL COMMENT '订单失效时间',
                                    `refund_succ_time` datetime DEFAULT NULL COMMENT '订单退款成功时间',
                                    `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
                                    PRIMARY KEY (`refund_order_id`) USING BTREE,
                                    UNIQUE KEY `IDX_MchId_MchOrderNo` (`mch_id`,`mch_refund_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='退款订单表';

-- ----------------------------
-- Records of pay_refund_order
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pay_sequence
-- ----------------------------
DROP TABLE IF EXISTS `pay_sequence`;
CREATE TABLE `pay_sequence` (
                                `id` bigint NOT NULL AUTO_INCREMENT,
                                `value` bigint NOT NULL,
                                `name` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
                                `gmt_create` datetime NOT NULL,
                                `gmt_modified` datetime NOT NULL,
                                PRIMARY KEY (`id`) USING BTREE,
                                UNIQUE KEY `uk_name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of pay_sequence
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for pay_trade_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_trade_order`;
CREATE TABLE `pay_trade_order` (
                                   `order_id` bigint NOT NULL,
                                   `channel_id` varchar(24) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `amount` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `currency` varchar(3) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '支付状态,0-订单生成,1-支付中(目前未使用),2-支付成功,3-业务处理完成',
                                   `client_ip` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `device` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `subject` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `body` varchar(256) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `extra` varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `channel_mch_id` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `channel_order_no` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `err_code` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `err_msg` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `param1` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `param2` varchar(64) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `notify_url` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL,
                                   `notify_count` tinyint DEFAULT '0' COMMENT '通知次数',
                                   `last_notify_time` bigint DEFAULT NULL COMMENT '最后一次通知时间',
                                   `expire_time` bigint DEFAULT NULL COMMENT '订单失效时间',
                                   `pay_succ_time` bigint DEFAULT NULL COMMENT '订单支付成功时间',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   `del_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0',
                                   `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
                                   PRIMARY KEY (`order_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='支付订单表';

-- ----------------------------
-- Records of pay_trade_order
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
