package com.github.pig.admin.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.service.SysLogService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.entity.SysLog;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.UserUtils;
import com.github.pig.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

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
        sysLog.setCreateBy(UserUtils.getUserName());
        return sysLogService.insert(sysLog);
    }

    /**
     * 分页查询日志信息
     *
     * @param params 分页对象
     * @return 分页对象
     */
    @RequestMapping("/logPage")
    public Page logPage(@RequestParam Map<String, Object> params) {
        params.put(CommonConstant.DEL_FLAG, CommonConstant.STATUS_NORMAL);
        return sysLogService.selectPage(new Query<>(params),new EntityWrapper<>());
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
