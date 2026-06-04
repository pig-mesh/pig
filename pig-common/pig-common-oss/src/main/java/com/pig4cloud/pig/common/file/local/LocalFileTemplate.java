package com.pig4cloud.pig.common.file.local;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pig.common.file.core.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

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
	public List<FileBucket> getAllBuckets() {
		return Arrays.stream(FileUtil.ls(properties.getLocal().getBasePath()))
			.filter(FileUtil::isDirectory)
			.map(dir -> new FileBucket(dir.getName()))
			.toList();
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
			bucketName = bucketName + FileUtil.FILE_SEPARATOR + dir;
		}
		putObject(bucketName, objectName, stream, contextType);
	}

	/**
	 * 获取文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @return 二进制流 API Documentation</a>
	 */
	@Override
	@SneakyThrows
	public FileObject getObject(String bucketName, String objectName) {
		String dir = properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName;
		File file = FileUtil.file(dir + FileUtil.FILE_SEPARATOR + objectName);
		return new FileObject(bucketName, objectName, Files.probeContentType(file.toPath()), file.length(),
				FileUtil.getInputStream(file));
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
			bucketName = bucketName + FileUtil.FILE_SEPARATOR + dir;
		}
		return getObject(bucketName, objectName);
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
	 * @return FileObjectSummary 列表
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListObjects">AWS
	 * API Documentation</a>
	 */
	@Override
	public List<FileObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
		String dir = properties.getLocal().getBasePath() + FileUtil.FILE_SEPARATOR + bucketName;

		return Arrays.stream(FileUtil.ls(dir))
			.filter(file -> file.getName().startsWith(prefix))
			.map(file -> new FileObjectSummary(bucketName, file.getName(), file.length(), null))
			.toList();
	}

}
