package com.github.pig.auth.component.social.qq.connect;

import com.github.pig.auth.component.social.qq.api.QQ;
import com.github.pig.auth.component.social.qq.api.impl.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * http://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token
 * Created on 2018/1/8 0008.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    /**
     * 获取code
     */
    private static final String QQ_URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
    /**
     * 获取access_token 也就是令牌
     */
    private static final String QQ_URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";
    private String appId;

    public QQServiceProvider(String appId, String appSecret) {
        super(new QQOAuth2Template(appId, appSecret, QQ_URL_AUTHORIZE, QQ_URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {

        return new QQImpl(accessToken, appId);
    }
}
