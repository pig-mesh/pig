package com.pig4cloud.pigx.common.api.encrypt.config;

import com.pig4cloud.pigx.common.api.encrypt.annotation.NoEncrypt;
import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * api 签名配置类
 *
 * @author licoy.cn
 * @author L.cm
 * @version 2018/9/6
 */
@Getter
@Setter
@ConfigurationProperties(ApiEncryptProperties.PREFIX)
public class ApiEncryptProperties implements InitializingBean {

    /**
     * 前缀
     */
    public static final String PREFIX = "security.api.encrypt";

    /**
     * 是否开启 api 签名
     */
    private boolean enable;

    /**
     * body 内容 json key, 默认：encryption
     */
    private String bodyJsonKey = "encryption";

    /**
     * 默认加密类型
     */
    private EncryptType defaultEncryptType = EncryptType.AES;

    /**
     * aes 密钥
     */
    private String aesKey;

    /**
     * des 密钥
     */
    private String desKey;

    /**
     * sm4 密钥
     */
    private String sm4Key;

    /**
     * rsa 私钥
     */
    private String rsaPrivateKey;


    /**
     * 跳过 URL
     */
    private List<String> skipUrl = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        RequestMappingHandlerMapping mapping = SpringContextHolder.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);

            // 获取方法上边的注解 替代path variable 为 *
            NoEncrypt method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), NoEncrypt.class);
            Optional.ofNullable(method)
                    .ifPresent(inner -> skipUrl.addAll(Objects.requireNonNull(info.getPathPatternsCondition())
                            .getPatternValues()));
        });
    }
}
