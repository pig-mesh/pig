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
import java.util.Date;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author lengleng
 * @since 2017-11-08
 */
@Data
@TableName("sys_menu")
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单ID
     */
    @TableId(value = "menu_id",type = IdType.INPUT)
	private Integer menuId;
    /**
     * 菜单名称
     */
	private String name;
    /**
     * 菜单权限标识
     */
	private String permission;
    /**
     * 请求链接
     */
	private String url;
    /**
     * 请求方法
     */
	private String method;
    /**
     * 父菜单ID
     */
	@TableField("parent_id")
	private Integer parentId;
    /**
     * 图标
     */
	private String icon;
    /**
     * VUE页面
     */
	private String component;
    /**
     * 排序值
     */
	private Integer sort;
    /**
     * 菜单类型 （0菜单 1按钮）
     */
	private String type;
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
     * 0--正常 1--删除
     */
	@TableField("del_flag")
	private String delFlag;
	/**
	 * 前端URL
	 */
	private String path;


	@Override
	protected Serializable pkVal() {
		return this.menuId;
	}

	@Override
	public String toString() {
		return "SysMenu{" +
			", menuId=" + menuId +
			", name=" + name +
			", permission=" + permission +
			", url=" + url +
			", method=" + method +
			", parentId=" + parentId +
			", icon=" + icon +
			", component=" + component +
			", sort=" + sort +
			", type=" + type +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", delFlag=" + delFlag +
			"}";
	}
}
