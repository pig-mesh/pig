package com.github.pig.admin.model.dto;

import com.github.pig.admin.model.entity.SysUser;
import lombok.Data;

/**
 * @author lengleng
 * @date 2017/11/5
 */
@Data
public class UserDTO extends SysUser {
    /**
     * 角色ID
     */
    private Integer role;

    private Integer deptId;

    /**
     * 新密码
     */
    private String newpassword1;
}
