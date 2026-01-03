/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
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

package com.pig4cloud.pigx.pay.handler;

import java.util.Map;

/**
 * @author lengleng
 * @date 2019-06-27
 * <p>
 * 支付回调处理器
 */
public interface PayNotifyCallbakHandler {

	/**
	 * 初始化执行
	 * @param params
	 */
	void before(Map<String, String> params);

	/**
	 * 去重处理
	 * @param params 回调报文
	 * @return
	 */
	Boolean duplicateChecker(Map<String, String> params);

	/**
	 * 验签逻辑
	 * @param params 回调报文
	 * @return
	 */
	Boolean verifyNotify(Map<String, String> params);

	/**
	 * 解析报文
	 * @param params
	 * @return
	 */
	String parse(Map<String, String> params);

	/**
	 * 调用入口
	 * @param params
	 * @return
	 */
	String handle(Map<String, String> params);

	/**
	 * 保存回调记录
	 * @param result 处理结果
	 * @param params 回调报文
	 */
	void saveNotifyRecord(Map<String, String> params, String result);

}
