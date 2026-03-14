package com.pig4cloud.pig.common.excel.handler;

import com.pig4cloud.pig.common.excel.vo.DictEnum;

/**
 * 字典数据提供者接口
 *
 * @author lengleng
 */
public interface DictDataProvider {

	/**
	 * 根据字典类型获取字典数据
	 * @param type 字典类型
	 * @return 字典枚举数组
	 */
	DictEnum[] getDict(String type);

}
