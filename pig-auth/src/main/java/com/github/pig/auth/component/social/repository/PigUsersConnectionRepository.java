package com.github.pig.auth.component.social.repository;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author lengleng
 * @date 2018/1/18
 *  覆盖原有的保存的逻辑，不保存登录状态
 */
public class PigUsersConnectionRepository implements UsersConnectionRepository {
    private ConnectionSignUp connectionSignUp;

    public ConnectionSignUp getConnectionSignUp() {
        return connectionSignUp;
    }

    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        return Collections.singletonList(connection.getKey().getProviderUserId());
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        return providerUserIds;
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        return new PigConnectionRepository();
    }
}
