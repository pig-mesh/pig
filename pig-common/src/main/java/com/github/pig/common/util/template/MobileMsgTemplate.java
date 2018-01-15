package com.github.pig.common.util.template;

import java.io.Serializable;

/**
 * @author lengleng
 * @date 2018/1/15
 * 短信消息模板
 */
public class MobileMsgTemplate implements Serializable{
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 文本
     */
    private String text;

    public MobileMsgTemplate(String mobile, String text) {
        this.mobile = mobile;
        this.text = text;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
