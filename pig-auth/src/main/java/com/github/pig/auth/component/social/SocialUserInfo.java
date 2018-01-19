package com.github.pig.auth.component.social;

import lombok.Data;

/**
 * Created on 2018/1/8 0008.
 *
 * @author zlf
 * @email i@merryyou.cn
 * @since 1.0
 */
@Data
public class SocialUserInfo {

    private String providerId;

    private String providerUserId;

    private String nickname;

    private String headImg;

}
