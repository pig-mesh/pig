package com.pig4cloud.pigx.common.api.encrypt.exception;

/**
 * <p>
 * 解密数据失败异常
 * </p>
 *
 * @author licoy.cn
 * @version 2018/9/6
 */
public class DecryptBodyFailException extends RuntimeException {

	public DecryptBodyFailException(String message) {
		super(message);
	}

}
