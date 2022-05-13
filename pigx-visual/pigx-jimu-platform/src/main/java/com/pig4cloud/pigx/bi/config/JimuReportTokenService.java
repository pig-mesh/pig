package com.pig4cloud.pigx.bi.config;

import com.pig4cloud.pigx.admin.api.feign.RemoteTokenService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.core.util.RetOps;
import lombok.RequiredArgsConstructor;
import org.jeecg.modules.jmreport.api.JmReportTokenServiceI;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lengleng
 * @date 2022/4/6
 *
 * 积木安全控制
 */
@Component
@RequiredArgsConstructor
public class JimuReportTokenService implements JmReportTokenServiceI {

	private final RemoteTokenService tokenService;

	@Override
	public String getUsername(String token) {
		// @formatter:off
		return RetOps.of(tokenService.queryToken(token, SecurityConstants.FROM_IN))
				.getDataIf(RetOps.CODE_SUCCESS)
				.map(o -> (String)o.get("username"))
				.orElse(null);
		// @formatter:off
	}

	@Override
	public Boolean verifyToken(String token) {
		R<Map<String, Object>> result = tokenService.queryToken(token, SecurityConstants.FROM_IN);
		if (CommonConstants.SUCCESS.equals(result.getCode())) {
			return true;
		}
		return false;
	}

}
