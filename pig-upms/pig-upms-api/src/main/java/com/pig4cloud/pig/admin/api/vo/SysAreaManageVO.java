package com.pig4cloud.pig.admin.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 行政区划管理视图
 *
 * @author lengleng
 * @date 2026/7/18
 */
@Data
@Schema(description = "行政区划管理视图")
public class SysAreaManageVO {

	@Schema(description = "数据库主键")
	private Long id;

	@Schema(description = "父级行政编码")
	private Long pid;

	@Schema(description = "行政编码")
	private Long adcode;

	@Schema(description = "名称")
	private String name;

	@Schema(description = "行政区划类型")
	private String areaType;

	@Schema(description = "热门标记")
	private String hot;

	@Schema(description = "状态")
	private String areaStatus;

	@Schema(description = "排序值")
	private Long areaSort;

	@Schema(description = "是否存在下级")
	private Boolean hasChildren;

	@Schema(description = "直属子级数量")
	private Long childCount;

	@Schema(description = "完整路径行政编码")
	private List<Long> pathCodes;

	@Schema(description = "完整路径名称")
	private String pathName;

}
