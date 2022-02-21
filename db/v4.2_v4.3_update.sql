USE pigxx_config;

UPDATE `config_info` SET `data_id` = 'application-dev.yml', `group_id` = 'DEFAULT_GROUP', `content` = '# 配置文件加密根密码\njasypt:\n  encryptor:\n    password: pigx\n    algorithm: PBEWithMD5AndDES\n    iv-generator-classname: org.jasypt.iv.NoIvGenerator\n\nspring:\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\n  redis:\n    host: pigx-redis\n  servlet:\n    multipart:\n      max-file-size: 100MB\n      max-request-size: 100MB\n  cloud:\n    sentinel:\n      eager: true\n      filter:\n        enabled: false\n      transport:\n        dashboard: pigx-sentinel:5020\n\n# 端点对外暴露\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'          \n  endpoint:\n    restart:\n      enabled: true\n    health:\n      show-details: ALWAYS\n# feign 相关配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 20000\n        readTimeout: 20000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n#开启灰度\ngray:\n  rule:\n    enabled: true\n\n# mybatis-plus 配置\nmybatis-plus:\n  tenant-enable: ture\n  mapper-locations: classpath:/mapper/*Mapper.xml\n  global-config:\n    capitalMode: true\n    banner: false\n    db-config:\n      id-type: auto\n      select-strategy: not_empty\n      insert-strategy: not_empty\n      update-strategy: not_empty\n  type-handlers-package:  com.pig4cloud.pigx.common.data.handler\n  configuration:\n    jdbc-type-for-null: \'null\'\n    call-setters-on-nulls: true\n# swagger 全局配置\nknife4j:\n  enable: true\nswagger:\n  enabled: true\n  title: PigX Swagger API\n  license: Powered By PigX\n  licenseUrl: https://pig4cloud.com/\n  terms-of-service-url: https://pig4cloud.com/\n  contact:\n    email: wangiegie@gmail.com\n    url: https://pig4cloud.com/about.html\n  authorization:\n    name: oauth2\n    auth-regex: ^.*$\n    authorization-scope-list:\n      - scope: server\n        description: server all\n    token-url-list:\n      - http://${GATEWAY_HOST:pigx-gateway}:${GATEWAY_PORT:9999}/auth/oauth/token\n\n# oauth2 资源服务器相关配置\nsecurity:\n  oauth2:\n    client:\n      ignore-urls:\n        - /css/**\n        - /error\n        - /actuator/**\n        - /v2/api-docs\n    resource:\n      loadBalanced: true\n      token-info-uri: http://pigx-auth/oauth/check_token', `md5` = '77d9d4800dc7537704e228f2a04dac55', `gmt_create` = '2019-04-18 02:10:20', `gmt_modified` = '2022-02-13 21:44:06', `src_user` = 'nacos', `src_ip` = '127.0.0.1', `app_name` = '', `tenant_id` = '', `c_desc` = '通用配置文件', `c_use` = 'null', `effect` = 'null', `type` = 'yaml', `c_schema` = 'null' WHERE `id` = 1;
UPDATE `config_info` SET `data_id` = 'pigx-codegen-dev.yml', `group_id` = 'DEFAULT_GROUP', `content` = '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      client-secret: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      scope: server\n# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_codegen}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/\n\n# 租户表维护\npigx:\n  tenant:\n    column: tenant_id\n    tables:\n      - gen_datasource_conf\n      - gen_form_conf', `md5` = 'ed93a557f5709d65bb45c14e80c7e9e1', `gmt_create` = '2019-04-18 02:12:10', `gmt_modified` = '2022-01-27 21:27:50', `src_user` = 'nacos', `src_ip` = '127.0.0.1', `app_name` = '', `tenant_id` = '', `c_desc` = '代码生成', `c_use` = 'null', `effect` = 'null', `type` = 'yaml', `c_schema` = 'null' WHERE `id` = 3;

-- PIGXX 数据源

USE pigxx;

