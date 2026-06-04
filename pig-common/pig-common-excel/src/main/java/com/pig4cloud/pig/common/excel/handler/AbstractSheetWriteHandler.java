package com.pig4cloud.pig.common.excel.handler;

import cn.hutool.core.util.StrUtil;
import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.ExcelWriter;
import org.apache.fesod.sheet.converters.Converter;
import org.apache.fesod.sheet.write.builder.ExcelWriterBuilder;
import org.apache.fesod.sheet.write.builder.ExcelWriterSheetBuilder;
import org.apache.fesod.sheet.write.handler.WriteHandler;
import org.apache.fesod.sheet.write.metadata.WriteSheet;
import com.pig4cloud.pig.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pig.common.excel.annotation.Sheet;
import com.pig4cloud.pig.common.excel.aop.DynamicNameAspect;
import com.pig4cloud.pig.common.excel.config.ExcelConfigProperties;
import com.pig4cloud.pig.common.excel.converters.BuiltinConverters;
import com.pig4cloud.pig.common.excel.enhance.WriterBuilderEnhancer;
import com.pig4cloud.pig.common.excel.head.HeadGenerator;
import com.pig4cloud.pig.common.excel.head.HeadMeta;
import com.pig4cloud.pig.common.excel.head.I18nHeaderCellWriteHandler;
import com.pig4cloud.pig.common.excel.kit.ExcelException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author lengleng
 * @author L.cm
 * @date 2020/3/31
 */
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler, ApplicationContextAware {

	private final ExcelConfigProperties configProperties;

	private final ObjectProvider<List<Converter<?>>> converterProvider;

	private final WriterBuilderEnhancer excelWriterBuilderEnhance;

	private final ObjectProvider<I18nHeaderCellWriteHandler> i18nHeaderProvider;

	private ApplicationContext applicationContext;

	protected AbstractSheetWriteHandler(ExcelConfigProperties configProperties,
			ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer excelWriterBuilderEnhance,
			ObjectProvider<I18nHeaderCellWriteHandler> i18nHeaderProvider) {
		this.configProperties = configProperties;
		this.converterProvider = converterProvider;
		this.excelWriterBuilderEnhance = excelWriterBuilderEnhance;
		this.i18nHeaderProvider = i18nHeaderProvider;
	}

	@Override
	public void check(ResponseExcel responseExcel) {
		if (responseExcel.sheets().length == 0) {
			throw new ExcelException("@ResponseExcel sheet 配置不合法");
		}
	}

	@Override
	public void export(Object o, HttpServletResponse response, ResponseExcel responseExcel) {
		check(responseExcel);
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		String name = requestAttributes == null ? null : (String) requestAttributes
			.getAttribute(DynamicNameAspect.EXCEL_NAME_KEY, RequestAttributes.SCOPE_REQUEST);
		if (!StringUtils.hasText(name)) {
			name = UUID.randomUUID().toString();
		}
		String fileName = URLEncoder.encode(name, StandardCharsets.UTF_8) + responseExcel.suffix().getValue();
		String contentType = MediaTypeFactory.getMediaType(fileName)
			.map(MediaType::toString)
			.orElse("application/vnd.ms-excel");
		response.setContentType(contentType);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
		write(o, response, responseExcel);
	}

	/**
	 * 通用的获取ExcelWriter方法
	 * @param response HttpServletResponse
	 * @param responseExcel ResponseExcel注解
	 * @return ExcelWriter
	 */
	@SneakyThrows(IOException.class)
	public ExcelWriter getExcelWriter(HttpServletResponse response, ResponseExcel responseExcel) {
		ExcelWriterBuilder writerBuilder = FesodSheet.write(response.getOutputStream())
			.autoCloseStream(true)
			.excelType(responseExcel.suffix())
			.inMemory(responseExcel.inMemory());

		BuiltinConverters.registerTo(writerBuilder);

		if (StringUtils.hasText(responseExcel.password())) {
			writerBuilder.password(responseExcel.password());
		}

		if (responseExcel.include().length != 0) {
			writerBuilder.includeColumnFieldNames(Arrays.asList(responseExcel.include()));
		}

		if (responseExcel.exclude().length != 0) {
			writerBuilder.excludeColumnFieldNames(Arrays.asList(responseExcel.exclude()));
		}

		for (Class<? extends WriteHandler> clazz : responseExcel.writeHandler()) {
			writerBuilder.registerWriteHandler(BeanUtils.instantiateClass(clazz));
		}

		// 开启国际化头信息处理
		if (responseExcel.i18nHeader()) {
			i18nHeaderProvider.ifAvailable(writerBuilder::registerWriteHandler);
		}

		// 自定义注入的转换器
		registerCustomConverter(writerBuilder);

		for (Class<? extends Converter> clazz : responseExcel.converter()) {
			writerBuilder.registerConverter(BeanUtils.instantiateClass(clazz));
		}

		String templatePath = configProperties.getTemplatePath();
		if (StringUtils.hasText(responseExcel.template())) {
			ClassPathResource classPathResource = new ClassPathResource(
					templatePath + StrUtil.SLASH + responseExcel.template());
			InputStream inputStream = classPathResource.getInputStream();
			writerBuilder.withTemplate(inputStream);
		}

		writerBuilder = excelWriterBuilderEnhance.enhanceExcel(writerBuilder, response, responseExcel, templatePath);

		return writerBuilder.build();
	}

	/**
	 * 自定义注入转换器 如果有需要，子类自己重写
	 * @param builder ExcelWriterBuilder
	 */
	public void registerCustomConverter(ExcelWriterBuilder builder) {
		converterProvider.ifAvailable(converters -> converters.forEach(builder::registerConverter));
	}

	/**
	 * 获取 WriteSheet 对象
	 * @param sheet sheet annotation info
	 * @param dataClass 数据类型
	 * @param template 模板
	 * @param bookHeadEnhancerClass 自定义头处理器
	 * @return WriteSheet
	 */
	public WriteSheet sheet(Sheet sheet, Class<?> dataClass, String template,
			Class<? extends HeadGenerator> bookHeadEnhancerClass) {

		// Sheet 编号和名称
		Integer sheetNo = sheet.sheetNo() >= 0 ? sheet.sheetNo() : null;
		String sheetName = sheet.sheetName();

		// 是否模板写入
		ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.hasText(template) ? FesodSheet.writerSheet(sheetNo)
				: FesodSheet.writerSheet(sheetNo, sheetName);

		// 头信息增强 1. 优先使用 sheet 指定的头信息增强 2. 其次使用 @ResponseExcel 中定义的全局头信息增强
		Class<? extends HeadGenerator> headGenerateClass = null;
		if (isNotInterface(sheet.headGenerateClass())) {
			headGenerateClass = sheet.headGenerateClass();
		}
		else if (isNotInterface(bookHeadEnhancerClass)) {
			headGenerateClass = bookHeadEnhancerClass;
		}
		// 定义头信息增强则使用其生成头信息，否则使用 dataClass 来自动获取
		if (headGenerateClass != null) {
			fillCustomHeadInfo(dataClass, bookHeadEnhancerClass, writerSheetBuilder);
		}
		else if (dataClass != null) {
			writerSheetBuilder.head(dataClass);
			if (sheet.excludes().length > 0) {
				writerSheetBuilder.excludeColumnFieldNames(Arrays.asList(sheet.excludes()));
			}
			if (sheet.includes().length > 0) {
				writerSheetBuilder.includeColumnFieldNames(Arrays.asList(sheet.includes()));
			}
		}

		// sheetBuilder 增强
		writerSheetBuilder = excelWriterBuilderEnhance.enhanceSheet(writerSheetBuilder, sheetNo, sheetName, dataClass,
				template, headGenerateClass);

		return writerSheetBuilder.build();
	}

	private void fillCustomHeadInfo(Class<?> dataClass, Class<? extends HeadGenerator> headEnhancerClass,
			ExcelWriterSheetBuilder writerSheetBuilder) {
		HeadGenerator headGenerator = this.applicationContext.getBean(headEnhancerClass);
		Assert.notNull(headGenerator, "The header generated bean does not exist.");
		HeadMeta head = headGenerator.head(dataClass);
		writerSheetBuilder.head(head.getHead());
		writerSheetBuilder.excludeColumnFieldNames(head.getIgnoreHeadFields());
	}

	/**
	 * 是否为Null Head Generator
	 * @param headGeneratorClass 头生成器类型
	 * @return true 已指定 false 未指定(默认值)
	 */
	private boolean isNotInterface(Class<? extends HeadGenerator> headGeneratorClass) {
		return !Modifier.isInterface(headGeneratorClass.getModifiers());
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
