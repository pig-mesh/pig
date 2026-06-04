package com.pig4cloud.pig.common.file.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 文件存储桶信息。
 *
 * @author lengleng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileBucket {

	private String name;

	private Instant creationDate;

	public FileBucket(String name) {
		this.name = name;
	}

}
