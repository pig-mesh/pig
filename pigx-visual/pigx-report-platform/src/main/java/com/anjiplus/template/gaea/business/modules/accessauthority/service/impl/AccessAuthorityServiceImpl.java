
package com.anjiplus.template.gaea.business.modules.accessauthority.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.anji.plus.gaea.bean.TreeNode;
import com.anji.plus.gaea.cache.CacheHelper;
import com.anji.plus.gaea.constant.Enabled;
import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anji.plus.gaea.init.InitRequestUrlMappings;
import com.anjiplus.template.gaea.business.constant.BusinessConstant;
import com.anjiplus.template.gaea.business.modules.accessauthority.dao.entity.AccessAuthority;
import com.anjiplus.template.gaea.business.modules.accessauthority.service.AccessAuthorityService;
import com.anjiplus.template.gaea.business.modules.accessauthority.dao.AccessAuthorityMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @desc AccessAuthority 权限管理服务实现
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:10.009
**/
@Service
public class AccessAuthorityServiceImpl implements AccessAuthorityService {

    @Autowired
    private InitRequestUrlMappings initRequestUrlMappings;

    @Autowired
    private CacheHelper cacheHelper;

    @Autowired
    private AccessAuthorityMapper accessAuthorityMapper;

    @Override
    public GaeaBaseMapper<AccessAuthority> getMapper() {
      return accessAuthorityMapper;
    }

    @Override
    public List<TreeNode> getAuthorityTree(String loginName, boolean withActionNode) {
        // 查询出所有的菜单记录
        LambdaQueryWrapper<AccessAuthority> wrapper = Wrappers.lambdaQuery();
        wrapper.select(AccessAuthority::getParentTarget, AccessAuthority::getTarget, AccessAuthority::getTargetName, AccessAuthority::getAction, AccessAuthority::getActionName)
                .eq(AccessAuthority::getEnableFlag, 1)
                .eq(AccessAuthority::getDeleteFlag, 0)
                .orderByAsc(AccessAuthority::getSort);
        // 按operator去筛选 后面再加where
        List<AccessAuthority> accessAuthorityList = getMapper().selectList(wrapper);

        // 筛选出一级菜单
        List<TreeNode> parentNodes = accessAuthorityList.stream().filter(accessAuthority -> StringUtils.isBlank(accessAuthority.getParentTarget()))
                .map(item -> {
                    TreeNode treeNode = new TreeNode();
                    treeNode.setId(item.getTarget());
                    treeNode.setLabel(item.getTargetName());
                    return treeNode;
                }).collect(Collectors.toList());

        // 菜单-按钮的map
        Map<String, List<AccessAuthority>> targetActionsMap = accessAuthorityList.stream()
                .filter(accessAuthority -> StringUtils.isNoneBlank(accessAuthority.getParentTarget()))
                .collect(Collectors.groupingBy(AccessAuthority::getTarget));

        // 设置每个一级菜单的二菜单
        parentNodes.stream().forEach(parentNode -> {
            List<String> alreadyTargets = new ArrayList<>();

            accessAuthorityList.stream().forEach(authority -> {
                if(!StringUtils.equals(parentNode.getId(), authority.getParentTarget()) || alreadyTargets.contains(authority.getTarget())){
                    return;
                }
                // 找到一级菜单对应的二级菜单

                // 初始化二级菜单节点
                TreeNode levelTwoMenuNode = new TreeNode();
                levelTwoMenuNode.setId(authority.getTarget());
                levelTwoMenuNode.setLabel(authority.getTargetName());
                levelTwoMenuNode.setChildren(new ArrayList<TreeNode>());

                // 初始化二级菜单的按钮
                if(withActionNode){
                    List<AccessAuthority> actionList = targetActionsMap.get(authority.getTarget());
                    if(actionList != null && !actionList.isEmpty()){
                        actionList.stream().forEach(action ->{
                            TreeNode buttonNode = new TreeNode();
                            buttonNode.setId(String.format("%s_%s", action.getTarget(), action.getAction()));
                            buttonNode.setLabel(action.getActionName());
                            levelTwoMenuNode.getChildren().add(buttonNode);
                        });
                    }
                }

                // 将上面找到的二级菜单加入到一菜单的子树中去
                List<TreeNode> children = parentNode.getChildren();
                if(children == null){
                    children = new ArrayList<TreeNode>();
                }
                children.add(levelTwoMenuNode);
                parentNode.setChildren(children);

                // 已经找过的二级菜单，后面不在重复添加
                alreadyTargets.add(authority.getTarget());
            });
        });

        return parentNodes;
    }

    @Override
    public void scanGaeaSecurityAuthorities() {
        /* 获取当前应用中所有的请求信息
        {
            "applicationName": "aj-report",
            "authCode": "authorityManage:query",
            "authName": "权限管理查询",
            "beanName": "accessAuthorityController",
            "menuCode": "authorityManage",
            "path": "GET#/accessAuthority/menuTree"
        }*/
        List<InitRequestUrlMappings.RequestInfo> requestInfos = initRequestUrlMappings.getRequestInfos(Enabled.YES.getValue());

        // key="GET#/accessAuthority/menuTree" value="authorityManage:query"
        Map<String, String>  securityAuthorityMap = new HashMap<String, String>();
        requestInfos.stream().forEach(requestInfo -> {
            securityAuthorityMap.put(requestInfo.getPath(), requestInfo.getAuthCode());
        });

        // 将key存入到缓存中
        cacheHelper.hashSet(BusinessConstant.GAEA_SECURITY_AUTHORITIES, securityAuthorityMap);
    }
}