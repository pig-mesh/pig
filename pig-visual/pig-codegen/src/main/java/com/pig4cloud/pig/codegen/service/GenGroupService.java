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

package com.pig4cloud.pig.codegen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.codegen.entity.GenGroupEntity;
import com.pig4cloud.pig.codegen.util.vo.GroupVO;
import com.pig4cloud.pig.codegen.util.vo.TemplateGroupDTO;

/**
 * 模板分组
 *
 * @author PIG
 * @date 2023-02-21 20:01:53
 */
public interface GenGroupService extends IService<GenGroupEntity> {

	void saveGenGroup(TemplateGroupDTO genTemplateGroup);

	/**
	 * 删除分组极其关系
	 * @param ids
	 */
	void delGroupAndTemplate(Long[] ids);

	/**
	 * 查询group数据
	 * @param id
	 */
	GroupVO getGroupVoById(Long id);

	/**
	 * 更新group数据
	 * @param GroupVo
	 */
	void updateGroupAndTemplateById(GroupVO GroupVo);

}
