package com.pig4cloud.pig.monitor.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Druid 数据源统计响应。
 * <p>
 * JSON 字段名与 Druid 监控接口保持一致。
 *
 * @author linchtech
 * @date 2020-09-16 18:32
 **/
@Data
@NoArgsConstructor
@Schema(description = "Druid 数据源统计响应")
public class DataSourceResult {

	@Schema(description = "Druid 原始字段：ResultCode")
	private int ResultCode;

	@Schema(description = "Druid 原始字段：Content")
	private List<ContentBean> Content;

	/** Druid 数据源统计明细。 */
	@NoArgsConstructor
	@Data
	@Schema(description = "Druid 数据源统计明细")
	public static class ContentBean {

		@Schema(description = "服务实例 ID")
		private String serviceId;

		@Schema(description = "Druid 原始字段：Identity")
		private int Identity;

		@Schema(description = "Druid 原始字段：Name")
		private String Name;

		@Schema(description = "Druid 原始字段：DbType")
		private String DbType;

		@Schema(description = "Druid 原始字段：DriverClassName")
		private String DriverClassName;

		@Schema(description = "Druid 原始字段：URL")
		private String URL;

		@Schema(description = "Druid 原始字段：UserName")
		private String UserName;

		@Schema(description = "Druid 原始字段：WaitThreadCount")
		private int WaitThreadCount;

		@Schema(description = "Druid 原始字段：NotEmptyWaitCount")
		private int NotEmptyWaitCount;

		@Schema(description = "Druid 原始字段：NotEmptyWaitMillis")
		private int NotEmptyWaitMillis;

		@Schema(description = "Druid 原始字段：PoolingCount")
		private int PoolingCount;

		@Schema(description = "Druid 原始字段：PoolingPeak")
		private int PoolingPeak;

		@Schema(description = "Druid 原始字段：PoolingPeakTime")
		private String PoolingPeakTime;

		@Schema(description = "Druid 原始字段：ActiveCount")
		private int ActiveCount;

		@Schema(description = "Druid 原始字段：ActivePeak")
		private int ActivePeak;

		@Schema(description = "Druid 原始字段：ActivePeakTime")
		private String ActivePeakTime;

		@Schema(description = "Druid 原始字段：InitialSize")
		private int InitialSize;

		@Schema(description = "Druid 原始字段：MinIdle")
		private int MinIdle;

		@Schema(description = "Druid 原始字段：MaxActive")
		private int MaxActive;

		@Schema(description = "Druid 原始字段：QueryTimeout")
		private int QueryTimeout;

		@Schema(description = "Druid 原始字段：TransactionQueryTimeout")
		private int TransactionQueryTimeout;

		@Schema(description = "Druid 原始字段：LoginTimeout")
		private int LoginTimeout;

		@Schema(description = "Druid 原始字段：ValidConnectionCheckerClassName")
		private String ValidConnectionCheckerClassName;

		@Schema(description = "Druid 原始字段：ExceptionSorterClassName")
		private String ExceptionSorterClassName;

		@Schema(description = "Druid 原始字段：TestOnBorrow")
		private boolean TestOnBorrow;

		@Schema(description = "Druid 原始字段：TestOnReturn")
		private boolean TestOnReturn;

		@Schema(description = "Druid 原始字段：TestWhileIdle")
		private boolean TestWhileIdle;

		@Schema(description = "Druid 原始字段：DefaultAutoCommit")
		private boolean DefaultAutoCommit;

		@Schema(description = "Druid 原始字段：DefaultReadOnly")
		private Object DefaultReadOnly;

		@Schema(description = "Druid 原始字段：DefaultTransactionIsolation")
		private Object DefaultTransactionIsolation;

		@Schema(description = "Druid 原始字段：LogicConnectCount")
		private int LogicConnectCount;

		@Schema(description = "Druid 原始字段：LogicCloseCount")
		private int LogicCloseCount;

		@Schema(description = "Druid 原始字段：LogicConnectErrorCount")
		private int LogicConnectErrorCount;

