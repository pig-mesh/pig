
package com.pig4cloud.pigx.flow.task.service;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.*;

import java.util.List;
import java.util.Map;

/**
 * 远程调用的接口
 */
public interface IRemoteService {

	/**
	 * 根据角色id集合查询用户id集合
	 * @param roleIdList
	 * @return
	 */
	R<List<Long>> queryUserIdListByRoleIdList(List<Long> roleIdList);

	/**
	 * 保存抄送
	 * @param copyDto
	 * @return
	 */
	R saveCC(ProcessCopyDto copyDto);

	/**
	 * 检查是否是所有的父级
	 * @param checkParentDto
	 * @return
	 */
	R<Boolean> checkIsAllParent(CheckParentDto checkParentDto);

	/**
	 * 根据部门id集合查询用户id集合
	 * @param depIdList
	 * @return
	 */
	R<List<Long>> queryUserIdListByDepIdList(List<Long> depIdList);

	/**
	 * 检查是否是所有的子级
	 * @param checkChildDto
	 * @return
	 */
	R<Boolean> checkIsAllChild(CheckChildDto checkChildDto);

	/**
	 * 获取用户的信息-包括扩展字段
	 * @param userId
	 * @return
	 */
	R<Map<String, Object>> queryUserAllInfo(long userId);

	/**
	 * 开始节点事件
	 * @param recordParamDto
	 * @return
	 */
	R startNodeEvent(ProcessNodeRecordParamDto recordParamDto);

	/**
	 * 流程创建了
	 * @param processInstanceRecordParamDto
	 * @return
	 */
	R createProcessEvent(ProcessInstanceRecordParamDto processInstanceRecordParamDto);

	/**
	 * 完成节点事件
	 * @param recordParamDto
	 * @return
	 */
	R endNodeEvent(ProcessNodeRecordParamDto recordParamDto);

	/**
	 * 开始设置执行人
	 * @param processNodeRecordAssignUserParamDto
	 * @return
	 */
	R startAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

	/**
	 * 任务结束事件
	 * @param processNodeRecordAssignUserParamDto
	 * @return
	 */
	R taskEndEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

	/**
	 * 实例结束
	 * @param processInstanceId
	 * @return
	 */
	R endProcess(String processInstanceId);

	/**
	 * 查询流程管理员
	 * @param flowId
	 * @return
	 */
	R<Long> queryProcessAdmin(String flowId);

}
