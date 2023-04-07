package com.pig4cloud.pigx.mp.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.mp.constant.MsgTypeEnum;
import com.pig4cloud.pigx.mp.entity.WxAccount;
import com.pig4cloud.pigx.mp.entity.WxAccountFans;
import com.pig4cloud.pigx.mp.entity.WxMsg;
import com.pig4cloud.pigx.mp.mapper.WxAccountFansMapper;
import com.pig4cloud.pigx.mp.mapper.WxAccountMapper;
import com.pig4cloud.pigx.mp.mapper.WxMsgMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lengleng
 * <p>
 * 保存微信消息
 */
@Slf4j
@Component

@AllArgsConstructor
public class LogHandler extends AbstractHandler {

	private final WxAccountFansMapper fansMapper;

	private final WxAccountMapper accountMapper;

	private final WxMsgMapper msgMapper;

	@Override
	@SneakyThrows
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) {
		log.debug("接收到请求消息，内容：{}", wxMessage.getContent());

		WxAccount wxAccount = accountMapper
				.selectOne(Wrappers.<WxAccount>lambdaQuery().eq(WxAccount::getAccount, wxMessage.getToUser()));

		WxAccountFans fans = fansMapper
				.selectOne(Wrappers.<WxAccountFans>lambdaQuery().eq(WxAccountFans::getOpenid, wxMessage.getFromUser()));

		if (fans == null) {
			return null;
		}

		WxMsg wxMsg = new WxMsg();
		wxMsg.setWxUserId(fans.getId());
		wxMsg.setNickName(fans.getNickname());
		wxMsg.setHeadimgUrl(fans.getHeadimgUrl());
		wxMsg.setType(MsgTypeEnum.USER2MP.getType());
		wxMsg.setRepEvent(wxMessage.getEvent());
		wxMsg.setRepType(wxMessage.getMsgType());
		wxMsg.setRepMediaId(wxMessage.getMediaId());
		wxMsg.setOpenId(fans.getOpenid());
		wxMsg.setAppId(wxAccount.getAppid());
		wxMsg.setAppLogo(wxAccount.getQrUrl());
		wxMsg.setAppName(wxAccount.getName());
		if (WxConsts.XmlMsgType.TEXT.equals(wxMessage.getMsgType())) {
			wxMsg.setRepContent(wxMessage.getContent());
		}

		if (WxConsts.MenuButtonType.VIEW.equalsIgnoreCase(wxMessage.getEvent())) {
			wxMsg.setRepUrl(wxMessage.getEventKey());
		}

		if (WxConsts.MenuButtonType.CLICK.equalsIgnoreCase(wxMessage.getEvent())) {
			wxMsg.setRepName(wxMessage.getEventKey());
		}

		if (WxConsts.XmlMsgType.VOICE.equals(wxMessage.getMsgType())) {
			wxMsg.setRepName(wxMessage.getMediaId() + "." + wxMessage.getFormat());
			wxMsg.setRepContent(wxMessage.getRecognition());
		}
		if (WxConsts.XmlMsgType.IMAGE.equals(wxMessage.getMsgType())) {
			wxMsg.setRepUrl(wxMessage.getPicUrl());
		}
		if (WxConsts.XmlMsgType.LINK.equals(wxMessage.getMsgType())) {
			wxMsg.setRepName(wxMessage.getTitle());
			wxMsg.setRepDesc(wxMessage.getDescription());
			wxMsg.setRepUrl(wxMessage.getUrl());
		}
		if (WxConsts.MediaFileType.FILE.equals(wxMessage.getMsgType())) {
			wxMsg.setRepName(wxMessage.getTitle());
			wxMsg.setRepDesc(wxMessage.getDescription());
		}
		if (WxConsts.XmlMsgType.VIDEO.equals(wxMessage.getMsgType())) {
			wxMsg.setRepThumbMediaId(wxMessage.getThumbMediaId());
		}
		if (WxConsts.XmlMsgType.LOCATION.equals(wxMessage.getMsgType())) {
			wxMsg.setRepLocationX(wxMessage.getLocationX());
			wxMsg.setRepLocationY(wxMessage.getLocationY());
			wxMsg.setRepScale(wxMessage.getScale());
			wxMsg.setRepContent(wxMessage.getLabel());
		}
		msgMapper.insert(wxMsg);
		log.debug("保存微信用户信息成功 {}", wxMsg);
		return null;
	}

}
