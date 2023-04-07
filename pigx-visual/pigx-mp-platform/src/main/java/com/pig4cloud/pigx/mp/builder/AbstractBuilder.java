package com.pig4cloud.pigx.mp.builder;

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public abstract class AbstractBuilder {

	/**
	 * 构建返回微信消息报文
	 * @param content
	 * @param wxMessage
	 * @param service
	 * @return
	 */
	public abstract WxMpXmlOutMessage build(String content, WxMpXmlMessage wxMessage, WxMpService service);

}
