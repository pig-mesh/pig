package com.pig4cloud.pig.common.job.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * xxl-job配置
 *
 * @author lishangbu
 * @date 2020/9/14
 */
@Data
@Component
@ConfigurationProperties(prefix = "xxl.job")
public class XxlJobProperties {

	private XxlAdminProperties admin = new XxlAdminProperties();

	private XxlExecutorProperties executor = new XxlExecutorProperties();

}
