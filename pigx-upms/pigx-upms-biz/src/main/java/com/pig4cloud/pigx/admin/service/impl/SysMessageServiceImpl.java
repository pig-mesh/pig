package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.amazonaws.services.s3.model.S3Object;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.pig4cloud.pigx.admin.api.constant.SmsBizCodeEnum;
import com.pig4cloud.pigx.admin.api.constant.SystemConfigTypeEnum;
import com.pig4cloud.pigx.admin.api.dto.MessageEmailDTO;
import com.pig4cloud.pigx.admin.api.dto.MessageHookDTO;
import com.pig4cloud.pigx.admin.api.dto.MessageSmsDTO;
import com.pig4cloud.pigx.admin.api.dto.SysLogDTO;
import com.pig4cloud.pigx.admin.api.entity.*;
import com.pig4cloud.pigx.admin.api.feign.RemoteLogService;
import com.pig4cloud.pigx.admin.api.vo.OrgTreeVO;
import com.pig4cloud.pigx.admin.api.vo.SysMessageReadVO;
import com.pig4cloud.pigx.admin.api.vo.SysMessageVO;
import com.pig4cloud.pigx.admin.mapper.*;
import com.pig4cloud.pigx.admin.service.SysMessageService;
import com.pig4cloud.pigx.common.core.constant.CacheConstants;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.constant.enums.LoginTypeEnum;
import com.pig4cloud.pigx.common.core.constant.enums.YesNoEnum;
import com.pig4cloud.pigx.common.core.exception.CheckedException;
import com.pig4cloud.pigx.common.core.exception.ErrorCodes;
import com.pig4cloud.pigx.common.core.util.MsgUtils;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.file.core.FileTemplate;
import com.pig4cloud.pigx.common.log.util.JacksonSensitiveFieldUtil;
import com.pig4cloud.pigx.common.log.util.LogTypeEnum;
import com.pig4cloud.pigx.common.log.util.SysLogUtils;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.api.OaSender;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.enums.MessageType;
import org.dromara.oa.comm.enums.OaType;
import org.dromara.oa.core.byteTalk.config.ByteTalkConfig;
import org.dromara.oa.core.dingTalk.config.DingTalkConfig;
import org.dromara.oa.core.provider.factory.OaFactory;
import org.dromara.oa.core.weTalk.config.WeTalkConfig;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.dingzhong.config.DingZhongConfig;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.netease.config.NeteaseConfig;
import org.dromara.sms4j.provider.config.BaseConfig;
import org.dromara.sms4j.qiniu.config.QiNiuConfig;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;
import org.dromara.sms4j.zhutong.config.ZhutongConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 站内信息
 *
 * @author pig
 * @date 2023-10-25 13:37:25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysMessageServiceImpl extends ServiceImpl<SysMessageMapper, SysMessageEntity> implements SysMessageService {

    /**
     * 支持短信服务商
     */
    private static final Class[] SUPPORT_CONFIG_CLAZZ = {AlibabaConfig.class, CloopenConfig.class, CtyunConfig.class, EmayConfig.class, HuaweiConfig.class, JdCloudConfig.class, NeteaseConfig.class, TencentConfig.class, UniConfig.class, YunpianConfig.class, ZhutongConfig.class, DingZhongConfig.class, QiNiuConfig.class};

    private final RemoteLogService remoteLogService;

    private final SysSystemConfigMapper sysSystemConfigMapper;

    private final SysMessageRelationMapper relationMapper;

    private final StringRedisTemplate redisTemplate;

    private final SysUserMapper userMapper;

    private final SysFileMapper fileMapper;

    private final FileTemplate fileTemplate;

    /**
     * 发送信息
     *
     * @param id id
     * @return true/false
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean sendMessage(Long id) {
        // 更新信息状态
        SysMessageEntity sysMessage = new SysMessageEntity();
        sysMessage.setSendFlag(YesNoEnum.YES.getCode());
        baseMapper.update(sysMessage, Wrappers.<SysMessageEntity>lambdaQuery().eq(SysMessageEntity::getId, id));
        return Boolean.TRUE;
    }

    /**
     * 保存消息及其关联关系
     *
     * @param sysMessage 消息对象
     * @return 是否保存成功
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateMessage(SysMessageVO sysMessage) {
        SysMessageEntity messageEntity = new SysMessageEntity();
        BeanUtils.copyProperties(sysMessage, messageEntity);

        if (Objects.isNull(sysMessage.getId())) {
            baseMapper.insert(messageEntity);
        } else {
            baseMapper.updateById(sysMessage);
        }

        // 删除原有用户
        relationMapper.delete(Wrappers.<SysMessageRelationEntity>lambdaQuery().eq(SysMessageRelationEntity::getMsgId, messageEntity.getId()));

        List<OrgTreeVO> userList = sysMessage.getUserList();
        if (CollUtil.isNotEmpty(userList)) {
            userList.forEach(user -> {
                SysMessageRelationEntity relationEntity = new SysMessageRelationEntity();
                relationEntity.setMsgId(messageEntity.getId());
                relationEntity.setUserId(user.getId());
                relationMapper.insert(relationEntity);
            });
        } else {
            userMapper.selectList(Wrappers.emptyWrapper()).stream().map(SysUser::getUserId).forEach(userId -> {
                SysMessageRelationEntity relationEntity = new SysMessageRelationEntity();
                relationEntity.setMsgId(messageEntity.getId());
                relationEntity.setUserId(userId);
                relationMapper.insert(relationEntity);
            });
        }
        return Boolean.TRUE;
    }

    /**
     * 获取用户的信息列表
     *
     * @return list
     */
    @Override
    public IPage<SysMessageVO> pageUserMessage(Page page, SysMessageVO sysMessage) {
        MPJLambdaWrapper<SysMessageEntity> wrapper = new MPJLambdaWrapper<SysMessageEntity>().selectAll(SysMessageEntity.class).select(SysMessageRelationEntity::getReadFlag).leftJoin(SysMessageRelationEntity.class, SysMessageRelationEntity::getMsgId, SysMessageEntity::getId).eq(SysMessageRelationEntity::getUserId, SecurityUtils.getUser().getId()).eq(SysMessageEntity::getSendFlag, YesNoEnum.YES.getCode()).eq(StrUtil.isNotBlank(sysMessage.getCategory()), SysMessageEntity::getCategory, sysMessage.getCategory()).eq(StrUtil.isNotBlank(sysMessage.getReadFlag()), SysMessageRelationEntity::getReadFlag, sysMessage.getReadFlag()).orderByDesc(SysMessageEntity::getSort).orderByDesc(SysMessageEntity::getCreateTime);
        return baseMapper.selectJoinPage(page, SysMessageVO.class, wrapper);
    }

    /**
     * 读取信息
     *
     * @param id id
     * @return true/false
     */
    @Override
    public Boolean readMessage(Long id) {
        SysMessageRelationEntity relationEntity = new SysMessageRelationEntity();
        relationEntity.setReadFlag(YesNoEnum.YES.getCode());
        relationMapper.update(relationEntity, Wrappers.<SysMessageRelationEntity>lambdaQuery().eq(SysMessageRelationEntity::getMsgId, id).eq(SysMessageRelationEntity::getUserId, SecurityUtils.getUser().getId()));
        return Boolean.TRUE;
    }

    /**
     * 获取信息
     *
     * @param id id
     * @return messageVO
     */
    @Override
    public SysMessageVO getMessage(Long id) {
        SysMessageEntity messageEntity = baseMapper.selectById(id);
        SysMessageVO SysMessageVO = new SysMessageVO();
        BeanUtils.copyProperties(messageEntity, SysMessageVO);

        // 全体通知
        if (messageEntity.getAllFlag().equals(YesNoEnum.YES.getCode())) {
            return SysMessageVO;
        }
        List<Long> userIdList = relationMapper.selectList(Wrappers.<SysMessageRelationEntity>lambdaQuery().eq(SysMessageRelationEntity::getMsgId, id)).stream().map(SysMessageRelationEntity::getUserId).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(userIdList)) {
            List<OrgTreeVO> orgNodeUserVoList = userMapper.selectBatchIds(userIdList).stream().map(user -> {
                OrgTreeVO nodeUserVo = new OrgTreeVO();
                nodeUserVo.setId(user.getUserId());
                nodeUserVo.setAvatar(user.getAvatar());
                nodeUserVo.setName(user.getName());
                return nodeUserVo;
            }).collect(Collectors.toList());

            SysMessageVO.setUserList(orgNodeUserVoList);
        }

        return SysMessageVO;
    }

    /**
     * 查询用户阅读情况
     *
     * @param page      分页
     * @param messageId 消息ID
     * @param name      姓名
     * @return page
     */
    @Override
    public Page pageUserRead(Page page, Long messageId, String name) {
        MPJLambdaWrapper<SysMessageRelationEntity> wrapper = new MPJLambdaWrapper<SysMessageRelationEntity>().selectAll(SysMessageRelationEntity.class).select(SysUser::getUsername, SysUser::getName, SysUser::getUserId).select(SysMessageEntity::getTitle).leftJoin(SysUser.class, SysUser::getUserId, SysMessageRelationEntity::getUserId).leftJoin(SysMessageEntity.class, SysMessageEntity::getId, SysMessageRelationEntity::getMsgId).eq(SysMessageRelationEntity::getMsgId, messageId).like(StrUtil.isNotBlank(name), SysUser::getName, name);
        return relationMapper.selectJoinPage(page, SysMessageReadVO.class, wrapper);
    }

    /**
     * 发送手机验证码 TODO: 调用短信网关发送验证码,测试返回前端
     *
     * @param mobile mobile
     * @return code
     */
    @Override
    public R<Boolean> sendSmsCode(String mobile) {
        List<SysUser> userList = userMapper.selectList(Wrappers.<SysUser>query().lambda().eq(SysUser::getPhone, mobile));

        if (CollUtil.isEmpty(userList)) {
            log.info("手机号未注册:{}", mobile);
            return R.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodes.SYS_APP_PHONE_UNREGISTERED, mobile));
        }

        String codeObj = redisTemplate.opsForValue().get(CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + StringPool.AT + mobile);

        if (StrUtil.isNotBlank(codeObj)) {
            log.info("手机号验证码未过期:{}，{}", mobile, codeObj);
            return R.ok(Boolean.FALSE, MsgUtils.getMessage(ErrorCodes.SYS_APP_SMS_OFTEN));
        }

        String code = RandomUtil.randomNumbers(Integer.parseInt(SecurityConstants.CODE_SIZE));
        log.debug("手机号生成验证码成功:{},{}", mobile, code);
        redisTemplate.opsForValue().set(CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + StringPool.AT + mobile, code, SecurityConstants.CODE_TIME, TimeUnit.SECONDS);

        // 发送
        return this.sendSms(MessageSmsDTO.builder().mobile(mobile).biz(SmsBizCodeEnum.SMS_NORMAL_CODE.getCode()).param(code).build());
    }

    /**
     * 发送短信
     *
     * @param messageSmsDTO 消息 短信 DTO
     * @return {@link R }
     */
    @Override
    public R sendSms(MessageSmsDTO messageSmsDTO) {

        // 根据业务编码获取短信通道配置
        List<SysSystemConfigEntity> configEntityList = sysSystemConfigMapper.selectList(Wrappers.<SysSystemConfigEntity>lambdaQuery().eq(SysSystemConfigEntity::getConfigType, SystemConfigTypeEnum.SMS.getValue()).eq(SysSystemConfigEntity::getConfigStatus, YesNoEnum.YES.getCode()).eq(SysSystemConfigEntity::getConfigKey, messageSmsDTO.getBizCode()));
        if (CollUtil.isEmpty(configEntityList)) {
            return R.failed("发送失败，短信通道配置不存在");
        }

        SysSystemConfigEntity configEntity = configEntityList.get(0);
        JSONObject configMap = JSONUtil.parseObj(configEntity.getConfigValue());

        // 组装SMS4J 的发送对象
        SmsBlend smsBlend = SmsFactory.getSmsBlend(messageSmsDTO.getBizCode());
        if (Objects.isNull(smsBlend)) {
            Class targetClass = Arrays.stream(SUPPORT_CONFIG_CLAZZ).filter(config -> config.getSimpleName().toLowerCase().contains(configMap.getStr(MessageSmsDTO.Fields.supplier))).findFirst().orElseThrow(() -> new CheckedException("发送失败，短信通道配置不存在"));
            BaseConfig baseConfig = (BaseConfig) JSONUtil.toBean(configEntity.getConfigValue(), targetClass);

            // 如果渠道扩展参数不为空则增加特性化参数
            if (StrUtil.isNotBlank(configMap.getStr(MessageSmsDTO.Fields.extParams))) {
                JSONObject extParams = configMap.getJSONObject(MessageSmsDTO.Fields.extParams);
                BeanUtil.copyProperties(extParams, baseConfig);
            }

            baseConfig.setConfigId(configEntity.getConfigKey());
            SmsFactory.createSmsBlend(baseConfig);
            smsBlend = SmsFactory.getSmsBlend(messageSmsDTO.getBizCode());
        }

        // 异步发送短信
        SysLogDTO sysLog = SysLogUtils.getSysLog();
        Long start = System.currentTimeMillis();
        sysLog.setTenantId(TenantContextHolder.getTenantId());
        for (String mobile : messageSmsDTO.getMobiles()) {
            smsBlend.sendMessageAsync(mobile, configMap.getStr(MessageSmsDTO.Fields.templateId), messageSmsDTO.getParams(), smsResponse -> {
                log.info("发送短信结果:{}", smsResponse);
                sysLog.setTitle(String.format("发送短信 %s", mobile));
                sysLog.setLogType(LogTypeEnum.NORMAL.getType());
                sysLog.setServiceId(SystemConfigTypeEnum.SMS.getValue());
                sysLog.setParams(JacksonSensitiveFieldUtil.writeValueAsString(smsResponse));
                sysLog.setTime(System.currentTimeMillis() - start);
                sysLog.setException(JacksonSensitiveFieldUtil.writeValueAsString(smsResponse));
                remoteLogService.saveLog(sysLog);
            });
        }
        return R.ok();
    }

    /**
     * 发送钩子消息
     *
     * @param messageHookDTO 消息挂钩 DTO
     * @return {@link R }
     */
    @Override
    public R sendHook(MessageHookDTO messageHookDTO) {

        // 根据业务编码获取短信通道配置
        List<SysSystemConfigEntity> configEntityList = sysSystemConfigMapper.selectList(Wrappers.<SysSystemConfigEntity>lambdaQuery()
                .eq(SysSystemConfigEntity::getConfigType, SystemConfigTypeEnum.WEBHOOK.getValue())
                .eq(SysSystemConfigEntity::getConfigStatus, YesNoEnum.YES.getCode())
                .eq(SysSystemConfigEntity::getConfigKey, messageHookDTO.getBizCode()));
        if (CollUtil.isEmpty(configEntityList)) {
            return R.failed("发送失败，短信通道配置不存在");
        }

        SysSystemConfigEntity configEntity = configEntityList.get(0);
        JSONObject configMap = JSONUtil.parseObj(configEntity.getConfigValue());
        String supplier = configMap.getStr("supplier");

        // 根据配置创建服务实例并注册
        OaSender alarm = OaFactory.getSmsOaBlend(messageHookDTO.getBizCode());
        if (Objects.isNull(alarm)) {
            if (StrUtil.equalsAnyIgnoreCase(OaType.DING_TALK.toString(), supplier)) {
                DingTalkConfig dingTalkConfig = new DingTalkConfig();
                dingTalkConfig.setConfigId(messageHookDTO.getBizCode());
                dingTalkConfig.setSign(configMap.getStr("sign"));
                dingTalkConfig.setTokenId(configMap.getStr("tokenId"));
                OaFactory.createAndRegisterOaSender(dingTalkConfig);
            }
            if (StrUtil.equalsAnyIgnoreCase(OaType.BYTE_TALK.toString(), supplier)) {
                ByteTalkConfig byteTalkConfig = new ByteTalkConfig();
                byteTalkConfig.setConfigId(messageHookDTO.getBizCode());
                byteTalkConfig.setSign(configMap.getStr("sign"));
                byteTalkConfig.setTokenId(configMap.getStr("tokenId"));
                OaFactory.createAndRegisterOaSender(byteTalkConfig);
            }


            if (StrUtil.equalsAnyIgnoreCase(OaType.WE_TALK.toString(), supplier)) {
                WeTalkConfig weTalkConfig = new WeTalkConfig();
                weTalkConfig.setConfigId(messageHookDTO.getBizCode());
                weTalkConfig.setSign(configMap.getStr("sign"));
                weTalkConfig.setTokenId(configMap.getStr("tokenId"));
                OaFactory.createAndRegisterOaSender(weTalkConfig);
            }
            alarm = OaFactory.getSmsOaBlend(messageHookDTO.getBizCode());
        }


        Request request = new Request();
        request.setContent(messageHookDTO.getMessageContent());
        request.setTitle(messageHookDTO.getMessageTitle());
        request.setMessageUrl(messageHookDTO.getMessageUrl());
        request.setPicUrl(messageHookDTO.getPicUrl());
        request.setIsNoticeAll(messageHookDTO.isNoticeAll());
        request.setPhoneList(messageHookDTO.getPhoneList());

        SysLogDTO sysLog = SysLogUtils.getSysLog();
        sysLog.setTenantId(TenantContextHolder.getTenantId());
        Long start = System.currentTimeMillis();
        alarm.senderAsync(request, MessageType.valueOf(String.format("%s_%s", supplier, messageHookDTO.getMessageType()).toUpperCase()), response -> {
            log.info("发送Hook结果:{}", response);
            sysLog.setTitle(String.format("发送Hook %s", messageHookDTO.getBizCode()));
            sysLog.setLogType(LogTypeEnum.NORMAL.getType());
            sysLog.setServiceId(SystemConfigTypeEnum.WEBHOOK.getValue());
            sysLog.setParams(JacksonSensitiveFieldUtil.writeValueAsString(messageHookDTO));
            sysLog.setTime(System.currentTimeMillis() - start);
            sysLog.setException(JacksonSensitiveFieldUtil.writeValueAsString(response));
            remoteLogService.saveLog(sysLog);
        });
        return R.ok();
    }

    /**
     * 发送邮件
     *
     * @param messageEmailDTO 留言内容 电子邮件 DTO
     * @return {@link R }
     */
    @SneakyThrows
    @Override
    public R sendEmail(MessageEmailDTO messageEmailDTO) {
        // 根据业务编码获取短信通道配置
        List<SysSystemConfigEntity> configEntityList = sysSystemConfigMapper.selectList(Wrappers.<SysSystemConfigEntity>lambdaQuery()
                .eq(SysSystemConfigEntity::getConfigType, SystemConfigTypeEnum.EMAIL.getValue())
                .eq(SysSystemConfigEntity::getConfigStatus, YesNoEnum.YES.getCode()).
                eq(SysSystemConfigEntity::getConfigKey, messageEmailDTO.getBizCode()));
        if (CollUtil.isEmpty(configEntityList)) {
            return R.failed("发送失败，通道配置不存在");
        }
        SysSystemConfigEntity configEntity = configEntityList.get(0);
        JSONObject configMap = JSONUtil.parseObj(configEntity.getConfigValue());


        List<File> tempFileList = new ArrayList<>();
        if (CollUtil.isNotEmpty(messageEmailDTO.getAttachmentList())) {
            TenantContextHolder.setTenantSkip();
            for (String fileName : messageEmailDTO.getAttachmentList()) {
                SysFile sysFile = fileMapper.selectOne(Wrappers.<SysFile>lambdaQuery().eq(SysFile::getFileName, fileName));
                S3Object s3Object = fileTemplate.getObject(sysFile.getBucketName(), sysFile.getDir(), fileName);
                File file = FileUtil.file(FileUtil.getTmpDirPath(), sysFile.getOriginal());
                tempFileList.add(file);
                IoUtil.copy(s3Object.getObjectContent(), FileUtil.getOutputStream(file));
            }
        }

        MailAccount account = new MailAccount();
        account.setHost(configMap.getStr("smtpServer"));
        account.setPort(configMap.getInt("port"));
        account.setAuth(configMap.getBool("isAuth"));
        account.setSslEnable(configMap.getBool("isSSL"));
        account.setFrom(configMap.getStr("username"));
        account.setUser(configMap.getStr("username"));
        account.setPass(configMap.getStr("password"));

        // 渲染模板
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
        Template template = engine.getTemplate(configMap.getStr("html"));
        String result = template.render(messageEmailDTO.getHtmlValues());

        Long start = System.currentTimeMillis();
        MailUtil.send(account, messageEmailDTO.getMailAddress(),
                messageEmailDTO.getCcList(),
                messageEmailDTO.getBccList(),
                messageEmailDTO.getTitle(), result, true, tempFileList.toArray(new File[0]));
        Long end = System.currentTimeMillis();
        // 写入日志
        SysLogDTO sysLog = SysLogUtils.getSysLog();
        sysLog.setTitle(String.format("发送邮件 %s", messageEmailDTO.getTitle()));
        sysLog.setLogType(LogTypeEnum.NORMAL.getType());
        sysLog.setServiceId(SystemConfigTypeEnum.EMAIL.getValue());
        sysLog.setParams(JacksonSensitiveFieldUtil.writeValueAsString(messageEmailDTO));
        sysLog.setTenantId(TenantContextHolder.getTenantId());
        sysLog.setTime(end - start);
        remoteLogService.saveLog(sysLog);
        // 删除临时文件
        tempFileList.forEach(FileUtil::del);
        return R.ok();
    }
}
