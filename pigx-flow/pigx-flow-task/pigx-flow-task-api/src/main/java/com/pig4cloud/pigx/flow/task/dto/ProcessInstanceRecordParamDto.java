package com.pig4cloud.pigx.flow.task.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 流程记录
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Getter
@Setter
public class ProcessInstanceRecordParamDto {

	/**
	 * 用户id
	 */
	private Long userId;

	/**
	 * 流程id
	 */
	private String flowId;

	/**
	 * 流程实例id
	 */
	private String processInstanceId;

	private String parentProcessInstanceId;

	/**
	 * 表单数据
	 */
	private String formData;

}
