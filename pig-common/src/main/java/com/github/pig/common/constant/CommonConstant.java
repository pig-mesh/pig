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
     * 菜单
     */
    String MENU = "0";

    /**
     * 按钮
     */
    String BUTTON = "1";
    /**
     * log rabbit队列名称
     */
    String LOG_QUEUE = "log";

    /**
     * 删除标记
     */
    String DEL_FLAG = "del_flag";
}
