package com.pig4cloud.pigx.app.api.vo;

import com.pig4cloud.pigx.app.api.entity.AppRole;
import com.pig4cloud.pigx.common.core.sensitive.Sensitive;
import com.pig4cloud.pigx.common.core.sensitive.SensitiveTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppUserVo implements Serializable {

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private Long userId;

	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String username;

	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String password;

	/**
	 * 手机号
	 */
	@ApiModelProperty(value = "手机号")
	@Sensitive(type = SensitiveTypeEnum.MOBILE_PHONE)
	private String phone;

	/**
	 * 头像
	 */
	@ApiModelProperty(value = "头像")
	private String avatar;

	/**
	 * 昵称
	 */
	@ApiModelProperty(value = "拓展字段:昵称")
	private String nickname;

	/**
	 * 姓名
	 */
	@ApiModelProperty(value = "拓展字段:姓名")
	private String name;

	/**
	 * 邮箱
	 */
	@ApiModelProperty(value = "拓展字段:邮箱")
	private String email;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 删除标记
	 */
	@ApiModelProperty(value = "删除标记,1:已删除,0:正常")
	private String delFlag;

	/**
	 * 所属租户
	 */
	@ApiModelProperty(value = "所属租户")
	private Long tenantId;

	/**
	 * 最后一次密码修改时间
	 */
	@ApiModelProperty(value = "最后一次密码修改时间")
	private LocalDateTime lastModifiedTime;

	/**
	 * 锁定标记
	 */
	@ApiModelProperty(value = "锁定标记")
	private String lockFlag;

	/**
	 * 角色列表
	 */
	@ApiModelProperty(value = "拥有的角色列表")
	private List<AppRole> roleList;

}
