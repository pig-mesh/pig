package com.github.pig.gateway.service.impl;

import com.github.pig.gateway.feign.MenuService;
import com.github.pig.gateway.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @author lengleng
 * @date 2017/10/28
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private MenuService menuService;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        //ele-admin options 跨域配置
        if (HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        Object principal = authentication.getPrincipal();
        List<SimpleGrantedAuthority> grantedAuthorityList = (List<SimpleGrantedAuthority>) authentication.getAuthorities();
        boolean hasPermission = false;

        if (principal != null) {
            Set<String> urls = menuService.findMenuByRole(grantedAuthorityList.get(0).getAuthority());
            for (String url : urls) {
                if (antPathMatcher.match(url,request.getRequestURI())) {
                    hasPermission = true;
                    break;
                }
            }

        }
        return hasPermission;
    }
}
