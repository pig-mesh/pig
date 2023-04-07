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
package com.pig4cloud.pigx.mp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.mp.config.WxMpInitConfigRunner;
import com.pig4cloud.pigx.mp.entity.WxAccount;
import com.pig4cloud.pigx.mp.mapper.WxAccountMapper;
import com.pig4cloud.pigx.mp.service.WxAccountService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpDataCubeService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeArticleResult;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeInterfaceResult;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeMsgResult;
import me.chanjar.weixin.mp.bean.datacube.WxDataCubeUserCumulate;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 公众号账户
 *
 * @author lengleng
 * @date 2019-03-26 22:07:53
 */
@Slf4j
@Service
public class WxAccountServiceImpl extends ServiceImpl<WxAccountMapper, WxAccount> implements WxAccountService {

	/**
	 * 生成公众号二维码
	 * @param appId
	 * @return
	 */

	@Override
	public R generateQr(String appId) {
		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
		try {
			WxMpQrCodeTicket ticket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(1);
			String url = wxMpService.getQrcodeService().qrCodePictureUrl(ticket.getTicket());

			WxAccount wxAccount = baseMapper
					.selectOne(Wrappers.<WxAccount>query().lambda().eq(WxAccount::getAppid, appId));
			wxAccount.setQrUrl(url);
			baseMapper.updateById(wxAccount);
		}
		catch (WxErrorException e) {
			log.error(" 获取公众号二维码失败", e);
			return R.failed("更新公众号二维码失败");
		}

		return R.ok();
	}

	/**
	 * 获取公众号统计数据
	 * @param appId 公众号信息
	 * @param interval 时间间隔
	 * @return
	 */
	@Override
	public R statistics(String appId, String interval) {
		String[] split = interval.split(StrUtil.DASHED);
		Date start = new Date(Long.parseLong(split[0]));
		Date end = new Date(Long.parseLong(split[1]));

		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
		WxMpDataCubeService cubeService = wxMpService.getDataCubeService();

		List<List<Object>> result = new ArrayList<>();
		try {
			// 获取累计用户数据
			List<Object> cumulateList = cubeService.getUserCumulate(start, end).stream()
					.map(WxDataCubeUserCumulate::getCumulateUser).collect(Collectors.toList());
			result.add(cumulateList);

			// 获取用户分享数据
			List<Object> shareList = cubeService.getUserShare(start, end).stream()
					.map(WxDataCubeArticleResult::getShareCount).collect(Collectors.toList());
			result.add(shareList);

			// 获取消息发送概况数据
			List<Object> upstreamList = cubeService.getUpstreamMsg(start, end).stream()
					.map(WxDataCubeMsgResult::getMsgCount).collect(Collectors.toList());
			result.add(upstreamList);

			// 获取接口调用概况数据
			List<WxDataCubeInterfaceResult> interfaceSummaryList = cubeService.getInterfaceSummary(start, end);
			List<Object> interfaceList = interfaceSummaryList.stream().map(WxDataCubeInterfaceResult::getCallbackCount)
					.collect(Collectors.toList());
			result.add(interfaceList);

			// 接口日期保存
			List<Object> dateList = interfaceSummaryList.stream().map(WxDataCubeInterfaceResult::getRefDate)
					.collect(Collectors.toList());
			result.add(dateList);
		}
		catch (WxErrorException e) {
			log.error(" 获取公众号统计数据报错", e);
			return R.failed("获取公众号数据失败:" + e.getError().getErrorMsg());
		}

		return R.ok(result);
	}

}
