package com.pig4cloud.pig.common.excel.provider;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.plugin.excel.handler.DictDataProvider;
import com.pig4cloud.plugin.excel.vo.DictEnum;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 远程字典数据提供程序实现类
 *
 * @author lengleng
 * @date 2025/05/31
 */
@RequiredArgsConstructor
public class RemoteDictDataProvider implements DictDataProvider {

	private final RemoteDictApiService remoteDictApiService;

	/**
	 * 根据类型获取字典枚举数组
	 * @param type 字典类型
	 * @return 字典枚举数组，无数据时返回空数组
	 */
	@Override
	public DictEnum[] getDict(String type) {
		R<List<Map<String, Object>>> dictDataListR = remoteDictApiService.getDictByType(type);
		List<Map<String, Object>> dictDataList = dictDataListR.getData();
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

}
