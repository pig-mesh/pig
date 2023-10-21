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

package com.pig4cloud.pigx.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.dto.SysFileGroupDTO;
import com.pig4cloud.pigx.admin.api.entity.SysFile;
import com.pig4cloud.pigx.admin.api.entity.SysFileGroup;
import com.pig4cloud.pigx.common.core.util.R;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件管理
 *
 * @author Luckly
 * @date 2019-06-18 17:18:42
 */
public interface SysFileService extends IService<SysFile> {

	/**
	 * 上传文件
	 * @param file 文件流
	 * @param dir 文件夹
	 * @param groupId 分组ID
	 * @param type 类型
	 * @return
	 */
	R uploadFile(MultipartFile file, String dir, Long groupId, String type);

	/**
	 * 读取文件
	 * @param fileName 文件名称
	 * @param response 输出流
	 */
	void getFile(String fileName, HttpServletResponse response);

	/**
	 * 删除文件
	 * @param id
	 * @return
	 */
	Boolean deleteFile(Long id);

	/**
	 * 查询文件组列表
	 * @param fileGroup SysFileGroup对象，用于筛选条件
	 * @return 包含文件组列表的Tree对象列表
	 */
	List<Tree<Long>> listFileGroup(SysFileGroup fileGroup);

	/**
	 * 添加或更新文件组
	 * @param fileGroup SysFileGroup对象，要添加或更新的文件组信息
	 * @return 添加或更新成功返回true，否则返回false
	 */
	Boolean saveOrUpdateGroup(SysFileGroup fileGroup);

	/**
	 * 删除文件组
	 * @param id 待删除文件组的ID
	 * @return 删除成功返回true，否则返回false
	 */
	Boolean deleteGroup(Long id);

	/**
	 * 移动文件组
	 * @param fileGroupDTO SysFileGroupDTO对象，要移动的文件组信息
	 * @return 移动成功返回true，否则返回false
	 */
	Boolean moveFileGroup(SysFileGroupDTO fileGroupDTO);

}
