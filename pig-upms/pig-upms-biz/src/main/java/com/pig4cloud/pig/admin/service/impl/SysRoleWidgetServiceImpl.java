<<<<<<< HEAD:pig-upms/pig-upms-biz/src/main/java/com/pig4cloud/pig/admin/service/impl/SysRoleWidgetServiceImpl.java
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

package com.pig4cloud.pig.admin.service.impl;
=======
package com.pig4cloud.pig.admin.service.impl;
>>>>>>> c791bc80f (feat(upms): 根据角色动态控制首页 widget 配置):pigx-upms/pigx-upms-biz/src/main/java/com/pig4cloud/pigx/admin/service/impl/SysRoleWidgetServiceImpl.java

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
<<<<<<< HEAD:pig-upms/pig-upms-biz/src/main/java/com/pig4cloud/pig/admin/service/impl/SysRoleWidgetServiceImpl.java
import com.pig4cloud.pig.admin.api.entity.SysRoleWidget;
import com.pig4cloud.pig.admin.mapper.SysRoleWidgetMapper;
import com.pig4cloud.pig.admin.service.SysRoleWidgetService;
=======
import com.pig4cloud.pig.admin.api.entity.SysRoleWidget;
import com.pig4cloud.pig.admin.mapper.SysRoleWidgetMapper;
import com.pig4cloud.pig.admin.service.SysRoleWidgetService;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
>>>>>>> c791bc80f (feat(upms): 根据角色动态控制首页 widget 配置):pigx-upms/pigx-upms-biz/src/main/java/com/pig4cloud/pigx/admin/service/impl/SysRoleWidgetServiceImpl.java
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;

/**
 * 角色首页组件配置 Service 实现
 *
 * @author lengleng
 */
@Service
@AllArgsConstructor
public class SysRoleWidgetServiceImpl extends ServiceImpl<SysRoleWidgetMapper, SysRoleWidget>
        implements SysRoleWidgetService {

    /**
     * 根据角色ID查询widget配置
     * @param roleId 角色ID
     * @return widget配置，无配置时返回 null
     */
    @Override
    public SysRoleWidget getByRoleId(Long roleId) {
        return getOne(Wrappers.<SysRoleWidget>lambdaQuery()
                .eq(SysRoleWidget::getRoleId, roleId));
    }

    /**
     * 根据当前登录用户的角色列表查询widget配置
     * <p>遍历用户所有角色对应的配置，返回 widgetKeys 数量最多的那条，
     * 以保证多角色场景下首页显示最完整的widget集合。</p>
     * @return widget配置，用户无角色或角色均无配置时返回 null
     */
    @Override
    public SysRoleWidget getByCurrentUser() {
        List<Long> roleIds = SecurityUtils.getRoles();
        if (roleIds.isEmpty()) {
            return null;
        }
        // 查询当前用户所有角色的 widget 配置，取 widgetKeys 数量最多的
        return list(Wrappers.<SysRoleWidget>lambdaQuery()
                .in(SysRoleWidget::getRoleId, roleIds))
                .stream()
                .filter(w -> StringUtils.hasText(w.getWidgetKeys()))
                .max(Comparator.comparingInt(
                        w -> w.getWidgetKeys().split(StrUtil.COMMA).length))
                .orElse(null);
    }

    /**
     * 保存或更新角色widget配置
     * <p>若该角色已存在配置则更新，否则新增。</p>
     * @param sysRoleWidget widget配置（roleId 必填）
     * @return 操作成功返回 true
     */
    @Override
    public boolean saveOrUpdateByRoleId(SysRoleWidget sysRoleWidget) {
        SysRoleWidget existing = getOne(Wrappers.<SysRoleWidget>lambdaQuery()
                .eq(SysRoleWidget::getRoleId, sysRoleWidget.getRoleId()));
        if (existing != null) {
            sysRoleWidget.setId(existing.getId());
            return updateById(sysRoleWidget);
        }
        return save(sysRoleWidget);
    }

}
