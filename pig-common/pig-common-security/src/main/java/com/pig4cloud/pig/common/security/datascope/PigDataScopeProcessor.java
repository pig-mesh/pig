package com.pig4cloud.pig.common.security.datascope;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.api.entity.SysRole;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.constant.enums.DataScopeTypeEnum;
import com.pig4cloud.pig.common.core.util.R;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hccake
 */
@RequiredArgsConstructor
public class PigDataScopeProcessor implements DataScopeProcessor {

	private final RemoteDeptService remoteDeptService;

	private final RemoteUserService remoteUserService;

	/**
	 * 合并角色的数据权限类型，排除相同的权限后，大的权限覆盖小的
	 * @param user 用户
	 * @param roles 角色列表
	 * @return List<Integer> 合并后的权限
	 */
	@Override
	public UserDataScope mergeScopeType(SysUser user, List<SysRole> roles) {
		UserDataScope userDataScope = new UserDataScope();
		Set<Integer> scopeUserIds = userDataScope.getScopeUserIds();
		Set<Integer> scopeDeptIds = userDataScope.getScopeDeptIds();

		// 任何用户都应该可以看到自己的数据
		Integer userId = user.getUserId();
		scopeUserIds.add(userId);

		if (CollectionUtil.isEmpty(roles)) {
			return userDataScope;
		}

		// 根据角色的权限返回进行分组
		Map<Integer, List<SysRole>> map = roles.stream().collect(Collectors.groupingBy(SysRole::getScopeType));

		// 如果有全部权限，直接返回
		if (map.containsKey(DataScopeTypeEnum.ALL.getType())) {
			userDataScope.setAllScope(true);
			return userDataScope;
		}

		// 如果有本级及子级，删除其包含的几类数据权限
		boolean hasLevelChildLevel = map.containsKey(DataScopeTypeEnum.LEVEL_CHILD_LEVEL.getType());
		if (hasLevelChildLevel) {
			map.remove(DataScopeTypeEnum.SELF.getType());
			map.remove(DataScopeTypeEnum.SELF_CHILD_LEVEL.getType());
			map.remove(DataScopeTypeEnum.LEVEL.getType());
		}

		// 是否有本人及子级权限
		boolean hasSelfChildLevel = map.containsKey(DataScopeTypeEnum.SELF_CHILD_LEVEL.getType());
		// 是否有本级权限
		boolean hasLevel = map.containsKey(DataScopeTypeEnum.LEVEL.getType());
		if (hasSelfChildLevel || hasLevel) {
			// 如果有本人及子级或者本级，都删除本人的数据权限
			map.remove(DataScopeTypeEnum.SELF.getType());
			// 如果同时拥有，则等于本级及子级权限
			if (hasSelfChildLevel && hasLevel) {
				map.remove(DataScopeTypeEnum.SELF_CHILD_LEVEL.getType());
				map.remove(DataScopeTypeEnum.LEVEL.getType());
				map.put(DataScopeTypeEnum.LEVEL_CHILD_LEVEL.getType(), new ArrayList<>());
			}
		}

		// 这时如果仅仅只能看个人的，直接返回
		if (map.size() == 1 && map.containsKey(DataScopeTypeEnum.SELF.getType())) {
			userDataScope.setOnlySelf(true);
			return userDataScope;
		}

		// 如果有 本级及子级 或者 本级，都把自己的 deptId 加进去
		Integer deptId = user.getDeptId();
		if (hasLevelChildLevel || hasLevel) {
			scopeDeptIds.add(deptId);
		}
		// 如果有 本级及子级 或者 本人及子级，都把下级组织的 deptId 加进去
		if (hasLevelChildLevel || hasSelfChildLevel) {
			List<Integer> childDeptIdList = remoteDeptService.listChildDeptId(deptId, SecurityConstants.FROM_IN)
					.getData();
			if (CollectionUtil.isNotEmpty(childDeptIdList)) {
				scopeDeptIds.addAll(childDeptIdList);
			}
		}
		// 自定义部门
		List<SysRole> sysRoles = map.get(DataScopeTypeEnum.CUSTOM.getType());
		if (CollectionUtil.isNotEmpty(sysRoles)) {
			Set<Integer> customDeptIds = sysRoles.stream().map(SysRole::getScopeResources).filter(Objects::nonNull)
					.flatMap(x -> Arrays.stream(x.split(StrUtil.COMMA))).map(Integer::parseInt)
					.collect(Collectors.toSet());
			scopeDeptIds.addAll(customDeptIds);
		}

		// 把部门对应的用户id都放入集合中
		if (CollectionUtil.isNotEmpty(scopeDeptIds)) {
			R<List<Integer>> r = remoteUserService.listUserIdByDeptIds(scopeDeptIds, SecurityConstants.FROM_IN);
			List<Integer> userIds = r.getData();
			if (CollectionUtil.isNotEmpty(userIds)) {
				scopeUserIds.addAll(userIds);
			}
		}

		return userDataScope;
	}

}
