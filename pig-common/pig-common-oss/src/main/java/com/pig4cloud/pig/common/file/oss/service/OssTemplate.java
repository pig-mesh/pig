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

import com.pig4cloud.pig.common.file.core.FileProperties;
import com.pig4cloud.pig.common.file.core.FileTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.InitializingBean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.InputStream;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * AWS S3通用存储操作模板类
 *
 * <p>
 * 支持所有兼容S3协议的云存储服务，包括AWS S3、MinIO、阿里云OSS、腾讯云COS等
 * </p>
 * <p>
 * 提供存储桶管理、文件对象管理、预签名URL生成等功能
 * </p>
 *
 * @author lengleng
 * @author 858695266
 * @date 2025/05/31
 * @since 1.0
 */
@RequiredArgsConstructor
public class OssTemplate implements InitializingBean, FileTemplate {

	/**
	 * 文件存储配置属性
	 */
	private final FileProperties properties;

	/**
	 * S3客户端实例，用于执行S3 API操作
	 */
	private S3Client s3Client;

	/**
	 * S3预签名器，用于生成预签名URL
	 */
	private S3Presigner s3Presigner;

	/**
	 * 创建存储桶
	 * @param bucketName 存储桶名称，必须全局唯一且符合DNS命名规范
	 * @throws Exception 创建失败时抛出异常
	 */
	@SneakyThrows
	public void createBucket(String bucketName) {
		// 检查存储桶是否已存在，避免重复创建
		if (!doesBucketExist(bucketName)) {
			CreateBucketRequest request = CreateBucketRequest.builder().bucket(bucketName).build();
			s3Client.createBucket(request);
		}
	}

	/**
	 * 检查存储桶是否存在
	 * @param bucketName 存储桶名称
	 * @return 存在返回true，否则返回false
	 */
	private boolean doesBucketExist(String bucketName) {
		try {
			HeadBucketRequest request = HeadBucketRequest.builder().bucket(bucketName).build();
			s3Client.headBucket(request);
			return true;
		}
		catch (NoSuchBucketException e) {
			return false;
		}
	}

	/**
	 * 获取所有存储桶列表
	 * @return 存储桶列表
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
	 * API Documentation</a>
	 */
	@SneakyThrows
	public List<Bucket> getAllBuckets() {
		ListBucketsResponse response = s3Client.listBuckets();
		return response.buckets();
	}

	/**
	 * 根据名称查找特定存储桶
	 * @param bucketName 存储桶名称
	 * @return Optional包装的Bucket对象
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
	 * API Documentation</a>
	 */
	@SneakyThrows
	public Optional<Bucket> getBucket(String bucketName) {
		return getAllBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
	}

	/**
	 * 删除存储桶
	 *
	 * <p>
	 * 注意：存储桶必须为空才能删除，删除操作不可逆
	 * </p>
	 * @param bucketName 存储桶名称
	 * @throws Exception 删除失败时抛出异常
	 * @see <a href=
	 * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteBucket">AWS API
	 * Documentation</a>
	 */
	@SneakyThrows
	public void removeBucket(String bucketName) {
		DeleteBucketRequest request = DeleteBucketRequest.builder().bucket(bucketName).build();
		s3Client.deleteBucket(request);
	}

	/**
	 * 根据前缀查询文件对象
	 * @param bucketName 存储桶名称
	 * @param prefix 文件名前缀，可为null或空字符串
	 * @param recursive 是否递归查询子目录
	 * @return S3Object列表
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListObjects">AWS
	 * API Documentation</a>
	 */
	@SneakyThrows
	public List<S3Object> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
		ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request.builder().bucket(bucketName);

		// 设置前缀过滤条件
		if (prefix != null && !prefix.isEmpty()) {
			requestBuilder.prefix(prefix);
		}

		// 非递归查询时设置分隔符
		if (!recursive) {
			requestBuilder.delimiter("/");
		}

