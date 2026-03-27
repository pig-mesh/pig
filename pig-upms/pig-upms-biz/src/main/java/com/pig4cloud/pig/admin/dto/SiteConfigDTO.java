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

package com.pig4cloud.pig.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 站点配置DTO
 *
 * @author lengleng
 * @date 2026-03-27
 */
@Data
@Schema(description = "站点配置")
public class SiteConfigDTO {

	@Schema(description = "Clarity 统计ID")
	private String clarityId;

	@Schema(description = "是否开启验证码")
	private Boolean captchaEnable;

	@Schema(description = "验证码类型")
	private String captchaType;

	@Schema(description = "是否开启图形验证码")
	private Boolean imageCaptchaEnable;

	@Schema(description = "是否强制重置密码")
	private Boolean forceResetPwd;

	@Schema(description = "是否强制下线")
	private Boolean forceLogout;

	@Schema(description = "是否开启短信登录")
	private Boolean smsLoginEnable;

	@Schema(description = "是否开启社交登录")
	private Boolean socialLoginEnable;

	@Schema(description = "是否开启注册")
	private Boolean registerEnable;

	@Schema(description = "是否开启国际化")
	private Boolean i18nEnable;

	@Schema(description = "是否开启暗黑模式")
	private Boolean darkModeEnable;

    @Schema(description = "是否开启反调试保护")
    private Boolean antiDebugEnable;

    @Schema(description = "反调试绕过密钥")
    private String antiDebugKey;

    @Schema(description = "是否开启钉钉导入")
    private Boolean syncDingtalkEnabled;

    @Schema(description = "是否开启企业微信导入")
    private Boolean syncWechatEnabled;

	@Schema(description = "站点标题")
	private String title;

	@Schema(description = "页脚信息")
	private String footer;

	@Schema(description = "隐私提示")
	private String privacyTip;

	@Schema(description = "站点描述")
	private String description;

}
