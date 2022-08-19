package com.anjiplus.template.gaea.business.enums;

/**
 * @author zhouhang
 * @description EXCEL居中方式
 * @date 2021/4/26
 */
public enum ExcelCenterStyleEnum {
    /**
     * 左对齐
     */
    LEFT((short) 1, 1, "左对齐"),
    /**
     * 右对齐
     */
    RIGHT((short) 3, 2, "右对齐"),
    /**
     * 居中
     */
    CENTER((short) 2, 0, "居中"),
    ;

    /**
     * excel居中code
     */
    private final short excelCode;

    /**
     * 在线文档居中code
     */
    private final Integer onlineExcelCode;

    /**
     * 名称
     */
    private final String name;


    public Integer getOnlineExcelCode() {
        return onlineExcelCode;
    }

    public String getName() {
        return name;
    }

    public short getExcelCode() {
        return excelCode;
    }

    ExcelCenterStyleEnum(short excelCode, Integer onlineExcelCode, String name) {
        this.excelCode = excelCode;
        this.onlineExcelCode = onlineExcelCode;
        this.name = name;
    }

    /**
     * @param code excel居中样式code
     * @return Enum_ExcelCenterStyle
     * @description 根据excel居中样式获取在线文档居中样式
     * @author zhouhang
     * @date 2021/4/26
     */
    public static ExcelCenterStyleEnum getExcelCenterStyleByExcelCenterCode(short code) {
        for (ExcelCenterStyleEnum value : ExcelCenterStyleEnum.values()) {
            if (code == value.getExcelCode()) {
                return value;
            }
        }
        return CENTER;
    }

}
