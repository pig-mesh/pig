package com.pig4cloud.pig.common.excel.converters;

import cn.hutool.core.util.StrUtil;
import org.apache.fesod.sheet.converters.Converter;
import org.apache.fesod.sheet.enums.CellDataTypeEnum;
import org.apache.fesod.sheet.metadata.GlobalConfiguration;
import org.apache.fesod.sheet.metadata.data.ReadCellData;
import org.apache.fesod.sheet.metadata.data.WriteCellData;
import org.apache.fesod.sheet.metadata.property.ExcelContentProperty;

/**
 * String[] and string converter
 *
 * @author L.cm
 */
public enum StringArrayConverter implements Converter<String[]> {

	/**
	 * 实例
	 */
	INSTANCE;

	@Override
	public Class<?> supportJavaTypeKey() {
		return String[].class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public String[] convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		return StrUtil.splitToArray(cellData.getStringValue(), StrUtil.COMMA);
	}

	@Override
	public WriteCellData<String> convertToExcelData(String[] value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		return new WriteCellData<>(String.join(StrUtil.COMMA, value));
	}

}
