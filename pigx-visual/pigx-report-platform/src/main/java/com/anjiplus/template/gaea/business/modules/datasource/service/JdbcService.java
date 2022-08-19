package com.anjiplus.template.gaea.business.modules.datasource.service;

import com.anjiplus.template.gaea.business.modules.datasource.controller.dto.DataSourceDto;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by raodeming on 2021/8/6.
 */
public interface JdbcService {

    /**
     * 删除数据库连接池
     *
     * @param id
     */
    void removeJdbcConnectionPool(Long id);


    /**
     * 获取连接
     *
     * @param dataSource
     * @return
     * @throws SQLException
     */
    Connection getPooledConnection(DataSourceDto dataSource) throws SQLException;

    /**
     * 测试数据库连接  获取一个连接
     *
     * @param dataSource
     * @return
     * @throws ClassNotFoundException driverName不正确
     * @throws SQLException
     */
    Connection getUnPooledConnection(DataSourceDto dataSource) throws SQLException;
}
