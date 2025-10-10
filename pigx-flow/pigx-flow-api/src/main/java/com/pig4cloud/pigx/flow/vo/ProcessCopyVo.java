package com.pig4cloud.pigx.flow.vo;

import com.pig4cloud.pigx.flow.entity.ProcessCopy;
import lombok.Data;

/**
 * 流程抄送视图对象
 * <p>
 * 继承自ProcessCopy实体类，用于展示流程抄送信息时的扩展字段。
 * 主要用于抄送列表的展示，包含抄送的基本信息和扩展的显示信息。
 * </p>
 */
@Data
public class ProcessCopyVo extends ProcessCopy {

	/**
	 * 发起人姓名
	 * <p>
	 * 流程发起人的姓名，用于在抄送列表中显示流程是由谁发起的。
	 * 该字段是对基础实体的扩展，便于前端直接展示发起人信息。
	 * </p>
	 */
	private String startUserName;

}
