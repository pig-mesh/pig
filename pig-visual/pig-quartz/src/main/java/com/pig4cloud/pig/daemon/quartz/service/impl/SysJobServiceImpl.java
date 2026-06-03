/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
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

package com.pig4cloud.pig.daemon.quartz.service.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.daemon.quartz.constants.JobTypeQuartzEnum;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import com.pig4cloud.pig.daemon.quartz.mapper.SysJobMapper;
import com.pig4cloud.pig.daemon.quartz.service.SysJobService;
import com.pig4cloud.pig.daemon.quartz.util.JobSecurityValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 定时任务调度表
 *
 * @author frwcloud
 * @date 2019-01-27 10:04:42
 */
@Slf4j
@Service
@AllArgsConstructor
public class SysJobServiceImpl extends ServiceImpl<SysJobMapper, SysJob> implements SysJobService {

    private final JobSecurityValidator jobSecurityValidator;

    /**
     * 检查任务配置
     *
     * @param field
     * @param sysJob sys 作业
     * @return {@link R }
     */
    @Override
    public R checkJob(String field, SysJob sysJob) {
        // Security validation for reflection-based job types
        // 对基于反射的任务类型进行安全验证
        if (JobTypeQuartzEnum.SPRING_BEAN.getType().equals(sysJob.getJobType()) ||
            JobTypeQuartzEnum.JAVA.getType().equals(sysJob.getJobType())) {

            // Comprehensive security check
            // 全面的安全检查
            String securityError = jobSecurityValidator.validateJobConfig(
                    sysJob.getClassName(),
                    sysJob.getMethodName(),
                    sysJob.getMethodParamsValue()
            );

            if (securityError != null) {
                log.error("Security validation failed for job: {}, error: {}", sysJob.getJobName(), securityError);
                return R.ok(securityError);
            }
        }

        // 如果是 spring bean 类型任务
        if (JobTypeQuartzEnum.SPRING_BEAN.getType().equals(sysJob.getJobType())) {

            R<?> ok = checkSpringBeanType(field, sysJob);
            if (ok != null) return ok;
        }

        if (JobTypeQuartzEnum.JAVA.getType().equals(sysJob.getJobType())) {

            R<?> ok = checkClassType(field, sysJob);
            if (ok != null) return ok;
        }
        return R.ok();
    }

    /**
     * 检查类类型
     *
     * @param field  字段
     * @param sysJob sys 作业
     * @return {@link R }<{@link ? }>
     */
    @Nullable
    private static R<?> checkClassType(String field, SysJob sysJob) {
        if (StrUtil.isEmpty(sysJob.getClassName())) {
            return R.ok("Class方式，类名称必须填写");
        }

        if (SysJob.Fields.className.equals(field)) {
            try {
                Class.forName(sysJob.getClassName());
                return R.ok();
            } catch (ClassNotFoundException e) {
                return R.ok("当前服务中，不包含此类路径");
            }
        }

        Class<Object> objectClass = ClassUtil.loadClass(sysJob.getClassName());
        if (SysJob.Fields.methodName.equals(field)) {
            Method method = ReflectUtil.getMethodByName(objectClass, sysJob.getMethodName());
            if (Objects.isNull(method)) {
                return R.ok("当前Class 未找到此方法");
            }
        }

        Method method = ReflectUtil.getMethodByName(objectClass, sysJob.getMethodName());
        if (StrUtil.isNotBlank(sysJob.getMethodParamsValue())) {
            if (method.getParameterCount() == 0) {
                return R.ok("当前方法不需要参数，请清空参数值");
            }
        } else if (method.getParameterCount() != 0) {
            return R.ok("当前方法需要参数，请填写 【参数值】字段");
        }
        return null;
    }

    /**
     * 检查 Spring Bean 类型
     *
     * @param field  字段
     * @param sysJob sys 作业
     * @return {@link R }<{@link ? }>
     */
    @Nullable
    private static R<?> checkSpringBeanType(String field, SysJob sysJob) {
        if (StrUtil.isEmpty(sysJob.getClassName())) {
            return R.ok("spring bean方式，Bean名称必须填写");
        }


        if (SysJob.Fields.className.equals(field)) {
            if (SpringUtil.getApplicationContext().containsBean(sysJob.getClassName())) {
                return R.ok();
            }
            return R.ok("当前服务未找到此Bean");
        }


        if (SysJob.Fields.methodName.equals(field)) {
            Method method = ReflectUtil.getMethodByName(SpringUtil.getBean(sysJob.getClassName()).getClass(), sysJob.getMethodName());
            if (Objects.isNull(method)) {
                return R.ok("当前Bean未找到此方法");
            }
            return R.ok();
        }


        Method method = ReflectUtil.getMethodByName(SpringUtil.getBean(sysJob.getClassName()).getClass(), sysJob.getMethodName());
        if (StrUtil.isNotBlank(sysJob.getMethodParamsValue())) {
            if (method.getParameterCount() == 0) {
                return R.ok("当前方法不需要参数，请清空参数值");
            }
        } else if (method.getParameterCount() != 0) {
            return R.ok("当前方法需要参数，请填写 【参数值】字段");
        }
        return null;
    }
}
