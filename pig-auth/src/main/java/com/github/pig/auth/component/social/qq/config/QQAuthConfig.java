package com.github.pig.auth.component.social.qq.config;

import com.github.pig.auth.component.social.qq.connect.QQConnectionFactory;
import com.github.pig.auth.component.social.repository.PigUsersConnectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

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
    @Autowired
    private ConnectionSignUp pigConnectionSignUp;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        return new QQConnectionFactory(socialQQPropertiesConfig.getProviderId(),
                socialQQPropertiesConfig.getClientId(), socialQQPropertiesConfig.getClientSecret());
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        PigUsersConnectionRepository repository = new PigUsersConnectionRepository();
        repository.setConnectionSignUp(pigConnectionSignUp);
        return repository;
    }

    @Bean
    public UsersConnectionRepository usersConnectionRepository(){
        PigUsersConnectionRepository repository = new PigUsersConnectionRepository();
        repository.setConnectionSignUp(pigConnectionSignUp);
        return repository;
    }
}
