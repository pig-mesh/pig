package com.pig4cloud.pig.codegen.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.codegen.entity.ColumnEntity;
import com.pig4cloud.pig.codegen.entity.GenConfig;
import com.pig4cloud.pig.codegen.entity.GenFormConf;
import com.pig4cloud.pig.codegen.entity.TableEntity;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.exception.CheckedException;
import lombok.SneakyThrows;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

/**
 * 生成代码抽象
 *
 * @author Fxz
 * @date 2022/7/21 02:24
 */
public interface GenCodeService {

	default Map<String, String> gen(GenConfig genConfig, Map<String, String> table, List<Map<String, String>> columns,
			ZipOutputStream zip, GenFormConf formConf) {
		// 构建表实体
		TableEntity tableEntity = buildTableEntity(genConfig, table);

		// 处理列相关
		buildColumnEntity(tableEntity, columns);

		// 模板入参
		Map<String, Object> tempModel = buildTempModel(tableEntity, genConfig);

		return renderData(genConfig, zip, tableEntity, tempModel, formConf);
	}

	/**
	 * 渲染数据
	 * @param genConfig 用户输入相关
	 * @param zip 输出zip流
	 * @param tableEntity 表格实体
	 * @param map 参数集合
	 * @param formConf 表单设计
	 */
	@SneakyThrows
	default Map<String, String> renderData(GenConfig genConfig, ZipOutputStream zip, TableEntity tableEntity,
			Map<String, Object> map, GenFormConf formConf) {
		// 设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(prop);

		VelocityContext context = new VelocityContext(map);
		// 函数库
		context.put("math", new MathTool());
		context.put("dateTool", new DateTool());

		// 获取模板列表
		List<String> templates = getTemplates(genConfig);
		Map<String, String> resultMap = new LinkedHashMap<>(8);

		for (String template : templates) {
			// 渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, CharsetUtil.UTF_8);
			tpl.merge(context, sw);

			// 添加到zip
			String fileName = getFileName(template, tableEntity.getCaseClassName(), map.get("package").toString(),
					map.get("moduleName").toString());

			if (zip != null) {
				try {
					zip.putNextEntry(new ZipEntry(Objects.requireNonNull(fileName)));
					IoUtil.write(zip, StandardCharsets.UTF_8, false, sw.toString());
					IoUtil.close(sw);
				}
				catch (ZipException zipException) {
				}
				zip.closeEntry();
			}
			resultMap.put(template, sw.toString());
		}

		return resultMap;
	}

	/**
	 * 注入支持的模板列表
	 * @param config 用户输入
	 * @return ListString
	 */
	default List<String> getTemplates(GenConfig config) {
		List<String> templates = new ArrayList<>();
		templates.add("template/Entity.java.vm");
		templates.add("template/Mapper.java.vm");
		templates.add("template/Mapper.xml.vm");
		templates.add("template/Service.java.vm");
		templates.add("template/ServiceImpl.java.vm");
		templates.add("template/Controller.java.vm");
		templates.add("template/menu.sql.vm");
		return templates;
	}

	/**
	 * 获取文件名
	 */
	default String getFileName(String template, String className, String packageName, String moduleName) {
		String packagePath = CommonConstants.BACK_END_PROJECT + File.separator + "src" + File.separator + "main"
				+ File.separator + "java" + File.separator;

		if (StringUtils.isNotBlank(packageName)) {
			packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
		}

		if (template.contains("Entity.java.vm")) {
			return packagePath + "entity" + File.separator + className + ".java";
		}

		if (template.contains("Mapper.java.vm")) {
			return packagePath + "mapper" + File.separator + className + "Mapper.java";
		}

		if (template.contains("Service.java.vm")) {
			return packagePath + "service" + File.separator + className + "Service.java";
		}

		if (template.contains("ServiceImpl.java.vm")) {
			return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
		}

		if (template.contains("Controller.java.vm")) {
			return packagePath + "controller" + File.separator + className + "Controller.java";
		}

		if (template.contains("Mapper.xml.vm")) {
			return CommonConstants.BACK_END_PROJECT + File.separator + "src" + File.separator + "main" + File.separator
					+ "resources" + File.separator + "mapper" + File.separator + className + "Mapper.xml";
		}

		if (template.contains("menu.sql.vm")) {
			return className.toLowerCase() + "_menu.sql";
		}

		return null;
	}

