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

package com.pig4cloud.pigx.act.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.act.dto.ProcessDefDTO;

import java.io.InputStream;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/9/25
 */
public interface ProcessService {

	/**
	 * 分页流程列表
	 * @param params
	 * @return
	 */
	IPage<ProcessDefDTO> getProcessByPage(Map<String, Object> params);

	/**
	 * 读取xml/image资源
	 * @param procDefId
	 * @param proInsId
	 * @param resType
	 * @return
	 */
	InputStream readResource(String procDefId, String proInsId, String resType);

	/**
	 * 更新状态
	 * @param status
	 * @param procDefId
	 * @return
	 */
	Boolean updateStatus(String status, String procDefId);

	/**
	 * 删除流程实例
	 * @param deploymentId
	 * @return
	 */
	Boolean removeProcIns(String deploymentId);

	/**
	 * 启动流程、更新请假单状态
	 * @param leaveId
	 * @return
	 */
	Boolean saveStartProcess(Long leaveId);

}
