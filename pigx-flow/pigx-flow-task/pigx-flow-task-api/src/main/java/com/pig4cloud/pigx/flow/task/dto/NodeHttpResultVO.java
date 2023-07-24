package com.pig4cloud.pigx.flow.task.dto;

import lombok.Data;

import java.util.Map;

/**
 * 节点处理http请求结果对象
 */
@Data
public class NodeHttpResultVO {

	private Boolean ok;

	private Map<String, Object> data;

}
