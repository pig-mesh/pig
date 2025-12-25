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
package com.pig4cloud.pig.common.core.jackson;

import tools.jackson.core.json.PackageVersion;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

import java.io.Serial;

/**
 * Long类型序列化为字符串模块，用于解决JavaScript中Long精度丢失问题
 *
 * @author lengleng
 * @date 2025/05/30
 */
public class LongToStringModule extends SimpleModule {

	@Serial
	private static final long serialVersionUID = 1L;

	public LongToStringModule() {
		super(PackageVersion.VERSION);
		// Long类型序列化为字符串
		this.addSerializer(Long.class, ToStringSerializer.instance);
		this.addSerializer(Long.TYPE, ToStringSerializer.instance);
	}

}
