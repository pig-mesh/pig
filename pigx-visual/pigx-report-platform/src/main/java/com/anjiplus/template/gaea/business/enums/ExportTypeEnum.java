package com.anjiplus.template.gaea.business.enums;

/**
 * Created by raodeming on 2021/9/3.
 */
public enum ExportTypeEnum {

    /**gaea_excel*/
    GAEA_TEMPLATE_EXCEL("gaea_template_excel", "gaea_template_excel"),
    /**gaea_pdf*/
    GAEA_TEMPLATE_PDF("gaea_template_pdf", "gaea_template_pdf"),
    ;

    private String codeValue;
    private String codeDesc;

    private ExportTypeEnum(String codeValue, String codeDesc) {
        this.codeValue = codeValue;
        this.codeDesc = codeDesc;
    }

    public String getCodeValue() {
        return this.codeValue;
    }

    public String getCodeDesc() {
        return this.codeDesc;
    }

}
