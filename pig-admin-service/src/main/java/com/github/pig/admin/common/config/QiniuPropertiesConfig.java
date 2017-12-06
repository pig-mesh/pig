package com.github.pig.admin.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author lengleng
 * @date 2017/12/6
 * 七牛参数
 */
@Configuration
@ConfigurationProperties(prefix = "qiniu")
public class QiniuPropertiesConfig {
    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.host}")
    private String qiniuHost;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getQiniuHost() {
        return qiniuHost;
    }

    public void setQiniuHost(String qiniuHost) {
        this.qiniuHost = qiniuHost;
    }
}
