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

package com.pig4cloud.pig.daemon.quartz.exception;

import java.io.Serial;

/**
 * 定时任务异常类
 *
 * @author lengleng
 * @date 2025/05/31
 */
public class TaskException extends Exception {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * 无参构造方法，创建一个TaskException实例
	 */
	public TaskException() {
		super();
	}

	/**
	 * 构造方法，使用指定消息创建TaskException实例
	 * @param msg 异常信息
	 */
	public TaskException(String msg) {
		super(msg);
	}

}
