package com.pig4cloud.pigx.app.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 导航栏
 *
 * @author lengleng
 * @date 2023-06-08 11:18:46
 */
@Data
@TenantTable
@TableName("app_tabbar")
@EqualsAndHashCode(callSuper = true)
@Schema(description = "导航栏")
public class AppTabbarEntity extends Model<AppTabbarEntity> {

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@Schema(description = "主键")
	private Long id;

	/**
	 * 导航名称
	 */
	@Schema(description = "导航名称")
	private String name;

	/**
	 * 未选图标
	 */
	@Schema(description = "未选图标")
	private String selected;

	/**
	 * 已选图标
	 */
	@Schema(description = "已选图标")
	private String unselected;

	/**
	 * 链接地址
	 */
	@Schema(description = "链接地址")
	@JsonRawValue
	@JsonDeserialize()
	private String link;

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
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "删除标记")
	private String delFlag;

}
