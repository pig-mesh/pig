package com.anjiplus.template.gaea.business.modules.datasetparam.controller.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * Created by raodeming on 2021/3/24.
 */
@Data
public class DataSetParamValidationParam implements Serializable {

    /** 参数示例项 */
    @NotBlank(message = "sampleItem not empty")
    private String sampleItem;


    /** js校验字段值规则，满足校验返回 true */
    @NotBlank(message = "validationRules not empty")
    private String validationRules;
}
