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
package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.CharUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.constant.TenantStateEnum;
import com.pig4cloud.pigx.admin.api.entity.*;
import com.pig4cloud.pigx.admin.config.ClientDetailsInitRunner;
import com.pig4cloud.pigx.admin.mapper.SysRoleMenuMapper;
import com.pig4cloud.pigx.admin.mapper.SysTenantMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.common.data.datascope.DataScopeTypeEnum;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.common.data.tenant.TenantBroker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 租户
 * <p>
 * mybatis-plus 3.4.3.3 特殊处理 https://github.com/baomidou/mybatis-plus/pull/3592
 *
 * @author lengleng
 * @date 2019-05-15 15:55:41
 */
@Service
@AllArgsConstructor
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements SysTenantService {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final SysOauthClientDetailsService clientServices;

    private final SysUserRoleService userRoleService;

    private final SysRoleMenuMapper roleMenuMapper;

    private final SysDictItemService dictItemService;

    private final SysPublicParamService paramService;

    private final SysUserService userService;

    private final SysRoleService roleService;

    private final SysMenuService menuService;

    private final SysDeptService deptService;

    private final SysDictService dictService;

    private final CacheManager cacheManager;

    /**
     * 获取正常状态租户
     * <p>
     * 1. 状态正常 2. 开始时间小于等于当前时间 3. 结束时间大于等于当前时间
     *
     * @return
     */
    @Override
    @Cacheable(value = CacheConstants.TENANT_DETAILS)
    public List<SysTenant> getNormalTenant() {
        return baseMapper
                .selectList(Wrappers.<SysTenant>lambdaQuery().eq(SysTenant::getStatus, TenantStateEnum.NORMAL.getCode()));
    }

    /**
     * 保存租户
     * <p>
     * 1. 保存租户 2. 初始化权限相关表 - sys_user - sys_role - sys_menu - sys_user_role -
     * sys_role_menu - sys_dict - sys_dict_item - sys_client_details - sys_public_params
     *
     * @param sysTenant 租户实体
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.TENANT_DETAILS)
    public Boolean saveTenant(SysTenant sysTenant) {
        this.save(sysTenant);
        // 查询系统默认租户配置参数
        TenantDefaultConfig tenantDefault = new TenantDefaultConfig();

        List<SysDict> dictList = new ArrayList<>(32);
        List<Long> dictIdList = new ArrayList<>(32);
        List<SysDictItem> dictItemList = new ArrayList<>(64);
        List<SysMenu> menuList = new ArrayList<>(128);
        List<SysOauthClientDetails> clientDetailsList = new ArrayList<>(16);
        List<SysPublicParam> publicParamList = new ArrayList<>(64);

        TenantBroker.runAs(tenantDefault.getTenantDefaultId(), (id) -> {
            // 查询系统内置字典
            dictList.addAll(dictService.list());
            // 查询系统内置字典项目
            dictIdList.addAll(dictList.stream().map(SysDict::getId).toList());
            dictItemList.addAll(
                    dictItemService.list(Wrappers.<SysDictItem>lambdaQuery().in(SysDictItem::getDictId, dictIdList)));
            List<SysMenu> newMenuList = menuService.list(Wrappers.<SysMenu>lambdaQuery()
                    .in(SysMenu::getMenuId, StrUtil.split(sysTenant.getMenuId(), CharUtil.COMMA)));
            // 查询当前租户菜单
            menuList.addAll(newMenuList);
            // 查询客户端配置
            clientDetailsList.addAll(clientServices.list());
            // 查询系统参数配置
            publicParamList.addAll(paramService.list());
        });

        // 保证插入租户为新的租户
        TenantBroker.applyAs(sysTenant.getId(), (id -> {
            // 插入部门
            SysDept dept = new SysDept();
            dept.setName(tenantDefault.getTenantDefaultDeptname());
            dept.setParentId(0L);
            deptService.save(dept);
            // 构造初始化用户
            SysUser user = new SysUser();
            user.setUsername(tenantDefault.getTenantDefaultUsername());
            user.setPasswordModifyTime(LocalDateTime.now());
            user.setPassword(ENCODER.encode(tenantDefault.getTenantDefaultPassword()));
            user.setDeptId(dept.getDeptId());
            userService.save(user);

            // 构造普通用户角色
            SysRole roleDefault = new SysRole();
            roleDefault.setRoleCode(tenantDefault.getTenantDefaultRolecode());
            roleDefault.setRoleName(tenantDefault.getTenantDefaultRolename());
            roleDefault.setDsType(DataScopeTypeEnum.SELF_LEVEL.getType());
            roleService.save(roleDefault);

            // 构造新角色 管理员角色
            SysRole role = new SysRole();
            role.setRoleCode(tenantDefault.getTenantDefaultRolecode());
            role.setRoleName(tenantDefault.getTenantDefaultRolename());
            role.setDsType(DataScopeTypeEnum.ALL.getType());
            roleService.save(role);
            // 用户角色关系
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(role.getRoleId());
            userRoleService.save(userRole);
            // 插入新的菜单
            saveTenantMenu(menuList, CommonConstants.MENU_TREE_ROOT_ID, CommonConstants.MENU_TREE_ROOT_ID);

            // 重新查询出所有的菜单关联角色
            List<SysMenu> list = menuService.list();

            // 查询全部菜单,构造角色菜单关系
            List<SysRoleMenu> roleMenuList = list.stream().map(menu -> {
                SysRoleMenu roleMenu = new SysRoleMenu();
                roleMenu.setRoleId(role.getRoleId());
                roleMenu.setMenuId(menu.getMenuId());
                return roleMenu;
            }).toList();
            roleMenuList.forEach(roleMenuMapper::insert);
            // 插入系统字典
            dictService.saveBatch(dictList.stream().peek(d -> d.setId(null)).collect(Collectors.toList()));
            // 处理字典项最新关联的字典ID
            List<SysDictItem> itemList = dictList.stream()
                    .flatMap(dict -> dictItemList.stream()
                            .filter(item -> item.getDictType().equals(dict.getDictType()))
                            .peek(item -> {
                                item.setDictId(dict.getId());
                                item.setId(null);
                            }))
                    .collect(Collectors.toList());

            // 插入客户端
            clientServices.saveBatch(clientDetailsList.stream().peek(d -> d.setId(null)).collect(Collectors.toList()));
            // 插入系统配置
            paramService
                    .saveBatch(publicParamList.stream().peek(d -> d.setPublicId(null)).collect(Collectors.toList()));
            return dictItemService.saveBatch(itemList);
        }));

        SpringContextHolder.publishEvent(new ClientDetailsInitRunner.ClientDetailsInitEvent(sysTenant));

        return Boolean.TRUE;
    }

    /**
     * 修改租户
     *
     * @param tenantDTO 新旧套餐数据
     * @return true/false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
    public Boolean updateTenant(SysTenant tenantDTO) {
        SysTenant oldTenant = baseMapper.selectById(tenantDTO.getId());
        List<String> newMenuIdList = StrUtil.split(tenantDTO.getMenuId(), CharUtil.COMMA);
        List<String> oldMenuIdList = StrUtil.split(oldTenant.getMenuId(), CharUtil.COMMA);

        // 更新租户信息
        updateById(tenantDTO);

        // 如果没有修改租户套餐
        TenantDefaultConfig tenantDefault = new TenantDefaultConfig();
        List<SysMenu> sysMenuList = TenantBroker.applyAs(tenantDefault.getTenantDefaultId(), id -> menuService.list());
        if (tenantDefault.getTenantDefaultId().equals(tenantDTO.getId())) {
            return Boolean.TRUE;
        }

        // 比较新旧套餐是否一致
        if (CollUtil.isEqualList(newMenuIdList, oldMenuIdList)) {
            return Boolean.TRUE;
        }

        // 0. 计算两个差集，差集表示变化的功能项
        Collection<String> disjunctionList = CollUtil.disjunction(newMenuIdList, oldMenuIdList);
        // 1. 如果旧套餐包含差集元素则删除
        CollUtil.intersection(oldMenuIdList, disjunctionList)
                .forEach(menuId -> TenantBroker.runAs(tenantDTO.getId(), (tenantId -> {
                    // 查询租户菜单元信息
                    SysMenu menu = CollUtil.findOneByField(sysMenuList, SysMenu.Fields.menuId, Long.parseLong(menuId));

                    // 根据path 或者 permisson 删除目标租户菜单
                    menuService.remove(Wrappers.<SysMenu>lambdaQuery()
                            .eq(StrUtil.isNotBlank(menu.getPath()), SysMenu::getPath, menu.getPath())
                            .or()
                            .eq(StrUtil.isNotBlank(menu.getPermission()), SysMenu::getPermission, menu.getPermission()));
                })));

        // 2. 如果旧套餐不包含差集元素则新增
        List<SysMenu> newTenantMenuIdList = CollUtil.subtract(disjunctionList, oldMenuIdList)
                .stream()
                .map(menuId -> TenantBroker.applyAs(tenantDTO.getId(), (tenantId -> {
                    // 查询租户菜单元信息
                    SysMenu menu = CollUtil.findOneByField(sysMenuList, SysMenu.Fields.menuId, Long.parseLong(menuId));

                    // 新增租户菜单，但未维护上下级关系
                    SysMenu newMenu = new SysMenu();
                    BeanUtils.copyProperties(menu, newMenu, SysMenu.Fields.menuId);
                    menuService.save(newMenu);
                    return newMenu;
                })))
                .toList();

        // 3. 更新新增菜单上下级关系
        for (SysMenu tenantMenu : newTenantMenuIdList) {
            // 查询租户菜单父元信息
            SysMenu parentMenu = CollUtil.findOneByField(sysMenuList, SysMenu.Fields.menuId, tenantMenu.getParentId());
            // 查询目标租户父菜单元信息
            TenantBroker.runAs(tenantDTO.getId(), tenantId -> {
                // 如何菜单父节点是根节点则直接插入，不要查询父节点判断
                if (Objects.equals(tenantMenu.getParentId(), CommonConstants.MENU_TREE_ROOT_ID)) {
                    menuService.updateById(tenantMenu);
                    return;
                }

                // 根据path 或者 permisson 查询目标父菜单
                SysMenu tenantParentMenu = menuService.getOne(Wrappers.<SysMenu>lambdaQuery()
                                .eq(StrUtil.isNotBlank(parentMenu.getPath()), SysMenu::getPath, parentMenu.getPath())
                                .or()
                                .eq(StrUtil.isNotBlank(parentMenu.getPermission()), SysMenu::getPermission,
                                        parentMenu.getPermission()),
                        false);

                tenantMenu.setParentId(tenantParentMenu.getMenuId());
                menuService.updateById(tenantMenu);
            });
        }

        // 清空菜单权限缓存
        cacheManager.getCache(CacheConstants.MENU_DETAILS).clear();
        return Boolean.TRUE;
    }

    /**
     * 保存新的租户菜单，维护成新的菜单
     *
     * @param menuList       菜单列表
     * @param originParentId 原始上级
     * @param targetParentId 目标上级
     */
    private void saveTenantMenu(List<SysMenu> menuList, Long originParentId, Long targetParentId) {
        menuList.stream().filter(menu -> menu.getParentId().equals(originParentId)).forEach(menu -> {
            // 保存菜单原始menuId， 方便查询子节点使用
            Long originMenuId = menu.getMenuId();
            menu.setMenuId(null);
            menu.setParentId(targetParentId);
            menuService.save(menu);
            // 查找此节点的子节点，然后子节点的重新插入父节点更改为新的menuId
            saveTenantMenu(menuList, originMenuId, menu.getMenuId());
        });
    }

}

/**
 * 租户默认配置
 */
@Data
class TenantDefaultConfig {
    private Long tenantDefaultId = 1L;
    private String tenantDefaultDeptname = "租户默认部门";
    private String tenantDefaultUsername = "admin";
    private String tenantDefaultPassword = "123456";
    private String tenantDefaultRolecode = "ROLE_ADMIN";
    private String tenantDefaultRolename = "租户默认角色";
    private String userDefaultRolecode = "GENERAL_USER";
    private String userDefaultRolename = "普通用户";

    @SneakyThrows
    public TenantDefaultConfig() {
        List<String> args = Arrays.stream(ReflectUtil.getFields(this.getClass()))
                .map(Field::getName).map(StrUtil::toUnderlineCase).map(String::toUpperCase).toList();
        Map<String, Object> paramsMap = ParamResolver.getMap(args.toArray(new String[]{}));
        for (Field field : ReflectUtil.getFields(this.getClass())) {
            String key = StrUtil.toUnderlineCase(field.getName()).toUpperCase();
            if (paramsMap.containsKey(key)) {
                field.set(this, paramsMap.get(key));
            }
        }
    }
}
