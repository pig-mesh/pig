package com.pig4cloud.pigx.flow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.admin.api.utils.DataUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.dto.*;
import com.pig4cloud.pigx.flow.entity.Process;
import com.pig4cloud.pigx.flow.entity.ProcessCopy;
import com.pig4cloud.pigx.flow.entity.ProcessGroup;
import com.pig4cloud.pigx.flow.entity.ProcessInstanceRecord;
import com.pig4cloud.pigx.flow.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 远程服务接口实现类
 * <p>
 * 该类为流程引擎提供远程调用接口，主要功能包括：
 * 1. 用户和部门数据查询（角色、部门关联的用户等）
 * 2. 流程事件处理（节点开始、结束、任务分配等）
 * 3. 抄送功能的保存和处理
 * 4. 部门层级关系的判断
 * 5. 流程管理员查询
 * 
 * 该服务作为流程引擎与业务系统的桥梁，处理流程执行过程中的各种回调事件
 * </p>
 * 
 * @author pigx code generator
 * @date 2023-10-01
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RemoteServiceImpl implements IRemoteService {

	private final IProcessInstanceRecordService processInstanceRecordService;

	private final IProcessNodeRecordService processNodeRecordService;

	private final IProcessNodeRecordAssignUserService processNodeRecordAssignUserService;

	private final IProcessInstanceService processInstanceService;

	private final IProcessCopyService processCopyService;

	private final IProcessService processService;

	private final IProcessGroupService processGroupService;

	private final RemoteUserService userService;

	private final RemoteDeptService deptService;

	/**
	 * 根据角色id集合查询用户id集合
	 * <p>
	 * 查询拥有指定角色的所有用户ID，用于流程节点的角色审批人设置
	 * </p>
	 * 
	 * @param roleIdList 角色ID列表
	 * @return R<List<Long>> 拥有这些角色的用户ID列表
	 */
	@Override
	public R<List<Long>> queryUserIdListByRoleIdList(List<Long> roleIdList) {
		return userService.getUserIdListByRoleIdList(roleIdList);
	}

	/**
	 * 保存抄送
	 * <p>
	 * 异步保存流程抄送记录。由于抄送可能在流程的第一个节点，
	 * 此时流程实例记录可能还未创建，因此采用异步重试机制，
	 * 最多重试10次，每次间隔5秒，确保抄送记录能正确保存
	 * </p>
	 * 
	 * @param copyDto 抄送信息，包含流程实例ID、抄送人、节点信息等
	 * @return R 操作结果
	 */
	@Override
	public R saveCC(ProcessCopyDto copyDto) {

		String processInstanceId = copyDto.getProcessInstanceId();

		// 如果抄送是第一个节点 会出现查询不到的情况
		ThreadUtil.execute(() -> {
			try {
				ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
					.eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
					.one();

				int index = 10;
				while (index > 0 && processInstanceRecord == null) {
					TimeUnit.SECONDS.sleep(5);
					processInstanceRecord = processInstanceRecordService.lambdaQuery()
						.eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
						.one();
					index--;
				}

				ProcessCopy processCopy = BeanUtil.copyProperties(copyDto, ProcessCopy.class);
				processCopy.setGroupId(processInstanceRecord.getGroupId());
				processCopy.setGroupName(processInstanceRecord.getGroupName());
				processCopy.setProcessName(processInstanceRecord.getName());
				processCopy.setStartTime(processInstanceRecord.getCreateTime());

				processCopyService.save(processCopy);
			}
			catch (Exception e) {
				log.error("Error:", e);
			}
		});

		return R.ok();
	}

	/**
	 * 检查是否是所有的父级
	 * <p>
	 * 判断给定的部门ID列表是否都是指定部门的父级部门。
	 * 用于流程审批中的"部门主管"、"上级部门"等审批人设置
	 * </p>
	 * 
	 * @param checkParentDto 包含父级部门ID和待检查的部门ID列表
	 * @return R<Boolean> 如果所有部门都是父级返回true，否则返回false
	 */
	@Override
	public R<Boolean> checkIsAllParent(CheckParentDto checkParentDto) {

		Long parentId = checkParentDto.getParentId();
		List<Long> deptIdList = checkParentDto.getDeptIdList();
		// 查询子级包括自己
		List<SysDept> allDept = deptService.getAllDept(null).getData();
		List<SysDept> childrenDeptList = DataUtil.selectChildrenByDept(parentId, allDept);

        List<Long> childrenDeptIdList = childrenDeptList.stream().map(SysDept::getDeptId).toList();
		childrenDeptIdList.remove(parentId);

		List<Long> remainIdList = CollUtil.removeAny(deptIdList, ArrayUtil.toArray(childrenDeptIdList, Long.class));

		return R.ok(remainIdList.isEmpty());
	}

	/**
	 * 根据部门id集合查询用户id集合
	 * <p>
	 * 查询指定部门下的所有用户ID，用于流程节点的部门审批人设置
	 * </p>
	 * 
	 * @param depIdList 部门ID列表
	 * @return R<List<Long>> 这些部门下的所有用户ID列表
	 */
	@Override
	public R<List<Long>> queryUserIdListByDepIdList(List<Long> depIdList) {
		List<Long> list = userService.getUserIdListByDeptIdList(depIdList)
			.getData()
			.stream()
			.map(SysUser::getUserId)
			.toList();
		return R.ok(list);
	}

	/**
	 * 检查是否是所有的子级
	 * <p>
	 * 判断给定的部门ID列表是否都是指定部门的子级部门。
	 * 用于流程审批中的下级部门相关的审批人设置
	 * </p>
	 * 
	 * @param checkChildDto 包含子级部门ID和待检查的部门ID列表
	 * @return R<Boolean> 如果所有部门都是子级返回true，否则返回false
	 */
	@Override
	public R<Boolean> checkIsAllChild(CheckChildDto checkChildDto) {
		Long childId = checkChildDto.getChildId();
		List<Long> deptIdList = checkChildDto.getDeptIdList();
		// 查询父级包括自己
		List<SysDept> allDept = deptService.getAllDept(null).getData();
		List<SysDept> parentDeptList = DataUtil.selectParentByDept(childId, allDept);
        List<Long> parentDeptIdList = parentDeptList.stream().map(SysDept::getDeptId).toList();
		parentDeptIdList.remove(childId);
		List<Long> remainIdList = CollUtil.removeAny(deptIdList, ArrayUtil.toArray(parentDeptIdList, Long.class));
		return R.ok(remainIdList.isEmpty());
	}

	/**
	 * 获取用户的信息-包括扩展字段
	 * <p>
	 * 查询用户的完整信息，包括基本信息和扩展字段。
	 * 用于流程表单中需要获取用户详细信息的场景
	 * </p>
	 * 
	 * @param userId 用户ID
	 * @return R<Map<String, Object>> 用户信息的Map格式，包含所有字段
	 */
	@Override
	public R<Map<String, Object>> queryUserAllInfo(long userId) {
		SysUser sysUser = userService.getUserById(CollUtil.newArrayList(userId)).getData().get(0);
		return R.ok(BeanUtil.beanToMap(sysUser));
	}

	/**
	 * 开始节点事件
	 * <p>
	 * 处理流程节点开始执行的事件，记录节点的开始时间和状态
	 * </p>
	 * 
	 * @param recordParamDto 节点记录参数
	 * @return R 操作结果
	 */
	@Override
	public R startNodeEvent(ProcessNodeRecordParamDto recordParamDto) {
		return processNodeRecordService.start(recordParamDto);
	}

	/**
	 * 流程创建了
	 * <p>
	 * 处理流程实例创建事件，保存流程实例的基本信息，包括：
	 * 流程名称、所属分组、发起人、logo等信息
	 * </p>
	 * 
	 * @param processInstanceRecordParamDto 流程实例参数，包含流程ID、实例ID、用户ID等
	 * @return R 操作结果
	 */
	@Override
	public R createProcessEvent(ProcessInstanceRecordParamDto processInstanceRecordParamDto) {
		ProcessInstanceRecord entity = BeanUtil.copyProperties(processInstanceRecordParamDto,
				ProcessInstanceRecord.class);

		Process oaForms = processService.getByFlowId(processInstanceRecordParamDto.getFlowId());

		ProcessGroup oaFormGroups = processGroupService.getById(oaForms.getGroupId());

		entity.setName(oaForms.getName());
		entity.setLogo(oaForms.getLogo());
		entity.setUserId(processInstanceRecordParamDto.getUserId());
		entity.setFlowId(processInstanceRecordParamDto.getFlowId());
		entity.setProcessInstanceId(processInstanceRecordParamDto.getProcessInstanceId());
		entity.setGroupId(oaFormGroups.getId());
		entity.setGroupName(oaFormGroups.getGroupName());
		entity.setStatus(NodeStatusEnum.JXZ.getCode());

		processInstanceRecordService.save(entity);

		return R.ok();
	}

	/**
	 * 完成节点事件
	 * <p>
	 * 处理流程节点完成事件，更新节点记录的结束时间和状态
	 * </p>
	 * 
	 * @param recordParamDto 节点记录参数
	 * @return R 操作结果
	 */
	@Override
	public R endNodeEvent(ProcessNodeRecordParamDto recordParamDto) {
		return processNodeRecordService.complete(recordParamDto);
	}

	/**
	 * 开始设置执行人
	 * <p>
	 * 为流程节点分配执行人，记录任务的分配信息
	 * </p>
	 * 
	 * @param processNodeRecordAssignUserParamDto 执行人分配参数
	 * @return R 操作结果
	 */
	@Override
	public R startAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
		return processNodeRecordAssignUserService.addAssignUser(processNodeRecordAssignUserParamDto);
	}

	/**
	 * 任务结束事件
	 * <p>
	 * 处理任务完成事件，更新执行人的任务完成状态和审批意见
	 * </p>
	 * 
	 * @param processNodeRecordAssignUserParamDto 任务完成参数
	 * @return R 操作结果
	 */
	@Override
	public R taskEndEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
		return processNodeRecordAssignUserService.completeTaskEvent(processNodeRecordAssignUserParamDto);
	}

	/**
	 * 结束流程实例的方法
	 * <p>
	 * 处理流程实例结束事件，更新流程实例的结束时间、状态和结束原因
	 * </p>
	 *
	 * @param processInstanceParamDto 包含流程实例标识的参数对象
	 * @return R 流程实例结束操作的响应结果
	 */
	@Override
	public R endProcess(ProcessInstanceParamDto processInstanceParamDto) {
		return processInstanceService.end(processInstanceParamDto);
	}

	/**
	 * 查询流程管理员
	 * <p>
	 * 根据流程ID查询该流程的管理员ID。
	 * 流程管理员拥有该流程的最高权限，可以进行流程的编辑、删除等操作
	 * </p>
	 * 
	 * @param flowId 流程ID
	 * @return R<Long> 流程管理员的用户ID
	 */
	@Override
	public R<Long> queryProcessAdmin(String flowId) {
		Process process = processService.getByFlowId(flowId);
		return R.ok(process.getAdminId());
	}

}
