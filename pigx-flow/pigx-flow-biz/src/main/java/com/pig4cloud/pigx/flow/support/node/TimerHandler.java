package com.pig4cloud.pigx.flow.support.node;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

/**
 * 延时器日期处理器
 * <p>
 * 将表单字段中的日期值转换为Flowable可解析的ISO 8601格式。
 * 表单字段通常存储为 "yyyy-MM-dd HH:mm:ss" 格式，
 * 而Flowable的timeDate要求ISO 8601格式 "yyyy-MM-ddTHH:mm:ss"。
 * </p>
 * <p>
 * 使用方式：
 * 在TimerEventDefinition的timeDate中配置：${timerHandler.resolveDate(execution, 'formFieldId')}
 * </p>
 *
 * @author pigx
 */
@Component("timerHandler")
@Slf4j
public class TimerHandler {

    /**
     * 从流程变量中获取日期字段值并转换为ISO 8601格式
     *
     * @param execution 流程执行上下文
     * @param fieldId   表单字段ID
     * @return ISO 8601格式日期字符串
     */
    public String resolveDate(DelegateExecution execution, String fieldId) {
        Object value = execution.getVariable(fieldId);
        if (value == null) {
            log.warn("延时器表单字段值为空，fieldId: {}，流程实例ID: {}", fieldId, execution.getProcessInstanceId());
            return null;
        }

        String dateStr = value.toString().trim();
        if (StrUtil.isBlank(dateStr)) {
            return null;
        }

        // 已经是ISO 8601格式（包含T），直接返回
        if (dateStr.contains("T")) {
            return dateStr;
        }

        // "yyyy-MM-dd HH:mm:ss" → "yyyy-MM-ddTHH:mm:ss"
        try {
            return LocalDateTimeUtil.parse(dateStr, DatePattern.NORM_DATETIME_FORMATTER).toString();
        } catch (Exception e) {
            log.error("延时器日期格式转换失败，原始值: {}，fieldId: {}", dateStr, fieldId, e);
            return dateStr;
        }
    }

}
