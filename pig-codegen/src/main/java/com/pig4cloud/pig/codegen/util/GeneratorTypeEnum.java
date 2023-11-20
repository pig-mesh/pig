package com.pig4cloud.pig.codegen.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GeneratorTypeEnum {

	/**
	 * zip压缩包 0
	 */
	ZIP_DOWNLOAD("0"),
	/**
	 * 自定义目录 1
	 */
	CUSTOM_DIRECTORY("1");

	private final String value;

}
