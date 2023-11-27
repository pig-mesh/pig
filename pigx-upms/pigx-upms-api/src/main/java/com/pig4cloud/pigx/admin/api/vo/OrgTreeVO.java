package com.pig4cloud.pigx.admin.api.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author : willian fu
 * @version : 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrgTreeVO implements Serializable {

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

	/**
	 * 头像
	 */
	private String avatar;

}
