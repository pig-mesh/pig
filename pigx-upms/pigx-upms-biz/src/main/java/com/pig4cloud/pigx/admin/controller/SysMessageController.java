package com.pig4cloud.pigx.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.admin.api.dto.MessageEmailDTO;
import com.pig4cloud.pigx.admin.api.dto.MessageHookDTO;
import com.pig4cloud.pigx.admin.api.dto.MessageSmsDTO;
import com.pig4cloud.pigx.admin.api.entity.SysMessageEntity;
import com.pig4cloud.pigx.admin.api.vo.SysMessageVO;
import com.pig4cloud.pigx.admin.service.SysMessageService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.ResponseExcel;
import com.pig4cloud.pigx.common.log.annotation.SysLog;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.common.security.annotation.Inner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 站内信息
 *
 * @author pig
 * @date 2023-10-25 13:37:25
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sysMessage")
@Tag(description = "sysMessage", name = "站内信息管理")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class SysMessageController {

    private final SysMessageService sysMessageService;

    /**
     * 分页查询
     *
     * @param page       分页对象
     * @param sysMessage 站内信息
     * @return
     */
    @Operation(summary = "分页查询", description = "分页查询")
    @GetMapping("/page")
    @HasPermission("sys_message_view")
    public R getSysMessagePage(@ParameterObject Page page, @ParameterObject SysMessageEntity sysMessage) {
        LambdaQueryWrapper<SysMessageEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(StrUtil.isNotBlank(sysMessage.getTitle()), SysMessageEntity::getTitle, sysMessage.getTitle());
        return R.ok(sysMessageService.page(page, wrapper));
    }

    /**
     * 通过id查询站内信息
     *
     * @param id id
     * @return R
     */
    @Operation(summary = "通过id查询", description = "通过id查询")
    @GetMapping("/{id}")
    @HasPermission("sys_message_view")
    public R getById(@PathVariable("id") Long id) {
        return R.ok(sysMessageService.getMessage(id));
    }

    /**
     * 新增站内信息
     *
     * @param sysMessage 站内信息
     * @return R
     */
    @Operation(summary = "新增站内信息", description = "新增站内信息")
    @SysLog("新增站内信息")
    @PostMapping
    @HasPermission("sys_message_add")
    public R save(@RequestBody SysMessageVO sysMessage) {
        return R.ok(sysMessageService.saveOrUpdateMessage(sysMessage));
    }

    /**
     * 修改站内信息
     *
     * @param sysMessage 站内信息
     * @return R
     */
    @Operation(summary = "修改站内信息", description = "修改站内信息")
    @SysLog("修改站内信息")
    @PutMapping
    @HasPermission("sys_message_edit")
    public R updateById(@RequestBody SysMessageVO sysMessage) {
        return R.ok(sysMessageService.saveOrUpdateMessage(sysMessage));
    }

    /**
     * 通过id删除站内信息
     *
     * @param ids id列表
     * @return R
     */
    @Operation(summary = "通过id删除站内信息", description = "通过id删除站内信息")
    @SysLog("通过id删除站内信息")
    @DeleteMapping
    @HasPermission("sys_message_del")
    public R removeById(@RequestBody Long[] ids) {
        return R.ok(sysMessageService.removeBatchByIds(CollUtil.toList(ids)));
    }

    /**
     * 导出excel 表格
     *
     * @param sysMessage 查询条件
     * @param ids        导出指定ID
     * @return excel 文件流
     */
    @ResponseExcel
    @GetMapping("/export")
    @HasPermission("sys_message_export")
    public List<SysMessageEntity> export(SysMessageEntity sysMessage, Long[] ids) {
        return sysMessageService
                .list(Wrappers.lambdaQuery(sysMessage).in(ArrayUtil.isNotEmpty(ids), SysMessageEntity::getId, ids));
    }

    /**
     * 发送站内信息
     *
     * @param id 站内信息
     * @return R
     */
    @Operation(summary = "发送站内信息", description = "发送站内信息")
    @SysLog("发送站内信息")
    @PostMapping("/send")
    @HasPermission("sys_message_add")
    public R send(Long id) {
        return R.ok(sysMessageService.sendMessage(id));
    }

    /**
     * 读取站内信息
     *
     * @param id 站内信息
     * @return R
     */
    @Operation(summary = "读取站内信息", description = "读取站内信息")
    @SysLog("发送站内信息")
    @PostMapping("/read")
    public R read(Long id) {
        return R.ok(sysMessageService.readMessage(id));
    }

    /**
     * 查询用户公告
     *
     * @param page       分页条件
     * @param sysMessage 查询条件
     * @return list
     */
    @Operation(summary = "查询用户信", description = "查询用户信")
    @GetMapping("/user/page")
    public R getUserMessageList(@ParameterObject Page page, SysMessageVO sysMessage) {
        return R.ok(sysMessageService.pageUserMessage(page, sysMessage));
    }

    /**
     * 查询信息阅读列表
     *
     * @return list
     */
    @Operation(summary = "查询用户阅读情况", description = "查询用户阅读情况")
    @GetMapping("/user/read/page")
    public R getUserMessageList(@ParameterObject Page page, @ParameterObject Long messageId,
                                @ParameterObject @RequestParam(required = false) String name) {
        return R.ok(sysMessageService.pageUserRead(page, messageId, name));
    }

    /**
     * 发送短信验证码
     *
     * @param mobile 手机号
     * @return {@link R }
     */
    @Inner(value = false)
    @GetMapping("/send/smsCode")
    public R sendSmsCode(@RequestParam String mobile) {
        return sysMessageService.sendSmsCode(mobile);
    }

    /**
     * 发送短信
     *
     * @param messageSmsDTO 消息 短信 DTO
     * @return {@link R }
     */
    @PostMapping("/send/sms")
    public R sendSms(@Valid @RequestBody MessageSmsDTO messageSmsDTO) {
        return sysMessageService.sendSms(messageSmsDTO);
    }

    /**
     * 发送邮件
     *
     * @param messageEmailDTO 留言内容 电子邮件 DTO
     * @return {@link R }
     */
    @PostMapping("/send/email")
    public R sendEmail(@Valid @RequestBody MessageEmailDTO messageEmailDTO) {
        return sysMessageService.sendEmail(messageEmailDTO);
    }

    /**
     * 发送 webhook
     *
     * @param messageHookDTO 消息挂钩 DTO
     * @return {@link R }
     */
    @PostMapping("/send/webhook")
    public R sendWebhook(@Valid @RequestBody MessageHookDTO messageHookDTO) {
        return sysMessageService.sendHook(messageHookDTO);
    }
}
