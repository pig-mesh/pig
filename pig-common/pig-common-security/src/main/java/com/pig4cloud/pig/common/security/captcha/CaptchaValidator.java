package com.pig4cloud.pig.common.security.captcha;

import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 行为/图形验证码统一校验入口，支持 blockPuzzle、clickWord、math 三种类型。
 *
 * <p>
 * 调用方传入 {@code captchaType} 与 {@code captchaVerification}， math 类型的
 * {@code captchaVerification} 约定为 {@code randomStr---code}， 与前端组件
 * {@code SmsCodeVerifyButton} / {@code SmsCodeButton} 中的拼装格式一致。
 * </p>
 *
 * @author lengleng
 * @date 2026-05-20
 */
@RequiredArgsConstructor
public class CaptchaValidator {

	private final CaptchaService captchaService;

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 校验验证码。
	 * @param captchaType 验证码类型，取值 blockPuzzle / clickWord / math
	 * @param captchaVerification 验证码凭据；math 类型为 randomStr---code
	 * @return 校验结果，失败时携带提示文案
	 */
	public CaptchaResult validate(String captchaType, String captchaVerification) {
		if (StrUtil.hasBlank(captchaType, captchaVerification)) {
			return CaptchaResult.failed("验证码参数不能为空");
		}

		if (StrUtil.equalsAnyIgnoreCase(captchaType, CommonConstants.IMAGE_CODE_BLOCK_PUZZLE,
				CommonConstants.IMAGE_CODE_CLICK_WORD)) {
			CaptchaVO vo = new CaptchaVO();
			vo.setCaptchaType(captchaType);
			vo.setCaptchaVerification(captchaVerification);
			return captchaService.verification(vo).isSuccess() ? CaptchaResult.ok() : CaptchaResult.failed("验证码错误或已过期");
		}

		if (StrUtil.equalsIgnoreCase(captchaType, CommonConstants.IMAGE_CODE_MATH)) {
			return validateMath(captchaVerification);
		}

		return CaptchaResult.failed("验证码类型不支持");
	}

	private CaptchaResult validateMath(String captchaVerification) {
		String separator = CommonConstants.CAPTCHA_VERIFICATION_SEPARATOR;
		int separatorIndex = captchaVerification.indexOf(separator);
		if (separatorIndex < 0) {
			return CaptchaResult.failed("验证码参数不能为空");
		}

		String randomStr = captchaVerification.substring(0, separatorIndex);
		String code = captchaVerification.substring(separatorIndex + separator.length());
		if (StrUtil.hasBlank(randomStr, code)) {
			return CaptchaResult.failed("验证码参数不能为空");
		}

		String key = CacheConstants.DEFAULT_CODE_KEY + randomStr;
		Object codeObj = redisTemplate.opsForValue().get(key);
		if (codeObj == null || StrUtil.isBlank(codeObj.toString()) || !StrUtil.equals(codeObj.toString(), code)) {
			redisTemplate.delete(key);
			return CaptchaResult.failed("验证码错误或已过期");
		}

		redisTemplate.delete(key);
		return CaptchaResult.ok();
	}

}
