package com.pig4cloud.pigx.flow.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义业务流基础数据传输对象
 *
 * @author lengleng
 * @date 2025/10/16
 */
@Data
public class BizFlowBaseDto {

    /**
     * 流程实例的编号
     */
    @Schema(description = "流程实例的编号")
    private String processInstanceId;

    /**
     * 流程参数 (用于存储用户选择的审批人等信息)
     */
    @TableField(exist = false)
    @Schema(description = "流程参数（审批人信息）")
    private Map<String, Object> flowParamMap = new HashMap<>();
}
