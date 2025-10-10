package com.pig4cloud.pigx.flow.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.constant.enums.YesNoEnum;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.entity.Process;
import com.pig4cloud.pigx.flow.entity.ProcessGroup;
import com.pig4cloud.pigx.flow.entity.ProcessStarter;
import com.pig4cloud.pigx.flow.service.ICombinationGroupService;
import com.pig4cloud.pigx.flow.service.IProcessGroupService;
import com.pig4cloud.pigx.flow.service.IProcessService;
import com.pig4cloud.pigx.flow.service.IProcessStarterService;
import com.pig4cloud.pigx.flow.vo.FormGroupVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 组合分组服务实现类
 * <p>
 * 该类负责处理流程分组相关的业务逻辑，主要包括： 1. 查询流程分组及其包含的流程信息 2. 根据用户权限过滤可发起的流程分组 3. 处理流程发起人权限验证
 * </p>
 *
 * @author pigx code generator
 * @date 2023-10-01
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CombinationGroupServiceImpl implements ICombinationGroupService {

	private final IProcessGroupService processGroupService;

	private final IProcessService processService;

	private final IProcessStarterService processStarterService;

	/**
	 * 查询表单组包含流程
	 * <p>
	 * 根据流程分组ID查询该分组下所有未隐藏的流程，按照排序值升序和创建时间降序排列
	 * </p>
	 * @param page 分页参数，用于控制查询结果的分页
	 * @param groupId 流程分组ID
	 * @return R 包含分页后的流程列表
	 */
	@Override
	public R listGroupWithProcess(Page page, Long groupId) {
		return R.ok(processService.lambdaQuery()
			.eq(Process::getGroupId, groupId)
			.eq(Process::getHidden, YesNoEnum.NO.getCode())
			.orderByAsc(Process::getSort)
			.orderByDesc(Process::getCreateTime)
			.page(page));
	}

	/**
	 * 查询所有我可以发起的表单组
	 * <p>
	 * 该方法会根据当前登录用户的权限，查询所有可发起的流程分组及其包含的流程。 主要处理逻辑包括： 1. 获取所有流程分组，按排序值升序排列 2.
	 * 对每个分组查询其下的流程（未隐藏且未停用） 3. 根据流程发起人配置，判断当前用户是否有权限发起 4. 支持用户和部门两种发起人类型的权限判断 5.
	 * 过滤掉用户无权发起的流程
	 * </p>
	 * @return R 包含FormGroupVo列表，每个FormGroupVo包含分组信息和该分组下用户可发起的流程列表
	 */
	@Override
	public R listCurrentUserStartGroup() {
		// 获取当前登录用户信息
		PigxUser user = SecurityUtils.getUser();

		// 存储所有流程分组及其可发起的流程
		List<FormGroupVo> formGroupVos = new LinkedList<>();

		// 查询所有流程分组，按排序值升序排列
		List<ProcessGroup> processGroupList = processGroupService.lambdaQuery()
			.orderByAsc(ProcessGroup::getSort)
			.list();

		// 遍历每个流程分组
		processGroupList.forEach(group -> {
			// 构建流程分组VO对象
			FormGroupVo formGroupVo = FormGroupVo.builder()
				.id(group.getId())
				.name(group.getGroupName())
				.items(new LinkedList<>())
				.build();
			formGroupVos.add(formGroupVo);

			// 查询该分组下所有未隐藏且未停用的流程
			List<Process> processList = processService.lambdaQuery()
				.eq(Process::getGroupId, group.getId())
				.eq(Process::getHidden, YesNoEnum.NO.getCode())
				.eq(Process::getStop, YesNoEnum.NO.getCode())
				.orderByAsc(Process::getSort)
				.list();

			// 用于存储每个流程是否允许当前用户发起的标识
			Map<Long, Boolean> existMap = new HashMap<>();

			if (!processList.isEmpty()) {
				// 获取所有流程ID
				List<Long> idList = processList.stream().map(Process::getId).toList();
				// 批量查询流程发起人配置
				List<ProcessStarter> processStarterList = processStarterService.lambdaQuery()
					.in(ProcessStarter::getProcessId, idList)
					.list();
				// 按流程ID分组
				Map<Long, List<ProcessStarter>> groupmap = processStarterList.stream()
					.collect(Collectors.groupingBy(ProcessStarter::getProcessId));

				// 判断每个流程是否允许当前用户发起
				for (Process process : processList) {
					List<ProcessStarter> processStarters = groupmap.get(process.getId());
					// 如果没有配置发起人，则所有人都可以发起
					if (processStarters == null) {
						existMap.put(process.getId(), true);
						continue;
					}

					// 检查是否配置了当前用户为发起人
					boolean match = processStarters.stream()
						.anyMatch(w -> w.getTypeId().longValue() == user.getId()
								&& w.getType().equals(NodeUserTypeEnum.USER.getKey()));
					if (match) {
						existMap.put(process.getId(), true);
						continue;
					}
					// 检查是否配置了当前用户所在部门为发起人
					Set<Long> deptIdSet = processStarters.stream()
						.filter(w -> w.getType().equals(NodeUserTypeEnum.DEPT.getKey()))
						.map(ProcessStarter::getTypeId)
						.collect(Collectors.toSet());

					boolean contains = CollUtil.containsAny(deptIdSet, user.getDeptIds());

					if (contains) {
						existMap.put(process.getId(), true);
						continue;
					}

					Set<Long> roleSet = processStarters.stream()
						.filter(w -> w.getType().equals(NodeUserTypeEnum.ROLE.getKey()))
						.map(ProcessStarter::getTypeId)
						.collect(Collectors.toSet());

					boolean roleContains = CollUtil.containsAny(roleSet, SecurityUtils.getRoleIds());
					if (roleContains) {
						existMap.put(process.getId(), true);
						continue;
					}

					Set<Long> postSet = processStarters.stream()
						.filter(w -> w.getType().equals(NodeUserTypeEnum.POST.getKey()))
						.map(ProcessStarter::getTypeId)
						.collect(Collectors.toSet());

					boolean postContains = CollUtil.containsAny(postSet, user.getPostIds());
					existMap.put(process.getId(), postContains);
				}

			}

			// 将有权限发起的流程添加到分组中
			processList.forEach(process -> {

				if (!existMap.get(process.getId())) {
					return;
				}

				formGroupVo.getItems()
					.add(FormGroupVo.FlowVo.builder()
						.flowId(process.getFlowId())
						.name(process.getName())
						.logo(process.getLogo())
						.remark(process.getRemark())
						.stop(process.getStop())
						.updated(process.getUpdateTime())
						.build());
			});
		});
		return R.ok(formGroupVos);
	}

}
