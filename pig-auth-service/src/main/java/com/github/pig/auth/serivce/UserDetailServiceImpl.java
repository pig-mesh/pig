package com.github.pig.auth.serivce;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;

/**
 * @author lengleng
 * @date 2017/10/26
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService, Serializable {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                GrantedAuthority grantedAuthority = (GrantedAuthority) () -> "ROLE_ADMIN";
                grantedAuthorities.add(grantedAuthority);
                return grantedAuthorities;
            }

            @Override
            public String getPassword() {
                return "admin";
            }

            @Override
            public String getUsername() {
                return "admin";
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
}
