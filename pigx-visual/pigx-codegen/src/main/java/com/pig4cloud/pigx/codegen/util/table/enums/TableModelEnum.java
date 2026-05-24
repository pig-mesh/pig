package com.pig4cloud.pigx.codegen.util.table.enums;
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

/**
 * 表生成模式
 *
 * @author luolin
 * @date 2022-09-18 21:56:11
 */
public enum TableModelEnum {

	/**
	 * 创建模式: 将删除所有已存在的表,重新创建,会清除所有数据
	 */
	CREATE,
	/**
	 * 更新模式: 保留已存在的表以及字段,不针对字段或其结构进行修改,不影响数据
	 */
	UPDATE,
	/**
	 * 默认模式: 不做任何操作
	 */
	NONE;

}
