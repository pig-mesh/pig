package com.pig4cloud.pig.admin.api.vo;

import lombok.Data;

/**
 * 前端展示令牌管理
 *
 * @author lengleng
 * @date 2022/6/2
 */
@Data
public class TokenVo {

	private String id;

	private Long userId;

	private String clientId;

	private String username;

	private String accessToken;

	private String issuedAt;

	private String expiresAt;

}
