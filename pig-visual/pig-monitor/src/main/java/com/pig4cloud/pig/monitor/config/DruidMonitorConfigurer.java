package com.pig4cloud.pig.monitor.config;

import com.pig4cloud.pig.monitor.support.MonitorViewServlet;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author lengleng
 * @date 2020/10/19
 * <p>
 * druid 监控配置类
 */
@Configuration
public class DruidMonitorConfigurer {

	@Bean
	public ServletRegistrationBean statViewServletRegistrationBean() {
		ServletRegistrationBean registrationBean = new ServletRegistrationBean();
		registrationBean.setServlet(new MonitorViewServlet());
		registrationBean.addUrlMappings("/druid/*");
		return registrationBean;
	}

	@Data
	@Component
	@ConfigurationProperties(prefix = "monitor")
	public class MonitorProperties {

		/**
		 * 需要监控的服务
		 */
		private List<String> applications;

	}

}
