package com.pig4cloud.pig.common.core.git;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.io.IOException;
import java.util.Properties;

/**
 * 版本号端点
 *
 * @author lishangbu
 * @date 2021/10/10
 */
@Endpoint(id = "gitrepositorystate")
public class GitRepositoryStateEndpoint {

    private GitRepositoryState gitRepositoryState;

    @ReadOperation
    public GitRepositoryState getGitRepositoryState() throws IOException {
        if (gitRepositoryState == null) {
            Properties properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream("git.properties"));
            gitRepositoryState = new GitRepositoryState(properties);
        }
        return gitRepositoryState;
    }
}
