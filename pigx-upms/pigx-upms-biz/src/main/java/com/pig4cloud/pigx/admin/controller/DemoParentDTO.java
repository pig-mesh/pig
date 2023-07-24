package com.pig4cloud.pigx.admin.controller;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author lengleng
 * @date 2023/7/19
 */
@Schema(description = "审计记录表")
public class DemoParentDTO {

	private ParentDTO parentDTO1;

	@Schema(description = "审计记录表")
	private ParentDTO parentDTO2;

	@Schema(description = "审计记录表")
	public class ParentDTO {

	}

}
