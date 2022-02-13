
package com.anjiplus.template.gaea.business.modules.accessauthority.controller;

import com.anji.plus.gaea.annotation.Permission;
import com.anji.plus.gaea.annotation.AccessKey;
import com.anji.plus.gaea.bean.KeyValue;
import com.anji.plus.gaea.bean.ResponseBean;
import com.anji.plus.gaea.bean.TreeNode;
import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anji.plus.gaea.holder.UserContentHolder;
import com.anji.plus.gaea.utils.GaeaBeanUtils;
import com.anji.plus.gaea.utils.GaeaUtils;
import com.anjiplus.template.gaea.business.modules.accessauthority.dao.entity.AccessAuthority;
import com.anjiplus.template.gaea.business.modules.accessauthority.service.AccessAuthorityService;
import com.anjiplus.template.gaea.business.modules.accessauthority.controller.dto.AccessAuthorityDto;
import com.anjiplus.template.gaea.business.modules.accessauthority.controller.param.AccessAuthorityParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
* @desc 权限管理 controller
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:10.009
**/
@RestController
@Api(tags = "权限管理管理")
@RequestMapping("/accessAuthority")
@Permission(code = "authorityManage", name = "权限管理")
public class AccessAuthorityController extends GaeaBaseController<AccessAuthorityParam, AccessAuthority, AccessAuthorityDto> {

    @Autowired
    private AccessAuthorityService accessAuthorityService;

    @Override
    public GaeaBaseService<AccessAuthorityParam, AccessAuthority> getService() {
        return accessAuthorityService;
    }

    @Override
    public AccessAuthority getEntity() {
        return new AccessAuthority();
    }

    @Override
    public AccessAuthorityDto getDTO() {
        return new AccessAuthorityDto();
    }

    /**
     * 获取一二级菜单
     * @return
     */
    @Permission( code = "query", name = "查询")
    @GetMapping("/menuTree")
    public ResponseBean menuTree(){
        String username = UserContentHolder.getContext().getUsername();
        List<TreeNode> parentTreeList = accessAuthorityService.getAuthorityTree(username, false);
        return responseSuccessWithData(parentTreeList);
    }
}