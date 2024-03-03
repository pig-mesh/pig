package com.pig4cloud.pigx.app.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 文章分类表
 *
 * @author pig
 * @date 2023-06-07 16:28:03
 */
@Data
@TenantTable
@TableName("app_article_category")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文章分类表")
public class AppArticleCategoryEntity extends Model<AppArticleCategoryEntity> {

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键")
	private Long id;

	/**
	 * 名称
	 */
	@Schema(description = "名称")
	private String name;

	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer sort;

	/**
	 * 是否显示: 0=否, 1=是
	 */
	@Schema(description = "是否显示: 0=否, 1=是")
	private Integer isShow;

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
	 * 更新人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "更新人")
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@Schema(description = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 是否删除: 0=否, 1=是
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "是否删除: 0=否, 1=是")
	private String delFlag;

}
