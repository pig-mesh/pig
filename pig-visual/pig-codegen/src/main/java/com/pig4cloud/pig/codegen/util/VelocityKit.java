package com.pig4cloud.pig.codegen.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.MathTool;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;
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
	 * 模板高危关键字黑名单。
	 * <p>
	 * 聚焦反射 / 命令执行原语，正常代码生成模板不会出现这些 token，命中即拒绝渲染（早失败）。 真正的安全边界是引擎层的
	 * {@link org.apache.velocity.util.introspection.SecureUberspector}， 本黑名单仅作纵深防御。
	 */
	private static final String[] DANGER_TOKENS = { "getClass", "forName", "getRuntime", "java.lang.Runtime",
			"java.lang.System", "ProcessBuilder", "ClassLoader", "java.lang.reflect", "getDeclaredMethod",
			"scriptEngine", "javax.script.ScriptEngine", "#evaluate" };

	/**
	 * Velocity 安全沙箱限制访问的包。
	 */
	private static final String[] RESTRICTED_PACKAGES = { "java.lang.reflect", "org.openjdk.nashorn", "bsh" };

	/**
	 * Velocity 安全沙箱限制访问的类。
	 */
	private static final String[] RESTRICTED_CLASSES = { "java.lang.Class", "java.lang.ClassLoader",
			"java.lang.Compiler", "java.lang.InheritableThreadLocal", "java.lang.Package", "java.lang.Process",
			"java.lang.ProcessBuilder", "java.lang.Reflect", "java.lang.Runtime", "java.lang.RuntimePermission",
			"java.lang.SecurityManager", "java.lang.System", "java.lang.Thread", "java.lang.ThreadGroup",
			"java.lang.ThreadLocal", "java.net.Socket", "javax.management.MBeanServer", "javax.script.ScriptEngine" };

	/**
	 * 安全的 Velocity 引擎，全局初始化一次。
	 * <p>
	 * 通过 {@link org.apache.velocity.util.introspection.SecureUberspector} 禁止 {@code getClass()}
	 * 及对 {@code java.lang.Runtime/System/Thread/ProcessBuilder/Process/ClassLoader/Class} 和
	 * {@code java.lang.reflect.*} 的方法调用，从根上阻断 Velocity SSTI → 反射 → RCE。
	 */
	private static final VelocityEngine ENGINE = buildSecureEngine();

	private static VelocityEngine buildSecureEngine() {
		Properties prop = new Properties();
		// classpath 资源加载（保留 render() 按名加载模板的能力）
		prop.put(RuntimeConstants.RESOURCE_LOADERS, "class");
		prop.put("resource.loader.class.class",
				"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		// 关键：安全 Uberspector，禁用反射 / getClass 访问危险类
		prop.put(RuntimeConstants.UBERSPECT_CLASSNAME,
				"org.apache.velocity.util.introspection.SecureUberspector");
		// 显式保留 Velocity 默认限制，并扩展封禁脚本引擎等二次利用面
		prop.put("introspector.restrict.packages", String.join(",", RESTRICTED_PACKAGES));
		prop.put("introspector.restrict.classes", String.join(",", RESTRICTED_CLASSES));
		VelocityEngine engine = new VelocityEngine();
		engine.init(prop);
		return engine;
	}

	/**
	 * 渲染前的安全校验：拦截包含反射 / 命令执行关键字的模板。
	 * @param template 模板内容
	 */
	private static void checkTemplateSecurity(String template) {
		if (StrUtil.isBlank(template)) {
			return;
		}
		Assert.isFalse(StrUtil.containsAnyIgnoreCase(template, DANGER_TOKENS), "模板包含非法内容，已被安全策略拦截");
	}

	/**
	 * 构建注入工具库的渲染上下文
	 * @param map 数据模型
	 * @return VelocityContext
	 */
	private static VelocityContext buildContext(Map<String, Object> map) {
		VelocityContext context = new VelocityContext(map);
		// 函数库
		context.put("math", new MathTool());
		context.put("dateTool", new DateTool());
		context.put("dict", new DictTool());
		context.put("str", new NamingCaseTool());
		return context;
	}

	/**
	 * Velocity 模板渲染方法
	 * @param template 模板
	 * @param map 数据模型
	 * @return 渲染结果
	 */
	public static String render(String template, Map<String, Object> map) {
		checkTemplateSecurity(template);
		VelocityContext context = buildContext(map);
		StringWriter sw = new StringWriter();
		getTemplate(template).merge(context, sw);
		return sw.toString();
	}

	private static Template getTemplate(String template) {
		return ENGINE.getTemplate(template, CharsetUtil.UTF_8);
	}

	/**
	 * 渲染文本
	 * @param str 模板字符串
	 * @param dataModel 数据
	 * @return 渲染结果
	 */
	public static String renderStr(String str, Map<String, Object> dataModel) {
		checkTemplateSecurity(str);
		StringWriter stringWriter = new StringWriter();
		VelocityContext context = buildContext(dataModel);
		ENGINE.evaluate(context, stringWriter, "renderStr", str);
		return stringWriter.toString();
	}

}
