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

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.file.core.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.checksums.RequestChecksumCalculation;
import software.amazon.awssdk.core.checksums.ResponseChecksumValidation;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

/**
 * S3 通用存储操作，支持所有兼容 S3 协议的云存储。
 *
 * @author lengleng
 * @author 858695266
 * @date 2020/5/23 6:36 上午
 * @since 1.0
 */
@RequiredArgsConstructor
public class OssTemplate implements InitializingBean, DisposableBean, FileTemplate {

	private static final String DEFAULT_REGION = "us-east-1";

	private final FileProperties properties;

	private S3Client s3Client;

	private S3Presigner s3Presigner;

	/**
	 * 创建bucket
	 * @param bucketName bucket名称
	 */
	@Override
	public void createBucket(String bucketName) {
		if (!bucketExists(bucketName)) {
			s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
		}
	}

	/**
	 * 获取全部bucket
	 * <p>
	 *
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
	 * API Documentation</a>
	 */
	@Override
	public List<FileBucket> getAllBuckets() {
		return s3Client.listBuckets().buckets().stream().map(this::toFileBucket).toList();
	}

	/**
	 * @param bucketName bucket名称
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
	 * API Documentation</a>
	 */
	public Optional<FileBucket> getBucket(String bucketName) {
		return getAllBuckets().stream().filter(b -> b.getName().equals(bucketName)).findFirst();
	}

	/**
	 * @param bucketName bucket名称
	 * @see <a href=
	 * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteBucket">AWS API
	 * Documentation</a>
	 */
	@Override
	public void removeBucket(String bucketName) {
		s3Client.deleteBucket(DeleteBucketRequest.builder().bucket(bucketName).build());
	}

