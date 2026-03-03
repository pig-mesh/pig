package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

/**
 * 延时器固定时长配置
 *
 * @author pigx
 */
@Data
public class TimerDuration {

    /**
     * 时长数值
     */
    private Integer value;

    /**
     * 时长单位: MINUTE-分钟, HOUR-小时, DAY-天, WEEK-周
     */
    private String unit;

}
