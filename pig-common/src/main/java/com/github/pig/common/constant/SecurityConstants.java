package com.github.pig.common.constant;

/**
 * @author lengleng
 * @date 2017-12-18
 */
public interface SecurityConstants {

    /**
     * 默认的处理验证码的url前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";

    /**
     * 默认生成图形验证码宽度
     */
    int DEFAULT_IMAGE_WIDTH = 67;

    /**
     * 默认生成图像验证码高度
     */
    int DEFAULT_IMAGE_HEIGHT = 23;

    /**
     * 默认生成图形验证码长度
     */
    int DEFAULT_IMAGE_LENGTH = 4;

    /**
     * 默认生成图形验证码过期时间
     */
    int DEFAULT_IMAGE_EXPIRE = 60;

    /**
     * 默认保存code的前缀
     */
    String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY";

}
