package com.pig4cloud.pig.common.excel.handler;

import cn.hutool.core.util.ReflectUtil;
import org.apache.fesod.sheet.context.AnalysisContext;
import com.pig4cloud.pig.common.excel.annotation.ExcelLine;
import com.pig4cloud.pig.common.excel.kit.Validators;
import com.pig4cloud.pig.common.excel.vo.ErrorMessage;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 默认的 AnalysisEventListener
 *
 * @author lengleng
 * @author L.cm
 * @date 2021/4/16
 */
@Slf4j
public class DefaultAnalysisEventListener extends ListAnalysisEventListener<Object> {

	/**
	 * 缓存带 @ExcelLine 标记的 Long 类型行号字段，避免逐行读取时重复反射扫描。
	 */
	private static final ConcurrentHashMap<Class<?>, Field[]> LINE_FIELDS_CACHE = new ConcurrentHashMap<>();

	private final List<Object> list = new ArrayList<>();

	private final List<ErrorMessage> errorMessageList = new ArrayList<>();

	private Long lineNum = 1L;

	@Override
	public void invoke(Object o, AnalysisContext analysisContext) {
		lineNum++;

		Set<ConstraintViolation<Object>> violations = Validators.validate(o);
		if (!violations.isEmpty()) {
			Set<String> messageSet = violations.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toSet());
			errorMessageList.add(new ErrorMessage(lineNum, messageSet));
			return;
		}

		for (Field field : resolveLineFields(o.getClass())) {
			ReflectUtil.setFieldValue(o, field, lineNum);
		}
		list.add(o);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		log.debug("Excel read analysed");
	}

	@Override
	public List<Object> getList() {
		return list;
	}

	@Override
	public List<ErrorMessage> getErrors() {
		return errorMessageList;
	}

	private static Field[] resolveLineFields(Class<?> clazz) {
		return LINE_FIELDS_CACHE.computeIfAbsent(clazz,
				c -> Arrays.stream(c.getDeclaredFields())
					.filter(f -> f.isAnnotationPresent(ExcelLine.class) && f.getType() == Long.class)
					.toArray(Field[]::new));
	}

}
