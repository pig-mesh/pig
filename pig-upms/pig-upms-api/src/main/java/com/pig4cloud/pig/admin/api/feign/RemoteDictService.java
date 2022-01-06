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

package com.pig4cloud.pig.admin.api.feign;

import com.pig4cloud.pig.admin.api.entity.SysDictItem;
import com.pig4cloud.pig.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pig.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * 字典调用
 *
 * @author lengleng
 */
@FeignClient(contextId = "remoteDictService", value = ServiceNameConstants.UMPS_SERVICE)
public interface RemoteDictService {

	/**
	 * @param type 字典类型
	 * @return 返回字典项列表
	 */
	@GetMapping("/dict/type/{type}")
	R<List<SysDictItem>> getDictByType(@PathVariable("type") String type);

}
