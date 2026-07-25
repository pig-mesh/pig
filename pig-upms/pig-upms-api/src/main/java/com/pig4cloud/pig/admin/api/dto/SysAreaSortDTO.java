package com.pig4cloud.pig.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 行政区划排序 DTO
 *
 * @author lengleng
 * @date 2026/7/18
 */
@Data
@Schema(description = "行政区划排序")
public class SysAreaSortDTO {

	/**
	 * 父级行政编码
	 */
	@NotNull(message = "父级行政编码不能为空")
	@Schema(description = "父级行政编码")
	private Long pid;

	/**
	 * 同级行政区划数据库主键，按目标顺序排列
	 */
	@NotEmpty(message = "行政区划ID列表不能为空")
	@Schema(description = "行政区划ID列表")
	private List<Long> areaIds;

}
