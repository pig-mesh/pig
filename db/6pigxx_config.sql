USE pigxx_config;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'group_id',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `c_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT 'configuration description',
  `c_use` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'configuration usage',
  `effect` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '配置生效的描述',
  `type` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '配置的类型',
  `c_schema` text COLLATE utf8_bin COMMENT '配置的模式',
  `encrypted_data_key` varchar(1024) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '密钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
BEGIN;
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (1, 'application-dev.yml', 'DEFAULT_GROUP', '# 配置文件加密根密码\njasypt:\n  encryptor:\n    password: pigx\n    algorithm: PBEWithMD5AndDES\n    iv-generator-classname: org.jasypt.iv.NoIvGenerator\n\n\nspring:\n  data:\n    redis:\n      host: pigx-redis\n  servlet:\n    multipart:\n      max-file-size: 100MB\n      max-request-size: 100MB\n  cloud:\n    sentinel:\n      eager: true\n      transport:\n        dashboard: pigx-sentinel:5020\n    openfeign:\n      sentinel:\n        enabled: true\n      okhttp:\n        enabled: true\n      httpclient:\n        enabled: false\n        connection-timeout: 20000\n      compression:\n        request:\n          enabled: true\n        response:\n          enabled: true\n\n# 端点对外暴露\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'          \n  endpoint:\n    restart:\n      enabled: true\n    health:\n      show-details: ALWAYS\n\n#开启灰度\ngray:\n  rule:\n    enabled: true\n\n# mybatis-plus 配置\nmybatis-plus:\n  tenant-enable: ture\n  mapper-locations: classpath:/mapper/*Mapper.xml\n  global-config:\n    capitalMode: true\n    banner: false\n    db-config:\n      id-type: auto\n      select-strategy: not_empty\n      insert-strategy: not_empty\n      update-strategy: not_null\n  type-handlers-package:  com.pig4cloud.pigx.common.data.handler\n  configuration:\n    jdbc-type-for-null: \'null\'\n    call-setters-on-nulls: true\n    shrink-whitespaces-in-sql: true\nmybatis-plus-join:\n  banner: false #关闭连表查询组件banner', '90942d7d4856ccba068fe5847643737d', '2022-12-16 10:44:25', '2023-08-14 11:44:19', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (2, 'pigx-auth-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  freemarker:\n    allow-request-override: false\n    allow-session-override: false\n    cache: true\n    charset: UTF-8\n    check-template-location: true\n    content-type: text/html\n    enabled: true\n    expose-request-attributes: false\n    expose-session-attributes: false\n    expose-spring-macro-helpers: true\n    prefer-file-system-access: true\n    suffix: .ftl\n    template-loader-path: classpath:/templates/\n\n# 前端登录验证码\nsecurity:\n  encodeKey: \'pigxpigxpigxpigx\'\n# 验证码相关配置参考： http://t.cn/A647jEdu\naj:\n  captcha:\n    cache-type: redis\n    water-mark: pig4cloud', 'bb6bd9b9e21afdec3d6c88636387df96', '2022-12-16 10:44:25', '2024-04-20 22:47:21', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (3, 'pigx-codegen-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_codegen}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/\n', 'ed6de06e34e2634c28a24e8a1db99b06', '2022-12-16 10:44:25', '2024-02-22 15:08:09', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (4, 'pigx-daemon-elastic-job-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_job}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true', '09996f46e0b80d7b2aa320ef4cb31c82', '2022-12-16 10:44:25', '2023-04-07 14:14:50', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (5, 'pigx-gateway-dev.yml', 'DEFAULT_GROUP', '# 固定路由转发配置 无修改\nspring:\n  cloud:\n    gateway:\n      routes:\n        - id: openapi\n          uri: lb://pigx-gateway\n          predicates:\n            - Path=/v3/api-docs/**\n          filters:\n            - RewritePath=/v3/api-docs/(?<path>.*), /$\\{path}/$\\{path}/v3/api-docs\n\n# gateway 刷新端点\nmanagement:\n  endpoint:\n    gateway:\n      enabled: true\n', 'd9454d877ff3d8bd3fd0d5be99810195', '2022-12-16 10:44:25', '2024-04-20 22:47:02', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (6, 'pigx-monitor-dev.yml', 'DEFAULT_GROUP', 'spring:\n  # 安全配置\n  security:\n    user:\n      name: ENC(rZHA4LW5hHmhLAAzJoFNag==)     # pigx\n      password: ENC(bjeyh+Aeii3kHXkoo00ZUw==) # pigx\n  autoconfigure:\n    exclude: com.pig4cloud.pigx.common.core.config.JacksonConfiguration\n  boot:\n    admin:\n      ui:\n        title: \'pigx 服务状态监控\'\n        brand: \'pigx 服务状态监控\'\n        external-views:\n          - label: \"SQL监控\"\n            url: /druid/sql.html\n            order: 2000\n            iframe: true\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS  #显示详细信息\n\n\n# druid 监控的服务\nmonitor:\n  applications:\n    - pigx-upms-biz\n', '2bb39e4dee3f90d4186d1e42aa666d2a', '2022-12-16 10:44:25', '2023-04-07 14:12:09', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (7, 'pigx-upms-biz-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      ignore-urls:\n        - /druid/**\n\n# 数据源\nspring:\n  autoconfigure:\n    exclude: org.springframework.cloud.gateway.config.GatewayAutoConfiguration,org.springframework.cloud.gateway.config.GatewayClassPathWarningAutoConfiguration\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n      stat-view-servlet:\n        enabled: true\n        allow: \"\"\n        url-pattern: /druid/*\n        #login-username: admin\n        #login-password: admin\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true\n          slow-sql-millis: 10000\n          merge-sql: false\n        wall:\n          config:\n            multi-statement-allow: true\n\n# 本地文件系统\nfile:\n  local:\n    enable: true\n    basePath: /Users/lengleng/Downloads/files', '28f2305c1cfb413dcc45a3d432ef7098', '2022-12-16 10:44:25', '2024-02-22 15:08:36', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (8, 'pigx-daemon-quartz-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_job}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/', '79619124a30cdb6e8bc1046aaa18d91c', '2022-12-16 10:44:25', '2024-04-19 21:49:00', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (9, 'pigx-pay-platform-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_pay}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  freemarker:\n    allow-request-override: false\n    allow-session-override: false\n    cache: true\n    charset: UTF-8\n    check-template-location: true\n    content-type: text/html\n    enabled: true\n    expose-request-attributes: false\n    expose-session-attributes: false\n    expose-spring-macro-helpers: true\n    prefer-file-system-access: true\n    suffix: .ftl\n    template-loader-path: classpath:/templates/\n# 租户表维护\npigx:\n  pay:\n    test: lengleng\n    aliPayConfig:\n      expire-time: 30\n      return-url: http://pig4cloud.com\n      notify-url: http://payx.yunjihuitong.com/pay/notify/ali/callbak\n    wxPayConfig:\n      notify-url: https://admin.pig4cloud.com/pay/notify/wx/callbak\n    mergePayConfig:\n      return-url: http://pig4cloud.com\n      notify-url: http://wechat.pigx.top/pay/notify/merge/callbak\n  xsequence:    #发号器相关配置\n    db:\n      retry-times: 3\n      table-name: pay_sequence', 'f4e5734a99f3c1f059071deac081b33d', '2022-12-16 10:44:25', '2024-02-22 15:09:08', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (11, 'pigx-mp-platform-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_mp}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/', 'd5936e0c352fb1fbe1d05f054f14c98a', '2022-12-16 10:44:25', '2024-02-22 15:09:20', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (12, 'pigx-xxl-job-admin-dev.yml', 'DEFAULT_GROUP', '# xxl\nxxl:\n  job:\n    i18n: zh_CN\n    logretentiondays: 30\n    triggerpool:\n      fast.max: 200\n      slow.max: 200\n\n# mybatis\nmybatis:\n  mapper-locations: classpath:/mybatis-mapper/*Mapper.xml\n\n# spring\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_job}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  mvc:\n    static-path-pattern: /static/**\n  freemarker:\n    suffix: .ftl\n    request-context-attribute: request\n    settings:\n      number_format: 0.##########\n  mail:\n    host: smtp.mxhichina.com\n    port: 465\n    from: xxxx@gitee.wang\n    username: xxxx@gitee.wang\n    password: xxxx\n    properties:\n      mail:\n        smtp:\n          auth: true\n          ssl.enable: true\n          starttls.enable: false\n          required: false\n\nmanagement:\n  health:\n    mail:\n      enabled: false\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS', '6d70dce1f3ee48f261dce29bcfa99cbb', '2022-12-16 10:44:25', '2023-01-04 18:22:48', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (13, 'pigx-report-platform-dev.yml', 'DEFAULT_GROUP', 'spring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_report}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  jpa:\n    database-platform: org.hibernate.dialect.MySQL8Dialect\n\nsecurity:\n  oauth2:\n    client:\n      ignore-urls:\n        - /\n        - /api/project/getData\n        - /static/**\n        - /api/project/get-file/*\n\n# 文件上传路径\ngv:\n  img-path: /Users/lengleng/Downloads/img/\n', 'ce3fde4b4592cdd61feb2e443b2ddfe5', '2022-12-16 10:44:25', '2023-04-07 12:56:57', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (14, 'pigx-jimu-platform-dev.yml', 'DEFAULT_GROUP', 'spring:\n  #配置静态资源\n  mvc:\n    static-path-pattern: /**\n  resource:\n    static-locations: classpath:/static/\n  #配置数据库\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_bi}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n   \n#JimuReport[minidao配置]\nminidao :\n  base-package: org.jeecg.modules.jmreport.desreport.dao*\n  db-type: mysql\n#JimuReport[上传配置]\njeecg :\n  jmreport:\n    customPrePath: /api/jimu\n    # 自动保存\n    autoSave: true\n    # 单位毫秒 默认5*60*1000 \n    interval: 10000\n  # local|minio|alioss\n  uploadType: local\n  # local\n  path :\n    #文件路径A\n    upload: ~/jimu/data\n  # alioss\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    staticDomain: ??\n    bucketName: ??\n  # minio\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: ??\n#输出sql日志\nlogging:\n  level:\n    org.jeecg.modules.jmreport : debug', '66319da04963c4c7217f807bf1bd5f2d', '2022-12-16 10:44:25', '2024-01-30 20:44:36', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (15, 'pigx-app-server-biz-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_app}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true', 'e4668a1854e92a5a666e83479ef7d26a', '2022-12-19 10:30:07', '2024-02-22 15:09:50', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (16, 'pigx-flow-engine-biz-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_flow}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&nullCatalogMeansCurrent=true', '233121e20445609e1ba378c3686ea7d5', '2023-07-28 13:41:32', '2023-07-28 13:43:36', 'nacos', '0:0:0:0:0:0:0:1', '', '', 'flowable 工作引擎', '', '', 'yaml', '', '');
INSERT INTO `config_info` (`id`, `data_id`, `group_id`, `content`, `md5`, `gmt_create`, `gmt_modified`, `src_user`, `src_ip`, `app_name`, `tenant_id`, `c_desc`, `c_use`, `effect`, `type`, `c_schema`, `encrypted_data_key`) VALUES (17, 'pigx-flow-task-biz-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_flow}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&nullCatalogMeansCurrent=true', '233121e20445609e1ba378c3686ea7d5', '2023-07-28 13:44:25', '2024-02-22 15:09:34', 'nacos', '0:0:0:0:0:0:0:1', '', '', 'flowable 业务', '', '', 'yaml', '', '');
COMMIT;

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` varchar(1024) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '密钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_gray
-- ----------------------------
DROP TABLE IF EXISTS `config_info_gray`;
CREATE TABLE `config_info_gray` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) NOT NULL COMMENT 'group_id',
  `content` longtext NOT NULL COMMENT 'content',
  `md5` varchar(32) DEFAULT NULL COMMENT 'md5',
  `src_user` text COMMENT 'src_user',
  `src_ip` varchar(100) DEFAULT NULL COMMENT 'src_ip',
  `gmt_create` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_create',
  `gmt_modified` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT 'gmt_modified',
  `app_name` varchar(128) DEFAULT NULL COMMENT 'app_name',
  `tenant_id` varchar(128) DEFAULT '' COMMENT 'tenant_id',
  `gray_name` varchar(128) NOT NULL COMMENT 'gray_name',
  `gray_rule` text NOT NULL COMMENT 'gray_rule',
  `encrypted_data_key` varchar(256) NOT NULL DEFAULT '' COMMENT 'encrypted_data_key',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfogray_datagrouptenantgray` (`data_id`,`group_id`,`tenant_id`,`gray_name`),
  KEY `idx_dataid_gmt_modified` (`data_id`,`gmt_modified`),
  KEY `idx_gmt_modified` (`gmt_modified`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='config_info_gray';

-- ----------------------------
-- Records of config_info_gray
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_tag';

-- ----------------------------
-- Records of config_info_tag
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_tags_relation
-- ----------------------------
DROP TABLE IF EXISTS `config_tags_relation`;
CREATE TABLE `config_tags_relation` (
  `id` bigint NOT NULL COMMENT 'id',
  `tag_name` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增长标识',
  PRIMARY KEY (`nid`),
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_tag_relation';

-- ----------------------------
-- Records of config_tags_relation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for group_capacity
-- ----------------------------
DROP TABLE IF EXISTS `group_capacity`;
CREATE TABLE `group_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='集群、各Group容量信息表';

