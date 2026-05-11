package com.pig4cloud.pigx.flow.vo;

import com.pig4cloud.pigx.flow.entity.ProcessInstanceRecord;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程实例记录视图对象
 * <p>
 * 继承自ProcessInstanceRecord实体类，用于扩展查询返回的额外信息。
 * 包含流程实例的驳回状态等运行时信息。
 * </p>
 *
 * @author pigx
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessInstanceRecordVo extends ProcessInstanceRecord {

	/**
	 * 是否驳回到发起人状态
	 * <p>
	 * true表示流程已被驳回到发起人，等待发起人重新编辑提交；
	 * false或null表示流程正常运行中
	 * </p>
	 */
	private Boolean rejectToStarter;

	/**
	 * 重新提交的任务ID
	 * <p>
	 * 当rejectToStarter为true时，此字段保存发起人节点(root)的任务ID，
	 * 用于前端调用重新提交接口
	 * </p>
	 */
	private String resubmitTaskId;

    /**
     * 是否配置了打印模板
     * <p>
     * true表示该流程定义已配置打印模板，前端显示打印按钮；
     * false或null表示未配置打印模板，前端不显示打印按钮
     * </p>
     */
    private Boolean hasPrintTemplate;

    /**
     * 是否允许撤回
     */
    private Boolean canWithdraw;

    /**
     * 不可撤回原因
     */
    private String withdrawDisabledReason;

}
