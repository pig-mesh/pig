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

package com.pig4cloud.pig.common.core.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lengleng
 * @date 2019-05-16
 * <p>
 * 字典类型
 */
@Getter
@RequiredArgsConstructor
public enum DictTypeEnum {

	/**
	 * 字典类型-系统内置（不可修改）
	 */
	SYSTEM("1", "系统内置"),

	/**
	 * 字典类型-业务类型
	 */
	BIZ("0", "业务类");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

}
