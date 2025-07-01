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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.pig4cloud.pigx.admin.api.constant.TenantStateEnum;
import com.pig4cloud.pigx.admin.api.dto.SysTenantUserDTO;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.entity.*;
import com.pig4cloud.pigx.admin.config.ClientDetailsInitRunner;
import com.pig4cloud.pigx.admin.mapper.*;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.common.data.cache.RedisUtils;
import com.pig4cloud.pigx.common.data.datascope.DataScopeTypeEnum;
import com.pig4cloud.pigx.common.data.resolver.ParamResolver;
import com.pig4cloud.pigx.common.data.tenant.TenantBroker;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 租户服务实现类
 * <p>
 * 提供租户相关的业务逻辑实现，包括租户的增删改查及初始化操作
 *
 * @author lengleng
 * @date 2025/06/30
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

    private final SysUserMapper sysUserMapper;
    private final SysTenantUserMapper sysTenantUserMapper;
    private final SysUserPostMapper sysUserPostMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final SysUserDeptMapper sysUserDeptMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysDeptMapper sysDeptMapper;
    private final SysPostMapper sysPostMapper;

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

            // 构造普通用户角色
            SysRole roleDefault = new SysRole();
            roleDefault.setRoleCode(tenantDefault.getUserDefaultRolecode());
            roleDefault.setRoleName(tenantDefault.getUserDefaultRolename());
            roleDefault.setDsType(DataScopeTypeEnum.SELF_LEVEL.getType());
            roleService.save(roleDefault);

            // 构造新角色 管理员角色
            SysRole role = new SysRole();
            role.setRoleCode(tenantDefault.getTenantDefaultRolecode());
            role.setRoleName(tenantDefault.getTenantDefaultRolename());
            role.setDsType(DataScopeTypeEnum.ALL.getType());
            roleService.save(role);
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
            dictService.saveBatch(dictList.stream().peek(d -> d.setId(null)).toList());
            // 处理字典项最新关联的字典ID
            List<SysDictItem> itemList = dictList.stream()
                    .flatMap(dict -> dictItemList.stream()
                            .filter(item -> item.getDictType().equals(dict.getDictType()))
                            .peek(item -> {
                                item.setDictId(dict.getId());
                                item.setId(null);
                            }))
                    .toList();

            // 插入客户端
            clientServices.saveBatch(clientDetailsList.stream().peek(d -> d.setId(null)).toList());
            // 插入系统配置
            paramService
                    .saveBatch(publicParamList.stream().peek(d -> d.setPublicId(null)).toList());
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

        // 清空指定租户的菜单权限缓存
        Set<String> keys = RedisUtils.keys(String.format("%s:%s::*", tenantDTO.getId(), CacheConstants.MENU_DETAILS));
        if (CollUtil.isNotEmpty(keys)) {
            RedisUtils.delete(keys.toArray(new String[0]));
        }
        return Boolean.TRUE;
    }

    /**
     * 获取用户所属租户列表
     *
     * @return 用户所属租户列表
     */
    @Override
    public List<SysTenant> getUserTenant() {
        MPJLambdaWrapper<SysTenant> wrapper = new MPJLambdaWrapper<SysTenant>()
                .selectAll(SysTenant.class)
                .leftJoin(SysTenantUser.class, SysTenantUser::getTenantId, SysTenant::getId)
                .eq(SysTenantUser::getUserId, SecurityUtils.getUser().getId());
        return baseMapper.selectJoinList(wrapper);
    }

    /**
     * 获取用户租户分页信息
     *
     * @param page    分页参数
     * @param userDTO 用户信息
     * @return 用户租户分页结果
     */
    @Override
    public Page getUserTenantPage(Page page, UserDTO userDTO) {
        MPJLambdaWrapper<SysUser> wrapper = new MPJLambdaWrapper<SysUser>()
                .selectAll(SysUser.class)
                .innerJoin(SysTenantUser.class, SysTenantUser::getUserId, SysUser::getUserId)
                .like(StrUtil.isNotBlank(userDTO.getUsername()), SysUser::getUsername, userDTO.getUsername())
                .like(StrUtil.isNotBlank(userDTO.getPhone()), SysUser::getPhone, userDTO.getPhone())
                .like(StrUtil.isNotBlank(userDTO.getEmail()), SysUser::getEmail, userDTO.getEmail())
                .eq(Objects.nonNull(userDTO.getTenantId()), SysTenantUser::getTenantId, userDTO.getTenantId());
        return TenantBroker.noneAs(() -> sysUserMapper.selectJoinPage(page, wrapper));
    }

    /**
     * 移除租户用户
     *
     * @param tenantUserDTO 租户用户信息
     * @return 是否移除成功
     */
    @Override
    public Boolean removeTenantUser(SysTenantUserDTO tenantUserDTO) {
        ArrayList<Long> userIdList = CollUtil.toList(tenantUserDTO.getUserIds());

        // 删除租户用户关系
        sysTenantUserMapper.delete(Wrappers.<SysTenantUser>lambdaQuery()
                .eq(SysTenantUser::getTenantId, tenantUserDTO.getTenantId())
                .in(SysTenantUser::getUserId, userIdList)
        );

        TenantBroker.runAs(tenantUserDTO.getTenantId(), (tenantId -> {
            // 删除用户岗位
            sysUserPostMapper.delete(Wrappers.<SysUserPost>lambdaQuery()
                    .in(SysUserPost::getUserId, userIdList));
            // 删除用户角色
            sysUserRoleMapper.delete(Wrappers.<SysUserRole>lambdaQuery()
                    .in(SysUserRole::getUserId, userIdList));
            // 删除用户部门
            sysUserDeptMapper.delete(Wrappers.<SysUserDept>lambdaQuery()
                    .in(SysUserDept::getUserId, userIdList));
        }));
        return Boolean.TRUE;
    }

    /**
     * 根据用户信息查询租户用户列表
     *
     * @param userDTO 用户信息传输对象
     * @return 租户用户列表
     */
    @Override
    public List<SysUser> listTenantUser(UserDTO userDTO) {
        return TenantBroker.noneAs(() -> sysUserMapper
                .selectList(Wrappers.<SysUser>lambdaQuery().like(StrUtil.isNotBlank(userDTO.getUsername()), SysUser::getUsername, userDTO.getUsername())));
    }

    /**
     * 保存租户用户信息
     *
     * @param tenantUserDTO 租户用户信息DTO
     * @return 保存是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveTenantUser(SysTenantUserDTO tenantUserDTO) {
        TenantBroker.runAs(tenantUserDTO.getTenantId(), (tenantId -> {
            for (Long userId : tenantUserDTO.getUserIds()) {
                List<SysTenantUser> sysTenantUsers = sysTenantUserMapper.selectList(Wrappers.<SysTenantUser>lambdaQuery()
                        .eq(SysTenantUser::getTenantId, tenantId)
                        .eq(SysTenantUser::getUserId, userId));
                if (CollUtil.isEmpty(sysTenantUsers)) {
                    // 插入租户用户关系
                    SysTenantUser sysTenantUser = new SysTenantUser();
                    sysTenantUser.setUserId(userId);
                    sysTenantUser.setTenantId(tenantId);
                    sysTenantUserMapper.insert(sysTenantUser);
                }


                List<SysUserRole> userRoleList = sysUserRoleMapper.selectList(Wrappers.<SysUserRole>lambdaQuery()
                        .eq(SysUserRole::getUserId, userId)
                        .eq(SysUserRole::getRoleId, tenantUserDTO.getRoleId()));
                if (CollUtil.isEmpty(userRoleList)) {
                    // 插入用户角色关系
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(userId);
                    sysUserRole.setRoleId(tenantUserDTO.getRoleId());
                    sysUserRoleMapper.insert(sysUserRole);
                }


                List<SysUserDept> userDeptList = sysUserDeptMapper.selectList(Wrappers.<SysUserDept>lambdaQuery()
                        .eq(SysUserDept::getUserId, userId)
                        .eq(SysUserDept::getDeptId, tenantUserDTO.getDeptId()));

                if (CollUtil.isEmpty(userDeptList)) {
                    // 插入用户部门关系
                    SysUserDept sysUserDept = new SysUserDept();
                    sysUserDept.setUserId(userId);
                    sysUserDept.setDeptId(tenantUserDTO.getDeptId());
                    sysUserDeptMapper.insert(sysUserDept);
                }

                List<SysUserPost> userPostList = sysUserPostMapper.selectList(Wrappers.<SysUserPost>lambdaQuery()
                        .eq(SysUserPost::getUserId, userId)
                        .eq(SysUserPost::getPostId, tenantUserDTO.getPostId()));

                if (CollUtil.isEmpty(userPostList) && Objects.nonNull(tenantUserDTO.getPostId())) {
                    // 插入用户岗位关系
                    SysUserPost sysUserPost = new SysUserPost();
                    sysUserPost.setUserId(userId);
                    sysUserPost.setPostId(tenantUserDTO.getPostId());
                    sysUserPostMapper.insert(sysUserPost);
                }
            }
        }));
        return Boolean.TRUE;
    }

    /**
     * 获取租户角色列表
     *
     * @param userDTO 用户信息
     * @return 租户角色列表
     */
    @Override
    public Map<String, Object> listTenantOrg(UserDTO userDTO) {
        return TenantBroker.applyAs(userDTO.getTenantId(), tenantId -> {
            // 过滤有权限配置的菜单
            List<SysRole> sysRoles = sysRoleMapper.selectList(Wrappers.lambdaQuery()).stream()
                    .filter(role -> roleMenuMapper.exists(Wrappers.<SysRoleMenu>lambdaQuery()
                            .eq(SysRoleMenu::getRoleId, role.getRoleId()))).toList();
            List<SysDept> sysDepts = sysDeptMapper.selectList(Wrappers.lambdaQuery());
            List<SysPost> sysPosts = sysPostMapper.selectList(Wrappers.lambdaQuery());
            return Map.of("sysRoles", sysRoles, "sysDepts", sysDepts, "sysPosts", sysPosts);
        });
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
