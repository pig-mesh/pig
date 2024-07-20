package com.pig4cloud.pigx.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.*;

/**
 * 短信发送
 *
 * @author lengleng
 * @date 2024/7/18
 */
@Data
@FieldNameConstants
public class MessageEmailDTO {

    /**
     * 邮件列表
     */
    @NotNull
    private List<String> mailAddress = new ArrayList<>();

    /**
     * 标题
     */
    @NotBlank
    private String title;

    /**
     * body
     */
    private String body;

    /**
     * [HTML全文]
     */
    private String html;

    /**
     * HTML 值
     */
    private Map<String, String> htmlValues = new HashMap<>();

    /**
     * CC 列表
     */
    private List<String> ccList = new ArrayList<>();

    /**
     * 密件抄送列表
     */
    private List<String> bccList = new ArrayList<>();

    /**
     * 附件文件 (fileName)
     */
    private List<String> attachmentList = new ArrayList<>();

    /**
     * ZIP 名称
     */
    private String zipName;

    /**
     * 业务代码
     */
    private String bizCode;

}
