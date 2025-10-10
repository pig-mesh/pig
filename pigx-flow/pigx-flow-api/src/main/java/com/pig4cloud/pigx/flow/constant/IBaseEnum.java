package com.pig4cloud.pigx.flow.constant;

import cn.hutool.core.util.ObjectUtil;

import java.util.EnumSet;
import java.util.Objects;

/**
 * 枚举通用接口
 * <p>
 * 为所有业务枚举提供统一的接口规范，实现枚举值和标签的标准化管理。
 * 提供了根据值获取枚举、根据值获取标签、根据标签获取值等通用方法。
 * 所有业务枚举类都应实现此接口，以保证枚举使用的一致性。
 * </p>
 *
 * @param <T> 枚举值的类型，通常为Integer或String
 * @author haoxr
 * @since 2022/3/27 12:06
 */
public interface IBaseEnum<T> {

	/**
	 * 获取枚举值
	 * <p>
	 * 返回枚举对应的实际值，通常用于数据库存储或接口传输
	 * </p>
	 *
	 * @return 枚举对应的值
	 */
	T getValue();

	/**
	 * 获取枚举标签
	 * <p>
	 * 返回枚举对应的显示标签，通常用于界面展示
	 * </p>
	 *
	 * @return 枚举对应的中文标签
	 */
	String getLabel();

	/**
	 * 根据值获取对应的枚举实例
	 * <p>
	 * 通过枚举值查找对应的枚举实例，常用于数据库查询结果的转换。
	 * 如果找不到匹配的枚举，返回null。
	 * </p>
	 *
	 * @param value 枚举值
	 * @param clazz 枚举类的Class对象
	 * @param <E>   枚举类型，必须实现IBaseEnum接口
	 * @return 匹配的枚举实例，如果没有找到返回null
	 */
	static <E extends Enum<E> & IBaseEnum> E getEnumByValue(Object value, Class<E> clazz) {
		Objects.requireNonNull(value);
		EnumSet<E> allEnums = EnumSet.allOf(clazz); // 获取类型下的所有枚举
		E matchEnum = allEnums.stream().filter(e -> ObjectUtil.equal(e.getValue(), value)).findFirst().orElse(null);
		return matchEnum;
	}

	/**
	 * 根据值获取对应的标签
	 * <p>
	 * 通过枚举值直接获取对应的显示标签，常用于列表展示时的值转换。
	 * 如果找不到匹配的枚举，返回null。
	 * </p>
	 *
	 * @param value 枚举值
	 * @param clazz 枚举类的Class对象
	 * @param <E>   枚举类型，必须实现IBaseEnum接口
	 * @return 枚举对应的标签，如果没有找到返回null
	 */
	static <E extends Enum<E> & IBaseEnum> String getLabelByValue(Object value, Class<E> clazz) {
		Objects.requireNonNull(value);
		EnumSet<E> allEnums = EnumSet.allOf(clazz); // 获取类型下的所有枚举
		E matchEnum = allEnums.stream().filter(e -> ObjectUtil.equal(e.getValue(), value)).findFirst().orElse(null);

		String label = null;
		if (matchEnum != null) {
			label = matchEnum.getLabel();
		}
		return label;
	}

	/**
	 * 根据标签获取对应的值
	 * <p>
	 * 通过枚举标签反向查找对应的枚举值，常用于表单提交时的数据转换。
	 * 如果找不到匹配的枚举，返回null。
	 * </p>
	 *
	 * @param label 枚举标签
	 * @param clazz 枚举类的Class对象
	 * @param <E>   枚举类型，必须实现IBaseEnum接口
	 * @return 枚举对应的值，如果没有找到返回null
	 */
	static <E extends Enum<E> & IBaseEnum> Object getValueByLabel(String label, Class<E> clazz) {
		Objects.requireNonNull(label);
		EnumSet<E> allEnums = EnumSet.allOf(clazz); // 获取类型下的所有枚举
		String finalLabel = label;
		E matchEnum = allEnums.stream()
			.filter(e -> ObjectUtil.equal(e.getLabel(), finalLabel))
			.findFirst()
			.orElse(null);

		Object value = null;
		if (matchEnum != null) {
			value = matchEnum.getValue();
		}
		return value;
	}

}
