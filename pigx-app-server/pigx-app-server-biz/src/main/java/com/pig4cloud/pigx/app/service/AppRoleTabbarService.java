package com.pig4cloud.pigx.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.app.api.dto.AppRoleTabbarDTO;
import com.pig4cloud.pigx.app.api.entity.AppRoleTabbar;

import java.util.List;

/**
 * App 角色底部导航服务，管理系统角色与 App 底部导航的授权关系。
 *
 * @author lengleng
 */
public interface AppRoleTabbarService extends IService<AppRoleTabbar> {

    /**
     * 查询指定角色已授权的底部导航 ID 列表。
     *
     * @param roleId 系统角色 ID
     * @return 底部导航 ID 列表
     */
    List<Long> listTabbarIdsByRoleId(Long roleId);

    /**
     * 查询指定角色列表已授权的底部导航 ID 列表。
     *
     * @param roleIds 系统角色 ID 列表
     * @return 底部导航 ID 列表
     */
    List<Long> listTabbarIdsByRoleIds(List<Long> roleIds);

    /**
     * 保存角色底部导航授权。
     *
     * @param dto 角色底部导航配置
     * @return 操作结果
     */
    Boolean saveRoleTabbars(AppRoleTabbarDTO dto);

}
