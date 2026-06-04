package com.pig4cloud.pig.common.file.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;

/**
 * 文件对象读取结果。
 *
 * @author lengleng
 */
@Getter
@RequiredArgsConstructor
public class FileObject implements AutoCloseable {

	private final String bucketName;

	private final String objectName;

	private final String contentType;

	private final Long contentLength;

	private final InputStream objectContent;

	@Override
	@SneakyThrows
	public void close() {
		if (objectContent != null) {
			objectContent.close();
		}
	}

}
