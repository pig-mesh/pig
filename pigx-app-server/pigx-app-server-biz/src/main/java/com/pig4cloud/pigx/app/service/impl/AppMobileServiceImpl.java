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

package com.pig4cloud.pigx.app.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.pig4cloud.pigx.app.api.enums.AppErrorCodes;
import com.pig4cloud.pigx.app.service.AppMobileService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.enums.LoginTypeEnum;
import com.pig4cloud.pigx.common.core.util.MsgUtils;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.cache.RedisUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * App 手机验证码服务实现。
 * <p>
 * 验证码按 {@code 验证码缓存前缀 + 登录类型 + 手机号} 写入 Redis，
 * 过期时间使用统一安全常量配置。
 *
 * @author lengleng
 * @date 2018/11/14
 * <p>
 * 手机登录相关业务实现
 */
@Slf4j
@Service
@AllArgsConstructor
public class AppMobileServiceImpl implements AppMobileService {

    /**
     * 发送手机验证码。
     * <p>
     * 如果验证码仍在有效期内，则拒绝重复发送。当前实现只生成并缓存验证码，
     * 后续接入短信网关时应在生成验证码后调用真实发送能力。
     *
     * @param mobile 手机号
     * @return true=生成成功，false=验证码未过期
     */
    @Override
    public R<Boolean> sendSmsCode(String mobile) {
        String codeObj = RedisUtils.get(CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.APPSMS.getType() + StringPool.AT + mobile);

        if (StrUtil.isNotBlank(codeObj)) {
            log.info("手机号验证码未过期:{}，{}", mobile, codeObj);
            return R.ok(Boolean.FALSE, MsgUtils.getMessage(AppErrorCodes.APP_SMS_OFTEN));
        }

        String code = RandomUtil.randomNumbers(Integer.parseInt(SecurityConstants.CODE_SIZE));
        log.debug("手机号生成验证码成功:{},{}", mobile, code);
        RedisUtils.set(CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.APPSMS.getType() + StringPool.AT + mobile, code, SecurityConstants.CODE_TIME);
        return R.ok(Boolean.TRUE);
    }

}
