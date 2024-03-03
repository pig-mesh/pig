package com.pig4cloud.pigx.flow.task.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.constant.enums.YesNoEnum;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.service.PigxUser;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.task.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.task.entity.Process;
import com.pig4cloud.pigx.flow.task.entity.ProcessGroup;
import com.pig4cloud.pigx.flow.task.entity.ProcessStarter;
import com.pig4cloud.pigx.flow.task.service.ICombinationGroupService;
import com.pig4cloud.pigx.flow.task.service.IProcessGroupService;
import com.pig4cloud.pigx.flow.task.service.IProcessService;
import com.pig4cloud.pigx.flow.task.service.IProcessStarterService;
import com.pig4cloud.pigx.flow.task.vo.FormGroupVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CombinationGroupServiceImpl implements ICombinationGroupService {

	private final IProcessGroupService processGroupService;

	private final IProcessService processService;

	private final IProcessStarterService processStarterService;

	/**
	 * 查询表单组包含流程
	 * @param groupId groupId
	 * @return 表单组数据
	 */
	@Override
	public R listGroupWithProcess(Page page, Long groupId) {
		return R.ok(processService.lambdaQuery()
			.eq(Process::getGroupId, groupId)
			.eq(Process::getHidden, false)
			.orderByAsc(Process::getSort)
			.orderByDesc(Process::getCreateTime)
			.page(page));
	}

	/**
	 * 查询所有我可以发起的表单组
	 * @return
	 */
	@Override
	public R listCurrentUserStartGroup() {
		PigxUser user = SecurityUtils.getUser();

		List<FormGroupVo> formGroupVos = new LinkedList<>();

		List<ProcessGroup> processGroupList = processGroupService.lambdaQuery()
			.orderByAsc(ProcessGroup::getSort)
			.list();

		processGroupList.forEach(group -> {
			FormGroupVo formGroupVo = FormGroupVo.builder()
				.id(group.getId())
				.name(group.getGroupName())
				.items(new LinkedList<>())
				.build();
			formGroupVos.add(formGroupVo);

			List<Process> processList = processService.lambdaQuery()
				.eq(Process::getGroupId, group.getId())
				.eq(Process::getHidden, YesNoEnum.NO.getCode())
				.eq(Process::getStop, YesNoEnum.NO.getCode())
				.orderByAsc(Process::getSort)
				.list();

			Map<Long, Boolean> existMap = new HashMap<>();

			if (!processList.isEmpty()) {
				List<Long> idList = processList.stream().map(Process::getId).collect(Collectors.toList());
				// 查询发起人集合
				List<ProcessStarter> processStarterList = processStarterService.lambdaQuery()
					.in(ProcessStarter::getProcessId, idList)
					.list();
				Map<Long, List<ProcessStarter>> groupmap = processStarterList.stream()
					.collect(Collectors.groupingBy(ProcessStarter::getProcessId));

				for (Process process : processList) {
					List<ProcessStarter> processStarters = groupmap.get(process.getId());
					if (processStarters == null) {
						existMap.put(process.getId(), true);
						continue;
					}
					boolean match = processStarters.stream()
						.anyMatch(w -> w.getTypeId().longValue() == user.getId()
								&& w.getType().equals(NodeUserTypeEnum.USER.getKey()));
					if (match) {
						existMap.put(process.getId(), true);
						continue;
					}
					Set<Long> deptIdSet = processStarters.stream()
						.filter(w -> w.getType().equals(NodeUserTypeEnum.DEPT.getKey()))
						.map(w -> w.getTypeId())
						.collect(Collectors.toSet());

					existMap.put(process.getId(), deptIdSet.contains(user.getDeptId()));

				}

			}

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
