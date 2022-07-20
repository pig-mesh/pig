package com.pig4cloud.pig.codegen.service.impl.temp;

import cn.hutool.core.io.IoUtil;
import com.pig4cloud.pig.codegen.entity.GenConfig;
import com.pig4cloud.pig.codegen.entity.GenFormConf;
import com.pig4cloud.pig.codegen.entity.TableEntity;
import com.pig4cloud.pig.codegen.service.GenCodeService;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Fxz
 * @date 2022/7/21 02:45
 */
@Service("avue")
public class AvueGenCodeServiceImpl implements GenCodeService {

	public final String CRUD_PREFIX = "export const tableOption =";

	/**
	 * 注入支持的模板列表
	 * @param config 用户输入
	 * @return ListString
	 */
	@Override
	public List<String> getTemplates(GenConfig config) {
		List<String> templates = GenCodeService.super.getTemplates(config);
		templates.add("template/avue/index.vue.vm");
		templates.add("template/avue/crud.js.vm");
		templates.add("template/avue/api.js.vm");
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
		if (template.contains("avue/index.vue.vm")) {
			return CommonConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "views"
					+ File.separator + moduleName + File.separator + className.toLowerCase() + File.separator
					+ "index.vue";
		}

		if (template.contains("avue/api.js.vm")) {
			return CommonConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "api" + File.separator
					+ className.toLowerCase() + ".js";
		}

		if (template.contains("avue/crud.js.vm")) {
			return CommonConstants.FRONT_END_PROJECT + File.separator + "src" + File.separator + "const"
					+ File.separator + "crud" + File.separator + className.toLowerCase() + ".js";
		}

		return GenCodeService.super.getFileName(template, className, packageName, moduleName);
	}

	/**
	 * 渲染数据
	 * @param genConfig 用户输入相关
	 * @param zip 输出zip流
	 * @param tableEntity 表格实体
	 * @param map 参数集合
	 * @param formConf 表单设计
	 * @return
	 */
	@SneakyThrows
	public Map<String, String> renderData(GenConfig genConfig, ZipOutputStream zip, TableEntity tableEntity,
			Map<String, Object> map, GenFormConf formConf) {

		Map<String, String> resultMap = new HashMap<>();

		if (Objects.nonNull(formConf)) {
			// 存在 curd 存在设计好的JSON 则使用Json 覆盖
			String crudTempName = "template/avue/crud.js.vm";
			String fileName = getFileName(crudTempName, tableEntity.getCaseClassName(), map.get("package").toString(),
					map.get("moduleName").toString());
			String contents = CRUD_PREFIX + formConf.getFormInfo();

			if (zip != null) {
				zip.putNextEntry(new ZipEntry(Objects.requireNonNull(fileName)));
				IoUtil.write(zip, StandardCharsets.UTF_8, false, contents);
				zip.closeEntry();
			}
			resultMap.putAll(GenCodeService.super.renderData(genConfig, zip, tableEntity, map, formConf));
			resultMap.put(crudTempName, contents);
			return resultMap;
		}
		return GenCodeService.super.renderData(genConfig, zip, tableEntity, map, formConf);
	}

}
