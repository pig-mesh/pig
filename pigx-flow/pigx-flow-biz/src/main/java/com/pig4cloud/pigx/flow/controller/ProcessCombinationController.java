package com.pig4cloud.pigx.flow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.service.ICombinationGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程组聚合控制器
 * <p>
 * 该控制器负责处理流程组的聚合查询操作，主要功能包括： 1. 查询流程组及其包含的流程列表 2. 查询当前用户有权限发起的流程组
 * </p>
 * <p>
 * 流程组是对多个相关流程的逻辑分组，便于用户快速找到和发起相关流程。 通过该控制器，前端可以展示流程分组导航，提升用户体验。
 * </p>
 *
 * @author pigx
 * @since 2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/combination/group")
public class ProcessCombinationController {

	private final ICombinationGroupService combinationGroupService;

	/**
	 * 分页查询流程组及其包含的流程列表
	 * <p>
	 * 该接口用于获取指定流程组的详细信息，包括组内的所有流程定义。 支持分页查询，适用于流程较多的场景。
	 * </p>
	 * @param page 分页参数对象，包含当前页码、每页大小等信息
	 * @param groupId 流程组ID，用于查询特定组的流程；如果为空则查询所有组
	 * @return R 统一响应对象，包含分页后的流程组及流程列表数据
	 */
	@GetMapping("listGroupWithProcess")
	public R listGroupWithProcess(Page page, @RequestParam(required = false) Long groupId) {
		return combinationGroupService.listGroupWithProcess(page, groupId);
	}

	/**
	 * 查询当前登录用户可以发起的所有流程组
	 * <p>
	 * 该接口根据当前用户的权限，返回其有权限发起流程的所有流程组。 用于在流程发起页面展示用户可用的流程分组，实现权限控制。
	 * </p>
	 * @return R 统一响应对象，包含当前用户可发起的流程组列表
	 */
	@GetMapping("listCurrentUserStartGroup")
	public R listCurrentUserStartGroup() {
		return combinationGroupService.listCurrentUserStartGroup();
	}

}
