USE pigxx_app;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
  `create_by` varchar(32) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
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
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '更新时间',
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
-- Table structure for app_contacts
-- ----------------------------
DROP TABLE IF EXISTS `app_contacts`;
CREATE TABLE `app_contacts` (
  `id` bigint NOT NULL COMMENT '主键',
  `contact_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记',
  `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='通讯录表';

-- ----------------------------
-- Records of app_contacts
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
INSERT INTO `app_page` VALUES (1, 1, '首页装修', '[{\"name\":\"search\",\"title\":\"搜索\",\"styles\":{},\"content\":{},\"disabled\":1},{\"name\":\"banner\",\"title\":\"首页轮播图\",\"styles\":{},\"content\":{\"data\":[{\"link\":{\"name\":\"个人中心\",\"path\":\"/pages/user/index\",\"type\":\"shop\"},\"name\":\"商品大抽拼\",\"image\":\"https://minio.pigx.vip/oss/202605/home.jpg\"}],\"enabled\":1}},{\"name\":\"nav\",\"title\":\"导航菜单\",\"styles\":{},\"content\":{\"data\":[{\"icon\":\"star\",\"link\":{\"name\":\"我的收藏\",\"path\":\"/pages/user/collection/index\",\"type\":\"shop\"},\"name\":\"我的收藏\",\"image\":\"https://minio.pigx.top/oss/app/nav02.png\"},{\"icon\":\"calendar-line\",\"name\":\"日历\",\"link\":{\"path\":\"/pages/workbench/calendar/index\",\"name\":\"日程\",\"type\":\"shop\"}},{\"icon\":\"message\",\"name\":\"消息中心\",\"link\":{\"path\":\"/pages/user/messages/index\",\"name\":\"消息中心\",\"type\":\"shop\"}},{\"icon\":\"user-group\",\"name\":\"通讯录\",\"link\":{\"path\":\"/pages/workbench/contacts/index\",\"name\":\"通讯录\",\"type\":\"shop\"}}],\"enabled\":1}}]', NULL, 'admin', NULL, '2026-05-13 23:37:53', '0', 1);
INSERT INTO `app_page` VALUES (2, 2, '个人中心', '[{\"name\":\"user-info\",\"title\":\"用户信息\",\"styles\":{},\"content\":{},\"disabled\":1},{\"name\":\"my-service\",\"title\":\"我的服务\",\"styles\":{},\"content\":{\"data\":[{\"icon\":\"settings\",\"link\":{\"name\":\"个人设置\",\"path\":\"/pages/user/settings/index\",\"type\":\"shop\"},\"name\":\"个人设置\",\"image\":\"https://minio.pigx.top/oss/app/user_setting.png\"},{\"icon\":\"star\",\"link\":{\"name\":\"我的收藏\",\"path\":\"/pages/user/collection/index\",\"type\":\"shop\"},\"name\":\"我的收藏\",\"image\":\"https://minio.pigx.top/oss/app/user_collect.png\"},{\"icon\":\"headset\",\"link\":{\"name\":\"联系客服\",\"path\":\"/pages/user/customer-service/index\",\"type\":\"shop\"},\"name\":\"联系客服\",\"image\":\"https://minio.pigx.top/oss/app/user_kefu.png\"},{\"icon\":\"info-circle\",\"name\":\"关于我们\",\"link\":{\"path\":\"/pages/user/about/index\",\"name\":\"关于我们\",\"type\":\"shop\"}}],\"style\":2,\"title\":\"服务中心\"}},{\"name\":\"user-banner\",\"title\":\"个人中心广告图\",\"styles\":{},\"content\":{\"data\":[{\"link\":{\"name\":\"个人中心\",\"path\":\"/pages/user/index\",\"type\":\"shop\"},\"name\":\"sdds\",\"image\":\"https://minio.pigx.vip/oss/202605/ad.jpg\"}],\"enabled\":1}}]', NULL, 'admin', NULL, '2026-05-13 23:37:53', '0', 1);
INSERT INTO `app_page` VALUES (3, 3, '客服设置', '[{\"title\":\"客服设置\",\"name\":\"customer-service\",\"content\":{\"title\":\"添加客服二维码\",\"time\":\"早上 9:00 - 22:00\",\"mobile\":\"13800138000\",\"qrcode\":\"/admin/sys-file/oss/file?fileName=86f81144ab2a4984bbf63fe851d49faf.png\"},\"styles\":{}}]', NULL, 'admin', NULL, '2026-05-13 23:37:53', '0', 1);
INSERT INTO `app_page` VALUES (4, 4, '工作台', '[{\"id\":\"mp47dnlr51ltq\",\"title\":\"常用功能\",\"name\":\"workbench-shortcuts\",\"content\":{\"enabled\":1,\"data\":[{\"icon\":\"send\",\"name\":\"我的\",\"link\":{\"path\":\"/pages/workbench/flow/tasks/index?type=started\",\"name\":\"我的\",\"type\":\"flow-tasks\"},\"color\":\"#2563eb\"},{\"icon\":\"clock-circle\",\"name\":\"待办\",\"link\":{\"path\":\"/pages/workbench/flow/tasks/index?type=pending\",\"name\":\"待办\",\"type\":\"flow-tasks\"},\"color\":\"#d97706\"},{\"icon\":\"email\",\"name\":\"抄送\",\"link\":{\"path\":\"/pages/workbench/flow/tasks/index?type=cc\",\"name\":\"抄送\",\"type\":\"flow-tasks\"},\"color\":\"#4f46e5\"},{\"icon\":\"check-circle\",\"name\":\"已办\",\"link\":{\"path\":\"/pages/workbench/flow/tasks/index?type=completed\",\"name\":\"已办\",\"type\":\"flow-tasks\"},\"color\":\"#15803d\"}]},\"styles\":{}}]', NULL, 'admin', NULL, '2026-05-13 23:44:57', '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for app_tabbar
-- ----------------------------
DROP TABLE IF EXISTS `app_tabbar`;
CREATE TABLE `app_tabbar` (
  `id` bigint unsigned NOT NULL COMMENT '主键',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '导航名称',
  `selected` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '选中图标',
  `unselected` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '未选中图标',
  `link` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '链接地址',
  `sort_order` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `login_flag` char(1) COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否需要登录（0-不需要，1-需要）',
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
INSERT INTO `app_tabbar` VALUES (1, '首页', 'home-fill', 'home', '{\"path\":\"/pages/index/index\",\"name\":\"首页\",\"type\":\"shop\"}', 0, '0', NULL, '2026-05-13 23:37:53', NULL, 'admin', '0', 1);
INSERT INTO `app_tabbar` VALUES (2, '工作台', 'apps', 'apps', '{\"path\":\"/pages/workbench/index\",\"name\":\"工作台\",\"type\":\"shop\"}', 1, '1', NULL, '2026-05-13 23:42:00', NULL, 'admin', '0', 1);
INSERT INTO `app_tabbar` VALUES (3, '我的', 'user', 'user', '{\"path\":\"/pages/user/index\",\"name\":\"个人中心\",\"type\":\"shop\"}', 2, '0', NULL, '2026-05-13 23:37:53', NULL, 'admin', '0', 1);
COMMIT;

-- ----------------------------
-- Table structure for app_role_tabbar
-- ----------------------------
DROP TABLE IF EXISTS `app_role_tabbar`;
CREATE TABLE `app_role_tabbar` (
  `role_id` bigint(20) NOT NULL COMMENT '系统角色ID',
  `tabbar_id` bigint(20) NOT NULL COMMENT '底部导航ID',
  `tenant_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属租户',
  PRIMARY KEY (`role_id`,`tabbar_id`,`tenant_id`) USING BTREE,
  KEY `app_role_tabbar_role_index` (`role_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='App角色底部导航表';

-- ----------------------------
-- Records of app_role_tabbar
-- ----------------------------
BEGIN;
INSERT INTO `app_role_tabbar` VALUES (1, 2, 1);
INSERT INTO `app_role_tabbar` VALUES (1, 3, 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
