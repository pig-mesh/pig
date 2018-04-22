package com.github.pig.monitor.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2018/4/22
 */
@Data
@ConditionalOnExpression("!'${webhook}'.isEmpty()")
public class MonitorDingTalkPropertiesConfig {
    /**
     * 是否开启钉钉通知
     */
    private Boolean enabled;
}
