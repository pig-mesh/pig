package com.github.pig.auth.controller;

import com.github.pig.auth.component.social.SocialUserInfo;
import com.github.pig.common.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lengleng
 * @date 2018/1/21
 * 控制社交登录
 */
@Slf4j
@Controller
public class SocialController {
    @Autowired
    private ProviderSignInUtils providerSignInUtils;

    /**
     * 未注册用户选择三方登录 重定向
     *
     * @param request  request
     * @return SocialUserInfo
     */
    @RequestMapping("/signup")
    @ResponseBody
    public R<SocialUserInfo> signUp(HttpServletRequest request) {
        SocialUserInfo userInfo = new SocialUserInfo();
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
        userInfo.setProviderId(connection.getKey().getProviderId());
        userInfo.setProviderUserId(connection.getKey().getProviderUserId());
        userInfo.setNickname(connection.getDisplayName());
        userInfo.setHeadImg(connection.getImageUrl());
        return new R<>(userInfo);
    }

    @RequestMapping("/binding")
    public R<Boolean> bindingSocial(Authentication authentication, HttpServletRequest request) {
        String username = (String) authentication.getPrincipal();
        providerSignInUtils.doPostSignUp(username, new ServletWebRequest(request));
        return new R<>(Boolean.TRUE);
    }
}
