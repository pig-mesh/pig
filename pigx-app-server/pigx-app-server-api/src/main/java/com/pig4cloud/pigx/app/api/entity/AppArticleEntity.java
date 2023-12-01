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
 * 文章资讯
 *
 * @author pig
 * @date 2023-06-07 16:32:35
 */
@Data
@TenantTable
@FieldNameConstants
@TableName("app_article")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文章资讯")
public class AppArticleEntity extends Model<AppArticleEntity> {

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键")
	private Long id;

	/**
	 * 分类
	 */
	@Schema(description = "分类")
	private Long cid;

	/**
	 * 标题
	 */
	@Schema(description = "标题")
	private String title;

	/**
	 * 简介
	 */
	@Schema(description = "简介")
	private String intro;

	/**
	 * 摘要
	 */
	@Schema(description = "摘要")
	private String summary;

	/**
	 * 封面
	 */
	@Schema(description = "封面")
	private String image;

	/**
	 * 内容
	 */
	@Schema(description = "内容")
	private String content;

	/**
	 * 作者
	 */
	@Schema(description = "作者")
	private String author;

	/**
	 * 浏览
	 */
	@Schema(description = "浏览")
	private Integer visit;

	/**
	 * 排序
	 */
	@Schema(description = "排序")
	private Integer sort;

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
	 * 删除时间
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标志")
	private String delFlag;

	/**
	 * 当前用户是否已收藏
	 */
	@TableField(exist = false)
	private boolean collect;

	/**
	 * 分类名称
	 */
	@TableField(exist = false)
	private String cname;

}
