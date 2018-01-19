package com.github.pig.common.constant.enums;

/**
 * @author lengleng
 * @date 2018/1/16
 * 短信通道枚举
 */
public enum EnumSmsChannel {
    ALIYUN("ALIYUN_SMS", "阿里大鱼");
    /**
     * 通道名称
     */
    private String name;
    /**
     * 通道描述
     */
    private String description;

    EnumSmsChannel(String name, String description) {
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
