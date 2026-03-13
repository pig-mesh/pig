package com.pig4cloud.pig.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 站内消息/公告发送
 *
 * @author lengleng
 * @date 2024/07/18
 */
@Data
@FieldNameConstants
public class MessageNoticeDTO {

	/**
	 * 分类 0-公告 1-站内信
	 */
	@NotBlank
	private String category;

	/**
	 * 标题
	 */
	@NotBlank
	private String title;

	/**
	 * 内容
	 */
	@NotBlank
	private String content;

	/**
	 * 通知全体 0-否 1-是
	 */
	private String allFlag = "0";

	/**
	 * 接收用户ID列表（allFlag=0时必填）
	 */
	private List<Long> userIds = new ArrayList<>();

	/**
	 * 排序（越大越在前）
	 */
	private Integer sort = 0;

}
