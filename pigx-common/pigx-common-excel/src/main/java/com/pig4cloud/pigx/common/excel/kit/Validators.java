package com.pig4cloud.pigx.common.excel.kit;

import jakarta.validation.*;
import java.util.Set;

/**
 * 校验工具
 *
 * @author L.cm
 */
public final class Validators {

	private Validators() {
	}

	private static final Validator VALIDATOR;

	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		VALIDATOR = factory.getValidator();
	}

	/**
	 * Validates all constraints on {@code object}.
	 * @param object object to validate
	 * @param <T> the type of the object to validate
	 * @return constraint violations or an empty set if none
	 * @throws IllegalArgumentException if object is {@code null} or if {@code null} is
	 * passed to the varargs groups
	 * @throws ValidationException if a non recoverable error happens during the
	 * validation process
	 */
	public static <T> Set<ConstraintViolation<T>> validate(T object) {
		return VALIDATOR.validate(object);
	}

}
