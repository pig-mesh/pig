package com.github.pig.common.util;

import com.github.pig.common.constant.CommonConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * @author lengleng
 * @date 2017/11/20
 * 用户相关工具类
 */
public class UserUtils {
    private static Logger logger = LoggerFactory.getLogger(UserUtils.class);
    private static final ThreadLocal<String> TL_User = new ThreadLocal<>();
    private static final String KEY_USER = "user";


    /**
     * 根据用户请求中的token 获取用户名
     *
     * @param request Request
     * @return “”、username
     */
    public static String getUserName(HttpServletRequest request) {
        String username = "";
        String authorization = request.getHeader(CommonConstant.REQ_HEADER);
        if (StringUtils.isEmpty(authorization)) {
            return username;
        }
        String token = StringUtils.substringAfter(authorization, CommonConstant.TOKEN_SPLIT);
        if (StringUtils.isEmpty(token)) {
            return username;
        }
        String key = Base64.getEncoder().encodeToString(CommonConstant.SIGN_KEY.getBytes());
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            username = claims.get("user_name").toString();
        } catch (Exception ex) {
            logger.error("用户名解析异常,token:{},key:{}", token, key);
        }
        return username;
    }

    /**
     * 通过token 获取用户名
     *
     * @param authorization
     * @return
     */
    public static String getUserName(String authorization) {
        String username = "";
        String token = StringUtils.substringAfter(authorization, CommonConstant.TOKEN_SPLIT);
        if (StringUtils.isEmpty(token)) {
            return username;
        }
        String key = Base64.getEncoder().encodeToString(CommonConstant.SIGN_KEY.getBytes());
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            username = claims.get("user_name").toString();
        } catch (Exception ex) {
            logger.error("用户名解析异常,token:{},key:{}", token, key);
        }
        return username;
    }

    /**
     * 设置用户信息
     *
     * @param username
     */
    public static void setUser(String username) {
        TL_User.set(username);

        MDC.put(KEY_USER, username);
    }

    /**
     * 如果没有登录，返回null
     *
     * @return
     */
    public static String getUserName() {
        return TL_User.get();
    }

    public static void clearAllUserInfo() {
        TL_User.remove();
        MDC.remove(KEY_USER);
    }
}
