package com.github.pig.admin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.github.pig.admin.dto.UserInfo;
import com.github.pig.admin.entity.SysUser;
import com.github.pig.common.util.Query;
import com.github.pig.common.vo.ImageCode;
import com.github.pig.common.vo.UserVo;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.List;

/**
 * @author lengleng
 * @date 2017/10/31
 */
public interface UserService extends IService<SysUser> {
    /**
     * 根据用户名查询用户角色信息
     *
     * @param username 用户名
     * @return userVo
     */
    UserVo findUserByUsername(String username);

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param query 查询条件
     * @return
     */
    Page selectWithRolePage(Query query);

    /**
     * 清除用户缓存
     *
     * @param userName 用户名
     */
    void clearCache(String userName);

    /**
     * 查询用户信息
     *
     * @param roleNames 角色名
     * @return userInfo
     */
    UserInfo findUserInfo(List<String> roleNames);

    /**
     * 保存验证码
     *
     * @param randomStr 随机串
     * @param imageCode 验证码
     */
    void save(String randomStr, ImageCode imageCode);
}
