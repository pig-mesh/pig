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

package com.pig4cloud.pig.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenTemplateEntity;
import com.pig4cloud.pig.common.core.util.R;

/**
 * 代码生成模板服务接口
 *
 * @author lengleng
 * @date 2025/05/31
 */
public interface GenTemplateService extends IService<GenTemplateEntity> {

	/**
	 * 检查版本信息
	 * @return 返回检查结果，包含版本信息
	 */
	R checkVersion();

	/**
	 * 在线更新
	 * @return 更新结果
	 */
	R onlineUpdate();

}
