package com.pig4cloud.pig.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 行政区划可编辑字段
 *
 * @author lengleng
 * @date 2026/7/18
 */
@Data
@Schema(description = "行政区划可编辑字段")
public class SysAreaUpdateDTO {

	@NotNull(message = "行政区划ID不能为空")
	private Long id;

	@NotBlank(message = "行政区划名称不能为空")
	@Size(max = 255, message = "行政区划名称长度不能超过255个字符")
	private String name;

	@Pattern(regexp = "[01]", message = "行政区划状态不正确")
	private String areaStatus;

	@Pattern(regexp = "[01]", message = "热门状态不正确")
	private String hot;

}
