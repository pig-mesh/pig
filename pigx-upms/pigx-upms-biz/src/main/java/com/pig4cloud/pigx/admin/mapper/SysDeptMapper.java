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

package com.pig4cloud.pigx.admin.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.common.data.datascope.PigxBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门管理 Mapper 接口
 *
 * @author lengleng
 * @date 2025/06/27
 */
@Mapper
public interface SysDeptMapper extends PigxBaseMapper<SysDept> {

    /**
     * 根据用户ID和租户ID查询部门列表
     *
     * @param userId   用户ID
     * @param tenantId 租户ID
     * @return 部门列表
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SysDept> listDeptsByUserId(@Param("userId") Long userId, @Param("tenantId") Long tenantId);

}
