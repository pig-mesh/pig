SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 角色首页组件配置表
DROP TABLE IF EXISTS `sys_role_widget`;
CREATE TABLE `sys_role_widget` (
  `id`            BIGINT       NOT NULL COMMENT '主键',
  `role_id`       BIGINT       NOT NULL COMMENT '角色ID',
  `widget_keys`   VARCHAR(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '允许的组件key列表，逗号分隔',
  `layout_config` TEXT         CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL COMMENT '默认布局JSON，格式同前端grid对象',
  `tenant_id`     BIGINT       DEFAULT NULL COMMENT '租户ID',
  `del_flag`      CHAR(1)      CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '删除标记,1:已删除,0:正常',
  `create_by`     VARCHAR(64)  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `update_by`     VARCHAR(64)  CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '修改人',
  `create_time`   DATETIME     DEFAULT NULL COMMENT '创建时间',
  `update_time`   DATETIME     DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_id` (`role_id`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='角色首页组件配置';

SET FOREIGN_KEY_CHECKS = 1;
