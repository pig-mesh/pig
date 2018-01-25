package com.github.pig.monitor.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2018/1/25
 * 监控短信接收者列表
 */
@Configuration
@ConditionalOnExpression("!'${mobiles}'.isEmpty()")
@ConfigurationProperties(prefix = "mobiles")
public class MonitorMobilePropertiesConfig {
    private List<String> users = new ArrayList<>();

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
