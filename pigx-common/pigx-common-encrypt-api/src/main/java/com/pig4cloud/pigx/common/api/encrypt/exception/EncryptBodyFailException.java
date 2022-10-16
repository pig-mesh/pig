package com.pig4cloud.pigx.common.api.encrypt.exception;

/**
 * <p>
 * 加密数据失败异常
 * </p>
 *
 * @author licoy.cn
 * @version 2018/9/6
 */
public class EncryptBodyFailException extends RuntimeException {

	public EncryptBodyFailException() {
		super("Encrypted data failed. (加密数据失败)");
	}

	public EncryptBodyFailException(String message) {
		super(message);
	}

}
