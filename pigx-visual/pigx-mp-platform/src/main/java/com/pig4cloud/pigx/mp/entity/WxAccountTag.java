package com.pig4cloud.pigx.mp.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 微信公众号账号标签
 *
 * @author lishangbu
 * @date 2021/12/31
 */
@Data
@TenantTable
@EqualsAndHashCode(callSuper = true)
public class WxAccountTag extends Model<WxAccount> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 标签
	 */
	@NotBlank(message = "标签不能为空")
	private String tag;

	/**
	 * 标签ID ， 微信公众平台返回
	 */
	private Long tagId;

	/**
	 * 微信公众号ID
	 */
	private Long wxAccountId;

	/**
	 * 微信公众号appid
	 */
	private String wxAccountAppid;

	/**
	 * 微信公众号名
	 */
	private String wxAccountName;

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	/**
	 * 是否删除 1：已删除 0：正常
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	private String delFlag;

}
