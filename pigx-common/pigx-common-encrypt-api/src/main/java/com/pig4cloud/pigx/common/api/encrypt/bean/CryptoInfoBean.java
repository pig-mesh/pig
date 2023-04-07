package com.pig4cloud.pigx.common.api.encrypt.bean;

import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 加密注解信息
 * </p>
 *
 * @author licoy.cn
 * @author L.cm
 * @version 2018/9/6
 */
@Getter
@RequiredArgsConstructor
public class CryptoInfoBean {

	/**
	 * 加密类型
	 */
	private final EncryptType type;

	/**
	 * 私钥
	 */
	private final String secretKey;

}