		ListObjectsV2Response response = s3Client.listObjectsV2(requestBuilder.build());
		return response.contents();
	}

	/**
	 * 生成文件的预签名访问URL
	 * @param bucketName 存储桶名称
	 * @param objectName 文件对象名称
	 * @param expires 过期时间（天数）
	 * @return 预签名的访问URL
	 * @throws Exception 生成失败时抛出异常
	 */
	@SneakyThrows
	public String getObjectURL(String bucketName, String objectName, Integer expires) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(objectName).build();

		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
			.signatureDuration(Duration.ofDays(expires))
			.getObjectRequest(getObjectRequest)
			.build();

		return s3Presigner.presignGetObject(presignRequest).url().toString();
	}

	/**
	 * 获取文件对象
	 * @param bucketName 存储桶名称
	 * @param objectName 文件对象名称
	 * @return S3响应对象，包含文件流和元数据
	 * @throws Exception 获取失败时抛出异常
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
	 * API Documentation</a>
	 */
	@SneakyThrows
	public Object getObject(String bucketName, String objectName) {
		GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName).key(objectName).build();
		return s3Client.getObject(request);
	}

	/**
	 * 上传文件（使用默认内容类型）
	 * @param bucketName 存储桶名称
	 * @param objectName 文件对象名称
	 * @param stream 文件输入流
	 * @throws Exception 上传失败时抛出异常
	 */
	public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
		putObject(bucketName, objectName, stream, "application/octet-stream");
	}

	/**
	 * 上传文件（指定内容类型）
	 * @param bucketName 存储桶名称
	 * @param objectName 文件对象名称
	 * @param stream 文件输入流
	 * @param contextType 文件MIME类型
	 * @throws Exception 上传失败时抛出异常
	 */
	public void putObject(String bucketName, String objectName, InputStream stream, String contextType)
			throws Exception {
		PutObjectRequest request = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(objectName)
			.contentType(contextType)
			.build();

		s3Client.putObject(request, RequestBody.fromInputStream(stream, stream.available()));
	}

	/**
	 * 上传文件（指定文件大小）
	 * @param bucketName 存储桶名称
	 * @param objectName 文件对象名称
	 * @param stream 文件输入流
	 * @param size 文件大小（字节数）
	 * @param contextType 文件MIME类型
	 * @return PutObjectResponse 上传响应对象
	 * @throws Exception 上传失败时抛出异常
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/PutObject">AWS
	 * API Documentation</a>
	 */
	public PutObjectResponse putObject(String bucketName, String objectName, InputStream stream, long size,
			String contextType) throws Exception {
		PutObjectRequest request = PutObjectRequest.builder()
			.bucket(bucketName)
			.key(objectName)
			.contentType(contextType)
			.contentLength(size)
			.build();

		return s3Client.putObject(request, RequestBody.fromInputStream(stream, size));
	}

	/**
	 * 获取文件元数据信息
	 * @param bucketName 存储桶名称
	 * @param objectName 文件对象名称
	 * @return HeadObjectResponse 文件元数据响应对象
	 * @throws Exception 获取失败时抛出异常
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
	 * API Documentation</a>
	 */
	public HeadObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
		HeadObjectRequest request = HeadObjectRequest.builder().bucket(bucketName).key(objectName).build();
		return s3Client.headObject(request);
	}

	/**
	 * 删除文件对象
	 *
	 * <p>
	 * 注意：删除操作不可逆，删除不存在的文件不会报错
	 * </p>
	 * @param bucketName 存储桶名称
	 * @param objectName 文件对象名称
	 * @throws Exception 删除失败时抛出异常
	 * @see <a href=
	 * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteObject">AWS API
	 * Documentation</a>
	 */
	public void removeObject(String bucketName, String objectName) throws Exception {
		DeleteObjectRequest request = DeleteObjectRequest.builder().bucket(bucketName).key(objectName).build();
		s3Client.deleteObject(request);
	}

	/**
	 * 初始化S3客户端和预签名器实例
	 *
	 * <p>
	 * 在Spring Bean属性设置完成后自动调用，配置端点地址、区域、访问凭证等
	 * </p>
	 * @throws Exception 初始化失败时抛出异常
	 */
	@Override
	public void afterPropertiesSet() {
		// 创建认证凭据
		AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(properties.getOss().getAccessKey(),
				properties.getOss().getSecretKey());

		// 构建S3配置
		S3Configuration.Builder s3ConfigBuilder = S3Configuration.builder()
			.pathStyleAccessEnabled(properties.getOss().getPathStyleAccess());

		// 创建S3客户端
		this.s3Client = S3Client.builder()
			.endpointOverride(URI.create(properties.getOss().getEndpoint()))
			.region(Region.of(properties.getOss().getRegion() != null ? properties.getOss().getRegion() : "us-east-1"))
			.credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
			.serviceConfiguration(s3ConfigBuilder.build())
			.build();

		// 创建S3预签名器
		this.s3Presigner = S3Presigner.builder()
			.endpointOverride(URI.create(properties.getOss().getEndpoint()))
			.region(Region.of(properties.getOss().getRegion() != null ? properties.getOss().getRegion() : "us-east-1"))
			.credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
			.serviceConfiguration(s3ConfigBuilder.build())
			.build();
	}

}
