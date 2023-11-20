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

import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author lengleng
 * @date 2018/7/29
 */
public interface GeneratorService {

	/**
	 * 生成代码zip写出
	 * @param tableId 表
	 * @param zip 输出流
	 */
	void downloadCode(Long tableId, ZipOutputStream zip);

	/**
	 * 预览代码
	 * @param tableId 表
	 * @return [{模板名称:渲染结果}]
	 */
	List<Map<String, String>> preview(Long tableId);

	/**
	 * 目标目录写入渲染结果
	 * @param tableId 表
	 */
	void generatorCode(Long tableId);

}
