package com.pig4cloud.pig.auth.support;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.oauth2.SaOAuth2Manager;
import cn.dev33.satoken.oauth2.config.SaOAuth2ServerConfig;
import cn.dev33.satoken.oauth2.consts.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.dao.SaOAuth2Dao;
import cn.dev33.satoken.oauth2.data.convert.SaOAuth2DataConverter;
import cn.dev33.satoken.oauth2.data.generate.SaOAuth2DataGenerateDefaultImpl;
import cn.dev33.satoken.oauth2.data.model.AccessTokenModel;
import cn.dev33.satoken.oauth2.data.model.CodeModel;
import cn.dev33.satoken.oauth2.data.model.RefreshTokenModel;
import cn.dev33.satoken.oauth2.data.model.loader.SaClientModel;
import cn.dev33.satoken.oauth2.data.model.request.RequestAuthModel;
import cn.dev33.satoken.oauth2.error.SaOAuth2ErrorCode;
import cn.dev33.satoken.oauth2.exception.SaOAuth2AuthorizationCodeException;
import cn.dev33.satoken.oauth2.exception.SaOAuth2RefreshTokenException;
import cn.dev33.satoken.oauth2.strategy.SaOAuth2Strategy;

/**
 * Token 方法逻辑；重写避免发放token 错误
 *
 * @author weimeilayer@gmail.com ✨
 * @date 💓💕2025年6月6日🐬🐇 💓💕
 */
public class SaOAuth2TokenDataGenerateImpl extends SaOAuth2DataGenerateDefaultImpl {

	/**
	 * 构建Model：Code授权码
	 * @param ra 请求参数Model
	 * @return 授权码Model
	 */
	@Override
	public CodeModel generateCode(RequestAuthModel ra) {

		SaOAuth2Dao dao = SaOAuth2Manager.getDao();

		// 当设置不允许同时在线的时，删除旧Code
		if (!SaManager.getConfig().getIsConcurrent()) {
			dao.deleteCode(dao.getCodeValue(ra.clientId, ra.loginId));
		}

		// 生成新Code
		String codeValue = SaOAuth2Strategy.instance.createCodeValue.execute(ra.clientId, ra.loginId, ra.scopes);
		CodeModel cm = new CodeModel(codeValue, ra.clientId, ra.scopes, ra.loginId, ra.redirectUri, null);

		// 保存新Code
		dao.saveCode(cm);
		dao.saveCodeIndex(cm);

		// 返回
		return cm;
	}

	/**
	 * 构建Model：Access-Token
	 * @param code 授权码Model
	 * @return AccessToken Model
	 */
	@Override
	public AccessTokenModel generateAccessToken(String code) {

		SaOAuth2Dao dao = SaOAuth2Manager.getDao();
		SaOAuth2DataConverter dataConverter = SaOAuth2Manager.getDataConverter();
		SaOAuth2ServerConfig config = SaOAuth2Manager.getServerConfig();
		// 1、先校验
		CodeModel cm = dao.getCode(code);
		SaOAuth2AuthorizationCodeException.throwBy(cm == null, "无效 code: " + code, code, SaOAuth2ErrorCode.CODE_30110);

		// 2、当设置不允许同时在线的时，删除 旧Token
		if (!SaManager.getConfig().getIsConcurrent()) {
			// 获取当前用户的所有AccessToken
			List<String> accessTokenList = dao.getAccessTokenValueList_FromAdjustAfter(cm.clientId, cm.loginId);
			// 获取当前用户的所有RefreshToken
			List<String> refreshTokenList = dao.getRefreshTokenValueList_FromAdjustAfter(cm.clientId, cm.loginId);

			// 删除所有AccessToken
			for (String accessToken : accessTokenList) {
				dao.deleteAccessToken(accessToken);
				dao.deleteAccessTokenIndex_BySingleData(cm.clientId, cm.loginId, accessToken);
			}

			// 删除所有RefreshToken
			for (String refreshToken : refreshTokenList) {
				dao.deleteRefreshToken(refreshToken);
				dao.deleteRefreshTokenIndex_BySingleData(cm.clientId, cm.loginId, refreshToken);
			}
		}

		// 3、生成token
		AccessTokenModel at = dataConverter.convertCodeToAccessToken(cm, config.getAccessTokenTimeout());
		SaOAuth2Strategy.instance.workAccessTokenByScope.accept(at);
		RefreshTokenModel rt = dataConverter.convertAccessTokenToRefreshToken(at, config.getRefreshTokenTimeout());
		at.refreshToken = rt.refreshToken;
		at.refreshExpiresTime = rt.expiresTime;

		// 4、保存token
		dao.saveAccessToken(at);
		dao.saveAccessTokenIndex_AndAdjust(at, SaOAuth2Manager.getServerConfig().getMaxAccessTokenCount());
		dao.saveRefreshToken(rt);
		dao.saveRefreshTokenIndex_AndAdjust(rt, SaOAuth2Manager.getServerConfig().getMaxRefreshTokenCount());

		// 5、删除此Code
		dao.deleteCode(code);
		dao.deleteCodeIndex(cm.clientId, cm.loginId);

		// 6、返回 Access-Token
		return at;
	}

