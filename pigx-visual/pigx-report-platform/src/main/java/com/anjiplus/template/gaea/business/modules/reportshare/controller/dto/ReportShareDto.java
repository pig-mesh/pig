
package com.anjiplus.template.gaea.business.modules.reportshare.controller.dto;

import com.anji.plus.gaea.curd.dto.GaeaBaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @description 报表分享 dto
 * @author Raod
 * @date 2021-08-18 13:37:26.663
 **/
@Data
public class ReportShareDto extends GaeaBaseDTO implements Serializable {

	/** 分享编码，系统生成，默认UUID */
	@Schema(description = "分享编码，系统生成，默认UUID")
	private String shareCode;

	/** 分享有效期类型，DIC_NAME=SHARE_VAILD */
	@Schema(description = "分享有效期类型，DIC_NAME=SHARE_VAILD")
	@NotNull(message = "6002")
	private Integer shareValidType;

	/** 分享有效期 */
	@Schema(description = "分享有效期")
	private Date shareValidTime;

	/** 分享token */
	@Schema(description = "分享token")
	private String shareToken;

	/** 分享url */
	@Schema(description = "分享url")
	@NotEmpty(message = "6002")
	private String shareUrl;

	/** 报表编码 */
	@Schema(description = "报表编码")
	@NotEmpty(message = "6002")
	private String reportCode;

	/** 0--已禁用 1--已启用 DIC_NAME=ENABLE_FLAG */
	@Schema(description = "0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG")
	private Integer enableFlag;

	/** 0--未删除 1--已删除 DIC_NAME=DELETE_FLAG */
	@Schema(description = "0--未删除 1--已删除 DIC_NAME=DELETE_FLAG")
	private Integer deleteFlag;

	/** 分享码 */
	private String sharePassword;

	private boolean sharePasswordFlag = false;

	private String reportType;

}
