package com.pig4cloud.pigx.common.data.datascope;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.github.yulichang.injector.MPJSqlInjector;

import java.util.List;

/**
 * 支持自定义数据权限方法注入
 *
 * @author lengleng
 * @date 2020-06-17
 */
public class DataScopeSqlInjector extends MPJSqlInjector {

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
		List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
		methodList.add(new SelectListByScope());
		methodList.add(new SelectPageByScope());
		methodList.add(new SelectCountByScope());
		return methodList;
	}

}
