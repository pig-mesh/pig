package com.pig4cloud.pigx.app.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.app.api.entity.AppContactsEntity;
import com.pig4cloud.pigx.app.mapper.AppContactsMapper;
import com.pig4cloud.pigx.app.service.AppContactsService;
import com.pig4cloud.pigx.common.data.datascope.DataScope;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import org.springframework.stereotype.Service;

/**
 * 通讯录服务实现类
 * <p>
 * 通讯录列表会结合系统数据权限，只返回当前登录用户可见范围内的数据。
 *
 * @author lengleng
 * @date 2025/05/29
 */
@Service
public class AppContactsServiceImpl extends ServiceImpl<AppContactsMapper, AppContactsEntity> implements AppContactsService {
    /**
     * 根据查询条件分页查询通讯录，并应用数据权限范围。
     *
     * @param page        分页对象
     * @param appContacts 应用联系人实体
     * @return 分页范围对象
     */
    @Override
    public Page pageScope(Page page, AppContactsEntity appContacts) {
        LambdaQueryWrapper<AppContactsEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(appContacts.getContactName()), AppContactsEntity::getContactName, appContacts.getContactName());
        wrapper.like(StrUtil.isNotBlank(appContacts.getContactPhone()), AppContactsEntity::getContactPhone, appContacts.getContactPhone());

        DataScope dataScope = new DataScope();
        dataScope.setUsername(SecurityUtils.getUser().getUsername());
        return baseMapper.selectPageByScope(page, wrapper, dataScope);
    }
}
