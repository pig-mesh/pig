package com.pig4cloud.pigx.flow.task.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 流程抄送数据
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@TenantTable
@Accessors(chain = true)
@TableName("process_copy")
public class ProcessCopy {

	/**
	 * 用户id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 流程发起时间
	 */
	@TableField("start_time")
	private LocalDateTime startTime;

	/**
	 * 当前节点时间
	 */
	@TableField("node_time")
	private LocalDateTime nodeTime;

	/**
	 * 发起人
	 */
	@TableField("start_user_id")
	private Long startUserId;

	/**
	 * 流程id
	 */
	@TableField("flow_id")
	private String flowId;

	/**
	 * 实例id
	 */
	@TableField("process_instance_id")
	private String processInstanceId;

	/**
	 * 节点id
	 */
	@TableField("node_id")
	private String nodeId;

	/**
	 * 分组id
	 */
	@TableField("group_id")
	private Long groupId;

	/**
	 * 分组名称
	 */
	@TableField("group_name")
	private String groupName;

	/**
	 * 流程名称
	 */
	@TableField("process_name")
	private String processName;

	/**
	 * 节点 名称
	 */
	@TableField("node_name")
	private String nodeName;

	/**
	 * 表单数据
	 */
	@TableField("form_data")
	private String formData;

	/**
	 * 抄送人id
	 */
	@TableField("user_id")
	private Long userId;

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
