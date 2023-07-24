package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

import java.util.List;

@Data
public class VariableQueryParamDto {

	private String taskId;

	private List<String> keyList;

	private String executionId;

}
