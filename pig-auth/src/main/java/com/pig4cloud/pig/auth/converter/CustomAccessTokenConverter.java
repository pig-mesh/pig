package com.pig4cloud.pig.auth.converter;

import com.pig4cloud.pig.common.core.constant.SecurityConstants;
import com.pig4cloud.pig.common.security.service.PigClientDetailsService;
import com.pig4cloud.pig.common.security.service.PigUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.Map;

/**
 * @author hccake
 */
@RequiredArgsConstructor
public class CustomAccessTokenConverter extends DefaultAccessTokenConverter {

	final PigClientDetailsService pigClientDetailsService;

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, ?> convertAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		Map<String, Object> response = (Map<String, Object>) super.convertAccessToken(token, authentication);

		ClientDetails clientDetails = pigClientDetailsService
				.loadClientByClientId(authentication.getOAuth2Request().getClientId());
		if (clientDetails != null && clientDetails.getScope().contains("read_data_scope")) {
			PigUser principal = (PigUser) authentication.getPrincipal();
			response.put(SecurityConstants.DETAILS_USER_DATA_SCOPE, principal.getUserDataScope());
		}

		return response;
	}

}
