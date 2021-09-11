package com.pig4cloud.pig.common.security.datascope;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author hccake
 */
@Data
public class UserDataScope implements Serializable {

	/**
	 * 是否是全部数据权限
	 */
	private boolean allScope = false;

	/**
	 * 是否仅能看自己
	 */
	private boolean onlySelf = false;

	/**
	 * 数据权限范围，用户所能查看的用户id 集合
	 */
	private Set<Integer> scopeUserIds = new HashSet<>();

	/**
	 * 数据权限范围，用户所能查看的部门id 集合
	 */
	private Set<Integer> scopeDeptIds = new HashSet<>();

}
