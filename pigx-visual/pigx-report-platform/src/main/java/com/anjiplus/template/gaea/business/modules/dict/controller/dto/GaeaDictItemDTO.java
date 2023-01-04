package com.anjiplus.template.gaea.business.modules.dict.controller.dto;

import com.anji.plus.gaea.curd.dto.GaeaBaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 数据字典项(GaeaDictItem)实体类
 *
 * @author lirui
 * @since 2021-03-10 13:05:59
 */
@Schema(description = "数据字典项")
@Data
public class GaeaDictItemDTO extends GaeaBaseDTO implements Serializable {

	/**
	 * 数据字典编码
	 */
	@Schema(description = "数据字典编码")
	private String dictCode;

	/**
	 * 字典项名称
	 */
	@Schema(description = "字典项名称")
	private String itemName;

	/**
	 * 字典项值
	 */
	@Schema(description = "字典项值")
	private String itemValue;

	/**
	 * 字典项扩展
	 */
	@Schema(description = "字典项扩展")
	private String itemExtend;

	/**
	 * 语言标识
	 */
	@Schema(description = "语言标识")
	private String locale;

	/**
	 * 描述
	 */
	@Schema(description = "描述")
	private String remark;

	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer sort;

	/**
	 * 1：启用，0:禁用
	 */
	private Integer enabled;

}
