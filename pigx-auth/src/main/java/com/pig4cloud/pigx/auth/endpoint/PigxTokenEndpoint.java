/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pigx.auth.endpoint;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.entity.SysTenant;
import com.pig4cloud.pigx.admin.api.feign.RemoteTenantService;
import com.pig4cloud.pigx.auth.service.PigxTokenDealServiceImpl;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/6/24 删除token端点
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class PigxTokenEndpoint {

	private final ClientDetailsService clientDetailsService;

	private final PigxTokenDealServiceImpl dealService;

	private final RemoteTenantService tenantService;

	/**
	 * 认证页面
	 * @param modelAndView
	 * @param error 表单登录失败处理回调的错误信息
	 * @return ModelAndView
	 */
	@GetMapping("/login")
	public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
		R<List<SysTenant>> tenantList = tenantService.list(SecurityConstants.FROM_IN);
		modelAndView.setViewName("ftl/login");
		modelAndView.addObject("error", error);
		modelAndView.addObject("tenantList", tenantList.getData());
		return modelAndView;
	}

	/**
	 * 确认授权页面
	 * @param request
	 * @param session
	 * @param modelAndView
	 * @return
	 */
	@GetMapping("/confirm_access")
	public ModelAndView confirm(HttpServletRequest request, HttpSession session, ModelAndView modelAndView) {
		Map<String, Object> scopeList = (Map<String, Object>) request.getAttribute("scopes");
		modelAndView.addObject("scopeList", scopeList.keySet());

		Object auth = session.getAttribute("authorizationRequest");
		if (auth != null) {
			AuthorizationRequest authorizationRequest = (AuthorizationRequest) auth;
			ClientDetails clientDetails = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
			modelAndView.addObject("app", clientDetails.getAdditionalInformation());
			modelAndView.addObject("user", SecurityUtils.getUser());
		}

		modelAndView.setViewName("ftl/confirm");
		return modelAndView;
	}

	/**
	 * 退出token
	 * @param authHeader Authorization
	 */
	@DeleteMapping("/logout")
	public R logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
		if (StrUtil.isBlank(authHeader)) {
			return R.ok(Boolean.FALSE, "退出失败，token 为空");
		}

		String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
		return delToken(tokenValue);
	}

	/**
	 * 令牌管理调用
	 * @param token token
	 * @return
	 */
	@Inner
	@DeleteMapping("/{token}")
	public R<Boolean> delToken(@PathVariable("token") String token) {
		return dealService.removeToken(token);
	}

	/**
	 * 查询token
	 * @param page 分页参数
	 * @param username 用户名
	 * @return
	 */
	@Inner
	@GetMapping("/page")
	public R<Page> tokenList(Page page, String username) {
		// 根据username 查询 token 列表
		if (StrUtil.isNotBlank(username)) {
			return dealService.queryTokenByUsername(page, username);
		}

		return dealService.queryToken(page);
	}

	/**
	 * 根据访问令牌获取用户信息
	 * @param accessToken 访问令牌
	 * @return 登录用户信息
	 */
	@GetMapping("/user")
	public ResponseEntity<PigxUser> userInfo(@RequestParam("access_token") String accessToken){
		return dealService.getUserInfo(accessToken);
	}

}
