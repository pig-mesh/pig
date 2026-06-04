package com.pig4cloud.pig.common.excel.aop;

import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.read.builder.ExcelReaderBuilder;
import com.pig4cloud.pig.common.excel.annotation.RequestExcel;
import com.pig4cloud.pig.common.excel.converters.BuiltinConverters;
import com.pig4cloud.pig.common.excel.handler.ListAnalysisEventListener;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.InputStream;
import java.util.List;

/**
 * 上传excel 解析注解
 *
 * @author lengleng
 * @author L.cm
 * @date 2021/4/16
 */
@Slf4j
public class RequestExcelArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestExcel.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		Class<?> parameterType = parameter.getParameterType();
		if (!parameterType.isAssignableFrom(List.class)) {
			throw new IllegalArgumentException(
					"Excel upload request resolver error, @RequestExcel parameter is not List " + parameterType);
		}

		// 处理自定义 readListener
		RequestExcel requestExcel = parameter.getParameterAnnotation(RequestExcel.class);
		Assert.notNull(requestExcel, "@RequestExcel annotation must not be null");
		Class<? extends ListAnalysisEventListener<?>> readListenerClass = requestExcel.readListener();
		ListAnalysisEventListener<?> readListener = BeanUtils.instantiateClass(readListenerClass);

		// 获取请求文件流
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		Assert.notNull(request, "HttpServletRequest must not be null");
		InputStream inputStream;
		if (request instanceof MultipartRequest multipartRequest) {
			MultipartFile file = multipartRequest.getFile(requestExcel.fileName());
			Assert.notNull(file, "Multipart file [" + requestExcel.fileName() + "] must not be null");
			inputStream = file.getInputStream();
		}
		else {
			inputStream = request.getInputStream();
		}

		// 获取目标类型
		Class<?> excelModelClass = ResolvableType.forMethodParameter(parameter).getGeneric(0).resolve();

		// 这里需要指定读用哪个 class 去读，然后读取第一个 sheet 文件流会自动关闭
		ExcelReaderBuilder readerBuilder = FesodSheet.read(inputStream, excelModelClass, readListener);
		BuiltinConverters.registerTo(readerBuilder);
		readerBuilder.ignoreEmptyRow(requestExcel.ignoreEmptyRow())
			.sheet()
			.headRowNumber(requestExcel.headRowNumber())
			.doRead();

		// 校验失败的数据处理 交给 BindResult
		WebDataBinder dataBinder = webDataBinderFactory.createBinder(webRequest, readListener.getErrors(), "excel");
		ModelMap model = modelAndViewContainer.getModel();
		model.put(BindingResult.MODEL_KEY_PREFIX + "excel", dataBinder.getBindingResult());

		return readListener.getList();
	}

}
