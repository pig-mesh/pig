package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.entity.SysAreaEntity;
import com.pig4cloud.pigx.admin.mapper.SysAreaMapper;
import com.pig4cloud.pigx.admin.service.SysAreaService;
import com.pig4cloud.pigx.common.core.constant.enums.YesNoEnum;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 行政区划
 *
 * @author lbw
 * @date 2024-02-16 22:40:06
 */
@Service
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysAreaEntity> implements SysAreaService {
    /**
     * 查询行政区划树
     *
     * @param sysArea 查询条件
     * @return 树
     */
    @Override
    public List<Tree<Long>> selectTree(SysAreaEntity sysArea) {
        List<SysAreaEntity> entityList = baseMapper.selectList(Wrappers.lambdaQuery(sysArea)
                        .eq(SysAreaEntity::getAreaStatus, YesNoEnum.YES.getCode())
                .orderByDesc(SysAreaEntity::getAreaSort));

        List<TreeNode<Long>> nodeList = CollUtil.newArrayList();
        for (SysAreaEntity sysAreaEntity : entityList) {
            TreeNode<Long> treeNode = new TreeNode<>(sysAreaEntity.getAdcode(), sysAreaEntity.getPid()
                    , sysAreaEntity.getName(), -Optional.ofNullable(sysAreaEntity.getAreaSort()).orElse(0L));

            HashMap<String, Object> extraMap = MapUtil.of(SysAreaEntity.Fields.adcode, sysAreaEntity.getAdcode());
            extraMap.put(SysAreaEntity.Fields.hot, sysAreaEntity.getHot());
            treeNode.setExtra(extraMap);
            nodeList.add(treeNode);
        }

        return TreeUtil.build(nodeList, Optional.ofNullable(sysArea.getPid()).orElse(100000L));
    }

    /**
     * 分页查询
     *
     * @param page    分页对象
     * @param sysArea 行政区划
     * @return Page
     */
    @Override
    public Page selectPage(Page page, SysAreaEntity sysArea) {
        LambdaQueryWrapper<SysAreaEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(sysArea.getName()), SysAreaEntity::getName, sysArea.getName());
        wrapper.eq(Objects.nonNull(sysArea.getAdcode()), SysAreaEntity::getPid, sysArea.getAdcode());
        wrapper.orderByAsc(SysAreaEntity::getAreaType);
        wrapper.orderByDesc(SysAreaEntity::getAreaSort);
        return baseMapper.selectPage(page, wrapper);
    }
}
