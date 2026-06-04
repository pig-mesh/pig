package com.pig4cloud.pig.common.file.core;

import org.springframework.boot.context.properties.bind.Binder;

/**
 * OSS 文件存储装配条件。
 *
 * @author lengleng
 */
public class OssFileTypeCondition extends AbstractFileTypeCondition {

	@Override
	protected boolean matchesType(String type) {
		return FileType.OSS.name().equalsIgnoreCase(type);
	}

	@Override
	protected boolean matchesLegacySwitch(Binder binder) {
		return isTrue(binder, "file.oss.enable");
	}

}
