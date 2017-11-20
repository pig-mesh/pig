package com.github.pig.common.util;

import com.github.pig.common.constant.CommonConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * @author lengleng
 * @date 2017/11/20
 * 用户相关工具类
 * TODO theardLocal 避免上下文开销
 */
public class UserUtils {
    private static Logger logger = LoggerFactory.getLogger(UserUtils.class);


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
     * @param token
     * @return
     */
    public static String getUserName(String authorization) {
        String username = "";
        String token = StringUtils.substringAfter(authorization, CommonConstant.TOKEN_SPLIT);
        String key = Base64.getEncoder().encodeToString(CommonConstant.SIGN_KEY.getBytes());
        try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            username = claims.get("user_name").toString();
        } catch (Exception ex) {
            logger.error("用户名解析异常,token:{},key:{}", token, key);
        }
        return username;
    }
}
