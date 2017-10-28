package com.github.pig.gateway.service.impl;

import com.github.pig.gateway.service.PermissionService;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lengleng
 * @date 2017/10/28
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        boolean hasPermission = false;

        if (principal != null) {
            //TODO  根据用户名查询缓存，没有的查询数据库
            Set<String> urls = new HashSet<>();
            urls.add("/demo");

            for (String url : urls) {
                if (request.getRequestURI().contains(url)){
                    hasPermission =true;
                    break;
                }
            }

        }
        return hasPermission;
    }
}
