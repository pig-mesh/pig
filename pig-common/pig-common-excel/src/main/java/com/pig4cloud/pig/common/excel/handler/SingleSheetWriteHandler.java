package com.pig4cloud.pig.common.excel.handler;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelWriter;
import cn.idev.excel.converters.Converter;
import cn.idev.excel.write.metadata.WriteSheet;
import com.pig4cloud.pig.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pig.common.excel.config.ExcelConfigProperties;
import com.pig4cloud.pig.common.excel.enhance.WriterBuilderEnhancer;
import com.pig4cloud.pig.common.excel.kit.ExcelException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author lengleng
 * @author L.cm
 * @date 2020/3/29
 * <p>
 * 处理单sheet 页面
 */
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

	public SingleSheetWriteHandler(ExcelConfigProperties configProperties,
			ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance) {
		super(configProperties, converterProvider, excelWriterBuilderEnhance);
	}

	/**
	 * obj 是List 且list不为空同时list中的元素不是是List 才返回true
	 * @param obj 返回对象
	 * @return boolean
	 */
	@Override
	public boolean support(Object obj) {
		if (obj instanceof List) {
			List<?> objList = (List<?>) obj;
			return !objList.isEmpty() && !(objList.get(0) instanceof List);
		}
		else {
			throw new ExcelException("@ResponseExcel 返回值必须为List类型");
		}
	}

	@Override
	public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
		List<?> eleList = (List<?>) obj;
		ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

		WriteSheet sheet;
		if (CollectionUtils.isEmpty(eleList)) {
			sheet = EasyExcel.writerSheet(responseExcel.sheets()[0].sheetName()).build();
		}
		else {
			// 有模板则不指定sheet名
			Class<?> dataClass = eleList.get(0).getClass();
			sheet = this.sheet(responseExcel.sheets()[0], dataClass, responseExcel.template(),
					responseExcel.headGenerator());
		}

		// 填充 sheet
		if (responseExcel.fill()) {
			excelWriter.fill(eleList, sheet);
		}
		else {
			// 写入sheet
			excelWriter.write(eleList, sheet);
		}
		excelWriter.finish();
	}

}
