
package com.anjiplus.template.gaea.business.modules.accessauthority.service;

import com.anji.plus.gaea.bean.TreeNode;
import com.anjiplus.template.gaea.business.modules.accessauthority.dao.entity.AccessAuthority;
import com.anjiplus.template.gaea.business.modules.accessauthority.controller.param.AccessAuthorityParam;
import com.anji.plus.gaea.curd.service.GaeaBaseService;

import java.util.List;

/**
* @desc AccessAuthority 权限管理服务接口
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:10.009
**/
public interface AccessAuthorityService extends GaeaBaseService<AccessAuthorityParam, AccessAuthority> {

    /**
     * @param loginName 当前登录的用户名
     * @param withActionNode 带第三层的按钮节点
     * @return
     */
    List<TreeNode> getAuthorityTree(String loginName, boolean withActionNode);

    /**
     * 扫描所有mvc url的需要权限码，建立拦截体系
     */
    void scanGaeaSecurityAuthorities();
}