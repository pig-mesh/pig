package com.pig4cloud.pigx.admin.api.feign;

import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 文件读取
 *
 * @author lengleng
 * @date 2024/3/14
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteFileService {

    @GetMapping("/sys-file/oss/file")
    Response getFile(@RequestParam String fileName);
}
