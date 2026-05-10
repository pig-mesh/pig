package com.pig4cloud.pigx.app.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.app.api.dto.AppRoleTabbarDTO;
import com.pig4cloud.pigx.app.api.entity.AppRoleTabbar;
import com.pig4cloud.pigx.app.mapper.AppRoleTabbarMapper;
import com.pig4cloud.pigx.app.service.AppRoleTabbarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * App 角色底部导航服务实现。
 * <p>
 * 只访问 app-server 本库的 {@code app_role_tabbar}。运行时角色来源由 Spring Security
 * token authorities 提供，避免 app-server 跨库 join upms 的用户角色表。
 *
 * @author lengleng
 * @date 2026-05-09
 */
@Service
@RequiredArgsConstructor
public class AppRoleTabbarServiceImpl extends ServiceImpl<AppRoleTabbarMapper, AppRoleTabbar>
        implements AppRoleTabbarService {

    @Override
    public List<Long> listTabbarIdsByRoleId(Long roleId) {
        return roleId == null ? List.of() : listTabbarIdsByRoleIds(List.of(roleId));
    }

    /**
     * 根据多个角色查询可见 Tabbar。
     * <p>
     * 一个用户可能同时拥有多个角色，因此这里会先对角色 ID 去重，再对查询出的 Tabbar ID 去重。
     *
     * @param roleIds 系统角色 ID 列表
     * @return 去重后的 Tabbar ID 列表
     */
    @Override
    public List<Long> listTabbarIdsByRoleIds(List<Long> roleIds) {
        if (CollUtil.isEmpty(roleIds)) {
            return List.of();
        }
        List<Long> distinctRoleIds = roleIds.stream().filter(Objects::nonNull).distinct().toList();
        if (CollUtil.isEmpty(distinctRoleIds)) {
            return List.of();
        }
        return this.list(Wrappers.<AppRoleTabbar>lambdaQuery().in(AppRoleTabbar::getRoleId, distinctRoleIds))
                .stream()
                .map(AppRoleTabbar::getTabbarId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRoleTabbars(AppRoleTabbarDTO dto) {
        // 覆盖保存：后台每次提交的是该角色完整授权结果，先删旧关系再写新关系。
        this.remove(Wrappers.<AppRoleTabbar>lambdaQuery().eq(AppRoleTabbar::getRoleId, dto.getRoleId()));
        if (CollUtil.isEmpty(dto.getTabbarIds())) {
            return Boolean.TRUE;
        }
        List<AppRoleTabbar> records = dto.getTabbarIds()
                .stream()
                .filter(Objects::nonNull)
                .distinct()
                .map(tabbarId -> {
                    AppRoleTabbar roleTabbar = new AppRoleTabbar();
                    roleTabbar.setRoleId(dto.getRoleId());
                    roleTabbar.setTabbarId(tabbarId);
                    return roleTabbar;
                })
                .toList();
        return this.saveBatch(records);
    }

}
