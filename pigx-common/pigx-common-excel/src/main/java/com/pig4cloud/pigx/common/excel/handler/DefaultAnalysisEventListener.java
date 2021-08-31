package com.pig4cloud.pigx.common.excel.handler;

import com.alibaba.excel.context.AnalysisContext;
import com.pig4cloud.pigx.common.excel.kit.Validators;
import com.pig4cloud.pigx.common.excel.vo.ErrorMessage;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

	private final List<Object> list = new ArrayList<>();

	private final List<ErrorMessage> errorMessageList = new ArrayList<>();

	private Long lineNum = 1L;

	@Override
	public void invoke(Object o, AnalysisContext analysisContext) {
		lineNum++;

		Set<ConstraintViolation<Object>> violations = Validators.validate(o);
		if (!violations.isEmpty()) {
			Set<String> messageSet = violations.stream().map(ConstraintViolation::getMessage)
					.collect(Collectors.toSet());
			errorMessageList.add(new ErrorMessage(lineNum, messageSet));
		}
		else {
			list.add(o);
		}
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

}
