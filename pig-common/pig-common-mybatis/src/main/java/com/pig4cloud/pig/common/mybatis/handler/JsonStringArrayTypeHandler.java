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
 * Mybatis数组,符串互转
 * <p>
 * MappedJdbcTypes 数据库中的数据类型 MappedTypes java中的的数据类型
 *
 * @author xuzihui
 * @date 2019-11-20
 */
@MappedTypes(value = { String[].class })
@MappedJdbcTypes(value = JdbcType.VARCHAR)
public class JsonStringArrayTypeHandler extends BaseTypeHandler<String[]> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, ArrayUtil.join(parameter, StrUtil.COMMA));
	}

	@Override
	@SneakyThrows
	public String[] getNullableResult(ResultSet rs, String columnName) {
		String reString = rs.getString(columnName);
		return Convert.toStrArray(reString);
	}

	@Override
	@SneakyThrows
	public String[] getNullableResult(ResultSet rs, int columnIndex) {
		String reString = rs.getString(columnIndex);
		return Convert.toStrArray(reString);
	}

	@Override
	@SneakyThrows
	public String[] getNullableResult(CallableStatement cs, int columnIndex) {
		String reString = cs.getString(columnIndex);
		return Convert.toStrArray(reString);
	}

}
