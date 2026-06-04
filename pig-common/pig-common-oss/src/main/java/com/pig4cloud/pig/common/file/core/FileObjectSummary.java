package com.pig4cloud.pig.common.file.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 文件对象摘要。
 *
 * @author lengleng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileObjectSummary {

	private String bucketName;

	private String key;

	private Long size;

	private Instant lastModified;

}
