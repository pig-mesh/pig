package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

/**
 * 延时器配置
 *
 * @author pigx
 */
@Data
public class TimerConfig {

    /**
     * 延时类型: DURATION-固定时长, DATETIME-指定日期时间, FORM_FIELD-表单字段日期
     */
    private String timerType;

    /**
     * 固定时长配置 当timerType=DURATION时使用
     */
    private TimerDuration duration;

    /**
     * 指定日期时间 当timerType=DATETIME时使用，ISO 8601格式
     */
    private String dateTime;

    /**
     * 表单字段ID 当timerType=FORM_FIELD时使用
     */
    private String formFieldId;

    /**
     * 表单字段名称 用于显示
     */
    private String formFieldName;

}
