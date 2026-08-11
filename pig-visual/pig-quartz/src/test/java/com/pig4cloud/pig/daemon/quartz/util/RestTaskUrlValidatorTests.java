package com.pig4cloud.pig.daemon.quartz.util;

import com.pig4cloud.pig.daemon.quartz.config.QuartzProtectionProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RestTaskUrlValidatorTests {

	private QuartzProtectionProperties properties;

	private RestTaskUrlValidator validator;

	@BeforeEach
	void setUp() {
		properties = new QuartzProtectionProperties();
		validator = new RestTaskUrlValidator(properties);
	}

	@Test
	void rejectsAllUrlsWhenWhitelistIsEmpty() {
		assertThat(validator.isAllowed("https://api.example.com/jobs")).isFalse();
	}

	@Test
	void acceptsPathAndQueryForWhitelistedOrigin() {
		properties.setRestTaskUrlWhitelist(Set.of("https://api.example.com"));

		assertThat(validator.isAllowed("https://api.example.com/jobs/run?id=1")).isTrue();
	}

	@Test
	void normalizesHostCaseAndDefaultPort() {
		properties.setRestTaskUrlWhitelist(Set.of("https://API.EXAMPLE.COM:443"));

		assertThat(validator.isAllowed("https://api.example.com/jobs")).isTrue();
	}

	@Test
	void rejectsPrefixAndUserInfoBypasses() {
		properties.setRestTaskUrlWhitelist(Set.of("http://www.baidu.com"));

		assertThat(validator.isAllowed("http://www.baidu.com.evil.example/internal")).isFalse();
		assertThat(validator.isAllowed("http://www.baidu.com@127.0.0.1/internal")).isFalse();
	}

	@Test
	void rejectsDifferentSchemeOrPort() {
		properties.setRestTaskUrlWhitelist(Set.of("https://api.example.com"));

		assertThat(validator.isAllowed("http://api.example.com/jobs")).isFalse();
		assertThat(validator.isAllowed("https://api.example.com:8443/jobs")).isFalse();
	}

	@Test
	void ignoresInvalidWhitelistEntries() {
		properties.setRestTaskUrlWhitelist(Set.of("https://api.example.com/private", "file:///tmp/task"));

		assertThat(validator.isAllowed("https://api.example.com/private")).isFalse();
	}

}
