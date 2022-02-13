
package com.anjiplus.template.gaea.business.modules.accessuser.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.anji.plus.gaea.bean.TreeNode;
import com.anji.plus.gaea.cache.CacheHelper;
import com.anji.plus.gaea.constant.BaseOperationEnum;
import com.anji.plus.gaea.exception.BusinessException;
import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anji.plus.gaea.holder.UserContentHolder;
import com.anji.plus.gaea.utils.GaeaUtils;
import com.anji.plus.gaea.utils.JwtBean;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.anjiplus.template.gaea.business.constant.BusinessConstant;
import com.anjiplus.template.gaea.business.modules.accessrole.dao.AccessRoleAuthorityMapper;
import com.anjiplus.template.gaea.business.modules.accessrole.dao.AccessRoleMapper;
import com.anjiplus.template.gaea.business.modules.accessrole.dao.entity.AccessRole;
import com.anjiplus.template.gaea.business.modules.accessrole.dao.entity.AccessRoleAuthority;
import com.anjiplus.template.gaea.business.modules.accessuser.controller.dto.AccessUserDto;
import com.anjiplus.template.gaea.business.modules.accessuser.controller.dto.GaeaUserDto;
import com.anjiplus.template.gaea.business.modules.accessuser.controller.dto.UpdatePasswordDto;
import com.anjiplus.template.gaea.business.modules.accessuser.dao.AccessUserRoleMapper;
import com.anjiplus.template.gaea.business.modules.accessuser.dao.entity.AccessUser;
import com.anjiplus.template.gaea.business.modules.accessuser.dao.entity.AccessUserRole;
import com.anjiplus.template.gaea.business.modules.accessuser.service.AccessUserService;
import com.anjiplus.template.gaea.business.modules.accessuser.dao.AccessUserMapper;
import com.anjiplus.template.gaea.business.util.MD5Util;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
* @desc AccessUser 用户管理服务实现
* @author 木子李·De <lide1202@hotmail.com>
* @date 2019-02-17 08:50:11.902
**/
@Service
public class AccessUserServiceImpl implements AccessUserService {

    @Autowired
    private AccessRoleMapper accessRoleMapper;

    @Autowired
    private AccessUserMapper accessUserMapper;

    @Autowired
    private AccessUserRoleMapper accessUserRoleMapper;

    @Autowired
    private AccessRoleAuthorityMapper accessRoleAuthorityMapper;

    @Value("${customer.user.default.password:'123456'}")
    private String defaultPassword;

    @Override
    public GaeaBaseMapper<AccessUser> getMapper() {
      return accessUserMapper;
    }

    @Autowired
    private JwtBean jwtBean;

    @Autowired
    private CacheHelper cacheHelper;

    @Override
    public Map getRoleTree(String loginName, String operator) {
        Map<String, Object> result = new HashMap<String, Object>();
        List<TreeNode> treeData = new ArrayList<>();
        List checkedKeys = new ArrayList();

        // 角色树
        LambdaQueryWrapper<AccessRole> roleQuery = Wrappers.lambdaQuery();
        roleQuery.select(AccessRole::getRoleCode, AccessRole::getRoleName);
        // 按operator去筛选role 后面再加where
        List<AccessRole> roleList = accessRoleMapper.selectList(roleQuery);
        if(roleList == null || roleList.isEmpty()){
            result.put("treeData", treeData);
            result.put("checkedKeys", checkedKeys);
            return result;
        }
        treeData = roleList.stream().map(role -> {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(role.getRoleCode());
            treeNode.setLabel(role.getRoleName());
            return treeNode;
        }).collect(Collectors.toList());

        // 该用户已保存的角色
        LambdaQueryWrapper<AccessUserRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(AccessUserRole::getRoleCode)
                .eq(AccessUserRole::getLoginName, loginName);
        checkedKeys = accessUserRoleMapper.selectObjs(queryWrapper);

        result.put("treeData", treeData);
        result.put("checkedKeys", checkedKeys);
        return result;
    }

    @Override
    public Boolean saveRoleTree(AccessUserDto accessUserDto) {
        // 校验
        String loginName = accessUserDto.getLoginName();
        List<String> roleCodeList = accessUserDto.getRoleCodeList();
        if(StringUtils.isBlank(loginName)){
            throw BusinessExceptionBuilder.build(ResponseCode.NOT_EMPTY, loginName);
        }
        if(roleCodeList == null || roleCodeList.isEmpty()){
            throw BusinessExceptionBuilder.build(ResponseCode.NOT_EMPTY, roleCodeList);
        }

        // 先清除该用户已保存的角色
        LambdaQueryWrapper<AccessUserRole> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AccessUserRole::getLoginName, loginName);
        accessUserRoleMapper.delete(wrapper);

