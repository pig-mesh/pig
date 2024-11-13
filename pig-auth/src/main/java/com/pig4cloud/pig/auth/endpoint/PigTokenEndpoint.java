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

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.oauth2.SaOAuth2Manager;
import cn.dev33.satoken.oauth2.consts.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.dao.SaOAuth2Dao;
import cn.dev33.satoken.oauth2.data.model.AccessTokenModel;
import cn.dev33.satoken.oauth2.processor.SaOAuth2ServerProcessor;
import cn.dev33.satoken.oauth2.template.SaOAuth2Util;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.auth.support.handle.NoLoginViewHandle;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.WebUtils;
import com.pig4cloud.pig.common.security.annotation.Inner;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * pig 令牌终端节点
 *
 * @author lengleng
 * @date 2024/11/10
 */
@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class PigTokenEndpoint {

	private final StringRedisTemplate stringRedisTemplate;

	private final NoLoginViewHandle noLoginViewHandle;

	/**
	 * 密码模式、刷新模型、客户端模式获取 token
	 * @return {@link Object }
	 */
	@RequestMapping(value = "/oauth2/*")
	public ResponseEntity<Object> mixed() {
		Object result = SaOAuth2ServerProcessor.instance.dister();
		SaResult saResult = (SaResult) result;
		if (saResult.getCode().equals(HttpStatus.OK.value())) {
			return ResponseEntity.ok(saResult);
		}
		else {
			return ResponseEntity.ok(R.failed(saResult.getMsg()));
		}
	}

	/**
	 * 授权码登录：跳转至登录页面
	 * <p>
	 * 需要保存当前请求地址，登录成功后重定向
	 * @return {@link Object }
	 */
	@RequestMapping(value = "/oauth2/authorize")
	public ResponseEntity authorize(HttpSession session, HttpServletRequest request) {
		session.setAttribute("preUrl", request.getRequestURL() + "?" + request.getQueryString());
		Object result = SaOAuth2ServerProcessor.instance.dister();
		return ResponseEntity.ok(result);
	}

	/**
	 * 授权码：账号密码登录
	 * <p>
	 * 成功：重定向至第三方应用带 code 失败：登录页面
	 * @return {@link Object }
	 */
	@SneakyThrows
	@PostMapping("/oauth2/doLogin")
	public ResponseEntity<String> doLogin(HttpSession session) {
		Object result = SaOAuth2ServerProcessor.instance.dister();
		SaResult saResult = (SaResult) result;

		if (saResult.getCode().equals(HttpStatus.OK.value())) {
			Object preUrl = session.getAttribute("preUrl");

			if (Objects.nonNull(preUrl)) {
				HttpHeaders headers = new HttpHeaders();
				headers.setLocation(URI.create(preUrl.toString()));
				return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302重定向至授权码URL重新
			}
		}

		// 如果未登录，跳转到登录页面
		Map<String, Object> model = new HashMap<>();
		model.put("error", saResult.getMsg());
		return ResponseEntity.ok(noLoginViewHandle.get(model));
	}

	/**
	 * 授权码：确认页面
	 * <p>
	 * 成功：重定向至第三方应用带 code 失败：登录页面
	 * @return {@link Object }
	 */
	@SneakyThrows
	@PostMapping("/oauth2/doConfirm")
	public ResponseEntity<String> doConfirm() {
		Object result = SaOAuth2ServerProcessor.instance.dister();
		SaResult saResult = (SaResult) result;

		if (saResult.getCode().equals(HttpStatus.OK.value())) {
			String redirectURI = saResult.get(SaOAuth2Consts.Param.redirect_uri, String.class);
			if (Objects.nonNull(redirectURI)) {
				HttpHeaders headers = new HttpHeaders();
				headers.setLocation(URI.create(redirectURI));
				return new ResponseEntity<>(headers, HttpStatus.FOUND); // 302重定向至用户页面
			}
		}

		// 如果未登录，跳转到登录页面
		Map<String, Object> model = new HashMap<>();
		model.put("error", saResult.getMsg());
		return ResponseEntity.ok(noLoginViewHandle.get(model));
	}

	/**
	 * OAuth2 注销 ，回至登录页面
	 * @return {@link R }
	 */
	@RequestMapping("/oauth2/logout")
	public ResponseEntity<String> oauth2Logout() {
		StpUtil.logout();
		return ResponseEntity.ok(noLoginViewHandle.get().toString());
	}

	/**
	 * Token 简易注销， 只回收当前 token
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
	public R checkToken(@RequestParam String token) {

		AccessTokenModel accessToken = SaOAuth2Util.getAccessToken(token);

		if (Objects.isNull(accessToken)) {
			return R.failed();
		}

		return R.ok(token);
	}

	/**
	 * 令牌管理调用
	 * @param token token
	 */
	@Inner
	@DeleteMapping("/token/remove/{token}")
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

		SaTokenDao saTokenDao = SaManager.getSaTokenDao();
		SaOAuth2Dao saOAuth2Dao = SaOAuth2Manager.getDao();
		if (StrUtil.isNotBlank(username)) {
			// 获取用户的所有 token
			String accessTokenSaveKey = saOAuth2Dao.splicingAccessTokenIndexKey("*", username);
			List<String> keyList = saTokenDao.searchData(accessTokenSaveKey, StrUtil.EMPTY, current, size, false);

			List<Map<String, String>> accessTokenModels = keyList.stream()
				.map(key -> stringRedisTemplate.opsForValue().get(key))
				.map(SaOAuth2Util::getAccessToken)
				.map(accessToken -> {
					Map<String, String> result = new HashMap<>(8);
					if (Objects.nonNull(accessToken.getLoginId())) {
						result.put(SaOAuth2Consts.Param.username, accessToken.getLoginId().toString());
					}
					result.put(SecurityConstants.CLIENT_ID, accessToken.getClientId());
					result.put("accessToken", accessToken.accessToken);
					result.put("expiresAt", DateUtil.date(accessToken.expiresTime).toString());
					return result;
				})
				.toList();

			Page result = new Page(current, size);
			result.setRecords(accessTokenModels);
			result.setTotal(stringRedisTemplate.keys(accessTokenSaveKey + "*").size());
			return R.ok(result);
		}

		// 获取 token 前缀
		String accessTokenSaveKey = saOAuth2Dao.splicingAccessTokenSaveKey(StrUtil.EMPTY);
		List<String> keyList = saTokenDao.searchData(accessTokenSaveKey,
				StrUtil.isBlank(username) ? StrUtil.EMPTY : username, current, size, false);
		List<Map<String, String>> accessTokenModels = keyList.stream()
			.map(key -> StrUtil.removeAll(key, accessTokenSaveKey))
			.map(SaOAuth2Util::getAccessToken)
			.map(accessToken -> {
				Map<String, String> result = new HashMap<>(8);
				if (Objects.nonNull(accessToken.getLoginId())) {
					result.put(SaOAuth2Consts.Param.username, accessToken.getLoginId().toString());
				}
				result.put(SecurityConstants.CLIENT_ID, accessToken.getClientId());
				result.put("accessToken", accessToken.accessToken);
				result.put("expiresAt", DateUtil.date(accessToken.expiresTime).toString());
				return result;
			})
			.toList();

		Page result = new Page(current, size);
		result.setRecords(accessTokenModels);
		result.setTotal(stringRedisTemplate.keys(accessTokenSaveKey + "*").size());
		return R.ok(result);
	}

}
