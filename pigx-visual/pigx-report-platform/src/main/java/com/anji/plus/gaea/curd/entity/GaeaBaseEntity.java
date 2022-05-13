package com.anji.plus.gaea.curd.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
 * 基础数据库实体
 *
 * @author lr
 * @since 2021-01-12
 */
public class GaeaBaseEntity extends BaseEntity {

	/**
	 * 数据ID
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	private String createBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	/**
	 * 修改人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateBy;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

	/**
	 * 版本号
	 * @Version:乐观锁,，需要添加mybatis plus插件optimisticLockerInterceptor
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE, update = "%s+1")
	@Version
	private Integer version;

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