        // 保存勾选的角色
        roleCodeList.stream().forEach(roleCode -> {
            AccessUserRole accessUserRole = new AccessUserRole();
            accessUserRole.setLoginName(loginName);
            accessUserRole.setRoleCode(roleCode);
            accessUserRoleMapper.insert(accessUserRole);
        });
        return true;
    }

    @Override
    public Boolean resetPassword(GaeaUserDto gaeaUserDto) {
        return false;
    }

    @Override
    public GaeaUserDto login(GaeaUserDto gaeaUserDto) {

        String loginName = gaeaUserDto.getLoginName();
        String password = gaeaUserDto.getPassword();

        // 1.判断用户是否存在
        LambdaQueryWrapper<AccessUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AccessUser::getLoginName, loginName);
        AccessUser accessUser = accessUserMapper.selectOne(wrapper);
        if (null == accessUser) {
            throw BusinessExceptionBuilder.build(ResponseCode.LOGIN_ERROR);
        }
        // 2.密码错误
        if (!accessUser.getPassword().equals(MD5Util.encrypt(password))) {
            throw BusinessExceptionBuilder.build(ResponseCode.USER_PASSWORD_ERROR);
        }

        // 3.如果该用户登录未过期，这里允许一个用户在多个终端登录
        String tokenKey = String.format(BusinessConstant.GAEA_SECURITY_LOGIN_TOKEN, loginName);
        String token = "";
        GaeaUserDto gaeaUser = new GaeaUserDto();
        if (cacheHelper.exist(tokenKey)) {
            token = cacheHelper.stringGet(tokenKey);
        } else {
            // 生成用户token
            String uuid = GaeaUtils.UUID();
            token = jwtBean.createToken(loginName, uuid);
            cacheHelper.stringSetExpire(tokenKey, token, 3600);
        }

        // 4.读取用户最新人权限主信息
        String userKey = String.format(BusinessConstant.GAEA_SECURITY_LOGIN_USER, loginName);

        //为了兼容底层其他数据库，不再写自定义sql
//        List<String> authorities = accessUserMapper.queryAuthoritiesByLoginName(loginName);

        //当前用户的roleCode集合
        LambdaQueryWrapper<AccessUserRole> accessUserWrapper = Wrappers.lambdaQuery();
        accessUserWrapper.select(AccessUserRole::getRoleCode);
        accessUserWrapper.eq(AccessUserRole::getLoginName, loginName);
        List<AccessUserRole> accessUserRoles = accessUserRoleMapper.selectList(accessUserWrapper);
        Set<String> roleCodeSet = accessUserRoles.stream().map(AccessUserRole::getRoleCode).collect(Collectors.toSet());
        if (roleCodeSet.size() < 1) {
            gaeaUser.setAuthorities(new ArrayList<>());
        }else {
            LambdaQueryWrapper<AccessRoleAuthority> accessRoleAuthorityWrapper = Wrappers.lambdaQuery();
            accessRoleAuthorityWrapper.select(AccessRoleAuthority::getTarget, AccessRoleAuthority::getAction);
            accessRoleAuthorityWrapper.in(AccessRoleAuthority::getRoleCode, roleCodeSet);
            List<AccessRoleAuthority> accessRoleAuthorities = accessRoleAuthorityMapper.selectList(accessRoleAuthorityWrapper);
            List<String> authorities = accessRoleAuthorities.stream()
                    .map(accessRoleAuthority -> accessRoleAuthority.getTarget().concat(":").concat(accessRoleAuthority.getAction())).distinct().collect(Collectors.toList());
            gaeaUser.setAuthorities(authorities);
        }

        gaeaUser.setLoginName(loginName);
        gaeaUser.setRealName(accessUser.getRealName());
        gaeaUser.setToken(token);

        String gaeaUserStr = JSONObject.toJSONString(gaeaUser);
        cacheHelper.stringSetExpire(userKey, gaeaUserStr, 3600);

        return gaeaUser;
    }

    /**
     * 修改密码
     *
     * @param dto
     * @return
     */
    @Override
    public Boolean updatePassword(UpdatePasswordDto dto) {
        if (!dto.getConfirmPassword().equals(dto.getPassword())) {
            //密码和确认密码不一致
            throw BusinessExceptionBuilder.build(ResponseCode.USER_INCONSISTENT_PASSWORD_ERROR);
        }
        //新密码不能与老密码一样
        if(StringUtils.equals(dto.getOldPassword(), dto.getPassword())){
            throw BusinessExceptionBuilder.build(ResponseCode.USER_PASSWORD_CONFIG_PASSWORD_CANOT_EQUAL);
        }

        String username = UserContentHolder.getUsername();


        LambdaQueryWrapper<AccessUser> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AccessUser::getLoginName, username);
        AccessUser accessUser = selectOne(wrapper);
        String password = accessUser.getPassword();
        if (!MD5Util.encrypt(dto.getOldPassword()).equals(password)) {
            throw BusinessExceptionBuilder.build(ResponseCode.USER_OLD_PASSWORD_ERROR);
        }
        accessUser.setPassword(MD5Util.encrypt(dto.getPassword()));

        accessUserMapper.updateById(accessUser);
        return true;
    }

    /**
     * 操作前处理
     *
     * @param entity        前端传递的对象
     * @param operationEnum 操作类型
     * @throws BusinessException 阻止程序继续执行或回滚事务
     */
    @Override
    public void processBeforeOperation(AccessUser entity, BaseOperationEnum operationEnum) throws BusinessException {
        //过滤密码
        switch (operationEnum) {
            case INSERT:
                //gaea是为了和前端加密保持一致
                entity.setPassword(MD5Util.encrypt(MD5Util.encrypt(defaultPassword.concat("gaea"))));
                break;
            case UPDATE:
                //更新用户不允许修改密码
                entity.setPassword(null);
                break;
            default:

                break;
        }

    }
}