ALTER TABLE `sys_dept`
DROP COLUMN `sort`,
  ADD COLUMN `sort_order` int NOT NULL DEFAULT '0' AFTER `name`,
  ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `sort_order`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  MODIFY COLUMN `dept_id` bigint NOT NULL FIRST,
  MODIFY COLUMN `create_time` datetime NULL DEFAULT NULL AFTER `update_by`,
  MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL AFTER `create_time`,
  MODIFY COLUMN `parent_id` bigint DEFAULT NULL AFTER `del_flag`,
  MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `parent_id`;

ALTER TABLE `sys_dept_relation`
    MODIFY COLUMN `ancestor` bigint NOT NULL FIRST,
    MODIFY COLUMN `descendant` bigint NOT NULL AFTER `ancestor`;

ALTER TABLE `sys_dict`
DROP COLUMN `type`,
  DROP COLUMN `system`,
  ADD COLUMN `dict_type` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL AFTER `id`,
  ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `description`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  ADD COLUMN `system_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0' AFTER `remarks`,
  MODIFY COLUMN `id` bigint NOT NULL FIRST,
  MODIFY COLUMN `create_time` datetime NULL DEFAULT NULL AFTER `update_by`,
  MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL AFTER `create_time`,
  MODIFY COLUMN `tenant_id` bigint NOT NULL DEFAULT '0' AFTER `del_flag`;

ALTER TABLE `sys_dict_item`
DROP COLUMN `sort`,
  DROP COLUMN `value`,
  DROP COLUMN `type`,
  ADD COLUMN `item_value` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL AFTER `dict_id`,
  ADD COLUMN `dict_type` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL AFTER `label`,
  ADD COLUMN `sort_order` int NOT NULL DEFAULT '0' AFTER `description`,
  ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `sort_order`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  MODIFY COLUMN `id` bigint NOT NULL FIRST,
  MODIFY COLUMN `dict_id` bigint NOT NULL AFTER `id`,
  MODIFY COLUMN `create_time` datetime NULL DEFAULT NULL AFTER `update_by`,
  MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL AFTER `create_time`,
  MODIFY COLUMN `tenant_id` bigint NOT NULL DEFAULT '0' AFTER `del_flag`,
DROP INDEX `sys_dict_del_flag`,
  ADD KEY `sys_dict_item_del_flag` (`del_flag`),
DROP INDEX `sys_dict_value`,
  ADD KEY `sys_dict_value` (`item_value`);

ALTER TABLE `sys_file`
DROP COLUMN `create_user`,
  DROP COLUMN `update_user`,
  ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `file_size`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  MODIFY COLUMN `id` bigint NOT NULL FIRST,
  MODIFY COLUMN `create_time` datetime NULL DEFAULT NULL AFTER `update_by`,
  MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL AFTER `create_time`,
  MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `del_flag`;

ALTER TABLE `sys_log`
DROP COLUMN `type`,
  ADD COLUMN `log_type` char(1) CHARACTER SET utf8mb4 DEFAULT '0' AFTER `id`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  MODIFY COLUMN `id` bigint NOT NULL FIRST,
  MODIFY COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `service_id`,
  MODIFY COLUMN `create_time` datetime NULL DEFAULT NULL AFTER `update_by`,
  MODIFY COLUMN `update_time` datetime NULL DEFAULT NULL AFTER `create_time`,
  MODIFY COLUMN `time` bigint DEFAULT NULL AFTER `params`,
  MODIFY COLUMN `tenant_id` bigint DEFAULT '0' AFTER `exception`,
DROP INDEX `sys_log_create_by`,
DROP INDEX `sys_log_type`,
  ADD KEY `sys_log_type` (`log_type`);

ALTER TABLE `sys_menu`
DROP COLUMN `type`,
  DROP COLUMN `sort`,
  ADD COLUMN `sort_order` int DEFAULT '1' AFTER `icon`,
  ADD COLUMN `menu_type` char(1) CHARACTER SET utf8mb4 DEFAULT '0' AFTER `keep_alive`,
  ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `menu_type`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_time`,
  MODIFY COLUMN `menu_id` bigint NOT NULL FIRST,
  MODIFY COLUMN `parent_id` bigint DEFAULT NULL AFTER `path`,
  MODIFY COLUMN `tenant_id` bigint unsigned DEFAULT NULL AFTER `del_flag`;

