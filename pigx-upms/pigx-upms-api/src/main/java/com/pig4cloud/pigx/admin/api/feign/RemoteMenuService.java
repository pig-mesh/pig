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

import com.pig4cloud.pigx.admin.api.entity.SysMenu;
import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 远程菜单服务
 *
 * @author lengleng
 * @date 2024/07/16
 */
@FeignClient(contextId = "remoteMenuService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteMenuService {


    /**
     * 获取菜单详细信息
     *
     * @param sysMenu SYS菜单
     * @return {@link R }<{@link List }<{@link SysMenu }>>
     */
    @GetMapping("/menu/details")
    R<List<SysMenu>> getMenuDetails(@SpringQueryMap SysMenu sysMenu);


    /**
     * 保存菜单
     *
     * @param sysMenu SYS菜单
     * @return {@link R }<{@link Boolean }>
     */
    @PostMapping("/menu")
    R<SysMenu> saveMenu(@RequestBody SysMenu sysMenu);
}
