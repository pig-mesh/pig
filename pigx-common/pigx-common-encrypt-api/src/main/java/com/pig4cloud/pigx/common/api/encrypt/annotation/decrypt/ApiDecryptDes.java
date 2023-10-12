package com.pig4cloud.pigx.common.api.encrypt.annotation.decrypt;

import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @author licoy.cn
 * @version 2018/9/7
 * @see ApiDecrypt
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiDecrypt(EncryptType.DES)
public @interface ApiDecryptDes {

	/**
	 * 参数名称 仅针对GET请求有效
	 * @return string
	 */
	@AliasFor(annotation = ApiDecrypt.class, attribute = "parameter")
	String value() default "";

	/**
	 * Alias for {@link ApiDecrypt#secretKey()}.
	 * @return {String}
	 */
	@AliasFor(annotation = ApiDecrypt.class)
	String secretKey() default "";

}
