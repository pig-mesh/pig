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

package com.pig4cloud.pig.common.file.oss.service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.pig4cloud.pig.common.file.core.FileProperties;
import com.pig4cloud.pig.common.file.core.FileTemplate;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * aws-s3 通用存储操作 支持所有兼容s3协议的云存储: {阿里云OSS，腾讯云COS，七牛云，京东云，minio 等}
 *
 * @author lengleng
 * @author 858695266
 * @date 2020/5/23 6:36 上午
 * @since 1.0
 */
@RequiredArgsConstructor
public class OssTemplate implements InitializingBean, FileTemplate {

	private final FileProperties properties;

	private AmazonS3 amazonS3;

	/**
	 * 创建bucket
	 * @param bucketName bucket名称
	 */
	@SneakyThrows
	public void createBucket(String bucketName) {
		if (!amazonS3.doesBucketExistV2(bucketName)) {
			amazonS3.createBucket((bucketName));
		}
	}

	/**
	 * 获取全部bucket
	 * <p>
	 *
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
	 * API Documentation</a>
	 */
	@SneakyThrows
	public List<Bucket> getAllBuckets() {
		return amazonS3.listBuckets();
	}

	/**
	 * @param bucketName bucket名称
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
	 * API Documentation</a>
	 */
	@SneakyThrows
	public Optional<Bucket> getBucket(String bucketName) {
		return amazonS3.listBuckets().stream().filter(b -> b.getName().equals(bucketName)).findFirst();
	}

	/**
	 * @param bucketName bucket名称
	 * @see <a href=
	 * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteBucket">AWS API
	 * Documentation</a>
	 */
	@SneakyThrows
	public void removeBucket(String bucketName) {
		amazonS3.deleteBucket(bucketName);
	}

	/**
	 * 根据文件前置查询文件
	 * @param bucketName bucket名称
	 * @param prefix 前缀
	 * @param recursive 是否递归查询
	 * @return S3ObjectSummary 列表
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListObjects">AWS
	 * API Documentation</a>
	 */
	@SneakyThrows
	public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
		ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
		return new ArrayList<>(objectListing.getObjectSummaries());
	}

	/**
	 * 获取文件外链
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param expires 过期时间 <=7
	 * @return url
	 * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
	 */
	@SneakyThrows
	public String getObjectURL(String bucketName, String objectName, Integer expires) {
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, expires);
		URL url = amazonS3.generatePresignedUrl(bucketName, objectName, calendar.getTime());
		return url.toString();
	}

	/**
	 * 获取文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @return 二进制流
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
	 * API Documentation</a>
	 */
	@SneakyThrows
	public S3Object getObject(String bucketName, String objectName) {
		return amazonS3.getObject(bucketName, objectName);
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @throws Exception
	 */
	public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
		putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @param contextType 文件类型
	 * @throws Exception
	 */
	public void putObject(String bucketName, String objectName, InputStream stream, String contextType)
			throws Exception {
		putObject(bucketName, objectName, stream, stream.available(), contextType);
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @param size 大小
	 * @param contextType 类型
	 * @throws Exception
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/PutObject">AWS
	 * API Documentation</a>
	 */
	public PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size,
			String contextType) throws Exception {
		// String fileName = getFileName(objectName);
		byte[] bytes = IOUtils.toByteArray(stream);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(size);
		objectMetadata.setContentType(contextType);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		// 上传
		return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);

	}

	/**
	 * 获取文件信息
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
	 * API Documentation</a>
	 */
	public S3Object getObjectInfo(String bucketName, String objectName) throws Exception {
		@Cleanup
		S3Object object = amazonS3.getObject(bucketName, objectName);
		return object;
	}

	/**
	 * 删除文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @throws Exception
	 * @see <a href=
	 * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteObject">AWS API
	 * Documentation</a>
	 */
	public void removeObject(String bucketName, String objectName) throws Exception {
		amazonS3.deleteObject(bucketName, objectName);
	}

	@Override
	public void afterPropertiesSet() {
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setMaxConnections(properties.getOss().getMaxConnections());

		AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
				properties.getOss().getEndpoint(), properties.getOss().getRegion());
		AWSCredentials awsCredentials = new BasicAWSCredentials(properties.getOss().getAccessKey(),
				properties.getOss().getSecretKey());
		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
		this.amazonS3 = AmazonS3Client.builder()
			.withEndpointConfiguration(endpointConfiguration)
			.withClientConfiguration(clientConfiguration)
			.withCredentials(awsCredentialsProvider)
			.disableChunkedEncoding()
			.withPathStyleAccessEnabled(properties.getOss().getPathStyleAccess())
			.build();
	}

}