ALTER TABLE `sys_oauth_client_details`
    ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `del_flag`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  ADD COLUMN `create_time` datetime NULL DEFAULT NULL AFTER `update_by`,
  ADD COLUMN `update_time` datetime NULL DEFAULT NULL AFTER `create_time`,
  MODIFY COLUMN `id` bigint NOT NULL FIRST,
  MODIFY COLUMN `tenant_id` bigint NOT NULL DEFAULT '0' AFTER `update_time`;

ALTER TABLE `sys_public_param`
DROP COLUMN `system`,
  ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `validate_code`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  ADD COLUMN `system_flag` char(1) CHARACTER SET utf8mb4 DEFAULT '0' AFTER `public_type`,
  MODIFY COLUMN `public_id` bigint NOT NULL FIRST,
  MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `del_flag`;

ALTER TABLE `sys_role`
    ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `ds_scope`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  MODIFY COLUMN `role_id` bigint NOT NULL FIRST,
  MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `del_flag`;

ALTER TABLE `sys_role_menu`
    MODIFY COLUMN `role_id` bigint NOT NULL FIRST,
    MODIFY COLUMN `menu_id` bigint NOT NULL AFTER `role_id`;

ALTER TABLE `sys_route_conf`
DROP COLUMN `order`,
  ADD COLUMN `sort_order` int DEFAULT '0' AFTER `uri`,
  ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `metadata`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `sys_social_details`
    ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `redirect_url`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  MODIFY COLUMN `id` bigint NOT NULL FIRST,
  MODIFY COLUMN `tenant_id` bigint NOT NULL DEFAULT '0' AFTER `del_flag`;

