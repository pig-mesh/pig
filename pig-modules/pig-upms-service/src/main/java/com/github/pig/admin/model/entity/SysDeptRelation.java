package com.github.pig.admin.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lengleng
 * @since 2018-01-22
 */
@TableName("sys_dept_relation")
public class SysDeptRelation extends Model<SysDeptRelation> {

    private static final long serialVersionUID = 1L;

    /**
     * 祖先节点
     */
	private Integer ancestor;
    /**
     * 后代节点
     */
	private Integer descendant;


	public Integer getAncestor() {
		return ancestor;
	}

	public void setAncestor(Integer ancestor) {
		this.ancestor = ancestor;
	}

	public Integer getDescendant() {
		return descendant;
	}

	public void setDescendant(Integer descendant) {
		this.descendant = descendant;
	}

	@Override
	protected Serializable pkVal() {
		return this.ancestor;
	}

	@Override
	public String toString() {
		return "SysDeptRelation{" +
			", ancestor=" + ancestor +
			", descendant=" + descendant +
			"}";
	}
}
