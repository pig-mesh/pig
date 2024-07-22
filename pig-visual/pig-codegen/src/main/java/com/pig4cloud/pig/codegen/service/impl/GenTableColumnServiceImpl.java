package com.pig4cloud.pig.codegen.service.impl;

import cn.hutool.core.text.NamingCase;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pig.codegen.entity.GenFieldType;
import com.pig4cloud.pig.codegen.entity.GenTableColumnEntity;
import com.pig4cloud.pig.codegen.mapper.GenFieldTypeMapper;
import com.pig4cloud.pig.codegen.mapper.GenTableColumnMapper;
import com.pig4cloud.pig.codegen.service.GenTableColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 表字段信息管理
 *
 * @author lengleng
 * @date 2020/5/18
 */
@Service
@RequiredArgsConstructor
public class GenTableColumnServiceImpl extends ServiceImpl<GenTableColumnMapper, GenTableColumnEntity>
		implements GenTableColumnService {

	private final GenFieldTypeMapper fieldTypeMapper;

	/**
	 * 初始化表单字段列表，主要是将数据库表中的字段转化为表单需要的字段数据格式，并为审计字段排序
	 * @param tableFieldList 表单字段列表
	 */
	public void initFieldList(List<GenTableColumnEntity> tableFieldList) {
		// 字段类型、属性类型映射
		List<GenFieldType> list = fieldTypeMapper.selectList(Wrappers.emptyWrapper());
		Map<String, GenFieldType> fieldTypeMap = new LinkedHashMap<>(list.size());
		list.forEach(
				fieldTypeMapping -> fieldTypeMap.put(fieldTypeMapping.getColumnType().toLowerCase(), fieldTypeMapping));

		// 索引计数器
		AtomicInteger index = new AtomicInteger(0);
		tableFieldList.forEach(field -> {
			// 将字段名转化为驼峰格式
			field.setAttrName(NamingCase.toCamelCase(field.getFieldName()));

			// 获取字段对应的类型
			GenFieldType fieldTypeMapping = fieldTypeMap.getOrDefault(field.getFieldType().toLowerCase(), null);
			if (fieldTypeMapping == null) {
				// 没找到对应的类型，则为Object类型
				field.setAttrType("Object");
			}
			else {
				field.setAttrType(fieldTypeMapping.getAttrType());
				field.setPackageName(fieldTypeMapping.getPackageName());
			}

			// 设置查询类型和表单查询类型都为“=”
			field.setQueryType("=");
			field.setQueryFormType("text");

			// 设置表单类型为文本框类型
			field.setFormType("text");

			// 保证审计字段最后显示
			field.setSort(Objects.isNull(field.getSort()) ? index.getAndIncrement() : field.getSort());
		});
	}

	/**
	 * 更新指定数据源和表名的表单字段信息
	 * @param dsName 数据源名称
	 * @param tableName 表名
	 * @param tableFieldList 表单字段列表
	 */
	@Override

	public void updateTableField(String dsName, String tableName, List<GenTableColumnEntity> tableFieldList) {
		AtomicInteger sort = new AtomicInteger();
		this.updateBatchById(tableFieldList.stream()
			.peek(field -> field.setSort(sort.getAndIncrement()))
			.collect(Collectors.toList()));
	}

}
