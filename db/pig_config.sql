DROP DATABASE IF EXISTS `pig_config`;

CREATE DATABASE  `pig_config` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE pig_config;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                               `data_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'data_id',
                               `group_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
                               `content` longtext COLLATE utf8_bin NOT NULL COMMENT 'content',
                               `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT 'md5',
                               `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
                               `src_user` text COLLATE utf8_bin COMMENT 'source user',
                               `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT 'source ip',
                               `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL,
                               `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
                               `c_desc` varchar(256) COLLATE utf8_bin DEFAULT NULL,
                               `c_use` varchar(64) COLLATE utf8_bin DEFAULT NULL,
                               `effect` varchar(64) COLLATE utf8_bin DEFAULT NULL,
                               `type` varchar(64) COLLATE utf8_bin DEFAULT NULL,
                               `c_schema` text COLLATE utf8_bin,
                               `encrypted_data_key` text COLLATE utf8_bin NOT NULL COMMENT '秘钥',
                               PRIMARY KEY (`id`),
                               UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='config_info';

-- ----------------------------
-- Records of config_info
-- ----------------------------
BEGIN;
INSERT INTO `config_info` VALUES (1, 'application-dev.yml', 'DEFAULT_GROUP', '# 加解密根密码\njasypt:\n  encryptor:\n    password: pig #根密码\n\n# Spring 相关\n\nspring:\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\n  redis:\n    host: pig-redis\n  cloud:\n    sentinel:\n      eager: true\n      transport:\n        dashboard: pig-sentinel:5003\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"  \n  endpoint:\n    health:\n      show-details: ALWAYS\n\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n# mybaits-plus配置\nmybatis-plus:\n  mapper-locations: classpath:/mapper/*Mapper.xml\n  global-config:\n    banner: false\n    db-config:\n      id-type: auto\n      table-underline: true\n      logic-delete-value: 1\n      logic-not-delete-value: 0\n  configuration:\n    map-underscore-to-camel-case: true\n\n# spring security 配置\nsecurity:\n  oauth2:\n    resource:\n      loadBalanced: true\n      token-info-uri: http://pig-auth/oauth/check_token\n    # 通用放行URL，服务个性化，请在对应配置文件覆盖\n    ignore:\n      urls:\n        - /v3/api-docs\n        - /actuator/**\n\n# swagger 配置\nswagger:\n  enabled: true\n  title: Pig Swagger API\n  gateway: http://${GATEWAY_HOST:pig-gateway}:${GATEWAY-PORT:9999}\n  token-url: ${swagger.gateway}/auth/oauth/token\n  services:\n    pig-upms-biz: admin\n    pig-codegen: gen', '8d9ffc03cef9e316905d4f86d1914e6b', '2022-05-08 12:10:37', '2022-05-08 12:10:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (2, 'pig-auth-dev.yml', 'DEFAULT_GROUP', '# 数据源\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: root\n    url: jdbc:mysql://pig-mysql:3306/pig?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true\n  freemarker:\n    allow-request-override: false\n    allow-session-override: false\n    cache: true\n    charset: UTF-8\n    check-template-location: true\n    content-type: text/html\n    enabled: true\n    expose-request-attributes: false\n    expose-session-attributes: false\n    expose-spring-macro-helpers: true\n    prefer-file-system-access: true\n    suffix: .ftl\n    template-loader-path: classpath:/templates/', 'd92c284f687ae64ac9661f6313994545', '2022-05-08 12:10:37', '2022-05-08 12:10:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (3, 'pig-codegen-dev.yml', 'DEFAULT_GROUP', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(27v1agvAug87ANOVnbKdsw==)\n      client-secret: ENC(VbnkopxrwgbFVKp+UxJ2pg==)\n      scope: server\n\n# 数据源配置\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: root\n    url: jdbc:mysql://pig-mysql:3306/pig_codegen?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/\n\n# 直接放行URL\nignore:\n  urls:\n    - /v2/api-docs\n    - /actuator/**\n', '02fe9e81c41986626067b8cbe589b77f', '2022-05-08 12:10:37', '2022-05-08 12:10:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (4, 'pig-gateway-dev.yml', 'DEFAULT_GROUP', 'spring:\n  cloud:\n    gateway:\n      locator:\n        enabled: true\n      routes:\n        # 认证中心\n        - id: pig-auth\n          uri: lb://pig-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - ValidateCodeGatewayFilter\n            # 前端密码解密\n            - PasswordDecoderFilter\n        #UPMS 模块\n        - id: pig-upms-biz\n          uri: lb://pig-upms-biz\n          predicates:\n            - Path=/admin/**\n          filters:\n            # 限流配置\n            - name: RequestRateLimiter\n              args:\n                key-resolver: \'#{@remoteAddrKeyResolver}\'\n                redis-rate-limiter.replenishRate: 100\n                redis-rate-limiter.burstCapacity: 200\n        # 代码生成模块\n        - id: pig-codegen\n          uri: lb://pig-codegen\n          predicates:\n            - Path=/gen/**\n        # demo模块\n        - id: pig-demo\n          uri: lb://demo-biz\n          predicates:\n            - Path=/demo/**\n        - id: openapi\n          uri: http://localhost:${server.port}\n          predicates:\n            - Path=/v3/api-docs/**\n          filters:\n            - RewritePath=/v3/api-docs/(?<path>.*), /$\\{path}/v3/api-docs\n\ngateway:\n  encode-key: \'thanks,pig4cloud\'\n  ignore-clients:\n    - test', 'dbf00e83f80de7f011d244ea1ce5686e', '2022-05-08 12:10:37', '2022-05-08 12:10:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (5, 'pig-monitor-dev.yml', 'DEFAULT_GROUP', 'spring:\n  autoconfigure:\n    exclude: com.pig4cloud.pig.common.core.config.JacksonConfiguration\n  # 安全配置\n  security:\n    user:\n      name: ENC(8Hk2ILNJM8UTOuW/Xi75qg==)     # pig\n      password: ENC(o6cuPFfUevmTbkmBnE67Ow====) # pig\n', '650bdfa15f60f3faa84dfe6e6878b8cf', '2022-05-08 12:10:37', '2022-05-08 12:10:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
INSERT INTO `config_info` VALUES (6, 'pig-upms-biz-dev.yml', 'DEFAULT_GROUP', 'security:\n  oauth2:\n    client:\n      client-id: ENC(imENTO7M8bLO38LFSIxnzw==)\n      client-secret: ENC(i3cDFhs26sa2Ucrfz2hnQw==)\n      scope: server\n\n# 数据源\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: root\n    url: jdbc:mysql://pig-mysql:3306/pig?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowMultiQueries=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true\n\n# 文件上传相关 支持阿里云、华为云、腾讯、minio\noss:\n  endpoint: http://minio.pig4cloud.com\n  accessKey: lengleng\n  secretKey: lengleng\n  bucket-name: tmp', '107614b40932e8237787b769e0937ed2', '2022-05-08 12:10:37', '2022-05-08 12:10:37', NULL, '127.0.0.1', '', '', NULL, NULL, NULL, 'yaml', NULL, '');
COMMIT;

-- ----------------------------
-- Table structure for config_info_aggr
-- ----------------------------
DROP TABLE IF EXISTS `config_info_aggr`;
CREATE TABLE `config_info_aggr` (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
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
                                    `encrypted_data_key` text COLLATE utf8_bin NOT NULL COMMENT '秘钥',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`,`group_id`,`tenant_id`)
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
                                        `nid` bigint NOT NULL AUTO_INCREMENT,
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
                                   `id` bigint unsigned NOT NULL,
                                   `nid` bigint unsigned NOT NULL AUTO_INCREMENT,
                                   `data_id` varchar(255) COLLATE utf8_bin NOT NULL,
                                   `group_id` varchar(128) COLLATE utf8_bin NOT NULL,
                                   `app_name` varchar(128) COLLATE utf8_bin DEFAULT NULL COMMENT 'app_name',
                                   `content` longtext COLLATE utf8_bin NOT NULL,
                                   `md5` varchar(32) COLLATE utf8_bin DEFAULT NULL,
                                   `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   `src_user` text COLLATE utf8_bin,
                                   `src_ip` varchar(50) COLLATE utf8_bin DEFAULT NULL,
                                   `op_type` char(10) COLLATE utf8_bin DEFAULT NULL,
                                   `tenant_id` varchar(128) COLLATE utf8_bin DEFAULT '' COMMENT '租户字段',
                                   `encrypted_data_key` text COLLATE utf8_bin NOT NULL COMMENT '秘钥',
                                   PRIMARY KEY (`nid`),
                                   KEY `idx_gmt_create` (`gmt_create`),
                                   KEY `idx_gmt_modified` (`gmt_modified`),
                                   KEY `idx_did` (`data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='多租户改造';

