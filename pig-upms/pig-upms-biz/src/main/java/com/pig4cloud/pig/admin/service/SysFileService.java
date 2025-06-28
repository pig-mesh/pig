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

package com.pig4cloud.pig.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.entity.SysFile;
import com.pig4cloud.pig.common.core.util.R;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件管理服务接口
 * <p>
 * 提供文件上传、获取、删除等操作
 * </p>
 *
 * @author Luckly
 * @date 2019-06-18 17:18:42
 */
public interface SysFileService extends IService<SysFile> {

	/**
	 * 上传文件
	 * @param file 要上传的文件
	 * @return 包含文件信息的响应结果，失败时返回错误信息
	 */
	R uploadFile(MultipartFile file);

	/**
	 * 从指定存储桶中获取文件并写入HTTP响应流
	 * @param bucket 存储桶名称
	 * @param fileName 文件名
	 * @param response HTTP响应对象
	 */
	void getFile(String bucket, String fileName, HttpServletResponse response);

	/**
	 * 根据ID删除文件
	 * @param id 文件ID
	 * @return 删除是否成功，文件不存在时返回false
	 */
	Boolean removeFile(Long id);

}
