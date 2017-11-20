package com.github.pig.admin.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.service.SysLogService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.entity.SysLog;
import com.github.pig.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 日志表 前端控制器
 * </p>
 *
 * @author lengleng
 * @since 2017-11-20
 */
@RestController
@RequestMapping("/log")
public class LogController extends BaseController {
    @Autowired
    private SysLogService sysLogService;

    @PostMapping
    public Boolean log(@RequestBody SysLog sysLog) {
        sysLog.setCreateBy(getUser());
        return sysLogService.insert(sysLog);
    }

    /**
     * 分页查询日志信息
     *
     * @param page  分页对象
     * @param limit 每页限制
     * @return 分页对象
     */
    @RequestMapping("/logPage")
    public Page logPage(Integer page, Integer limit) {
        SysLog condition = new SysLog();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        EntityWrapper wrapper = new EntityWrapper(condition);
        wrapper.orderBy("createTime", false);
        return sysLogService.selectPage(new Page<>(page, limit), wrapper);
    }

    /**
     * 根据ID
     *
     * @param id ID
     * @return success/false
     */
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Integer id) {
        SysLog sysLog = new SysLog();
        sysLog.setId(id);
        sysLog.setDelFlag(CommonConstant.STATUS_DEL);
        sysLog.setUpdateTime(new Date());
        return sysLogService.updateById(sysLog);
    }
}
