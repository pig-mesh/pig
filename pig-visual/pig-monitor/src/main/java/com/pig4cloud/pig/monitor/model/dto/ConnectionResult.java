package com.pig4cloud.pig.monitor.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Druid 数据源活跃连接响应。
 * <p>
 * JSON 字段名与 Druid 监控接口保持一致。
 *
 * @author linchtech
 * @date 2020-09-21 9:26
 **/

@NoArgsConstructor
@Data
@Schema(description = "Druid 数据源活跃连接响应")
public class ConnectionResult {

	@Schema(description = "Druid 原始字段：ResultCode")
	private int ResultCode;

	@Schema(description = "Druid 原始字段：Content")
	private List<ContentBean> Content;

	/** Druid 数据源活跃连接明细。 */
	@NoArgsConstructor
	@Data
	@Schema(description = "Druid 数据源活跃连接明细")
	public static class ContentBean {

		@Schema(description = "Druid 原始字段：id")
		private int id;

		@Schema(description = "Druid 原始字段：connectionId")
		private int connectionId;

		@Schema(description = "Druid 原始字段：useCount")
		private int useCount;

		@Schema(description = "Druid 原始字段：lastActiveTime")
		private String lastActiveTime;

		@Schema(description = "Druid 原始字段：connectTime")
		private String connectTime;

		@Schema(description = "Druid 原始字段：holdability")
		private int holdability;

		@Schema(description = "Druid 原始字段：transactionIsolation")
		private int transactionIsolation;

		@Schema(description = "Druid 原始字段：autoCommit")
		private boolean autoCommit;

		@Schema(description = "Druid 原始字段：readoOnly")
		private boolean readoOnly;

		@Schema(description = "Druid 原始字段：keepAliveCheckCount")
		private int keepAliveCheckCount;

		@Schema(description = "Druid 原始字段：pscache")
		private List<?> pscache;

	}

}
