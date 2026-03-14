/*
 *
 *      Copyright (c) 2018-2026, lengleng All rights reserved.
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

package com.pig4cloud.pig.admin.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.api.vo.UserVO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户表 Mapper 接口
 *
 * @author lengleng
 * @date 2025/06/30
 */
@Mapper
public interface SysUserMapper extends MPJBaseMapper<SysUser> {

	/**
	 * 通过用户DTO查询用户信息（包含角色信息）
	 * @param userDTO 用户查询DTO
	 * @return 包含角色信息的用户VO
	 */
	@InterceptorIgnore(tenantLine = "true")
	UserVO getUserVo(@Param("query") UserDTO userDTO);

	/**
	 * 分页查询用户信息（含角色）
	 * @param page 分页对象
	 * @param userDTO 用户查询参数
	 * @return 分页用户信息列表
	 */
	IPage<UserVO> getUserVosPage(Page page, @Param("query") UserDTO userDTO);

	/**
	 * 通过ID查询用户信息
	 * @param id 用户ID
	 * @return 用户信息VO对象
	 */
	UserVO getUserVoById(Long id);

	/**
	 * 查询用户列表
	 * @param userDTO 查询条件
	 * @param ids 用户ID数组
	 * @return 用户VO列表
	 */
	List<UserVO> getUserVoList(@Param("query") UserDTO userDTO, @Param("ids") Long[] ids);

}
