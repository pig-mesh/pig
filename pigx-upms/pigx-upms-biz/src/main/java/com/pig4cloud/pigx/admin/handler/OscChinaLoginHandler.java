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
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserInfo;
import com.pig4cloud.pigx.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.mapper.SysSocialDetailsMapper;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.enums.LoginTypeEnum;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2019/4/8
 * <p>
 * 开源中国登录
 */
@Slf4j
@Component("OSC")
@AllArgsConstructor
public class OscChinaLoginHandler extends AbstractLoginHandler {

    private final SysSocialDetailsMapper sysSocialDetailsMapper;

    private final SysUserService sysUserService;

    /**
     * 开源中国传入code
     * <p>
     * 通过code 调用qq 获取唯一标识
     *
     * @param code
     * @return
     */
    @Override
    public String identify(String code) {
        SysSocialDetails condition = new SysSocialDetails();
        condition.setType(LoginTypeEnum.OSC.getType());
        SysSocialDetails socialDetails = sysSocialDetailsMapper.selectOne(new QueryWrapper<>(condition));

        Map<String, Object> params = new HashMap<>(8);

        params.put("client_id", socialDetails.getAppId());
        params.put("client_secret", socialDetails.getAppSecret());
        params.put("grant_type", "authorization_code");
        params.put("redirect_uri", socialDetails.getRedirectUrl());
        params.put("code", code);
        params.put("dataType", "json");

        String result = HttpUtil.post(SecurityConstants.OSC_AUTHORIZATION_CODE_URL, params);
        log.debug("开源中国响应报文:{}", result);

        String accessToken = JSONUtil.parseObj(result).getStr("access_token");

        String url = String.format(SecurityConstants.OSC_USER_INFO_URL, accessToken);
        String resp = HttpUtil.get(url);
        log.debug("开源中国获取个人信息返回报文{}", resp);

        JSONObject userInfo = JSONUtil.parseObj(resp);
        // 开源中国唯一标识
        return userInfo.getStr("id");
    }

    /**
     * 根据开源中国标识获取用户信息
     *
     * @param identify 开源中国用户标识
     * @return 用户信息对象，未找到时返回null
     */
    @Override
    public UserInfo info(String identify) {
        if (StrUtil.isBlank(identify)) {
            log.warn("开源中国标识为空，无法获取用户信息");
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setOscId(identify);

        R<UserInfo> userInfoR = sysUserService.getUserInfo(userDTO);

        if (userInfoR.getData() == null) {
            log.info("开源中国 不存在用户:{}", identify);
            return null;
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
        user.setOscId(identify);
        sysUserService.updateById(user);
		return true;
	}

}
