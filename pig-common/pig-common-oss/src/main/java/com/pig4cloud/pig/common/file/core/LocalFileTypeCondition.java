package com.pig4cloud.pig.common.file.core;

import org.springframework.boot.context.properties.bind.Binder;

/**
 * 本地文件存储装配条件。
 *
 * @author lengleng
 */
public class LocalFileTypeCondition extends AbstractFileTypeCondition {

	@Override
	protected boolean matchesType(String type) {
		return FileType.LOCAL.name().equalsIgnoreCase(type);
	}

	@Override
	protected boolean matchesLegacySwitch(Binder binder) {
		if (isTrue(binder, "file.oss.enable")) {
			return false;
		}
		return binder.bind("file.local.enable", Boolean.class).orElse(Boolean.TRUE);
	}

}
