
package com.anjiplus.template.gaea.business.modules.accessuser.dao.entity;

import lombok.Data;
import com.anji.plus.gaea.curd.entity.GaeaBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import javax.validation.constraints.*;
import java.util.Date;
/**
* @description 用户管理 entity
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:11.902
**/
@TableName(keepGlobalPrefix=true, value="access_user")
@Data
public class AccessUser extends GaeaBaseEntity {

    /**  登录名 */
    private String loginName;

    /** 密码 */
    private String password;

    /** 真实用户 */
    private String realName;

    /** 手机号码 */
    private String phone;

    /** 用户邮箱 */
    private String email;

    /** 备注 */
    private String remark;

    /** 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG */
    private Integer enableFlag;

    /**  0--未删除 1--已删除 DIC_NAME=DEL_FLAG */
    private Integer deleteFlag;

    /** 最后一次登录IP */
    private String lastLoginIp;

    /** 最后一次登陆时间 */
    private Date lastLoginTime;

}
