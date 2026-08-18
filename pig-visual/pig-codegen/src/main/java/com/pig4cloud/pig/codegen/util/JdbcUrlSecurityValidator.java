/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.pig4cloud.pig.codegen.util;

import cn.hutool.core.util.StrUtil;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import com.mysql.cj.conf.ConnectionUrlParser;
import com.mysql.cj.conf.PropertyKey;
import com.pig4cloud.pig.common.core.exception.CheckedException;
import oracle.jdbc.OracleConnection;
import org.postgresql.PGProperty;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * JDBC URL 危险参数校验器。
 *
 * @author lengleng
 */
public final class JdbcUrlSecurityValidator {

	private static final Pattern GENERIC_PARAMETER_SEPARATOR = Pattern.compile("[?&;,():]");

	private static final Pattern QUERY_PARAMETER_SEPARATOR = Pattern.compile("[?&]");

	private static final Set<String> MYSQL_UNSAFE_PARAMETERS = normalizedSet(
			PropertyKey.allowLoadLocalInfile.getKeyName(), PropertyKey.allowLoadLocalInfileInPath.getKeyName(),
			PropertyKey.allowUrlInLocalInfile.getKeyName(), PropertyKey.authenticationPlugins.getKeyName(),
			PropertyKey.authenticationOpenidConnectCallbackHandler.getKeyName(),
			PropertyKey.authenticationWebAuthnCallbackHandler.getKeyName(),
			PropertyKey.clientCertificateKeyStoreUrl.getKeyName(), PropertyKey.defaultAuthenticationPlugin.getKeyName(),
			PropertyKey.connectionLifecycleInterceptors.getKeyName(), PropertyKey.exceptionInterceptors.getKeyName(),
			PropertyKey.queryInterceptors.getKeyName(), PropertyKey.idTokenFile.getKeyName(),
			PropertyKey.ociConfigFile.getKeyName(), PropertyKey.propertiesTransform.getKeyName(),
			PropertyKey.socketFactory.getKeyName(), PropertyKey.logger.getKeyName(),
			PropertyKey.profilerEventHandler.getKeyName(), PropertyKey.queryInfoCacheFactory.getKeyName(),
			PropertyKey.serverConfigCacheFactory.getKeyName(), PropertyKey.serverRSAPublicKeyFile.getKeyName(),
			PropertyKey.trustCertificateKeyStoreUrl.getKeyName(), "autoDeserialize", "statementInterceptors");

	private static final Set<String> POSTGRESQL_UNSAFE_PARAMETERS = normalizedSet(
			PGProperty.AUTHENTICATION_PLUGIN_CLASS_NAME.getName(), PGProperty.SERVICE.getName(),
			PGProperty.SSL_CERT.getName(), PGProperty.SSL_KEY.getName(), PGProperty.SSL_ROOT_CERT.getName(),
			"loggerFile", PGProperty.SOCKET_FACTORY.getName(), PGProperty.SSL_FACTORY.getName(),
			PGProperty.SSL_HOSTNAME_VERIFIER.getName(), PGProperty.SSL_PASSWORD_CALLBACK.getName(),
			PGProperty.XML_FACTORY_FACTORY.getName());

	private static final Set<String> HIGHGO_UNSAFE_PARAMETERS = normalizedSet(
			com.highgo.jdbc.PGProperty.BULKLOAD_CTL_FILE.getName(), com.highgo.jdbc.PGProperty.LOGGER_FILE.getName(),
			com.highgo.jdbc.PGProperty.SSL_CERT.getName(), com.highgo.jdbc.PGProperty.SSL_ENC_CERT.getName(),
			com.highgo.jdbc.PGProperty.SSL_ENC_KEY.getName(), com.highgo.jdbc.PGProperty.SSL_KEY.getName(),
			com.highgo.jdbc.PGProperty.SSL_ROOT_CERT.getName(), com.highgo.jdbc.PGProperty.SOCKET_FACTORY.getName(),
			com.highgo.jdbc.PGProperty.SSL_FACTORY.getName(),
			com.highgo.jdbc.PGProperty.SSL_HOSTNAME_VERIFIER.getName(),
			com.highgo.jdbc.PGProperty.SSL_PASSWORD_CALLBACK.getName(),
			com.highgo.jdbc.PGProperty.XML_FACTORY_FACTORY.getName());

	private static final Set<String> SQL_SERVER_UNSAFE_PARAMETERS = normalizedSet("accessTokenCallbackClass",
			"socketFactoryClass", "trustManagerClass", "clientCertificate", "clientKey", "keyStoreLocation",
			"serverCertificate", "trustStore");

	private static final Set<String> ORACLE_UNSAFE_PARAMETERS = normalizedSet("TNS_ADMIN",
			OracleConnection.CONNECTION_PROPERTY_TNS_ADMIN, OracleConnection.CONNECTION_PROPERTY_WALLET_LOCATION,
			OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_KEYSTORE,
			OracleConnection.CONNECTION_PROPERTY_THIN_JAVAX_NET_SSL_TRUSTSTORE,
			OracleConnection.CONNECTION_PROPERTY_ONS_WALLET_FILE, "MY_WALLET_DIRECTORY");

