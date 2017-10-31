package com.github.pig.gateway.service.impl;

import com.github.pig.gateway.feign.MenuService;
import com.github.pig.gateway.service.PermissionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashSet;
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

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        Collection<SimpleGrantedAuthority> grantedAuthorityList = (Collection<SimpleGrantedAuthority>) authentication.getAuthorities();
        boolean hasPermission = false;

        if (principal != null) {
            //TODO  根据角色查询缓存，没有的查询数据库
            Set<String> urls = menuService.findMenuByRole("admin");

            for (String url : urls) {
                if (request.getRequestURI().contains(url)) {
                    hasPermission = true;
                    break;
                }
            }

        }
        return hasPermission;
    }
}
