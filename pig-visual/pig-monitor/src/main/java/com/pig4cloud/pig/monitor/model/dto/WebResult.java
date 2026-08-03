package com.pig4cloud.pig.monitor.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Druid Web URI 统计响应。
 * <p>
 * JSON 字段名与 Druid 监控接口保持一致。
 *
 * @author linchtech
 * @date 2020-09-17 18:27
 **/
@NoArgsConstructor
@Data
@Schema(description = "Druid Web URI 统计响应")
public class WebResult {

	@Schema(description = "Druid 原始字段：ResultCode")
	private int ResultCode;

	@Schema(description = "Druid 原始字段：Content")
	private List<ContentBean> Content;

	/** Druid Web URI 统计明细。 */
	@NoArgsConstructor
	@Data
	@Schema(description = "Druid Web URI 统计明细")
	public static class ContentBean {

		@Schema(description = "Druid 原始字段：URI")
		private String URI;

		@Schema(description = "Druid 原始字段：RunningCount")
		private int RunningCount;

		@Schema(description = "Druid 原始字段：ConcurrentMax")
		private int ConcurrentMax;

		@Schema(description = "Druid 原始字段：RequestCount")
		private int RequestCount;

		@Schema(description = "Druid 原始字段：RequestTimeMillis")
		private int RequestTimeMillis;

		@Schema(description = "Druid 原始字段：ErrorCount")
		private int ErrorCount;

		@Schema(description = "Druid 原始字段：LastAccessTime")
		private String LastAccessTime;

		@Schema(description = "Druid 原始字段：JdbcCommitCount")
		private int JdbcCommitCount;

		@Schema(description = "Druid 原始字段：JdbcRollbackCount")
		private int JdbcRollbackCount;

		@Schema(description = "Druid 原始字段：JdbcExecuteCount")
		private int JdbcExecuteCount;

		@Schema(description = "Druid 原始字段：JdbcExecuteErrorCount")
		private int JdbcExecuteErrorCount;

		@Schema(description = "Druid 原始字段：JdbcExecutePeak")
		private int JdbcExecutePeak;

		@Schema(description = "Druid 原始字段：JdbcExecuteTimeMillis")
		private int JdbcExecuteTimeMillis;

		@Schema(description = "Druid 原始字段：JdbcFetchRowCount")
		private int JdbcFetchRowCount;

		@Schema(description = "Druid 原始字段：JdbcFetchRowPeak")
		private int JdbcFetchRowPeak;

		@Schema(description = "Druid 原始字段：JdbcUpdateCount")
		private int JdbcUpdateCount;

		@Schema(description = "Druid 原始字段：JdbcUpdatePeak")
		private int JdbcUpdatePeak;

		@Schema(description = "Druid 原始字段：JdbcPoolConnectionOpenCount")
		private int JdbcPoolConnectionOpenCount;

		@Schema(description = "Druid 原始字段：JdbcPoolConnectionCloseCount")
		private int JdbcPoolConnectionCloseCount;

		@Schema(description = "Druid 原始字段：JdbcResultSetOpenCount")
		private int JdbcResultSetOpenCount;

		@Schema(description = "Druid 原始字段：JdbcResultSetCloseCount")
		private int JdbcResultSetCloseCount;

		@Schema(description = "Druid 原始字段：RequestTimeMillisMax")
		private int RequestTimeMillisMax;

		@Schema(description = "Druid 原始字段：RequestTimeMillisMaxOccurTime")
		private String RequestTimeMillisMaxOccurTime;

		@Schema(description = "Druid 原始字段：Histogram")
		private List<Integer> Histogram;

		@Schema(description = "Druid 原始字段：Profiles")
		private List<?> Profiles;

	}

}
