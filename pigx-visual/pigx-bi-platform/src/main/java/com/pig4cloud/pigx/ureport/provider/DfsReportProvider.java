package com.pig4cloud.pigx.ureport.provider;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.bstek.ureport.provider.report.ReportFile;
import com.bstek.ureport.provider.report.ReportProvider;
import com.pig4cloud.pigx.common.file.core.FileProperties;
import com.pig4cloud.pigx.common.file.core.FileTemplate;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2020/7/25
 * <p>
 * 基于OSS 的文件存储支持
 */
@Slf4j
@RequiredArgsConstructor
public class DfsReportProvider implements ReportProvider {

	/**
	 * 保存文件增加默认前缀，方便查找
	 */
	private static final String PREFIX = "oss_";

	private final FileTemplate fileTemplate;

	private final FileProperties properties;

	@Override
	public InputStream loadReport(String file) {
		S3Object object = fileTemplate.getObject(properties.getBucketName(), file);
		return object.getObjectContent();
	}

	@Override
	public void deleteReport(String file) {
		try {
			fileTemplate.removeObject(properties.getBucketName(), file);
		}
		catch (Exception e) {
			log.error("文件删除失败 ", e);
		}
	}

	@Override
	public List<ReportFile> getReportFiles() {
		List<S3ObjectSummary> objectSummaryList = fileTemplate.getAllObjectsByPrefix(properties.getBucketName(), PREFIX,
				true);

		return objectSummaryList.stream()
				.map(s3ObjectSummary -> new ReportFile(s3ObjectSummary.getKey().replaceAll(PREFIX, ""),
						s3ObjectSummary.getLastModified()))
				.collect(Collectors.toList());
	}

	@Override
	public void saveReport(String file, String content) {
		try {
			@Cleanup
			InputStream inputStream = IOUtils.toInputStream(content);
			fileTemplate.putObject(properties.getBucketName(), file, inputStream);
		}
		catch (Exception e) {
			log.error("文件上传失败", e);
		}
	}

	@Override
	public String getName() {
		return "分布式文件存储";
	}

	@Override
	public boolean disabled() {
		return false;
	}

	@Override
	public String getPrefix() {
		return PREFIX;
	}

}
