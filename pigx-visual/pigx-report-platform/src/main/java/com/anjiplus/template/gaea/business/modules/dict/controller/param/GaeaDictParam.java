package com.anjiplus.template.gaea.business.modules.dict.controller.param;


import com.anji.plus.gaea.annotation.Query;
import com.anji.plus.gaea.constant.QueryEnum;
import com.anji.plus.gaea.curd.params.PageParam;
import lombok.Data;

import java.io.Serializable;

/**
 * (GaeaDict)param
 *
 * @author lr
 * @since 2021-02-23 10:01:02
 */
@Data
public class GaeaDictParam extends PageParam implements Serializable {
    /**
     * 字典名称
     */
    @Query(QueryEnum.LIKE)
    private String dictName;
    /**
     * 字典编号
     */
    @Query(QueryEnum.LIKE)
    private String dictCode;
}
