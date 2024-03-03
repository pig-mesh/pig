package com.pig4cloud.pigx.codegen.service;

/*
 *      Copyright (c) 2018-2025, luolin All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: luolin (766488893@qq.com)
 */

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.codegen.entity.GenCreateTable;
import com.pig4cloud.pigx.codegen.util.vo.GenCreateTableVO;

/**
 * 自动创建表管理
 *
 * @author luolin
 * @date 2022-09-23 21:56:11
 */
public interface GenCreateTableService extends IService<GenCreateTable> {

	/**
	 * 自动创建表
	 * @param createTableVO 表信息
	 */
	Boolean createTable(GenCreateTableVO createTableVO);

}