	default Map<String, Object> buildTempModel(TableEntity tableEntity, GenConfig genConfig) {
		// 封装模板数据
		Map<String, Object> map = new HashMap<>(16);
		map.put("tableName", tableEntity.getTableName());
		map.put("pk", tableEntity.getPk());
		map.put("className", tableEntity.getCaseClassName());
		map.put("classname", tableEntity.getLowerClassName());
		map.put("pathName", tableEntity.getLowerClassName().toLowerCase());
		map.put("columns", tableEntity.getColumns());
		map.put("datetime", DateUtil.now());

		if (StrUtil.isNotBlank(genConfig.getComments())) {
			map.put("comments", genConfig.getComments());
		}
		else {
			map.put("comments", tableEntity.getComments());
		}

		if (StrUtil.isNotBlank(genConfig.getAuthor())) {
			map.put("author", genConfig.getAuthor());
		}
		else {
			map.put("author", getConfig().getString("author"));
		}

		if (StrUtil.isNotBlank(genConfig.getModuleName())) {
			map.put("moduleName", genConfig.getModuleName());
		}
		else {
			map.put("moduleName", getConfig().getString("moduleName"));
		}

		if (StrUtil.isNotBlank(genConfig.getPackageName())) {
			map.put("package", genConfig.getPackageName());
			map.put("mainPath", genConfig.getPackageName());
		}
		else {
			map.put("package", getConfig().getString("package"));
			map.put("mainPath", getConfig().getString("mainPath"));
		}

		return map;
	}

	/**
	 * 构建 ColumnEntity
	 * @param columns 列元信息
	 */
	default void buildColumnEntity(TableEntity tableEntity, List<Map<String, String>> columns) {
		// 获取需要在swagger文档中隐藏的属性字段
		List<Object> hiddenColumns = getConfig().getList("hiddenColumn");
		// 列信息
		List<ColumnEntity> columnList = new ArrayList<>();
		for (Map<String, String> column : columns) {
			ColumnEntity columnEntity = new ColumnEntity();
			columnEntity.setColumnName(column.get("columnName"));
			columnEntity.setDataType(column.get("dataType"));
			columnEntity.setExtra(column.get("extra"));
			columnEntity.setNullable("NO".equals(column.get("isNullable")));
			columnEntity.setColumnType(column.get("columnType"));
			// 隐藏不需要的在接口文档中展示的字段
			if (hiddenColumns.contains(column.get("columnName"))) {
				columnEntity.setHidden(Boolean.TRUE);
			}
			else {
				columnEntity.setHidden(Boolean.FALSE);
			}
			// 列名转换成Java属性名
			String attrName = columnToJava(columnEntity.getColumnName());
			columnEntity.setCaseAttrName(attrName);
			columnEntity.setLowerAttrName(StringUtils.uncapitalize(attrName));

			// 判断注释是否为空
			if (StrUtil.isNotBlank(column.get("columnComment"))) {
				// 注意去除换行符号
				columnEntity.setComments(StrUtil.removeAllLineBreaks(column.get("columnComment")));
			}
			else {
				columnEntity.setComments(columnEntity.getLowerAttrName());
			}

			// 列的数据类型，转换成Java类型
			String dataType = StrUtil.subBefore(columnEntity.getDataType(), "(", false);
			String attrType = getConfig().getString(dataType, "unknowType");
			columnEntity.setAttrType(attrType);

			// 是否主键
			if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
				tableEntity.setPk(columnEntity);
			}

			columnList.add(columnEntity);
		}
		tableEntity.setColumns(columnList);

		// 没主键，则第一个字段为主键
		if (tableEntity.getPk() == null) {
			tableEntity.setPk(tableEntity.getColumns().get(0));
		}
	}

	/**
	 * 构建 TableEntity
	 * @param genConfig 用户输入相关
	 * @param table 表元信息
	 * @return TableEntity
	 */
	default TableEntity buildTableEntity(GenConfig genConfig, Map<String, String> table) {
		// 表信息
		TableEntity tableEntity = new TableEntity();
		tableEntity.setTableName(table.get("tableName"));

		if (StrUtil.isNotBlank(genConfig.getComments())) {
			tableEntity.setComments(genConfig.getComments());
		}
		else {
			tableEntity.setComments(table.get("tableComment"));
		}

		String tablePrefix;
		if (StrUtil.isNotBlank(genConfig.getTablePrefix())) {
			tablePrefix = genConfig.getTablePrefix();
		}
		else {
			tablePrefix = getConfig().getString("tablePrefix");
		}
		// 表名转换成Java类名
		String className = tableToJava(tableEntity.getTableName(), tablePrefix);
		tableEntity.setCaseClassName(className);
		tableEntity.setLowerClassName(StringUtils.uncapitalize(className));

		return tableEntity;
	}

	/**
	 * 获取配置信息
	 */
	default Configuration getConfig() {
		try {
			return new PropertiesConfiguration("generator.properties");
		}
		catch (ConfigurationException e) {
			throw new CheckedException("获取配置文件失败，", e);
		}
	}

	/**
	 * 表名转换成Java类名
	 */
	default String tableToJava(String tableName, String tablePrefix) {
		if (StringUtils.isNotBlank(tablePrefix)) {
			tableName = tableName.replaceFirst(tablePrefix, "");
		}
		return columnToJava(tableName);
	}

	/**
	 * 列名转换成Java属性名
	 */
	default String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[] { '_' }).replace("_", "");
	}

}
