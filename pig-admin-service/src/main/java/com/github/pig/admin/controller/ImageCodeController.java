package com.github.pig.admin.controller;

import com.github.pig.admin.service.UserService;
import com.github.pig.common.constant.SecurityConstants;
import com.google.code.kaptcha.Producer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * @author lengleng
 * @date 2017/12/18
 */
@Controller
public class ImageCodeController {
    @Autowired
    private Producer producer;
    @Autowired
    private UserService userService;

    /**
     * 创建验证码
     *
     * @param request request
     * @throws Exception
     */
    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{randomStr}")
    public void createCode(@PathVariable String randomStr, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        //生成文字验证码
        String text = producer.createText();
        //生成图片验证码
        BufferedImage image = producer.createImage(text);
        userService.save(randomStr, text);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(image, "JPEG", out);
        IOUtils.closeQuietly(out);
    }
}
