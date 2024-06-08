package com.pig4cloud.pigx.common.sequence.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author lengleng
 * @date 2019-05-26
 * <p>
 * 发号器通用属性
 */
@Data
@ConfigurationProperties(prefix = "pigx.xsequence")
public class BaseSequenceProperties {

    /**
     * 获取range步长[可选，默认：1000]
     */
    private int step = 10;

    /**
     * 序列号分配起始值[可选：默认：0]
     */
    private long stepStart = 0;

    /**
     * 业务名称
     */
    private String bizName = "pigx";


    @NestedConfigurationProperty
    private SequenceSnowflakeProperties snowflake = new SequenceSnowflakeProperties();


    @NestedConfigurationProperty
    private SequenceRedisProperties redis = new SequenceRedisProperties();

    @NestedConfigurationProperty
    private SequenceDbProperties db = new SequenceDbProperties();
}
