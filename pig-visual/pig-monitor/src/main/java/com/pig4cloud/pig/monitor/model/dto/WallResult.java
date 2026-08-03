package com.pig4cloud.pig.monitor.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Druid SQL 防火墙统计响应。
 * <p>
 * JSON 字段名与 Druid 监控接口保持一致。
 *
 * @author linchtech
 * @date 2020-09-17 18:18
 **/
@Data
@NoArgsConstructor
@Schema(description = "Druid SQL 防火墙统计响应")
public class WallResult {

	@Schema(description = "Druid 原始字段：ResultCode")
	private int ResultCode;

	@Schema(description = "Druid 原始字段：Content")
	private ContentBean Content = new ContentBean();

	/** Druid SQL 防火墙统计内容。 */
	@NoArgsConstructor
	@Data
	@Schema(description = "Druid SQL 防火墙统计内容")
	public static class ContentBean {

		@Schema(description = "Druid 原始字段：checkCount")
		private int checkCount;

		@Schema(description = "Druid 原始字段：hardCheckCount")
		private int hardCheckCount;

		@Schema(description = "Druid 原始字段：violationCount")
		private int violationCount;

		@Schema(description = "Druid 原始字段：violationEffectRowCount")
		private int violationEffectRowCount;

		@Schema(description = "Druid 原始字段：blackListHitCount")
		private int blackListHitCount;

		@Schema(description = "Druid 原始字段：blackListSize")
		private int blackListSize;

		@Schema(description = "Druid 原始字段：whiteListHitCount")
		private int whiteListHitCount;

		@Schema(description = "Druid 原始字段：whiteListSize")
		private int whiteListSize;

		@Schema(description = "Druid 原始字段：syntaxErrorCount")
		private int syntaxErrorCount;

		@Schema(description = "Druid 原始字段：tables")
		private List<TablesBean> tables = new ArrayList<>();

		@Schema(description = "Druid 原始字段：functions")
		private List<FunctionsBean> functions = new ArrayList<>();

		@Schema(description = "Druid 原始字段：blackList")
		private List<Object> blackList = new ArrayList<>();

		@Schema(description = "Druid 原始字段：whiteList")
		private List<WhiteListBean> whiteList = new ArrayList<>();

		/** Druid SQL 防火墙表统计。 */
		@NoArgsConstructor
		@Data
		@Schema(description = "Druid SQL 防火墙表统计")
		public static class TablesBean {

			@Schema(description = "Druid 原始字段：name")
			private String name;

			@Schema(description = "Druid 原始字段：selectCount")
			private int selectCount;

			@Schema(description = "Druid 原始字段：fetchRowCount")
			private int fetchRowCount;

			@Schema(description = "Druid 原始字段：fetchRowCountHistogram")
			private List<Integer> fetchRowCountHistogram;

		}

		/** Druid SQL 防火墙函数统计。 */
		@NoArgsConstructor
		@Data
		@Schema(description = "Druid SQL 防火墙函数统计")
		public static class FunctionsBean {

			@Schema(description = "Druid 原始字段：name")
			private String name;

			@Schema(description = "Druid 原始字段：invokeCount")
			private int invokeCount;

		}

		/** Druid SQL 防火墙白名单统计。 */
		@NoArgsConstructor
		@Data
		@Schema(description = "Druid SQL 防火墙白名单统计")
		public static class WhiteListBean {

			@Schema(description = "Druid 原始字段：sql")
			private String sql;

			@Schema(description = "Druid 原始字段：sample")
			private String sample;

			@Schema(description = "Druid 原始字段：executeCount")
			private int executeCount;

			@Schema(description = "Druid 原始字段：fetchRowCount")
			private int fetchRowCount;

		}

	}

	/**
	 * 将单个节点的 SQL 防火墙统计累加到汇总结果中。
	 * <p>
	 * {@code wallResult}、{@code sumResult} 或其 {@code Content} 为空时直接返回，不修改汇总结果。
	 * @param wallResult 待累加的节点统计；为 {@code null} 时忽略
	 * @param sumResult 累加目标；为 {@code null} 时忽略
	 */
	public void sum(WallResult wallResult, WallResult sumResult) {
		if (wallResult == null || sumResult == null || wallResult.getContent() == null
				|| sumResult.getContent() == null) {
			return;
		}
		sumResult.getContent()
			.setCheckCount(sumResult.getContent().getCheckCount() + wallResult.getContent().getCheckCount());
		sumResult.getContent()
			.setHardCheckCount(
					sumResult.getContent().getHardCheckCount() + wallResult.getContent().getHardCheckCount());
		sumResult.getContent()
			.setViolationCount(
					sumResult.getContent().getViolationCount() + wallResult.getContent().getViolationCount());
		sumResult.getContent()
			.setViolationEffectRowCount(sumResult.getContent().getViolationEffectRowCount()
					+ wallResult.getContent().getViolationEffectRowCount());
		sumResult.getContent()
			.setBlackListHitCount(
					sumResult.getContent().getBlackListHitCount() + wallResult.getContent().getBlackListHitCount());
		sumResult.getContent()
			.setBlackListSize(sumResult.getContent().getBlackListSize() + wallResult.getContent().getBlackListSize());
		sumResult.getContent()
			.setWhiteListHitCount(
					sumResult.getContent().getWhiteListHitCount() + wallResult.getContent().getWhiteListHitCount());
		sumResult.getContent()
			.setWhiteListSize(sumResult.getContent().getWhiteListSize() + wallResult.getContent().getWhiteListSize());
		sumResult.getContent()
			.setSyntaxErrorCount(
					sumResult.getContent().getSyntaxErrorCount() + wallResult.getContent().getSyntaxErrorCount());

		sumResult.getContent()
			.getTables()
			.addAll(wallResult.getContent().getTables() == null ? Collections.emptyList()
					: wallResult.getContent().getTables());
		sumResult.getContent()
			.getFunctions()
			.addAll(wallResult.getContent().getFunctions() == null ? Collections.emptyList()
					: wallResult.getContent().getFunctions());
		sumResult.getContent()
			.getBlackList()
			.addAll(wallResult.getContent().getBlackList() == null ? Collections.emptyList()
					: wallResult.getContent().getBlackList());
		sumResult.getContent()
			.getWhiteList()
			.addAll(wallResult.getContent().getWhiteList() == null ? Collections.emptyList()
					: wallResult.getContent().getWhiteList());
	}

}
