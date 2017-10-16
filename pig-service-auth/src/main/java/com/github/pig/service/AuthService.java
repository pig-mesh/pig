package com.github.pig.service;

/**
 * @author lengleng
 */
public interface AuthService {
    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return token
     * @throws Exception Exception
     */
    String login(String username, String password) throws Exception;

    /**
     * 刷新token
     *
     * @param oldToken 旧的token
     * @return 新token
     */
    String refresh(String oldToken);

    /**
     * 校验token
     *
     * @param token token
     * @throws Exception Exception
     */
    void validate(String token) throws Exception;

    /**
     * token置为失效
     *
     * @param token token
     * @return
     */
    Boolean invalid(String token);

    /**
     * 通过token换取用户名
     *
     * @param token token
     * @return 用户名
     */
    String getUserNameByToken(String token);
}
