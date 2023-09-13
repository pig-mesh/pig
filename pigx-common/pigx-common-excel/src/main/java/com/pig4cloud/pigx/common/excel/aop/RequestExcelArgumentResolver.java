package com.pig4cloud.pigx.common.excel.aop;

import com.alibaba.excel.EasyExcel;
import com.pig4cloud.pigx.common.excel.annotation.RequestExcel;
import com.pig4cloud.pigx.common.excel.converters.*;
import com.pig4cloud.pigx.common.excel.handler.ListAnalysisEventListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import jakarta.servlet.http.HttpServletRequest;
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
	@SneakyThrows(Exception.class)
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) {
		Class<?> parameterType = parameter.getParameterType();
		if (!parameterType.isAssignableFrom(List.class)) {
			throw new IllegalArgumentException(
					"Excel upload request resolver error, @RequestExcel parameter is not List " + parameterType);
		}

		// 处理自定义 readListener
		RequestExcel requestExcel = parameter.getParameterAnnotation(RequestExcel.class);
		assert requestExcel != null;
		Class<? extends ListAnalysisEventListener<?>> readListenerClass = requestExcel.readListener();
		ListAnalysisEventListener<?> readListener = BeanUtils.instantiateClass(readListenerClass);

		// 获取请求文件流
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		assert request != null;
		InputStream inputStream;
		if (request instanceof MultipartRequest) {
			MultipartFile file = ((MultipartRequest) request).getFile(requestExcel.fileName());
			assert file != null;
			inputStream = file.getInputStream();
		}
		else {
			inputStream = request.getInputStream();
		}

		// 获取目标类型
		Class<?> excelModelClass = ResolvableType.forMethodParameter(parameter).getGeneric(0).resolve();

		// 这里需要指定读用哪个 class 去读，然后读取第一个 sheet 文件流会自动关闭
		EasyExcel.read(inputStream, excelModelClass, readListener)
			.registerConverter(LocalDateStringConverter.INSTANCE)
			.registerConverter(LocalTimeStringConverter.INSTANCE)
			.registerConverter(LocalDateTimeStringConverter.INSTANCE)
			.registerConverter(LongStringConverter.INSTANCE)
			.registerConverter(StringArrayConverter.INSTANCE)
			.ignoreEmptyRow(requestExcel.ignoreEmptyRow())
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
