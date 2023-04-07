package com.pig4cloud.pigx.mp.builder;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class ImageBuilder extends AbstractBuilder {

	@Override
	public WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage, WxMpService service) {

		WxMpXmlOutImageMessage m = WxMpXmlOutMessage.IMAGE().mediaId(content).fromUser(wxMessage.getToUser())
				.toUser(wxMessage.getFromUser()).build();

		return m;
	}

}
