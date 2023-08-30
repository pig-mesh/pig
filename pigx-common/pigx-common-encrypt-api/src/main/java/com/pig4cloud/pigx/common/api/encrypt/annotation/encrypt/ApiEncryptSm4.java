package com.pig4cloud.pigx.common.api.encrypt.annotation.encrypt;

import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * sm4 加密
 *
 * @author lengleng
 * @version 2023/8/30
 * @see ApiEncrypt
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiEncrypt(EncryptType.SM4)
public @interface ApiEncryptSm4 {

	/**
	 * Alias for {@link ApiEncrypt#secretKey()}.
	 * @return {String}
	 */
	@AliasFor(annotation = ApiEncrypt.class)
	String secretKey() default "";

}
