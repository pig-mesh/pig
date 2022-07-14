package com.pig4cloud.pigx.codegen.service.impl.temp;

import com.pig4cloud.pigx.codegen.entity.GenConfig;
import com.pig4cloud.pigx.codegen.service.GenCodeService;
import com.pig4cloud.pigx.common.core.constant.CommonConstants;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

/**
 * uview代码实现
 *
 * @author lengleng
 * @date 2022/7/11
 */
@Service("uview")
public class UviewGenCodeServiceImpl implements GenCodeService {

	/**
	 * 注入支持的模板列表
	 * @param config 用户输入
	 * @return ListString
	 */
	@Override
	public List<String> getTemplates(GenConfig config) {
		List<String> templates = GenCodeService.super.getTemplates(config);
		templates.add("template/uview/index.vue.vm");
		templates.add("template/uview/form.vue.vm");
		templates.add("template/uview/api.js.vm");
		templates.add("template/uview/page.json.vm");
		return templates;
	}

	/**
	 * 获取文件名
	 * @param template
	 * @param className
	 * @param packageName
	 * @param moduleName
	 */
	@Override
	public String getFileName(String template, String className, String packageName, String moduleName) {
		if (template.contains("uview/index.vue.vm")) {
			return CommonConstants.UNI_END_PROJECT + File.separator + "pages" + File.separator + moduleName
					+ File.separator + className.toLowerCase() + File.separator + className.toLowerCase()
					+ "-index.vue";
		}

		if (template.contains("uview/form.vue.vm")) {
			return CommonConstants.UNI_END_PROJECT + File.separator + "pages" + File.separator + moduleName
					+ File.separator + className.toLowerCase() + File.separator + className.toLowerCase() + "-form.vue";
		}

		if (template.contains("uview/api.js.vm")) {
			return CommonConstants.UNI_END_PROJECT + File.separator + "common" + File.separator
					+ className.toLowerCase() + ".api.js";
		}

		if (template.contains("uview/page.json.vm")) {
			return className.toLowerCase() + ".page.json";
		}

		return GenCodeService.super.getFileName(template, className, packageName, moduleName);
	}

}
