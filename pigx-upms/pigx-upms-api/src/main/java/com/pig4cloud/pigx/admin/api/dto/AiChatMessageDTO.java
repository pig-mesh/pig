package com.pig4cloud.pigx.admin.api.dto;

import lombok.Data;

/**
 * AI 聊天消息 DTO
 *
 * @author lengleng
 * @date 2025/02/20
 */
@Data
public class AiChatMessageDTO {

    /**
     * 消息
     */
    private String message;

    /**
     * 网络搜索
     */
    private boolean webSearch;
}
