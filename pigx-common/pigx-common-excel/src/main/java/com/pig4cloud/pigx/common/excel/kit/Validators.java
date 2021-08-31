package com.pig4cloud.pigx.common.excel.kit;

import javax.validation.*;
import java.util.Set;

/**
 * 校验工具
 *
 * @author L.cm
 */
public class Validators {

	private static final Validator validator;

	static {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
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
		return validator.validate(object);
	}

}
