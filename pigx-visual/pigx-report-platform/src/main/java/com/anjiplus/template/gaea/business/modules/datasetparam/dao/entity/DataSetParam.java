
package com.anjiplus.template.gaea.business.modules.datasetparam.dao.entity;

import com.anji.plus.gaea.curd.entity.GaeaBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description 数据集动态参数 entity
 * @author Raod
 * @date 2021-03-18 12:12:33.108033200
 **/
@TableName(keepGlobalPrefix = true, value = "gaea_report_data_set_param")
@Data
public class DataSetParam extends GaeaBaseEntity {

	@Schema(description = "数据集编码")
	private String setCode;

	@Schema(description = "参数名")
	private String paramName;

	@Schema(description = "参数描述")
	private String paramDesc;

	@Schema(description = "参数类型，字典=")
	private String paramType;

	@Schema(description = "参数示例项")
	private String sampleItem;

	@Schema(description = "0--非必填 1--必填 DIC_NAME=REQUIRED_FLAG")
	private Integer requiredFlag;

	@Schema(description = "js校验字段值规则，满足校验返回 true")
	private String validationRules;

	@Schema(description = "排序")
	private Integer orderNum;

	@Schema(description = "0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG")
	private Integer enableFlag;

	@Schema(description = "0--未删除 1--已删除 DIC_NAME=DELETE_FLAG")
	private Integer deleteFlag;

}
