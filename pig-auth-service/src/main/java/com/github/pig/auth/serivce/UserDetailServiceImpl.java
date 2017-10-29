package com.github.pig.auth.serivce;

import com.github.pig.auth.mapper.SysUserMapper;
import com.github.pig.auth.util.UserInfo;
import com.github.pig.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author lengleng
 * @date 2017/10/26
 * <p>
 * TODO 通过调用 admin模块
 */
@Service("userDetailService")
public class UserDetailServiceImpl implements UserDetailsService, Serializable {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    @Cacheable(value = "user_details",key = "#username + '::loadUserByUsername'")
    public UserInfo loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVo userVo = sysUserMapper.selectUserVoByUsername(username);
        return new UserInfo(userVo);
    }
}
