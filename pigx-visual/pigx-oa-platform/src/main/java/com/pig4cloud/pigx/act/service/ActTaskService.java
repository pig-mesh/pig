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
import com.pig4cloud.pigx.act.dto.CommentDto;
import com.pig4cloud.pigx.act.dto.LeaveBillDto;
import com.pig4cloud.pigx.act.dto.TaskDTO;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author lengleng
 * @date 2018/9/25
 */
public interface ActTaskService {

	/**
	 * 获取用户代办列表
	 * @param params
	 * @param name
	 * @return
	 */
	IPage getTaskByName(Map<String, Object> params, String name);

	/**
	 * 通过任务ID查询任务信息关联申请单信息
	 * @param id
	 * @return
	 */
	LeaveBillDto getTaskById(String id);

	/**
	 * 提交任务
	 * @param leaveBillDto
	 * @return
	 */
	Boolean submitTask(LeaveBillDto leaveBillDto);

	/**
	 * 通过任务ID 查询批注信息
	 * @param taskId 任务ID
	 * @return
	 */
	List<CommentDto> getCommentByTaskId(String taskId);

	/**
	 * 追踪图片节点
	 * @param id
	 * @return
	 */
	InputStream viewByTaskId(String id);

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	void delTasks(String[] ids);

	List<TaskDTO> list(TaskDTO taskDTO);

}
