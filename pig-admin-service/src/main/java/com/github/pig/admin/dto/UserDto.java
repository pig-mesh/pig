package com.github.pig.admin.dto;

import com.github.pig.admin.entity.SysUser;

/**
 * @author lengleng
 * @date 2017/11/5
 */
public class UserDto extends SysUser {
    /**
     * 角色ID
     */
    private Integer role;

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
