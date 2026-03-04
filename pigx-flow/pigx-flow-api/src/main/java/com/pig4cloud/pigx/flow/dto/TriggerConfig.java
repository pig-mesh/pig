package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.util.List;

/**
 * 触发器节点配置
 *
 * @author pigx
 */
@Data
public class TriggerConfig {

    private String url;

    private List<TriggerParam> headers;

    private List<TriggerParam> body;

    private List<TriggerReturnMapping> returnMapping;

    /**
     * 异常处理方式: THROW(抛出异常) | TERMINATE(终止流程)
     */
    private String errorHandler;

}
