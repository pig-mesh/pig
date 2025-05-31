package com.pig4cloud.pig.common.security.service;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.entity.SysUser;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.core.util.RetOps;
import org.springframework.core.Ordered;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户详情服务接口，扩展了Spring Security的UserDetailsService和Ordered接口 提供用户详情加载、客户端支持校验及排序功能
 *
 * @author lengleng
 * @date 2025/05/31
 */
public interface PigUserDetailsService extends UserDetailsService, Ordered {

	/**
	 * 是否支持此客户端校验
	 * @param clientId 目标客户端
	 * @return true/false
	 */
	default boolean support(String clientId, String grantType) {
		return true;
	}

	/**
	 * 排序值 默认取最大的
	 * @return 排序值
	 */
	default int getOrder() {
		return 0;
	}

	/**
	 * 根据用户信息构建UserDetails对象
	 * @param result 包含用户信息的R对象
	 * @return 构建好的UserDetails对象
	 * @throws UsernameNotFoundException 当用户信息不存在时抛出异常
	 */
	default UserDetails getUserDetails(R<UserInfo> result) {
		UserInfo info = RetOps.of(result).getData().orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

		Set<String> dbAuthsSet = new HashSet<>();

		if (ArrayUtil.isNotEmpty(info.getRoles())) {
			// 获取角色
			Arrays.stream(info.getRoles()).forEach(role -> dbAuthsSet.add(SecurityConstants.ROLE + role));
			// 获取资源
			dbAuthsSet.addAll(Arrays.asList(info.getPermissions()));

		}

		Collection<GrantedAuthority> authorities = AuthorityUtils
			.createAuthorityList(dbAuthsSet.toArray(new String[0]));
		SysUser user = info.getSysUser();

		// 构造security用户
		return new PigUser(user.getUserId(), user.getDeptId(), user.getUsername(),
				SecurityConstants.BCRYPT + user.getPassword(), user.getPhone(), true, true, true,
				StrUtil.equals(user.getLockFlag(), CommonConstants.STATUS_NORMAL), authorities);
	}

	/**
	 * 通过用户实体查询用户详情
	 * @param pigUser 用户实体对象
	 * @return 用户详情信息
	 */
	default UserDetails loadUserByUser(PigUser pigUser) {
		return this.loadUserByUsername(pigUser.getUsername());
	}

}
