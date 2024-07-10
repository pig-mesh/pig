package com.pig4cloud.pigx.flow.task.service.impl;

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
import com.pig4cloud.pigx.flow.task.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.task.dto.*;
import com.pig4cloud.pigx.flow.task.entity.Process;
import com.pig4cloud.pigx.flow.task.entity.ProcessCopy;
import com.pig4cloud.pigx.flow.task.entity.ProcessGroup;
import com.pig4cloud.pigx.flow.task.entity.ProcessInstanceRecord;
import com.pig4cloud.pigx.flow.task.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
	 * @param roleIdList
	 * @return
	 */
	@Override
	public R<List<Long>> queryUserIdListByRoleIdList(List<Long> roleIdList) {
		return userService.getUserIdListByRoleIdList(roleIdList);
	}

	/**
	 * 保存抄送
	 * @param copyDto
	 * @return
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
	 * @param checkParentDto
	 * @return
	 */
	@Override
	public R<Boolean> checkIsAllParent(CheckParentDto checkParentDto) {

		Long parentId = checkParentDto.getParentId();
		List<Long> deptIdList = checkParentDto.getDeptIdList();
		// 查询子级包括自己
		List<SysDept> allDept = deptService.getAllDept(null).getData();
		List<SysDept> childrenDeptList = DataUtil.selectChildrenByDept(parentId, allDept);

		List<Long> childrenDeptIdList = childrenDeptList.stream().map(SysDept::getDeptId).collect(Collectors.toList());
		childrenDeptIdList.remove(parentId);

		List<Long> remainIdList = CollUtil.removeAny(deptIdList, ArrayUtil.toArray(childrenDeptIdList, Long.class));

		return R.ok(remainIdList.isEmpty());
	}

	/**
	 * 根据部门id集合查询用户id集合
	 * @param depIdList
	 * @return
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
	 * @param checkChildDto
	 * @return
	 */
	@Override
	public R<Boolean> checkIsAllChild(CheckChildDto checkChildDto) {
		Long childId = checkChildDto.getChildId();
		List<Long> deptIdList = checkChildDto.getDeptIdList();
		// 查询父级包括自己
		List<SysDept> allDept = deptService.getAllDept(null).getData();
		List<SysDept> parentDeptList = DataUtil.selectParentByDept(childId, allDept);
		List<Long> parentDeptIdList = parentDeptList.stream().map(SysDept::getDeptId).collect(Collectors.toList());
		parentDeptIdList.remove(childId);
		List<Long> remainIdList = CollUtil.removeAny(deptIdList, ArrayUtil.toArray(parentDeptIdList, Long.class));
		return R.ok(remainIdList.isEmpty());
	}

	/**
	 * 获取用户的信息-包括扩展字段
	 * @param userId
	 * @return
	 */
	@Override
	public R<Map<String, Object>> queryUserAllInfo(long userId) {
		SysUser sysUser = userService.getUserById(CollUtil.newArrayList(userId)).getData().get(0);
		return R.ok(BeanUtil.beanToMap(sysUser));
	}

	/**
	 * 开始节点事件
	 * @param recordParamDto
	 * @return
	 */
	@Override
	public R startNodeEvent(ProcessNodeRecordParamDto recordParamDto) {
		return processNodeRecordService.start(recordParamDto);
	}

	/**
	 * 流程创建了
	 * @param processInstanceRecordParamDto
	 * @return
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
	 * @param recordParamDto
	 * @return
	 */
	@Override
	public R endNodeEvent(ProcessNodeRecordParamDto recordParamDto) {
		return processNodeRecordService.complete(recordParamDto);
	}

	/**
	 * 开始设置执行人
	 * @param processNodeRecordAssignUserParamDto
	 * @return
	 */
	@Override
	public R startAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
		return processNodeRecordAssignUserService.addAssignUser(processNodeRecordAssignUserParamDto);
	}

	/**
	 * 任务结束事件
	 * @param processNodeRecordAssignUserParamDto
	 * @return
	 */
	@Override
	public R taskEndEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
		return processNodeRecordAssignUserService.completeTaskEvent(processNodeRecordAssignUserParamDto);
	}

	/**
	 * 实例结束
	 * @param processInstanceId
	 * @return
	 */
	@Override
	public R endProcess(String processInstanceId) {
		return processInstanceService.end(processInstanceId);
	}

	/**
	 * 查询流程管理员
	 * @param flowId
	 * @return
	 */
	@Override
	public R<Long> queryProcessAdmin(String flowId) {
		Process process = processService.getByFlowId(flowId);
		return R.ok(process.getAdminId());
	}

}
