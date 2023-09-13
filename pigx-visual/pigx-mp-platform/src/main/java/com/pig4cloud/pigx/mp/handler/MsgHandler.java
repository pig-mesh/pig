package com.pig4cloud.pigx.mp.handler;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.mp.config.WxMpContextHolder;
import com.pig4cloud.pigx.mp.constant.MsgTypeEnum;
import com.pig4cloud.pigx.mp.constant.ReplyMateEnum;
import com.pig4cloud.pigx.mp.constant.ReplyTypeEnum;
import com.pig4cloud.pigx.mp.entity.WxAccount;
import com.pig4cloud.pigx.mp.entity.WxAccountFans;
import com.pig4cloud.pigx.mp.entity.WxAutoReply;
import com.pig4cloud.pigx.mp.entity.WxMsg;
import com.pig4cloud.pigx.mp.mapper.WxAccountFansMapper;
import com.pig4cloud.pigx.mp.mapper.WxAccountMapper;
import com.pig4cloud.pigx.mp.mapper.WxMsgMapper;
import com.pig4cloud.pigx.mp.service.WxAutoReplyService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.builder.outxml.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType;

/**
 * @author JL
 */
@Component
@AllArgsConstructor
public class MsgHandler extends AbstractHandler {

	private final WxAutoReplyService wxAutoReplyService;

	private final WxAccountFansMapper wxAccountFansMapper;

	private final WxAccountMapper wxAccountMapper;

