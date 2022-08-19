package com.anjiplus.template.gaea.business.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by raodeming on 2021/7/5.
 */
@Slf4j
@Component
@AllArgsConstructor
@ConditionalOnProperty(value = {"spring.flyway.enabled"})
public class DatabaseInitializer {

    private final FlywayProperties flywayProperties;
    private final DataSourceProperties dataSourceProperties;

    @PostConstruct
    public void init() throws SQLException {
        log.info("DatabaseInitializer uses flyway init-sqls to initiate database");
        String url = dataSourceProperties.getUrl();
        // jdbc url最后一个 '/' 用于分割具体 schema?参数
        int lastSplitIndex = url.lastIndexOf('?');
        // 获取spring.datasource.url具体数据库schema前的jdbc url
        String addressUrl = url.substring(0, lastSplitIndex);
        String addresslast = url.substring(lastSplitIndex);
        addressUrl = addressUrl.substring(0, addressUrl.lastIndexOf("/"));
        // 直连数据库地址:jdbc:mysql://yourIp:port
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(addressUrl.concat(addresslast));
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        for (String sql : flywayProperties.getInitSqls()) {
            // 通过flyway的init-sqls配置进行建库与数据库配置
            // executeUpdate:执行给定的SQL语句，该语句可以是INSERT，UPDATE或DELETE语句或不返回任何内容的SQL语句，例如SQL DDL语句。
            statement.executeUpdate(sql);
        }
        statement.close();
        connection.close();
        dataSource.close();
        log.info("DatabaseInitializer initialize completed");
    }

}
