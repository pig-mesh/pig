/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysDict;
import com.pig4cloud.pig.common.core.util.R;

/**
 * 字典表服务接口 提供字典数据的增删改查及缓存同步功能
 *
 * @author lengleng
 * @date 2025/05/30
 */
public interface SysDictService extends IService<SysDict> {

	/**
	 * 根据ID列表删除字典
	 * @param ids 要删除的字典ID数组
	 * @return 操作结果
	 */
	R removeDictByIds(Long[] ids);

	/**
	 * 更新字典
	 * @param sysDict 要更新的字典对象
	 * @return 操作结果
	 */
	R updateDict(SysDict sysDict);

	/**
	 * 同步字典缓存（清空缓存）
	 * @return 操作结果
	 */
	R syncDictCache();

}
