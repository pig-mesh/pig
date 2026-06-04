/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.pig4cloud.pig.admin.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 菜单排序DTO
 *
 * @author lengleng
 * @date 2026/5/21
 */
@Data
@Schema(description = "菜单排序")
public class SysMenuSortDTO {

	/**
	 * 父菜单ID
	 */
	@NotNull(message = "菜单父ID不能为空")
	@Schema(description = "父菜单ID")
	private Long parentId;

	/**
	 * 同级菜单ID列表，按目标顺序排列
	 */
	@NotEmpty(message = "菜单ID列表不能为空")
	@Schema(description = "菜单ID列表")
	private List<Long> menuIds;

}
