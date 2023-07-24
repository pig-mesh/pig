package com.pig4cloud.pigx.flow.task.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.service.ICombinationGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 组聚合接口控制器
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/combination/group")
public class CombinationGroupController {

	private final ICombinationGroupService combinationGroupService;

	/**
	 * 查询表单组包含流程
	 * @return 表单组数据
	 */
	@GetMapping("listGroupWithProcess")
	public R listGroupWithProcess(Page page, Long groupId) {
		return combinationGroupService.listGroupWithProcess(page, groupId);
	}

	/**
	 * 查询所有我可以发起的表单组
	 * @return
	 */
	@GetMapping("listCurrentUserStartGroup")
	public R listCurrentUserStartGroup() {
		return combinationGroupService.listCurrentUserStartGroup();
	}

}