	private final WxMsgMapper wxMsgMapper;

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) {
		// 组装回复消息
		if (!wxMessage.getMsgType().equals(XmlMsgType.EVENT)) {
			WxMpXmlOutMessage rs;
			WxAccountFans fans = wxAccountFansMapper
				.selectOne(Wrappers.<WxAccountFans>lambdaQuery().eq(WxAccountFans::getOpenid, wxMessage.getFromUser()));
			// 查询公众号 基本信息
			WxAccount wxAccount = wxAccountMapper
				.selectOne(Wrappers.<WxAccount>lambdaQuery().eq(WxAccount::getAccount, wxMessage.getToUser()));
			// 1、先处理是否有文本关键字回复
			if (WxConsts.KefuMsgType.TEXT.equals(wxMessage.getMsgType())) {
				// 先全匹配
				List<WxAutoReply> listWxAutoReply = wxAutoReplyService.list(Wrappers.<WxAutoReply>query()
					.lambda()
					.eq(WxAutoReply::getType, ReplyTypeEnum.KEYWORD.getType())
					.eq(WxAutoReply::getRepMate, ReplyMateEnum.ALL.getType())
					.eq(WxAutoReply::getAppId, WxMpContextHolder.getAppId())
					.eq(WxAutoReply::getReqKey, wxMessage.getContent()));
				if (listWxAutoReply != null && listWxAutoReply.size() > 0) {
					rs = getWxMpXmlOutMessage(wxMessage, listWxAutoReply, fans, wxMsgMapper, wxAccount);
					if (rs != null) {
						return rs;
					}
				}
				// 再半匹配
				listWxAutoReply = wxAutoReplyService.list(Wrappers.<WxAutoReply>query()
					.lambda()
					.eq(WxAutoReply::getType, ReplyTypeEnum.KEYWORD.getType())
					.eq(WxAutoReply::getAppId, WxMpContextHolder.getAppId())
					.eq(WxAutoReply::getRepMate, ReplyMateEnum.LIKE.getType())
					.like(WxAutoReply::getReqKey, wxMessage.getContent()));
				if (listWxAutoReply != null && listWxAutoReply.size() > 0) {
					rs = getWxMpXmlOutMessage(wxMessage, listWxAutoReply, fans, wxMsgMapper, wxAccount);
					if (rs != null) {
						return rs;
					}
				}
			}
			// 2、再处理消息回复
			List<WxAutoReply> listWxAutoReply = wxAutoReplyService.list(Wrappers.<WxAutoReply>query()
				.lambda()
				.eq(WxAutoReply::getAppId, WxMpContextHolder.getAppId())
				.eq(WxAutoReply::getType, ReplyTypeEnum.MSG.getType())
				.eq(WxAutoReply::getReqType, wxMessage.getMsgType()));
			rs = getWxMpXmlOutMessage(wxMessage, listWxAutoReply, fans, wxMsgMapper, wxAccount);
			return rs;
		}
		return null;

	}

	/**
	 * 组装回复消息，并记录消息
	 * @param wxMessage
	 * @param listWxAutoReply
	 * @param wxMsgMapper
	 * @param wxAccount
	 * @return
	 */
	public static WxMpXmlOutMessage getWxMpXmlOutMessage(WxMpXmlMessage wxMessage, List<WxAutoReply> listWxAutoReply,
			WxAccountFans fans, WxMsgMapper wxMsgMapper, WxAccount wxAccount) {
		WxMpXmlOutMessage wxMpXmlOutMessage = null;
		// 记录接收消息

		if (listWxAutoReply != null && listWxAutoReply.size() > 0) {
			WxAutoReply wxAutoReply = listWxAutoReply.get(0);
			// 记录回复消息
			WxMsg wxMsg = new WxMsg();
			wxMsg.setWxUserId(fans.getId());
			wxMsg.setNickName(fans.getNickname());
			wxMsg.setHeadimgUrl(fans.getHeadimgUrl());
			wxMsg.setType(MsgTypeEnum.MP2USER.getType());
			wxMsg.setRepType(wxAutoReply.getRepType());
			wxMsg.setOpenId(fans.getOpenid());
			wxMsg.setAppName(wxAccount.getName());
			wxMsg.setAppLogo(wxAccount.getQrUrl());
			wxMsg.setAppId(wxAccount.getAppid());
			// 文本
			if (WxConsts.KefuMsgType.TEXT.equals(wxAutoReply.getRepType())) {
				wxMsg.setRepContent(wxAutoReply.getRepContent());
				wxMpXmlOutMessage = new TextBuilder().fromUser(wxMessage.getToUser())
					.toUser(wxMessage.getFromUser())
					.content(wxAutoReply.getRepContent())
					.build();
			}

			// 图片
			if (WxConsts.KefuMsgType.IMAGE.equals(wxAutoReply.getRepType())) {
				wxMsg.setRepName(wxAutoReply.getRepName());
				wxMsg.setRepUrl(wxAutoReply.getRepUrl());
				wxMsg.setRepMediaId(wxAutoReply.getRepMediaId());
				wxMpXmlOutMessage = new ImageBuilder().fromUser(wxMessage.getToUser())
					.toUser(wxMessage.getFromUser())
					.mediaId(wxAutoReply.getRepMediaId())
					.build();
			}
			if (WxConsts.KefuMsgType.VOICE.equals(wxAutoReply.getRepType())) {
				wxMsg.setRepName(wxAutoReply.getRepName());
				wxMsg.setRepUrl(wxAutoReply.getRepUrl());
				wxMsg.setRepMediaId(wxAutoReply.getRepMediaId());
				wxMpXmlOutMessage = new VoiceBuilder().fromUser(wxMessage.getToUser())
					.toUser(wxMessage.getFromUser())
					.mediaId(wxAutoReply.getRepMediaId())
					.build();
			}
			if (WxConsts.KefuMsgType.VIDEO.equals(wxAutoReply.getRepType())) {
				wxMsg.setRepName(wxAutoReply.getRepName());
				wxMsg.setRepDesc(wxAutoReply.getRepDesc());
				wxMsg.setRepUrl(wxAutoReply.getRepUrl());
				wxMsg.setRepMediaId(wxAutoReply.getRepMediaId());
				wxMpXmlOutMessage = new VideoBuilder().fromUser(wxMessage.getToUser())
					.toUser(wxMessage.getFromUser())
					.mediaId(wxAutoReply.getRepMediaId())
					.title(wxAutoReply.getRepName())
					.description(wxAutoReply.getRepDesc())
					.build();
			}
			if (WxConsts.KefuMsgType.MUSIC.equals(wxAutoReply.getRepType())) {
				wxMsg.setRepName(wxAutoReply.getRepName());
				wxMsg.setRepDesc(wxAutoReply.getRepDesc());
				wxMsg.setRepUrl(wxAutoReply.getRepUrl());
				wxMsg.setRepHqUrl(wxAutoReply.getRepHqUrl());
				wxMsg.setRepThumbMediaId(wxAutoReply.getRepThumbMediaId());
				wxMsg.setRepThumbUrl(wxAutoReply.getRepThumbUrl());
				wxMpXmlOutMessage = new MusicBuilder().fromUser(wxMessage.getToUser())
					.toUser(wxMessage.getFromUser())
					.thumbMediaId(wxAutoReply.getRepThumbMediaId())
					.title(wxAutoReply.getRepName())
					.description(wxAutoReply.getRepDesc())
					.musicUrl(wxAutoReply.getRepUrl())
					.hqMusicUrl(wxAutoReply.getRepHqUrl())
					.build();
			}
			if (WxConsts.KefuMsgType.NEWS.equals(wxAutoReply.getRepType())) {
				List<WxMpXmlOutNewsMessage.Item> list = new ArrayList<>();
				JSONArray jsonArray = JSONUtil.parseArray(wxAutoReply.getContent());
				for (Object obj : jsonArray) {
					JSONObject jsonObject = (JSONObject) obj;
					WxMpXmlOutNewsMessage.Item t = new WxMpXmlOutNewsMessage.Item();
					t.setTitle(jsonObject.getStr("title"));
					t.setDescription(jsonObject.getStr("digest"));
					t.setPicUrl(jsonObject.getStr("thumbUrl"));
					t.setUrl(jsonObject.getStr("url"));
					list.add(t);
				}
				wxMsg.setRepName(wxAutoReply.getRepName());
				wxMsg.setRepDesc(wxAutoReply.getRepDesc());
				wxMsg.setRepUrl(wxAutoReply.getRepUrl());
				wxMsg.setRepMediaId(wxAutoReply.getRepMediaId());
				wxMsg.setContent(wxAutoReply.getContent());
				wxMpXmlOutMessage = new NewsBuilder().fromUser(wxMessage.getToUser())
					.toUser(wxMessage.getFromUser())
					.articles(list)
					.build();
			}
			wxMsgMapper.insert(wxMsg);
		}
		return wxMpXmlOutMessage;
	}

}
