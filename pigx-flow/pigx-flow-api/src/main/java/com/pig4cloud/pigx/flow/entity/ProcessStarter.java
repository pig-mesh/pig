package com.pig4cloud.pigx.flow.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 流程发起人配置实体类
 * <p>
 * 用于配置哪些用户或部门可以发起特定的流程。通过本实体，可以灵活控制流程的发起权限，
 * 支持按用户或按部门进行权限配置。这是流程权限管理的重要组成部分，确保只有授权的
 * 人员才能发起相应的流程申请。
 * </p>
 * <p>
 * 与Process实体的关系：多个ProcessStarter可以关联到同一个Process，
 * 表示该流程可以由多个用户或部门发起。通过processId字段与Process表建立关联。
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@TenantTable
@Accessors(chain = true)
@TableName("process_starter")
public class ProcessStarter {

	/**
	 * 类型关联ID
	 * <p>
	 * 根据type字段的值，此ID可能代表不同的含义：
	 * - 当type为"user"时：表示用户ID，对应系统用户表的用户标识
	 * - 当type为"dept"时：表示部门ID，对应组织架构中的部门标识
	 * 通过这种设计，可以灵活配置个人权限或部门权限。
	 * </p>
	 */
	@TableField("type_id")
	private Long typeId;

	/**
	 * 发起人类型
	 * <p>
	 * 标识发起人的类型，支持以下值：
	 * - "user"：表示按用户配置，typeId为具体的用户ID
	 * - "dept"：表示按部门配置，typeId为部门ID，该部门下的所有用户都有发起权限
	 * 这种设计支持精细化的权限控制和批量授权。
	 * </p>
	 */
	@TableField("type")
	private String type;

	/**
	 * 流程ID
	 * <p>
	 * 关联到Process表的主键ID，标识这个发起人配置属于哪个流程。
	 * 通过此字段建立与流程定义的关联关系，一个流程可以配置多个发起人。
	 * </p>
	 */
	@TableField("process_id")
	private Long processId;

	/**
	 * 主键ID
	 * <p>
	 * 流程发起人配置的唯一标识符，采用雪花算法生成。
	 * </p>
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 逻辑删除标记
	 * <p>
	 * 用于软删除功能，删除时不会真正从数据库中移除记录，而是标记为已删除。
	 * 0-正常，1-已删除。保留历史配置数据便于审计和恢复。
	 * </p>
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

	/**
	 * 创建时间
	 * <p>
	 * 记录发起人配置的创建时间，自动填充。
	 * 用于追踪权限配置的时间线，便于审计和问题排查。
	 * </p>
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 * <p>
	 * 记录配置的最后更新时间，在创建和更新时自动填充。
	 * 用于跟踪权限配置的变更历史。
	 * </p>
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}
