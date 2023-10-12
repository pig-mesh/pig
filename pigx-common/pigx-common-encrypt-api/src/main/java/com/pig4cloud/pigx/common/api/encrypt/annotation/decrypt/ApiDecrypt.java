package com.pig4cloud.pigx.common.api.encrypt.annotation.decrypt;

import com.pig4cloud.pigx.common.api.encrypt.enums.EncryptType;

import java.lang.annotation.*;

/**
 * <p>
 * 解密含有{@link org.springframework.web.bind.annotation.RequestBody}注解的参数请求数据，可用于整个控制类或者某个控制器上
 * </p>
 *
 * @author licoy.cn
 * @author L.cm
 * @version 2018/9/7
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface ApiDecrypt {

	/**
	 * 参数名称 仅针对GET请求有效
	 * @return string
	 */
	String parameter() default "";

	/**
	 * 解密类型
	 * @return 类型
	 */
	EncryptType value();

	/**
	 * 私钥，用于某些需要单独配置私钥的方法，没有时读取全局配置的私钥
	 * @return 私钥
	 */
	String secretKey() default "";

}
