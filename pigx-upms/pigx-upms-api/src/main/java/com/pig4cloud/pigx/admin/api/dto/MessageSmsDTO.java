package com.pig4cloud.pigx.admin.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 短信发送
 *
 * @author lengleng
 * @date 2024/7/18
 */
@FieldNameConstants
public class MessageSmsDTO {

    /**
     * 手机号列表
     */
    @NotNull
    @Getter
    @Setter
    private List<String> mobiles;

    /**
     * 业务标识
     */
    @Getter
    @Setter
    private String bizCode;

    /**
     * 模板参数
     */
    @Getter
    @Setter
    private LinkedHashMap<String, String> params;

    /**
     * ext 参数
     */
    private String extParams;

    /**
     * 供应商
     */
    private String supplier;


    /**
     * 模板 ID
     */
    private String templateId;

    // Builder类
    public static class Builder {
        private MessageSmsDTO dto;

        public Builder() {
            dto = new MessageSmsDTO();
            dto.mobiles = new ArrayList<>();
            dto.bizCode = "DEFAULT_CODE";
            dto.params = new LinkedHashMap<>();
        }

        public Builder mobile(String mobile) {
            dto.mobiles.add(mobile);
            return this;
        }

        public Builder biz(String bizCode) {
            dto.bizCode = bizCode;
            return this;
        }

        public Builder param(String key, String value) {
            dto.params.put(key, value);
            return this;
        }

        public Builder param(String value) {
            dto.params.put("code", value);
            return this;
        }

        public MessageSmsDTO build() {
            return dto;
        }
    }

    // 静态方法用于创建Builder实例
    public static Builder builder() {
        return new Builder();
    }
}
