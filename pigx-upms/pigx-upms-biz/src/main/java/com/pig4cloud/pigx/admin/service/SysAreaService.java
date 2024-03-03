package com.pig4cloud.pigx.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.entity.SysAreaEntity;

import java.util.List;

public interface SysAreaService extends IService<SysAreaEntity> {

    /**
     * 查询行政区划树
     *
     * @param sysArea 查询条件
     * @return 树
     */
    List<Tree<Long>> selectTree(SysAreaEntity sysArea);

    /**
     * 分页查询
     *
     * @param page    分页对象
     * @param sysArea 行政区划
     * @return Page
     */
    Page selectPage(Page page, SysAreaEntity sysArea);
}
