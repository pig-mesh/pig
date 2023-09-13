package com.pig4cloud.pigx.mp.handler;

import cn.binarywang.tools.generator.ChineseNameGenerator;
import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.mp.builder.TextBuilder;
import com.pig4cloud.pigx.mp.config.WxMpContextHolder;
import com.pig4cloud.pigx.mp.constant.ReplyTypeEnum;
import com.pig4cloud.pigx.mp.entity.WxAccount;
import com.pig4cloud.pigx.mp.entity.WxAccountFans;
import com.pig4cloud.pigx.mp.entity.WxAutoReply;
import com.pig4cloud.pigx.mp.mapper.WxAccountFansMapper;
import com.pig4cloud.pigx.mp.mapper.WxAccountMapper;
import com.pig4cloud.pigx.mp.mapper.WxMsgMapper;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
@Slf4j
@Component
@AllArgsConstructor
public class SubscribeHandler extends AbstractHandler {

	private final WxAutoReplyService wxAutoReplyService;

	private final WxAccountFansMapper wxAccountFansMapper;

	private final WxAccountMapper wxAccountMapper;

	private final WxMsgMapper msgMapper;

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService weixinService,
			WxSessionManager sessionManager) throws WxErrorException {

		log.info("新关注用户 OPENID: " + wxMessage.getFromUser());

		// 获取微信用户基本信息
		try {
			WxMpUser wxMpUser = weixinService.getUserService().userInfo(wxMessage.getFromUser(), null);
			WxAccountFans wxAccountFans = wxAccountFansMapper.selectOne(
					Wrappers.<WxAccountFans>lambdaUpdate().eq(WxAccountFans::getOpenid, wxMessage.getFromUser()));

			// 删除历史关注保存的信息
			if (wxAccountFans != null) {
				wxAccountFansMapper.deleteById(wxAccountFans);
			}

			wxAccountFans = new WxAccountFans();
			wxAccountFans.setOpenid(wxMpUser.getOpenId());
			wxAccountFans.setSubscribeStatus(String.valueOf(BooleanUtil.toInt(wxMpUser.getSubscribe())));
			wxAccountFans.setSubscribeTime(LocalDateTime
				.ofInstant(Instant.ofEpochMilli(wxMpUser.getSubscribeTime() * 1000L), ZoneId.systemDefault()));

			// 随机生成一个昵称，方便平台内部使用
			String generatedName = ChineseNameGenerator.getInstance().generate();
			wxAccountFans.setNickname(generatedName);
			wxAccountFans.setLanguage(wxMpUser.getLanguage());
			wxAccountFans.setHeadimgUrl(wxMpUser.getHeadImgUrl());
			wxAccountFans.setRemark(wxMpUser.getRemark());
			wxAccountFans.setTagIds(wxAccountFans.getTagIds());

			WxAccount wxAccount = wxAccountMapper
				.selectOne(Wrappers.<WxAccount>lambdaQuery().eq(WxAccount::getAppid, WxMpContextHolder.getAppId()));
			wxAccountFans.setWxAccountId(wxAccount.getId());
			wxAccountFans.setWxAccountAppid(wxAccount.getAppid());
			wxAccountFans.setWxAccountName(wxAccount.getName());

			wxAccountFansMapper.insert(wxAccountFans);
			return this.handleSpecial(wxMessage, wxAccountFans);
		}
		catch (WxErrorException e) {
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
		WxMpXmlOutMessage wxMpXmlOutMessage = MsgHandler.getWxMpXmlOutMessage(wxMessage, listWxAutoReply, fans,
				msgMapper, wxAccount);
		return wxMpXmlOutMessage;
	}

}
