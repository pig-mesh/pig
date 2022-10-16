package com.pig4cloud.pigx.common.api.encrypt.annotation.encrypt;

import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * aes 加密
 *
 * @author licoy.cn
 * @author L.cm
 * @version 2018/9/4
 * @see ApiEncrypt
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiEncrypt(EncryptType.AES)
public @interface ApiEncryptAes {

	/**
	 * Alias for {@link ApiEncrypt#secretKey()}.
	 * @return {String}
	 */
	@AliasFor(annotation = ApiEncrypt.class)
	String secretKey() default "";

}
