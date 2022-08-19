package com.anjiplus.template.gaea.business.modules.reportexcel.util;

/**
 * 单元格类型
 */
public enum CellType {
    DYNAMIC_MERGE("DYNAMIC_MERGE","动态合并单元格"),
    DYNAMIC("DYNAMIC","动态单元格"),
    STATIC("STATIC","静态单元格"),
    STATIC_AUTO("STATIC_AUTO","静态扩展单元格"),
    STATIC_MERGE("STATIC_MERGE","静态合并单元格"),
    STATIC_MERGE_AUTO("STATIC_MERGE_AUTO","静态合并扩展单元格"),
    BLACK("BLACK","空白单元格");

    private String code;
    private String name;

    CellType(String code,String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}

