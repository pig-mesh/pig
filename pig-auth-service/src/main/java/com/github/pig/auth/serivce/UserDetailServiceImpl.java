package com.github.pig.auth.serivce;

import com.github.pig.auth.mapper.SysUserMapper;
import com.github.pig.common.vo.SysRole;
import com.github.pig.common.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVo userVo = sysUserMapper.selectUserVoByUsername(username);
        if (userVo != null) {
            return new UserDetails() {
                @Override
                public Collection<? extends GrantedAuthority> getAuthorities() {
                    List<GrantedAuthority> authorityList = new ArrayList<>();
                    if (!CollectionUtils.isEmpty(userVo.getRoleList())) {
                        for (SysRole role : userVo.getRoleList()) {
                            authorityList.add(new SimpleGrantedAuthority(role.getRoleCode()));
                        }
                    }
                    return authorityList;
                }

                @Override
                public String getPassword() {
                    return userVo.getPassword();
                }

                @Override
                public String getUsername() {
                    return userVo.getUsername();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        }
        return null;
    }
}
