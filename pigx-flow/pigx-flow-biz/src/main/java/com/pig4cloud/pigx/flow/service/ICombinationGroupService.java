package com.pig4cloud.pigx.flow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 流程组合服务接口
 * <p>
 * 该服务负责管理流程表单组的聚合查询功能，提供了流程组与流程关联关系的查询，
 * 以及用户权限范围内可发起流程组的查询等功能。主要用于流程发起页面的表单组展示
 * 和流程选择等场景。
 * </p>
 *
 * @author pig code generator
 * @date 2023-07-11
 */
public interface ICombinationGroupService {

	/**
	 * 分页查询指定表单组下的流程列表
	 * <p>
	 * 根据表单组ID查询该组下所有关联的流程定义，支持分页查询。
	 * 主要用于流程发起页面，展示某个表单组下可以发起的所有流程。
	 * </p>
	 *
	 * @param page 分页参数，包含当前页码、每页大小等分页信息
	 * @param groupId 表单组ID，用于查询该组下的所有流程
	 * @return R 响应结果，包含分页后的流程列表数据，每个流程包含流程定义信息、表单配置等
	 */
	R listGroupWithProcess(Page page, Long groupId);

	/**
	 * 查询当前用户可发起的所有表单组
	 * <p>
	 * 根据当前登录用户的权限，查询其有权限发起的所有流程表单组。
	 * 该接口会根据用户角色、部门等权限信息过滤表单组，确保用户只能看到
	 * 自己有权限发起的流程组。通常用于流程发起入口页面的初始化展示。
	 * </p>
	 *
	 * @return R 响应结果，包含当前用户可发起的表单组列表，每个表单组包含组名称、图标、包含的流程数量等信息
	 */
	R listCurrentUserStartGroup();

}
