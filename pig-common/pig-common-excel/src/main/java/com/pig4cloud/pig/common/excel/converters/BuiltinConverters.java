package com.pig4cloud.pig.common.excel.converters;

import org.apache.fesod.sheet.converters.Converter;
import org.apache.fesod.sheet.read.builder.ExcelReaderBuilder;
import org.apache.fesod.sheet.write.builder.ExcelWriterBuilder;

import java.util.List;

/**
 * 内置 Converter 集合，集中管理避免读写两侧重复注册。
 *
 * @author lengleng
 */
public final class BuiltinConverters {

	private BuiltinConverters() {
	}

	/**
	 * 读写 Excel 时默认启用的转换器列表。
	 */
	private static final List<Converter<?>> INSTANCES = List.of(LocalDateStringConverter.INSTANCE,
			LocalTimeStringConverter.INSTANCE, LocalDateTimeStringConverter.INSTANCE, LongStringConverter.INSTANCE,
			StringArrayConverter.INSTANCE);

	/**
	 * 注册内置转换器到 Excel 写构造器。
	 * @param builder Excel 写构造器
	 */
	public static void registerTo(ExcelWriterBuilder builder) {
		INSTANCES.forEach(builder::registerConverter);
	}

	/**
	 * 注册内置转换器到 Excel 读构造器。
	 * @param builder Excel 读构造器
	 */
	public static void registerTo(ExcelReaderBuilder builder) {
		INSTANCES.forEach(builder::registerConverter);
	}

}
