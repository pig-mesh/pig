package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.teaopenapi.models.Config;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRoleListRequest;
import com.dingtalk.api.request.OapiUserListidRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiRoleListResponse;
import com.dingtalk.api.response.OapiUserListidResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.pig4cloud.pigx.admin.api.entity.*;
import com.pig4cloud.pigx.admin.mapper.SysSocialDetailsMapper;
import com.pig4cloud.pigx.admin.service.*;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 互联平台实现
 * <p>
 * 钉钉: https://open-dev.dingtalk.com/apiExplorer
 *
 * @author lengleng
 * @date 2022/4/22
 */
@SuppressWarnings("unchecked")
@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectServiceImpl implements ConnectService {

	private final SysSocialDetailsMapper sysSocialDetailsMapper;

	private final SysRoleService sysRoleService;

	private final SysDeptService sysDeptService;

	private final SysUserService sysUserService;

	private final SysUserRoleService sysUserRoleService;

	private String getDingAccessToken() {
		SysSocialDetails dingTalk = sysSocialDetailsMapper
				.selectOne(Wrappers.<SysSocialDetails>lambdaQuery().eq(SysSocialDetails::getType, "ding"));
		GetAccessTokenRequest getAccessTokenRequest = new GetAccessTokenRequest().setAppKey(dingTalk.getAppId())
				.setAppSecret(dingTalk.getAppSecret());
		GetAccessTokenResponse tokenResponse = null;
		try {
			tokenResponse = createDingClient().getAccessToken(getAccessTokenRequest);
		}
		catch (Exception e) {
			log.error("调用钉钉异常", e);
		}
		return tokenResponse.getBody().getAccessToken();
	}

	/**
	 * 创建钉钉客户端
	 * @return
	 */
	@SneakyThrows
	private com.aliyun.dingtalkoauth2_1_0.Client createDingClient() {
		Config config = new Config();
		config.protocol = "https";
		config.regionId = "central";
		return new com.aliyun.dingtalkoauth2_1_0.Client(config);
	}

	/**
	 * 同步钉钉角色
	 *
	 * @see <a href=
	 * "https://open.dingtalk.com/document/orgapp-server/obtains-a-list-of-enterprise-roles"/>
	 */
	@SneakyThrows
	@Override
	public Boolean syncDingRole() {
		// 分页偏移量
		long offset = 0L;
		// 是否还有非同步的数据
		boolean hasMore;
		// 数据库中现有的角色
		List<SysRole> roleList = sysRoleService.list(Wrappers.emptyWrapper());
		// 钉钉同步过来的角色
		List<SysRole> insertRole = new LinkedList<>();

		do {
			DingTalkClient client = new DefaultDingTalkClient(SecurityConstants.DING_OLD_ROLE_URL);
			OapiRoleListRequest req = new OapiRoleListRequest();
			// 分页查询,默认分页20条
			req.setSize(2L);
			req.setOffset(offset);
			OapiRoleListResponse rsp = client.execute(req, getDingAccessToken());
			offset += req.getSize();

			JSONObject result = JSONUtil.parseObj(rsp.getBody());
			JSONObject resultStr = result.getJSONObject("result");

			hasMore = resultStr.get("hasMore", Boolean.class);
			List<JSONObject> list = resultStr.get("list", List.class);

			if (CollectionUtils.isNotEmpty(list)) {
				list.forEach(item -> {
					List<JSONObject> roles = (List<JSONObject>) item.get("roles");
					if (CollectionUtils.isNotEmpty(roles)) {
						// 过滤出数据库不存在的角色
						List<SysRole> sysRoles = roles.stream().filter(
								role -> roleList.stream().noneMatch(db -> db.getRoleName().equals(role.get("name"))))
								.map(role -> {
									SysRole sysRole = new SysRole();
									sysRole.setRoleId(Convert.toLong(role.get("id")));
									sysRole.setRoleName(Convert.toStr(role.get("name")));
									return sysRole;
								}).collect(Collectors.toList());
						insertRole.addAll(sysRoles);
					}
				});
			}
		}
		while (hasMore);

		// 保存到数据库中
		return sysRoleService.saveBatch(insertRole);
	}

	/**
	 * 同步钉钉部门
	 *
	 * @see <a href=
	 * "https://open.dingtalk.com/document/orgapp-server/obtain-the-department-list-v2"/>
	 */
	@Override
	public Boolean syncDingDept() {
		OapiV2DepartmentListsubRequest req = new OapiV2DepartmentListsubRequest();
		req.setDeptId(1L);
		req.setLanguage("zh_CN");

		// 数据库中现有的部门
		List<SysDept> deptList = sysDeptService.list(Wrappers.emptyWrapper());

		// 查询根部门下的所有部门
		List<SysDept> insertDept = queryChildDept(req, getDingAccessToken(), deptList);

		// 去重
		List<SysDept> syncDept = insertDept.stream().distinct().collect(Collectors.toList());

		// 保存部门以及部门关系
		if (CollectionUtils.isNotEmpty(syncDept)) {
			syncDept.forEach(sysDeptService::saveDept);
		}

		return Boolean.TRUE;
	}

	/**
	 * 查询当前部门下的所有部门
	 */
	@SneakyThrows
	private List<SysDept> queryChildDept(OapiV2DepartmentListsubRequest req, String token, List<SysDept> deptList) {
		List<SysDept> sysDepts = new LinkedList<>();

		// 调用Api查询
		DingTalkClient client = new DefaultDingTalkClient(SecurityConstants.DING_OLD_DEPT_URL);
		OapiV2DepartmentListsubResponse rsp = client.execute(req, token);

		JSONObject result = JSONUtil.parseObj(rsp.getBody());
		List<JSONObject> resultList = result.get("result", List.class);

		if (CollectionUtils.isNotEmpty(resultList)) {
			// 过滤出数据库存在的部门
			List<SysDept> depts = resultList.stream()
					.filter(dept -> deptList.stream().noneMatch(sysDept -> sysDept.getName().equals(dept.get("name"))))
					.map(dept -> {
						SysDept sysDept = new SysDept();
						sysDept.setName(Convert.toStr(dept.get("name")));
						sysDept.setDeptId(Convert.toLong(dept.get("dept_id")));
						sysDept.setParentId(Convert.toLong(dept.get("parent_id")));
						return sysDept;
					}).collect(Collectors.toList());

			// 递归查询下一级
			if (CollectionUtils.isNotEmpty(depts)) {
				sysDepts.addAll(depts);
				depts.forEach(dept -> {
					OapiV2DepartmentListsubRequest request = new OapiV2DepartmentListsubRequest();
					request.setDeptId(dept.getDeptId());
					request.setLanguage("zh_CN");
					List<SysDept> childDept = queryChildDept(request, token, deptList);
					sysDepts.addAll(childDept);
				});
			}
		}

		return sysDepts;
	}

	/**
	 * 同步部门下的钉钉用户
	 *
	 * @see <a href=
	 * "https://open.dingtalk.com/document/isvapp-server/query-the-list-of-department-userids"/>
	 * @see <a href=
	 * "https://open.dingtalk.com/document/isvapp-server/query-user-details"/>
	 */
	@SneakyThrows
	@Override
	public Boolean syncDingUser(Long deptId) {
		// 数据库中现有用户
		List<SysUser> users = sysUserService.list(Wrappers.emptyWrapper());

		String token = getDingAccessToken();

		// 查询部门下的用户id列表
		DingTalkClient client = new DefaultDingTalkClient(SecurityConstants.DING_DEPT_USERIDS_URL);
		OapiUserListidRequest req = new OapiUserListidRequest();
		req.setDeptId(deptId);
		OapiUserListidResponse rsp = client.execute(req, token);

		JSONObject jsonObject = JSONUtil.parseObj(rsp.getBody());
		JSONObject result = jsonObject.getJSONObject("result");
		List<String> useridList = result.get("userid_list", List.class);

		// 查询用户详情
		if (CollectionUtils.isNotEmpty(useridList)) {
			for (String userid : useridList) {
				DingTalkClient userClient = new DefaultDingTalkClient(SecurityConstants.DING_USER_INFO_URL);
				OapiV2UserGetRequest userGetRequest = new OapiV2UserGetRequest();
				userGetRequest.setUserid(userid);
				OapiV2UserGetResponse userGetResponse = userClient.execute(userGetRequest, token);
				JSONObject userResult = JSONUtil.parseObj(userGetResponse.getBody());
				JSONObject userObj = JSONUtil.parseObj(userResult.get("result"));

				boolean exist = users.stream().anyMatch(user -> user.getPhone().equals(userObj.getStr("mobile")));

				if (!exist) {
					// 用户部门信息
					JSONArray deptOrders = userObj.getJSONArray("dept_order_list");
					JSONObject deptInfo = (JSONObject) deptOrders.get(0);

					// 用户信息
					SysUser sysUser = new SysUser();
					sysUser.setDeptId(Convert.toLong(deptInfo.get("dept_id")));
					sysUser.setAvatar(userObj.getStr("avatar"));
					sysUser.setUsername(userObj.getStr("name"));
					sysUser.setPhone(userObj.getStr("mobile"));
					sysUser.setNickname(userObj.getStr("nickname"));
					sysUser.setEmail(userObj.getStr("email"));
					sysUserService.save(sysUser);

					// 用户角色信息
					JSONArray roleList = userObj.getJSONArray("role_list");
					if (roleList != null && roleList.size() > 0) {
						JSONObject roleInfo = (JSONObject) roleList.get(0);
						Long userRoleId = Convert.toLong(roleInfo.get("id"));
						SysUserRole sysUserRole = new SysUserRole();
						sysUserRole.setUserId(sysUser.getUserId());
						sysUserRole.setRoleId(userRoleId);
						sysUserRoleService.save(sysUserRole);
					}
				}

			}

		}

		return Boolean.TRUE;
	}

}
