package com.pig4cloud.pigx.flow.dto;

import lombok.Data;

import java.util.Map;

/**
 * 节点HTTP请求结果数据传输对象
 * <p>
 * 该DTO用于封装流程节点执行HTTP请求后的返回结果。
 * 在流程执行过程中，某些节点可能需要调用外部系统的接口（如获取审批人、
 * 验证业务数据等），此对象用于统一封装这些HTTP请求的响应结果。
 * </p>
 * 
 * @author pigx
 */
@Data
public class NodeHttpResultVO {

	/**
	 * 请求是否成功
	 * <p>
	 * 标识HTTP请求是否执行成功：
	 * - true: 请求成功，data字段包含返回的业务数据
	 * - false: 请求失败，data字段可能包含错误信息
	 * </p>
	 */
	private Boolean ok;

	/**
	 * 返回的数据内容
	 * <p>
	 * HTTP请求返回的业务数据，以Map形式存储，便于灵活处理各种数据结构。
	 * 成功时包含业务数据，失败时可能包含错误码、错误信息等。
	 * 具体内容取决于调用的接口定义。
	 * </p>
	 */
	private Map<String, Object> data;

}
