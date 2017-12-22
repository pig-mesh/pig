package com.github.pig.common.bean.resolver;

import com.github.pig.common.util.UserUtils;
import com.github.pig.common.vo.SysRole;
import com.github.pig.common.vo.UserVo;
import com.xiaoleilu.hutool.util.CollectionUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2017/12/21
 * Token转化UserVo
 */
@Configuration
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(UserVo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        String username = UserUtils.getUserName(request);
        List<String> roles = UserUtils.getRole(request);
        logger.info("Auth-Token-User:{}-Roles:{}", username, roles);
        UserVo userVo = new UserVo();
        if (StringUtils.isNotEmpty(username)) {
            userVo.setUsername(username);
        }
        if (CollectionUtil.isNotEmpty(roles)) {
            List<SysRole> sysRoleList = new ArrayList<>();
            for (String roleName : roles) {
                SysRole sysRole = new SysRole();
                sysRole.setRoleName(roleName);
                sysRoleList.add(sysRole);
            }
            userVo.setRoleList(sysRoleList);
        }
        return userVo;
    }
}
