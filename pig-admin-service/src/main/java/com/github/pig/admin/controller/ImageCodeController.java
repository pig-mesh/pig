package com.github.pig.admin.controller;

import com.github.pig.admin.common.util.ImageCodeGenerator;
import com.github.pig.admin.service.UserService;
import com.github.pig.common.constant.SecurityConstants;
import com.github.pig.common.vo.ImageCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lengleng
 * @date 2017/12/18
 */
@Controller
public class ImageCodeController {
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
        ImageCode imageCode = new ImageCodeGenerator().generate(new ServletWebRequest(request));
        userService.save(randomStr, imageCode);
        ImageIO.write(imageCode.getImage(), "JPEG",response.getOutputStream());
    }
}
