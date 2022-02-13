package com.anjiplus.template.gaea.business.modules.dict.controller.param;


import com.anji.plus.gaea.annotation.Query;
import com.anji.plus.gaea.constant.QueryEnum;
import com.anji.plus.gaea.curd.params.PageParam;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据字典项(GaeaDictItem)param
 *
 * @author lirui
 * @since 2021-03-10 13:05:59
 */
@Data
public class GaeaDictItemParam extends PageParam implements Serializable {

    /**
     * 数据字典编码
     */
    private String dictCode;
    /**
     * 字典项名称
     */
    @Query(QueryEnum.LIKE)
    private String itemName;

    /**
     * 语言标识
     */
    private String locale;

    /**
     * 1：启用，0:禁用
     */
    private Integer enabled;
}
