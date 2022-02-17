package com.pig4cloud.pigx.admin.api.dto;

import com.pig4cloud.pigx.admin.api.entity.SysOauthClientDetails;
import lombok.Data;

/**
 * @author lengleng
 * @date 2020/11/18
 * <p>
 * 终端管理传输对象
 */
@Data
public class SysOauthClientDetailsDTO extends SysOauthClientDetails {

	/**
	 * 验证码开关
	 */
	private String captchaFlag;

	/**
	 * 前端密码传输是否加密
	 */
	private String encFlag;

	/**
	 * 客户端允许同时在线数量
	 */
	private String onlineQuantity;

}
