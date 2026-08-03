package com.pig4cloud.pig.monitor.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Druid SQL 监控详情响应。
 * <p>
 * JSON 字段名与 Druid 监控接口保持一致。
 *
 * @author linchtech
 * @date 2020-09-16 16:18
 **/
@NoArgsConstructor
@Data
@Schema(description = "Druid SQL 监控详情响应")
public class SqlDetailResult {

	@Schema(description = "Druid 原始字段：ResultCode")
	private int ResultCode;

	@Schema(description = "Druid 原始字段：Content")
	private ContentBean Content;

	/** Druid SQL 监控详情。 */
	@NoArgsConstructor
	@Data
	@Schema(description = "Druid SQL 监控详情")
	public static class ContentBean {

		@Schema(description = "Druid 原始字段：ExecuteAndResultSetHoldTime")
		private int ExecuteAndResultSetHoldTime;

		@Schema(description = "Druid 原始字段：LastErrorMessage")
		private Object LastErrorMessage;

		@Schema(description = "Druid 原始字段：InputStreamOpenCount")
		private int InputStreamOpenCount;

		@Schema(description = "Druid 原始字段：BatchSizeTotal")
		private int BatchSizeTotal;

		@Schema(description = "Druid 原始字段：FetchRowCountMax")
		private int FetchRowCountMax;

		@Schema(description = "Druid 原始字段：ErrorCount")
		private int ErrorCount;

		@Schema(description = "Druid 原始字段：BatchSizeMax")
		private int BatchSizeMax;

		@Schema(description = "Druid 原始字段：URL")
		private Object URL;

		@Schema(description = "Druid 原始字段：Name")
		private Object Name;

		@Schema(description = "Druid 原始字段：LastErrorTime")
		private Object LastErrorTime;

		@Schema(description = "Druid 原始字段：ReaderOpenCount")
		private int ReaderOpenCount;

		@Schema(description = "Druid 原始字段：parsedRelationships")
		private String parsedRelationships;

		@Schema(description = "Druid 原始字段：EffectedRowCountMax")
		private int EffectedRowCountMax;

		@Schema(description = "Druid 原始字段：LastErrorClass")
		private Object LastErrorClass;

		@Schema(description = "Druid 原始字段：InTransactionCount")
		private int InTransactionCount;

		@Schema(description = "Druid 原始字段：LastErrorStackTrace")
		private Object LastErrorStackTrace;

		@Schema(description = "Druid 原始字段：ResultSetHoldTime")
		private int ResultSetHoldTime;

		@Schema(description = "Druid 原始字段：TotalTime")
		private int TotalTime;

		@Schema(description = "Druid 原始字段：ID")
		private int ID;

		@Schema(description = "Druid 原始字段：ConcurrentMax")
		private int ConcurrentMax;

		@Schema(description = "Druid 原始字段：RunningCount")
		private int RunningCount;

		@Schema(description = "Druid 原始字段：FetchRowCount")
		private int FetchRowCount;

		@Schema(description = "Druid 原始字段：parsedFields")
		private String parsedFields;

		@Schema(description = "Druid 原始字段：MaxTimespanOccurTime")
		private String MaxTimespanOccurTime;

		@Schema(description = "Druid 原始字段：LastSlowParameters")
		private Object LastSlowParameters;

		@Schema(description = "Druid 原始字段：ReadBytesLength")
		private int ReadBytesLength;

		@Schema(description = "Druid 原始字段：formattedSql")
		private String formattedSql;

		@Schema(description = "Druid 原始字段：DbType")
		private String DbType;

		@Schema(description = "Druid 原始字段：DataSource")
		private Object DataSource;

		@Schema(description = "Druid 原始字段：SQL")
		private String SQL;

		@Schema(description = "Druid 原始字段：HASH")
		private long HASH;

		@Schema(description = "Druid 原始字段：LastError")
		private Object LastError;

		@Schema(description = "Druid 原始字段：MaxTimespan")
		private int MaxTimespan;

		@Schema(description = "Druid 原始字段：parsedTable")
		private String parsedTable;

		@Schema(description = "Druid 原始字段：parsedOrderbycolumns")
		private String parsedOrderbycolumns;

		@Schema(description = "Druid 原始字段：BlobOpenCount")
		private int BlobOpenCount;

		@Schema(description = "Druid 原始字段：ExecuteCount")
		private int ExecuteCount;

		@Schema(description = "Druid 原始字段：EffectedRowCount")
		private int EffectedRowCount;

		@Schema(description = "Druid 原始字段：ReadStringLength")
		private int ReadStringLength;

		@Schema(description = "Druid 原始字段：File")
		private Object File;

		@Schema(description = "Druid 原始字段：ClobOpenCount")
		private int ClobOpenCount;

		@Schema(description = "Druid 原始字段：LastTime")
		private String LastTime;

		@Schema(description = "Druid 原始字段：parsedConditions")
		private String parsedConditions;

		@Schema(description = "Druid 原始字段：EffectedRowCountHistogram")
		private List<Integer> EffectedRowCountHistogram;

		@Schema(description = "Druid 原始字段：Histogram")
		private List<Integer> Histogram;

		@Schema(description = "Druid 原始字段：ExecuteAndResultHoldTimeHistogram")
		private List<Integer> ExecuteAndResultHoldTimeHistogram;

		@Schema(description = "Druid 原始字段：FetchRowCountHistogram")
		private List<Integer> FetchRowCountHistogram;

	}

}
