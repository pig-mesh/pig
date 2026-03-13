package com.pig4cloud.pig.common.excel.converters;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

import java.text.ParseException;

/**
 * Long and string converter
 *
 * @author L.cm
 */
public enum LongStringConverter implements Converter<Long> {

	/**
	 * 实例
	 */
	INSTANCE;

	@Override
	public Class supportJavaTypeKey() {
		return Long.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public Long convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws ParseException {
		return Long.parseLong(cellData.getStringValue());
	}

	@Override
	public WriteCellData<String> convertToExcelData(Long value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		return new WriteCellData<>(String.valueOf(value));
	}

}
