package com.pig4cloud.pig.admin.api.feign;

import com.pig4cloud.pig.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.feign.annotation.NoToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author lengleng
 * @date 2020/5/12
 * <p>
 * 查询参数相关
 */
@FeignClient(contextId = "remoteParamService", value = ServiceNameConstants.UMPS_SERVICE)
public interface RemoteParamService {

    /**
     * 通过key 查询参数配置
     *
     * @param key key
     * @NoToken 声明成内部调用，避免MQ 等无法调用
     */
    @NoToken
    @GetMapping("/param/publicValue/{key}")
    R<String> getByKey(@PathVariable("key") String key);

}