	/**
	 * 刷新Model：根据 Refresh-Token 生成一个新的 Access-Token
	 * @param refreshToken Refresh-Token值
	 * @return 新的 Access-Token
	 */
	@Override
	public AccessTokenModel refreshAccessToken(String refreshToken) {

		SaOAuth2Dao dao = SaOAuth2Manager.getDao();
		SaOAuth2DataConverter dataConverter = SaOAuth2Manager.getDataConverter();
		SaOAuth2ServerConfig config = SaOAuth2Manager.getServerConfig();

		// 获取 Refresh-Token 信息
		RefreshTokenModel rt = dao.getRefreshToken(refreshToken);
		SaOAuth2RefreshTokenException.throwBy(rt == null, "无效 refresh_token: " + refreshToken, refreshToken,
				SaOAuth2ErrorCode.CODE_30111);

		// 如果配置了[每次刷新产生新的Refresh-Token]
		SaClientModel clientModel = SaOAuth2Manager.getDataLoader().getClientModelNotNull(rt.clientId);
		if (clientModel.getIsNewRefresh()) {
			// 删除旧 Refresh-Token
			if (!SaManager.getConfig().getIsConcurrent()) {
				dao.deleteRefreshToken(rt.refreshToken);
				dao.deleteRefreshTokenIndex_BySingleData(rt.clientId, rt.loginId, rt.refreshToken);
			}

			// 创建并保存新的 Refresh-Token
			rt = dataConverter.convertRefreshTokenToRefreshToken(rt, config.getRefreshTokenTimeout());
			dao.saveRefreshToken(rt);
			dao.saveRefreshTokenIndex_AndAdjust(rt, config.getMaxRefreshTokenCount());
		}

		// 删除旧 Access-Token
		if (!SaManager.getConfig().getIsConcurrent()) {
			List<String> accessTokenList = dao.getAccessTokenValueList_FromAdjustAfter(rt.clientId, rt.loginId);
			for (String atValue : accessTokenList) {
				dao.deleteAccessToken(atValue);
				dao.deleteAccessTokenIndex_BySingleData(rt.clientId, rt.loginId, atValue);
			}
		}

		// 生成新 Access-Token
		AccessTokenModel at = dataConverter.convertRefreshTokenToAccessToken(rt, config.getAccessTokenTimeout());
		SaOAuth2Strategy.instance.workAccessTokenByScope.accept(at);
		at.refreshToken = rt.refreshToken;
		at.refreshExpiresTime = rt.expiresTime;

		// 保存新 Access-Token
		dao.saveAccessToken(at);
		dao.saveAccessTokenIndex_AndAdjust(at, config.getMaxAccessTokenCount());

		// 返回新 Access-Token
		return at;
	}

	/**
	 * 构建Model：Access-Token (根据RequestAuthModel构建，用于隐藏式 and 密码式)
	 * @param ra 请求参数Model
	 * @param isCreateRt 是否生成对应的Refresh-Token
	 * @return Access-Token Model
	 */
	@Override
	public AccessTokenModel generateAccessToken(RequestAuthModel ra, boolean isCreateRt,
			Consumer<AccessTokenModel> appendWork) {

		SaOAuth2Dao dao = SaOAuth2Manager.getDao();
		SaOAuth2DataConverter converter = SaOAuth2Manager.getDataConverter();
		SaOAuth2ServerConfig config = SaOAuth2Manager.getServerConfig();
		// 1、当设置不允许同时在线的时，删除 旧Token
		if (!SaManager.getConfig().getIsConcurrent()) {
			List<String> atList = dao.getAccessTokenValueList_FromAdjustAfter(ra.clientId, ra.loginId);
			for (String atVal : atList) {
				dao.deleteAccessToken(atVal);
				dao.deleteAccessTokenIndex_BySingleData(ra.clientId, ra.loginId, atVal);
			}

			if (isCreateRt) {
				List<String> rtList = dao.getRefreshTokenValueList_FromAdjustAfter(ra.clientId, ra.loginId);
				for (String rtVal : rtList) {
					dao.deleteRefreshToken(rtVal);
					dao.deleteRefreshTokenIndex_BySingleData(ra.clientId, ra.loginId, rtVal);
				}
			}
		}

		// 2、生成 新Access-Token
		String newAtValue = SaOAuth2Strategy.instance.createAccessToken.execute(ra.clientId, ra.loginId, ra.scopes);
		AccessTokenModel at = new AccessTokenModel(newAtValue, ra.clientId, ra.loginId, ra.scopes);
		at.tokenType = SaOAuth2Consts.TokenType.bearer;

		// 3、根据权限构建额外参数
		at.extraData = new LinkedHashMap<>();
		SaOAuth2Strategy.instance.workAccessTokenByScope.accept(at);

		SaClientModel clientModel = SaOAuth2Manager.getDataLoader().getClientModelNotNull(ra.clientId);
		at.expiresTime = System.currentTimeMillis() + (clientModel.getAccessTokenTimeout() * 1000);

		// 3、生成&保存 Refresh-Token
		if (isCreateRt) {
			RefreshTokenModel rt = converter.convertAccessTokenToRefreshToken(at, clientModel.getRefreshTokenTimeout());
			at.refreshToken = rt.refreshToken;
			at.refreshExpiresTime = rt.expiresTime;

			dao.saveRefreshToken(rt);
			dao.saveRefreshTokenIndex_AndAdjust(rt, config.getMaxRefreshTokenCount());
		}

		// 5、保存 新Access-Token
		dao.saveAccessToken(at);
		dao.saveAccessTokenIndex_AndAdjust(at, config.getMaxAccessTokenCount());

		// 6、返回 新Access-Token
		return at;
	}

}