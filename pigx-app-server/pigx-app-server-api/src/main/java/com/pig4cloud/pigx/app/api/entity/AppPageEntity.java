package com.pig4cloud.pigx.app.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

/**
 * App 装修页面实体。
 * <p>
 * 仅对应 {@code app_page} 表，保存页面类型、页面名称和页面组件 JSON。
 * 底部 Tabbar 已拆分到 {@code app_tabbar}，不要再塞进 {@link #pageData}。
 *
 * @author lengleng
 * @date 2023-06-08 11:19:23
 */
@Data
@TenantTable
@TableName("app_page")
@FieldNameConstants
@EqualsAndHashCode(callSuper = true)
@Schema(description = "页面")
public class AppPageEntity extends Model<AppPageEntity> {

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键")
	private Long id;

	/**
     * 页面类型，对应 {@link com.pig4cloud.pigx.app.api.enums.PageTypeEnums}
	 */
	@Schema(description = "页面类型")
	private Integer pageType;

	/**
	 * 页面名称
	 */
	@Schema(description = "页面名称")
	private String pageName;

	/**
     * 页面装修组件 JSON。
     * <p>
     * 只保存页面内部组件，例如搜索、轮播、导航块、服务块等；
     * 运行时需要 Tabbar 时由服务层聚合 {@code app_tabbar} 返回。
	 */
	@Schema(description = "页面数据")
	private String pageData;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建人")
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "修改人")
	private String updateBy;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "修改时间")
	private LocalDateTime updateTime;

	/**
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记")
	private String delFlag;

}
