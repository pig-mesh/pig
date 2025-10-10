package com.pig4cloud.pigx.flow.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

/**
 * 表单项视图对象
 * <p>
 * 用于定义流程表单中的各种表单项配置，包括输入框、选择框、日期选择器等各种表单控件。
 * 支持动态表单的构建和渲染，提供丰富的表单项属性配置。
 * </p>
 */
@Data
@NoArgsConstructor
@FieldNameConstants
public class FormItemVO {

    /**
     * 表单项ID
     * <p>
     * 表单项的唯一标识符，用于表单数据的提交和绑定
     * </p>
     */
    private String id;

    /**
     * 权限标识
     * <p>
     * 表单项的权限控制标识，如"write"（可写）、"read"（只读）、"hidden"（隐藏）
     * </p>
     */
    private String perm;

    /**
     * 表单项图标
     * <p>
     * 表单项的图标类名，用于在界面上展示对应的图标
     * </p>
     */
    private String icon;

    /**
     * 表单项名称
     * <p>
     * 表单项的显示名称，如"姓名"、"部门"、"金额"等
     * </p>
     */
    private String name;

    /**
     * 表单项类型
     * <p>
     * 表单项的控件类型，如"input"（输入框）、"select"（选择框）、"date"（日期选择）等
     * </p>
     */
    private String type;

    /**
     * 是否必填
     * <p>
     * 标识该表单项是否为必填项，true表示必填，false表示选填
     * </p>
     */
    private Boolean required;

    /**
     * 类型名称
     * <p>
     * 表单项类型的中文名称，用于前端展示
     * </p>
     */
    private String typeName;

    /**
     * 占位提示文本
     * <p>
     * 表单项的输入提示信息，显示在输入框内
     * </p>
     */
    private String placeholder;

    /**
     * 表单项属性配置
     * <p>
     * 包含表单项的各种扩展属性配置
     * </p>
     */
    private Props props = new Props();

    /**
     * 表单项属性配置对象
     * <p>
     * 封装了表单项的各种属性配置，支持不同类型表单项的特定属性
     * </p>
     */
    @Data
    @NoArgsConstructor
    @FieldNameConstants
    public static class Props {

        /**
         * 表单项默认值
         * <p>
         * 表单项的默认值，可以是字符串、数字、数组等类型
         * </p>
         */
        private Object value;

        /**
         * 字典值
         * <p>
         * 当表单项类型为选择框时，指定使用的数据字典
         * </p>
         */
        private Object dictValue;

        /**
         * 选项列表
         * <p>
         * 选择类表单项的选项数据，通常为数组格式
         * </p>
         */
        private Object options;

        /**
         * 是否自定义
         * <p>
         * 标识是否允许用户自定义输入，通常用于选择框支持自定义选项
         * </p>
         */
        private Boolean self;

        /**
         * 是否多选
         * <p>
         * 标识选择类表单项是否支持多选
         * </p>
         */
        private Boolean multi;

        /**
         * 原始表单配置
         * <p>
         * 保存原始的表单配置信息，用于表单的动态生成
         * </p>
         */
        private Object oriForm;

        /**
         * 最大长度
         * <p>
         * 输入类表单项的最大输入长度限制
         * </p>
         */
        private Object maxLength;

        /**
         * 最小长度
         * <p>
         * 输入类表单项的最小输入长度限制
         * </p>
         */
        private Object minLength;

        /**
         * 正则表达式
         * <p>
         * 用于输入验证的正则表达式
         * </p>
         */
        private Object regex;

        /**
         * 正则验证说明
         * <p>
         * 正则表达式验证失败时的提示信息
         * </p>
         */
        private Object regexDesc;

        /**
         * 前缀文本
         * <p>
         * 显示在输入框前的文本，如货币符号"¥"
         * </p>
         */
        private String prefix;

        /**
         * 最小值
         * <p>
         * 数字类表单项的最小值限制
         * </p>
         */
        private Long min;

        /**
         * 最大值
         * <p>
         * 数字类表单项的最大值限制
         * </p>
         */
        private Long max;

        /**
         * 单位
         * <p>
         * 数字类表单项的单位，如"元"、"天"等
         * </p>
         */
        private String unit;

        /**
         * 小数位数
         * <p>
         * 数字类表单项允许的小数位数
         * </p>
         */
        private Integer radixNum;

        /**
         * 最大文件大小
         * <p>
         * 文件上传类表单项的文件大小限制，单位为MB
         * </p>
         */
        private Integer maxSize;

        /**
         * 允许的文件后缀
         * <p>
         * 文件上传类表单项允许的文件类型后缀数组，如["jpg", "png", "pdf"]
         * </p>
         */
        private String[] suffixArray;

    }

}
