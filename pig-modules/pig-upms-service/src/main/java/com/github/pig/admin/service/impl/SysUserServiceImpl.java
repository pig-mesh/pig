package com.github.pig.admin.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.admin.mapper.SysUserMapper;
import com.github.pig.admin.model.dto.UserDTO;
import com.github.pig.admin.model.dto.UserInfo;
import com.github.pig.admin.model.entity.SysDeptRelation;
import com.github.pig.admin.model.entity.SysUser;
import com.github.pig.admin.model.entity.SysUserRole;
import com.github.pig.admin.service.SysDeptRelationService;
import com.github.pig.admin.service.SysMenuService;
import com.github.pig.admin.service.SysUserRoleService;
import com.github.pig.admin.service.SysUserService;
import com.github.pig.common.bean.interceptor.DataScope;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.constant.MqQueueConstant;
import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.common.util.UserUtils;
import com.github.pig.common.util.template.MobileMsgTemplate;
import com.github.pig.common.vo.SysRole;
import com.github.pig.common.vo.UserVO;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.util.RandomUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
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
    @Autowired
    private SysDeptRelationService sysDeptRelationService;

    @Override
    public UserInfo findUserInfo(UserVO userVo) {
        SysUser condition = new SysUser();
        condition.setUsername(userVo.getUsername());
        SysUser sysUser = this.selectOne(new EntityWrapper<>(condition));

        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        //设置角色列表
        List<SysRole> roleList = userVo.getRoleList();
        List<String> roleNames = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(roleList)) {
            for (SysRole sysRole : roleList) {
                if (!StrUtil.equals(SecurityConstants.BASE_ROLE, sysRole.getRoleName())) {
                    roleNames.add(sysRole.getRoleName());
                }
            }
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
    public UserVO findUserByUsername(String username) {
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
    public UserVO findUserByMobile(String mobile) {
        return sysUserMapper.selectUserVoByMobile(mobile);
    }

    /**
     * 通过openId查询用户
     *
     * @param openId openId
     * @return 用户信息
     */
    @Override
    @Cacheable(value = "user_details_openid", key = "#openId")
    public UserVO findUserByOpenId(String openId) {
        return sysUserMapper.selectUserVoByOpenId(openId);
    }

    @Override
    public Page selectWithRolePage(Query query) {
        DataScope dataScope = new DataScope();
        dataScope.setScopeName("deptId");
        dataScope.setIsOnly(true);
        dataScope.setDeptIds(getChildDepts());
        dataScope.putAll(query.getCondition());
        query.setRecords(sysUserMapper.selectUserVoPageDataScope(query, dataScope));
        return query;
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @Override
    public UserVO selectUserVoById(Integer id) {
        return sysUserMapper.selectUserVoById(id);
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
     * 2. 未发送： 判断手机号是否存 ? false :产生4位数字  手机号-验证码
     * 3. 发往消息中心-》发送信息
     * 4. 保存redis
     *
     * @param mobile 手机号
     * @return true、false
     */
    @Override
    public R<Boolean> sendSmsCode(String mobile) {
        Object tempCode = redisTemplate.opsForValue().get(SecurityConstants.DEFAULT_CODE_KEY + mobile);
        if (tempCode != null) {
            log.error("用户:{}验证码未失效{}", mobile, tempCode);
            return new R<>(false, "验证码未失效，请失效后再次申请");
        }

        SysUser params = new SysUser();
        params.setIntroduction(mobile);
        List<SysUser> userList = this.selectList(new EntityWrapper<>(params));

        if (CollectionUtil.isEmpty(userList)) {
            log.error("根据用户手机号{}查询用户为空", mobile);
            return new R<>(false, "手机号不存在");
        }

        String code = RandomUtil.randomNumbers(4);
        log.info("短信发送请求消息中心 -> 手机号:{} -> 验证码：{}", mobile, code);
        rabbitTemplate.convertAndSend(MqQueueConstant.MOBILE_CODE_QUEUE, new MobileMsgTemplate(mobile, code, CommonConstant.ALIYUN_SMS));
        redisTemplate.opsForValue().set(SecurityConstants.DEFAULT_CODE_KEY + mobile, code, SecurityConstants.DEFAULT_IMAGE_EXPIRE, TimeUnit.SECONDS);
        return new R<>(true);
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
    public Boolean updateUserInfo(UserDTO userDto, String username) {
        UserVO userVo = this.findUserByUsername(username);

        SysUser sysUser = new SysUser();
        if (ENCODER.matches(userDto.getPassword(), userVo.getPassword())) {
            sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
        }
        sysUser.setUserId(userVo.getUserId());
        sysUser.setAvatar(userDto.getAvatar());
        return this.updateById(sysUser);
    }

    @Override
    @CacheEvict(value = "user_details", key = "#username")
    public Boolean updateUser(UserDTO userDto, String username) {
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

    /**
     * 获取当前用户的子部门信息
     *
     * @return 子部门列表
     */
    private List<Integer> getChildDepts() {
        //获取当前用户的部门
        String username = UserUtils.getUser();
        UserVO userVo = findUserByUsername(username);
        Integer deptId = userVo.getDeptId();

        //获取当前部门的子部门
        SysDeptRelation deptRelation = new SysDeptRelation();
        deptRelation.setAncestor(deptId);
        List<SysDeptRelation> deptRelationList = sysDeptRelationService.selectList(new EntityWrapper<>(deptRelation));
        List<Integer> deptIds = new ArrayList<>();
        for (SysDeptRelation sysDeptRelation : deptRelationList) {
            deptIds.add(sysDeptRelation.getDescendant());
        }
        return deptIds;
    }

}
