package com.pig4cloud.pigx.mp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.pig4cloud.pigx.common.data.mybatis.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 微信公众号账号标签
 *
 * @author lishangbu
 * @date 2021/12/31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class WxAccountTag extends BaseEntity {

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

}
