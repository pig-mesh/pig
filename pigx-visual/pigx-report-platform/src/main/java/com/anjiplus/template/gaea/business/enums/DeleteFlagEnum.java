package com.anjiplus.template.gaea.business.enums;
public enum DeleteFlagEnum {
    DELETED(1,"已删除"),
    UNDELETED(0,"未删除"),
    ;

    private int codeValue;
    private String codeDesc;

    private DeleteFlagEnum(int  codeValue, String codeDesc) {
        this.codeValue = codeValue;
        this.codeDesc = codeDesc;
    }

    public int   getCodeValue(){ return this.codeValue;}

    public String getCodeDesc(){ return this.codeDesc;}

    //根据codeValue获取枚举
    public static DeleteFlagEnum parseFromCodeValue(int codeValue){
        for (DeleteFlagEnum e : DeleteFlagEnum.values()){
            if(e.codeValue == codeValue){ return e;}
        }
        return null;
    }

    //根据codeValue获取描述
    public static String getCodeDescByCodeValue(int codeValue){
        DeleteFlagEnum enumItem = parseFromCodeValue(codeValue);
        return enumItem == null ? "" : enumItem.getCodeDesc();
    }

    //验证codeValue是否有效
    public static boolean validateCodeValue(int codeValue){ return parseFromCodeValue(codeValue)!=null;}

    //列出所有值字符串
    public static String getString(){
        StringBuffer buffer = new StringBuffer();
        for (DeleteFlagEnum e : DeleteFlagEnum.values()){
            buffer.append(e.codeValue).append("--").append(e.getCodeDesc()).append(", ");
        }
        buffer.deleteCharAt(buffer.lastIndexOf(","));
        return buffer.toString().trim();
    }


}
