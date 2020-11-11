package com.alibaba.nacos.utils;

import com.alibaba.nacos.config.ConfigConstants;
import com.alibaba.nacos.sys.utils.ApplicationUtils;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author lengleng
 * @date 2020/11/7
 * <p>
 * 初始化启动配置
 */
@UtilityClass
public class StartUpEnvInitUtil {

	@SneakyThrows
	public void init() {
		// 启动时候， 创建对应目录. 避免 1.4.0 启动失败
		String confFilePath = ApplicationUtils.getConfFilePath();
		FileUtils.forceMkdir(new File(confFilePath));

		// 设置环境变量，Nacos 读取这部分配置
		System.setProperty(ConfigConstants.NACOS_TOMCAT_BASEDIR, ApplicationUtils.getNacosTmpDir());
		System.setProperty(ConfigConstants.STANDALONE_MODE, Boolean.TRUE.toString());
		System.setProperty(ConfigConstants.AUTH_ENABLED, Boolean.FALSE.toString());
		System.setProperty(ConfigConstants.MCP_SERVER_ENABLED, Boolean.FALSE.toString());
	}

}
