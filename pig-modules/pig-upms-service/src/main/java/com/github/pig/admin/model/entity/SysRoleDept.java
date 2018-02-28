package com.github.pig.admin.model.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 角色与部门对应关系
 * </p>
 *
 * @author lengleng
 * @since 2018-01-20
 */
@TableName("sys_role_dept")
public class SysRoleDept extends Model<SysRoleDept> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 角色ID
     */
	@TableField("role_id")
	private Integer roleId;
    /**
     * 部门ID
     */
	@TableField("dept_id")
	private Integer deptId;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysRoleDept{" +
			", id=" + id +
			", roleId=" + roleId +
			", deptId=" + deptId +
			"}";
	}
}
