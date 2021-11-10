package com.pig4cloud.pig.common.security.component;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author efenderbosch
 * @date 2020/9/30
 * <p>
 * @link https://github.com/spring-projects/spring-security-oauth/pull/1660
 * 重写RedisTokenStore ,主要解决 #1814 oauth2中client_id_to_access数据膨胀问题
 */
public class PigRedisTokenStore implements TokenStore {

	private static final String ACCESS = "access:";

	private static final String AUTH_TO_ACCESS = "auth_to_access:";

	private static final String AUTH = "auth:";

	private static final String REFRESH_AUTH = "refresh_auth:";

	private static final String REFRESH = "refresh:";

	private static final String REFRESH_TO_ACCESS = "refresh_to_access:";

	private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access_z:";

	private static final String UNAME_TO_ACCESS = "uname_to_access_z:";

	private static final boolean springDataRedis_2_0 = ClassUtils.isPresent(
			"org.springframework.data.redis.connection.RedisStandaloneConfiguration",
			RedisTokenStore.class.getClassLoader());

	private final RedisConnectionFactory connectionFactory;

	private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();

	private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

	private String prefix = "";

	private Method redisConnectionSet_2_0;

	public PigRedisTokenStore(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
		if (springDataRedis_2_0) {
			this.loadRedisConnectionMethods_2_0();
		}
	}

