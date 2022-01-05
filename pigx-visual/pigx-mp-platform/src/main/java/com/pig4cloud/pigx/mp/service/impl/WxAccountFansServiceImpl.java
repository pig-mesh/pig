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

import cn.binarywang.tools.generator.ChineseNameGenerator;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.data.tenant.TenantBroker;
import com.pig4cloud.pigx.mp.config.WxMpInitConfigRunner;
import com.pig4cloud.pigx.mp.entity.WxAccount;
import com.pig4cloud.pigx.mp.entity.WxAccountFans;
import com.pig4cloud.pigx.mp.mapper.WxAccountFansMapper;
import com.pig4cloud.pigx.mp.mapper.WxAccountMapper;
import com.pig4cloud.pigx.mp.service.WxAccountFansService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2019-03-26 22:08:08
 * <p>
 * 微信公众号粉丝
 */
@Slf4j
@Service
@AllArgsConstructor
public class WxAccountFansServiceImpl extends ServiceImpl<WxAccountFansMapper, WxAccountFans>
		implements WxAccountFansService {

	private static final int SIZE = 100;

	private final WxAccountMapper wxAccountMapper;

	/**
	 * 获取公众号粉丝，生产建议异步
	 * @param appId
	 * @return
	 */
	@Async
	@Override
	public Boolean syncAccountFans(String appId) {
		WxAccount wxAccount = wxAccountMapper
				.selectOne(Wrappers.<WxAccount>query().lambda().eq(WxAccount::getAppid, appId));

		// 获取操作微信接口类
		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
		WxMpUserService wxMpUserService = wxMpService.getUserService();
		// 根据公众号查询已同步用户openid 查询最新的一条 (注意不能按订阅时间排序)
		String finalNextOpenId = queryNextOpenId(appId);
		TenantBroker.runAs(() -> WxMpInitConfigRunner.getTenants().get(appId),
				(id) -> fetchUser(finalNextOpenId, wxAccount, wxMpUserService));

		log.info("公众号 {} 粉丝同步完成", wxAccount.getName());
		return Boolean.TRUE;
	}

	/**
	 * 获取微信用户
	 * @param nextOpenid 下一组开始的openid
	 * @param wxAccount 公众号信息
	 * @param wxMpUserService mp操作类
	 */
	private void fetchUser(String nextOpenid, WxAccount wxAccount, WxMpUserService wxMpUserService) {
		try {
			WxMpUserList wxMpUserList = wxMpUserService.userList(nextOpenid);
			// openId 分组 每组 100个 openid
			List<List<String>> openIdsList = CollUtil.split(wxMpUserList.getOpenids(), SIZE).stream()
					.filter(CollUtil::isNotEmpty).collect(Collectors.toList());
			// 处理每个分组. 调用查询用户信息
			for (List<String> openIdList : openIdsList) {
				log.info("开始批量获取用户信息 {}", openIdList);
				List<WxAccountFans> wxAccountFansList = new ArrayList<>();
				wxMpUserService.userInfoList(openIdList).forEach(wxMpUser -> {
					WxAccountFans wxAccountFans = buildDbUser(wxAccount, wxMpUser);
					wxAccountFansList.add(wxAccountFans);
				});
				this.saveOrUpdateBatch(wxAccountFansList);
				log.info("批量插入用户信息完成 {}", openIdList);
			}
			// 如果nextOpenId 不为空，则继续获取
			if (StrUtil.isNotBlank(wxMpUserList.getNextOpenid())) {
				fetchUser(wxMpUserList.getNextOpenid(), wxAccount, wxMpUserService);
			}
		}
		catch (Exception e) {
			log.warn("同步微信公众号 {} 粉丝异常 {}", wxAccount.getName(), e.getMessage());
			// 失败重试逻辑
			String finalNextOpenId = queryNextOpenId(wxAccount.getAppid());
			fetchUser(finalNextOpenId, wxAccount, wxMpUserService);
		}

	}

	/**
	 * 构建数据库存取对象
	 * @param wxAccount 公众号信息
	 * @param wxMpUser 用户微信信息
	 */
	private WxAccountFans buildDbUser(WxAccount wxAccount, WxMpUser wxMpUser) {
		WxAccountFans wxAccountFans = new WxAccountFans();
		wxAccountFans.setOpenid(wxMpUser.getOpenId());
		wxAccountFans.setSubscribeStatus(String.valueOf(BooleanUtil.toInt(wxMpUser.getSubscribe())));

		// 2020-11-25 部分用户订阅时间为空，跳过此字段处理
		if (ObjectUtil.isNotEmpty(wxMpUser.getSubscribeTime())) {
			wxAccountFans.setSubscribeTime(LocalDateTime
					.ofInstant(Instant.ofEpochMilli(wxMpUser.getSubscribeTime() * 1000L), ZoneId.systemDefault()));
		}

		// 随机生成一个昵称，方便平台内部使用
		String generatedName = ChineseNameGenerator.getInstance().generate();
		wxAccountFans.setNickname(generatedName);
		wxAccountFans.setLanguage(wxMpUser.getLanguage());
		wxAccountFans.setHeadimgUrl(wxMpUser.getHeadImgUrl());
		wxAccountFans.setRemark(wxMpUser.getRemark());
		wxAccountFans.setWxAccountId(wxAccount.getId());
		wxAccountFans.setWxAccountAppid(wxAccount.getAppid());
		wxAccountFans.setWxAccountName(wxAccount.getName());
		return wxAccountFans;
	}

	/**
	 * 查询当前公众号最后插入的openId
	 * @param appId 公众号标识
	 * @return openid / null
	 */
	private String queryNextOpenId(String appId) {
		Page<WxAccountFans> queryPage = new Page<>(0, 1);
		Page<WxAccountFans> fansPage = baseMapper.selectPage(queryPage, Wrappers.<WxAccountFans>query().lambda()
				.eq(WxAccountFans::getWxAccountAppid, appId).orderByDesc(WxAccountFans::getCreateTime));
		String nextOpenId = null;
		if (fansPage.getTotal() > 0) {
			nextOpenId = fansPage.getRecords().get(0).getOpenid();
		}
		return nextOpenId;
	}

}
