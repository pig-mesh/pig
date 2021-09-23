package com.pigcloud.pig.common.datascope.holder;

import com.pigcloud.pig.common.datascope.DataScope;

import java.util.List;

/**
 * DataScope 持有者。 方便解析 SQL 时的参数透传
 *
 * @author hccake
 */
public final class DataScopeHolder {

	private DataScopeHolder() {
	}

	private static final ThreadLocal<List<DataScope>> DATA_SCOPES = new ThreadLocal<>();

	/**
	 * get dataScope
	 * @return dataScopes
	 */
	public static List<DataScope> get() {
		return DATA_SCOPES.get();
	}

	/**
	 * 添加 dataScope
	 */
	public static void set(List<DataScope> dataScopes) {
		DATA_SCOPES.set(dataScopes);
	}

	/**
	 * 删除 dataScope
	 */
	public static void remove() {
		DATA_SCOPES.remove();
	}

}
