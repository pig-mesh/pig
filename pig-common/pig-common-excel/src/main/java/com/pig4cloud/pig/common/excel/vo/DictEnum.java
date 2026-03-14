package com.pig4cloud.pig.common.excel.vo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 字典枚举接口
 *
 * @author lengleng
 */
public interface DictEnum {

	String getValue();

	String getLabel();

	default boolean eq(Object value) {
		return getValue().equals(String.valueOf(value));
	}

	static <E extends DictEnum> String getLabelByValue(E[] enums, String value) {
		return Arrays.stream(enums).filter(e -> e.getValue().equals(value)).findFirst().map(DictEnum::getLabel)
			.orElse(null);
	}

	static <E extends DictEnum> String getValueByLabel(E[] enums, String label) {
		return Arrays.stream(enums).filter(e -> e.getLabel().equals(label)).findFirst().map(DictEnum::getValue)
			.orElse(null);
	}

	static DictEnum of(String value, String label) {
		return new DictEnum() {
			@Override
			public String getValue() {
				return value;
			}

			@Override
			public String getLabel() {
				return label;
			}
		};
	}

	static Builder builder() {
		return new Builder();
	}

	class Builder {

		private final List<DictEnum> items = new ArrayList<>();

		public Builder add(String value, String label) {
			items.add(DictEnum.of(value, label));
			return this;
		}

		public DictEnum[] build() {
			return items.toArray(new DictEnum[0]);
		}

	}

}
