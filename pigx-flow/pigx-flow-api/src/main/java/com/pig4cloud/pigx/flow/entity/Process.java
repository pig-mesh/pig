package com.pig4cloud.pigx.flow.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 流程定义实体类
 * <p>
 * 该实体类用于存储工作流程的定义信息，包括流程的基本信息、表单配置、流程配置等。 每个流程定义可以创建多个流程实例，支持多租户隔离。
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
	 * 流程唯一标识ID 用于唯一标识一个流程定义，在流程实例创建时使用
	 */
	@TableField("flow_id")
	private String flowId;

	/**
	 * 流程名称 流程的显示名称，用于在流程列表中展示
	 */
	@TableField("name")
	private String name;

	/**
	 * 流程图标配置 存储流程的图标信息，支持自定义图标样式
	 */
	@TableField("logo")
	private String logo;

	/**
	 * 流程设置项 JSON格式存储流程的各种配置信息，如通知设置、权限设置等
	 */
	@TableField("settings")
	private String settings;

	/**
	 * 流程分组ID 关联process_group表，用于对流程进行分类管理
	 */
	@TableField("group_id")
	private Long groupId;

	/** 表单配置 */
	@TableField("form_config")
	private String formConfig;

	/**
	 * 表单配置内容 JSON格式存储表单的结构定义，包括表单项类型、验证规则、默认值等
	 */
	@TableField("form_items")
	private String formItems;

	/**
	 * 流程配置内容 JSON格式存储流程的节点信息、流转条件、审批规则等核心流程定义
	 */
	@TableField("process")
	private String process;

	/**
	 * 流程备注说明 用于记录流程的用途、注意事项等补充信息
	 */
	@TableField("remark")
	private String remark;

	/**
	 * 排序号 用于控制流程在列表中的显示顺序，数值越小越靠前
	 */
	@TableField("sort")
	private Integer sort;

	/**
	 * 流程隐藏标识 0=正常显示，1=隐藏（隐藏的流程不在列表中显示，但仍可使用）
	 */
	@TableField("is_hidden")
	private String hidden;

	/**
	 * 流程停用标识 0=正常启用，1=停用（停用的流程不能创建新实例）
	 */
	@TableField("is_stop")
	private String stop;

	/**
	 * 流程管理员ID 主管理员用户ID，拥有该流程的完全管理权限
	 */
	@TableField("admin_id")
	private Long adminId;

	/**
	 * 流程全局唯一ID 用于流程的版本控制和跨系统识别
	 */
	@TableField("unique_id")
	private String uniqueId;

	/**
	 * 管理员列表 JSON格式存储多个管理员ID，支持多人共同管理流程
	 */
	@TableField("admin_list")
	private String adminList;

	/**
	 * 流程使用范围描述 显示该流程的适用部门、人员范围等信息
	 */
	@TableField("range_show")
	private String rangeShow;

	/**
	 * 主键ID 使用雪花算法生成的分布式ID
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
