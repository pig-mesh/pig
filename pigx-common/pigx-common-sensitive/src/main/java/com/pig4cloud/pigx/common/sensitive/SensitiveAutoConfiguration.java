package com.pig4cloud.pigx.common.sensitive;

import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.common.sensitive.core.SensitiveService;
import com.pig4cloud.pigx.common.sensitive.util.RemoteSensitiveService;
import com.pig4cloud.pigx.common.sensitive.util.SensitiveWordsProperties;
import com.pig4cloud.pigx.common.sensitive.word.SensitiveWordCustomizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

/**
 * 脱敏配置类
 *
 * @author lengleng
 * @date 2024/6/27
 */
@Slf4j
@AutoConfiguration
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(SensitiveWordsProperties.class)
public class SensitiveAutoConfiguration implements InitializingBean {

    private final Optional<RedisMessageListenerContainer> redisMessageListenerContainerOptional;

    /**
     * 注入默认的脱敏权限判断
     *
     * @return SensitiveService
     */
    @Bean
    @ConditionalOnMissingBean
    public SensitiveService sensitiveService(SensitiveWordsProperties sensitiveWordsProperties) {
        return sensitive -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.isNull(authentication)) {
                return true;
            }

            // 判断角色是否包含 no_mask 的权限
            return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .noneMatch(s -> s.equals(sensitiveWordsProperties.getMask()));
        };
    }

    /**
     * 初始化敏感词定制
     *
     * @return SensitiveWordCustomizer
     */
    @Bean
    @Lazy
    public SensitiveWordCustomizer sensitiveWordCustomizer(RemoteSensitiveService remoteSensitiveService) {
        return new SensitiveWordCustomizer(remoteSensitiveService);
    }

    /**
     * 懒加载初始化引导类
     *
     * @return 初始化引导类
     */
    @Bean
    @Lazy
    public SensitiveWordBs sensitiveWordBs(@Lazy SensitiveWordCustomizer sensitiveWordCustomizer
            , SensitiveWordsProperties sensitiveWordsProperties) {


        return SensitiveWordBs.newInstance()
                // 加载白名单数据
                .wordAllow(sensitiveWordCustomizer)
                // 加载黑名单数据
                .wordDeny(WordDenys.chains(WordDenys.defaults(), sensitiveWordCustomizer))
                // 忽略大小写
                .ignoreCase(sensitiveWordsProperties.isIgnoreCase())
                // 忽略半角圆角
                .ignoreWidth(sensitiveWordsProperties.isIgnoreWidth())
                // 忽略数字的写法
                .ignoreNumStyle(sensitiveWordsProperties.isIgnoreNumStyle())
                // 忽略中文的书写格式：简繁体
                .ignoreChineseStyle(sensitiveWordsProperties.isIgnoreChineseStyle())
                // 忽略英文的书写格式
                .ignoreEnglishStyle(sensitiveWordsProperties.isIgnoreEnglishStyle())
                // 忽略重复词
                .ignoreRepeat(sensitiveWordsProperties.isIgnoreRepeat())
                // 是否启用数字检测
                .enableNumCheck(sensitiveWordsProperties.isEnableNumCheck())
                // 是否启用邮箱检测
                .enableEmailCheck(sensitiveWordsProperties.isEnableEmailCheck())
                // 是否启用链接检测
                .enableUrlCheck(sensitiveWordsProperties.isEnableUrlCheck())
                // 数字检测，自定义指定长度
                .numCheckLen(sensitiveWordsProperties.getNumCheckLen())
                .init();
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        redisMessageListenerContainerOptional.ifPresent(redisMessageListenerContainer -> {
            redisMessageListenerContainer.addMessageListener((message, bytes) -> {
                        log.info("开始重新加载敏感词库");
                        SpringContextHolder.getBean(SensitiveWordBs.class).init();
                        log.info("敏感词库加载完成");
                    },
                    new ChannelTopic(CacheConstants.SENSITIVE_REDIS_RELOAD_TOPIC));
        });
    }
}
