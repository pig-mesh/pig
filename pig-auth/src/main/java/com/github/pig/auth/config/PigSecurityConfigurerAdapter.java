package com.github.pig.auth.config;

import com.github.pig.auth.component.mobile.MobileSecurityConfigurer;
import com.github.pig.common.bean.config.FilterUrlsPropertiesConifg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @author lengleng
 * @date 2018/3/10
 */
@Configuration
@EnableWebSecurity
public class PigSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
    @Autowired
    private FilterUrlsPropertiesConifg filterUrlsPropertiesConifg;
    @Autowired
    private MobileSecurityConfigurer mobileSecurityConfigurer;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
                http.formLogin().loginPage("/authentication/require")
                        .loginProcessingUrl("/authentication/form")
                        .and()
                        .authorizeRequests();
        for (String url : filterUrlsPropertiesConifg.getAnon()) {
            registry.antMatchers(url).permitAll();
        }
        registry.anyRequest().authenticated()
                .and()
                .csrf().disable();
        http.apply(mobileSecurityConfigurer);
    }
}
