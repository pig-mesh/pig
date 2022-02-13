
package com.anjiplus.template.gaea.business.modules.accessrole.service.impl;

import com.anji.plus.gaea.bean.TreeNode;
import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.anjiplus.template.gaea.business.modules.accessauthority.service.AccessAuthorityService;
import com.anjiplus.template.gaea.business.modules.accessrole.controller.dto.AccessRoleDto;
import com.anjiplus.template.gaea.business.modules.accessrole.dao.AccessRoleAuthorityMapper;
import com.anjiplus.template.gaea.business.modules.accessrole.dao.entity.AccessRole;
import com.anjiplus.template.gaea.business.modules.accessrole.dao.entity.AccessRoleAuthority;
import com.anjiplus.template.gaea.business.modules.accessrole.service.AccessRoleService;
import com.anjiplus.template.gaea.business.modules.accessrole.dao.AccessRoleMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @desc AccessRole 角色管理服务实现
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:14.136
**/
@Service
public class AccessRoleServiceImpl implements AccessRoleService {

    @Autowired
    private AccessRoleMapper accessRoleMapper;

    @Autowired
    private AccessRoleAuthorityMapper accessRoleAuthorityMapper;

    @Autowired
    private AccessAuthorityService accessAuthorityService;

    @Override
    public GaeaBaseMapper<AccessRole> getMapper() {
      return accessRoleMapper;
    }

    @Override
    public Map getAuthorityTree(String roleCode, String operator) {
        Map<String, Object> result = new HashMap<String, Object>();

        // 菜单按钮树
        List<TreeNode> treeData = accessAuthorityService.getAuthorityTree(operator, true);

        // 该角色已选中的菜单及按钮
//        List<String> checkedKeys = accessRoleMapper.checkedAuthoritys(roleCode);

        LambdaQueryWrapper<AccessRoleAuthority> accessRoleAuthorityWrapper = Wrappers.lambdaQuery();
        accessRoleAuthorityWrapper.select(AccessRoleAuthority::getTarget, AccessRoleAuthority::getAction);
        accessRoleAuthorityWrapper.eq(AccessRoleAuthority::getRoleCode, roleCode);
        List<AccessRoleAuthority> accessRoleAuthorities = accessRoleAuthorityMapper.selectList(accessRoleAuthorityWrapper);
        List<String> checkedKeys = accessRoleAuthorities.stream()
                .map(accessRoleAuthority -> accessRoleAuthority.getTarget().concat("_").concat(accessRoleAuthority.getAction())).distinct().collect(Collectors.toList());

        result.put("treeData", treeData);
        result.put("checkedKeys", checkedKeys);
        return result;
    }

    @Override
    public Boolean saveAuthorityTree(AccessRoleDto accessRoleDto) {
        // 校验
        String roleCode = accessRoleDto.getRoleCode();
        List<String> authorityList = accessRoleDto.getAuthorityList();
        if(StringUtils.isBlank(roleCode)){
            throw BusinessExceptionBuilder.build(ResponseCode.NOT_EMPTY, roleCode);
        }
        if(authorityList == null || authorityList.isEmpty()){
            throw BusinessExceptionBuilder.build(ResponseCode.NOT_EMPTY, authorityList);
        }

        // 先清除该角色已保存的权限
        LambdaQueryWrapper<AccessRoleAuthority> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AccessRoleAuthority::getRoleCode, roleCode);
        accessRoleAuthorityMapper.delete(wrapper);

        // 保存勾选的权限(菜单和按钮)
        authorityList.stream().forEach(authorityStr -> {
            if(!authorityStr.contains("_")){
                return;
            }
            String[] array = authorityStr.split("_");
            AccessRoleAuthority accessRoleAuthority = new AccessRoleAuthority();
            accessRoleAuthority.setRoleCode(roleCode);
            accessRoleAuthority.setTarget(array[0].trim());
            accessRoleAuthority.setAction(array[1].trim());
            accessRoleAuthorityMapper.insert(accessRoleAuthority);
        });
        return true;
    }
}
