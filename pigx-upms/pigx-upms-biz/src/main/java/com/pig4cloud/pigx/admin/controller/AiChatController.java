package com.pig4cloud.pigx.admin.controller;

import com.pig4cloud.pigx.admin.api.dto.AiChatMessageDTO;
import io.github.pigmesh.ai.deepseek.core.DeepSeekClient;
import io.github.pigmesh.ai.deepseek.core.chat.ChatCompletionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Optional;

/**
 * AI 聊天控制器
 *
 * @author lengleng
 * @date 2025/02/20
 */
@Slf4j
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class AiChatController {

    private final Optional<DeepSeekClient> deepSeekClientOptional;


    /**
     * 聊天
     *
     * @param message 消息
     * @return {@link Flux }<{@link ChatCompletionResponse }>
     */
    @PostMapping("/chat")
    public Flux<ChatCompletionResponse> chat(@RequestBody AiChatMessageDTO message) {

        if (deepSeekClientOptional.isEmpty()) {
            log.warn("DeepSeek 大模型聊天未开启，请检查配置");
            return Flux.empty();
        }

        if (message.isWebSearch()) {
            return deepSeekClientOptional.get().chatSearchCompletion(message.getMessage());
        }
        return deepSeekClientOptional.get().chatFluxCompletion(message.getMessage());
    }
}
