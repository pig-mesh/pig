package com.pig4cloud.pig.auth.support;

/**
 * @author lengleng
 * @date 2024/7/23
 */

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.admin.api.entity.SysLog;
import com.pig4cloud.pig.admin.api.feign.RemoteLogService;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.util.WebUtils;
import com.pig4cloud.pig.common.log.util.LogTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * 用户行为监听
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomSaTokenListener implements SaTokenListener {

	private final RemoteLogService remoteLogService;

	/**
	 * 每次登录时触发
	 * @param loginType 账号类别
	 * @param loginId 账号id
	 * @param tokenValue 本次登录产生的 token 值
	 * @param loginModel 登录参数
	 */
	@Override
	public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginModel loginModel) {
		log.info("用户登录成功, loginType: {}, loginId: {}, tokenValue: {}, loginModel: {}", loginType, loginId, tokenValue,
				loginModel);

		SysLog sysLog = new SysLog();
		sysLog.setTitle("用户登录");
		sysLog.setCreateBy(loginId.toString());
		sysLog.setLogType(LogTypeEnum.NORMAL.getType());

		String clientIP = JakartaServletUtil.getClientIP(WebUtils.getRequest().get());
		sysLog.setRemoteAddr(clientIP);
		sysLog.setUserAgent(WebUtils.getRequest().get().getHeader(HttpHeaders.USER_AGENT));
		sysLog.setServiceId(SpringUtil.getProperty("spring.application.name"));
		sysLog.setMethod(HttpMethod.POST.name());
		sysLog.setParams(tokenValue);
		sysLog.setTime(System.currentTimeMillis() - SaHolder.getStorage().getLong(CommonConstants.REQUEST_START_TIME));
		remoteLogService.saveLog(sysLog);
	}

	/**
	 * 每次注销时触发
	 * @param loginType 账号类别
	 * @param loginId 账号id
	 * @param tokenValue token值
	 */
	@Override
	public void doLogout(String loginType, Object loginId, String tokenValue) {

	}

	/**
	 * 每次被踢下线时触发
	 * @param loginType 账号类别
	 * @param loginId 账号id
	 * @param tokenValue token值
	 */
	@Override
	public void doKickout(String loginType, Object loginId, String tokenValue) {

	}

	/**
	 * 每次被顶下线时触发
	 * @param loginType 账号类别
	 * @param loginId 账号id
	 * @param tokenValue token值
	 */
	@Override
	public void doReplaced(String loginType, Object loginId, String tokenValue) {

	}

	/**
	 * 每次被封禁时触发
	 * @param loginType 账号类别
	 * @param loginId 账号id
	 * @param service 指定服务
	 * @param level 封禁等级
	 * @param disableTime 封禁时长，单位: 秒
	 */
	@Override
	public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {

	}

	/**
	 * 每次被解封时触发
	 * @param loginType 账号类别
	 * @param loginId 账号id
	 * @param service 指定服务
	 */
	@Override
	public void doUntieDisable(String loginType, Object loginId, String service) {

	}

	/**
	 * 每次打开二级认证时触发
	 * @param loginType 账号类别
	 * @param tokenValue token值
	 * @param service 指定服务
	 * @param safeTime 认证时间，单位：秒
	 */
	@Override
	public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {

	}

	/**
	 * 每次关闭二级认证时触发
	 * @param loginType 账号类别
	 * @param tokenValue token值
	 * @param service 指定服务
	 */
	@Override
	public void doCloseSafe(String loginType, String tokenValue, String service) {

	}

	/**
	 * 每次创建 SaSession 时触发
	 * @param id SessionId
	 */
	@Override
	public void doCreateSession(String id) {

	}

	/**
	 * 每次注销 SaSession 时触发
	 * @param id SessionId
	 */
	@Override
	public void doLogoutSession(String id) {

	}

	/**
	 * 每次 Token 续期时触发（注意：是 timeout 续期，而不是 active-timeout 续期）
	 * @param tokenValue token 值
	 * @param loginId 账号id
	 * @param timeout 续期时间
	 */
	@Override
	public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {

	}

}
