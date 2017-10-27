package com.github.pig.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author lengleng
 * @date 2017/10/26
 */
@RestController
public class UserController {
    @GetMapping("/user")
    public Object user(Principal user) {
        return user;
    }

}