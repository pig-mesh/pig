package com.pig4cloud.pigx.flow.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.entity.ProcessGroup;
import com.pig4cloud.pigx.flow.service.IProcessGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 流程分组控制器：提供流程分组的增删查等操作
 *
 * @author lengleng
 * @date 2025/07/14
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("processGroup")
public class ProcessGroupController {

	private final IProcessGroupService processGroupService;

	/**
	 * 查询流程组列表
	 * @return 流程组列表
	 */
	@GetMapping("list")
	public R<List<ProcessGroup>> queryList() {
		return processGroupService.queryList();
	}

	/**
	 * 创建流程分组
	 * @param processGroup 流程分组实体对象
	 * @return 操作结果
	 */
	@PostMapping("create")
	public R create(@RequestBody ProcessGroup processGroup) {
		return processGroupService.create(processGroup);
	}

	/**
	 * 删除分组
	 * @param id 分组ID
	 * @return 操作结果
	 */
	@DeleteMapping("delete/{id}")
	public R delete(@PathVariable long id) {
		return processGroupService.delete(id);
	}

}
