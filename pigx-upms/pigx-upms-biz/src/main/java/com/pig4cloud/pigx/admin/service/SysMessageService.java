package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.dto.MessageEmailDTO;
import com.pig4cloud.pigx.admin.api.dto.MessageHookDTO;
import com.pig4cloud.pigx.admin.api.dto.MessageSmsDTO;
import com.pig4cloud.pigx.admin.api.entity.SysMessageEntity;
import com.pig4cloud.pigx.admin.api.vo.SysMessageVO;
import com.pig4cloud.pigx.common.core.util.R;

public interface SysMessageService extends IService<SysMessageEntity> {

    /**
     * 发送信息
     *
     * @param id 消息ID
     * @return true/false
     */
    Boolean sendMessage(Long id);

    /**
     * 保存
     *
     * @param sysMessage 消息
     * @return true/false
     */
    Boolean saveOrUpdateMessage(SysMessageVO sysMessage);

    /**
     * 获取用户的信息列表
     *
     * @return list
     */
    IPage<SysMessageVO> pageUserMessage(Page page, SysMessageVO sysMessage);

    /**
     * 读取信息
     *
     * @param id id
     * @return true/false
     */
    Boolean readMessage(Long id);

    /**
     * 获取信息
     *
     * @param id id
     * @return messageVO
     */
    SysMessageVO getMessage(Long id);

    /**
     * 查询用户阅读情况
     *
     * @param page      分页
     * @param messageId 消息ID
     * @param name      姓名
     * @return page
     */
    Page pageUserRead(Page page, Long messageId, String name);

    /**
     * 发送手机验证码
     *
     * @param mobile mobile
     * @return code
     */
    R<Boolean> sendSmsCode(String mobile);

    /**
     * 发送短信
     *
     * @param messageSmsDTO 消息 短信 DTO
     * @return {@link R }
     */
    R sendSms(MessageSmsDTO messageSmsDTO);

    /**
     * 发送邮件
     *
     * @param messageEmailDTO 留言内容 电子邮件 DTO
     * @return {@link R }
     */
    R sendEmail(MessageEmailDTO messageEmailDTO);

    /**
     * 发送钩子消息
     *
     * @param messageHookDTO 消息挂钩 DTO
     * @return {@link R }
     */
    R sendHook(MessageHookDTO messageHookDTO);
}
