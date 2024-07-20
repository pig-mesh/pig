package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.entity.SysSystemConfigEntity;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * SYS 系统配置服务
 *
 * @author lengleng
 * @date 2024/07/18
 */
public interface SysSystemConfigService extends IService<SysSystemConfigEntity> {

    /**
     * 列出系统配置
     *
     * @param query 查询
     * @return {@link R }
     */
    R listSystemConfig(SysSystemConfigEntity query);

    /**
     * 系统配置
     *
     * @param page            页
     * @param sysSystemConfig sys 系统配置
     * @return {@link R }
     */
    R pageSystemConfig(Page page, SysSystemConfigEntity sysSystemConfig);

    /**
     * 更新系统配置
     *
     * @param sysSystemConfig sys 系统配置
     * @return {@link R }
     */
    R updateSystemConfig(SysSystemConfigEntity sysSystemConfig);
}
