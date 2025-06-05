package com.pig4cloud.pig.common.excel.provider;

import com.pig4cloud.pig.common.core.util.R;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.Map;

/**
 * 远程字典API服务接口，基于RestClient GetExchange实现
 *
 * @author lengleng
 * @date 2025/05/31
 */
public interface RemoteDictApiService {

	/**
	 * 根据类型获取字典数据
	 * @param type 字典类型
	 * @return 包含字典数据的响应对象，字典数据以Map列表形式返回
	 */
	@GetExchange("/dict/remote/type/{type}")
	R<List<Map<String, Object>>> getDictByType(@PathVariable String type);

}
