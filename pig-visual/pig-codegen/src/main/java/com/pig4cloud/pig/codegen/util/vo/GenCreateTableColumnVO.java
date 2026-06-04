package com.pig4cloud.pig.codegen.util.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

/**
 * 自动创建表字段信息
 *
 * @author luolin
 * @date 2026-04-10
 */
@Data
@FieldNameConstants
@Schema(description = "自动创建表字段信息")
public class GenCreateTableColumnVO {

	@Schema(description = "字段名称")
	private String name;

	@Schema(description = "字段注释")
	private String comment;

	@Schema(description = "字段类型")
	private String type;

	@Schema(description = "字段长度")
	private Integer length;

	@Schema(description = "精度")
	private Integer precision;

	@Schema(description = "小数位")
	private Integer scale;

	@Schema(description = "默认值")
	private Object defaultValue;

	@Schema(description = "是否主键")
	private Boolean primary;

	@Schema(description = "是否允许为空")
	private Boolean nullable;

	@Schema(description = "是否自增")
	private Boolean autoIncrement;

}