	private static final Set<String> DB2_UNSAFE_PARAMETERS = normalizedSet("pluginClassName", "sslCertLocation",
			"sslTrustStoreLocation", "sslKeyStoreLocation", "traceFile", "dbPath", "pLoadLoPath");

	private static final Set<String> DM_UNSAFE_PARAMETERS = normalizedSet("cipherPath", "customFilter", "feFilePath",
			"kerberosLoginConfPath", "loginCertificate", "logDir", "sslFilesPath", "statDir", "unixSocketFile");

	private JdbcUrlSecurityValidator() {
	}

	/**
	 * 校验 JDBC URL 中是否包含危险参数。
	 * @param url JDBC URL
	 */
	public static void validate(String url) {
		if (StrUtil.isBlank(url)) {
			return;
		}

		String normalizedUrl = url.toLowerCase(Locale.ROOT);
		try {
			if (normalizedUrl.startsWith("jdbc:mysql")) {
				rejectUnsafeParameters(mysqlParameterNames(url), MYSQL_UNSAFE_PARAMETERS);
			}
			else if (normalizedUrl.startsWith("jdbc:postgresql:")) {
				// PG 的 service 参数会触发驱动读取本地 pg_service.conf，必须在调用驱动解析器前拦截。
				rejectUnsafeParameters(parameterNames(url, QUERY_PARAMETER_SEPARATOR), POSTGRESQL_UNSAFE_PARAMETERS);
				rejectUnsafeParameters(postgresqlParameterNames(url), POSTGRESQL_UNSAFE_PARAMETERS);
			}
			else if (normalizedUrl.startsWith("jdbc:highgo:")) {
				rejectUnsafeParameters(highgoParameterNames(url), HIGHGO_UNSAFE_PARAMETERS);
			}
			else if (normalizedUrl.startsWith("jdbc:sqlserver:")) {
				rejectUnsafeParameters(sqlServerParameterNames(url), SQL_SERVER_UNSAFE_PARAMETERS);
			}
			else if (normalizedUrl.startsWith("jdbc:oracle:")) {
				rejectUnsafeParameters(genericParameterNames(url), ORACLE_UNSAFE_PARAMETERS);
			}
			else if (normalizedUrl.startsWith("jdbc:db2:")) {
				rejectUnsafeParameters(genericParameterNames(url), DB2_UNSAFE_PARAMETERS);
			}
			else if (normalizedUrl.startsWith("jdbc:dm:")) {
				rejectUnsafeParameters(genericParameterNames(url), DM_UNSAFE_PARAMETERS);
			}
		}
		catch (CheckedException e) {
			throw e;
		}
		catch (Exception e) {
			throw new CheckedException("JDBC URL 参数格式不合法");
		}
	}

	private static Collection<String> mysqlParameterNames(String url) {
		ConnectionUrlParser parser = ConnectionUrlParser.parseConnectionString(url);
		return Stream
			.concat(parser.getProperties().keySet().stream(),
					parser.getHosts().stream().flatMap(host -> host.getHostProperties().keySet().stream()))
			.toList();
	}

	private static Collection<String> postgresqlParameterNames(String url) {
		Properties properties = Objects.requireNonNull(org.postgresql.Driver.parseURL(url, new Properties()));
		return properties.stringPropertyNames();
	}

	private static Collection<String> highgoParameterNames(String url) {
		Properties properties = Objects.requireNonNull(com.highgo.jdbc.Driver.parseURL(url, new Properties()));
		return properties.stringPropertyNames();
	}

	private static Collection<String> sqlServerParameterNames(String url) throws Exception {
		return Arrays.stream(new SQLServerDriver().getPropertyInfo(url, new Properties()))
			.filter(property -> StrUtil.isNotBlank(property.value))
			.map(property -> property.name)
			.toList();
	}

	private static Collection<String> genericParameterNames(String url) {
		return parameterNames(url, GENERIC_PARAMETER_SEPARATOR);
	}

	private static Collection<String> parameterNames(String url, Pattern parameterSeparator) {
		Set<String> parameterNames = new HashSet<>();
		for (String segment : parameterSeparator.split(url)) {
			int separatorIndex = segment.indexOf('=');
			if (separatorIndex < 0) {
				continue;
			}
			parameterNames.add(URLDecoder.decode(segment.substring(0, separatorIndex), StandardCharsets.UTF_8).trim());
		}
		return parameterNames;
	}

	private static void rejectUnsafeParameters(Collection<String> parameterNames, Set<String> unsafeParameters) {
		boolean containsUnsafeParameter = parameterNames.stream()
			.map(JdbcUrlSecurityValidator::normalize)
			.anyMatch(unsafeParameters::contains);
		if (containsUnsafeParameter) {
			throw new CheckedException("JDBC URL 包含不安全参数");
		}
	}

	private static Set<String> normalizedSet(String... parameters) {
		return Arrays.stream(parameters)
			.map(JdbcUrlSecurityValidator::normalize)
			.collect(Collectors.toUnmodifiableSet());
	}

	private static String normalize(String parameter) {
		return parameter.trim().toLowerCase(Locale.ROOT);
	}

}
