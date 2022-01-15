package com.anjiplus.template.gaea.business.base;

import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.dto.BaseDTO;
import com.anji.plus.gaea.curd.entity.BaseEntity;
import com.anji.plus.gaea.curd.params.PageParam;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 项目级的Controller公共处理基类
 *
 * @author WongBin
 * @date 2021/3/26
 */
public abstract class BaseController<P extends PageParam, T extends BaseEntity, D extends BaseDTO>
		extends GaeaBaseController<P, T, D> {

	/**
	 * 获取当前语言类型
	 * @return
	 */
	public String getI18nLang() {
		return LocaleContextHolder.getLocale().getLanguage();
	}

}
