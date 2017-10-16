package com.github.pig.service.impl;

import com.github.pig.service.AuthService;
import com.github.pig.service.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lengleng
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public String login(String username, String password) throws Exception {
        return jwtTokenUtil.generateToken(username);
    }

    @Override
    public void validate(String token) throws Exception {
    }

    @Override
    public Boolean invalid(String token) {
        return null;
    }

    @Override
    public String refresh(String oldToken) {
        return null;
    }

    @Override
    public String getUserNameByToken(String token) {
        return jwtTokenUtil.getUsernameFromToken(token);
    }
}
