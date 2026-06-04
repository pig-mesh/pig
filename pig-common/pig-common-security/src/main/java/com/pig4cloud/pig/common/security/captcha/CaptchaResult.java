package com.pig4cloud.pig.common.security.captcha;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 验证码校验结果。失败时携带提示文案，由上层决定包装成异常或 {@code R.failed}。
 *
 * @author lengleng
 * @date 2026-05-20
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CaptchaResult {

	/**
	 * 校验是否通过
	 */
	private final boolean ok;

	/**
	 * 失败时的提示文案，校验成功时为空
	 */
	private final String errorMessage;

	/**
	 * 构造验证码校验成功结果。
	 * @return 成功结果，错误提示为空
	 */
	public static CaptchaResult ok() {
		return new CaptchaResult(true, null);
	}

	/**
	 * 构造验证码校验失败结果。
	 * @param errorMessage 失败提示文案
	 * @return 失败结果
	 */
	public static CaptchaResult failed(String errorMessage) {
		return new CaptchaResult(false, errorMessage);
	}

}
