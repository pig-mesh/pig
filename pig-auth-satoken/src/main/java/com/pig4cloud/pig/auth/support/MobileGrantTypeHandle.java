package com.pig4cloud.pig.auth.support;

import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.oauth2.config.SaOAuth2Config;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Consts;
import cn.dev33.satoken.oauth2.logic.SaOAuth2Util;
import cn.dev33.satoken.oauth2.model.AccessTokenModel;
import cn.dev33.satoken.oauth2.model.RequestAuthModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.extra.spring.SpringUtil;
import com.pig4cloud.pig.admin.api.dto.UserDTO;
import com.pig4cloud.pig.admin.api.dto.UserInfo;
import com.pig4cloud.pig.admin.api.feign.RemoteUserService;
import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.core.util.R;
import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * 移动授权类型处理
 *
 * @author lengleng
 * @date 2024/07/23
 */
@UtilityClass
public class MobileGrantTypeHandle {

	/**
	 * 模式三：扩展手机号模式
	 * @param req 请求对象
	 * @param res 响应对象
	 * @param cfg 配置对象
	 * @return 处理结果
	 */
	public static Object mobile(SaRequest req, SaResponse res, SaOAuth2Config cfg) {
		// 1、获取请求参数
		String mobile = req.getParamNotNull(SecurityConstants.SMS_PARAMETER_NAME);
		String clientId = req.getParamNotNull(SaOAuth2Consts.Param.client_id);
		String clientSecret = req.getParamNotNull(SaOAuth2Consts.Param.client_secret);
		String scope = req.getParam(SaOAuth2Consts.Param.scope, "");

		// 2、校验 ClientScope 和 scope
		SaOAuth2Util.checkClientSecretAndScope(clientId, clientSecret, scope);

		// 3、调用API 开始登录，如果没能成功登录，则直接退出
		UserDTO query = new UserDTO();
		query.setPhone(mobile);
		R<UserInfo> infoR = SpringUtil.getBean(RemoteUserService.class).info(query);

		if (Objects.isNull(infoR.getData())) {
			return SaResult.error("手机号不存在");
		}

		StpUtil.login(infoR.getData().getSysUser().getUsername());

		// 4、构建 ra对象
		RequestAuthModel ra = new RequestAuthModel();
		ra.clientId = clientId;
		ra.loginId = StpUtil.getLoginId();
		ra.scope = scope;

		// 5、生成 Access-Token
		AccessTokenModel at = SaOAuth2Util.generateAccessToken(ra, true);

		// 6、返回 Access-Token
		return SaResult.data(at.toLineMap());
	}

}