-- ----------------------------
-- Records of his_config_info
-- ----------------------------
BEGIN;
INSERT INTO `his_config_info` VALUES (0, 1, 'application-dev.yml', 'DEFAULT_GROUP', '', '# 加解密根密码\njasypt:\n  encryptor:\n    password: pig #根密码\n\n# Spring 相关\n\nspring:\n  mvc:\n    pathmatch:\n      matching-strategy: ant_path_matcher\n  redis:\n    host: pig-redis\n  cloud:\n    sentinel:\n      eager: true\n      transport:\n        dashboard: pig-sentinel:5003\n\n# 暴露监控端点\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"  \n  endpoint:\n    health:\n      show-details: ALWAYS\n\n\n# feign 配置\nfeign:\n  sentinel:\n    enabled: true\n  okhttp:\n    enabled: true\n  httpclient:\n    enabled: false\n  client:\n    config:\n      default:\n        connectTimeout: 10000\n        readTimeout: 10000\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n\n# mybaits-plus配置\nmybatis-plus:\n  mapper-locations: classpath:/mapper/*Mapper.xml\n  global-config:\n    banner: false\n    db-config:\n      id-type: auto\n      table-underline: true\n      logic-delete-value: 1\n      logic-not-delete-value: 0\n  configuration:\n    map-underscore-to-camel-case: true\n\n# spring security 配置\nsecurity:\n  oauth2:\n    resource:\n      loadBalanced: true\n      token-info-uri: http://pig-auth/oauth/check_token\n    # 通用放行URL，服务个性化，请在对应配置文件覆盖\n    ignore:\n      urls:\n        - /v3/api-docs\n        - /actuator/**\n\n# swagger 配置\nswagger:\n  enabled: true\n  title: Pig Swagger API\n  gateway: http://${GATEWAY_HOST:pig-gateway}:${GATEWAY-PORT:9999}\n  token-url: ${swagger.gateway}/auth/oauth/token\n  services:\n    pig-upms-biz: admin\n    pig-codegen: gen', '8d9ffc03cef9e316905d4f86d1914e6b', '2022-05-08 12:10:36', '2022-05-08 12:10:37', NULL, '127.0.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 2, 'pig-auth-dev.yml', 'DEFAULT_GROUP', '', '# 数据源\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: root\n    url: jdbc:mysql://pig-mysql:3306/pig?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true\n  freemarker:\n    allow-request-override: false\n    allow-session-override: false\n    cache: true\n    charset: UTF-8\n    check-template-location: true\n    content-type: text/html\n    enabled: true\n    expose-request-attributes: false\n    expose-session-attributes: false\n    expose-spring-macro-helpers: true\n    prefer-file-system-access: true\n    suffix: .ftl\n    template-loader-path: classpath:/templates/', 'd92c284f687ae64ac9661f6313994545', '2022-05-08 12:10:36', '2022-05-08 12:10:37', NULL, '127.0.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 3, 'pig-codegen-dev.yml', 'DEFAULT_GROUP', '', '## spring security 配置\nsecurity:\n  oauth2:\n    client:\n      client-id: ENC(27v1agvAug87ANOVnbKdsw==)\n      client-secret: ENC(VbnkopxrwgbFVKp+UxJ2pg==)\n      scope: server\n\n# 数据源配置\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: root\n    url: jdbc:mysql://pig-mysql:3306/pig_codegen?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true\n  resources:\n    static-locations: classpath:/static/,classpath:/views/\n\n# 直接放行URL\nignore:\n  urls:\n    - /v2/api-docs\n    - /actuator/**\n', '02fe9e81c41986626067b8cbe589b77f', '2022-05-08 12:10:36', '2022-05-08 12:10:37', NULL, '127.0.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 4, 'pig-gateway-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  cloud:\n    gateway:\n      locator:\n        enabled: true\n      routes:\n        # 认证中心\n        - id: pig-auth\n          uri: lb://pig-auth\n          predicates:\n            - Path=/auth/**\n          filters:\n            # 验证码处理\n            - ValidateCodeGatewayFilter\n            # 前端密码解密\n            - PasswordDecoderFilter\n        #UPMS 模块\n        - id: pig-upms-biz\n          uri: lb://pig-upms-biz\n          predicates:\n            - Path=/admin/**\n          filters:\n            # 限流配置\n            - name: RequestRateLimiter\n              args:\n                key-resolver: \'#{@remoteAddrKeyResolver}\'\n                redis-rate-limiter.replenishRate: 100\n                redis-rate-limiter.burstCapacity: 200\n        # 代码生成模块\n        - id: pig-codegen\n          uri: lb://pig-codegen\n          predicates:\n            - Path=/gen/**\n        # demo模块\n        - id: pig-demo\n          uri: lb://demo-biz\n          predicates:\n            - Path=/demo/**\n        - id: openapi\n          uri: http://localhost:${server.port}\n          predicates:\n            - Path=/v3/api-docs/**\n          filters:\n            - RewritePath=/v3/api-docs/(?<path>.*), /$\\{path}/v3/api-docs\n\ngateway:\n  encode-key: \'thanks,pig4cloud\'\n  ignore-clients:\n    - test', 'dbf00e83f80de7f011d244ea1ce5686e', '2022-05-08 12:10:36', '2022-05-08 12:10:37', NULL, '127.0.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 5, 'pig-monitor-dev.yml', 'DEFAULT_GROUP', '', 'spring:\n  autoconfigure:\n    exclude: com.pig4cloud.pig.common.core.config.JacksonConfiguration\n  # 安全配置\n  security:\n    user:\n      name: ENC(8Hk2ILNJM8UTOuW/Xi75qg==)     # pig\n      password: ENC(o6cuPFfUevmTbkmBnE67Ow====) # pig\n', '650bdfa15f60f3faa84dfe6e6878b8cf', '2022-05-08 12:10:36', '2022-05-08 12:10:37', NULL, '127.0.0.1', 'I', '', '');
INSERT INTO `his_config_info` VALUES (0, 6, 'pig-upms-biz-dev.yml', 'DEFAULT_GROUP', '', 'security:\n  oauth2:\n    client:\n      client-id: ENC(imENTO7M8bLO38LFSIxnzw==)\n      client-secret: ENC(i3cDFhs26sa2Ucrfz2hnQw==)\n      scope: server\n\n# 数据源\nspring:\n  datasource:\n    type: com.zaxxer.hikari.HikariDataSource\n    driver-class-name: com.mysql.cj.jdbc.Driver\n    username: root\n    password: root\n    url: jdbc:mysql://pig-mysql:3306/pig?characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowMultiQueries=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Asia/Shanghai&nullCatalogMeansCurrent=true&allowPublicKeyRetrieval=true\n\n# 文件上传相关 支持阿里云、华为云、腾讯、minio\noss:\n  endpoint: http://minio.pig4cloud.com\n  accessKey: lengleng\n  secretKey: lengleng\n  bucket-name: tmp', '107614b40932e8237787b769e0937ed2', '2022-05-08 12:10:36', '2022-05-08 12:10:37', NULL, '127.0.0.1', 'I', '', '');
COMMIT;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
                               `role` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
                               `resource` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
                               `action` varchar(8) COLLATE utf8mb4_general_ci NOT NULL,
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
                         `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
                         `role` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
                         UNIQUE KEY `idx_user_role` (`username`,`role`) USING BTREE
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
                         `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
                         `password` varchar(500) COLLATE utf8mb4_general_ci NOT NULL,
                         `enabled` tinyint(1) NOT NULL,
                         PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of users
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
