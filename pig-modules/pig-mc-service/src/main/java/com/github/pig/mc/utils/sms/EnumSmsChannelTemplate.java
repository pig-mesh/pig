package com.github.pig.mc.utils.sms;

/**
 * @author lengleng
 * @date 2018/1/16
 * 短信通道模板
 */
public enum EnumSmsChannelTemplate {
    LOGIN_NAME_LOGIN("loginCodeChannel", "登录验证");
    /**
     * 模板名称
     */
    private String name;
    /**
     * 模板签名
     */
    private String description;

    EnumSmsChannelTemplate(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
