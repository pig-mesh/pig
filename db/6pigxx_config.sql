USE pigxx_config;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `c_use` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `effect` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `type` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `c_schema` text COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
BEGIN;
INSERT INTO `config_info` VALUES (1, 'application-dev.yml', 'DEFAULT_GROUP', '# 配置文件加密根密码\njasypt:\n  encryptor:\n    password: pigx\n    algorithm: PBEWithMD5AndDES\n    iv-generator-classname: org.jasypt.iv.NoIvGenerator\n\nspring:\n  redis:\n    host: pigx-redis\n  servlet:\n    multipart:\n      max-file-size: 100MB\n      max-request-size: 100MB\n  cloud:\n    sentinel:\n      eager: true\n      filter:\n        url-patterns: /** \n      transport:\n        dashboard: pigx-sentinel:5020\n\n# 端点对外暴露\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'          \n  endpoint:\n    health:\n      show-details: ALWAYS\n# feign 相关配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 20000\n        readTimeout: 20000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n#开启灰度\ngray:\n  rule:\n    enabled: true\n\n# mybatis-plus 配置\nmybatis-plus:\n  tenant-enable: ture\n  mapper-locations: classpath:/mapper/*Mapper.xml\n  global-config:\n    banner: false\n    db-config:\n      db-type: oracle\n      id-type: auto\n      select-strategy: not_empty\n      insert-strategy: not_empty\n      update-strategy: not_empty\n  type-handlers-package:  com.pig4cloud.pigx.common.data.handler\n  configuration:\n    jdbc-type-for-null: null\n# swagger 全局配置\nswagger:\n  enabled: true\n  title: PigX Swagger API\n  license: Powered By PigX\n  licenseUrl: https://pig4cloud.com/\n  terms-of-service-url: https://pig4cloud.com/\n  contact:\n    email: wangiegie@gmail.com\n    url: https://pig4cloud.com/about.html\n  authorization:\n    name: oauth2\n    auth-regex: ^.*$\n    authorization-scope-list:\n      - scope: server\n        description: server all\n    token-url-list:\n      - http://${GATEWAY_HOST:pigx-gateway}:${GATEWAY_PORT:9999}/auth/oauth/token\n\n# oauth2 资源服务器相关配置\nsecurity:\n  oauth2:\n    client:\n      ignore-urls:\n        - /error\n        - /actuator/**\n        - /v2/api-docs\n    resource:\n      loadBalanced: true\n      token-info-uri: http://pigx-auth/oauth/check_token', '72abb624f721ae5042c48b5a8b2e8801', '2019-04-18 02:10:20', '2021-06-30 13:35:38', '', '172.16.1.60', '', '', '通用配置文件', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (2, 'pigx-auth-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  freemarker:\n    allow-request-override: false\n    allow-session-override: false\n    cache: true\n    charset: UTF-8\n    check-template-location: true\n    content-type: text/html\n    enabled: true\n    expose-request-attributes: false\n    expose-session-attributes: false\n    expose-spring-macro-helpers: true\n    prefer-file-system-access: true\n    suffix: .ftl\n    template-loader-path: classpath:/templates/', '74f53b71c7799aa754da75662378b93c', '2019-04-18 02:11:32', '2021-05-07 23:40:38', NULL, '10.1.1.143', '', '', '认证中心配置文件', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (3, 'pigx-codegen-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      client-secret: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      scope: server\n# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_codegen}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/\n\npigx:\n  tenant:\n    column: tenant_id\n    tables:\n      - gen_datasource_conf', 'b7d132a3ecdfac4252c994adbb65b23a', '2019-04-18 02:12:10', '2019-08-27 18:24:18', NULL, '127.0.0.1', '', '', '代码生成', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (4, 'pigx-daemon-elastic-job-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(tz2NM4GcmnE7sNJTYL8ZSg==)\n      client-secret: ENC(tz2NM4GcmnE7sNJTYL8ZSg==)\n      scope: server\n## 定时任务\n# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_job}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true', '4124589805657db024b20772cccf57f5', '2019-04-18 02:12:57', '2020-11-09 16:18:05', NULL, '172.16.1.198', '', '', '定时任务-elastic-job配置', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (5, 'pigx-gateway-dev.yml', 'DEFAULT_GROUP', 'gateway:\n  encode-key: \'pigxpigxpigxpigx\'\n\nswagger:\n  ignore-providers:\n    - pigx-auth\n    - pigx-tx-manager\n\nribbon:\n  rule:\n    gray-enabled: true\n\n# 验证码相关配置参考： http://t.cn/A647jEdu\naj:\n  captcha:\n    cache-type: redis\n    water-mark: pig4cloud', 'c9c6c4268b5df12740d28d5aeffb49ea', '2019-04-18 02:13:52', '2021-01-11 17:37:42', NULL, '172.16.1.105', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (6, 'pigx-monitor-dev.yml', 'DEFAULT_GROUP', 'spring:\n  # 安全配置\n  security:\n    user:\n      name: ENC(rZHA4LW5hHmhLAAzJoFNag==)     # pigx\n      password: ENC(bjeyh+Aeii3kHXkoo00ZUw==) # pigx\n  autoconfigure:\n    exclude: com.pig4cloud.pigx.common.core.config.JacksonConfig\n  boot:\n    admin:\n      ui:\n        title: \'pigx 服务状态监控\'\n        brand: \'pigx 服务状态监控\'\n        external-views:\n          - label: \"SQL监控\"\n            url: /druid/sql.html\n            order: 2000\n            iframe: true\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS  #显示详细信息\n\n\n# druid 监控的服务\nmonitor:\n  applications:\n    - pigx-upms-biz', 'bafb4b4e1c86115d15be0bf23ec48a6c', '2019-04-18 02:14:17', '2021-02-10 21:32:49', NULL, '10.1.1.42', '', '', '监控配置文件', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (7, 'pigx-upms-biz-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(ltJPpR50wT0oIY9kfOe1Iw==)\n      client-secret: ENC(ltJPpR50wT0oIY9kfOe1Iw==)\n      scope: server\n      ignore-urls:\n        - /error\n        - /druid/**\n        - /actuator/**\n        - /v2/api-docs\n\n# 数据源\nspring:\n  autoconfigure:\n    exclude: org.springframework.cloud.gateway.config.GatewayAutoConfiguration,org.springframework.cloud.gateway.config.GatewayClassPathWarningAutoConfiguration\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n      stat-view-servlet:\n        enabled: true\n        allow: \"\"\n        url-pattern: /druid/*\n        #login-username: admin\n        #login-password: admin\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true\n          slow-sql-millis: 10000\n          merge-sql: false\n        wall:\n          config:\n            multi-statement-allow: true\n\n# 文件系统 (提供测试环境，不要乱传)\noss:\n  endpoint: http://minio.pig4cloud.com\n  access-key: lengleng\n  secret-key: lengleng\n  bucket-name: lengleng\n\n# Logger Config\nlogging:\n  level:\n    com.pig4cloud.pigx.admin.mapper: debug\n\n# 租户表维护\npigx:\n  tenant:\n    column: tenant_id\n    tables:\n      - sys_user\n      - sys_role\n      - sys_menu\n      - sys_dept\n      - sys_log\n      - sys_social_details\n      - sys_dict\n      - sys_dict_item\n      - sys_public_param\n      - sys_log\n      - sys_file\n      - sys_oauth_client_details', 'ef3968c8268783f233bec0d657dbaaab', '2019-04-18 02:32:44', '2021-02-18 12:46:02', NULL, '10.1.1.60', '', '', 'admin 服务配置', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (8, 'pigx-tx-manager-dev.yml', 'DEFAULT_GROUP', '# 页面配置\nspring:\n  mvc:\n    static-path-pattern: /**\n  resources:\n    static-locations: classpath:/static/\n\n\n# LCN 配置\ntm:\n  transaction:\n    netty:\n      delaytime: 5   # 客户端链接最大通讯时间 （秒）\n      hearttime: 15  # 客户端心跳时间   （秒）\n  redis:\n    savemaxtime: 30  # redis 保存时间  （秒）\n  socket:\n    port: 9998       # 通讯端口\n    maxconnection: 1000  #最大链接数\n  compensate:\n    auto: false   #是否自动补偿\n    notifyUrl: http://ip:port/path #补偿结果通知（配消息总线里面）\n    tryTime: 30     # z再次重试时间间隔\n    maxWaitTime: 5000   # 请求超时的最大时间 (毫秒)', '885f5c125f2df7a08692929ccce154be', '2019-04-18 02:54:03', '2019-04-18 02:54:03', NULL, '127.0.0.1', '', '', '分布式事务协调模块', NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (9, 'pigx-daemon-quartz-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(tz2NM4GcmnE7sNJTYL8ZSg==)\n      client-secret: ENC(tz2NM4GcmnE7sNJTYL8ZSg==)\n      scope: server\n\n# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_job}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/\n  quartz:\n    #相关属性配置\n    properties:\n      org:\n        quartz:\n          scheduler:\n            instanceName: clusteredScheduler\n            instanceId: AUTO\n          jobStore:\n            class: org.quartz.impl.jdbcjobstore.JobStoreTX\n            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate\n            tablePrefix: QRTZ_\n            isClustered: true\n            clusterCheckinInterval: 10000\n            useProperties: false\n          threadPool:\n            class: org.quartz.simpl.SimpleThreadPool\n            threadCount: 50\n            threadPriority: 5\n            threadsInheritContextClassLoaderOfInitializingThread: true\n    #数据库方式\n    job-store-type: jdbc\n    #初始化表结构\n    #jdbc:\n    #initialize-schema: never\n\n', '276cf549fb8b084e35dc07d1eb309932', '2019-04-18 03:08:34', '2019-08-27 18:25:41', NULL, '127.0.0.1', '', '', 'quartz 配置文件', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (10, 'pigx-pay-platform-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      client-secret: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      scope: server\n# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_pay}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  freemarker:\n    allow-request-override: false\n    allow-session-override: false\n    cache: true\n    charset: UTF-8\n    check-template-location: true\n    content-type: text/html\n    enabled: true\n    expose-request-attributes: false\n    expose-session-attributes: false\n    expose-spring-macro-helpers: true\n    prefer-file-system-access: true\n    suffix: .ftl\n    template-loader-path: classpath:/templates/\n# 租户表维护\npigx:\n  pay:\n    test: lengleng\n    aliPayConfig:\n      expire-time: 30\n      return-url: http://pig4cloud.com\n      notify-url: http://wechat.pigx.top/pay/notify/ali/callbak\n    wxPayConfig:\n      notify-url: https://admin.pig4cloud.com/pay/notify/wx/callbak\n    mergePayConfig:\n      return-url: http://pig4cloud.com\n      notify-url: http://wechat.pigx.top/pay/notify/merge/callbak\n  xsequence:    #发号器相关配置\n    db:\n      retry-times: 3\n      table-name: pay_sequence\n  tenant:\n    column: tenant_id\n    tables:\n      - pay_channel\n      - pay_trade_order\n      - pay_goods_order\n      - pay_notify_record', '5ae561216eef67b9fc83a2b077c0adb0', '2019-05-28 11:46:27', '2021-01-04 22:16:41', NULL, '10.1.1.163', '', '', '支付模块配置', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (11, 'pigx-oa-platform-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      client-secret: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      scope: server\n      ignore-urls:\n        - \'/actuator/**\'\n        - \'/v2/api-docs\'\n        - \'/editor-app/**\'\n        - \'/modeler.html\'\n        - \'/ws/**\'\nspring:\n  autoconfigure:\n    exclude: org.activiti.spring.boot.SecurityAutoConfiguration\n  activiti:\n    check-process-definitions: false\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_ac}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n\n# 租户表维护\npigx:\n  tenant:\n    column: tenant_id\n    tables:\n      - oa_leave_bill', 'bb2ee29e458875b0334e9f204f958272', '2019-10-16 16:38:52', '2019-10-16 16:38:52', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL);
INSERT INTO `config_info` VALUES (12, 'pigx-mp-platform-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(vW+Nup9LaTfIFwgufUBsYg==)\n      client-secret: ENC(vW+Nup9LaTfIFwgufUBsYg==)\n      scope: server\n# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL-USER:root}\n      password: ${MYSQL-PWD:root}\n      url: jdbc:mysql://${MYSQL-HOST:pigx-mysql}:${MYSQL-PORT:3306}/${MYSQL-DB:pigxx_mp}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/\n\n\n# 租户表维护\npigx:\n  tenant:\n    column: tenant_id\n    tables:\n      - wx_mp_menu\n      - wx_account\n      - wx_account_fans\n      - wx_msg\n      - wx_auto_reply', 'b179debedf55403402031192824a251e', '2019-10-21 08:58:24', '2020-06-09 15:04:07', NULL, '172.17.0.156', '', '', 'null', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (13, 'pigx-bi-platform-dev.yml', 'DEFAULT_GROUP', 'spring:\n  # 安全配置\n  security:\n    user:\n      name: ENC(rZHA4LW5hHmhLAAzJoFNag==)     # pigx\n      password: ENC(bjeyh+Aeii3kHXkoo00ZUw==) # pigx\n      \n# ureport 报表相关设置\nureport:\n  debug: false\n  disableFileProvider: true\n  disableHttpSessionReportCache: true\n\n# oss 保存报表设计文件\noss:\n  bucket-name: lengleng\n  access-key: lengleng\n  secret-key: lengleng\n  endpoint: http://minio.pig4cloud.com', '77e5829593e85d395aafdd88e662c036', '2020-10-15 18:18:13', '2021-02-03 19:12:08', NULL, '172.16.1.92', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (14, 'pigx-xxl-job-admin-dev.yml', 'DEFAULT_GROUP', '# xxl\nxxl:\n  job:\n    i18n: zh_CN\n    logretentiondays: 30\n    triggerpool:\n      fast.max: 200\n      slow.max: 200\n\n# mybatis\nmybatis:\n  mapper-locations: classpath:/mybatis-mapper/*Mapper.xml\n\n# spring\nspring:\n  datasource:\n    url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_job}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: ${MYSQL_USER:root}\n    password: ${MYSQL_PWD:root}\n  mvc:\n    static-path-pattern: /static/**\n  freemarker:\n    suffix: .ftl\n    request-context-attribute: request\n    settings:\n      number_format: 0.##########\n  mail:\n    host: smtp.mxhichina.com\n    port: 465\n    from: xxxx@gitee.wang\n    username: xxxx@gitee.wang\n    password: xxxx\n    properties:\n      mail:\n        smtp:\n          auth: true\n          ssl.enable: true\n          starttls.enable: false\n          required: false\n# spring boot admin 配置\n\nmanagement:\n  health:\n    mail:\n      enabled: false\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS', 'bcee40a6beef8c855c7cfc20a2e1c3ac', '2021-07-21 20:45:02', '2021-07-21 20:45:02', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL);
COMMIT;

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `tag_name` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info` (
  `id` bigint(64) unsigned NOT NULL,
  `nid` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` text COLLATE utf8_bin,
  `src_ip` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `op_type` char(10) COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `role` varchar(50) NOT NULL,
  `resource` varchar(512) NOT NULL,
  `action` varchar(8) NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `username` varchar(50) NOT NULL,
  `role` varchar(50) NOT NULL,
  UNIQUE KEY `uk_username_role` (`username`,`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of roles
-- ----------------------------
BEGIN;
INSERT INTO `roles` VALUES ('nacos', 'ROLE_ADMIN');
COMMIT;

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint(20) NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint(20) NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL,
  `password` varchar(500) NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
