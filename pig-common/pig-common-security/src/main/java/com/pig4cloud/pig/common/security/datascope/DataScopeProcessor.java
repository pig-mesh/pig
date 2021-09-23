package com.pig4cloud.pig.common.security.datascope;

import com.pig4cloud.pig.admin.api.entity.SysRole;
import com.pig4cloud.pig.admin.api.entity.SysUser;

import java.util.List;

/**
 * @author hccake
 */
public interface DataScopeProcessor {

	/**
	 * 根据用户和角色信息，合并用户最终的数据权限
	 * @param user 用户
	 * @param roles 角色列表
	 * @return UserDataScope
	 */
	UserDataScope mergeScopeType(SysUser user, List<SysRole> roles);

}
