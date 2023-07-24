package com.pig4cloud.pigx.flow.task.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class ProcessCopyDto {

	/**
	 * 当前节点时间
	 */
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date nodeTime;

	/**
	 * 发起人
	 */
	private Long startUserId;

	/**
	 * 流程id
	 */
	private String flowId;

	/**
	 * 实例id
	 */
	private String processInstanceId;

	/**
	 * 节点id
	 */
	private String nodeId;

	/**
	 * 节点 名称
	 */
	private String nodeName;

	/**
	 * 表单数据
	 */
	private String formData;

	/**
	 * 抄送人id
	 */
	private Long userId;

}
