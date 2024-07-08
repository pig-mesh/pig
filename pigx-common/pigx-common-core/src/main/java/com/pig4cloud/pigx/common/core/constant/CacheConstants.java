package com.pig4cloud.pigx.common.core.constant;

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
    String MENU_DETAILS = "menu_details";

    /**
     * 用户信息缓存
     */
    String USER_DETAILS = "user_details";

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
    String CLIENT_DETAILS_KEY = "pigx_oauth:client:details";

    /**
     * spring boot admin 事件key
     */
    String EVENT_KEY = GLOBALLY + "event_key";

    /**
     * 路由存放
     */
    String ROUTE_KEY = GLOBALLY + "gateway_route_key";

    /**
     * 内存reload 时间
     */
    String ROUTE_JVM_RELOAD_TOPIC = "gateway_jvm_route_reload_topic";

    /**
     * redis 重新加载 路由信息
     */
    String ROUTE_REDIS_RELOAD_TOPIC = "upms_redis_route_reload_topic";

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
     * 支付 reload 事件
     */
    String PAY_REDIS_RELOAD_TOPIC = "pay_redis_reload_topic";

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

    /**
     * oauth 缓存前缀
     */
    String PROJECT_OAUTH_ACCESS = "token::access_token";

}