	/**
	 * 根据文件前置查询文件
	 * @param bucketName bucket名称
	 * @param prefix 前缀
	 * @param recursive 是否递归查询
	 * @return FileObjectSummary 列表
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListObjects">AWS
	 * API Documentation</a>
	 */
	@Override
	public List<FileObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
		ListObjectsV2Request.Builder builder = ListObjectsV2Request.builder().bucket(bucketName).prefix(prefix);
		if (!recursive) {
			builder.delimiter(StrUtil.SLASH);
		}
		ListObjectsV2Response response = s3Client.listObjectsV2(builder.build());
		return response.contents().stream().map(object -> toFileObjectSummary(bucketName, object)).toList();
	}

	/**
	 * 获取文件外链
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param expires 过期时间 <=7
	 * @return url
	 */
	public String getObjectURL(String bucketName, String objectName, Integer expires) {
		GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(bucketName).key(objectName).build();
		GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
			.signatureDuration(Duration.ofDays(expires))
			.getObjectRequest(getObjectRequest)
			.build();
		PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
		URL url = presignedGetObjectRequest.url();
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
	@Override
	public FileObject getObject(String bucketName, String objectName) {
		GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName).key(objectName).build();
		ResponseInputStream<GetObjectResponse> stream = s3Client.getObject(request,
				ResponseTransformer.toInputStream());
		GetObjectResponse response = stream.response();
		return new FileObject(bucketName, objectName, response.contentType(), response.contentLength(), stream);
	}

	/**
	 * 获取文件
	 * @param bucketName bucket名称
	 * @param dir 文件夹名称
	 * @param objectName 文件名称
	 * @return 二进制流 API Documentation</a>
	 */
	@Override
	public FileObject getObject(String bucketName, String dir, String objectName) {
		if (StrUtil.isNotBlank(dir)) {
			objectName = dir + StrUtil.SLASH + objectName;
		}
		return getObject(bucketName, objectName);
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @throws Exception
	 */
	@Override
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
	@Override
	public void putObject(String bucketName, String objectName, InputStream stream, String contextType)
			throws Exception {
		putObject(bucketName, objectName, stream, stream.available(), contextType);
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param dir 文件夹名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @param contextType 文件类型
	 * @throws Exception
	 */
	@Override
	public void putObject(String bucketName, String dir, String objectName, InputStream stream, String contextType)
			throws Exception {
		if (StrUtil.isNotBlank(dir)) {
			objectName = dir + StrUtil.SLASH + objectName;
		}
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
	public FileObjectInfo putObject(String bucketName, String objectName, InputStream stream, long size,
			String contextType) throws Exception {
		PutObjectRequest.Builder request = PutObjectRequest.builder().bucket(bucketName).key(objectName);
		if (StrUtil.isNotBlank(contextType)) {
			request.contentType(contextType);
		}
		Path tempFile = Files.createTempFile("pig-oss-", ".tmp");
		try {
			Files.copy(stream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			long contentLength = Files.size(tempFile);
			request.contentLength(contentLength);
			PutObjectResponse response = s3Client.putObject(request.build(), RequestBody.fromFile(tempFile));
			return new FileObjectInfo(bucketName, objectName, contextType, contentLength, response.eTag(), null);
		}
		finally {
			deleteTempFile(tempFile);
		}
	}

	/**
	 * 获取文件信息
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
	 * API Documentation</a>
	 */
	public FileObjectInfo getObjectInfo(String bucketName, String objectName) {
		HeadObjectResponse response = s3Client
			.headObject(HeadObjectRequest.builder().bucket(bucketName).key(objectName).build());
		return new FileObjectInfo(bucketName, objectName, response.contentType(), response.contentLength(),
				response.eTag(), response.lastModified());
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
	@Override
	public void removeObject(String bucketName, String objectName) throws Exception {
		s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(objectName).build());
	}

	@Override
	public void afterPropertiesSet() {
		Region region = Region.of(StrUtil.blankToDefault(properties.getOss().getRegion(), DEFAULT_REGION));
		AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider
			.create(AwsBasicCredentials.create(properties.getOss().getAccessKey(), properties.getOss().getSecretKey()));
		S3Configuration serviceConfiguration = S3Configuration.builder()
			.pathStyleAccessEnabled(properties.getOss().getPathStyleAccess())
			.chunkedEncodingEnabled(properties.getOss().getChunkedEncodingEnabled())
			.build();
		RequestChecksumCalculation requestChecksumCalculation = properties.getOss().isSkipMd5Check()
				? RequestChecksumCalculation.WHEN_REQUIRED : RequestChecksumCalculation.WHEN_SUPPORTED;
		ResponseChecksumValidation responseChecksumValidation = properties.getOss().isSkipMd5Check()
				? ResponseChecksumValidation.WHEN_REQUIRED : ResponseChecksumValidation.WHEN_SUPPORTED;
		S3ClientBuilder clientBuilder = S3Client.builder()
			.region(region)
			.credentialsProvider(credentialsProvider)
			.serviceConfiguration(serviceConfiguration)
			.requestChecksumCalculation(requestChecksumCalculation)
			.responseChecksumValidation(responseChecksumValidation);
		S3Presigner.Builder presignerBuilder = S3Presigner.builder()
			.region(region)
			.credentialsProvider(credentialsProvider)
			.serviceConfiguration(serviceConfiguration);
		if (StrUtil.isNotBlank(properties.getOss().getEndpoint())) {
			URI endpoint = URI.create(properties.getOss().getEndpoint());
			clientBuilder.endpointOverride(endpoint);
			presignerBuilder.endpointOverride(endpoint);
		}
		this.s3Client = clientBuilder.build();
		this.s3Presigner = presignerBuilder.build();
	}

	@Override
	public void destroy() {
		if (s3Presigner != null) {
			s3Presigner.close();
		}
		if (s3Client != null) {
			s3Client.close();
		}
	}

	private boolean bucketExists(String bucketName) {
		try {
			s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
			return true;
		}
		catch (NoSuchBucketException ex) {
			return false;
		}
		catch (S3Exception ex) {
			if (ex.statusCode() == 404) {
				return false;
			}
			throw ex;
		}
	}

	private FileBucket toFileBucket(Bucket bucket) {
		return new FileBucket(bucket.name(), bucket.creationDate());
	}

	private FileObjectSummary toFileObjectSummary(String bucketName, S3Object object) {
		return new FileObjectSummary(bucketName, object.key(), object.size(), object.lastModified());
	}

	private void deleteTempFile(Path tempFile) throws IOException {
		Files.deleteIfExists(tempFile);
	}

}
