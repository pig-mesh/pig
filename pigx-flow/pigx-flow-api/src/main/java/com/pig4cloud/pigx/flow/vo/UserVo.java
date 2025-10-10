package com.pig4cloud.pigx.flow.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户视图对象
 * <p>
 * 用于在流程处理过程中展示用户信息，包括审批人、抄送人等角色的信息展示。
 * 主要用于流程节点的用户列表、审批记录等场景。
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

	/**
	 * 用户ID
	 * <p>
	 * 用户的唯一标识符
	 * </p>
	 */
	private Long id;

	/**
	 * 用户姓名
	 * <p>
	 * 用户的显示名称，通常为真实姓名
	 * </p>
	 */
	private String name;

	/**
	 * 操作时间
	 * <p>
	 * 用户执行操作的时间，如审批时间、查看时间等
	 * </p>
	 */
	private LocalDateTime showTime;

	/**
	 * 用户头像
	 * <p>
	 * 用户头像的URL地址，用于在界面上展示用户头像
	 * </p>
	 */
	private String avatar;

	/**
	 * 审批意见
	 * <p>
	 * 用户在审批时填写的意见或备注信息
	 * </p>
	 */
	private String approveDesc;

	/**
	 * 操作类型
	 * <p>
	 * 用户执行的操作类型，如"同意"、"拒绝"、"转办"、"加签"等
	 * </p>
	 */
	private String operType;

	/**
	 * 处理状态
	 * <p>
	 * 用户的处理状态：0-待处理，1-处理中，2-已完成。默认为0
	 * </p>
	 */
	private Integer status = 0;

}
