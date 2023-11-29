/*
 *
 *      Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the pig4cloud.com developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: lengleng (wangiegie@gmail.com)
 *
 */

package com.pig4cloud.pigx.admin.api.feign;

import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lengleng
 * @date 2018/6/22
 */
@FeignClient(contextId = "remoteDeptService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteDeptService {

	/**
	 * 获取所有部门接口
	 * @param parentDeptId 父部门ID
	 * @return R 返回结果对象，包含所有部门信息列表
	 */
	@GetMapping("/dept/list")
	R<List<SysDept>> getAllDept(@RequestParam("parentDeptId") Long parentDeptId);

	/**
	 * 通过部门ID获取负责人列表
	 * @param deptId 部门ID
	 * @return 负责人ID列表
	 */
	@GetMapping("/dept/leader/{deptId}")
	R<List<Long>> getAllDeptLeader(@PathVariable("deptId") Long deptId);

}
