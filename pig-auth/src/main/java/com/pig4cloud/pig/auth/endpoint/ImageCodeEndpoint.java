package com.pig4cloud.pig.auth.endpoint;

import cn.hutool.core.lang.Validator;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import io.springboot.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
public class ImageCodeEndpoint {

	private static final Integer DEFAULT_IMAGE_WIDTH = 100;

	private static final Integer DEFAULT_IMAGE_HEIGHT = 40;

	private final StringRedisTemplate redisTemplate;

	/**
	 * 创建图形验证码
	 */
	@SneakyThrows
	@GetMapping("/image")
	public void image(String randomStr, HttpServletResponse response) {
		ArithmeticCaptcha captcha = new ArithmeticCaptcha(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);

		if (Validator.isMobile(randomStr)) {
			return;
		}

		String result = captcha.text();
		redisTemplate.opsForValue()
			.set(CacheConstants.DEFAULT_CODE_KEY + randomStr, result, SecurityConstants.CODE_TIME, TimeUnit.SECONDS);
		// 转换流信息写出
		captcha.out(response.getOutputStream());
	}

}
