package com.github.pig.config.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lengleng
 * @date 2017/10/26
 */
@RestController
public class DemoController {
    @GetMapping("/demo")
    public String demo() {
        return "demo method success";
    }
}
