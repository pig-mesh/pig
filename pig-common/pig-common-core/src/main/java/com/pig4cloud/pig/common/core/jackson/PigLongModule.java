package com.pig4cloud.pig.common.core.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Long / long 序列化为 String 的 Jackson 模块，避免前端 JS Number 53-bit 精度丢失。
 * <p>
 * 与 {@link PigJavaTimeModule} 拆开是为了职责单一：本 module 只处理整型序列化， 单独使用也能保证 Long 字符串化能力，不依赖
 * {@code JacksonConfiguration}。
 *
 * @author lengleng
 */
public class PigLongModule extends SimpleModule {

	public PigLongModule() {
		super("PigLongModule");
		this.addSerializer(Long.class, ToStringSerializer.instance);
		this.addSerializer(Long.TYPE, ToStringSerializer.instance);
	}

}
