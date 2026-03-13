package com.pig4cloud.pig.common.excel.converters;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * LocalDate and string converter
 *
 * @author L.cm
 */
public enum LocalDateStringConverter implements Converter<LocalDate> {

	/**
	 * 实例
	 */
	INSTANCE;

	@Override
	public Class supportJavaTypeKey() {
		return LocalDate.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public LocalDate convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws ParseException {
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			return LocalDate.parse(cellData.getStringValue());
		}
		else {
			DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
			return LocalDate.parse(cellData.getStringValue(), formatter);
		}
	}

	@Override
	public WriteCellData<String> convertToExcelData(LocalDate value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		DateTimeFormatter formatter;
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			formatter = DateTimeFormatter.ISO_LOCAL_DATE;
		}
		else {
			formatter = DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
		}
		return new WriteCellData<>(value.format(formatter));
	}

}
