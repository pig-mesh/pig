package com.pig4cloud.pig.common.core.git;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 版本号端点
 * <p>
 * 可以通过该接口检查线上版本与代码库中的版本的一致性
 * <p>
 * 实现发布的二进制文件和代码进行关联
 * <p>
 * 该功能依赖于<a href=
 * "https://github.com/git-commit-id/git-commit-id-maven-plugin">git-commit-id-maven-plugin</a>实现
 * <p>
 * 因此,只适用于项目构建工具为<a href="https://maven.apache.org/">maven</a>,版本控制软件为<a href=
 * "https://git-scm.com">git</a>的情况
 *
 * @author lishangbu
 * @date 2021/10/10
 */
@Slf4j
@Endpoint(id = "gitrepositorystate")
public class GitRepositoryStateEndpoint {

	private GitRepositoryState gitRepositoryState;

	@ReadOperation
	public GitRepositoryState getGitRepositoryState() throws IOException {
		if (gitRepositoryState != null) {
			return gitRepositoryState;
		}
		try {
			File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + "git.properties");
			@Cleanup
			InputStream inputStream = new FileInputStream(file);
			if (inputStream != null) {
				Properties properties = new Properties();
				properties.load(inputStream);
				gitRepositoryState = new GitRepositoryState(properties);
				return gitRepositoryState;
			}
		}
		catch (RuntimeException e) {
			log.error("加载git资源文件git.properties失败,错误原因为:[{}]", e.getMessage(), e);
		}
		// 资源文件加载可能没有报错，但在某些情况下可能为空,此时,日志里给出提示,交给开发人员自行解决
		log.warn("git仓库状态信息初始化失败,您将无法调用/actuator/gitrepositorystate端点");
		log.warn("请保证这个仓库是一个git托管的仓库，并且经过了maven的编译");
		return null;
	}

}
