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
import com.github.pig.admin.model.entity.SysMenu;
import com.github.pig.common.vo.MenuVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author lengleng
 * @since 2017-10-29
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 通过角色名查询菜单
     *
     * @param role 角色名称
     * @return 菜单列表
     */
    List<MenuVO> findMenuByRoleName(@Param("role") String role);
}