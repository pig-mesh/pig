package com.pig4cloud.pig.auth.endpoint;

import cn.hutool.core.lang.Validator;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import io.springboot.captcha.ArithmeticCaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 验证码相关的接口
 *
 * @author lengleng
 * @date 2022/6/27
 */
@RestController
@RequestMapping("/code")
@RequiredArgsConstructor
public class ImageCodeEndpoint {

    private static final Integer DEFAULT_IMAGE_WIDTH = 100;

    private static final Integer DEFAULT_IMAGE_HEIGHT = 40;

    /**
     * 创建图形验证码
     */
    @SneakyThrows
    @GetMapping("/image")
    public void image(String randomStr, HttpServletResponse response) {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);

        String result = captcha.text();

        // 如果是手机号码，不存储下发验证码
        if (Validator.isMobile(randomStr)) {
            return;
        }

        RedisUtils.set(CacheConstants.DEFAULT_CODE_KEY + randomStr, result, SecurityConstants.CODE_TIME);
        // 转换流信息写出
        captcha.out(response.getOutputStream());
    }

    /**
     * 行为 创建滑块图形验证码
     */
    @SneakyThrows
    @GetMapping("/create")
    public R behavior(String captchaType) {
        CaptchaVO vo = new CaptchaVO();
        vo.setCaptchaType(captchaType);
        CaptchaService captchaService = SpringContextHolder.getBean(CaptchaService.class);
        return R.ok(captchaService.get(vo));
    }

    /**
     * 行为 创建滑块图形验证码
     */
    @SneakyThrows
    @PostMapping("/check")
    public R check(HttpServletRequest request) {
        CaptchaVO vo = new CaptchaVO();
        vo.setPointJson(request.getParameter("pointJson"));
        vo.setToken(request.getParameter("token"));
        vo.setCaptchaType(request.getParameter("captchaType"));

        CaptchaService captchaService = SpringContextHolder.getBean(CaptchaService.class);
        return R.ok(captchaService.check(vo));
	}

}
