package com.pig4cloud.pig.common.excel.converters;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDate and string converter
 *
 * @author L.cm
 */
public enum LocalTimeStringConverter implements Converter<LocalTime> {

	/**
	 * 实例
	 */
	INSTANCE;

	@Override
	public Class supportJavaTypeKey() {
		return LocalTime.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public LocalTime convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws ParseException {
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			return LocalTime.parse(cellData.getStringValue());
		}
		else {
			DateTimeFormatter formatter = DateTimeFormatter
				.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
			return LocalTime.parse(cellData.getStringValue(), formatter);
		}
	}

	@Override
	public WriteCellData<String> convertToExcelData(LocalTime value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		DateTimeFormatter formatter;
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			formatter = DateTimeFormatter.ISO_LOCAL_TIME;
		}
		else {
			formatter = DateTimeFormatter.ofPattern(contentProperty.getDateTimeFormatProperty().getFormat());
		}
		return new WriteCellData<>(value.format(formatter));
	}

}
