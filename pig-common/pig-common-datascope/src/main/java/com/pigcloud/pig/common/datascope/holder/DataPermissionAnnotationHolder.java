package com.pigcloud.pig.common.datascope.holder;

import com.pigcloud.pig.common.datascope.annotation.DataPermission;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 数据权限注解的持有者，使用栈存储调用链中各方法对应数据权限注解
 *
 * @author hccake
 */
public final class DataPermissionAnnotationHolder {

	private DataPermissionAnnotationHolder() {
	}

	/**
	 * 使用栈存储 DataPermission，便于在方法嵌套调用时使用不同的数据权限控制。
	 */
	private static final ThreadLocal<Deque<DataPermission>> DATA_PERMISSIONS = ThreadLocal.withInitial(ArrayDeque::new);

	/**
	 * 获取当前的 DataPermission 注解
	 * @return DataPermission
	 */
	public static DataPermission peek() {
		return DATA_PERMISSIONS.get().peek();
	}

	/**
	 * 入栈一个 DataPermission 注解
	 * @return DataPermission
	 */
	public static DataPermission push(DataPermission dataPermission) {
		DATA_PERMISSIONS.get().push(dataPermission);
		return dataPermission;
	}

	/**
	 * 弹出最顶部 DataPermission
	 */
	public static void poll() {
		Deque<DataPermission> deque = DATA_PERMISSIONS.get();
		// 当没有元素时，清空 ThreadLocal
		if (deque.poll() == null) {
			DATA_PERMISSIONS.remove();
		}
	}

	/**
	 * 清除 TreadLocal
	 */
	public static void clear() {
		DATA_PERMISSIONS.remove();
	}

}
