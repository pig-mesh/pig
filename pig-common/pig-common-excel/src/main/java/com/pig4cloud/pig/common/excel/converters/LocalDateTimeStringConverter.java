package com.pig4cloud.pig.common.excel.converters;

import cn.idev.excel.converters.Converter;
import cn.idev.excel.enums.CellDataTypeEnum;
import cn.idev.excel.metadata.GlobalConfiguration;
import cn.idev.excel.metadata.data.ReadCellData;
import cn.idev.excel.metadata.data.WriteCellData;
import cn.idev.excel.metadata.property.ExcelContentProperty;
import cn.idev.excel.util.DateUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * LocalDateTime and string converter
 *
 * @author L.cm
 */
public enum LocalDateTimeStringConverter implements Converter<LocalDateTime> {

	/**
	 * 实例
	 */
	INSTANCE;

	private static final String MINUS = "-";

	@Override
	public Class supportJavaTypeKey() {
		return LocalDateTime.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public LocalDateTime convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws ParseException {
		String stringValue = cellData.getStringValue();
		String pattern;
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			pattern = switchDateFormat(stringValue);
		}
		else {
			pattern = contentProperty.getDateTimeFormatProperty().getFormat();
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return LocalDateTime.parse(cellData.getStringValue(), formatter);
	}

	@Override
	public WriteCellData<String> convertToExcelData(LocalDateTime value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) {
		String pattern;
		if (contentProperty == null || contentProperty.getDateTimeFormatProperty() == null) {
			pattern = DateUtils.DATE_FORMAT_19;
		}
		else {
			pattern = contentProperty.getDateTimeFormatProperty().getFormat();
		}
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return new WriteCellData<>(value.format(formatter));
	}

	/**
	 * switch date format
	 * @param dateString dateString
	 * @return pattern
	 */
	private static String switchDateFormat(String dateString) {
		int length = dateString.length();
		switch (length) {
			case 19:
				if (dateString.contains(MINUS)) {
					return DateUtils.DATE_FORMAT_19;
				}
				else {
					return DateUtils.DATE_FORMAT_19_FORWARD_SLASH;
				}
			case 17:
				return DateUtils.DATE_FORMAT_17;
			case 14:
				return DateUtils.DATE_FORMAT_14;
			case 10:
				return DateUtils.DATE_FORMAT_10;
			default:
				throw new IllegalArgumentException("can not find date format for：" + dateString);
		}
	}

}
