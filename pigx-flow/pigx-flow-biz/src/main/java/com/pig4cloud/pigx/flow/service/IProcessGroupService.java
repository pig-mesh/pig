package com.pig4cloud.pigx.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.entity.ProcessGroup;

import java.util.List;

/**
 * 流程分组管理服务接口
 * <p>
 * 该服务负责管理流程的分组功能，提供流程分组的创建、查询、删除等操作。
 * 流程分组是对流程进行分类管理的重要机制，可以将相似或相关的流程组织在一起，
 * 便于用户快速找到和使用流程。分组支持层级结构，可以实现流程的树形分类管理。
 * </p>
 *
 * @author Vincent
 * @since 2023-05-25
 */
public interface IProcessGroupService extends IService<ProcessGroup> {

	/**
	 * 查询所有流程分组列表
	 * <p>
	 * 获取系统中所有的流程分组信息，包括分组名称、图标、排序号等。
	 * 返回的列表会按照排序号和创建时间进行排序，支持树形结构的展示。
	 * 该接口通常用于流程管理页面的分组树展示和流程发起页面的分类导航。
	 * </p>
	 *
	 * @return R<List<ProcessGroup>> 响应结果，包含所有流程分组的列表，每个分组包含：
	 *                               - id: 分组ID
	 *                               - groupName: 分组名称
	 *                               - groupIcon: 分组图标
	 *                               - sortOrder: 排序号
	 *                               - parentId: 父分组ID（用于构建树形结构）
	 */
	R<List<ProcessGroup>> queryList();

	/**
	 * 创建新的流程分组
	 * <p>
	 * 添加一个新的流程分组到系统中。创建时会检查分组名称的唯一性，
	 * 并自动设置默认的排序号。支持创建子分组，通过设置parentId实现。
	 * 新创建的分组默认为启用状态，可以立即用于流程的分类。
	 * </p>
	 *
	 * @param processGroup 流程分组对象，需要包含：
	 *                     - groupName: 分组名称（必填，不能重复）
	 *                     - groupIcon: 分组图标（可选）
	 *                     - sortOrder: 排序号（可选，默认为最大值+1）
	 *                     - parentId: 父分组ID（可选，用于创建子分组）
	 * @return R 响应结果，成功返回创建的分组信息，失败返回错误信息（如名称重复等）
	 */
	R create(ProcessGroup processGroup);

	/**
	 * 删除流程分组
	 * <p>
	 * 根据分组ID删除指定的流程分组。删除前会检查该分组下是否存在流程，
	 * 如果存在关联的流程则不允许删除。同时会检查是否有子分组，有子分组
	 * 的情况下也不允许删除。删除操作是逻辑删除，保留历史记录。
	 * </p>
	 *
	 * @param id 要删除的分组ID
	 * @return R 响应结果，成功返回删除成功信息，失败返回错误原因（如分组下有流程、有子分组等）
	 */
	R delete(long id);

}
