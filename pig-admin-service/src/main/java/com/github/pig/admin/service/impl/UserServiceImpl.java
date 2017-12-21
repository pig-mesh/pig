package com.github.pig.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.admin.dto.UserDto;
import com.github.pig.admin.dto.UserInfo;
import com.github.pig.admin.entity.SysUser;
import com.github.pig.admin.entity.SysUserRole;
import com.github.pig.admin.mapper.SysUserMapper;
import com.github.pig.admin.service.SysMenuService;
import com.github.pig.admin.service.SysUserRoleService;
import com.github.pig.admin.service.UserService;
import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.common.util.Query;
import com.github.pig.common.vo.SysRole;
import com.github.pig.common.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public UserInfo findUserInfo(UserVo userVo) {
        SysUser condition = new SysUser();
        condition.setUsername(userVo.getUsername());
        SysUser sysUser = this.selectOne(new EntityWrapper<>(condition));

        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        //设置角色列表
        List<SysRole> roleList = userVo.getRoleList();
        List<String> roleNames = new ArrayList<>();
        for (SysRole sysRole:roleList){
            roleNames.add(sysRole.getRoleName());
        }
        String[] roles = roleNames.toArray(new String[roleNames.size()]);
        userInfo.setRoles(roles);
        //设置权限列表（menu.permission）
        String[] permissions = sysMenuService.findPermission(roles);
        userInfo.setPermissions(permissions);
        return userInfo;
    }

    @Override
    @Cacheable(value = "user_details", key = "#username")
    public UserVo findUserByUsername(String username) {
        return sysUserMapper.selectUserVoByUsername(username);
    }

    @Override
    public Page selectWithRolePage(Query query) {
        query.setRecords(sysUserMapper.selectUserVoPage(query, query.getCondition()));
        return query;
    }

    /**
     * 保存用户验证码，和randomStr绑定
     *
     * @param randomStr 客户端生成
     * @param imageCode 验证码信息
     */
    @Override
    public void save(String randomStr, String imageCode) {
        redisTemplate.opsForValue().set(SecurityConstants.DEFAULT_CODE_KEY + randomStr, imageCode, SecurityConstants.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 删除用户
     *
     * @param sysUser 用户
     * @return Boolean
     */
    @Override
    @CacheEvict(value = "user_details", key = "#sysUser.username")
    public Boolean deleteUserById(SysUser sysUser) {
        sysUserRoleService.deleteByUserId(sysUser.getUserId());
        this.deleteById(sysUser.getUserId());
        return Boolean.TRUE;
    }

    @Override
    @CacheEvict(value = "user_details", key = "#username")
    public Boolean updateUserInfo(UserDto userDto, String username) {
        UserVo userVo = this.findUserByUsername(username);

        if (!ENCODER.matches(userDto.getPassword(), userVo.getPassword())) {
            return Boolean.FALSE;
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userVo.getUserId());
        sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
        sysUser.setAvatar(userDto.getAvatar());
        return this.updateById(sysUser);
    }

    @Override
    @CacheEvict(value = "user_details", key = "#username")
    public Boolean updateUser(UserDto userDto, String username) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(new Date());
        this.updateById(sysUser);

        SysUserRole condition = new SysUserRole();
        condition.setUserId(userDto.getUserId());
        SysUserRole sysUserRole = sysUserRoleService.selectOne(new EntityWrapper<>(condition));
        sysUserRole.setRoleId(userDto.getRole());
        return sysUserRoleService.update(sysUserRole, new EntityWrapper<>(condition));
    }
}
