package com.github.pig.common.constant;

/**
 * @author lengleng
 * @date 2017/10/29
 */
public interface CommonConstant {
    /**
     * token请求头名称
     */
    String REQ_HEADER = "Authorization";

    /**
     * token分割符
     */
    String TOKEN_SPLIT = "Bearer ";

    /**
     * jwt签名
     */
    String SIGN_KEY = "PIG";
    /**
     * 删除
     */
    String STATUS_DEL = "1";
    /**
     * 正常
     */
    String STATUS_NORMAL = "0";

    /**
     * 锁定
     */
    String STATUS_LOCK = "9";

    /**
     * 菜单
     */
    String MENU = "0";

    /**
     * 按钮
     */
    String BUTTON = "1";

    /**
     * 删除标记
     */
    String DEL_FLAG = "del_flag";

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * JSON 资源
     */
    String CONTENT_TYPE = "application/json; charset=utf-8";

    /**
     * 阿里大鱼
     */
    String ALIYUN_SMS = "aliyun_sms";
}
