package com.pig4cloud.pigx.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.entity.ProcessCopy;

/**
 * 流程抄送服务接口
 * <p>
 * 该服务负责管理工作流程中的抄送功能，包括抄送记录的创建、查询和管理。
 * 抄送是工作流中的重要功能，允许将流程信息同步给相关人员查看，但不需要其参与审批。
 * 抄送人员可以查看流程的详细信息、表单数据和审批进度，实现流程的透明化管理。
 * </p>
 *
 * @author Vincent
 * @since 2023-05-20
 */
public interface IProcessCopyService extends IService<ProcessCopy> {

	/**
	 * 查询单个抄送记录的详细信息
	 * <p>
	 * 根据抄送记录ID查询完整的抄送信息，包括流程实例信息、表单数据、
	 * 抄送时间、抄送人信息等。该接口主要用于抄送接收人查看具体的抄送内容。
	 * 返回的数据会包含流程的当前状态、审批历史和相关的业务表单数据。
	 * </p>
	 *
	 * @param id 抄送记录的唯一标识ID
	 * @return R 响应结果，包含抄送的详细信息：
	 *         - 流程实例信息（流程名称、发起人、发起时间等）
	 *         - 表单数据（业务表单的完整数据）
	 *         - 抄送信息（抄送时间、抄送人、抄送节点等）
	 *         - 流程进度（当前节点、审批历史等）
	 */
	R querySingleDetail(long id);

}
