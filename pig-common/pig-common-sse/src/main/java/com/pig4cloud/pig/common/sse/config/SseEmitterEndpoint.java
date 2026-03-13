package com.pig4cloud.pig.common.sse.config;

import com.pig4cloud.pig.common.security.util.SecurityUtils;
import com.pig4cloud.pig.common.sse.holder.SseEmitterHolder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Objects;

/**
 * SSE端点控制器：提供服务器发送事件(SSE)功能
 *
 * @author lengleng
 * @date 2025/06/06
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseEmitterEndpoint {

    /**
     * 创建并返回一个SSE事件发射器
     *
     * @return SSE事件发射器，用于服务器推送事件，若用户未认证则返回null
     * @throws Exception 若发送初始消息失败时抛出异常
     */
    @SneakyThrows
    @RequestMapping("/info")
    public SseEmitter info() {
        SseEmitter sseEmitter = new SseEmitter(0L);

        if (Objects.isNull(SecurityUtils.getUser())) {
            log.debug("MSG: SseConnectError");
            return null;
        }

        Long userId = SecurityUtils.getUser().getId();
        log.info("MSG: SseConnect | EmitterHash: {} | ID: {}", sseEmitter.hashCode(), SecurityUtils.getUser().getId());
        sseEmitter.onCompletion(() -> {
            log.info("MSG: SseConnectCompletion | EmitterHash: {} |ID: {}", sseEmitter.hashCode(),
                    SecurityUtils.getUser().getId());
            SseEmitterHolder.removeSseEmitter(sseEmitter);
        });
        sseEmitter.onTimeout(() -> {
            log.error("MSG: SseConnectTimeout | EmitterHash: {} |ID: {} ", sseEmitter.hashCode(),
                    SecurityUtils.getUser().getId());
            SseEmitterHolder.removeSseEmitter(sseEmitter);
        });
        sseEmitter.onError(t -> {
            log.error("MSG: SseConnectError | EmitterHash: {} |ID: {}", sseEmitter.hashCode(),
                    SecurityUtils.getUser().getId());
            SseEmitterHolder.removeSseEmitter(sseEmitter);
        });
        SseEmitterHolder.addSseEmitter(userId, sseEmitter);
        sseEmitter.send("pong");
        return sseEmitter;
    }

}
