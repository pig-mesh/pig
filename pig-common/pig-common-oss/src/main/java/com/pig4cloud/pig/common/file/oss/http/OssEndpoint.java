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

package com.pig4cloud.pig.common.file.oss.http;

import com.pig4cloud.pig.common.file.oss.service.OssTemplate;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.Bucket;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AWS 对象存储服务端点
 *
 * @author lengleng
 * @author 858695266
 * @date 2025/05/31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/oss")
@ConditionalOnProperty(name = "file.oss.info", havingValue = "true")
public class OssEndpoint {

	private final OssTemplate template;

	/**
	 * 创建指定名称的存储桶
	 * @param bucketName 存储桶名称
	 * @return 创建的存储桶对象
	 * @throws Exception 创建过程中可能抛出的异常
	 */
	@SneakyThrows
	@PostMapping("/bucket/{bucketName}")
	public Bucket createBucket(@PathVariable String bucketName) {

		template.createBucket(bucketName);
		return template.getBucket(bucketName).get();

	}

	/**
	 * 获取所有存储桶列表
	 * @return 存储桶列表
	 * @throws Exception 获取过程中可能抛出的异常
	 */
	@SneakyThrows
	@GetMapping("/bucket")
	public List<Bucket> getBuckets() {
		return template.getAllBuckets();
	}

	/**
	 * 根据桶名称获取桶信息
	 * @param bucketName 桶名称
	 * @return 对应的桶对象
	 * @throws IllegalArgumentException 当桶不存在时抛出异常
	 */
	@SneakyThrows
	@GetMapping("/bucket/{bucketName}")
	public Bucket getBucket(@PathVariable String bucketName) {
		return template.getBucket(bucketName).orElseThrow(() -> new IllegalArgumentException("Bucket Name not found!"));
	}

	/**
	 * 删除指定名称的存储桶
	 * @param bucketName 要删除的存储桶名称
	 * @throws Exception 删除过程中可能抛出的异常
	 */
	@SneakyThrows
	@DeleteMapping("/bucket/{bucketName}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteBucket(@PathVariable String bucketName) {
		template.removeBucket(bucketName);
	}

	/**
	 * 创建对象到指定存储桶
	 * @param object 要上传的文件对象
	 * @param bucketName 目标存储桶名称
	 * @return 上传后的对象信息响应
	 * @throws IOException 文件操作异常
	 */
	@SneakyThrows
	@PostMapping("/object/{bucketName}")
	public Map<String, Object> createObject(@RequestBody MultipartFile object, @PathVariable String bucketName) {
		String name = object.getOriginalFilename();
		@Cleanup
		InputStream inputStream = object.getInputStream();
		template.putObject(bucketName, name, inputStream, object.getSize(), object.getContentType());

		Map<String, Object> result = new HashMap<>();
		result.put("bucket", bucketName);
		result.put("object", name);
		result.put("size", object.getSize());
		result.put("contentType", object.getContentType());
		return result;
	}

	/**
	 * 创建对象到指定存储桶
	 * @param object 上传的文件对象
	 * @param bucketName 存储桶名称
	 * @param objectName 对象名称
	 * @return 创建成功的对象信息
	 * @throws Exception 当文件上传或获取对象信息失败时抛出异常
	 */
	@SneakyThrows
	@PostMapping("/object/{bucketName}/{objectName}")
	public Map<String, Object> createObject(@RequestBody MultipartFile object, @PathVariable String bucketName,
			@PathVariable String objectName) {
		@Cleanup
		InputStream inputStream = object.getInputStream();
		template.putObject(bucketName, objectName, inputStream, object.getSize(), object.getContentType());

		Map<String, Object> result = new HashMap<>();
		result.put("bucket", bucketName);
		result.put("object", objectName);
		result.put("size", object.getSize());
		result.put("contentType", object.getContentType());
		return result;
	}

	/**
	 * 根据对象名前缀过滤对象列表
	 * @param bucketName 存储桶名称
	 * @param objectName 对象名前缀
	 * @return 匹配前缀的S3对象列表
	 * @throws Exception 操作执行过程中可能抛出的异常
	 */
	@SneakyThrows
	@GetMapping("/object/{bucketName}/{objectName}")
	public List<S3Object> filterObject(@PathVariable String bucketName, @PathVariable String objectName) {

		return template.getAllObjectsByPrefix(bucketName, objectName, true);

	}

	/**
	 * 获取对象信息及访问URL
	 * @param bucketName 存储桶名称
	 * @param objectName 对象名称
	 * @param expires URL过期时间(秒)
	 * @return 包含存储桶、对象、URL和过期时间的Map
	 */
	@SneakyThrows
	@GetMapping("/object/{bucketName}/{objectName}/{expires}")
	public Map<String, Object> getObject(@PathVariable String bucketName, @PathVariable String objectName,
			@PathVariable Integer expires) {
		Map<String, Object> responseBody = new HashMap<>(8);
		// Put Object info
		responseBody.put("bucket", bucketName);
		responseBody.put("object", objectName);
		responseBody.put("url", template.getObjectURL(bucketName, objectName, expires));
		responseBody.put("expires", expires);
		return responseBody;
	}

	/**
	 * 删除指定存储桶中的对象
	 * @param bucketName 存储桶名称
	 * @param objectName 对象名称
	 * @throws Exception 删除对象时可能抛出的异常
	 */
	@SneakyThrows
	@ResponseStatus(HttpStatus.ACCEPTED)
	@DeleteMapping("/object/{bucketName}/{objectName}/")
	public void deleteObject(@PathVariable String bucketName, @PathVariable String objectName) {

		template.removeObject(bucketName, objectName);
	}

}
