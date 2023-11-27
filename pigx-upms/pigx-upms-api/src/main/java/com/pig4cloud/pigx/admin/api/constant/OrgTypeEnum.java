package com.pig4cloud.pigx.admin.api.constant;

/**
 * 组织类型
 *
 * @author lengleng
 * @date 2023/11/15
 */
public enum OrgTypeEnum {

	USER("user", "用户"), ROLE("role", "角色"), DEPT("dept", "部门");

	private String type;

	private String desc;

	OrgTypeEnum(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
