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
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.constant.MqQueueConstant;
import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.template.MobileMsgTemplate;
import com.github.pig.common.vo.SysRole;
import com.github.pig.common.vo.UserVo;
import com.xiaoleilu.hutool.util.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
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
        for (SysRole sysRole : roleList) {
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

    /**
     * 通过手机号查询用户信息
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    @Override
    @Cacheable(value = "user_details_mobile", key = "#mobile")
    public UserVo findUserByMobile(String mobile) {
        return sysUserMapper.selectUserVoByMobile(mobile);
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
    public void saveImageCode(String randomStr, String imageCode) {
        redisTemplate.opsForValue().set(SecurityConstants.DEFAULT_CODE_KEY + randomStr, imageCode, SecurityConstants.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 发送验证码
     * <p>
     * 1. 先去redis 查询是否 60S内已经发送
     * 2. 未发送： 产生4位数字  手机号-验证码
     * 3. 发往消息中心-》发送信息
     * 4. 保存redis
     *
     * @param mobile 手机号
     * @return true、false
     */
    @Override
    public Boolean sendSmsCode(String mobile) {
        Object tempCode = redisTemplate.opsForValue().get(SecurityConstants.DEFAULT_CODE_KEY + mobile);
        boolean result = false;
        if (tempCode == null) {
            String code = RandomUtil.randomNumbers(4);
            logger.info("短信发送请求消息中心 -> 手机号:{} -> 验证码：{}", mobile, code);
            rabbitTemplate.convertAndSend(MqQueueConstant.MOBILE_CODE_QUEUE,new MobileMsgTemplate(mobile,code, CommonConstant.ALIYUN_SMS));
            redisTemplate.opsForValue().set(SecurityConstants.DEFAULT_CODE_KEY + mobile, code, SecurityConstants.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
            result = true;
        }
        return result;
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
