package com.pig4cloud.pigx.mp.controller;

import cn.hutool.core.util.StrUtil;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import com.pig4cloud.pigx.mp.config.WxMpInitConfigRunner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 微信JSSDK配置
 *
 * @author akhan
 * @date 13:59 2025/4/1
 */
@Slf4j
@Inner(value = false)
@RestController
@AllArgsConstructor
@RequestMapping("/{appId}/jssdk-config")
public class WxJssdkController {

    @GetMapping
    public R getJssdkConfig(@PathVariable("appId") String appId,
                            @RequestParam(name = "url", required = false) String url){
        if (StrUtil.isAllBlank(url)) {
            throw new IllegalArgumentException("请求参数非法，请核实!");
        }

        final WxMpService wxService = WxMpInitConfigRunner.getMpServices().get(appId);

        if (wxService == null) {
            throw new IllegalArgumentException(String.format("未找到对应appid=[%d]的配置，请核实！", appId));
        }

        try {
            WxJsapiSignature signature = wxService.createJsapiSignature(url);

            return R.ok(Map.of(
                    "appId", signature.getAppId(),
                    "timestamp", signature.getTimestamp(),
                    "nonceStr", signature.getNonceStr(),
                    "signature", signature.getSignature()
            ));
        } catch (WxErrorException e) {
            log.error("获取微信js api出现错误: {}", e.getLocalizedMessage());
            return R.failed("获取微信js api出现错误: " + e.getLocalizedMessage());
        }
    }
}