		@Schema(description = "Druid 原始字段：PhysicalConnectCount")
		private int PhysicalConnectCount;

		@Schema(description = "Druid 原始字段：PhysicalCloseCount")
		private int PhysicalCloseCount;

		@Schema(description = "Druid 原始字段：PhysicalConnectErrorCount")
		private int PhysicalConnectErrorCount;

		@Schema(description = "Druid 原始字段：ExecuteCount")
		private int ExecuteCount;

		@Schema(description = "Druid 原始字段：ExecuteUpdateCount")
		private int ExecuteUpdateCount;

		@Schema(description = "Druid 原始字段：ExecuteQueryCount")
		private int ExecuteQueryCount;

		@Schema(description = "Druid 原始字段：ExecuteBatchCount")
		private int ExecuteBatchCount;

		@Schema(description = "Druid 原始字段：ErrorCount")
		private int ErrorCount;

		@Schema(description = "Druid 原始字段：CommitCount")
		private int CommitCount;

		@Schema(description = "Druid 原始字段：RollbackCount")
		private int RollbackCount;

		@Schema(description = "Druid 原始字段：PSCacheAccessCount")
		private int PSCacheAccessCount;

		@Schema(description = "Druid 原始字段：PSCacheHitCount")
		private int PSCacheHitCount;

		@Schema(description = "Druid 原始字段：PSCacheMissCount")
		private int PSCacheMissCount;

		@Schema(description = "Druid 原始字段：StartTransactionCount")
		private int StartTransactionCount;

		@Schema(description = "Druid 原始字段：RemoveAbandoned")
		private boolean RemoveAbandoned;

		@Schema(description = "Druid 原始字段：ClobOpenCount")
		private int ClobOpenCount;

		@Schema(description = "Druid 原始字段：BlobOpenCount")
		private int BlobOpenCount;

		@Schema(description = "Druid 原始字段：KeepAliveCheckCount")
		private int KeepAliveCheckCount;

		@Schema(description = "Druid 原始字段：KeepAlive")
		private boolean KeepAlive;

		@Schema(description = "Druid 原始字段：FailFast")
		private boolean FailFast;

		@Schema(description = "Druid 原始字段：MaxWait")
		private int MaxWait;

		@Schema(description = "Druid 原始字段：MaxWaitThreadCount")
		private int MaxWaitThreadCount;

		@Schema(description = "Druid 原始字段：PoolPreparedStatements")
		private boolean PoolPreparedStatements;

		@Schema(description = "Druid 原始字段：MaxPoolPreparedStatementPerConnectionSize")
		private int MaxPoolPreparedStatementPerConnectionSize;

		@Schema(description = "Druid 原始字段：MinEvictableIdleTimeMillis")
		private int MinEvictableIdleTimeMillis;

		@Schema(description = "Druid 原始字段：MaxEvictableIdleTimeMillis")
		private int MaxEvictableIdleTimeMillis;

		@Schema(description = "Druid 原始字段：LogDifferentThread")
		private boolean LogDifferentThread;

		@Schema(description = "Druid 原始字段：RecycleErrorCount")
		private int RecycleErrorCount;

		@Schema(description = "Druid 原始字段：PreparedStatementOpenCount")
		private int PreparedStatementOpenCount;

		@Schema(description = "Druid 原始字段：PreparedStatementClosedCount")
		private int PreparedStatementClosedCount;

		@Schema(description = "Druid 原始字段：UseUnfairLock")
		private boolean UseUnfairLock;

		@Schema(description = "Druid 原始字段：InitGlobalVariants")
		private boolean InitGlobalVariants;

		@Schema(description = "Druid 原始字段：InitVariants")
		private boolean InitVariants;

		@Schema(description = "Druid 原始字段：FilterClassNames")
		private List<String> FilterClassNames;

		@Schema(description = "Druid 原始字段：TransactionHistogram")
		private List<Integer> TransactionHistogram;

		@Schema(description = "Druid 原始字段：ConnectionHoldTimeHistogram")
		private List<Integer> ConnectionHoldTimeHistogram;

	}

}
