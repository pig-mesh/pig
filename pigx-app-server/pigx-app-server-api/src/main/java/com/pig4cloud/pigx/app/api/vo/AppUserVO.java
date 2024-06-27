package com.pig4cloud.pigx.app.api.vo;

import com.pig4cloud.pigx.app.api.entity.AppRole;
import com.pig4cloud.pigx.common.sensitive.annotation.Sensitive;
import com.pig4cloud.pigx.common.sensitive.core.SensitiveTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppUserVO implements Serializable {

	/**
	 * 主键
	 */
	@Schema(description = "主键")
	private Long userId;

	/**
	 * 用户名
	 */
	@Schema(description = "用户名")
	private String username;

	/**
	 * 密码
	 */
	@Schema(description = "密码")
	private String password;

	/**
	 * 手机号
	 */
	@Schema(description = "手机号")
	@Sensitive(type = SensitiveTypeEnum.MOBILE_PHONE)
	private String phone;

	/**
	 * 头像
	 */
	@Schema(description = "头像")
	private String avatar;

	/**
	 * 昵称
	 */
	@Schema(description = "拓展字段:昵称")
	private String nickname;

	/**
	 * 姓名
	 */
	@Schema(description = "拓展字段:姓名")
	private String name;

	/**
	 * 邮箱
	 */
	@Schema(description = "拓展字段:邮箱")
	private String email;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 删除标记
	 */
	@Schema(description = "删除标记,1:已删除,0:正常")
	private String delFlag;

	/**
	 * 所属租户
	 */
	@Schema(description = "所属租户")
	private Long tenantId;

	/**
	 * 最后一次密码修改时间
	 */
	@Schema(description = "最后一次密码修改时间")
	private LocalDateTime lastModifiedTime;

	/**
	 * 锁定标记
	 */
	@Schema(description = "锁定标记")
	private String lockFlag;

	/**
	 * 角色列表
	 */
	@Schema(description = "拥有的角色列表")
	private List<AppRole> roleList;

}
