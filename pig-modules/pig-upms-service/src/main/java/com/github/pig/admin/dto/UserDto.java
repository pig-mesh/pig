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

    /**
     * 新密码
     */
    private String newpassword1;

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getNewpassword1() {
        return newpassword1;
    }

    public void setNewpassword1(String newpassword1) {
        this.newpassword1 = newpassword1;
    }
}
