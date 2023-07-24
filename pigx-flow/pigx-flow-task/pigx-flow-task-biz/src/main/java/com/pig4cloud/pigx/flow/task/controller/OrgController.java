package com.pig4cloud.pigx.flow.task.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.service.IOrgService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : willian fu
 * @date : 2022/6/27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/org")
public class OrgController {

	private final IOrgService orgService;

	/**
	 * 查询组织架构树
	 * @param deptId 部门id
	 * @param showLeave 是否显示离职员工
	 * @return 组织架构树数据
	 */
	@GetMapping("tree")
	public R getOrgTreeData(@RequestParam(defaultValue = "0") Long deptId, String type,
			@RequestParam(defaultValue = "false") Boolean showLeave) {
		return orgService.getOrgTreeData(deptId, type, showLeave);
	}

	/**
	 * 模糊搜索用户
	 * @param userName 用户名/拼音/首字母
	 * @return 匹配到的用户
	 */
	@GetMapping("tree/user/search")
	public R getOrgTreeUser(@RequestParam String userName) {
		return orgService.getOrgTreeUser(userName.trim());
	}

}