	public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
		this.authenticationKeyGenerator = authenticationKeyGenerator;
	}

	public void setSerializationStrategy(RedisTokenStoreSerializationStrategy serializationStrategy) {
		this.serializationStrategy = serializationStrategy;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	private void loadRedisConnectionMethods_2_0() {
		this.redisConnectionSet_2_0 = ReflectionUtils.findMethod(RedisConnection.class, "set", byte[].class,
				byte[].class);
	}

	private RedisConnection getConnection() {
		return connectionFactory.getConnection();
	}

	private byte[] serialize(Object object) {
		return serializationStrategy.serialize(object);
	}

	private byte[] serializeKey(String object) {
		return serialize(prefix + object);
	}

	private OAuth2AccessToken deserializeAccessToken(byte[] bytes) {
		return serializationStrategy.deserialize(bytes, OAuth2AccessToken.class);
	}

	private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
		return serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
	}

	private OAuth2RefreshToken deserializeRefreshToken(byte[] bytes) {
		return serializationStrategy.deserialize(bytes, OAuth2RefreshToken.class);
	}

	private byte[] serialize(String string) {
		return serializationStrategy.serialize(string);
	}

	private String deserializeString(byte[] bytes) {
		return serializationStrategy.deserializeString(bytes);
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		String key = authenticationKeyGenerator.extractKey(authentication);
		byte[] serializedKey = serializeKey(AUTH_TO_ACCESS + key);
		byte[] bytes;
		try (RedisConnection conn = getConnection()) {
			bytes = conn.get(serializedKey);
		}
		OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
		if (accessToken != null) {
			OAuth2Authentication storedAuthentication = readAuthentication(accessToken.getValue());
			if ((storedAuthentication == null
					|| !key.equals(authenticationKeyGenerator.extractKey(storedAuthentication)))) {
				// Keep the stores consistent (maybe the same user is
				// represented by this authentication but the details have
				// changed)
				storeAccessToken(accessToken, authentication);
			}

		}
		return accessToken;
	}

	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
		return readAuthentication(token.getValue());
	}

	@Override
	public OAuth2Authentication readAuthentication(String token) {
		byte[] bytes;
		try (RedisConnection conn = getConnection()) {
			bytes = conn.get(serializeKey(AUTH + token));
		}
		return deserializeAuthentication(bytes);
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
		return readAuthenticationForRefreshToken(token.getValue());
	}

	public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
		try (RedisConnection conn = getConnection()) {
			byte[] bytes = conn.get(serializeKey(REFRESH_AUTH + token));
			return deserializeAuthentication(bytes);
		}
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		byte[] serializedAccessToken = serialize(token);
		byte[] serializedAuth = serialize(authentication);
		byte[] accessKey = serializeKey(ACCESS + token.getValue());
		byte[] authKey = serializeKey(AUTH + token.getValue());
		byte[] authToAccessKey = serializeKey(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication));
		byte[] approvalKey = serializeKey(UNAME_TO_ACCESS + getApprovalKey(authentication));
		byte[] clientId = serializeKey(CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());

		try (RedisConnection conn = getConnection()) {
			conn.openPipeline();
			if (springDataRedis_2_0) {
				try {
					this.redisConnectionSet_2_0.invoke(conn, accessKey, serializedAccessToken);
					this.redisConnectionSet_2_0.invoke(conn, authKey, serializedAuth);
					this.redisConnectionSet_2_0.invoke(conn, authToAccessKey, serializedAccessToken);
				}
				catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			else {
				conn.set(accessKey, serializedAccessToken);
				conn.set(authKey, serializedAuth);
				conn.set(authToAccessKey, serializedAccessToken);
			}

			if (token.getExpiration() != null) {
				int seconds = token.getExpiresIn();
				long expirationTime = token.getExpiration().getTime();

				if (!authentication.isClientOnly()) {
					conn.zAdd(approvalKey, expirationTime, serializedAccessToken);
				}
				conn.zAdd(clientId, expirationTime, serializedAccessToken);

				conn.expire(accessKey, seconds);
				conn.expire(authKey, seconds);
				conn.expire(authToAccessKey, seconds);
				conn.expire(clientId, seconds);
				conn.expire(approvalKey, seconds);
			}
			else {
				conn.zAdd(clientId, -1, serializedAccessToken);
				if (!authentication.isClientOnly()) {
					conn.zAdd(approvalKey, -1, serializedAccessToken);
				}
			}
			OAuth2RefreshToken refreshToken = token.getRefreshToken();
			if (refreshToken != null && refreshToken.getValue() != null) {
				byte[] auth = serialize(token.getValue());
				byte[] refreshToAccessKey = serializeKey(REFRESH_TO_ACCESS + token.getRefreshToken().getValue());
				if (springDataRedis_2_0) {
					try {
						this.redisConnectionSet_2_0.invoke(conn, refreshToAccessKey, auth);
					}
					catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				}
				else {
					conn.set(refreshToAccessKey, auth);
				}
				if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
					ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
					Date expiration = expiringRefreshToken.getExpiration();
					if (expiration != null) {
						int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
								.intValue();
						conn.expire(refreshToAccessKey, seconds);
					}
				}
			}
			conn.closePipeline();
		}
	}

	private static String getApprovalKey(OAuth2Authentication authentication) {
		String userName = authentication.getUserAuthentication() == null ? ""
				: authentication.getUserAuthentication().getName();
		return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
	}

	private static String getApprovalKey(String clientId, String userName) {
		return clientId + (userName == null ? "" : ":" + userName);
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken accessToken) {
		removeAccessToken(accessToken.getValue());
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		byte[] key = serializeKey(ACCESS + tokenValue);
		byte[] bytes;
		try (RedisConnection conn = getConnection()) {
			bytes = conn.get(key);
		}
		return deserializeAccessToken(bytes);
	}

	public void removeAccessToken(String tokenValue) {
		byte[] accessKey = serializeKey(ACCESS + tokenValue);
		byte[] authKey = serializeKey(AUTH + tokenValue);
		try (RedisConnection conn = getConnection()) {
			conn.openPipeline();
			conn.get(accessKey);
			conn.get(authKey);
			conn.del(accessKey);
			// Don't remove the refresh token - it's up to the caller to do that
			conn.del(authKey);
			List<Object> results = conn.closePipeline();
			byte[] access = (byte[]) results.get(0);
			byte[] auth = (byte[]) results.get(1);

			OAuth2Authentication authentication = deserializeAuthentication(auth);
			if (authentication != null) {
				String key = authenticationKeyGenerator.extractKey(authentication);
				byte[] authToAccessKey = serializeKey(AUTH_TO_ACCESS + key);
				byte[] unameKey = serializeKey(UNAME_TO_ACCESS + getApprovalKey(authentication));
				byte[] clientId = serializeKey(CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());
				conn.openPipeline();
				conn.del(authToAccessKey);
				conn.zRem(unameKey, access);
				conn.zRem(clientId, access);
				conn.del(serialize(ACCESS + key));
				conn.closePipeline();
			}
		}
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		byte[] refreshKey = serializeKey(REFRESH + refreshToken.getValue());
		byte[] refreshAuthKey = serializeKey(REFRESH_AUTH + refreshToken.getValue());
		byte[] serializedRefreshToken = serialize(refreshToken);
		try (RedisConnection conn = getConnection()) {
			conn.openPipeline();
			if (springDataRedis_2_0) {
				try {
					this.redisConnectionSet_2_0.invoke(conn, refreshKey, serializedRefreshToken);
					this.redisConnectionSet_2_0.invoke(conn, refreshAuthKey, serialize(authentication));
				}
				catch (Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			else {
				conn.set(refreshKey, serializedRefreshToken);
				conn.set(refreshAuthKey, serialize(authentication));
			}
			if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
				ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
				Date expiration = expiringRefreshToken.getExpiration();
				if (expiration != null) {
					int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L).intValue();
					conn.expire(refreshKey, seconds);
					conn.expire(refreshAuthKey, seconds);
				}
			}
			conn.closePipeline();
		}
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
		byte[] key = serializeKey(REFRESH + tokenValue);
		byte[] bytes;
		try (RedisConnection conn = getConnection()) {
			bytes = conn.get(key);
		}
		return deserializeRefreshToken(bytes);
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
		removeRefreshToken(refreshToken.getValue());
	}

	public void removeRefreshToken(String tokenValue) {
		byte[] refreshKey = serializeKey(REFRESH + tokenValue);
		byte[] refreshAuthKey = serializeKey(REFRESH_AUTH + tokenValue);
		byte[] refresh2AccessKey = serializeKey(REFRESH_TO_ACCESS + tokenValue);
		try (RedisConnection conn = getConnection()) {
			conn.openPipeline();
			conn.del(refreshKey);
			conn.del(refreshAuthKey);
			conn.del(refresh2AccessKey);
			conn.closePipeline();
		}
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		removeAccessTokenUsingRefreshToken(refreshToken.getValue());
	}

	private void removeAccessTokenUsingRefreshToken(String refreshToken) {
		byte[] key = serializeKey(REFRESH_TO_ACCESS + refreshToken);
		List<Object> results;
		try (RedisConnection conn = getConnection()) {
			conn.openPipeline();
			conn.get(key);
			conn.del(key);
			results = conn.closePipeline();
		}
		byte[] bytes = (byte[]) results.get(0);
		String accessToken = deserializeString(bytes);
		if (accessToken != null) {
			removeAccessToken(accessToken);
		}
	}

	private List<byte[]> getZByteLists(byte[] key, RedisConnection conn) {
		// Sorted Set expiration maintenance
		long currentTime = System.currentTimeMillis();
		conn.zRemRangeByScore(key, 0, currentTime);

		List<byte[]> byteList;
		Long size = conn.zCard(key);
		assert size != null;
		byteList = new ArrayList<>(size.intValue());
		Cursor<RedisZSetCommands.Tuple> cursor = conn.zScan(key, ScanOptions.NONE);

		while (cursor.hasNext()) {
			RedisZSetCommands.Tuple t = cursor.next();

			// Probably not necessary because of the maintenance at the beginning but why
			// not...
			if (t.getScore() == -1 || t.getScore() > currentTime) {
				byteList.add(t.getValue());
			}
		}
		return byteList;
	}

	/**
	 * Runs a maintenance of the RedisTokenStore.
	 * <p>
	 * SortedSets UNAME_TO_ACCESS and CLIENT_ID_TO_ACCESS contains access tokens that can
	 * expire. This expiration is set as a score of the Redis SortedSet data structure.
	 * Redis does not support expiration of items in a container data structure. It
	 * supports only expiration of whole key. In case there is still new access tokens
	 * being stored into the RedisTokenStore before whole key gets expired, the expiration
	 * is prolonged and the key is not effectively deleted. To do "garbage collection"
	 * this method should be called once upon a time.
	 * @return how many items were removed
	 */
	public long doMaintenance() {
		long removed = 0;
		try (RedisConnection conn = getConnection()) {
			// client_id_to_acccess maintenance
			Cursor<byte[]> clientToAccessKeys = conn
					.scan(ScanOptions.scanOptions().match(prefix + CLIENT_ID_TO_ACCESS + "*").build());
			while (clientToAccessKeys.hasNext()) {
				byte[] clientIdToAccessKey = clientToAccessKeys.next();

				removed += conn.zRemRangeByScore(clientIdToAccessKey, 0, System.currentTimeMillis());
			}

			// uname_to_access maintenance
			Cursor<byte[]> unameToAccessKeys = conn
					.scan(ScanOptions.scanOptions().match(prefix + UNAME_TO_ACCESS + "*").build());
			while (unameToAccessKeys.hasNext()) {
				byte[] unameToAccessKey = unameToAccessKeys.next();

				removed += conn.zRemRangeByScore(unameToAccessKey, 0, System.currentTimeMillis());
			}
		}
		return removed;
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
		byte[] approvalKey = serializeKey(UNAME_TO_ACCESS + getApprovalKey(clientId, userName));
		List<byte[]> byteList;
		try (RedisConnection conn = getConnection()) {
			byteList = getZByteLists(approvalKey, conn);
		}
		if (byteList.size() == 0) {
			return Collections.emptySet();
		}
		List<OAuth2AccessToken> accessTokens = new ArrayList<>(byteList.size());
		for (byte[] bytes : byteList) {
			OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
			accessTokens.add(accessToken);
		}
		return Collections.unmodifiableCollection(accessTokens);
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		byte[] key = serializeKey(CLIENT_ID_TO_ACCESS + clientId);
		List<byte[]> byteList;
		try (RedisConnection conn = getConnection()) {
			byteList = getZByteLists(key, conn);
		}
		if (byteList.size() == 0) {
			return Collections.emptySet();
		}
		List<OAuth2AccessToken> accessTokens = new ArrayList<>(byteList.size());
		for (byte[] bytes : byteList) {
			OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
			accessTokens.add(accessToken);
		}
		return Collections.unmodifiableCollection(accessTokens);
	}

}
