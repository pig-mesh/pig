/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pigx.admin.api.feign;

import com.pig4cloud.pigx.admin.api.entity.SysDeptRelation;
import com.pig4cloud.pigx.admin.api.entity.SysRole;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author lengleng
 * @date 2019-09-07
 * <p>
 * 远程数据权限调用接口
 */
@FeignClient(contextId = "remoteDataScopeService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteDataScopeService {

	/**
	 * 通过角色ID 查询角色列表
	 * @param roleIdList 角色ID
	 * @return
	 */
	@PostMapping("/role/getRoleList")
	R<List<SysRole>> getRoleList(@RequestBody List<String> roleIdList);

	/**
	 * 获取子级部门
	 * @param deptId 部门ID
	 * @return
	 */
	@GetMapping("/dept/getDescendantList/{deptId}")
	R<List<SysDeptRelation>> getDescendantList(@PathVariable("deptId") Long deptId);

}
