package com.anjiplus.template.gaea.business.enums;

public enum SetTypeEnum {
    SQL("sql", "sql"),
    HTTP("http", "http"),
    ;

    private String codeValue;
    private String codeDesc;

    private SetTypeEnum(String codeValue, String codeDesc) {
        this.codeValue = codeValue;
        this.codeDesc = codeDesc;
    }

    public String getCodeValue() {
        return this.codeValue;
    }

    public String getCodeDesc() {
        return this.codeDesc;
    }

    //根据codeValue获取枚举
    public static SetTypeEnum parseFromCodeValue(String codeValue) {
        for (SetTypeEnum e : SetTypeEnum.values()) {
            if (e.codeValue == codeValue) {
                return e;
            }
        }
        return null;
    }

    //根据codeValue获取描述
    public static String getCodeDescByCodeBalue(String codeValue) {
        SetTypeEnum enumItem = parseFromCodeValue(codeValue);
        return enumItem == null ? "" : enumItem.getCodeDesc();
    }

    //验证codeValue是否有效
    public static boolean validateCodeValue(String codeValue) {
        return parseFromCodeValue(codeValue) != null;
    }

    //列出所有值字符串
    public static String getString() {
        StringBuffer buffer = new StringBuffer();
        for (SetTypeEnum e : SetTypeEnum.values()) {
            buffer.append(e.codeValue).append("--").append(e.getCodeDesc()).append(", ");
        }
        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return buffer.toString().trim();
    }


}
