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

package com.github.pig.auth.feign.fallback;

import com.github.pig.auth.feign.UserService;
import com.github.pig.common.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author lengleng
 * @date 2017/10/31
 * 用户服务的fallback
 */
@Service
public class UserServiceFallbackImpl implements UserService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserVO findUserByUsername(String username) {
        logger.error("调用{}异常:{}", "findUserByUsername", username);
        return null;
    }

    /**
     * 通过手机号查询用户、角色信息
     *
     * @param mobile 手机号
     * @return UserVo
     */
    @Override
    public UserVO findUserByMobile(String mobile) {
        logger.error("调用{}异常:{}", "通过手机号查询用户", mobile);
        return null;
    }

    /**
     * 根据OpenId查询用户信息
     *
     * @param openId openId
     * @return UserVo
     */
    @Override
    public UserVO findUserByOpenId(String openId) {
        logger.error("调用{}异常:{}", "通过OpenId查询用户", openId);
        return null;
    }
}
