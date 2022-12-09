package com.pig4cloud.pigx.admin.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 租户菜单
 */
@Data
@ApiModel(value = "租户菜单")
@EqualsAndHashCode(callSuper = true)
public class SysTenantMenu extends Model<SysTenantMenu> {

	/**
	 * id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 租户菜单名称
	 */
	@ApiModelProperty(value = "租户菜单名称")
	private String name;

	/**
	 * 菜单id集合
	 */
	@ApiModelProperty(value = "菜单id集合")
	private String menuIds;

	/**
	 * 0正常 9-冻结
	 */
	@ApiModelProperty(value = "租户菜单,9:冻结,0:正常")
	private String status;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建人")
	private String createBy;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.UPDATE)
	@ApiModelProperty(value = "修改人")
	private String updateBy;

	/**
	 * 删除标记
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "删除标记,1:已删除,0:正常")
	private String delFlag;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.UPDATE)
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}
