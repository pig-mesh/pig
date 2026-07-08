package com.pig4cloud.pig.common.datasource.config;

import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link JdbcDynamicDataSourceProvider} 单元测试：验证 query-ds-enabled 开关的默认开启与显式关闭行为
 *
 * @author lengleng
 * @date 2026/7/6
 */
class JdbcDynamicDataSourceProviderTests {

	@Test
	void enablesJdbcTableLoadingByDefault() {
		DruidDataSourceProperties properties = druidProperties();
		JdbcDynamicDataSourceProvider provider = newProvider(properties);

		assertThat(provider.isQueryDsEnabled()).isTrue();
	}

	@Test
	void skipsJdbcTableLoadingWhenDisabledExplicitly() {
		DruidDataSourceProperties properties = druidProperties();
		properties.setQueryDsEnabled(false);

		JdbcDynamicDataSourceProvider provider = newProvider(properties);

		assertThat(provider.loadDataSources()).isEmpty();
		assertThat(provider.isQueryDsEnabled()).isFalse();
	}

	private static JdbcDynamicDataSourceProvider newProvider(DruidDataSourceProperties properties) {
		StringEncryptor stringEncryptor = new StringEncryptor() {
			@Override
			public String encrypt(String message) {
				return message;
			}

			@Override
			public String decrypt(String encryptedMessage) {
				return encryptedMessage;
			}
		};
		return new JdbcDynamicDataSourceProvider(new DefaultDataSourceCreator(), stringEncryptor, properties);
	}

	private static DruidDataSourceProperties druidProperties() {
		DruidDataSourceProperties properties = new DruidDataSourceProperties();
		properties.setDriverClassName("not.exists.Driver");
		properties.setUrl("jdbc:not-executed");
		properties.setUsername("root");
		properties.setPassword("root");
		return properties;
	}

}
