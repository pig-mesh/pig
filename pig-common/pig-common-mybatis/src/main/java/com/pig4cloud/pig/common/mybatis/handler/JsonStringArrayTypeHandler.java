package com.pig4cloud.pig.common.mybatis.handler;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * MyBatis 字符串数组类型处理器，用于数据库VARCHAR类型与Java字符串数组的相互转换
 *
 * @author lengleng
 * @date 2025/05/31
 * @see MappedTypes 指定处理的Java类型
 * @see MappedJdbcTypes 指定处理的JDBC类型
 */
@MappedTypes(value = { String[].class })
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class JsonStringArrayTypeHandler extends BaseTypeHandler<String[]> {

	/**
	 * 设置非空参数到PreparedStatement
	 * @param ps PreparedStatement对象
	 * @param i 参数位置
	 * @param parameter 字符串数组参数
	 * @param jdbcType JDBC类型
	 * @throws SQLException 数据库操作异常
	 */
	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, ArrayUtil.join(parameter, StrUtil.COMMA));
	}

	/**
	 * 从ResultSet中获取指定列名的字符串数组结果，允许为null
	 * @param rs 结果集
	 * @param columnName 列名
	 * @return 转换后的字符串数组，可能为null
	 * @throws SQLException 数据库访问错误时抛出
	 */
	@Override
	@SneakyThrows
	public String[] getNullableResult(ResultSet rs, String columnName) {
		String reString = rs.getString(columnName);
		return Convert.toStrArray(reString);
	}

	/**
	 * 从ResultSet中获取指定列的可空字符串数组结果
	 * @param rs 结果集
	 * @param columnIndex 列索引
	 * @return 转换后的字符串数组，可能为null
	 * @throws SQLException 数据库访问错误时抛出
	 */
	@Override
	@SneakyThrows
	public String[] getNullableResult(ResultSet rs, int columnIndex) {
		String reString = rs.getString(columnIndex);
		return Convert.toStrArray(reString);
	}

	/**
	 * 从CallableStatement中获取指定列的可空字符串结果并转换为字符串数组
	 * @param cs CallableStatement对象
	 * @param columnIndex 列索引
	 * @return 转换后的字符串数组
	 * @throws Exception 如果操作过程中发生错误
	 */
	@Override
	@SneakyThrows
	public String[] getNullableResult(CallableStatement cs, int columnIndex) {
		String reString = cs.getString(columnIndex);
		return Convert.toStrArray(reString);
	}

}
