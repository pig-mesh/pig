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

package com.pig4cloud.pigx.admin.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.pig4cloud.pigx.admin.api.constant.UpmsErrorCodes;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.mapper.SysSocialDetailsMapper;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.enums.LoginTypeEnum;
import com.pig4cloud.pigx.common.core.exception.CheckedException;
import com.pig4cloud.pigx.common.core.util.MsgUtils;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author lengleng
 * @date 2019年11月02日
 * <p>
 * 微信小程序
 */
@Slf4j
@Component("MINI")
@AllArgsConstructor
public class MiniAppLoginHandler extends AbstractLoginHandler {

    private final SysUserService sysUserService;

    private final SysSocialDetailsMapper sysSocialDetailsMapper;

    /**
     * 小程序登录传入code
     * <p>
     * 通过code 调用qq 获取唯一标识
     *
     * @param code
     * @return
     */
    @Override
    public String identify(String code) {
        SysSocialDetails condition = new SysSocialDetails();
        condition.setType(LoginTypeEnum.MINI_APP.getType());
        SysSocialDetails socialDetails = sysSocialDetailsMapper.selectOne(new QueryWrapper<>(condition));

        String url = String.format(SecurityConstants.MINI_APP_AUTHORIZATION_CODE_URL, socialDetails.getAppId(),
                socialDetails.getAppSecret(), code);
        String result = HttpUtil.get(url);
        log.debug("微信小程序响应报文:{}", result);
        JSONObject resultJsonObj = JSONUtil.parseObj(result);

        if (resultJsonObj.containsKey("errcode")) {
            log.error("微信小程序登录失败:{}", result);
            throw new CheckedException(MsgUtils.getMessage(UpmsErrorCodes.SYS_MINIAPP_LOGIN_FAILED));
        }

        return resultJsonObj.getStr("openid");
    }

    /**
     * 根据openId获取用户信息
     *
     * @param openId 用户openId
     * @return 用户信息对象，未找到时返回null
     */
    @Override
    public UserInfo info(String openId) {
        if (StrUtil.isBlank(openId)) {
            log.warn("小程序openId为空，无法获取用户信息");
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setMiniOpenid(openId);

        R<UserInfo> userInfoR = sysUserService.getUserInfo(userDTO);

        if (userInfoR.getData() == null) {
            log.info("小程序不存在用户:{}, 开始自动创建", openId);
            return createMiniAppUser(openId);
        }

        return userInfoR.getData();
    }

    /**
     * 绑定逻辑
     *
     * @param user     用户实体
     * @param identify 渠道返回唯一标识
     * @return
     */
    @Override
    public Boolean bind(SysUser user, String identify) {
        user.setMiniOpenid(identify);
        sysUserService.updateById(user);
        return true;
    }

    /**
     * 自动创建小程序用户
     *
     * @param openId 小程序openId
     * @return 创建成功返回用户信息，失败返回null
     */
    private UserInfo createMiniAppUser(String openId) {
        try {
            log.info("开始为openId[{}]创建小程序用户", openId);

            // 构造用户DTO
            UserDTO newUser = new UserDTO();
            newUser.setMiniOpenid(openId);
            newUser.setUsername(openId);
            newUser.setPassword(openId);
            newUser.setTenantId(TenantContextHolder.getTenantId());
            // 保存用户
            sysUserService.saveUser(newUser);
            log.info("小程序用户创建成功，openId: {}", openId);

            // 重新获取用户信息
            UserDTO queryDTO = new UserDTO();
            queryDTO.setMiniOpenid(openId);
            R<UserInfo> userInfoR = sysUserService.getUserInfo(queryDTO);

            if (userInfoR.getData() != null) {
                return userInfoR.getData();
            }
        } catch (Exception e) {
            log.error("创建小程序用户异常，openId: {}, 错误信息: {}", openId, e.getMessage(), e);
        }

        return null;
    }

}
