package com.github.pig.admin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.github.pig.admin.entity.SysUser;
import com.github.pig.admin.mapper.SysUserMapper;
import com.github.pig.admin.service.UserService;
import com.github.pig.common.util.Query;
import com.github.pig.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@Service
public class UserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements UserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    @Cacheable(value = "user_details", key = "#username")
    public UserVo findUserByUsername(String username) {
        return sysUserMapper.selectUserVoByUsername(username);
    }

    @Override
    public Page selectWithRolePage(Query query) {
        query.setRecords(sysUserMapper.selectUserVoPage(query,query.getCondition()));
        return query;
    }

    @Override
    @CacheEvict(value = "user_details", key = "#username")
    public void clearCache(String username) {

    }
}
