package com.github.pig.common.bean.resolver;

import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.common.util.UserUtils;
import com.github.pig.common.vo.SysRole;
import com.github.pig.common.vo.UserVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lengleng
 * @date 2017/12/21
 * Token转化UserVo
 */
@Configuration
public class TokenArgumentResolver implements HandlerMethodArgumentResolver {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private CacheManager cacheManager;

    public TokenArgumentResolver(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * 1. 入参筛选
     *
     * @param methodParameter 参数集合
     * @return 格式化后的参数
     */
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(UserVo.class);
    }

    /**
     * 1. 先从 cache 中判断token 是否已经有缓存
     * 2. 不存在缓存情况，解析token 获取用户信息
     * 3. 不存在缓存情况，在AOP进行缓存添加，因为这里添加只会对入参含有 UserVo的生效，而不是全局
     *
     * @param methodParameter       入参集合
     * @param modelAndViewContainer model 和 view
     * @param nativeWebRequest      web相关
     * @param webDataBinderFactory  入参解析
     * @return 包装对象
     * @throws Exception exception
     */
    @Override
    public Object resolveArgument(MethodParameter methodParameter,
                                  ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest,
                                  WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);

        String token = UserUtils.getToken(request);
        if (StringUtils.isBlank(token)) {
            logger.error("resolveArgument error token is empty");
            return null;
        }
        Optional<UserVo> optional = Optional.ofNullable(cacheManager.getCache(SecurityConstants.TOKEN_USER_DETAIL).get(token, UserVo.class));
        if (optional.isPresent()) {
            logger.info("return cache user vo,token :{}", token);
            return optional.get();
        }
        return optional.orElseGet(() -> generatorByToken(request, token));
    }

    private UserVo generatorByToken(HttpServletRequest request, String token) {
        String username = UserUtils.getUserName(request);
        List<String> roles = UserUtils.getRole(request);
        logger.info("Auth-Token-User:{}-Roles:{}", username, roles);
        UserVo userVo = new UserVo();
        userVo.setUsername(username);
        List<SysRole> sysRoleList = new ArrayList<>();
        roles.stream().forEach(role -> {
            SysRole sysRole = new SysRole();
            sysRole.setRoleName(role);
            sysRoleList.add(sysRole);
        });
        userVo.setRoleList(sysRoleList);
        cacheManager.getCache(SecurityConstants.TOKEN_USER_DETAIL).put(token, userVo);
        return userVo;
    }

}
