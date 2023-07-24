package com.pig4cloud.pigx.flow.task.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgTreeVo {

	/**
	 * 用户od
	 */
	private Long id;

	/**
	 * 用户名称
	 */
	private String name;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 选择
	 */
	private Boolean selected;

	private String avatar;

	private Integer status = 1;

}
