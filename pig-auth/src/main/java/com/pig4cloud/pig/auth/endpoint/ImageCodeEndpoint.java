package com.pig4cloud.pig.auth.endpoint;

import cn.hutool.core.lang.Validator;
import com.pig4cloud.captcha.ArithmeticCaptcha;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.RedisUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * 验证码相关的接口
 *
 * @author lengleng
 * @date 2022/6/27
 */
@RestController
@RequestMapping("/code")
@RequiredArgsConstructor
@Tag(description = "code", name = "验证码控制器管理模块")
public class ImageCodeEndpoint {

	private static final Integer DEFAULT_IMAGE_WIDTH = 100;

	private static final Integer DEFAULT_IMAGE_HEIGHT = 40;

	/**
	 * 创建图形验证码并输出到响应流
	 * @param randomStr 随机字符串，用于缓存验证码
	 * @param response HTTP响应对象，用于输出验证码图片
	 */
	@SneakyThrows
	@GetMapping("/image")
	@Operation(summary = "创建图形验证码并输出到响应流", description = "创建图形验证码并输出到响应流")
	public void image(String randomStr, HttpServletResponse response) {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);

		if (Validator.isMobile(randomStr)) {
			return;
		}

		String result = captcha.text();
		RedisUtils.set(CacheConstants.DEFAULT_CODE_KEY + randomStr, result, SecurityConstants.CODE_TIME,
				TimeUnit.SECONDS);
		// 转换流信息写出
		captcha.out(response.getOutputStream());
	}

}
