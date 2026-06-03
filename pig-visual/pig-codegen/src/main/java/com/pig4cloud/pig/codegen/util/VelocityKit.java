package com.pig4cloud.pig.codegen.util;

import cn.hutool.core.util.CharsetUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * 模板引擎工具类
 *
 * @author lengleng
 * @date 2023/2/7
 */
@Service
public class VelocityKit {

	/**
	 * Velocity 模板渲染方法
	 * @param template 模板
	 * @param map 数据模型
	 * @return 渲染结果
	 */
	public static String render(String template, Map<String, Object> map) {
		// 设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("resource.loader.file.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(prop);

		VelocityContext context = new VelocityContext(map);
		// 函数库，使用 Lambda 表达式简化代码
		Optional.of(new MathTool()).ifPresent(mt -> context.put("math", mt));
		Optional.of(new DateTool()).ifPresent(dt -> context.put("dateTool", dt));
		Optional.of(new DictTool()).ifPresent(dt -> context.put("dict", dt));
		Optional.of(new NamingCaseTool()).ifPresent(nct -> context.put("str", nct));

		// 渲染模板，使用 Lambda 表达式简化代码
		StringWriter sw = new StringWriter();
		Optional.ofNullable(Velocity.getTemplate(template, CharsetUtil.UTF_8)).ifPresent(tpl -> tpl.merge(context, sw));
		return sw.toString();
	}

	/**
	 * 渲染文本
	 * @param str
	 * @param dataModel 数据
	 * @return
	 */
	public static String renderStr(String str, Map<String, Object> dataModel) {
		// 设置velocity资源加载器
		Velocity.init();
		StringWriter stringWriter = new StringWriter();
		VelocityContext context = new VelocityContext(dataModel);
		// 函数库
		context.put("math", new MathTool());
		context.put("dateTool", new DateTool());
		context.put("dict", new DictTool());
		context.put("str", new NamingCaseTool());
		Velocity.evaluate(context, stringWriter, "renderStr", str);
		return stringWriter.toString();
	}

}
