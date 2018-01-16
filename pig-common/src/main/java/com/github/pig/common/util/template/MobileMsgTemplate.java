package com.github.pig.common.util.template;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lengleng
 * @date 2018/1/15
 * 短信消息模板
 */
@Data
public class MobileMsgTemplate implements Serializable {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 文本
     */
    private String text;
    /**
     * 类型（通道）
     */
    private String type;

    public MobileMsgTemplate(String mobile, String text, String type) {
        this.mobile = mobile;
        this.text = text;
        this.type = type;
    }
}
