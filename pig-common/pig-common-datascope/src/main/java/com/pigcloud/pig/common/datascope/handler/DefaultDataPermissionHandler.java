package com.pigcloud.pig.common.datascope.handler;

import com.pigcloud.pig.common.datascope.DataScope;
import com.pigcloud.pig.common.datascope.annotation.DataPermission;
import com.pigcloud.pig.common.datascope.holder.DataPermissionAnnotationHolder;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 默认的数据权限控制处理器
 *
 * @author Hccake 2021/1/27
 * @version 1.0
 */
@RequiredArgsConstructor
public class DefaultDataPermissionHandler implements DataPermissionHandler {

	private final List<DataScope> dataScopes;

	/**
	 * 系统配置的所有的数据范围
	 * @return 数据范围集合
	 */
	@Override
	public List<DataScope> dataScopes() {
		return dataScopes;
	}

	/**
	 * 系统配置的所有的数据范围
	 * @param mappedStatementId Mapper方法ID
	 * @return 数据范围集合
	 */
	@Override
	public List<DataScope> filterDataScopes(String mappedStatementId) {
		if (this.dataScopes == null || this.dataScopes.isEmpty()) {
			return new ArrayList<>();
		}
		// 获取当前方法对应的权限注解，根据注解进行数据范围控制的过滤
		DataPermission dataPermission = DataPermissionAnnotationHolder.peek();
		if (dataPermission == null) {
			return dataScopes;
		}

		if (dataPermission.ignore()) {
			return new ArrayList<>();
		}

		// 当指定了只包含的资源时，只对该资源的DataScope
		if (dataPermission.includeResources().length > 0) {
			Set<String> a = new HashSet<>(Arrays.asList(dataPermission.includeResources()));
			return dataScopes.stream().filter(x -> a.contains(x.getResource())).collect(Collectors.toList());
		}

		// 当未指定只包含的资源，且指定了排除的资源时，则排除此部分资源的 DataScope
		if (dataPermission.excludeResources().length > 0) {
			Set<String> a = new HashSet<>(Arrays.asList(dataPermission.excludeResources()));
			return dataScopes.stream().filter(x -> !a.contains(x.getResource())).collect(Collectors.toList());
		}

		return dataScopes;
	}

	/**
	 * 是否忽略权限控制，默认不忽略
	 * @param mappedStatementId Mapper方法ID
	 * @return always false
	 */
	@Override
	public boolean ignorePermissionControl(String mappedStatementId) {
		return false;
	}

}
