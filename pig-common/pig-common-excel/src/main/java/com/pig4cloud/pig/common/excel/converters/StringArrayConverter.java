package com.pig4cloud.pig.common.excel.converters;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * LocalDate and string converter
 *
 * @author L.cm
 */
public enum StringArrayConverter implements Converter<String[]> {

	/**
	 * 实例
	 */
	INSTANCE;

	@Override
	public Class supportJavaTypeKey() {
		return String[].class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public String[] convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws ParseException {
		return cellData.getStringValue().split(",");
	}

	@Override
	public WriteCellData<String> convertToExcelData(String[] value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		return new WriteCellData<>(Arrays.stream(value).collect(Collectors.joining()));
	}

}
