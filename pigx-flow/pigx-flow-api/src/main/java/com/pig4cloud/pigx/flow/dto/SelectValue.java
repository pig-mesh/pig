package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

/**
 * 选择项数据传输对象
 * <p>
 * 该DTO用于表示下拉选择、单选、多选等表单控件的选项。
 * 提供键值对的数据结构，key用于存储和逻辑处理，value用于界面显示。
 * 常用于表单配置、条件选择、数据字典等场景。
 * </p>
 * 
 * @author Huijun Zhao
 * @date 2023-07-28 10:36
 */
@Data
public class SelectValue {

	/**
	 * 选项键
	 * <p>
	 * 选项的唯一标识，用于数据存储和逻辑处理。
	 * 通常是编码、ID等不会改变的值，如"1"、"APPROVED"、"DEPT_001"等。
	 * </p>
	 */
	private String key;

	/**
	 * 选项显示值
	 * <p>
	 * 选项的显示文本，用于在界面上展示给用户。
	 * 通常是描述性的文字，如"已批准"、"销售部"、"紧急"等。
	 * </p>
	 */
	private String value;

}
