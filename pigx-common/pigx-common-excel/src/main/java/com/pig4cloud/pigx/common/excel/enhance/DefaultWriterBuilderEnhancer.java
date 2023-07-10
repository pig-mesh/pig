package com.pig4cloud.pigx.common.excel.enhance;

import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.excel.head.HeadGenerator;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Hccake 2020/12/18
 * @version 1.0
 */
public class DefaultWriterBuilderEnhancer implements WriterBuilderEnhancer {

	/**
	 * ExcelWriterBuilder 增强
	 * @param writerBuilder ExcelWriterBuilder
	 * @param response HttpServletResponse
	 * @param responseExcel ResponseExcel
	 * @param templatePath 模板地址
	 * @return ExcelWriterBuilder
	 */
	@Override
	public ExcelWriterBuilder enhanceExcel(ExcelWriterBuilder writerBuilder, HttpServletResponse response,
			ResponseExcel responseExcel, String templatePath) {
		// doNothing
		return writerBuilder;
	}

	/**
	 * ExcelWriterSheetBuilder 增强
	 * @param writerSheetBuilder ExcelWriterSheetBuilder
	 * @param sheetNo sheet角标
	 * @param sheetName sheet名，有模板时为空
	 * @param dataClass 当前写入的数据所属类
	 * @param template 模板文件
	 * @param headEnhancerClass 当前指定的自定义头处理器
	 * @return ExcelWriterSheetBuilder
	 */
	@Override
	public ExcelWriterSheetBuilder enhanceSheet(ExcelWriterSheetBuilder writerSheetBuilder, Integer sheetNo,
			String sheetName, Class<?> dataClass, String template, Class<? extends HeadGenerator> headEnhancerClass) {
		// doNothing
		return writerSheetBuilder;
	}

}
