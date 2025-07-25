/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.log.event;

import com.pig4cloud.pig.admin.api.entity.SysLog;
import org.springframework.context.ApplicationEvent;

/**
 * 系统日志事件类
 *
 * @author lengleng
 * @date 2025/05/31
 */
public class SysLogEvent extends ApplicationEvent {

	/**
	 * 构造方法，根据源SysLog对象创建SysLogEvent
	 * @param source 源SysLog对象
	 */
	public SysLogEvent(SysLog source) {
		super(source);
	}

}
