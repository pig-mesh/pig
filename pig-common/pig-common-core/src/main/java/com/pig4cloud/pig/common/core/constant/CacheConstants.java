package com.pig4cloud.pig.common.core.constant;

/**
 * @author lengleng
 * @date 2019-04-28
 * <p>
 * 缓存的key 常量
 */
public interface CacheConstants {

    /**
     * 全局缓存，在缓存名称上加上该前缀表示该缓存不区分租户，比如:
     * <p/>
     * {@code @Cacheable(value = CacheConstants.GLOBALLY+CacheConstants.MENU_DETAILS, key = "#roleId  + '_menu'", unless = "#result == null")}
     */
    String GLOBALLY = "gl:";

    /**
     * 验证码前缀
     */
    String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY:";

    /**
     * 菜单信息缓存
     */
    String MENU_DETAILS = GLOBALLY + "menu_details";

    /**
     * 用户信息缓存
     */
    String USER_DETAILS = GLOBALLY + "user_details";

    /**
     * 移动端用户信息缓存
     */
    String USER_DETAILS_MINI = "user_details_mini";

    /**
     * 角色信息缓存
     */
    String ROLE_DETAILS = "role_details";

    /**
     * 字典信息缓存
     */
    String DICT_DETAILS = "dict_details";

    /**
     * oauth 客户端信息
     */
    String CLIENT_DETAILS_KEY = "pig_oauth:client:details";

    /**
     * redis 重新加载客户端信息
     */
    String CLIENT_REDIS_RELOAD_TOPIC = "upms_redis_client_reload_topic";

    /**
     * 敏感词重新加载
     */
    String SENSITIVE_REDIS_RELOAD_TOPIC = "sensitive_client_reload_topic";

    /**
     * 公众号 reload
     */
    String MP_REDIS_RELOAD_TOPIC = "mp_redis_reload_topic";

    /**
     * 参数缓存
     */
    String PARAMS_DETAILS = "params_details";

    /**
     * 租户缓存 (不区分租户)
     */
    String TENANT_DETAILS = GLOBALLY + "tenant_details";

    /**
     * i18n缓存 (不区分租户)
     */
    String I18N_DETAILS = GLOBALLY + "i18n_details";

    /**
     * 客户端配置缓存
     */
    String CLIENT_FLAG = "client_config_flag";

    /**
     * 登录错误次数
     */
    String LOGIN_ERROR_TIMES = "login_error_times";

}
