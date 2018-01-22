package com.github.pig.auth.component.social.qq.config;

import com.github.pig.auth.component.social.qq.connect.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * Created on 2018/1/8 0008.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
@Configuration
public class QQAuthConfig extends SocialAutoConfigurerAdapter {
    @Autowired
    private SocialQQPropertiesConfig socialQQPropertiesConfig;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        return new QQConnectionFactory(socialQQPropertiesConfig.getProviderId(),
                socialQQPropertiesConfig.getClientId(), socialQQPropertiesConfig.getClientSecret());
    }
}
