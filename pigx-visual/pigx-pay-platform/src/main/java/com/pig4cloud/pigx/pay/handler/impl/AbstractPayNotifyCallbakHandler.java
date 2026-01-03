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

package com.pig4cloud.pigx.pay.handler.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.pay.entity.PayNotifyRecord;
import com.pig4cloud.pigx.pay.handler.PayNotifyCallbakHandler;
import com.pig4cloud.pigx.pay.service.PayNotifyRecordService;
import com.pig4cloud.pigx.pay.utils.PayConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @author lengleng
 * @date 2019-06-27
 */
@Slf4j
public abstract class AbstractPayNotifyCallbakHandler implements PayNotifyCallbakHandler {

	/**
	 * 调用入口
	 * @param params
	 * @return
	 */
	@Override
	public String handle(Map<String, String> params) {

		// 初始化租户
		before(params);

		// 去重处理
		if (duplicateChecker(params)) {
			return null;
		}

		// 验签处理
		if (!verifyNotify(params)) {
			return null;
		}

		String result = parse(params);
		// 保存处理结果
		saveNotifyRecord(params, result);
		return result;
	}

	/**
	 * 保存记录
	 * @param params
	 * @param result
	 * @param record
	 * @param notifyId
	 * @param recordService
	 */
	void saveRecord(Map<String, String> params, String result, PayNotifyRecord record, String notifyId,
			PayNotifyRecordService recordService) {
		record.setNotifyId(notifyId);
		String orderNo = params.get(PayConstants.OUT_TRADE_NO);
		record.setOrderNo(orderNo);
		record.setRequest(MapUtil.join(params, StrUtil.DASHED, StrUtil.DASHED));
		record.setResponse(result);
		recordService.save(record);
	}

}
