package com.github.pig.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangjie19558
 * @date 2017/10/12
 */
@RestController
public class IndexController {
    @Value("${from}")
    private String from;

    @GetMapping("/")
    public String index() {
        return from;
    }
}
