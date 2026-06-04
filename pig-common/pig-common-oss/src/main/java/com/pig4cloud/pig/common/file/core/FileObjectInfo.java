package com.pig4cloud.pig.common.file.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 文件对象元数据。
 *
 * @author lengleng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileObjectInfo {

	private String bucketName;

	private String objectName;

	private String contentType;

	private Long contentLength;

	private String eTag;

	private Instant lastModified;

}
