package com.pig4cloud.pigx.common.sensitive;

import com.pig4cloud.pigx.common.sensitive.core.SensitiveService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * 脱敏配置类
 *
 * @author lengleng
 * @date 2024/6/27
 */
@AutoConfiguration
@Configuration(proxyBeanMethods = false)
public class SensitiveAutoConfiguration {
    /**
     * 不脱敏的权限
     */
    public static String NO_MASK = "no_mask";


    /**
     * 注入默认的脱敏权限判断
     *
     * @return SensitiveService
     */
    @Bean
    @ConditionalOnMissingBean
    public SensitiveService sensitiveService() {
        return sensitive -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (Objects.isNull(authentication)) {
                return true;
            }

            // 判断角色是否包含 no_mask 的权限
            return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .noneMatch(s -> s.equals(NO_MASK));
        };
    }
}