-- ----------------------------
-- Records of group_capacity
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for his_config_info
-- ----------------------------
DROP TABLE IF EXISTS `his_config_info`;
CREATE TABLE `his_config_info` (
  `id` bigint unsigned NOT NULL COMMENT 'id',
  `nid` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'nid, 自增标识',
  `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `src_user` text COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `op_type` char(10) COLLATE utf8_bin DEFAULT NULL COMMENT 'operation type',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  `encrypted_data_key` varchar(1024) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '密钥',
  `publish_type` varchar(50) COLLATE utf8_bin DEFAULT 'formal' COMMENT 'publish type gray or formal',
  `gray_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'gray name',
  `ext_info` longtext COLLATE utf8_bin COMMENT 'ext info',
  PRIMARY KEY (`nid`),
  KEY `idx_gmt_create` (`gmt_create`),
  KEY `idx_gmt_modified` (`gmt_modified`),
  KEY `idx_did` (`data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `role` varchar(50) NOT NULL COMMENT 'role',
  `resource` varchar(128) NOT NULL COMMENT 'resource',
  `action` varchar(8) NOT NULL COMMENT 'action',
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permissions
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for roles
-- ----------------------------
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
  `username` varchar(50) NOT NULL COMMENT 'username',
  `role` varchar(50) NOT NULL COMMENT 'role',
  UNIQUE KEY `idx_user_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of roles
-- ----------------------------
BEGIN;
INSERT INTO `roles` (`username`, `role`) VALUES ('nacos', 'ROLE_ADMIN');
COMMIT;

-- ----------------------------
-- Table structure for tenant_capacity
-- ----------------------------
DROP TABLE IF EXISTS `tenant_capacity`;
CREATE TABLE `tenant_capacity` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='租户容量信息表';

-- ----------------------------
-- Records of tenant_capacity
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info`;
CREATE TABLE `tenant_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `kp` varchar(128) COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`),
  KEY `idx_tenant_id` (`tenant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='tenant_info';

-- ----------------------------
-- Records of tenant_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `username` varchar(50) NOT NULL COMMENT 'username',
  `password` varchar(500) NOT NULL COMMENT 'password',
  `enabled` tinyint(1) NOT NULL COMMENT 'enabled',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` (`username`, `password`, `enabled`) VALUES ('nacos', '$2a$10$W6PKgRTzXUp6R/NY853Kn.nRaIcX3whIMTZ/WWkNqo2MTOeSBjKJq', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
