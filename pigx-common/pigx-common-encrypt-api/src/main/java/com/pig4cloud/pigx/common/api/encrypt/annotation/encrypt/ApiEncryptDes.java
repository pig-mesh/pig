package com.pig4cloud.pigx.common.api.encrypt.annotation.encrypt;

import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * des 加埋
 *
 * @author licoy.cn
 * @author L.cm
 * @version 2018/9/4
 * @see ApiEncrypt
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiEncrypt(EncryptType.DES)
public @interface ApiEncryptDes {

	/**
	 * Alias for {@link ApiEncrypt#secretKey()}.
	 * @return {String}
	 */
	@AliasFor(annotation = ApiEncrypt.class)
	String secretKey() default "";

}
