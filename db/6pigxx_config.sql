USE pigxx_config;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
                               `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
                               `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
                               `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
                               `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
                               `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
                               `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
                               `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
                               `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
                               `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
                               `c_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
                               `c_use` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
                               `effect` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
                               `type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
                               `c_schema` text CHARACTER SET utf8 COLLATE utf8_bin,
                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1006 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
BEGIN;
INSERT INTO `config_info` VALUES (1, 'application-dev.yml', 'DEFAULT_GROUP', '# 配置文件加密根密码\njasypt:\n  encryptor:\n    password: pigx\n    algorithm: PBEWithMD5AndDES\n    iv-generator-classname: org.jasypt.iv.NoIvGenerator\n\nspring:\n  redis:\n    host: pigx-redis\n  servlet:\n    multipart:\n      max-file-size: 100MB\n      max-request-size: 100MB\n  cloud:\n    sentinel:\n      eager: true\n      transport:\n        dashboard: pigx-sentinel:5020\n\n# 端点对外暴露\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'          \n  endpoint:\n    restart:\n      enabled: true\n    health:\n      show-details: ALWAYS\n# feign 相关配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 20000\n        readTimeout: 20000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n#开启灰度\ngray:\n  rule:\n    enabled: true\n\n# mybatis-plus 配置\nmybatis-plus:\n  tenant-enable: ture\n  mapper-locations: classpath:/mapper/*Mapper.xml\n  global-config:\n    capitalMode: true\n    banner: false\n    db-config:\n      id-type: auto\n      select-strategy: not_empty\n      insert-strategy: not_empty\n      update-strategy: not_null\n  type-handlers-package:  com.pig4cloud.pigx.common.data.handler\n  configuration:\n    jdbc-type-for-null: \'null\'\n    call-setters-on-nulls: true\n# swagger 全局配置\nknife4j:\n  enable: true\nswagger:\n  enabled: true\n  title: PigX Swagger API\n  license: Powered By PigX\n  licenseUrl: https://pig4cloud.com/\n  terms-of-service-url: https://pig4cloud.com/\n  contact:\n    email: wangiegie@gmail.com\n    url: https://pig4cloud.com/about.html\n  authorization:\n    name: oauth2\n    auth-regex: ^.*$\n    authorization-scope-list:\n      - scope: server\n        description: server all\n    token-url-list:\n      - http://${GATEWAY_HOST:pigx-gateway}:${GATEWAY_PORT:9999}/auth/oauth/token\n\n# oauth2 资源服务器相关配置\nsecurity:\n  oauth2:\n    client:\n      ignore-urls:\n        - /css/**\n        - /error\n        - /actuator/**\n        - /v2/api-docs\n    resource:\n      loadBalanced: true\n      token-info-uri: http://pigx-auth/oauth/check_token', '01f97b71c9b83d7efd03d6f56881c3db', '2019-04-18 02:10:20', '2022-08-07 14:05:10', 'nacos', '127.0.0.1', '', '', '通用配置文件', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (2, 'pigx-auth-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  freemarker:\n    allow-request-override: false\n    allow-session-override: false\n    cache: true\n    charset: UTF-8\n    check-template-location: true\n    content-type: text/html\n    enabled: true\n    expose-request-attributes: false\n    expose-session-attributes: false\n    expose-spring-macro-helpers: true\n    prefer-file-system-access: true\n    suffix: .ftl\n    template-loader-path: classpath:/templates/', '74f53b71c7799aa754da75662378b93c', '2019-04-18 02:11:32', '2021-05-07 23:40:38', NULL, '10.1.1.143', '', '', '认证中心配置文件', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (3, 'pigx-codegen-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      client-secret: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      scope: server\n# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_codegen}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/\n\n# 租户表维护\npigx:\n  tenant:\n    column: tenant_id\n    tables:\n      - gen_datasource_conf\n      - gen_form_conf', 'ed93a557f5709d65bb45c14e80c7e9e1', '2019-04-18 02:12:10', '2022-01-27 21:27:50', 'nacos', '127.0.0.1', '', '', '代码生成', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (4, 'pigx-daemon-elastic-job-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(tz2NM4GcmnE7sNJTYL8ZSg==)\n      client-secret: ENC(tz2NM4GcmnE7sNJTYL8ZSg==)\n      scope: server\n## 定时任务\n# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_job}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true', '4124589805657db024b20772cccf57f5', '2019-04-18 02:12:57', '2020-11-09 16:18:05', NULL, '172.16.1.198', '', '', '定时任务-elastic-job配置', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (5, 'pigx-gateway-dev.yml', 'DEFAULT_GROUP', 'gateway:\n  encode-key: \'pigxpigxpigxpigx\'\n\nswagger:\n  ignore-providers:\n    - pigx-monitor\n    - pigx-auth\n    - pigx-tx-manager\n\nribbon:\n  rule:\n    gray-enabled: true\n\n# 验证码相关配置参考： http://t.cn/A647jEdu\naj:\n  captcha:\n    cache-type: redis\n    water-mark: pig4cloud', 'cd9f8a6a46e2c128f3b172be76ea3221', '2019-04-18 02:13:52', '2021-11-09 12:09:34', '', '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (6, 'pigx-monitor-dev.yml', 'DEFAULT_GROUP', 'spring:\n  # 安全配置\n  security:\n    user:\n      name: ENC(rZHA4LW5hHmhLAAzJoFNag==)     # pigx\n      password: ENC(bjeyh+Aeii3kHXkoo00ZUw==) # pigx\n  autoconfigure:\n    exclude: com.pig4cloud.pigx.common.core.config.JacksonConfiguration\n  boot:\n    admin:\n      ui:\n        title: \'pigx 服务状态监控\'\n        brand: \'pigx 服务状态监控\'\n        external-views:\n          - label: \"SQL监控\"\n            url: /druid/sql.html\n            order: 2000\n            iframe: true\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS  #显示详细信息\n\n\n# druid 监控的服务\nmonitor:\n  applications:\n    - pigx-upms-biz\n    - pigx-pay-platform\n', 'cbc0b536fe11eeff0dcf81be11dab6e4', '2019-04-18 02:14:17', '2022-02-18 13:44:07', 'nacos', '127.0.0.1', '', '', '监控配置文件', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (7, 'pigx-upms-biz-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(ltJPpR50wT0oIY9kfOe1Iw==)\n      client-secret: ENC(ltJPpR50wT0oIY9kfOe1Iw==)\n      scope: server\n      ignore-urls:\n        - /error\n        - /druid/**\n        - /actuator/**\n        - /v2/api-docs\n\n# 数据源\nspring:\n  autoconfigure:\n    exclude: org.springframework.cloud.gateway.config.GatewayAutoConfiguration,org.springframework.cloud.gateway.config.GatewayClassPathWarningAutoConfiguration\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n      stat-view-servlet:\n        enabled: true\n        allow: \"\"\n        url-pattern: /druid/*\n        #login-username: admin\n        #login-password: admin\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true\n          slow-sql-millis: 10000\n          merge-sql: false\n        wall:\n          config:\n            multi-statement-allow: true\n\n# 本地文件系统\nfile:\n  local:\n    enable: true\n    basePath: /data/file\n\n# Logger Config\nlogging:\n  level:\n    com.pig4cloud.pigx.admin.mapper: debug\n\n# 租户表维护\npigx:\n  tenant:\n    column: tenant_id\n    tables:\n      - sys_user\n      - sys_role\n      - sys_menu\n      - sys_dept\n      - sys_log\n      - sys_social_details\n      - sys_dict\n      - sys_dict_item\n      - sys_public_param\n      - sys_log\n      - sys_file\n      - sys_oauth_client_details\n      - sys_post', '9762403bc4feb48532167e8df21557d4', '2019-04-18 02:32:44', '2022-05-12 17:58:58', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (9, 'pigx-daemon-quartz-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(tz2NM4GcmnE7sNJTYL8ZSg==)\n      client-secret: ENC(tz2NM4GcmnE7sNJTYL8ZSg==)\n      scope: server\n\n# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_job}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/\n  quartz:\n    #相关属性配置\n    properties:\n      org:\n        quartz:\n          scheduler:\n            instanceName: clusteredScheduler\n            instanceId: AUTO\n          jobStore:\n            class: org.springframework.scheduling.quartz.LocalDataSourceJobStore\n            driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate\n            tablePrefix: QRTZ_\n            isClustered: true\n            clusterCheckinInterval: 10000\n            useProperties: false\n          threadPool:\n            class: org.quartz.simpl.SimpleThreadPool\n            threadCount: 50\n            threadPriority: 5\n            threadsInheritContextClassLoaderOfInitializingThread: true\n    #数据库方式\n    job-store-type: jdbc\n    #初始化表结构\n    #jdbc:\n    #initialize-schema: never\n\n', '9ff6e1d6f4d7f8776cb13e31c9dc8ce9', '2019-04-18 03:08:34', '2022-02-18 14:44:22', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (10, 'pigx-pay-platform-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      client-secret: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      scope: server\n      ignore-urls:\n        - /druid/**\n        - /error\n        - /actuator/**\n        - /v2/api-docs\n# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_pay}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  freemarker:\n    allow-request-override: false\n    allow-session-override: false\n    cache: true\n    charset: UTF-8\n    check-template-location: true\n    content-type: text/html\n    enabled: true\n    expose-request-attributes: false\n    expose-session-attributes: false\n    expose-spring-macro-helpers: true\n    prefer-file-system-access: true\n    suffix: .ftl\n    template-loader-path: classpath:/templates/\n# 租户表维护\npigx:\n  pay:\n    test: lengleng\n    aliPayConfig:\n      expire-time: 30\n      return-url: http://pig4cloud.com\n      notify-url: http://wechat.pigx.top/pay/notify/ali/callbak\n    wxPayConfig:\n      notify-url: https://admin.pig4cloud.com/pay/notify/wx/callbak\n    mergePayConfig:\n      return-url: http://pig4cloud.com\n      notify-url: http://wechat.pigx.top/pay/notify/merge/callbak\n  xsequence:    #发号器相关配置\n    db:\n      retry-times: 3\n      table-name: pay_sequence\n  tenant:\n    column: tenant_id\n    tables:\n      - pay_channel\n      - pay_trade_order\n      - pay_goods_order\n      - pay_notify_record', 'ee15c87a8ec17f7272d0f2f4597ee764', '2019-05-28 11:46:27', '2022-02-18 14:44:43', 'nacos', '127.0.0.1', '', '', '支付模块配置', 'null', 'null', 'yaml', 'null');
INSERT INTO `config_info` VALUES (11, 'pigx-oa-platform-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      client-secret: ENC(gPFcUOmJm8WqM3k3eSqS0Q==)\n      scope: server\n      ignore-urls:\n        - \'/actuator/**\'\n        - \'/v2/api-docs\'\n        - \'/editor-app/**\'\n        - \'/modeler.html\'\n        \nspring:\n  autoconfigure:\n    exclude: org.activiti.spring.boot.SecurityAutoConfiguration\n  activiti:\n    check-process-definitions: false\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_ac}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n\n# 租户表维护\npigx:\n  tenant:\n    column: tenant_id\n    tables:\n      - oa_leave_bill', '60d4303ef8696b3cad851f7cfefa7059', '2019-10-16 16:38:52', '2021-11-05 22:46:22', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (12, 'pigx-mp-platform-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(vW+Nup9LaTfIFwgufUBsYg==)\n      client-secret: ENC(vW+Nup9LaTfIFwgufUBsYg==)\n      scope: server\n# 数据源\nspring:\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_mp}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/\n\n\n# 租户表维护\npigx:\n  tenant:\n    column: tenant_id\n    tables:\n      - wx_mp_menu\n      - wx_account\n      - wx_account_tag\n      - wx_account_fans\n      - wx_msg\n      - wx_auto_reply', 'd3f9d0ebd82dc5727cf6b3e63b669931', '2019-10-21 08:58:24', '2022-01-05 11:02:19', '', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (14, 'pigx-xxl-job-admin-dev.yml', 'DEFAULT_GROUP', '# xxl\nxxl:\n  job:\n    i18n: zh_CN\n    logretentiondays: 30\n    triggerpool:\n      fast.max: 200\n      slow.max: 200\n\n# mybatis\nmybatis:\n  mapper-locations: classpath:/mybatis-mapper/*Mapper.xml\n\n# spring\nspring:\n  datasource:\n    driver-class-name: org.h2.Driver  \n    url: jdbc:h2:file:~/pigxx_job;MODE=MYSQL;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE;\n  sql:\n    init:\n      mode: always\n      schema-locations: classpath:db/schema-h2.sql\n      data-locations: classpath:db/data-h2.sql\n  mvc:\n    static-path-pattern: /static/**\n  freemarker:\n    suffix: .ftl\n    request-context-attribute: request\n    settings:\n      number_format: 0.##########\n  mail:\n    host: smtp.mxhichina.com\n    port: 465\n    from: xxxx@gitee.wang\n    username: xxxx@gitee.wang\n    password: xxxx\n    properties:\n      mail:\n        smtp:\n          auth: true\n          ssl.enable: true\n          starttls.enable: false\n          required: false\n# spring boot admin 配置\n\nmanagement:\n  health:\n    mail:\n      enabled: false\n  endpoints:\n    web:\n      exposure:\n        include: \'*\'\n  endpoint:\n    health:\n      show-details: ALWAYS', '2a08e1ac7a4f2f079550c394d097b520', '2021-07-21 20:45:02', '2021-12-27 21:53:18', 'nacos', '127.0.0.1', '', '', '', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (15, 'pigx-report-platform-dev.yml', 'DEFAULT_GROUP', 'spring:\n  messages:\n    basename: i18n/messages\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_report}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\nmybatis-plus:\n  configuration:\n    call-setters-on-nulls: true\n  mapperLocations:\n    - classpath*:/mapper/**/*.xml\n    - classpath*:/modeler-mybatis-mappings/**/*.xml\n\n  \n# 本应用自定义参数\ncustomer:\n  file:\n    #上传对应本地全路径\n    dist-path: /Users/lengleng/Downloads/file/\n    white-list: .png|.jpg|.gif|.icon|.pdf|.xlsx|.xls|.csv|.mp4|.avi\n    excelSuffix: .xlsx|.xls|.csv\n    downloadPath: http://127.0.0.1:9095/file/download\n', 'da03b3efe2b4ac44745cadc2a23328ab', '2021-11-05 09:13:54', '2022-02-18 14:39:12', 'nacos', '127.0.0.1', '', '', '大屏设计模块配置文件', '', '', 'yaml', '');
INSERT INTO `config_info` VALUES (16, 'pigx-jimu-platform-dev.yml', 'DEFAULT_GROUP', 'spring:\n  #配置静态资源\n  mvc:\n    static-path-pattern: /**\n  resource:\n    static-locations: classpath:/static/\n  #配置数据库\n  datasource:\n    type: com.alibaba.druid.pool.DruidDataSource\n    druid:\n      driver-class-name: com.mysql.cj.jdbc.Driver\n      username: ${MYSQL_USER:root}\n      password: ${MYSQL_PWD:root}\n      url: jdbc:mysql://${MYSQL_HOST:pigx-mysql}:${MYSQL_PORT:3306}/${MYSQL_DB:pigxx_bi}?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=GMT%2B8&allowMultiQueries=true&allowPublicKeyRetrieval=true\n   \n#JimuReport[minidao配置]\nminidao :\n  base-package: org.jeecg.modules.jmreport.desreport.dao*\n  db-type: mysql\n#JimuReport[上传配置]\njeecg :\n  jmreport:\n    customPrePath: /jimu\n    # 自动保存\n    autoSave: true\n    # 单位毫秒 默认5*60*1000 \n    interval: 10000\n  # local|minio|alioss\n  uploadType: local\n  # local\n  path :\n    #文件路径A\n    upload: ~/jimu/data\n  # alioss\n  oss:\n    endpoint: oss-cn-beijing.aliyuncs.com\n    accessKey: ??\n    secretKey: ??\n    staticDomain: ??\n    bucketName: ??\n  # minio\n  minio:\n    minio_url: http://minio.jeecg.com\n    minio_name: ??\n    minio_pass: ??\n    bucketName: ??\n#输出sql日志\nlogging:\n  level:\n    org.jeecg.modules.jmreport : debug', '423f74383b37d44b24832570ffd54f3c', '2022-04-15 22:45:41', '2022-04-15 22:45:41', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL);
COMMIT;

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `datum_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'datum_id',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '内容',
  `gmt_modified` datetime NOT NULL COMMENT '修改时间',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`,`group_id`,`tenant_id`,`datum_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='增加租户字段';

