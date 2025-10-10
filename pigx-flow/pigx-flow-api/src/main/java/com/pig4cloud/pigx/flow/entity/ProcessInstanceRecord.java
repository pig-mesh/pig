package com.pig4cloud.pigx.flow.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 流程实例记录实体类
 * <p>
 * 该实体记录每个流程实例的执行情况，包括发起人、表单数据、执行状态等。
 * 一个流程定义(Process)可以创建多个流程实例记录。
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@TenantTable
@Accessors(chain = true)
@FieldNameConstants
@TableName("process_instance_record")
public class ProcessInstanceRecord {

	/**
	 * 流程实例名称
	 * 通常继承自流程定义的名称，也可以在创建时自定义
	 */
	@TableField("name")
	private String name;

	/**
	 * 流程图标
	 * 继承自流程定义的图标配置
	 */
	@TableField("logo")
	private String logo;

	/**
	 * 流程发起人ID
	 * 记录该流程实例的创建者用户ID
	 */
	@TableField("user_id")
	private Long userId;

	/**
	 * 流程定义ID
	 * 关联process表的flow_id，指明该实例属于哪个流程定义
	 */
	@TableField("flow_id")
	private String flowId;

	/**
	 * Flowable引擎流程实例ID
	 * 对应Flowable引擎中的流程实例ID，用于与引擎交互
	 */
	@TableField("process_instance_id")
	private String processInstanceId;

	/**
	 * 表单数据
	 * JSON格式存储用户提交的表单数据，在流程执行过程中使用
	 */
	@TableField("form_data")
	private String formData;

	/**
	 * 流程分组ID
	 * 继承自流程定义的分组ID
	 */
	@TableField("group_id")
	private Long groupId;

	/**
	 * 流程分组名称
	 * 缓存流程所属分组的名称，方便查询展示
	 */
	@TableField("group_name")
	private String groupName;

	/**
	 * 流程实例状态
	 * 1=进行中，2=已完成，3=已取消，4=已中止
	 */
	@TableField("status")
	private Integer status;

	/**
	 * 流程结束原因
	 * 记录流程完成、取消或中止的具体原因
	 */
	@TableField("finish_reason")
	private String finishReason;

	/**
	 * 流程结束时间
	 * 记录流程实例的完成、取消或中止时间
	 */
	@TableField("end_time")
	private Date endTime;

	/**
	 * 父流程实例ID
	 * 当前流程是子流程时，记录父流程的实例ID，支持流程嵌套
	 */
	@TableField("parent_process_instance_id")
	private String parentProcessInstanceId;

	/**
	 * 主键ID
	 * 使用雪花算法生成的分布式ID
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
