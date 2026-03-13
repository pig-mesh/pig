package com.pig4cloud.pig.common.feign.endpoint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Feign client 端点
 *
 * @author L.cm
 */
@Endpoint(id = "feign")
public class FeignClientEndpoint implements SmartInitializingSingleton {

	private final ApplicationContext context;

	private final List<FeignClientInfo> clientList;

	public FeignClientEndpoint(ApplicationContext context) {
		this.context = context;
		this.clientList = new ArrayList<>();
	}

	@ReadOperation
	public List<FeignClientInfo> invoke() {
		return clientList;
	}

	@Override
	public void afterSingletonsInstantiated() {
		clientList.addAll(getClientList(context));
	}

	private static List<FeignClientInfo> getClientList(ApplicationContext context) {
		Map<String, Object> feignClientMap = context.getBeansWithAnnotation(FeignClient.class);
		// 1. 解析注解
		List<FeignClientInfo> feignClientInfoList = new ArrayList<>();
		Set<Map.Entry<String, Object>> feignClientEntrySet = feignClientMap.entrySet();
		for (Map.Entry<String, Object> feignClientEntry : feignClientEntrySet) {
			String beanName = feignClientEntry.getKey();
			Object feignClientBean = feignClientEntry.getValue();
			if (feignClientBean == null) {
				continue;
			}
			// 解析注解
			Class<?> feignClientClass = feignClientBean.getClass();
			FeignClient feignClientAnn = AnnotationUtils.findAnnotation(feignClientClass, FeignClient.class);
			if (feignClientAnn == null) {
				continue;
			}
			FeignClientInfo feignClientInfo = new FeignClientInfo();
			feignClientInfo.setBeanName(beanName);
			String serviceId = feignClientAnn.value();
			String contextId = feignClientAnn.contextId();
			String url = feignClientAnn.url();
			String path = feignClientAnn.path();
			feignClientInfo.setServiceId(serviceId);
			feignClientInfo.setContextId(contextId);
			feignClientInfo.setUrl(url);
			feignClientInfo.setPath(path);
			// 组装客户端信息
			List<ClientInfo> clientInfoList = new ArrayList<>();
			Class<?>[] interfaces = feignClientClass.getInterfaces();
			for (Class<?> clientInterface : interfaces) {
				Method[] methods = clientInterface.getDeclaredMethods();
				for (Method method : methods) {
					if (method.isDefault()) {
						continue;
					}
					RequestMapping requestMapping = AnnotatedElementUtils.getMergedAnnotation(method,
							RequestMapping.class);
					if (requestMapping == null) {
						continue;
					}
					clientInfoList.add(new ClientInfo(requestMapping.method(), requestMapping.value()));
				}
			}
			feignClientInfo.setClientList(clientInfoList);
			feignClientInfoList.add(feignClientInfo);
		}
		return feignClientInfoList;
	}

	@Getter
	@Setter
	public static class FeignClientInfo {

		private String beanName;

		private String serviceId;

		private String contextId;

		private String url;

		private String path;

		private List<ClientInfo> clientList;

	}

	@Getter
	@AllArgsConstructor
	public static class ClientInfo {

		private final RequestMethod[] methods;

		private final String[] mappings;

	}

}
