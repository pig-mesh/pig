package com.anjiplus.template.gaea.business.modules.file.controller.param;


import com.anji.plus.gaea.annotation.Query;
import com.anji.plus.gaea.constant.QueryEnum;
import com.anji.plus.gaea.curd.params.PageParam;

import java.io.Serializable;

/**
 * (GaeaFile)param
 *
 * @author peiyanni
 * @since 2021-02-18 14:48:29
 */
public class GaeaFileParam extends PageParam implements Serializable {

    /** 模糊查询 */
    @Query(value = QueryEnum.LIKE)
    private String filePath;

    /** 模糊查询 */
    @Query(value = QueryEnum.EQ)
    private String fileType;
}
