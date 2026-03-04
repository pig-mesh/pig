package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

/**
 * 触发器请求参数
 *
 * @author pigx
 */
@Data
public class TriggerParam {

    private String key;

    private Boolean useForm;

    private String formFieldId;

    private String formFieldName;

    private String value;

}
