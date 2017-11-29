package com.github.pig.admin.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.entity.SysDict;
import com.github.pig.admin.service.SysDictService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2017-11-19
 */
@RestController
@RequestMapping("/dict")
public class DictController extends BaseController {
    @Autowired
    private SysDictService sysDictService;

    /**
     * 通过ID查询字典信息
     *
     * @param id ID
     * @return 字典信息
     */
    @GetMapping("/{id}")
    public SysDict dict(@PathVariable Integer id) {
        return sysDictService.selectById(id);
    }

    /**
     * 分页查询字典信息
     *
     * @param page  分页对象
     * @param limit 每页限制
     * @return 分页对象
     */
    @RequestMapping("/dictPage")
    public Page dictPage(Integer page, Integer limit) {
        SysDict condition = new SysDict();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        EntityWrapper wrapper = new EntityWrapper(condition);
        wrapper.orderBy("createTime", false);
        return sysDictService.selectPage(new Page<>(page, limit), wrapper);
    }

    /**
     * 通过字典类型查找字典
     *
     * @param type 类型
     * @return 同类型字典
     */
    @GetMapping("/type/{type}")
    @Cacheable(value = "dict_details", key = "#type")
    public List<SysDict> findDictByType(@PathVariable String type) {
        SysDict condition = new SysDict();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        condition.setType(type);
        return sysDictService.selectList(new EntityWrapper<>(condition));
    }

}
