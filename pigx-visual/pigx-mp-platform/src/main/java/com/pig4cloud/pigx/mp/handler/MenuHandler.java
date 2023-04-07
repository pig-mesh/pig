package com.pig4cloud.pigx.mp.handler;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.mp.config.WxMpContextHolder;
import com.pig4cloud.pigx.mp.entity.WxMpMenu;
import com.pig4cloud.pigx.mp.mapper.WxMenuMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
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

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@Component
@AllArgsConstructor
public class MenuHandler extends AbstractHandler {

	private final WxMenuMapper wxMenuMapper;

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
			WxSessionManager sessionManager) throws WxErrorException {
		// 组装菜单回复消息
		return getWxMpXmlOutMessage(wxMessage);
	}

	/**
	 * 组装菜单回复消息
	 * @param wxMessage
	 * @return
	 */
	public WxMpXmlOutMessage getWxMpXmlOutMessage(WxMpXmlMessage wxMessage) {

		WxMpMenu wxMpMenu = wxMenuMapper.selectOne(
				Wrappers.<WxMpMenu>lambdaQuery().eq(WxMpMenu::getWxAccountAppid, WxMpContextHolder.getAppId()));
		List<JSONObject> buttons = JSONUtil.parseObj(wxMpMenu.getMenu()).getJSONArray("button")
				.toList(JSONObject.class);
		String eventKey = wxMessage.getEventKey();

		// 获取子集
		List<JSONObject> buttonList = new ArrayList<>();
		for (JSONObject button : buttons) {
			JSONArray subButton = button.getJSONArray("sub_button");
			if (subButton.isEmpty()) {
				buttonList.add(button);
				continue;
			}
			List<JSONObject> subButtonList = subButton.toList(JSONObject.class);
			buttonList.addAll(subButtonList);
		}

		// 遍历规则
		for (JSONObject button : buttonList) {

			// 可以在这里进行自定义逻辑开发
			if (!eventKey.equalsIgnoreCase(button.getStr("key"))) {
				continue;
			}

			WxMpXmlOutMessage wxMpXmlOutMessage = null;
			String repType = button.getStr("repType");
			String repName = button.getStr("repName");
			String repContent = button.getStr("repContent");
			String repMediaId = button.getStr("repMediaId");
			String repDesc = button.getStr("repDesc");
			if (WxConsts.KefuMsgType.TEXT.equals(repType)) {
				wxMpXmlOutMessage = new TextBuilder().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
						.content(repContent).build();
			}

			if (WxConsts.KefuMsgType.IMAGE.equals(repType)) {
				wxMpXmlOutMessage = new ImageBuilder().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
						.mediaId(repMediaId).build();
			}

			if (WxConsts.KefuMsgType.VOICE.equals(repType)) {
				wxMpXmlOutMessage = new VoiceBuilder().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
						.mediaId(repMediaId).build();
			}

			if (WxConsts.KefuMsgType.VIDEO.equals(repType)) {
				wxMpXmlOutMessage = new VideoBuilder().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
						.mediaId(repMediaId).title(repName).description(repDesc).build();
			}

			if (WxConsts.KefuMsgType.MUSIC.equals(repType)) {
				String repThumbMediaId = button.getStr("repThumbMediaId");
				String repUrl = button.getStr("repUrl");
				String repHqUrl = button.getStr("repHqUrl");

				wxMpXmlOutMessage = new MusicBuilder().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
						.thumbMediaId(repThumbMediaId).title(repName).description(repDesc).musicUrl(repUrl)
						.hqMusicUrl(repHqUrl).build();
			}

			if (WxConsts.KefuMsgType.NEWS.equals(repType)) {
				String content = button.getStr("content");

				List<WxMpXmlOutNewsMessage.Item> list = new ArrayList<>();
				List<JSONObject> articles = JSONUtil.parseObj(content).getJSONArray("articles")
						.toList(JSONObject.class);
				WxMpXmlOutNewsMessage.Item t;
				for (JSONObject jsonObject : articles) {
					t = new WxMpXmlOutNewsMessage.Item();
					t.setTitle(jsonObject.getStr("title"));
					t.setDescription(jsonObject.getStr("digest"));
					t.setPicUrl(jsonObject.getStr("thumbUrl"));
					t.setUrl(jsonObject.getStr("url"));
					list.add(t);
				}
				wxMpXmlOutMessage = new NewsBuilder().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
						.articles(list).build();
			}
			return wxMpXmlOutMessage;
		}
		return null;
	}

}
