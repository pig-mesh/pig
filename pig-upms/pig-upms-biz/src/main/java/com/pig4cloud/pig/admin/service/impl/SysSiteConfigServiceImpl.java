/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pig.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.admin.api.entity.SysPublicParam;
import com.pig4cloud.pig.admin.config.ClientDetailsInitRunner;
import com.pig4cloud.pig.admin.dto.SiteConfigDTO;
import com.pig4cloud.pig.admin.service.SysI18nService;
import com.pig4cloud.pig.admin.service.SysOauthClientDetailsService;
import com.pig4cloud.pig.admin.service.SysPublicParamService;
import com.pig4cloud.pig.admin.service.SysSiteConfigService;
import com.pig4cloud.pig.common.core.constant.CacheConstants;
import com.pig4cloud.pig.common.core.constant.CommonConstants;
import com.pig4cloud.pig.common.core.constant.enums.YesNoEnum;
import com.pig4cloud.pig.common.core.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 站点配置服务实现
 *
 * @author lengleng
 * @date 2026-03-27
 */
@Service
@RequiredArgsConstructor
public class SysSiteConfigServiceImpl implements SysSiteConfigService {

	/**
	 * SITE_ 参数键与 DTO 字段名的映射
	 */
	private static final Map<String, String> KEY_FIELD_MAP = new LinkedHashMap<>();

	/**
	 * 布尔类型的字段集合
	 */
    private static final Set<String> BOOLEAN_FIELDS = Set.of("captchaEnable", "imageCaptchaEnable", "forceResetPwd",
            "forceLogout", "smsLoginEnable", "socialLoginEnable", "registerEnable", "i18nEnable", "darkModeEnable",
            "antiDebugEnable", "syncDingtalkEnabled", "syncWechatEnabled");

	static {
		KEY_FIELD_MAP.put("SITE_CLARITY_ID", "clarityId");
		KEY_FIELD_MAP.put("SITE_CAPTCHA_ENABLE", "captchaEnable");
		KEY_FIELD_MAP.put("SITE_CAPTCHA_TYPE", "captchaType");
		KEY_FIELD_MAP.put("SITE_IMAGE_CAPTCHA_ENABLE", "imageCaptchaEnable");
		KEY_FIELD_MAP.put("SITE_FORCE_RESET_PWD", "forceResetPwd");
		KEY_FIELD_MAP.put("SITE_FORCE_LOGOUT", "forceLogout");
		KEY_FIELD_MAP.put("SITE_SMS_LOGIN_ENABLE", "smsLoginEnable");
		KEY_FIELD_MAP.put("SITE_SOCIAL_LOGIN_ENABLE", "socialLoginEnable");
		KEY_FIELD_MAP.put("SITE_REGISTER_ENABLE", "registerEnable");
		KEY_FIELD_MAP.put("SITE_I18N_ENABLE", "i18nEnable");
		KEY_FIELD_MAP.put("SITE_DARK_MODE_ENABLE", "darkModeEnable");
        KEY_FIELD_MAP.put("SITE_ANTI_DEBUG_ENABLE", "antiDebugEnable");
        KEY_FIELD_MAP.put("SITE_ANTI_DEBUG_KEY", "antiDebugKey");
        KEY_FIELD_MAP.put("SITE_SYNC_DINGTALK_ENABLED", "syncDingtalkEnabled");
        KEY_FIELD_MAP.put("SITE_SYNC_WECHAT_ENABLED", "syncWechatEnabled");
		KEY_FIELD_MAP.put("SITE_TITLE", "title");
		KEY_FIELD_MAP.put("SITE_FOOTER", "footer");
		KEY_FIELD_MAP.put("SITE_PRIVACY_TIP", "privacyTip");
		KEY_FIELD_MAP.put("SITE_DESCRIPTION", "description");
	}

	private final SysPublicParamService sysPublicParamService;

	private final SysI18nService sysI18nService;

	private final SysOauthClientDetailsService sysOauthClientDetailsService;

	@Override
	@Cacheable(value = CacheConstants.SITE_CONFIG_DETAILS)
	public Map<String, Object> getAggregatedConfig() {
		Map<String, Object> result = new HashMap<>(4);
		result.put("i18n", sysI18nService.listMap());
		result.put("site", getSiteConfig());
		return result;
	}

	@Override
	public SiteConfigDTO getSiteConfig() {
		List<SysPublicParam> paramList = sysPublicParamService
			.list(Wrappers.<SysPublicParam>lambdaQuery().likeRight(SysPublicParam::getPublicKey, "SITE_"));

		SiteConfigDTO dto = new SiteConfigDTO();
		for (SysPublicParam param : paramList) {
			String fieldName = KEY_FIELD_MAP.get(param.getPublicKey());
			if (fieldName == null) {
				continue;
			}

			Object value;
			if (BOOLEAN_FIELDS.containsKey(fieldName)) {
				value = YesNoEnum.YES.getCode().equals(param.getPublicValue());
			}
			else {
				value = param.getPublicValue();
			}
			BeanUtil.setFieldValue(dto, fieldName, value);
		}
		return dto;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = { CacheConstants.SITE_CONFIG_DETAILS, CacheConstants.PARAMS_DETAILS }, allEntries = true)
	public void updateSiteConfig(SiteConfigDTO dto) {
		Map<String, Object> fieldValues = BeanUtil.beanToMap(dto, false, true);

		for (Map.Entry<String, String> entry : KEY_FIELD_MAP.entrySet()) {
			String paramKey = entry.getKey();
			String fieldName = entry.getValue();
			Object fieldValue = fieldValues.get(fieldName);
			if (fieldValue == null) {
				continue;
			}

			String publicValue;
			if (BOOLEAN_FIELDS.containsKey(fieldName)) {
				publicValue = YesNoEnum.getCode((Boolean) fieldValue);
			}
			else {
				publicValue = fieldValue.toString();
			}

			sysPublicParamService.update(Wrappers.<SysPublicParam>lambdaUpdate()
				.eq(SysPublicParam::getPublicKey, paramKey)
				.set(SysPublicParam::getPublicValue, publicValue));
		}

		// 同步 captcha_flag 到 OAuth 客户端扩展信息
		if (dto.getCaptchaEnable() != null) {
			syncCaptchaFlagToClients(dto.getCaptchaEnable());
		}
	}

	/**
	 * 刷新站点配置缓存
	 */
	@Override
	@CacheEvict(value = { CacheConstants.SITE_CONFIG_DETAILS, CacheConstants.PARAMS_DETAILS }, allEntries = true)
	public void refreshCache() {
		// 仅触发 @CacheEvict，无需额外逻辑
	}

	/**
	 * 同步验证码开关到所有 OAuth 客户端的 additionalInformation
	 * @param captchaEnable 是否启用验证码
	 */
	private void syncCaptchaFlagToClients(Boolean captchaEnable) {
		String captchaFlag = YesNoEnum.getCode(captchaEnable);
		List<SysOauthClientDetails> clients = sysOauthClientDetailsService.list();

        for (SysOauthClientDetails client : clients) {
            if (StrUtil.isBlank(client.getAdditionalInformation())) {
                continue;
            }
			JSONObject informationObj = JSONUtil.parseObj(client.getAdditionalInformation());
			informationObj.set(CommonConstants.CAPTCHA_FLAG, captchaFlag);
			client.setAdditionalInformation(informationObj.toString());
			sysOauthClientDetailsService.updateById(client);
        }

		// 刷新 Redis 客户端缓存
		SpringContextHolder.publishEvent(new ClientDetailsInitRunner.ClientDetailsInitEvent(this));
	}

}
