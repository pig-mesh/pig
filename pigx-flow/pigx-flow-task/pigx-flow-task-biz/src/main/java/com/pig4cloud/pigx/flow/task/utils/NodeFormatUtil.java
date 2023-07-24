package com.pig4cloud.pigx.flow.task.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.task.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.task.constant.NodeTypeEnum;
import com.pig4cloud.pigx.flow.task.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.NodeUser;
import com.pig4cloud.pigx.flow.task.entity.ProcessInstanceRecord;
import com.pig4cloud.pigx.flow.task.entity.ProcessNodeRecordAssignUser;
import com.pig4cloud.pigx.flow.task.service.IProcessInstanceRecordService;
import com.pig4cloud.pigx.flow.task.service.IProcessNodeRecordAssignUserService;
import com.pig4cloud.pigx.flow.task.service.IRemoteService;
import com.pig4cloud.pigx.flow.task.vo.NodeVo;
import com.pig4cloud.pigx.flow.task.vo.UserVo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 节点格式化显示工具
 */
public class NodeFormatUtil {

	/**
	 * 格式化流程节点显示
	 * @param node
	 * @param completeNodeSet
	 * @param continueNodeSet
	 * @param processInstanceId
	 * @param paramMap
	 */
	public static List<NodeVo> formatProcessNodeShow(Node node, Set<String> completeNodeSet,
			Set<String> continueNodeSet, String processInstanceId, Map<String, Object> paramMap) {
		List<NodeVo> list = new ArrayList<>();

		if (!NodeUtil.isNode(node)) {
			return list;
		}

		String name = node.getName();
		Integer type = node.getType();

		// SELF_SELECT

		NodeVo nodeVo = new NodeVo();
		nodeVo.setId(node.getId());
		nodeVo.setName(name);
		nodeVo.setType(type);
		nodeVo.setStatus(NodeStatusEnum.WKS.getCode());
		if (completeNodeSet.contains(node.getId())) {
			nodeVo.setStatus(NodeStatusEnum.YJS.getCode());

		}
		if (continueNodeSet.contains(node.getId())) {
			nodeVo.setStatus(NodeStatusEnum.JXZ.getCode());

		}

		{

			nodeVo.setPlaceholder(node.getPlaceHolder());

		}

		List<UserVo> userVoList = new ArrayList<>();
		if (type == NodeTypeEnum.APPROVAL.getValue().intValue()) {

			Integer assignedType = node.getAssignedType();

			boolean selfSelect = assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT;
			nodeVo.setSelectUser(selfSelect);
			if (selfSelect) {
				nodeVo.setMultiple(node.getMultiple());
			}

			// 用户列表
			if (StrUtil.isNotBlank(processInstanceId)) {
				IProcessNodeRecordAssignUserService processNodeRecordAssignUserService = SpringUtil
					.getBean(IProcessNodeRecordAssignUserService.class);
				List<ProcessNodeRecordAssignUser> processNodeRecordAssignUserList = processNodeRecordAssignUserService
					.lambdaQuery()
					.eq(ProcessNodeRecordAssignUser::getNodeId, node.getId())
					.eq(ProcessNodeRecordAssignUser::getProcessInstanceId, processInstanceId)
					.orderByAsc(ProcessNodeRecordAssignUser::getCreateTime)
					.list();
				Map<String, List<ProcessNodeRecordAssignUser>> map = processNodeRecordAssignUserList.stream()
					.collect(Collectors.groupingBy(ProcessNodeRecordAssignUser::getTaskId));

				for (Map.Entry<String, List<ProcessNodeRecordAssignUser>> entry : map.entrySet()) {
					List<ProcessNodeRecordAssignUser> value = entry.getValue();
					List<UserVo> collect = value.stream().map(w -> {
						UserVo userVo = buildUser(Long.parseLong(w.getUserId()));
						userVo.setShowTime(w.getEndTime());
						userVo.setApproveDesc(w.getApproveDesc());
						userVo.setStatus(w.getStatus());
						userVo.setOperType(w.getTaskType());
						return userVo;
					}).toList();
					userVoList.addAll(collect);
				}

				if (processNodeRecordAssignUserList.isEmpty()) {
					if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF) {
						// 发起人自己
						userVoList.addAll(CollUtil.newArrayList(buildRootUser(processInstanceId)));
					}
					if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT) {
						// 发起人自选
						Object variable = paramMap.get(StrUtil.format("{}_assignee_select", node.getId()));
						List<NodeUser> nodeUserDtos = JSON.parseArray(JSON.toJSONString(variable), NodeUser.class);

						List<Long> collect = nodeUserDtos.stream().map(w -> Long.valueOf(w.getId())).toList();
						for (Long aLong : collect) {
							UserVo userVo = buildUser(aLong);
							userVoList.addAll(CollUtil.newArrayList(userVo));
						}
					}
				}

			}
			else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.USER) {
				// 指定用户

				List<NodeUser> nodeUserList = node.getNodeUserList();
				List<UserVo> tempList = buildUser(nodeUserList);
				userVoList.addAll(tempList);

			}
			else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.FORM_USER) {
				// 表单人员
				String formUser = node.getFormUserId();

				Object o = paramMap.get(formUser);
				if (o != null) {
					String jsonString = JSON.toJSONString(o);
					if (StrUtil.isNotBlank(jsonString)) {
						List<NodeUser> nodeUserDtoList = JSON.parseArray(jsonString, NodeUser.class);
						List<Long> userIdList = nodeUserDtoList.stream().map(NodeUser::getId).toList();
						for (Long aLong : userIdList) {
							userVoList.addAll(CollUtil.newArrayList(buildUser(aLong)));
						}
					}
				}

			}
			else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF) {
				// 发起人自己
				userVoList.addAll(CollUtil.newArrayList(buildUser(SecurityUtils.getUser().getId())));
			}

		}
		else if (Objects.equals(node.getType(), NodeTypeEnum.ROOT.getValue())) {
			// 发起节点
			if (StrUtil.isBlank(processInstanceId)) {
				UserVo userVo = buildUser(SecurityUtils.getUser().getId());
				userVoList.addAll(CollUtil.newArrayList(userVo));
			}
			else {

				IProcessInstanceRecordService processInstanceRecordService = SpringUtil
					.getBean(IProcessInstanceRecordService.class);
				ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
					.eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
					.one();

				UserVo userVo = buildRootUser(processInstanceId);
				userVo.setShowTime(processInstanceRecord.getCreateTime());
				userVo.setStatus(NodeStatusEnum.YJS.getCode());
				userVoList.addAll(CollUtil.newArrayList(userVo));

			}
		}
		else if (node.getType() == NodeTypeEnum.CC.getValue()) {
			// 抄送节点

			List<NodeUser> nodeUserList = node.getNodeUserList();

			List<UserVo> tempList = buildUser(nodeUserList);
			userVoList.addAll(tempList);

		}
		nodeVo.setUserVoList(userVoList);

		List<NodeVo> branchList = new ArrayList<>();

		if (type == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue()
				|| type == NodeTypeEnum.PARALLEL_GATEWAY.getValue().intValue()) {
			// 条件分支
			List<Node> branchs = node.getConditionNodes();

			for (Node branch : branchs) {
				Node children = branch.getChildren();
				List<NodeVo> processNodeShowDtos = formatProcessNodeShow(children, completeNodeSet, continueNodeSet,
						processInstanceId, paramMap);

				NodeVo p = new NodeVo();
				p.setChildren(processNodeShowDtos);

				p.setPlaceholder(branch.getPlaceHolder());
				branchList.add(p);
			}
		}
		nodeVo.setBranch(branchList);

		list.add(nodeVo);

		List<NodeVo> next = formatProcessNodeShow(node.getChildren(), completeNodeSet, continueNodeSet,
				processInstanceId, paramMap);
		list.addAll(next);

		return list;
	}

	/**
	 * 根据实例id
	 * @param processInstanceId
	 * @return
	 */
	private static UserVo buildRootUser(String processInstanceId) {

		IProcessInstanceRecordService processInstanceRecordService = SpringUtil
			.getBean(IProcessInstanceRecordService.class);
		ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery()
			.eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
			.one();
		Long userId = processInstanceRecord.getUserId();
		UserVo userVo = buildUser(userId);
		return userVo;
	}

	/**
	 * 根据用户id
	 * @param userId
	 * @return
	 */
	private static UserVo buildUser(long userId) {
		RemoteUserService userService = SpringUtil.getBean(RemoteUserService.class);
		SysUser user = userService.getUserById(userId).getData();
		if (user == null) {
			return null;
		}

		return UserVo.builder().id(userId).name(user.getName()).avatar(user.getAvatar()).build();
	}

	private static List<UserVo> buildUser(List<NodeUser> nodeUserList) {
		List<UserVo> userVoList = new ArrayList<>();
		// 用户id
		List<Long> userIdList = nodeUserList.stream()
			.filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey()))
			.map(w -> Convert.toLong(w.getId()))
			.collect(Collectors.toList());
		// 部门id
		List<Long> deptIdList = nodeUserList.stream()
			.filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey()))
			.map(w -> Convert.toLong(w.getId()))
			.collect(Collectors.toList());

		if (CollUtil.isNotEmpty(deptIdList)) {

			IRemoteService iRemoteService = SpringUtil.getBean(IRemoteService.class);

			List<Long> data = iRemoteService.queryUserIdListByDepIdList(deptIdList).getData();

			if (CollUtil.isNotEmpty(data)) {
				for (long datum : data) {
					if (!userIdList.contains(datum)) {
						userIdList.add(datum);
					}
				}
			}
		}
		{
			for (Long aLong : userIdList) {
				userVoList.addAll(CollUtil.newArrayList(buildUser(aLong)));
			}
		}
		return userVoList;
	}

}
