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
import java.util.Objects;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@Component
@AllArgsConstructor
public class LocationHandler extends AbstractHandler {

    private final WxAccountFansService wxAccountFansService;

    private final WxAutoReplyService wxAutoReplyService;

    private final WxAccountMapper wxAccountMapper;

    private final WxMsgMapper msgMapper;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
                                    WxSessionManager sessionManager) {
        // 上报地理位置事件
        log.info("上报地理位置，纬度 : {}，经度 : {}，精度 : {}", wxMessage.getLatitude(), wxMessage.getLongitude(),
                wxMessage.getPrecision());

        // 发送关注消息
        List<WxAutoReply> listWxAutoReply = wxAutoReplyService.list(Wrappers.<WxAutoReply>query()
                .lambda()
                .eq(WxAutoReply::getType, ReplyTypeEnum.MSG.getType())
                .eq(WxAutoReply::getAppId, WxMpContextHolder.getAppId())
                .eq(WxAutoReply::getReqType, wxMessage.getMsgType()));
        // 查询公众号 基本信息
        WxAccount wxAccount = wxAccountMapper
                .selectOne(Wrappers.<WxAccount>lambdaQuery().eq(WxAccount::getAccount, wxMessage.getToUser()));

        // 查询粉丝基本信息
        WxAccountFans fans = wxAccountFansService.getOne(
                Wrappers.<WxAccountFans>lambdaQuery().eq(WxAccountFans::getOpenid, wxMessage.getFromUser()), false);

        if (Objects.isNull(fans)) {
            WxMpUser wxMpUser = null;
            try {
                wxMpUser = weixinService.getUserService().userInfo(wxMessage.getFromUser());
            } catch (WxErrorException e) {
                log.error("获取用户信息失败！", e);
                return new TextBuilder().build("感谢使用", wxMessage, weixinService);
            }
            fans = wxAccountFansService.saveFans(wxMpUser);
        }

        return MsgHandler.getWxMpXmlOutMessage(wxMessage, listWxAutoReply, fans, msgMapper, wxAccount);
    }

}
