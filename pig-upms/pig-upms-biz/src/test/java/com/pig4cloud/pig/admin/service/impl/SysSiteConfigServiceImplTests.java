package com.pig4cloud.pig.admin.service.impl;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.pig4cloud.pig.admin.api.dto.SiteConfigDTO;
import com.pig4cloud.pig.admin.api.entity.SysOauthClientDetails;
import com.pig4cloud.pig.admin.api.entity.SysPublicParam;
import com.pig4cloud.pig.admin.service.SysI18nService;
import com.pig4cloud.pig.admin.service.SysOauthClientDetailsService;
import com.pig4cloud.pig.admin.service.SysPublicParamService;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SysSiteConfigServiceImplTests {

	@Test
	void changesOnlyTheSelectedWebsiteClientAndInvalidatesItsCache() {
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), "client"),
				SysOauthClientDetails.class);
		TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), "param"),
				SysPublicParam.class);
		SysPublicParamService params = mock(SysPublicParamService.class);
		SysOauthClientDetailsService clients = mock(SysOauthClientDetailsService.class);
		SysOauthClientDetails website = new SysOauthClientDetails();
		website.setId(1L);
		website.setClientId("website");
		website.setAdditionalInformation("{\"captcha_flag\":\"1\",\"enc_flag\":\"1\"}");
		when(clients.getOne(any())).thenReturn(website);
		when(clients.updateById(any())).thenReturn(true);
		SysSiteConfigServiceImpl service = new SysSiteConfigServiceImpl(params, mock(SysI18nService.class), clients);
		SiteConfigDTO config = new SiteConfigDTO();
		config.setCaptchaType("none");
		config.setCaptchaClientId("website");

		service.updateSiteConfig(config);

		ArgumentCaptor<Wrapper<SysOauthClientDetails>> query = ArgumentCaptor.forClass(Wrapper.class);
		verify(clients).getOne(query.capture());
		assertThat(query.getValue().getSqlSegment()).contains("client_id =");
		ArgumentCaptor<SysOauthClientDetails> update = ArgumentCaptor.forClass(SysOauthClientDetails.class);
		verify(clients).updateById(update.capture());
		assertThat(update.getValue().getId()).isEqualTo(1L);
		assertThat(update.getValue().getAdditionalInformation()).contains("\"captcha_flag\":\"0\"",
				"\"enc_flag\":\"1\"");
		verify(clients).syncClientCache();
		verifyNoMoreInteractions(clients);
	}

	@Test
	void rejectsMissingWebsiteClientBeforeChangingAnyConfiguration() {
		SysPublicParamService params = mock(SysPublicParamService.class);
		SysOauthClientDetailsService clients = mock(SysOauthClientDetailsService.class);
		SysSiteConfigServiceImpl service = new SysSiteConfigServiceImpl(params, mock(SysI18nService.class), clients);
		SiteConfigDTO config = new SiteConfigDTO();
		config.setCaptchaType("none");
		assertThatThrownBy(() -> service.updateSiteConfig(config)).isInstanceOf(IllegalArgumentException.class);
		verifyNoInteractions(params, clients);
	}

}
