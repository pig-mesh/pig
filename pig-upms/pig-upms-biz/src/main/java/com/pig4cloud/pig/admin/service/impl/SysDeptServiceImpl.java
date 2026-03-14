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

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.pig4cloud.pig.admin.api.constant.OrgTypeEnum;
import com.pig4cloud.pig.admin.api.constant.PostCodeEnum;
import com.pig4cloud.pig.admin.api.constant.UpmsErrorCodes;
import com.pig4cloud.pig.admin.api.entity.*;
import com.pig4cloud.pig.admin.api.utils.DataUtil;
import com.pig4cloud.pig.admin.api.vo.DeptExcelVO;
import com.pig4cloud.pig.admin.api.vo.OrgTreeVO;
import com.pig4cloud.pig.admin.mapper.*;
import com.pig4cloud.pig.admin.service.SysDeptService;
import com.pig4cloud.pig.common.core.util.MsgUtils;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.plugin.excel.vo.ErrorMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.*;

/**
 * <p>
 * 部门管理 服务实现类
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private final SysUserMapper userMapper;

    private final SysDeptMapper deptMapper;

    private final SysPostMapper postMapper;

    private final SysRoleMapper roleMapper;

    private final SysUserDeptMapper userDeptMapper;

    private final SysUserPostMapper userPostMapper;

    /**
     * 删除部门
     *
     * @param id 部门 ID
     * @return 成功、失败
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDeptById(Long id) {
        // 级联删除部门
        List<Long> idList = this.listDescendant(id).stream().map(SysDept::getDeptId).toList();

        Optional.ofNullable(idList).filter(CollUtil::isNotEmpty).ifPresent(this::removeByIds);

        return Boolean.TRUE;
    }

    /**
     * 查询全部部门树
     *
     * @param deptName
     * @param parentId
     * @return 树 部门名称
     */
    @Override
    public List<Tree<Long>> selectTree(String deptName, Long parentId) {
        // 查询全部部门
        List<SysDept> deptAllList = deptMapper
                .selectList(Wrappers.<SysDept>lambdaQuery().like(StrUtil.isNotBlank(deptName), SysDept::getName, deptName));

        // 所有部门均可见
        List<TreeNode<Long>> collect = deptAllList.stream()
                .filter(dept -> dept.getDeptId().intValue() != dept.getParentId())
                .sorted(Comparator.comparingInt(SysDept::getSortOrder))
                .map(dept -> {
                    TreeNode<Long> treeNode = new TreeNode<>();
                    treeNode.setId(dept.getDeptId());
                    treeNode.setParentId(dept.getParentId());
                    treeNode.setName(dept.getName());
                    treeNode.setWeight(dept.getSortOrder());
                    Map<String, Object> extra = new HashMap<>(8);
                    extra.put("isLock", false);
                    extra.put("createTime", dept.getCreateTime());
                    treeNode.setExtra(extra);
                    return treeNode;
                })
                .toList();

        // 模糊查询 不组装树结构 直接返回 表格方便编辑
        if (StrUtil.isNotBlank(deptName)) {
            return collect.stream().map(node -> {
                Tree<Long> tree = new Tree<>();
                tree.putAll(node.getExtra());
                BeanUtils.copyProperties(node, tree);
                return tree;
            }).toList();
        }

        return TreeUtil.build(collect, parentId == null ? 0 : parentId);
    }

    /**
     * 导出部门
     *
     * @return
     */
    @Override
    public List<DeptExcelVO> listExcelVo() {
        List<SysDept> list = this.list();
        List<DeptExcelVO> deptExcelVos = list.stream().map(item -> {
            DeptExcelVO deptExcelVo = new DeptExcelVO();
            deptExcelVo.setName(item.getName());
            Optional<String> first = this.list()
                    .stream()
                    .filter(it -> item.getParentId().equals(it.getDeptId()))
                    .map(SysDept::getName)
                    .findFirst();
            deptExcelVo.setParentName(first.orElse("根部门"));
            deptExcelVo.setSortOrder(item.getSortOrder());
            return deptExcelVo;
        }).toList();
        return deptExcelVos;
    }

    @Override
    public R importDept(List<DeptExcelVO> excelVOList, BindingResult bindingResult) {
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

        List<SysDept> deptList = this.list();
        for (DeptExcelVO item : excelVOList) {
            Set<String> errorMsg = new HashSet<>();
            boolean exsitUsername = deptList.stream().anyMatch(sysDept -> item.getName().equals(sysDept.getName()));
            if (exsitUsername) {
                errorMsg.add("部门名称已经存在");
            }
            SysDept one = this.getOne(Wrappers.<SysDept>lambdaQuery().eq(SysDept::getName, item.getParentName()));
            if (item.getParentName().equals("根部门")) {
                one = new SysDept();
                one.setDeptId(0L);
            }
            if (one == null) {
                errorMsg.add("上级部门不存在");
            }
            if (CollUtil.isEmpty(errorMsg)) {
                SysDept sysDept = new SysDept();
                sysDept.setName(item.getName());
                sysDept.setParentId(one.getDeptId());
                sysDept.setSortOrder(item.getSortOrder());
                baseMapper.insert(sysDept);
            } else {
                // 数据不合法情况
                errorMessageList.add(new ErrorMessage(item.getLineNum(), errorMsg));
            }
        }
        if (CollUtil.isNotEmpty(errorMessageList)) {
            return R.failed(errorMessageList);
        }
        return R.ok(null, MsgUtils.getMessage(UpmsErrorCodes.SYS_DEPT_IMPORT_SUCCEED));
    }

    /**
     * 查询所有子节点 （包含当前节点）
     *
     * @param deptId 部门ID 目标部门ID
     * @return ID
     */
    @Override
    public List<SysDept> listDescendant(Long deptId) {
        // 查询全部部门
        List<SysDept> allDeptList = baseMapper.selectList(Wrappers.emptyWrapper());

        // 递归查询所有子节点
        List<SysDept> resDeptList = new ArrayList<>();
        recursiveDept(allDeptList, deptId, resDeptList);

        // 添加当前节点
        resDeptList.addAll(allDeptList.stream().filter(sysDept -> deptId.equals(sysDept.getDeptId())).toList());
        return resDeptList;
    }

    /**
     * 获取部门负责人
     * <p>
     * 1. 根据dept 查询用户 2. 筛选用户列表中 post
     *
     * @param deptId deptId
     * @return user id list
     */
    @Override
    public List<Long> listDeptLeader(Long deptId) {

        List<SysUserDept> userDeptList = userDeptMapper
                .selectList(Wrappers.<SysUserDept>lambdaQuery().eq(SysUserDept::getDeptId, deptId));
        if (CollUtil.isEmpty(userDeptList)) {
            return null;
        }

        SysPost deptLeader = postMapper
                .selectOne(Wrappers.<SysPost>lambdaQuery().eq(SysPost::getPostCode, PostCodeEnum.TEAM_LEADER.getCode()));
        if (deptLeader == null) {
            return null;
        }

        List<Long> userIdList = userDeptList.stream().map(SysUserDept::getUserId).toList();
        return userPostMapper.selectList(Wrappers.<SysUserPost>lambdaQuery().in(SysUserPost::getUserId, userIdList))
                .stream()
                .filter(post -> Objects.equals(post.getPostId(), deptLeader.getPostId()))
                .map(SysUserPost::getUserId)
                .toList();
    }

    /**
     * 根据父部门ID和类型获取组织树
     *
     * @param parentDeptId 父部门ID
     * @param type         类型
     * @return 组织树信息
     */
    public Map<String, Object> listOrgTree(Long parentDeptId, String type) {

        List<OrgTreeVO> orgs = new LinkedList<>();

        if (StrUtil.equals(type, OrgTypeEnum.ROLE.getType())) {
            // 角色
            List<SysRole> roleList = roleMapper.selectList(Wrappers.emptyWrapper());

            roleList.forEach(role -> {
                OrgTreeVO orgTreeVo = new OrgTreeVO();
                orgTreeVo.setId(role.getRoleId());
                orgTreeVo.setName(role.getRoleName());
                orgTreeVo.setType(OrgTypeEnum.ROLE.getType());
                orgTreeVo.setSelected(false);
                orgs.add(orgTreeVo);
            });

            return Dict.create()
                    .set("roleList", orgs)
                    .set("postList", new ArrayList<>())
                    .set("childDepartments", orgs)
                    .set("employees", new ArrayList<>());
        }

        if (StrUtil.equals(type, OrgTypeEnum.POST.getType())) {
            // 角色
            List<SysPost> postList = postMapper.selectList(Wrappers.emptyWrapper());

            postList.forEach(post -> {
                OrgTreeVO orgTreeVo = new OrgTreeVO();
                orgTreeVo.setId(post.getPostId());
                orgTreeVo.setName(post.getPostName());
                orgTreeVo.setType(OrgTypeEnum.POST.getType());
                orgTreeVo.setSelected(false);
                orgs.add(orgTreeVo);
            });

            return Dict.create()
                    .set("roleList", new ArrayList<>())
                    .set("postList", orgs)
                    .set("childDepartments", orgs)
                    .set("employees", new ArrayList<>());
        }

        Dict dict = Dict.create()
                .set("titleDepartments", new ArrayList<>())
                .set("roleList", new ArrayList<>())
                .set("postList", new ArrayList<>())
                .set("employees", new ArrayList<>());

        List<SysDept> deptList = this.list(
                Wrappers.<SysDept>lambdaQuery().eq(Objects.nonNull(parentDeptId), SysDept::getParentId, parentDeptId));

        // 查询所有部门及员工
        List<OrgTreeVO> deptVoList = new ArrayList<>();
        deptList.forEach(dept -> {
            OrgTreeVO orgTreeVo = new OrgTreeVO();
            orgTreeVo.setId(dept.getDeptId());
            orgTreeVo.setName(dept.getName());
            orgTreeVo.setType(OrgTypeEnum.DEPT.getType());
            orgTreeVo.setSelected(false);
            deptVoList.add(orgTreeVo);
        });
        dict.set("childDepartments", deptVoList);

        if (!StrUtil.equals(type, OrgTypeEnum.DEPT.getType())) {

            List<OrgTreeVO> userVoList = new ArrayList<>();

            MPJLambdaWrapper<SysUser> userQuery = new MPJLambdaWrapper<>();
            userQuery.selectAll(SysUser.class)
                    .leftJoin(SysUserDept.class, SysUserDept::getUserId, SysUser::getUserId)
                    .eq(SysUserDept::getDeptId, parentDeptId);

            List<SysUser> userList = userMapper.selectJoinList(userQuery);

            userList.forEach(user -> {
                OrgTreeVO orgTreeVo = new OrgTreeVO();
                orgTreeVo.setId(user.getUserId());
                orgTreeVo.setName(user.getUsername());
                orgTreeVo.setType(OrgTypeEnum.USER.getType());
                orgTreeVo.setSelected(false);
                orgTreeVo.setAvatar(user.getAvatar());
                userVoList.add(orgTreeVo);
            });
            dict.set("employees", userVoList);
        }

        if (parentDeptId > 0) {
            List<SysDept> allDept = this.list();
            List<SysDept> depts = DataUtil.selectParentByDept(parentDeptId, allDept);
            dict.set("titleDepartments", CollUtil.reverse(depts));
        }

        return dict;
    }

    /**
     * 模糊搜索用户
     *
     * @param username 用户名/拼音/首字母
     * @return 匹配到的用户
     */
    public List<OrgTreeVO> getOrgTreeUser(String username) {
        return userMapper.selectList(Wrappers.<SysUser>lambdaQuery().like(SysUser::getUsername, username))
                .stream()
                .map(user -> {
                    OrgTreeVO orgTreeVo = new OrgTreeVO();
                    orgTreeVo.setId(user.getUserId());
                    orgTreeVo.setName(user.getUsername());
                    orgTreeVo.setType(OrgTypeEnum.USER.getType());
                    orgTreeVo.setSelected(false);
                    orgTreeVo.setAvatar(user.getAvatar());
                    return orgTreeVo;
                })
                .toList();
    }

    /**
     * 递归查询所有子节点。
     *
     * @param allDeptList 所有部门列表
     * @param parentId    父部门ID
     * @param resDeptList 结果集合
     */
    private void recursiveDept(List<SysDept> allDeptList, Long parentId, List<SysDept> resDeptList) {
        // 使用 Stream API 进行筛选和遍历
        allDeptList.stream().filter(sysDept -> sysDept.getParentId().equals(parentId)).forEach(sysDept -> {
            resDeptList.add(sysDept);
            recursiveDept(allDeptList, sysDept.getDeptId(), resDeptList);
        });
    }

}
