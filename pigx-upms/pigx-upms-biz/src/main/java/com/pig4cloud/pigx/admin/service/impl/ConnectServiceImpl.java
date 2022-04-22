package com.pig4cloud.pigx.admin.service.impl;

import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.teaopenapi.models.Config;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pigx.admin.mapper.SysSocialDetailsMapper;
import com.pig4cloud.pigx.admin.service.ConnectService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 互联平台实现
 *
 * 钉钉: https://open-dev.dingtalk.com/apiExplorer
 *
 * @author lengleng
 * @date 2022/4/22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectServiceImpl implements ConnectService {

	private final SysSocialDetailsMapper sysSocialDetailsMapper;

	private String getDingAccessToken() {
		SysSocialDetails dingTalk = sysSocialDetailsMapper
				.selectOne(Wrappers.<SysSocialDetails>lambdaQuery().eq(SysSocialDetails::getType, "ding"));
		GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest().setAppKey(dingTalk.getAppId())
				.setAppSecret(dingTalk.getAppSecret());
		GetAccessTokenResponse tokenResponse = null;
		try {
			tokenResponse = createDingClient().getAccessToken(getAccessTokenRequest);
		}
		catch (Exception e) {
			log.error("调用钉钉异常", e);
		}
		return tokenResponse.getBody().getAccessToken();
	}

	/**
	 * 创建钉钉客户端
	 * @return
	 */
	@SneakyThrows
	private com.aliyun.dingtalkoauth2_1_0.Client createDingClient() {
		Config config = new Config();
		config.protocol = "https";
		config.regionId = "central";
		return new com.aliyun.dingtalkoauth2_1_0.Client(config);
	}

}
