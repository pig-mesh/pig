package com.pig4cloud.pigx.bi.config;

import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.admin.api.feign.RemoteTokenService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.RetOps;
import lombok.RequiredArgsConstructor;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2022/4/6
 * <p>
 * 积木安全控制
 */
@Component
@RequiredArgsConstructor
public class JimuReportTokenService implements JmReportTokenServiceI {

	private final RemoteTokenService tokenService;

	@Override
	public String getUsername(String token) {
		// 分割出 PIGX 的租户信息
		String tenant = StrUtil.subBefore(token, CharUtil.UNDERLINE, false);
		String tokenStr = StrUtil.subAfter(token, CharUtil.UNDERLINE, false);
		// @formatter:off
		return RetOps.of(tokenService.queryToken(tokenStr, tenant ))
				.getDataIf(RetOps.CODE_SUCCESS)
				.map(o -> (String)o.get("principalName"))
				.orElse(null);
		// @formatter:off
	}
	@Override public String[] getRoles(String s) {
    return new String[]{"admin"};
    }

	@Override
	public Map<String, Object> getUserInfo(String token) {
		String username = this.getUsername(token);
		Map<String, Object> map = new HashMap<>(4);
		map.put("principalName", username);
		map.put("access_token", token);
		// 将所有信息存放至map 解析sql会根据map的键值解析,可自定义其他值
		return map;
	}

	@Override
	public Boolean verifyToken(String token) {
		// 分割出 PIGX 的租户信息
		String tenant = StrUtil.subBefore(token, CharUtil.UNDERLINE, false);
		String tokenStr = StrUtil.subAfter(token, CharUtil.UNDERLINE, false);
		R<Map<String, Object>> result = tokenService.queryToken(tokenStr,tenant );
		if (CommonConstants.SUCCESS.equals(result.getCode())) {
			return true;
		}
		return false;
	}

}
