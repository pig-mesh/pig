package com.pig4cloud.pig.common.file.core;

import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * 文件存储类型条件。
 *
 * @author lengleng
 */
abstract class AbstractFileTypeCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Environment environment = context.getEnvironment();
		Binder binder = Binder.get(environment);
		String type = binder.bind("file.type", String.class).orElse(null);
		if (type != null) {
			return matchesType(type);
		}
		return matchesLegacySwitch(binder);
	}

	protected abstract boolean matchesType(String type);

	protected abstract boolean matchesLegacySwitch(Binder binder);

	protected boolean isTrue(Binder binder, String name) {
		return binder.bind(name, Boolean.class).orElse(Boolean.FALSE);
	}

}
