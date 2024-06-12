package com.pig4cloud.pigx.flow.task.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表单
 */
@Data
@NoArgsConstructor
public class FormItemVO {

    private String id;

    private String perm;

    private String icon;

    private String name;

    private String type;

    private Boolean required;

    private String typeName;

    private String placeholder;

    private Props props;

    @Data
    @NoArgsConstructor
    public static class Props {

        private Object value;

        private Object dictValue;

        private Object options;

        private Boolean self;

        private Boolean multi;

        private Object oriForm;

        private Object maxLength;

        private Object minLength;

        private Object regex;

        private Object regexDesc;

        private String prefix;

        private Long min;

        private Long max;

        private String unit;

        private Integer radixNum;

        private Integer maxSize;

        private String[] suffixArray;

    }

}
