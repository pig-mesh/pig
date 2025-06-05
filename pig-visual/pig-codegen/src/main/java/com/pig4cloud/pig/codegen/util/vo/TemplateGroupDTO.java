package com.pig4cloud.pig.codegen.util.vo;

import java.io.Serial;
import java.util.List;

import com.pig4cloud.pig.codegen.entity.GenGroupEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author weimeilayer@gmail.com ✨
 * @date 💓💕 2025年5月30日 🐬🐇 💓💕
 */
@Data
@Schema(description = "模板传输对象")
@EqualsAndHashCode(callSuper = true)
public class TemplateGroupDTO extends GenGroupEntity {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 模板id集合
	 */
	@Schema(description = "模板id集合")
	private List<Long> templateId;

}
