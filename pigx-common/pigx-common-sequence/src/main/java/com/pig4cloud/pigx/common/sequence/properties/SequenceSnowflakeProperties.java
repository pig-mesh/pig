package com.pig4cloud.pigx.common.sequence.properties;

import lombok.Data;

/**
 * @author lengleng
 * @date 2019-05-26
 * <p>
 * Snowflake 发号器属性
 */
@Data
public class SequenceSnowflakeProperties {

    /**
     * 数据中心ID，值的范围在[0,31]之间，一般可以设置机房的IDC[必选]
     */
    private long datacenterId;

    /**
     * 工作机器ID，值的范围在[0,31]之间，一般可以设置机器编号[必选]
     */
    private long workerId;

}
