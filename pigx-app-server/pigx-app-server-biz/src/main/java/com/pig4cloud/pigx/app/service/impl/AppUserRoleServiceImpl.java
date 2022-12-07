/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
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
package com.pig4cloud.pigx.app.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.app.api.entity.AppUserRole;
import com.pig4cloud.pigx.app.mapper.AppUserRoleMapper;
import com.pig4cloud.pigx.app.service.AppUserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户角色表
 *
 * @author aeizzz
 * @date 2022-12-07 09:52:03
 */
@Service
public class AppUserRoleServiceImpl extends ServiceImpl<AppUserRoleMapper, AppUserRole> implements AppUserRoleService {

}
