-- ----------------------------
-- Table structure for gaea_dict
-- ----------------------------
DROP TABLE IF EXISTS `gaea_dict`;
CREATE TABLE `gaea_dict`
(
    `id`          int         NOT NULL AUTO_INCREMENT,
    `dict_name`   varchar(64) NOT NULL COMMENT '字典名称',
    `dict_code`   varchar(64) NOT NULL COMMENT '字典编码',
    `remark`      varchar(64) DEFAULT NULL COMMENT '描述',
    `create_by`   varchar(64) DEFAULT NULL COMMENT '创建人',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64) DEFAULT NULL COMMENT '更新用户',
    `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
    `version`     int         DEFAULT NULL COMMENT '版本',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数组字典';

-- ----------------------------
-- Table structure for gaea_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `gaea_dict_item`;
CREATE TABLE `gaea_dict_item`
(
    `id`          int         NOT NULL AUTO_INCREMENT,
    `dict_code`   varchar(64)   DEFAULT NULL COMMENT '数据字典编码',
    `item_name`   varchar(64) NOT NULL COMMENT '字典项名称',
    `item_value`  varchar(64) NOT NULL COMMENT '字典项值',
    `item_extend` varchar(2048) DEFAULT NULL COMMENT '字典扩展项',
    `enabled`     int           DEFAULT '1' COMMENT '1:启用 0:禁用',
    `locale`      varchar(16)   DEFAULT NULL COMMENT '语言标识',
    `remark`      varchar(64)   DEFAULT NULL COMMENT '描述',
    `sort`        int           DEFAULT NULL COMMENT '排序',
    `create_by`   varchar(64)   DEFAULT NULL COMMENT '创建人',
    `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
    `update_by`   varchar(64)   DEFAULT NULL COMMENT '更新用户',
    `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
    `version`     int           DEFAULT NULL COMMENT '版本',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=319 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据字典项';

-- ----------------------------
-- Table structure for gaea_file
-- ----------------------------
DROP TABLE IF EXISTS `gaea_file`;
CREATE TABLE `gaea_file`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `file_id`          varchar(64)   DEFAULT NULL COMMENT '生成的唯一uuid',
    `file_type`        varchar(20)   DEFAULT NULL COMMENT '文件类型，字典FILE_TYPE',
    `file_path`        varchar(1024) DEFAULT NULL COMMENT '文件在linux中的完整目录，比如/app/dist/export/excel/${fileid}.xlsx',
    `url_path`         varchar(1024) DEFAULT NULL COMMENT '通过接口的下载完整http路径',
    `file_instruction` varchar(1024) DEFAULT NULL COMMENT '文件内容说明，比如 对账单(202001~202012)',
    `create_by`        varchar(64)   DEFAULT NULL,
    `create_time`      timestamp NULL DEFAULT NULL,
    `update_by`        varchar(64)   DEFAULT NULL,
    `update_time`      timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `version`          int           DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for gaea_report
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report`;
CREATE TABLE `gaea_report`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `report_name`  varchar(100) DEFAULT NULL COMMENT '名称',
    `report_code`  varchar(100) DEFAULT NULL COMMENT '报表编码',
    `report_group` varchar(100) DEFAULT NULL COMMENT '分组',
    `report_type`  varchar(20)  DEFAULT NULL COMMENT '报表类型',
    `report_image` varchar(512) DEFAULT NULL COMMENT '报表缩略图',
    `report_desc`  varchar(255) DEFAULT NULL COMMENT '报表描述',
    `enable_flag`  int          DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
    `delete_flag`  int          DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
    `create_by`    varchar(255) DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(255) DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新时间',
    `version`      int          DEFAULT NULL COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNIQUE_REPORT_CODE` (`report_code`)
) ENGINE=InnoDB  AUTO_INCREMENT=195 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for gaea_report_dashboard
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_dashboard`;
CREATE TABLE `gaea_report_dashboard`
(
    `id`               bigint      NOT NULL AUTO_INCREMENT COMMENT '看板id',
    `report_code`      varchar(50) NOT NULL COMMENT '报表编码',
    `title`            varchar(254)  DEFAULT NULL COMMENT '看板标题',
    `width`            bigint        DEFAULT NULL COMMENT '宽度px',
    `height`           bigint        DEFAULT NULL COMMENT '高度px',
    `background_color` varchar(24)   DEFAULT NULL COMMENT '背景色',
    `background_image` varchar(254)  DEFAULT NULL COMMENT '背景图片',
    `preset_line`      varchar(4096) DEFAULT NULL COMMENT '工作台中的辅助线',
    `refresh_seconds`  int           DEFAULT NULL COMMENT '自动刷新间隔秒',
    `enable_flag`      int           DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
    `delete_flag`      int           DEFAULT '0' COMMENT ' 0--未删除 1--已删除 DIC_NAME=DEL_FLAG',
    `sort`             int           DEFAULT '0' COMMENT '排序，降序',
    `create_by`        varchar(64)   DEFAULT NULL,
    `create_time`      datetime      DEFAULT NULL,
    `update_by`        varchar(64)   DEFAULT NULL,
    `update_time`      datetime      DEFAULT NULL,
    `version`          int           DEFAULT NULL COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNIQUE_DASHBOARD_REPORT_CODE` (`report_code`)
) ENGINE=InnoDB AUTO_INCREMENT=277 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for gaea_report_dashboard_widget
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_dashboard_widget`;
CREATE TABLE `gaea_report_dashboard_widget`
(
    `id`              bigint      NOT NULL AUTO_INCREMENT COMMENT '组件id',
    `report_code`     varchar(50) NOT NULL COMMENT '报表编码',
    `type`            varchar(50)   DEFAULT NULL COMMENT '组件类型参考字典DASHBOARD_PANEL_TYPE',
    `setup`           varchar(4096) DEFAULT NULL COMMENT '组件的渲染属性json',
    `data`            varchar(4096) DEFAULT NULL COMMENT '组件的数据属性json',
    `collapse`        varchar(4096) DEFAULT NULL COMMENT '组件的配置属性json',
    `position`        varchar(4096) DEFAULT NULL COMMENT '组件的大小位置属性json',
    `options`         text COMMENT 'options配置项',
    `refresh_seconds` int           DEFAULT NULL COMMENT '自动刷新间隔秒',
    `enable_flag`     int           DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
    `delete_flag`     int           DEFAULT '0' COMMENT ' 0--未删除 1--已删除 DIC_NAME=DEL_FLAG',
    `sort`            bigint        DEFAULT '0' COMMENT '排序，图层的概念',
    `create_by`       varchar(64)   DEFAULT NULL,
    `create_time`     datetime      DEFAULT NULL,
    `update_by`       varchar(64)   DEFAULT NULL,
    `update_time`     datetime      DEFAULT NULL,
    `version`         int           DEFAULT NULL COMMENT '版本号',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8713 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for gaea_report_data_set
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_data_set`;
CREATE TABLE `gaea_report_data_set`
(
    `id`           bigint NOT NULL AUTO_INCREMENT,
    `set_code`     varchar(50)   DEFAULT NULL COMMENT '数据集编码',
    `set_name`     varchar(100)  DEFAULT NULL COMMENT '数据集名称',
    `set_desc`     varchar(255)  DEFAULT NULL COMMENT '数据集描述',
    `source_code`  varchar(50)   DEFAULT NULL COMMENT '数据源编码',
    `dyn_sentence` varchar(2048) DEFAULT NULL COMMENT '动态查询sql或者接口中的请求体',
    `case_result`  text COMMENT '结果案例',
    `enable_flag`  int           DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
    `delete_flag`  int           DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
    `create_by`    varchar(255)  DEFAULT NULL COMMENT '创建人',
    `create_time`  datetime      DEFAULT NULL COMMENT '创建时间',
    `update_by`    varchar(255)  DEFAULT NULL COMMENT '更新人',
    `update_time`  datetime      DEFAULT NULL COMMENT '更新时间',
    `version`      int           DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_set_code` (`set_code`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据集管理';

-- ----------------------------
-- Table structure for gaea_report_data_set_param
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_data_set_param`;
CREATE TABLE `gaea_report_data_set_param`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `set_code`         varchar(50)   DEFAULT NULL COMMENT '数据集编码',
    `param_name`       varchar(50)   DEFAULT NULL COMMENT '参数名',
    `param_desc`       varchar(100)  DEFAULT NULL COMMENT '参数描述',
    `param_type`       varchar(255)  DEFAULT NULL COMMENT '参数类型，字典=',
    `sample_item`      varchar(1080) DEFAULT NULL COMMENT '参数示例项',
    `required_flag`    int           DEFAULT '1' COMMENT '0--非必填 1--必填 DIC_NAME=REQUIRED_FLAG',
    `validation_rules` varchar(2048) DEFAULT NULL COMMENT 'js校验字段值规则，满足校验返回 true',
    `order_num`        int           DEFAULT NULL COMMENT '排序',
    `enable_flag`      int           DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
    `delete_flag`      int           DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
    `create_by`        varchar(255)  DEFAULT NULL COMMENT '创建人',
    `create_time`      datetime      DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(255)  DEFAULT NULL COMMENT '更新人',
    `update_time`      datetime      DEFAULT NULL COMMENT '更新时间',
    `version`          int           DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据集查询参数';


-- ----------------------------
-- Table structure for gaea_report_data_set_transform
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_data_set_transform`;
CREATE TABLE `gaea_report_data_set_transform`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `set_code`         varchar(50)    DEFAULT NULL COMMENT '数据集编码',
    `transform_type`   varchar(50)    DEFAULT NULL COMMENT '数据转换类型，DIC_NAME=TRANSFORM_TYPE; js，javaBean，字典转换',
    `transform_script` varchar(10800) DEFAULT NULL COMMENT '数据转换script,处理逻辑',
    `order_num`        int            DEFAULT NULL COMMENT '排序,执行数据转换顺序',
    `enable_flag`      int            DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
    `delete_flag`      int            DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
    `create_by`        varchar(255)   DEFAULT NULL COMMENT '创建人',
    `create_time`      datetime       DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(255)   DEFAULT NULL COMMENT '更新人',
    `update_time`      datetime       DEFAULT NULL COMMENT '更新时间',
    `version`          int            DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='数据集数据转换';

-- ----------------------------
-- Table structure for gaea_report_data_source
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_data_source`;
CREATE TABLE `gaea_report_data_source`
(
    `id`            bigint NOT NULL AUTO_INCREMENT,
    `source_code`   varchar(100)  DEFAULT NULL COMMENT '数据源编码',
    `source_name`   varchar(100)  DEFAULT NULL COMMENT '数据源名称',
    `source_desc`   varchar(255)  DEFAULT NULL COMMENT '数据源描述',
    `source_type`   varchar(50)   DEFAULT NULL COMMENT '数据源类型 DIC_NAME=SOURCE_TYPE; mysql，orace，sqlserver，elasticsearch，接口，javaBean，数据源类型字典中item-extend动态生成表单',
    `source_config` varchar(2048) DEFAULT NULL COMMENT '数据源连接配置json：关系库{ jdbcUrl:'''', username:'''', password:'''' } ES{ hostList:''ip1:9300,ip2:9300,ip3:9300'', clusterName:''elasticsearch_cluster'' }  接口{ apiUrl:''http://ip:port/url'', method:'''' } javaBean{ beanNamw:''xxx'' }',
    `enable_flag`   int           DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
    `delete_flag`   int           DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
    `create_by`     varchar(255)  DEFAULT NULL COMMENT '创建人',
    `create_time`   datetime      DEFAULT NULL COMMENT '创建时间',
    `update_by`     varchar(255)  DEFAULT NULL COMMENT '更新人',
    `update_time`   datetime      DEFAULT NULL COMMENT '更新时间',
    `version`       int           DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_source_code` (`source_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='数据源管理';

-- ----------------------------
-- Table structure for gaea_report_share
-- ----------------------------
DROP TABLE IF EXISTS `gaea_report_share`;
CREATE TABLE `gaea_report_share`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `share_code`       varchar(50)  DEFAULT NULL COMMENT '分享编码，系统生成，默认UUID',
    `share_valid_type` int          DEFAULT NULL COMMENT '分享有效期类型，DIC_NAME=SHARE_VAILD',
    `share_valid_time` datetime     DEFAULT NULL COMMENT '分享有效期',
    `share_token`      varchar(255) DEFAULT NULL COMMENT '分享token',
    `share_url`        varchar(100) DEFAULT NULL COMMENT '分享url',
    `report_code`      varchar(50)  DEFAULT NULL COMMENT '报表编码',
    `enable_flag`      int          DEFAULT '1' COMMENT '0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG',
    `delete_flag`      int          DEFAULT '0' COMMENT '0--未删除 1--已删除 DIC_NAME=DELETE_FLAG',
    `create_by`        varchar(255) DEFAULT NULL COMMENT '创建人',
    `create_time`      datetime     DEFAULT NULL COMMENT '创建时间',
    `update_by`        varchar(255) DEFAULT NULL COMMENT '更新人',
    `update_time`      datetime     DEFAULT NULL COMMENT '更新时间',
    `version`          int          DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNIQUE_SHARE_CODE` (`share_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='报表分享';

-- ----------------------------
-- Table structure for aj_report_access
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_access`;
CREATE TABLE `aj_report_access`
(
    `datetime` date NOT NULL,
    `access`   int DEFAULT NULL COMMENT '访问量',
    `register` int DEFAULT NULL COMMENT '注册量',
    PRIMARY KEY (`datetime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for aj_report_barstack
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_barstack`;
CREATE TABLE `aj_report_barstack`
(
    `id`   int NOT NULL AUTO_INCREMENT,
    `time` date         DEFAULT NULL,
    `type` varchar(255) DEFAULT NULL,
    `nums` int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for aj_report_common1
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_common1`;
CREATE TABLE `aj_report_common1`
(
    `id`   int NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    `nums` int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for aj_report_common2
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_common2`;
CREATE TABLE `aj_report_common2`
(
    `id`   int NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    `nums` int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for aj_report_common3
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_common3`;
CREATE TABLE `aj_report_common3`
(
    `id`      int NOT NULL AUTO_INCREMENT,
    `time`    varchar(255) DEFAULT NULL,
    `collect` int          DEFAULT NULL,
    `start`   int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for aj_report_comparestack
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_comparestack`;
CREATE TABLE `aj_report_comparestack`
(
    `id`   int NOT NULL AUTO_INCREMENT,
    `time` date         DEFAULT NULL,
    `type` varchar(255) DEFAULT NULL,
    `nums` bigint       DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for aj_report_devices
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_devices`;
CREATE TABLE `aj_report_devices`
(
    `device_id`        varchar(255) NOT NULL COMMENT '设备编号',
    `device_name`      varchar(255) DEFAULT NULL COMMENT '设备名称',
    `device_type`      varchar(255) DEFAULT NULL COMMENT '设备类型',
    `device_type_code` int          DEFAULT NULL COMMENT '设备类型编号',
    `online_time`      datetime     DEFAULT NULL COMMENT '上线日期',
    `device_state`     int          DEFAULT NULL COMMENT '1上线，0下线',
    PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for aj_report_exper
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_exper`;
CREATE TABLE `aj_report_exper`
(
    `datetime` date NOT NULL,
    `rt`       double DEFAULT NULL,
    `qps`      bigint DEFAULT NULL,
    `error`    int    DEFAULT NULL,
    PRIMARY KEY (`datetime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for aj_report_fireacl
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_fireacl`;
CREATE TABLE `aj_report_fireacl`
(
    `id`          int NOT NULL AUTO_INCREMENT,
    `acl_type`    varchar(255) DEFAULT NULL COMMENT 'acl类型',
    `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for aj_report_fireattack
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_fireattack`;
CREATE TABLE `aj_report_fireattack`
(
    `id`          int          NOT NULL AUTO_INCREMENT COMMENT '事件编号',
    `attack_type` varchar(255) NOT NULL COMMENT '攻击类型',
    `create_time` datetime DEFAULT NULL COMMENT '事件创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


-- ----------------------------
-- Table structure for aj_report_mail
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_mail`;
CREATE TABLE `aj_report_mail`
(
    `id`          int NOT NULL AUTO_INCREMENT COMMENT '事件id',
    `username`    varchar(255) DEFAULT NULL COMMENT '登陆用户',
    `status`      int          DEFAULT NULL COMMENT '1成功，0失败',
    `create_time` datetime     DEFAULT NULL COMMENT '事件时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=245 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for aj_report_manus
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_manus`;
CREATE TABLE `aj_report_manus`
(
    `id`       int NOT NULL AUTO_INCREMENT,
    `datetime` date         DEFAULT NULL COMMENT '日期',
    `brand`    varchar(255) DEFAULT NULL COMMENT '系列',
    `manus`    int          DEFAULT NULL COMMENT '生产量',
    `sales`    int          DEFAULT NULL COMMENT '销售量',
    `unsales`  int          DEFAULT NULL COMMENT '滞销量',
    `rework`   int          DEFAULT NULL COMMENT '返修量',
    `return`   int          DEFAULT NULL COMMENT '退货量',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for aj_report_nums
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_nums`;
CREATE TABLE `aj_report_nums`
(
    `id`    int NOT NULL AUTO_INCREMENT,
    `inter` int    DEFAULT NULL,
    `doub`  double DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for aj_report_table
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_table`;
CREATE TABLE `aj_report_table`
(
    `id`      int NOT NULL AUTO_INCREMENT,
    `date`    date         DEFAULT NULL,
    `name`    varchar(255) DEFAULT NULL,
    `code`    varchar(255) DEFAULT NULL,
    `address` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for aj_report_wifiamount
-- ----------------------------
DROP TABLE IF EXISTS `aj_report_wifiamount`;
CREATE TABLE `aj_report_wifiamount`
(
    `datetime` date NOT NULL,
    `success`  int DEFAULT NULL COMMENT '成功次数',
    `fail`     int DEFAULT NULL COMMENT '失败次数',
    PRIMARY KEY (`datetime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;