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

package com.pig4cloud.pig.common.core.context;

/**
 * 用户上下文供给方。security 模块作为唯一实现并以 Spring Bean 形式暴露， 通用模块（data、audit 等）只依赖该接口即可。
 *
 * @author lengleng
 */
public interface UserContextHolder {

	/**
	 * 获取当前登录用户上下文。
	 * @return 未登录时返回 {@code null}
	 */
	UserContext get();

}
