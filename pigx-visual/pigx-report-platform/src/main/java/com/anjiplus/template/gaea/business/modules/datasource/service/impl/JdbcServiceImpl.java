package com.anjiplus.template.gaea.business.modules.datasource.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.anjiplus.template.gaea.business.config.DruidProperties;
import com.anjiplus.template.gaea.business.modules.datasource.controller.dto.DataSourceDto;
import com.anjiplus.template.gaea.business.modules.datasource.service.JdbcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by raodeming on 2021/8/6.
 */
@Service
@Slf4j
public class JdbcServiceImpl implements JdbcService {

	@Autowired
	private DruidProperties druidProperties;

	/**
	 * 所有数据源的连接池存在map里
	 */
	private Map<Long, DruidDataSource> map = new ConcurrentHashMap<>();

	private Object lock = new Object();

	public DruidDataSource getJdbcConnectionPool(DataSourceDto dataSource) {
		if (map.containsKey(dataSource.getId())) {
			return map.get(dataSource.getId());
		}
		else {
			try {
				synchronized (lock) {
					if (!map.containsKey(dataSource.getId())) {
						DruidDataSource pool = druidProperties.dataSource(dataSource.getJdbcUrl(),
								dataSource.getUsername(), dataSource.getPassword(), dataSource.getDriverName());
						map.put(dataSource.getId(), pool);
						log.info("创建连接池成功：{}", dataSource.getJdbcUrl());
					}
				}
				return map.get(dataSource.getId());
			}
			finally {
			}
		}
	}

	/**
	 * 删除数据库连接池
	 * @param id
	 */
	@Override
	public void removeJdbcConnectionPool(Long id) {
		try {
			DruidDataSource pool = map.get(id);
			if (pool != null) {
				log.info("remove pool success, datasourceId:{}", id);
				map.remove(id);
			}
		}
		catch (Exception e) {
			log.error("error", e);
		}
		finally {
		}
	}

	/**
	 * 获取连接
	 * @param dataSource
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Connection getPooledConnection(DataSourceDto dataSource) throws SQLException {
		DruidDataSource pool = getJdbcConnectionPool(dataSource);

		try {
			return pool.getConnection();
		}
		catch (Exception ex) {
			log.warn("数据库链接失败 {}", dataSource.getId());
			return null;
		}
	}

	/**
	 * 测试数据库连接 获取一个连接
	 * @param dataSource
	 * @return
	 * @throws ClassNotFoundException driverName不正确
	 * @throws SQLException
	 */
	@Override
	public Connection getUnPooledConnection(DataSourceDto dataSource) throws SQLException {
		DruidDataSource druidDataSource = druidProperties.dataSource(dataSource.getJdbcUrl(), dataSource.getUsername(),
				dataSource.getPassword(), dataSource.getDriverName());
		return druidDataSource.getConnection();
	}

}
