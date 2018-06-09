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

package com.github.pig.admin.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.github.pig.admin.model.entity.SysRole;
import com.github.pig.common.util.Query;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 查询角色列表含有部门信息
     * @param query 查询对象
     * @param condition 条件
     * @return List
     */
    List<Object> selectRolePage(Query<Object> query, Map<String, Object> condition);

    /**
     * 通过部门ID查询角色列表
     *
     * @param deptId 部门ID
     * @return 角色列表
     */
    List<SysRole> selectListByDeptId(Integer deptId);
}