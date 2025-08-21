/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.nacos.bootstrap;

import static org.springframework.boot.context.logging.LoggingApplicationListener.CONFIG_PROPERTY;
import static org.springframework.core.io.ResourceLoader.CLASSPATH_URL_PREFIX;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.alibaba.nacos.NacosServerBasicApplication;
import com.alibaba.nacos.NacosServerWebApplication;
import com.alibaba.nacos.console.NacosConsole;
import com.alibaba.nacos.core.listener.startup.NacosStartUp;
import com.alibaba.nacos.core.listener.startup.NacosStartUpManager;
import com.alibaba.nacos.sys.env.Constants;
import com.alibaba.nacos.sys.env.DeploymentType;
import com.alibaba.nacos.sys.env.EnvUtil;

/**
 * @author nacos
 * <p>
 * nacos console 源码运行，方便开发 生产从官网下载zip最新版集群配置运行
 */
public class PigNacosApplication {

	/**
	 * 独立模式系统属性名称
	 */
	private static String STANDALONE_MODE = "nacos.standalone";

	public static void main(String[] args) {
		System.setProperty(STANDALONE_MODE, "true");
		System.setProperty(CONFIG_PROPERTY, CLASSPATH_URL_PREFIX + "logback-spring.xml");

		String type = System.getProperty(Constants.NACOS_DEPLOYMENT_TYPE, Constants.NACOS_DEPLOYMENT_TYPE_MERGED);
		DeploymentType deploymentType = DeploymentType.getType(type);
		EnvUtil.setDeploymentType(deploymentType);

		// Start Core Context
		NacosStartUpManager.start(NacosStartUp.CORE_START_UP_PHASE);
		ConfigurableApplicationContext coreContext = new SpringApplicationBuilder(NacosServerBasicApplication.class)
			.web(WebApplicationType.NONE)
			.run(args);

		// Start Server Web Context
		NacosStartUpManager.start(NacosStartUp.WEB_START_UP_PHASE);
		ConfigurableApplicationContext serverWebContext = new SpringApplicationBuilder(NacosServerWebApplication.class)
			.parent(coreContext)
			.run(args);

		// Start Console Context
		NacosStartUpManager.start(NacosStartUp.CONSOLE_START_UP_PHASE);
		ConfigurableApplicationContext consoleContext = new SpringApplicationBuilder(NacosConsole.class)
			.parent(coreContext)
			.run(args);
	}

}
