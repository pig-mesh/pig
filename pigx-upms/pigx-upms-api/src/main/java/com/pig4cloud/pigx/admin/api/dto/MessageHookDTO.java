package com.pig4cloud.pigx.admin.api.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 短信发送
 *
 * @author lengleng
 * @date 2024/7/18
 */
@Data
@FieldNameConstants
public class MessageHookDTO {


    /**
     * 业务代码
     */
    private String bizCode;

    /**
     * 通知列表
     */
    private List<String> phoneList = new ArrayList<>();


    /**
     * 通知全部
     */
    private boolean noticeAll;


    /**
     * 消息类型
     */
    private String messageType;


    /**
     * 留言内容
     */
    private String messageContent;

    /**
     * 消息标题
     */
    private String messageTitle;

    /**
     * 消息 URL
     */
    private String messageUrl;

    /**
     * 图片链接
     */
    private String picUrl;

}
