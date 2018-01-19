package com.github.pig.auth.component.social;

import com.github.pig.common.constant.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.stereotype.Component;

/**
 * 继承默认的社交登录配置，加入自定义的后处理逻辑
 * Created on 2018/1/8 0008.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
@Component
public class PigSocialConfigurer extends SpringSocialConfigurer {
    @Autowired
    private AuthenticationSuccessHandler socialLoginSuccessHandler;
    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        filter.setFilterProcessesUrl(SecurityConstants.DEFAULT_SOCIAL_PROCESS_URL);
        filter.setAlwaysUsePostLoginUrl(true);
        filter.setAuthenticationSuccessHandler(socialLoginSuccessHandler);
        return (T) filter;
    }

}
