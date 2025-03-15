package com.pig4cloud.pigx.mp.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.mp.builder.TextBuilder;
import com.pig4cloud.pigx.mp.config.WxMpContextHolder;
import com.pig4cloud.pigx.mp.constant.ReplyTypeEnum;
import com.pig4cloud.pigx.mp.entity.WxAccount;
import com.pig4cloud.pigx.mp.entity.WxAccountFans;
import com.pig4cloud.pigx.mp.entity.WxAutoReply;
import com.pig4cloud.pigx.mp.mapper.WxAccountMapper;
import com.pig4cloud.pigx.mp.mapper.WxMsgMapper;
import com.pig4cloud.pigx.mp.service.WxAccountFansService;
import com.pig4cloud.pigx.mp.service.WxAutoReplyService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@Component
@AllArgsConstructor
public class SubscribeHandler extends AbstractHandler {

    private final WxAccountFansService wxAccountFansService;

    private final WxAutoReplyService wxAutoReplyService;

    private final WxAccountMapper wxAccountMapper;

    private final WxMsgMapper msgMapper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) throws WxErrorException {

        log.info("新关注用户 OPENID: " + wxMessage.getFromUser());

        // 获取微信用户基本信息
        try {
            WxMpUser wxMpUser = weixinService.getUserService().userInfo(wxMessage.getFromUser());
            WxAccountFans wxAccountFans = wxAccountFansService.saveFans(wxMpUser);
            return this.handleSpecial(wxMessage, wxAccountFans);
        } catch (Exception e) {
            log.error("该公众号没有获取用户信息失败！", e);
        }

        return new TextBuilder().build("感谢关注", wxMessage, weixinService);
    }

    /**
     * 处理特殊请求，比如如果是扫码进来的，可以做相应处理
     */
    private WxMpXmlOutMessage handleSpecial(WxMpXmlMessage wxMessage, WxAccountFans fans) {
        // 发送关注消息
        List<WxAutoReply> listWxAutoReply = wxAutoReplyService.list(Wrappers.<WxAutoReply>query()
                .lambda()
                .eq(WxAutoReply::getType, ReplyTypeEnum.ATTENTION.getType())
                .eq(WxAutoReply::getAppId, WxMpContextHolder.getAppId()));
        // 查询公众号 基本信息
        WxAccount wxAccount = wxAccountMapper
                .selectOne(Wrappers.<WxAccount>lambdaQuery().eq(WxAccount::getAccount, wxMessage.getToUser()));
        return MsgHandler.getWxMpXmlOutMessage(wxMessage, listWxAutoReply, fans,
                msgMapper, wxAccount);
    }

}
