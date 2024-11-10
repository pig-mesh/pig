package com.pig4cloud.pig.common.excel.provider;

import com.pig4cloud.pig.common.core.util.R;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.Map;

/**
 * 字典接口, 基于 RestClient GetExchange 实现
 *
 * @author lengleng
 * @date 2024/9/7
 */
public interface RemoteDictApiService {

	/**
	 * 按类型获取 dict
	 * @param type 类型
	 * @return {@link R }<{@link List }<{@link Map }<{@link String }, {@link Object }>>>
	 */
	@GetExchange("/dict/remote/type/{type}")
	R<List<Map<String, Object>>> getDictByType(@PathVariable String type);

}
