package com.pig4cloud.pig.admin.api.feign;

import com.pig4cloud.pig.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.feign.annotation.NoToken;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件读取
 *
 * @author lengleng
 * @date 2024/3/14
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteFileService {

	/**
	 * 获取文件的方法
	 *
	 * @param fileName 文件的名称
	 * @return 响应对象，包含文件内容
	 */
	@GetMapping("/sys-file/oss/file")
	Response getFile(@RequestParam String fileName);

	/**
	 * 上传文件
	 *
	 * @param file 资源
	 * @return R(/ admin / bucketName / filename)
	 */
	@PostMapping(value = "/sys-file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	R upload(@RequestPart("file") MultipartFile file);

	/**
	 * 内部上传文件接口
	 *
	 * @param file     要上传的文件
	 * @param fileName 文件名
	 * @return 上传结果
	 */
	@NoToken
	@PostMapping(value = "/sys-file/inner/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	R innerUpload(@RequestPart("file") MultipartFile file, @RequestParam String fileName);

}
