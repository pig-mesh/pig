package com.pig4cloud.pig.common.file.local;

import cn.hutool.core.io.FileUtil;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.pig4cloud.pig.common.file.core.FileProperties;
import com.pig4cloud.pig.common.file.core.FileTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 本地文件读取模式
 *
 * @author lengleng
 * @date 2022/4/19
 */
@RequiredArgsConstructor
public class LocalFileTemplate implements FileTemplate {

	private final FileProperties properties;

	/**
	 * 创建bucket
	 * @param bucketName bucket名称
	 */
	@Override
	public void createBucket(String bucketName) {
		FileUtil.mkdir(properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName);
	}

	/**
	 * 获取全部bucket
	 * <p>
	 * <p>
	 * API Documentation</a>
	 */
	@Override
	public List<Bucket> getAllBuckets() {
		return Arrays.stream(FileUtil.ls(properties.getLocal().getBasePath()))
			.filter(FileUtil::isDirectory)
			.map(dir -> new Bucket(dir.getName()))
			.collect(Collectors.toList());
	}

	/**
	 * @param bucketName bucket名称
	 * @see <a href= Documentation</a>
	 */
	@Override
	public void removeBucket(String bucketName) {
		FileUtil.del(properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName);
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @param contextType 文件类型
	 */
	@Override
	public void putObject(String bucketName, String objectName, InputStream stream, String contextType) {
		// 当 Bucket 不存在时创建
		String dir = properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName;
		if (!FileUtil.isDirectory(properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName)) {
			createBucket(bucketName);
		}

		// 写入文件
		File file = FileUtil.file(dir + FileUtil.FILE_SEPARATOR + objectName);
		FileUtil.writeFromStream(stream, file);
	}

	/**
	 * 获取文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @return 二进制流 API Documentation</a>
	 */
	@Override
	@SneakyThrows
	public S3Object getObject(String bucketName, String objectName) {
		String dir = properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName;
		S3Object s3Object = new S3Object();
		s3Object.setObjectContent(FileUtil.getInputStream(dir + FileUtil.FILE_SEPARATOR + objectName));
		return s3Object;
	}

	/**
	 * @param bucketName
	 * @param objectName
	 * @throws Exception
	 */
	@Override
	public void removeObject(String bucketName, String objectName) throws Exception {
		String dir = properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName;
		FileUtil.del(dir + FileUtil.FILE_SEPARATOR + objectName);
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
		putObject(bucketName, objectName, stream, null);
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
	@Override
	public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
		String dir = properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName;

		return Arrays.stream(FileUtil.ls(dir)).filter(file -> file.getName().startsWith(prefix)).map(file -> {
			S3ObjectSummary summary = new S3ObjectSummary();
			summary.setKey(file.getName());
			return new S3ObjectSummary();
		}).collect(Collectors.toList());
	}

}
