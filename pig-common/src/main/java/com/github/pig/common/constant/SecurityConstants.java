package com.github.pig.common.constant;

/**
 * @author lengleng
 * @date 2017-12-18
 */
public interface SecurityConstants {
    /**
     * 密码模式
     */
    String PASSWORD = "password";
    /**
     * 刷新token
     */
    String REFRESH_TOKEN = "refresh_token";

    /**
     * oauth token
     */
    String OAUTH_TOKEN_URL = "/oauth/token";

    /**
     * 手机登录URL
     */
    String MOBILE_TOKEN_URL = "/mobile/token";

    /**
     * 默认的处理验证码的url前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";

    /**
     * 手机号的处理验证码的url前缀
     */
    String MOBILE_VALIDATE_CODE_URL_PREFIX = "/smsCode";

    /**
     * 默认生成图形验证码宽度
     */
    String DEFAULT_IMAGE_WIDTH = "150";

    /**
     * 默认生成图像验证码高度
     */
    String DEFAULT_IMAGE_HEIGHT = "32";

    /**
     * 默认生成图形验证码长度
     */
    String DEFAULT_IMAGE_LENGTH = "4";

    /**
     * 默认生成图形验证码过期时间
     */
    int DEFAULT_IMAGE_EXPIRE = 60;
    /**
     * 边框颜色，合法值： r,g,b (and optional alpha) 或者 white,black,blue.
     */
    String DEFAULT_COLOR_FONT = "black";

    /**
     * 图片边框
     */
    String DEFAULT_IMAGE_BORDER = "no";
    /**
     * 默认图片间隔
     */
    String DEFAULT_CHAR_SPACE = "5";

    /**
     * 默认保存code的前缀
     */
    String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY";
    /**
     * 验证码文字大小
     */
    String DEFAULT_IMAGE_FONT_SIZE = "30";

    /**
     * token-uservo
     */
    String TOKEN_USER_DETAIL = "token-user-detail";

    /**
     * 认证服务的SERVICEID（zuul 配置的对应）
     */
    String AUTH_SERVICE_ID = "auth-service";
}
