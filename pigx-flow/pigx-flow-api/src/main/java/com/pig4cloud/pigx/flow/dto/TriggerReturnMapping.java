package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

/**
 * 触发器返回值映射
 *
 * @author pigx
 */
@Data
public class TriggerReturnMapping {

    private String formFieldId;

    private String formFieldName;

    private String responseKey;

}
