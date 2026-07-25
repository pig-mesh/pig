package com.pig4cloud.pig.common.feign;

import com.pig4cloud.pig.common.feign.annotation.EnablePigFeignClients;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.boot.http.converter.autoconfigure.ClientHttpMessageConvertersCustomizer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.FeignHttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

class PigFeignClientConfigurationTests {

	@EnablePigFeignClients
	private static class TestApplication {
	}

	@Test
	void shouldInitializeConvertersBeforePublishingBean() {
		AtomicBoolean initialized = new AtomicBoolean();
		StaticListableBeanFactory clientCustomizerFactory = new StaticListableBeanFactory();
		clientCustomizerFactory.addBean("initializationProbe",
				(ClientHttpMessageConvertersCustomizer) builder -> initialized.set(true));
		StaticListableBeanFactory cloudCustomizerFactory = new StaticListableBeanFactory();

		FeignHttpMessageConverters converters = new PigFeignClientConfiguration().pigFeignHttpMessageConverters(
				clientCustomizerFactory.getBeanProvider(ClientHttpMessageConvertersCustomizer.class),
				cloudCustomizerFactory.getBeanProvider(HttpMessageConverterCustomizer.class));

		assertThat(initialized).isTrue();
		assertThat(converters.getConverters()).isNotEmpty();
	}

	@Test
	void shouldRegisterWarmupConfigurationForAllFeignClients() {
		AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(TestApplication.class,
				EnableFeignClients.class);

		assertThat(attributes).isNotNull();
		assertThat(attributes.getClassArray("defaultConfiguration")).contains(PigFeignClientConfiguration.class);
	}

}
