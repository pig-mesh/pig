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
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
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

	private static final String DEFAULT_COMPONENT_TYPE = "text";

	private final GenFieldTypeMapper fieldTypeMapper;

	/**
	 * 初始化表单字段列表，主要是将数据库表中的字段转化为表单需要的字段数据格式，并为审计字段排序
	 * @param tableFieldList 表单字段列表
	 */
	public void initFieldList(List<GenTableColumnEntity> tableFieldList) {
		Map<String, GenFieldType> fieldTypeMap = this.buildFieldTypeMap();
		AtomicInteger index = new AtomicInteger(0);
		tableFieldList.forEach(field -> this.initNewField(field, fieldTypeMap, index));
	}

	@Override
	public void syncFieldList(String dsName, String tableName, List<GenTableColumnEntity> tableFieldList) {
		Map<String, GenFieldType> fieldTypeMap = this.buildFieldTypeMap();
		List<GenTableColumnEntity> existingFieldList = this.list(Wrappers.<GenTableColumnEntity>lambdaQuery()
			.eq(GenTableColumnEntity::getDsName, dsName)
			.eq(GenTableColumnEntity::getTableName, tableName)
			.orderByAsc(GenTableColumnEntity::getSort)
			.orderByAsc(GenTableColumnEntity::getId));
		Map<String, GenTableColumnEntity> existingFieldMap = existingFieldList.stream()
			.collect(Collectors.toMap(GenTableColumnEntity::getFieldName, Function.identity(), (left, right) -> left,
					LinkedHashMap::new));
		int nextSort = existingFieldList.stream()
			.map(GenTableColumnEntity::getSort)
			.filter(Objects::nonNull)
			.max(Comparator.naturalOrder())
			.orElse(-1) + 1;
		AtomicInteger sortCounter = new AtomicInteger(nextSort);
		List<GenTableColumnEntity> mergedFieldList = new ArrayList<>(tableFieldList.size());

		tableFieldList.forEach(field -> {
			GenTableColumnEntity existingField = existingFieldMap.remove(field.getFieldName());
			if (Objects.isNull(existingField)) {
				this.initNewField(field, fieldTypeMap, sortCounter);
				mergedFieldList.add(field);
				return;
			}
			this.mergeField(existingField, field, fieldTypeMap, sortCounter);
			mergedFieldList.add(existingField);
		});

		if (!existingFieldMap.isEmpty()) {
			this.removeBatchByIds(existingFieldMap.values().stream().map(GenTableColumnEntity::getId).toList());
		}
		if (!mergedFieldList.isEmpty()) {
			this.saveOrUpdateBatch(mergedFieldList);
		}
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
		this.updateBatchById(tableFieldList.stream().peek(field -> field.setSort(sort.getAndIncrement())).toList());
	}

	private Map<String, GenFieldType> buildFieldTypeMap() {
		List<GenFieldType> list = fieldTypeMapper.selectList(Wrappers.emptyWrapper());
		Map<String, GenFieldType> fieldTypeMap = new LinkedHashMap<>(list.size());
		list.stream()
			.filter(fieldType -> Objects.nonNull(fieldType.getColumnType()))
			.forEach(fieldType -> fieldTypeMap.put(this.normalizeColumnType(fieldType.getColumnType()), fieldType));
		return fieldTypeMap;
	}

	private void initNewField(GenTableColumnEntity field, Map<String, GenFieldType> fieldTypeMap,
			AtomicInteger sortCounter) {
		field.setAttrName(NamingCase.toCamelCase(field.getFieldName()));
		this.applyFieldTypeMapping(field, fieldTypeMap);
		field.setQueryType("=");
		field.setSort(Objects.nonNull(field.getSort()) ? field.getSort() : sortCounter.getAndIncrement());
	}

	private void mergeField(GenTableColumnEntity existingField, GenTableColumnEntity latestField,
			Map<String, GenFieldType> fieldTypeMap, AtomicInteger sortCounter) {
		existingField.setFieldType(latestField.getFieldType());
		existingField.setFieldComment(latestField.getFieldComment());
		existingField.setPrimaryPk(latestField.getPrimaryPk());
		this.applyFieldTypeMapping(existingField, fieldTypeMap);
		if (Objects.isNull(existingField.getAttrName())) {
			existingField.setAttrName(NamingCase.toCamelCase(existingField.getFieldName()));
		}
		if (Objects.isNull(existingField.getSort())) {
			existingField.setSort(
					Objects.nonNull(latestField.getSort()) ? latestField.getSort() : sortCounter.getAndIncrement());
		}
	}

	private void applyFieldTypeMapping(GenTableColumnEntity field, Map<String, GenFieldType> fieldTypeMap) {
		GenFieldType fieldType = Objects.isNull(field.getFieldType()) ? null
				: fieldTypeMap.get(this.normalizeColumnType(field.getFieldType()));
		if (Objects.isNull(fieldType)) {
			field.setAttrType("Object");
			field.setPackageName(null);
			field.setFormType(DEFAULT_COMPONENT_TYPE);
			field.setQueryFormType(DEFAULT_COMPONENT_TYPE);
			return;
		}
		field.setAttrType(fieldType.getAttrType());
		field.setPackageName(fieldType.getPackageName());
		field.setFormType(this.resolveComponentType(fieldType.getDefaultFormType()));
		field.setQueryFormType(this.resolveComponentType(fieldType.getDefaultQueryFormType()));
	}

	private String resolveComponentType(String type) {
		return StringUtils.hasText(type) ? type : DEFAULT_COMPONENT_TYPE;
	}

	private String normalizeColumnType(String type) {
		return type.toLowerCase(Locale.ROOT);
	}

}
