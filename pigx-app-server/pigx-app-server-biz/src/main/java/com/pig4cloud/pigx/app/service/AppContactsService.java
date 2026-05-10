package com.pig4cloud.pigx.app.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.app.api.entity.AppContactsEntity;

/**
 * 应用联系人服务接口
 *
 * @author lengleng
 * @date 2025/05/29
 */
public interface AppContactsService extends IService<AppContactsEntity> {

    /**
     * 根据应用联系人实体获取分页范围
     *
     * @param page           分页对象
     * @param contactsEntity 应用联系人实体
     * @return 分页范围对象
     */
    Page pageScope(Page page, AppContactsEntity contactsEntity);
}