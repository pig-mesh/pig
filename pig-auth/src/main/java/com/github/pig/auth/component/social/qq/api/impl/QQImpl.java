package com.github.pig.auth.component.social.qq.api.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pig.auth.component.social.qq.api.QQ;
import com.github.pig.auth.component.social.qq.api.QQUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * Created on 2018/1/8 0008.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
@Slf4j
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    private static final String QQ_URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
    private static final String QQ_URL_GET_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";
    /**
     * appId 配置文件读取
     */
    private String appId;
    /**
     * openId 请求QQ_URL_GET_OPENID返回
     */
    private String openId;
    /**
     * 工具类
     */
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 构造方法获取openId
     */
    public QQImpl(String accessToken, String appId) {
        //access_token作为查询参数来携带。
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);

        this.appId = appId;

        String url = String.format(QQ_URL_GET_OPENID, accessToken);
        String result = getRestTemplate().getForObject(url, String.class);

        log.info("【QQImpl】 QQ_URL_GET_OPENID={} result={}", QQ_URL_GET_OPENID, result);

        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
    }

    @Override
    public QQUserInfo getUserInfo() {
        String url = String.format(QQ_URL_GET_USER_INFO, appId, openId);
        String result = getRestTemplate().getForObject(url, String.class);

        log.info("【QQImpl】 QQ_URL_GET_USER_INFO={} result={}", QQ_URL_GET_USER_INFO, result);

        QQUserInfo userInfo = null;
        try {
            userInfo = objectMapper.readValue(result, QQUserInfo.class);
            userInfo.setOpenId(openId);
            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }
}
