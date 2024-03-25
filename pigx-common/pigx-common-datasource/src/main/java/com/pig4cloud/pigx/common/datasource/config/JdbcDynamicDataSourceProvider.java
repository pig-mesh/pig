/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.common.datasource.config;

import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.creator.druid.DruidConfig;
import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.pig4cloud.pigx.common.datasource.support.DataSourceConstants;
import com.pig4cloud.pigx.common.datasource.util.DsConfTypeEnum;
import com.pig4cloud.pigx.common.datasource.util.DsJdbcUrlEnum;
import org.jasypt.encryption.StringEncryptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2020/2/6
 * <p>
 * 从数据源中获取 配置信息
 */
public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {
    private static final String SQL_LOG_FILTER = "sqlLogFilter";

    private final DruidDataSourceProperties properties;

    private final StringEncryptor stringEncryptor;

    public JdbcDynamicDataSourceProvider(DefaultDataSourceCreator defaultDataSourceCreator,
                                         StringEncryptor stringEncryptor, DruidDataSourceProperties properties) {
        super(defaultDataSourceCreator, properties.getDriverClassName(), properties.getUrl(), properties.getUsername(),
                properties.getPassword());
        this.stringEncryptor = stringEncryptor;
        this.properties = properties;
    }

    /**
     * 执行语句获得数据源参数
     *
     * @param statement 语句
     * @return 数据源参数
     * @throws SQLException sql异常
     */
    @Override
    protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery(properties.getQueryDsSql());

        Map<String, DataSourceProperty> map = new HashMap<>(8);
        while (rs.next()) {
            String name = rs.getString(DataSourceConstants.NAME);
            String username = rs.getString(DataSourceConstants.DS_USER_NAME);
            String password = rs.getString(DataSourceConstants.DS_USER_PWD);
            Integer confType = rs.getInt(DataSourceConstants.DS_CONFIG_TYPE);
            String dsType = rs.getString(DataSourceConstants.DS_TYPE);

            DataSourceProperty property = new DataSourceProperty();
            property.setUsername(username);
            property.setPassword(stringEncryptor.decrypt(password));

            String url;
            // JDBC 配置形式
            DsJdbcUrlEnum urlEnum = DsJdbcUrlEnum.get(dsType);
            if (DsConfTypeEnum.JDBC.getType().equals(confType)) {
                url = rs.getString(DataSourceConstants.DS_JDBC_URL);
            } else {
                String host = rs.getString(DataSourceConstants.DS_HOST);
                String port = rs.getString(DataSourceConstants.DS_PORT);
                String dsName = rs.getString(DataSourceConstants.DS_NAME);
                url = String.format(urlEnum.getUrl(), host, port, dsName);
            }

            // Druid Config
            DruidConfig druidConfig = new DruidConfig();
            druidConfig.setProxyFilters(SQL_LOG_FILTER);
            druidConfig.setValidationQuery(urlEnum.getValidationQuery());
            property.setDruid(druidConfig);
            property.setUrl(url);

            map.put(name, property);
        }

        // 添加默认主数据源
        DataSourceProperty property = new DataSourceProperty();
        property.setUsername(properties.getUsername());
        property.setPassword(properties.getPassword());
        property.setUrl(properties.getUrl());

        DruidConfig druidConfig = new DruidConfig();
        druidConfig.setProxyFilters(SQL_LOG_FILTER);
        property.setDruid(druidConfig);
        map.put(DataSourceConstants.DS_MASTER, property);
        return map;
    }

}
