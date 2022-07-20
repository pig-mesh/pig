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
package com.pig4cloud.pig.codegen.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.codegen.entity.ColumnEntity;
import com.pig4cloud.pig.codegen.entity.GenFormConf;
import com.pig4cloud.pig.codegen.mapper.GenFormConfMapper;
import com.pig4cloud.pig.codegen.mapper.GeneratorMapper;
import com.pig4cloud.pig.codegen.service.GenCodeService;
import com.pig4cloud.pig.codegen.service.GenFormConfService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 表单管理
 *
 * @author lengleng
 * @date 2019-08-12 15:55:35
 */
@Service
@RequiredArgsConstructor
public class GenFormConfServiceImpl extends ServiceImpl<GenFormConfMapper, GenFormConf> implements GenFormConfService {

	private final GeneratorMapper generatorMapper;

	private final GenCodeService avue;

	/**
	 * 1. 根据数据源、表名称，查询已配置表单信息 2. 不存在调用模板生成
	 * @param dsName 数据源ID
	 * @param tableName 表名称
	 * @return
	 */
	@Override
	@SneakyThrows
	public String getForm(String dsName, String tableName) {
		GenFormConf form = getOne(Wrappers.<GenFormConf>lambdaQuery().eq(GenFormConf::getTableName, tableName)
				.orderByDesc(GenFormConf::getCreateTime), false);

		if (form != null) {
			return form.getFormInfo();
		}

		List<Map<String, String>> columns = generatorMapper.queryColumns(tableName, dsName);
		// 设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("file.resource.loader.class", ClasspathResourceLoader.class.getName());
		Velocity.init(prop);
		Template template = Velocity.getTemplate("template/avue/crud.js.vm", CharsetUtil.UTF_8);
		VelocityContext context = new VelocityContext();

		List<ColumnEntity> columnList = new ArrayList<>();
		for (Map<String, String> column : columns) {
			ColumnEntity columnEntity = new ColumnEntity();
			columnEntity.setComments(column.get("columnComment"));
			columnEntity.setLowerAttrName(StringUtils.uncapitalize(avue.columnToJava(column.get("columnName"))));
			columnList.add(columnEntity);
		}
		context.put("columns", columnList);
		StringWriter writer = new StringWriter();
		template.merge(context, writer);
		return StrUtil.trim(StrUtil.removePrefix(writer.toString(), "export const tableOption ="));
	}

}
