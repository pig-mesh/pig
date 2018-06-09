/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.github.pig.admin.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author lengleng
 * @since 2017-11-19
 */
@Data
@TableName("sys_dict")
public class SysDict extends Model<SysDict> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 数据值
     */
	private String value;
    /**
     * 标签名
     */
	private String label;
    /**
     * 类型
     */
	private String type;
    /**
     * 描述
     */
	private String description;
    /**
     * 排序（升序）
     */
	private BigDecimal sort;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Date updateTime;
    /**
     * 备注信息
     */
	private String remarks;
    /**
     * 删除标记
     */
	@TableField("del_flag")
	private String delFlag;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysDict{" +
			", id=" + id +
			", value=" + value +
			", label=" + label +
			", type=" + type +
			", description=" + description +
			", sort=" + sort +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", remarks=" + remarks +
			", delFlag=" + delFlag +
			"}";
	}
}
