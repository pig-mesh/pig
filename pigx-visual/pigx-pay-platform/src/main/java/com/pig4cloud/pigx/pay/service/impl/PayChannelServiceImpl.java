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
package com.pig4cloud.pigx.pay.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.sensitive.util.DesensitizedUtils;
import com.pig4cloud.pigx.pay.entity.PayChannel;
import com.pig4cloud.pigx.pay.mapper.PayChannelMapper;
import com.pig4cloud.pigx.pay.service.PayChannelService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 渠道
 *
 * @author lengleng
 * @date 2019-05-28 23:57:58
 */
@Service
@AllArgsConstructor
public class PayChannelServiceImpl extends ServiceImpl<PayChannelMapper, PayChannel> implements PayChannelService {

    /**
     * 新增支付渠道
     *
     * @param payChannel 支付渠道
     * @return
     */
    @Override
    public Boolean saveChannel(PayChannel payChannel) {
        return save(payChannel);
    }

    /**
     * 分页查询
     *
     * @param page       页
     * @param payChannel 付费频道
     * @return {@link Page }<{@link PayChannel }>
     */
    @Override
    public Page<PayChannel> queryPage(Page page, PayChannel payChannel) {
        LambdaQueryWrapper<PayChannel> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(payChannel.getChannelName()), PayChannel::getChannelName,
                payChannel.getChannelName());
        wrapper.eq(StrUtil.isNotBlank(payChannel.getState()), PayChannel::getState, payChannel.getState());

        Page<PayChannel> selectedPage = baseMapper.selectPage(page, wrapper);

        // 对敏感信息进行脱敏
        selectedPage.setRecords(selectedPage.getRecords().stream().peek(channel -> channel
                .setParam(DesensitizedUtils.json(channel.getParam(), true, "key"))).toList());

        return selectedPage;
    }

    /**
     * 获取channel
     *
     * @param id id
     * @return {@link PayChannel }
     */
    @Override
    public PayChannel getChannel(Long id) {

        PayChannel channel = getById(id);

        if (channel == null) {
            return null;
        }

        // 手动对key进行脱敏
        channel.setParam(DesensitizedUtils.json(channel.getParam(), true, "key"));
        return channel;
    }


}
