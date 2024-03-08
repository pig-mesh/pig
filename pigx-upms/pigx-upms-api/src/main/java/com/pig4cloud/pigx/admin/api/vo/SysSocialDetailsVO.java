package com.pig4cloud.pigx.admin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 系统登录账号VO
 *
 * @author lengleng
 * @date 2024/3/8
 */
@Data
public class SysSocialDetailsVO {

    /**
     * 账号
     */
    @Schema(description = "appId")
    private String appId;

    /**
     * 类型
     */
    @Schema(description = "账号类型")
    private String type;

    /**
     * 扩展字段
     */
    @Schema(description = "扩展字段")
    private String ext;
}