-- ----------------------------
-- Records of config_info_aggr
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_beta
-- ----------------------------
DROP TABLE IF EXISTS `config_info_beta`;
CREATE TABLE `config_info_beta` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `beta_ips` varchar(1024) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'betaIps',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info_beta';

-- ----------------------------
-- Records of config_info_beta
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for config_info_tag
-- ----------------------------
DROP TABLE IF EXISTS `config_info_tag`;
CREATE TABLE `config_info_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tag_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_id',
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'content',
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin COMMENT 'source user',
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`,`group_id`,`tenant_id`,`tag_id`) USING BTREE
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
  `tag_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'tag_name',
  `tag_type` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tag_type',
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'data_id',
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'group_id',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `nid` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`nid`) USING BTREE,
  UNIQUE KEY `uk_configtagrelation_configidtag` (`id`,`tag_name`,`tag_type`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
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
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_group_id` (`group_id`) USING BTREE
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
  `id` bigint unsigned NOT NULL,
  `nid` bigint unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `group_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `app_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
  `content` longtext CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `md5` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `src_user` text CHARACTER SET utf8 COLLATE utf8_bin,
  `src_ip` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `op_type` char(10) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
  PRIMARY KEY (`nid`) USING BTREE,
  KEY `idx_gmt_create` (`gmt_create`) USING BTREE,
  KEY `idx_gmt_modified` (`gmt_modified`) USING BTREE,
  KEY `idx_did` (`data_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `role` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `resource` varchar(512) CHARACTER SET utf8mb4 NOT NULL,
  `action` varchar(8) CHARACTER SET utf8mb4 NOT NULL,
  UNIQUE KEY `uk_role_permission` (`role`,`resource`,`action`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `username` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `role` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  UNIQUE KEY `uk_username_role` (`username`,`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
  `quota` int unsigned NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
  `usage` int unsigned NOT NULL DEFAULT '0' COMMENT '使用量',
  `max_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
  `max_aggr_count` int unsigned NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
  `max_aggr_size` int unsigned NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
  `max_history_count` int unsigned NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00' COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_id` (`tenant_id`) USING BTREE
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
  `kp` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT 'kp',
  `tenant_id` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_id',
  `tenant_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT '' COMMENT 'tenant_name',
  `tenant_desc` varchar(256) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'tenant_desc',
  `create_source` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'create_source',
  `gmt_create` bigint NOT NULL COMMENT '创建时间',
  `gmt_modified` bigint NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`,`tenant_id`) USING BTREE,
  KEY `idx_tenant_id` (`tenant_id`) USING BTREE
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
  `username` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `password` varchar(500) CHARACTER SET utf8mb4 NOT NULL,
  `enabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
