package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.entity.SysTenantMenu;

public interface SysTenantMenuService extends IService<SysTenantMenu> {

	Boolean saveTenant(SysTenantMenu sysTenantMenu);

}
