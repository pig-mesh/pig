/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.auth.endpoint;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.oauth2.SaOAuth2Manager;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.oauth2.error.SaOAuth2ErrorCode;
import cn.dev33.satoken.oauth2.exception.SaOAuth2Exception;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Handle;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.oauth2.model.AccessTokenModel;
import cn.dev33.satoken.oauth2.model.ClientTokenModel;
import cn.dev33.satoken.oauth2.model.CodeModel;
import cn.dev33.satoken.oauth2.model.RequestAuthModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.auth.support.MobileGrantTypeHandle;
import com.pig4cloud.pig.auth.support.PasswordLoginHandle;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.WebUtils;
import com.pig4cloud.pig.common.security.annotation.Inner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

import static com.pig4cloud.pig.common.core.constant.CommonConstants.REQUEST_START_TIME;

/**
 * @author lengleng
 * @date 2019/2/1 删除token端点
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PigTokenEndpoint {

	private final StringRedisTemplate stringRedisTemplate;

	private final SaOAuth2Template saOAuth2Template;

	private final PasswordLoginHandle passwordLoginHandle;

	private final SaTokenDao saTokenDao;

	/**
	 * 密码登录端点
	 * @return {@link Object }
	 */
	@RequestMapping(value = "/oauth2/token", params = "grant_type=password")
	public ResponseEntity passwordToken() {
		SaRequest req = SaHolder.getRequest();
		SaResponse res = SaHolder.getResponse();
		SaOAuth2Config cfg = SaOAuth2Manager.getConfig();

		SaHolder.getStorage().set(REQUEST_START_TIME, System.currentTimeMillis());

		Object result = SaOAuth2Handle.password(req, res, cfg);
		SaResult saResult = (SaResult) result;
		if (saResult.getCode().equals(HttpStatus.OK.value())) {
			return ResponseEntity.ok(saResult.getData());
		}
		else {
			return ResponseEntity.ok(R.failed(saResult.getMsg()));
		}
	}

	/**
	 * 手机号登录端点
	 * @return {@link Object }
	 */
	@RequestMapping(value = "/oauth2/token", params = "grant_type=mobile")
	public ResponseEntity mobileToken() {
		SaRequest req = SaHolder.getRequest();
		SaResponse res = SaHolder.getResponse();
		SaOAuth2Config cfg = SaOAuth2Manager.getConfig();
		SaHolder.getStorage().set(REQUEST_START_TIME, System.currentTimeMillis());
		Object result = MobileGrantTypeHandle.mobile(req, res, cfg);
		SaResult saResult = (SaResult) result;
		if (saResult.getCode().equals(HttpStatus.OK.value())) {
			return ResponseEntity.ok(saResult.getData());
		}
		else {
			return ResponseEntity.ok(R.failed(saResult.getMsg()));
		}
	}

	/**
	 * 刷新令牌端点
	 * @return {@link Object }
	 */
	@RequestMapping(value = "/oauth2/token", params = "grant_type=refresh_token")
	public ResponseEntity refreshToken() {
		SaRequest req = SaHolder.getRequest();
		// 获取参数
		String clientId = req.getParamNotNull(SaOAuth2Consts.Param.client_id);
		String clientSecret = req.getParamNotNull(SaOAuth2Consts.Param.client_secret);
		String refreshToken = req.getParamNotNull(SaOAuth2Consts.Param.refresh_token);

		// 校验参数
		SaOAuth2Util.checkRefreshTokenParam(clientId, clientSecret, refreshToken);

		// 获取新Token返回
		Object data = SaOAuth2Util.refreshAccessToken(refreshToken).toLineMap();

		return ResponseEntity.ok(data);
	}

	/**
	 * 客户端令牌端点
	 * @return {@link Object }
	 */
	@RequestMapping(value = "/oauth2/token", params = "grant_type=client_credentials")
	public ResponseEntity clientToken() {
		SaRequest req = SaHolder.getRequest();
		// 获取参数
		String clientId = req.getParamNotNull(SaOAuth2Consts.Param.client_id);
		String clientSecret = req.getParamNotNull(SaOAuth2Consts.Param.client_secret);
		String scope = req.getParam(SaOAuth2Consts.Param.scope);

		// 校验 ClientScope
		SaOAuth2Util.checkContract(clientId, scope);

		// 校验 ClientSecret
		SaOAuth2Util.checkClientSecret(clientId, clientSecret);

		// 返回 Client-Token
		ClientTokenModel ct = SaOAuth2Util.generateClientToken(clientId, scope);
		return ResponseEntity.ok(ct.toLineMap());
	}

	/**
	 * 授权码模式端点
	 */
	@RequestMapping("/oauth2/authorize")
	public ModelAndView authorize(ModelAndView modelAndView) {

		// 获取变量
		SaRequest req = SaHolder.getRequest();

		// 1、如果尚未登录, 则先去登录
		if (!StpUtil.isLogin()) {
			modelAndView.addObject("req", req);
			modelAndView.setViewName("/ftl/login");
			return modelAndView;
		}

		Object requestAuthModel = StpUtil.getSession().get("RequestAuthModel");
		RequestAuthModel ra = (RequestAuthModel) requestAuthModel;

		// 3、校验：重定向域名是否合法
		SaOAuth2Util.checkRightUrl(ra.clientId, ra.redirectUri);

		// 4、校验：此次申请的Scope，该Client是否已经签约
		SaOAuth2Util.checkContract(ra.clientId, ra.scope);

		// 5、判断：如果此次申请的Scope，该用户尚未授权，则转到授权页面
		boolean isGrant = SaOAuth2Util.isGrant(ra.loginId, ra.clientId, ra.scope);
		if (!isGrant) {
			modelAndView.addObject("requestAuthModel", ra);
			modelAndView.setViewName("/ftl/confirm");
			return modelAndView;
		}

		// 6、判断授权类型
		// 如果是 授权码式，则：开始重定向授权，下放code
		if (SaOAuth2Consts.ResponseType.code.equals(ra.responseType)) {
			CodeModel codeModel = SaOAuth2Util.generateCode(ra);
			String redirectUri = SaOAuth2Util.buildRedirectUri(ra.redirectUri, codeModel.code, ra.state);
			modelAndView.setViewName("redirect:" + redirectUri);
			return modelAndView;
		}

		// 如果是 隐藏式，则：开始重定向授权，下放 token
		if (SaOAuth2Consts.ResponseType.token.equals(ra.responseType)) {
			AccessTokenModel at = SaOAuth2Util.generateAccessToken(ra, false);
			String redirectUri = SaOAuth2Util.buildImplicitRedirectUri(ra.redirectUri, at.accessToken, ra.state);
			modelAndView.setViewName("redirect:" + redirectUri);
			return modelAndView;
		}

		// 默认返回
		throw new SaOAuth2Exception("无效response_type: " + ra.responseType).setCode(SaOAuth2ErrorCode.CODE_30125);
	}

	/**
	 * 授权码确认页
	 * @param modelAndView 模型和视图
	 * @return {@link ModelAndView }
	 */
	@RequestMapping("/oauth2/doConfirm")
	public ModelAndView doConfirm(ModelAndView modelAndView) {
		SaRequest req = SaHolder.getRequest();

		String clientId = req.getParamNotNull(SaOAuth2Consts.Param.client_id);
		String scope = req.getParamNotNull(SaOAuth2Consts.Param.scope);
		Object loginId = StpUtil.getLoginId();
		SaOAuth2Util.saveGrantScope(clientId, loginId, scope);
		modelAndView.setViewName("redirect:/oauth2/authorize");
		return modelAndView;
	}

	/**
	 * OAuth2 注销
	 * @return {@link R }
	 */
	@RequestMapping("/oauth2/logout")
	public R oauth2Logout() {
		StpUtil.logout();
		return R.ok();
	}

	/**
	 * 授权码模式登录
	 * @param modelAndView 模型和视图
	 * @param username 用户名
	 * @param password 密码
	 * @return {@link ModelAndView }
	 */
	@RequestMapping("/token/form")
	public ModelAndView doLogin(ModelAndView modelAndView, @RequestParam String username,
			@RequestParam String password) {

		SaResult saResult = (SaResult) passwordLoginHandle.apply(username, password);
		// form 登录失败
		if (saResult.getCode() != HttpStatus.OK.value()) {
			modelAndView.addObject("error", saResult.getMsg());
			modelAndView.setViewName("/ftl/login");
			return modelAndView;
		}

		// 全局 ra
		SaRequest req = SaHolder.getRequest();
		RequestAuthModel ra = SaOAuth2Util.generateRequestAuth(req, StpUtil.getLoginId());
		StpUtil.getSession().set("RequestAuthModel", ra);

		// 继续授权
		modelAndView.setViewName("redirect:/oauth2/authorize");
		return modelAndView;
	}

	/**
	 * 注销
	 * @return {@link R }
	 */
	@RequestMapping("/token/logout")
	public R logout() {
		String token = WebUtils.getToken();

		if (StrUtil.isBlank(token)) {
			return R.failed("token 不能为空");
		}
		SaOAuth2Util.revokeAccessToken(token);
		return R.ok();
	}

	/**
	 * 检查令牌
	 * @param token 令 牌
	 * @return {@link ResponseEntity }
	 */
	@RequestMapping("/token/check_token")
	public ResponseEntity checkToken(@RequestParam String token) {

		AccessTokenModel accessToken = SaOAuth2Util.getAccessToken(token);

		if (accessToken == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		return ResponseEntity.ok(token);
	}

	/**
	 * 令牌管理调用
	 * @param token token
	 */
	@Inner
	@DeleteMapping("/remove/{token}")
	public R<Boolean> removeToken(@PathVariable("token") String token) {
		SaOAuth2Util.revokeAccessToken(token);
		return R.ok();
	}

	/**
	 * 查询token
	 * @param params 分页参数
	 * @return
	 */
	@Inner
	@PostMapping("/token/page")
	public R<Page> tokenList(@RequestBody Map<String, Object> params) {
		int current = MapUtil.getInt(params, CommonConstants.CURRENT) - 1;
		int size = MapUtil.getInt(params, CommonConstants.SIZE);
		String username = MapUtil.getStr(params, SaOAuth2Consts.Param.username);

		String accessTokenSaveKey;
		if (StrUtil.isNotBlank(username)) {
			accessTokenSaveKey = saOAuth2Template.splicingAccessTokenIndexKey("*", username);
		}
		else {
			accessTokenSaveKey = saOAuth2Template.splicingAccessTokenSaveKey(StrUtil.EMPTY);
		}

		List<String> keyList = saTokenDao.searchData(accessTokenSaveKey, StrUtil.EMPTY, current, size, false);
		List<AccessTokenModel> accessTokenModels = keyList.stream()
			.map(key -> StrUtil.removeAll(key, accessTokenSaveKey))
			.map(SaOAuth2Util::getAccessToken)
			.toList();

		Page result = new Page(current, size);
		result.setRecords(accessTokenModels);
		result.setTotal(stringRedisTemplate.keys(accessTokenSaveKey + "*").size());
		return R.ok(result);
	}

}
