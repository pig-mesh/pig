package com.pig4cloud.pigx.flow.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.service.IProcessCopyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 流程抄送控制器
 * <p>
 * 该控制器负责管理流程抄送功能，处理抄送相关的查询操作。
 * 流程抄送是工作流中的重要功能，允许将流程执行情况通知给相关人员，
 * 这些人员无需参与审批，但需要了解流程进展。
 * </p>
 * <p>
 * 主要应用场景：
 * 1. 领导知晓 - 将重要流程的执行情况抄送给相关领导
 * 2. 部门协同 - 将跨部门流程抄送给相关部门负责人
 * 3. 信息共享 - 将流程信息共享给利益相关者
 * </p>
 *
 * @author Vincent
 * @since 2023-05-20
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/processCopy")
public class ProcessCopyController {

	private final IProcessCopyService processCopyService;

	/**
	 * 查询单个抄送记录的详细信息
	 * <p>
	 * 获取指定抄送记录的完整信息，包括抄送的流程信息、抄送时间、
	 * 抄送人、被抄送人等详细数据。用于在抄送列表中查看具体抄送内容。
	 * </p>
	 *
	 * @param id 抄送记录ID
	 * @return R 统一响应对象，包含抄送记录的详细信息
	 */
	@GetMapping("querySingleDetail")
	public R querySingleDetail(long id) {
		return processCopyService.querySingleDetail(id);
	}

}
