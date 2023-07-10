/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pigx.common.data.mybatis;

import com.alibaba.druid.DbType;
import com.alibaba.druid.filter.FilterChain;
import com.alibaba.druid.filter.FilterEventAdapter;
import com.alibaba.druid.proxy.jdbc.JdbcParameter;
import com.alibaba.druid.proxy.jdbc.ResultSetProxy;
import com.alibaba.druid.proxy.jdbc.StatementProxy;
import com.alibaba.druid.spring.boot3.autoconfigure.DruidDataSourceAutoConfigure;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Import;

import java.sql.SQLException;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;

/**
 * 打印可执行的 sql 日志
 *
 * <p>
 * 参考：https://jfinal.com/share/2204
 * </p>
 *
 * @author L.cm
 */
@Slf4j
@RequiredArgsConstructor
@Import(DruidDataSourceAutoConfigure.class)
public class DruidSqlLogFilter extends FilterEventAdapter {

	private static final SQLUtils.FormatOption FORMAT_OPTION = new SQLUtils.FormatOption(false, false);

	private final PigxMybatisProperties properties;

	@Override
	protected void statementExecuteBefore(StatementProxy statement, String sql) {
		statement.setLastExecuteStartNano();
	}

	@Override
	protected void statementExecuteBatchBefore(StatementProxy statement) {
		statement.setLastExecuteStartNano();
	}

	@Override
	protected void statementExecuteUpdateBefore(StatementProxy statement, String sql) {
		statement.setLastExecuteStartNano();
	}

	@Override
	protected void statementExecuteQueryBefore(StatementProxy statement, String sql) {
		statement.setLastExecuteStartNano();
	}

	@Override
	protected void statementExecuteAfter(StatementProxy statement, String sql, boolean firstResult) {
		statement.setLastExecuteTimeNano();
	}

	@Override
	protected void statementExecuteBatchAfter(StatementProxy statement, int[] result) {
		statement.setLastExecuteTimeNano();
	}

	@Override
	protected void statementExecuteQueryAfter(StatementProxy statement, String sql, ResultSetProxy resultSet) {
		statement.setLastExecuteTimeNano();
	}

	@Override
	protected void statementExecuteUpdateAfter(StatementProxy statement, String sql, int updateCount) {
		statement.setLastExecuteTimeNano();
	}

	@Override
	public void statement_close(FilterChain chain, StatementProxy statement) throws SQLException {
		// 先调用父类关闭 statement
		super.statement_close(chain, statement);
		// 支持动态开启
		if (!properties.isShowSql()) {
			return;
		}

		// 是否开启调试
		if (!log.isInfoEnabled()) {
			return;
		}

		// 打印可执行的 sql
		String sql = statement.getBatchSql();
		// sql 为空直接返回
		if (StringUtils.isEmpty(sql)) {
			return;
		}
		int parametersSize = statement.getParametersSize();
		List<Object> parameters = new ArrayList<>(parametersSize);
		for (int i = 0; i < parametersSize; ++i) {
			// 转换参数，处理 java8 时间
			parameters.add(getJdbcParameter(statement.getParameter(i)));
		}
		String dbType = statement.getConnectionProxy().getDirectDataSource().getDbType();
		String formattedSql = SQLUtils.format(sql, DbType.of(dbType), parameters, FORMAT_OPTION);
		printSql(formattedSql, statement);
	}

	private static Object getJdbcParameter(JdbcParameter jdbcParam) {
		if (jdbcParam == null) {
			return null;
		}
		Object value = jdbcParam.getValue();
		// 处理 java8 时间
		if (value instanceof TemporalAccessor) {
			return value.toString();
		}
		return value;
	}

	private static void printSql(String sql, StatementProxy statement) {
		// 打印 sql
		String sqlLogger = "\n\n======= Sql Logger ======================" + "\n{}"
				+ "\n======= Sql Execute Time: {} =======\n";
		log.info(sqlLogger, sql.trim(), format(statement.getLastExecuteTimeNano()));
	}

	/**
	 * 格式化执行时间，单位为 ms 和 s，保留三位小数
	 * @param nanos 纳秒
	 * @return 格式化后的时间
	 */
	private static String format(long nanos) {
		if (nanos < 1) {
			return "0ms";
		}
		double millis = (double) nanos / (1000 * 1000);
		// 不够 1 ms，最小单位为 ms
		if (millis > 1000) {
			return String.format("%.3fs", millis / 1000);
		}
		else {
			return String.format("%.3fms", millis);
		}
	}

}
