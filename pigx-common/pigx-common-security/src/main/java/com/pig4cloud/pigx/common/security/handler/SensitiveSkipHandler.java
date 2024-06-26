package com.pig4cloud.pigx.common.security.handler;

import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.sensitive.SensitiveSkipContextHolder;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 敏感词跳过的处理器
 *
 * @author lengleng
 * @date 2024/6/26
 */
public class SensitiveSkipHandler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        PigxUser user = SecurityUtils.getUser();

        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                    .toList().contains(SecurityConstants.NO_MASK)) {
                SensitiveSkipContextHolder.setSkip(true);
            }
            filterChain.doFilter(request, response);
        } finally {
            SensitiveSkipContextHolder.clear();
        }
    }
}
