/**/
package com.anjiplus.template.gaea.business.modules.accessuser.controller.param;

import lombok.Data;
import java.io.Serializable;
import com.anji.plus.gaea.annotation.Query;
import com.anji.plus.gaea.constant.QueryEnum;
import com.anji.plus.gaea.curd.params.PageParam;
import java.util.List;

import java.util.Date;

/**
* @desc AccessUser 用户管理查询输入类
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:11.902
**/
@Data
public class AccessUserParam extends PageParam implements Serializable{

    //  登录名
    @Query(value = QueryEnum.LIKE)
    private String loginName;

    // 真实用户
    @Query(value = QueryEnum.LIKE)
    private String realName;

    // 手机号码
    @Query(value = QueryEnum.LIKE)
    private String phone;

    // 0--已禁用 1--已启用  DIC_NAME=ENABLE_FLAG
    @Query
    private Integer enableFlag;

}