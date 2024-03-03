package com.pig4cloud.pigx.flow.task.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@TenantTable
@Accessors(chain = true)
@TableName("process")
public class Process {

	/**
	 * 表单ID
	 */
	@TableField("flow_id")
	private String flowId;

	/**
	 * 表单名称
	 */
	@TableField("name")
	private String name;

	/**
	 * 图标配置
	 */
	@TableField("logo")
	private String logo;

	/**
	 * 设置项
	 */
	@TableField("settings")
	private String settings;

	/**
	 * 分组ID
	 */
	@TableField("group_id")
	private Long groupId;

	/**
	 * 表单设置内容
	 */
	@TableField("form_items")
	private String formItems;

	/**
	 * 流程设置内容
	 */
	@TableField("process")
	private String process;

	/**
	 * 备注
	 */
	@TableField("remark")
	private String remark;

	@TableField("sort")
	private Integer sort;

	/**
	 * 0 正常 1=隐藏
	 */
	@TableField("is_hidden")
	private String hidden;

	/**
	 * 0 正常 1=停用
	 */
	@TableField("is_stop")
	private String stop;

	/**
	 * 流程管理员
	 */
	@TableField("admin_id")
	private Long adminId;

	/**
	 * 唯一性id
	 */
	@TableField("unique_id")
	private String uniqueId;

	/**
	 * 管理员
	 */
	@TableField("admin_list")
	private String adminList;

	/**
	 * 范围描述显示
	 */
	@TableField("range_show")
	private String rangeShow;

	/**
	 * 用户id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 逻辑删除字段
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}
