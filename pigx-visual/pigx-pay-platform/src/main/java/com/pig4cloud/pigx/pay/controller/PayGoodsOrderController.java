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

package com.pig4cloud.pigx.pay.controller;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import com.pig4cloud.pigx.pay.entity.PayChannel;
import com.pig4cloud.pigx.pay.entity.PayGoodsOrder;
import com.pig4cloud.pigx.pay.service.PayChannelService;
import com.pig4cloud.pigx.pay.service.PayGoodsOrderService;
import com.pig4cloud.pigx.pay.utils.PayChannelNameEnum;
import com.pig4cloud.pigx.pay.utils.PayConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 商品
 *
 * @author lengleng
 * @date 2019-05-28 23:58:27
 */
@Slf4j
@Controller
@AllArgsConstructor
@RequestMapping("/goods")
@Tag(description = "goods", name = "商品订单管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class PayGoodsOrderController {

	private final PayGoodsOrderService payGoodsOrderService;

	private final PayChannelService channelService;

	private final ObjectMapper objectMapper;

	/**
	 * 商品订单
	 * @param goods 商品
	 * @param response
	 *
	 * AliPayApiConfigKit.setAppId WxPayApiConfigKit.setAppId shezhi
	 *
	 */
	@SneakyThrows
	@Inner(false)
	@GetMapping("/buy")
	@SysLog("购买商品")
	public void buy(PayGoodsOrder goods, HttpServletRequest request, HttpServletResponse response) {
		String ua = request.getHeader(HttpHeaders.USER_AGENT);
		log.info("当前扫码方式 UA:{}", ua);

		if (ua.contains(PayConstants.MICRO_MESSENGER)) {
			PayChannel channel = channelService.getOne(
					Wrappers.<PayChannel>lambdaQuery().eq(PayChannel::getChannelId, PayChannelNameEnum.WEIXIN_MP),
					false);

			if (channel == null) {
				throw new IllegalArgumentException("公众号支付配置不存在");
			}

			String wxUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s"
					+ "&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s";

			String redirectUri = String.format("%s/pay/goods/wx?amount=%s&TENANT-ID=%s", channel.getNotifyUrl(),
					goods.getAmount(), TenantContextHolder.getTenantId());

			response.sendRedirect(
					String.format(wxUrl, channel.getAppId(), URLUtil.encode(redirectUri), channel.getAppId()));
		}

		if (ua.contains(PayConstants.ALIPAY)) {
			payGoodsOrderService.buy(goods, false);
		}

	}

	@SneakyThrows
	@Inner(false)
	@GetMapping("/merge/buy")
	@SysLog("聚合支付购买商品")
	public void mergeBuy(PayGoodsOrder goods, HttpServletResponse response) {
		Map<String, Object> result = payGoodsOrderService.buy(goods, true);
		response.setContentType(ContentType.JSON.getValue());
		response.getWriter().print(objectMapper.writeValueAsString(result));
	}

	/**
	 * oauth
	 * @param goods 商品信息
	 * @param code 回调code
	 * @param modelAndView
	 * @return
	 * @throws WxErrorException
	 */
	@Inner(false)
	@SneakyThrows
	@GetMapping("/wx")
	public ModelAndView wx(PayGoodsOrder goods, String code, ModelAndView modelAndView) {
		PayChannel channel = channelService.getOne(
				Wrappers.<PayChannel>lambdaQuery().eq(PayChannel::getChannelId, PayChannelNameEnum.WEIXIN_MP), false);

		if (channel == null) {
			throw new IllegalArgumentException("公众号支付配置不存在");
		}

		JSONObject params = JSONUtil.parseObj(channel.getParam());
		WxMpService wxMpService = new WxMpServiceImpl();
		WxMpDefaultConfigImpl storage = new WxMpDefaultConfigImpl();
		storage.setAppId(channel.getAppId());
		storage.setSecret(params.getStr("secret"));
		wxMpService.setWxMpConfigStorage(storage);

		WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
		goods.setUserId(accessToken.getOpenId());
		goods.setAmount(goods.getAmount());
		modelAndView.setViewName("pay");
		modelAndView.addAllObjects(payGoodsOrderService.buy(goods, false));
		return modelAndView;
	}

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param payGoodsOrder 商品订单表
	 * @return
	 */
	@ResponseBody
	@GetMapping("/page")
	public R getPayGoodsOrderPage(Page page, PayGoodsOrder payGoodsOrder) {
		return R.ok(payGoodsOrderService.page(page, Wrappers.query(payGoodsOrder)));
	}

	/**
	 * 通过id查询商品订单表
	 * @param goodsOrderId id
	 * @return R
	 */
	@ResponseBody
	@GetMapping(value = "/{goodsOrderId}")
	public R getById(@PathVariable("goodsOrderId") Integer goodsOrderId) {
		return R.ok(payGoodsOrderService.getById(goodsOrderId));
	}

	/**
	 * 新增商品订单表
	 * @param payGoodsOrder 商品订单表
	 * @return R
	 */
	@SysLog("新增商品订单表")
	@PostMapping
	@ResponseBody
	@PreAuthorize("@pms.hasPermission('generator_paygoodsorder_add')")
	public R save(@RequestBody PayGoodsOrder payGoodsOrder) {
		return R.ok(payGoodsOrderService.save(payGoodsOrder));
	}

	/**
	 * 修改商品订单表
	 * @param payGoodsOrder 商品订单表
	 * @return R
	 */
	@SysLog("修改商品订单表")
	@PutMapping
	@ResponseBody
	@PreAuthorize("@pms.hasPermission('generator_paygoodsorder_edit')")
	public R updateById(@RequestBody PayGoodsOrder payGoodsOrder) {
		return R.ok(payGoodsOrderService.updateById(payGoodsOrder));
	}

	/**
	 * 通过id删除商品订单表
	 * @param goodsOrderId id
	 * @return R
	 */
	@SysLog("删除商品订单表")
	@ResponseBody
	@DeleteMapping("/{goodsOrderId}")
	@PreAuthorize("@pms.hasPermission('generator_paygoodsorder_del')")
	public R removeById(@PathVariable Integer goodsOrderId) {
		return R.ok(payGoodsOrderService.removeById(goodsOrderId));
	}

}
