package com.pig4cloud.pigx.common.sequence.range.impl.db;

import cn.hutool.core.util.RandomUtil;
import com.pig4cloud.pigx.common.sequence.exception.SeqException;

import javax.sql.DataSource;
import java.sql.*;

/**
 * 操作DB帮助类
 *
 * @author xuan on 2018/4/29.
 */
abstract class BaseDbHelper {

	private static final long DELTA = 100000000L;

	private final static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS #tableName("
			+ "id bigint(20) NOT NULL AUTO_INCREMENT," + "value bigint(20) NOT NULL," + "name varchar(32) NOT NULL,"
			+ "gmt_create DATETIME NOT NULL," + "gmt_modified DATETIME NOT NULL,"
			+ "PRIMARY KEY (`id`),UNIQUE uk_name (`name`)" + ")";

	private final static String SQL_INSERT_RANGE = "INSERT IGNORE INTO #tableName(id,name,value,gmt_create,gmt_modified)"
			+ " VALUE(?,?,?,?,?)";

	private final static String SQL_UPDATE_RANGE = "UPDATE #tableName SET value=?,gmt_modified=? WHERE name=? AND "
			+ "value=?";

	private final static String SQL_SELECT_RANGE = "SELECT value FROM #tableName WHERE name=?";

	/**
	 * 创建表
	 * @param dataSource DB来源
	 * @param tableName 表名
	 */
	static void createTable(DataSource dataSource, String tableName) {

		Connection conn = null;
		Statement stmt = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.createStatement();
			stmt.executeUpdate(SQL_CREATE_TABLE.replace("#tableName", tableName));
		}
		catch (SQLException e) {
			throw new SeqException(e);
		}
		finally {
			closeQuietly(stmt);
			closeQuietly(conn);
		}
	}

	/**
	 * 插入数据区间
	 * @param dataSource DB来源
	 * @param tableName 表名
	 * @param name 区间名称
	 * @param stepStart 初始位置
	 */
	private static void insertRange(DataSource dataSource, String tableName, String name, long stepStart) {

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_INSERT_RANGE.replace("#tableName", tableName));
			stmt.setLong(1, RandomUtil.randomLong());
			stmt.setString(2, name);
			stmt.setLong(3, stepStart);
			stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			stmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			stmt.executeUpdate();
		}
		catch (SQLException e) {
			throw new SeqException(e);
		}
		finally {
			closeQuietly(stmt);
			closeQuietly(conn);
		}
	}

	/**
	 * 更新区间，乐观策略
	 * @param dataSource DB来源
	 * @param tableName 表名
	 * @param newValue 更新新数据
	 * @param oldValue 更新旧数据
	 * @param name 区间名称
	 * @return 成功/失败
	 */
	static boolean updateRange(DataSource dataSource, String tableName, Long newValue, Long oldValue, String name) {

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_UPDATE_RANGE.replace("#tableName", tableName));
			stmt.setLong(1, newValue);
			stmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
			stmt.setString(3, name);
			stmt.setLong(4, oldValue);
			int affectedRows = stmt.executeUpdate();
			return affectedRows > 0;
		}
		catch (SQLException e) {
			throw new SeqException(e);
		}
		finally {
			closeQuietly(stmt);
			closeQuietly(conn);
		}
	}

	/**
	 * 查询区间，如果区间不存在，会新增一个区间，并返回null，由上层重新执行
	 * @param dataSource DB来源
	 * @param tableName 来源
	 * @param name 区间名称
	 * @param stepStart 初始位置
	 * @return 区间值
	 */
	static Long selectRange(DataSource dataSource, String tableName, String name, long stepStart) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		long oldValue;

		try {
			conn = dataSource.getConnection();
			stmt = conn.prepareStatement(SQL_SELECT_RANGE.replace("#tableName", tableName));
			stmt.setString(1, name);

			rs = stmt.executeQuery();
			if (!rs.next()) {
				// 没有此类型数据，需要初始化
				insertRange(dataSource, tableName, name, stepStart);
				return null;
			}
			oldValue = rs.getLong(1);

			if (oldValue < 0) {
				String msg = "Sequence value cannot be less than zero, value = " + oldValue
						+ ", please check table sequence" + tableName;
				throw new SeqException(msg);
			}

			if (oldValue > Long.MAX_VALUE - DELTA) {
				String msg = "Sequence value overflow, value = " + oldValue + ", please check table sequence"
						+ tableName;
				throw new SeqException(msg);
			}

			return oldValue;
		}
		catch (SQLException e) {
			throw new SeqException(e);
		}
		finally {
			closeQuietly(rs);
			closeQuietly(stmt);
			closeQuietly(conn);
		}
	}

	private static void closeQuietly(AutoCloseable closeable) {
		if (null != closeable) {
			try {
				closeable.close();
			}
			catch (Throwable e) {
				// Ignore
			}
		}
	}

}
