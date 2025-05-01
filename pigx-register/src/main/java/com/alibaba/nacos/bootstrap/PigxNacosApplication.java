/*
 * Copyright 1999-2023 Alibaba Group Holding Ltd.
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

import com.alibaba.nacos.NacosServerBasicApplication;
import com.alibaba.nacos.NacosServerWebApplication;
import com.alibaba.nacos.bootstrap.config.ConfigConstants;
import com.alibaba.nacos.console.NacosConsole;
import com.alibaba.nacos.core.listener.startup.NacosStartUp;
import com.alibaba.nacos.core.listener.startup.NacosStartUpManager;
import com.alibaba.nacos.sys.env.Constants;
import com.alibaba.nacos.sys.env.DeploymentType;
import com.alibaba.nacos.sys.env.EnvUtil;
import org.springframework.boot.Banner;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jmx.export.MBeanExporter;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * Nacos启动类。
 * <p>
 * 针对于开发环境的启动类，主要是为了方便开发者开箱即用 PIGX ，而不需要考虑 nacos 的各版本兼容性问题。
 *
 * @author lengleng
 * @date 2025/04/30
 */
@SpringBootApplication
public class PigxNacosApplication {

    private static final String SPRING_JXM_ENABLED = "spring.jmx.enabled";

    public static void main(String[] args) {
        // 修改这里的配置项，来指定Nacos Web Console的端口
        System.setProperty(ConfigConstants.NACOS_CONSOLE_PORT, "18080");
        String type = System.getProperty(Constants.NACOS_DEPLOYMENT_TYPE, Constants.NACOS_DEPLOYMENT_TYPE_MERGED);
        DeploymentType deploymentType = DeploymentType.getType(type);
        System.setProperty(ConfigConstants.STANDALONE_MODE, "true");
        EnvUtil.setDeploymentType(deploymentType);
        switch (deploymentType) {
            case MERGED:
                startWithConsole(args);
                break;
            case SERVER:
                startWithoutConsole(args);
                break;
            case CONSOLE:
                startOnlyConsole(args);
                break;
            default:
                throw new IllegalArgumentException("Unsupported nacos deployment type " + type);
        }
    }

    private static void prepareCoreContext(ConfigurableApplicationContext coreContext) {
        if (coreContext.getEnvironment().getProperty(SPRING_JXM_ENABLED, Boolean.class, false)) {
            // Avoid duplicate registration MBean to exporter.
            coreContext.getBean(MBeanExporter.class).setRegistrationPolicy(RegistrationPolicy.IGNORE_EXISTING);
        }
    }

    private static void startWithoutConsole(String[] args) {
        ConfigurableApplicationContext coreContext = startCoreContext(args);
        prepareCoreContext(coreContext);
        ConfigurableApplicationContext webContext = startServerWebContext(args, coreContext);
    }

    private static void startWithConsole(String[] args) {
        ConfigurableApplicationContext coreContext = startCoreContext(args);
        prepareCoreContext(coreContext);
        ConfigurableApplicationContext serverWebContext = startServerWebContext(args, coreContext);
        ConfigurableApplicationContext consoleContext = startConsoleContext(args, coreContext);
    }

    private static ConfigurableApplicationContext startCoreContext(String[] args) {
        NacosStartUpManager.start(NacosStartUp.CORE_START_UP_PHASE);
        return new SpringApplicationBuilder(NacosServerBasicApplication.class).web(WebApplicationType.NONE)
                .banner(getBanner("core-banner.txt")).run(args);
    }

    private static ConfigurableApplicationContext startServerWebContext(String[] args,
                                                                        ConfigurableApplicationContext coreContext) {
        NacosStartUpManager.start(NacosStartUp.WEB_START_UP_PHASE);
        return new SpringApplicationBuilder(NacosServerWebApplication.class).parent(coreContext)
                .banner(getBanner("nacos-server-web-banner.txt")).run(args);
    }

    private static ConfigurableApplicationContext startConsoleContext(String[] args,
                                                                      ConfigurableApplicationContext coreContext) {
        NacosStartUpManager.start(NacosStartUp.CONSOLE_START_UP_PHASE);
        return new SpringApplicationBuilder(NacosConsole.class).parent(coreContext)
                .banner(getBanner("nacos-console-banner.txt")).run(args);
    }

    private static void startOnlyConsole(String[] args) {
        NacosStartUpManager.start(NacosStartUp.CONSOLE_START_UP_PHASE);
        ConfigurableApplicationContext consoleContext = new SpringApplicationBuilder(NacosConsole.class).banner(
                getBanner("nacos-console-banner.txt")).run(args);
    }

    private static Banner getBanner(String bannerFileName) {
        return new ResourceBanner(new ClassPathResource(bannerFileName));
    }
}