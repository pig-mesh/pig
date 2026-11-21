package com.pig4cloud.pig.common.jasypt;

import com.ulisesbocchio.jasyptspringbootstarter.JasyptSpringBootAutoConfiguration;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class JasyptBoot42ConfigurationTests {

	@Test
	void shouldDecryptPropertiesAndBindEncryptedEnvironmentVariables() {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword("test-only-password");
		encryptor.setAlgorithm("PBEWithMD5AndDES");
		String encrypted = "ENC(" + encryptor.encrypt("decrypted-value") + ")";
		new ApplicationContextRunner()
				.withConfiguration(AutoConfigurations.of(JasyptBoot42Configuration.class, JasyptSpringBootAutoConfiguration.class))
				.withPropertyValues("jasypt.encryptor.password=test-only-password",
						"jasypt.encryptor.algorithm=PBEWithMD5AndDES",
						"jasypt.encryptor.iv-generator-classname=org.jasypt.iv.NoIvGenerator",
						"sample.property=" + encrypted)
				.withInitializer(context -> context.getEnvironment().getPropertySources().replace("systemEnvironment",
						new SystemEnvironmentPropertySource("systemEnvironment",
								new HashMap<>(Map.of("SAMPLE_ENVIRONMENT", encrypted,
										"SAMPLE_REFERENCE", "${sample.property}")))))
				.run(context -> {
					assertThat(context).hasNotFailed();
					assertThat(context.getBean("enableEncryptablePropertySourcesPostProcessor"))
							.isInstanceOf(JasyptBoot42Configuration.Boot42EncryptablePostProcessor.class);
					assertThat(context.getEnvironment().getProperty("sample.property")).isEqualTo("decrypted-value");
					assertThat(context.getEnvironment().getProperty("sample.reference")).isEqualTo("decrypted-value");
					assertThat(Binder.get(context.getEnvironment()).bind("sample.environment", String.class).get())
							.isEqualTo("decrypted-value");
				});
	}

}
