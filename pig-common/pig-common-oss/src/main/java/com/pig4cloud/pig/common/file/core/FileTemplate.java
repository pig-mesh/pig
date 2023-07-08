package com.pig4cloud.pig.common.file.core;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.InitializingBean;

import java.io.InputStream;
import java.util.List;

/**
 * 文件操作模板
 *
 * @author lengleng
 * @date 2022/4/19
 */
public interface FileTemplate extends InitializingBean {

	/**
	 * 创建bucket
	 * @param bucketName bucket名称
	 */
	void createBucket(String bucketName);

	/**
	 * 获取全部bucket
	 * <p>
	 *
	 * API Documentation</a>
	 */
	List<Bucket> getAllBuckets();

	/**
	 * @param bucketName bucket名称
	 * @see <a href= Documentation</a>
	 */
	void removeBucket(String bucketName);

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @param contextType 文件类型
	 * @throws Exception
	 */
	void putObject(String bucketName, String objectName, InputStream stream, String contextType) throws Exception;

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @param contextType 文件类型
	 * @throws Exception
	 */
	void putObject(String bucketName, String objectName, InputStream stream) throws Exception;

	/**
	 * 获取文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @return 二进制流 API Documentation</a>
	 */
	S3Object getObject(String bucketName, String objectName);

	void removeObject(String bucketName, String objectName) throws Exception;

	/**
	 * @throws Exception
	 */
	@Override
	default void afterPropertiesSet() throws Exception {
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
	List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive);

}
