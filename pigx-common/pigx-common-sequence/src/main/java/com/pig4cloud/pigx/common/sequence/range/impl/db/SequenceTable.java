package com.pig4cloud.pigx.common.sequence.range.impl.db;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

/**
 * 存储序列化的区间
 *
 * @author lengleng
 * @date 2024/6/6
 * "CREATE TABLE IF NOT EXISTS #tableName(" + "id bigint(20) NOT NULL AUTO_INCREMENT,"
 * + "value bigint(20) NOT NULL," + "name varchar(64) NOT NULL," + "gmt_create DATETIME NOT NULL,"
 * + "gmt_modified DATETIME NOT NULL," + "PRIMARY KEY (`id`),UNIQUE uk_name (`name`)" + ")";
 */
@Data
@FieldNameConstants
public class SequenceTable {

    private Long id;

    private Long value;

    private String name;

    private LocalDateTime gmtCreate;

    private LocalDateTime gmtModified;
}
