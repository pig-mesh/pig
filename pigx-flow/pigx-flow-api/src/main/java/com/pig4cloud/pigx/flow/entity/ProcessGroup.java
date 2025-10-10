package com.pig4cloud.pigx.flow.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 流程分组实体类
 * <p>
 * 用于对流程进行分类管理，将相关的流程归类到同一个分组中，便于用户查找和管理。
 * 支持按照分组名称和排序顺序来组织流程的展示结构。
 * </p>
 *
 * @author Vincent
 * @since 2023-07-06
 */
@Getter
@Setter
@TenantTable
@Accessors(chain = true)
@TableName("process_group")
public class ProcessGroup {

	/**
	 * 分组名称
	 * <p>
	 * 流程分组的显示名称，用于标识该分组的用途或类型。
	 * 例如：人事管理、财务审批、行政事务等。
	 * </p>
	 */
	@TableField("group_name")
	private String groupName;

	/**
	 * 排序序号
	 * <p>
	 * 用于控制分组在列表中的显示顺序，数值越小越靠前。
	 * 可以根据业务重要性或使用频率来设置排序值。
	 * </p>
	 */
	@TableField("sort")
	private Integer sort;

	/**
	 * 分组ID
	 * <p>
	 * 分组的唯一标识符，采用雪花算法生成。
	 * 在Process实体中通过groupId字段引用此ID，建立流程与分组的关联关系。
	 * </p>
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 逻辑删除标记
	 * <p>
	 * 用于软删除功能，删除时不会真正从数据库中移除记录，而是标记为已删除。
	 * 0-正常，1-已删除。这样可以保留历史数据，便于数据恢复和审计。
	 * </p>
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

	/**
	 * 创建时间
	 * <p>
	 * 记录分组的创建时间，自动填充，用于数据审计和追踪。
	 * </p>
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 * <p>
	 * 记录分组信息的最后更新时间，在创建和更新时自动填充。
	 * 用于跟踪数据的变更历史。
	 * </p>
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}
