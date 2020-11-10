package com.pig4cloud.pig.common.core.mybatis;

import cn.hutool.core.util.StrUtil;
import org.junit.Test;

/**
 * @author lengleng
 * @date 2020/10/12
 */
public class SqlFilterArgumentResolverTest {

    @Test
    public void supportsParameter() {
    }

    @Test
    public void resolveArgument() {
    	String param = "delete create_time";

		System.out.println(clear(param));
	}

	private String clear(String param) {
		if (StrUtil.isBlank(param)) {
			return StrUtil.trim(param);
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < param.length(); i++) {
			char c = param.charAt(i);
			if (Character.isJavaIdentifierPart(c)) {
				builder.append(c);
			}
		}
		return builder.toString();
	}
}