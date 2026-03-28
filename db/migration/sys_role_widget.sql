-- 角色首页组件配置表
CREATE TABLE IF NOT EXISTS `sys_role_widget` (
  `id`            BIGINT       NOT NULL COMMENT '主键',
  `role_id`       BIGINT       NOT NULL COMMENT '角色ID',
  `widget_keys`   VARCHAR(500) NOT NULL COMMENT '允许的组件key列表，逗号分隔',
  `layout_config` TEXT         NOT NULL COMMENT '默认布局JSON，格式同前端grid对象',
  `create_by`     VARCHAR(64)  DEFAULT NULL,
  `update_by`     VARCHAR(64)  DEFAULT NULL,
  `create_time`   DATETIME     DEFAULT NULL,
  `update_time`   DATETIME     DEFAULT NULL,
  `del_flag`      CHAR(1)      DEFAULT '0' COMMENT '删除标记,1:已删除,0:正常',
  `tenant_id`     BIGINT       DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_id` (`role_id`, `tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色首页组件配置';
