package com.pig4cloud.pig.common.excel.provider;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.plugin.excel.handler.DictDataProvider;
import com.pig4cloud.plugin.excel.vo.DictEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 远程 dict 数据提供程序
 *
 * @author lengleng
 * @date 2024/09/01
 */
@RequiredArgsConstructor
public class RemoteDictDataProvider implements DictDataProvider {

	private final RestTemplate restTemplate;

	/**
	 * 获取 dict
	 * @param type 类型
	 * @return {@link DictEnum[] }
	 */
	@Override
	public DictEnum[] getDict(String type) {
		// 获取服务URL
		String serviceUrl = getServiceUrl(type);
		// 创建请求实体
		HttpHeaders headers = new HttpHeaders();
		headers.add(SecurityConstants.FROM, SecurityConstants.FROM_IN);
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		// 发送HTTP请求并获取响应
		ResponseEntity<Map> response = restTemplate.exchange(serviceUrl, HttpMethod.GET, requestEntity, Map.class,
				type);

		// 解析响应数据
		List<Map<String, Object>> dictDataList = MapUtil.get(response.getBody(), R.Fields.data, ArrayList.class);
		if (CollUtil.isEmpty(dictDataList)) {
			return new DictEnum[0];
		}

		// 构建 DictEnum 数组
		DictEnum.Builder dictEnumBuilder = DictEnum.builder();
		for (Map<String, Object> dictData : dictDataList) {
			String value = MapUtil.getStr(dictData, "value");
			String label = MapUtil.getStr(dictData, "label");
			dictEnumBuilder.add(value, label);
		}

		return dictEnumBuilder.build();
	}

	/**
	 * 获取服务 URL
	 * @param param 参数
	 * @return {@link String }
	 */
	private String getServiceUrl(String param) {
		// 根据当前架构模式，组装URL
		if (SpringContextHolder.isMicro()) {
			return String.format("http://%s/dict/remote/type/%s", ServiceNameConstants.UPMS_SERVICE, param);
		}
		else {
			return String.format("http://%s/dict/remote/type/%s", SpringContextHolder.getEnvironment()
				.resolvePlaceholders("127.0.0.1:${server.port}${server.servlet.context-path}"), param);
		}
	}

}
