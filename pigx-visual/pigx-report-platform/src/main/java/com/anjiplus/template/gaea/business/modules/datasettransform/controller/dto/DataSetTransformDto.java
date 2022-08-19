
package com.anjiplus.template.gaea.business.modules.datasettransform.controller.dto;

import com.anji.plus.gaea.curd.dto.GaeaBaseDTO;
import lombok.Data;

import java.io.Serializable;


/**
*
* @description 数据集数据转换 dto
* @author Raod
* @date 2021-03-18 12:13:15.591309400
**/
@Data
public class DataSetTransformDto extends GaeaBaseDTO implements Serializable {
    /** 数据集编码 */
     private String setCode;

    /** 数据转换类型，DIC_NAME=TRANSFORM_TYPE; js，javaBean，字典转换 */
     private String transformType;

    /** 数据转换script,处理逻辑 */
     private String transformScript;

    /** 排序,执行数据转换顺序 */
     private Integer orderNum;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
     private Integer enableFlag;

    /** 0--未删除 1--已删除 DIC_NAME=DELETE_FLAG */
     private Integer deleteFlag;

}
