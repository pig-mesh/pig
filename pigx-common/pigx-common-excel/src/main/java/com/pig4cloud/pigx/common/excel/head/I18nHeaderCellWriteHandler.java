package com.pig4cloud.pigx.common.excel.head;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 对表头进行国际化处理
 *
 * @author hccake
 */
@RequiredArgsConstructor
public class I18nHeaderCellWriteHandler implements CellWriteHandler {

	/**
	 * 国际化消息源
	 */
	private final MessageSource messageSource;

	/**
	 * 国际化翻译
	 */
	private final PropertyPlaceholderHelper.PlaceholderResolver placeholderResolver;

	public I18nHeaderCellWriteHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
		this.placeholderResolver = placeholderName -> this.messageSource.getMessage(placeholderName, null,
				LocaleContextHolder.getLocale());
	}

	/**
	 * 占位符处理
	 */
	private final PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("{", "}");

	@Override
	public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row,
			Head head, Integer columnIndex, Integer relativeRowIndex, Boolean isHead) {
		if (isHead != null && isHead) {
			List<String> originHeadNameList = head.getHeadNameList();
			if (CollectionUtils.isNotEmpty(originHeadNameList)) {
				// 国际化处理
				List<String> i18nHeadNames = originHeadNameList.stream()
					.map(headName -> propertyPlaceholderHelper.replacePlaceholders(headName, placeholderResolver))
					.collect(Collectors.toList());
				head.setHeadNameList(i18nHeadNames);
			}
		}
	}

}
