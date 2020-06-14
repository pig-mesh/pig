package com.pig4cloud.pig.common.sentinel.feign;

import com.alibaba.cloud.sentinel.feign.SentinelContractHolder;
import feign.Contract;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Target;
import feign.hystrix.FallbackFactory;
import org.springframework.beans.BeansException;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 支持自动降级注入 重写 {@link com.alibaba.cloud.sentinel.feign.SentinelFeign}
 *
 * @author lengleng
 * @date 2020/6/9
 */
public final class PigSentinelFeign {

	private PigSentinelFeign() {

	}

	public static PigSentinelFeign.Builder builder() {
		return new PigSentinelFeign.Builder();
	}

	public static final class Builder extends Feign.Builder implements ApplicationContextAware {

		private Contract contract = new Contract.Default();

		private ApplicationContext applicationContext;

		private FeignContext feignContext;

		@Override
		public Feign.Builder invocationHandlerFactory(InvocationHandlerFactory invocationHandlerFactory) {
			throw new UnsupportedOperationException();
		}

		@Override
		public PigSentinelFeign.Builder contract(Contract contract) {
			this.contract = contract;
			return this;
		}

		@Override
		public Feign build() {
			super.invocationHandlerFactory(new InvocationHandlerFactory() {
				@Override
				public InvocationHandler create(Target target, Map<Method, MethodHandler> dispatch) {
					// using reflect get fallback and fallbackFactory properties from
					// FeignClientFactoryBean because FeignClientFactoryBean is a package
					// level class, we can not use it in our package
					Object feignClientFactoryBean = PigSentinelFeign.Builder.this.applicationContext
							.getBean("&" + target.type().getName());

					Class fallback = (Class) getFieldValue(feignClientFactoryBean, "fallback");
					Class fallbackFactory = (Class) getFieldValue(feignClientFactoryBean, "fallbackFactory");
					String beanName = (String) getFieldValue(feignClientFactoryBean, "contextId");
					if (!StringUtils.hasText(beanName)) {
						beanName = (String) getFieldValue(feignClientFactoryBean, "name");
					}

					Object fallbackInstance;
					FallbackFactory fallbackFactoryInstance;
					// check fallback and fallbackFactory properties
					if (void.class != fallback) {
						fallbackInstance = getFromContext(beanName, "fallback", fallback, target.type());
						return new PigSentinelInvocationHandler(target, dispatch,
								new FallbackFactory.Default(fallbackInstance));
					}
					if (void.class != fallbackFactory) {
						fallbackFactoryInstance = (FallbackFactory) getFromContext(beanName, "fallbackFactory",
								fallbackFactory, FallbackFactory.class);
						return new PigSentinelInvocationHandler(target, dispatch, fallbackFactoryInstance);
					}
					return new PigSentinelInvocationHandler(target, dispatch);
				}

				private Object getFromContext(String name, String type, Class fallbackType, Class targetType) {
					Object fallbackInstance = feignContext.getInstance(name, fallbackType);
					if (fallbackInstance == null) {
						throw new IllegalStateException(String.format(
								"No %s instance of type %s found for feign client %s", type, fallbackType, name));
					}

					if (!targetType.isAssignableFrom(fallbackType)) {
						throw new IllegalStateException(String.format(
								"Incompatible %s instance. Fallback/fallbackFactory of type %s is not assignable to %s for feign client %s",
								type, fallbackType, targetType, name));
					}
					return fallbackInstance;
				}
			});

			super.contract(new SentinelContractHolder(contract));
			return super.build();
		}

		private Object getFieldValue(Object instance, String fieldName) {
			Field field = ReflectionUtils.findField(instance.getClass(), fieldName);
			field.setAccessible(true);
			try {
				return field.get(instance);
			}
			catch (IllegalAccessException e) {
				// ignore
			}
			return null;
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.applicationContext = applicationContext;
			feignContext = this.applicationContext.getBean(FeignContext.class);
		}

	}

}
