package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiUserListidRequest;
import com.dingtalk.api.request.OapiV2DepartmentListsubRequest;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiUserListidResponse;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysSocialDetails;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.mapper.SysSocialDetailsMapper;
import com.pig4cloud.pigx.admin.service.ConnectService;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.enums.LoginTypeEnum;
import com.pig4cloud.pigx.common.core.exception.ErrorCodes;
import com.pig4cloud.pigx.common.core.util.MsgUtils;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	private final SysDeptService sysDeptService;

	private final SysUserService sysUserService;

	/**
	 * 同步钉钉部门
	 * @return Boolean
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
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
	 * 同步部门下的钉钉用户
	 * @param deptId 部门ID
	 * @return R
	 */
	@SneakyThrows
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R syncDingUser(Long deptId) {
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

		if (result == null) {
			return R.ok();
		}
		List<String> useridList = result.get("userid_list", List.class);

		// 查询用户详情
		for (String userid : useridList) {
			DingTalkClient userClient = new DefaultDingTalkClient(SecurityConstants.DING_USER_INFO_URL);
			OapiV2UserGetRequest userGetRequest = new OapiV2UserGetRequest();
			userGetRequest.setUserid(userid);
			OapiV2UserGetResponse userGetResponse = userClient.execute(userGetRequest, token);
			JSONObject userResult = JSONUtil.parseObj(userGetResponse.getBody());
			JSONObject userObj = JSONUtil.parseObj(userResult.get("result"));

			boolean exist = users.stream().filter(user -> StrUtil.isNotBlank(user.getPhone()))
					.anyMatch(user -> user.getPhone().equals(userObj.getStr("mobile")));

			if (exist || StrUtil.isBlank(userObj.getStr("mobile"))) {
				log.info("用户已存在或手机号不合法 {}", userid);
				continue;
			}

			// 用户部门信息
			JSONArray deptOrders = userObj.getJSONArray("dept_order_list");
			JSONObject deptInfo = (JSONObject) deptOrders.get(0);

			// 用户信息
			UserDTO userDTO = new UserDTO();
			userDTO.setDeptId(deptInfo.getLong("dept_id"));
			userDTO.setAvatar(userObj.getStr("avatar"));
			userDTO.setUsername(userObj.getStr("mobile"));
			userDTO.setPhone(userObj.getStr("mobile"));
			userDTO.setNickname(userObj.getStr("nickname"));
			userDTO.setName(userObj.getStr("name"));
			userDTO.setEmail(userObj.getStr("email"));
			// 初始化密码为 手机号
			userDTO.setPassword(userObj.getStr("mobile"));
			sysUserService.saveUser(userDTO);
		}

		return R.ok();
	}

	/**
	 * 同步企微部门
	 * @return R
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public R<Boolean> syncCpDept() {
		WxCpService wxCpService = new WxCpServiceImpl();
		wxCpService.setWxCpConfigStorage(getCpConfig());

		List<WxCpDepart> departList;
		try {
			departList = wxCpService.getDepartmentService().list(null);
		}
		catch (WxErrorException e) {
			log.error("获取企业微信部门列表失败", e);
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_CONNECT_CP_DEPT_SYNC_ERROR));
		}

		if (CollUtil.isEmpty(departList)) {
			return R.ok();
		}

		// 数据库中现有的部门
		List<SysDept> deptList = sysDeptService.list(Wrappers.emptyWrapper());
		departList.stream()
				.filter(depart -> deptList.stream().noneMatch(sysDept -> depart.getName().equals(sysDept.getName())))
				.map(dept -> {
					SysDept sysDept = new SysDept();
					sysDept.setName(dept.getName());
					sysDept.setDeptId(dept.getId());
					sysDept.setParentId(dept.getParentId());
					sysDept.setSortOrder(dept.getOrder().intValue());
					return sysDept;
				}).forEach(sysDeptService::saveDept);
		return R.ok();
	}

	/**
	 * 同步企微用户
	 * @return R
	 */
	@Override
	public R<Boolean> syncCpUser() {
		WxCpService wxCpService = new WxCpServiceImpl();
		wxCpService.setWxCpConfigStorage(getCpConfig());

		List<WxCpUser> cpUserList;
		try {
			cpUserList = wxCpService.getUserService().listByDepartment(1L, true, 0);
		}
		catch (WxErrorException e) {
			log.error("获取企业微信用户列表失败", e);
			return R.failed(MsgUtils.getMessage(ErrorCodes.SYS_CONNECT_CP_USER_SYNC_ERROR));
		}

		if (CollUtil.isEmpty(cpUserList)) {
			return R.ok();
		}

		// 系统原有用户
		List<SysUser> userList = sysUserService.list(Wrappers.emptyWrapper());

		for (WxCpUser cpUser : cpUserList) {
			boolean exist = userList.stream().filter(user -> StrUtil.isNotBlank(user.getPhone()))
					.anyMatch(user -> user.getPhone().equals(cpUser.getMobile()));
			if (exist || StrUtil.isBlank(cpUser.getMobile())) {
				log.info("用户已存在或手机号不合法跳过 {}", cpUser);
				continue;
			}

			UserDTO user = new UserDTO();
			user.setUsername(cpUser.getMobile());
			user.setName(cpUser.getName());
			user.setDeptId(cpUser.getDepartIds()[0]);
			user.setEmail(cpUser.getEmail());
			user.setPhone(cpUser.getMobile());
			user.setAvatar(cpUser.getAvatar());
			// 初始化密码为 手机号
			user.setPassword(cpUser.getMobile());
			sysUserService.saveUser(user);
		}

		return R.ok();
	}

	private String getDingAccessToken() {
		SysSocialDetails dingTalk = sysSocialDetailsMapper.selectOne(Wrappers.<SysSocialDetails>lambdaQuery()
				.eq(SysSocialDetails::getType, LoginTypeEnum.DINGTALK.getType()));

		DingTalkClient client = new DefaultDingTalkClient(SecurityConstants.DING_OLD_GET_TOKEN);
		OapiGettokenRequest request = new OapiGettokenRequest();
		request.setAppkey(dingTalk.getAppId());
		request.setAppsecret(dingTalk.getAppSecret());
		request.setHttpMethod(HttpMethod.GET.name());
		try {
			OapiGettokenResponse response = client.execute(request);
			return response.getAccessToken();
		}
		catch (Exception e) {
			log.error("调用钉钉异常", e);
		}
		return null;
	}

	private WxCpDefaultConfigImpl getCpConfig() {
		SysSocialDetails cp = sysSocialDetailsMapper.selectOne(Wrappers.<SysSocialDetails>lambdaQuery()
				.eq(SysSocialDetails::getType, LoginTypeEnum.WEIXIN_CP.getType()));

		WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
		config.setCorpId(cp.getAppId()); // 设置微信企业号的appid
		config.setCorpSecret(cp.getAppSecret()); // 设置微信企业号的app
		// corpSecret
		JSONObject ext = JSONUtil.parseObj(cp.getExt());
		config.setAgentId(ext.getInt("agentId")); // 设置微信企业号应用ID
		config.setToken(ext.getStr("token")); // 设置微信企业号应用的token
		config.setAesKey(ext.getStr("aesKey")); // 设置微信企业号应用的EncodingAESKey

		return config;
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

		if (CollUtil.isEmpty(resultList)) {
			return sysDepts;
		}

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

		return sysDepts;
	}

}