ALTER TABLE `sys_tenant`
    ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `del_flag`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `sys_user`
    ADD COLUMN `create_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `dept_id`,
  ADD COLUMN `update_by` varchar(64) CHARACTER SET utf8 NOT NULL DEFAULT ' ' AFTER `create_by`,
  MODIFY COLUMN `user_id` bigint NOT NULL FIRST,
  MODIFY COLUMN `dept_id` bigint DEFAULT NULL AFTER `email`,
  MODIFY COLUMN `tenant_id` bigint NOT NULL DEFAULT '0' AFTER `osc_id`;

ALTER TABLE `sys_user_role`
    MODIFY COLUMN `user_id` bigint NOT NULL FIRST,
    MODIFY COLUMN `role_id` bigint NOT NULL AFTER `user_id`;

-- pigxx_ac
USE pigxx_ac;
ALTER TABLE `oa_leave_bill`
    MODIFY COLUMN `leave_id` bigint NOT NULL FIRST,
    MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `del_flag`;

-- pigxx_codegen
USE pigxx_codegen;
ALTER TABLE `gen_datasource_conf`
DROP COLUMN `update_date`,
  DROP COLUMN `create_date`,
  ADD COLUMN `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP AFTER `password`,
  ADD COLUMN `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER `create_time`,
                                                                                      MODIFY COLUMN `id` bigint NOT NULL FIRST,
                                                                                      MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `del_flag`;

ALTER TABLE `gen_form_conf`
    MODIFY COLUMN `id` bigint NOT NULL FIRST,
    MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `del_flag`;

-- pigxx_job
USE pigxx_job;
ALTER TABLE `sys_job`
    MODIFY COLUMN `job_id` bigint NOT NULL FIRST,
    MODIFY COLUMN `tenant_id` bigint DEFAULT '1' AFTER `next_time`;

ALTER TABLE `sys_job_log`
    MODIFY COLUMN `job_log_id` bigint NOT NULL FIRST,
    MODIFY COLUMN `job_id` bigint NOT NULL AFTER `job_log_id`,
    MODIFY COLUMN `tenant_id` bigint NOT NULL DEFAULT '1' AFTER `create_time`;

-- pigxx_mp
USE pigxx_mp;

ALTER TABLE `wx_account`
    MODIFY COLUMN `id` bigint NOT NULL FIRST,
    MODIFY COLUMN `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP AFTER `qr_url`,
    MODIFY COLUMN `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER `create_time`,
                                                                                MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `del_flag`;

ALTER TABLE `wx_account_fans`
    ADD COLUMN `tag_ids` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL AFTER `city`,
  MODIFY COLUMN `id` bigint NOT NULL FIRST,
  MODIFY COLUMN `wx_account_id` bigint DEFAULT NULL AFTER `remark`,
  MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `del_flag`;

CREATE TABLE IF NOT EXISTS `wx_account_tag` (
                                                `id` bigint NOT NULL,
                                                `tag` varchar(100) CHARACTER SET utf8mb4 NOT NULL,
    `create_by` varchar(64) CHARACTER SET utf8mb4 NOT NULL,
    `update_by` varchar(64) CHARACTER SET utf8mb4 NOT NULL,
    `create_time` datetime NOT NULL,
    `update_time` datetime NOT NULL,
    `del_flag` char(1) CHARACTER SET utf8mb4 NOT NULL,
    `tenant_id` bigint NOT NULL,
    `wx_account_id` bigint NOT NULL,
    `wx_account_name` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
    `wx_account_appid` varchar(255) CHARACTER SET utf8mb4 NOT NULL,
    `tag_id` bigint NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

ALTER TABLE `wx_auto_reply`
    MODIFY COLUMN `id` bigint NOT NULL FIRST,
    MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `update_time`;

ALTER TABLE `wx_mp_menu`
    MODIFY COLUMN `id` bigint NOT NULL FIRST,
    MODIFY COLUMN `wx_account_id` bigint DEFAULT NULL AFTER `menu`,
    MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `pub_flag`;

ALTER TABLE `wx_msg`
    MODIFY COLUMN `id` bigint NOT NULL FIRST,
    MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `update_time`;

-- pigxx_pay
USE pigxx_pay;
ALTER TABLE `pay_channel`
    MODIFY COLUMN `id` bigint NOT NULL FIRST,
    MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `update_time`;

ALTER TABLE `pay_goods_order`
    MODIFY COLUMN `goods_order_id` bigint NOT NULL FIRST,
    MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `update_time`;

ALTER TABLE `pay_notify_record`
    MODIFY COLUMN `id` bigint NOT NULL FIRST,
    MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `update_time`;

ALTER TABLE `pay_refund_order`
    MODIFY COLUMN `refund_order_id` bigint NOT NULL FIRST,
    MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `update_time`;

ALTER TABLE `pay_sequence`
    MODIFY COLUMN `name` varchar(32) NOT NULL AFTER `value`;

ALTER TABLE `pay_trade_order`
    MODIFY COLUMN `order_id` bigint NOT NULL FIRST,
    MODIFY COLUMN `tenant_id` bigint DEFAULT NULL AFTER `del_flag`;

-- pigxx_report
USE pigxx_report;

ALTER TABLE `gaea_dict`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `gaea_dict_item`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `gaea_file`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `gaea_report`
    ADD COLUMN `report_author` varchar(255) CHARACTER SET utf8  DEFAULT NULL AFTER `report_desc`,
  ADD COLUMN `download_count` bigint DEFAULT NULL AFTER `report_author`,
  MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `gaea_report_dashboard`
    MODIFY COLUMN `id` bigint NOT NULL FIRST,
DROP INDEX `UNIQUE_REPORT_CODE`,
  ADD UNIQUE KEY `UNIQUE_DASHBOARD_REPORT_CODE` (`report_code`);

ALTER TABLE `gaea_report_dashboard_widget`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `gaea_report_data_set`
    ADD COLUMN `set_type` varchar(10) CHARACTER SET utf8  DEFAULT NULL AFTER `version`,
  MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `gaea_report_data_set_param`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `gaea_report_data_set_transform`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `gaea_report_data_source`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `gaea_report_share`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;
