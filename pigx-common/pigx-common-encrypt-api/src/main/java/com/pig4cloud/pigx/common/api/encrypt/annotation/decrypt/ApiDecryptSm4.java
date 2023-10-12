package com.pig4cloud.pigx.common.api.encrypt.annotation.decrypt;

import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * sm4 解密
 *
 * @author lengleng
 * @version 2023/8/30
 * @see ApiDecrypt
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiDecrypt(EncryptType.SM4)
public @interface ApiDecryptSm4 {

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
