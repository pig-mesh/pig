package com.pig4cloud.pigx.mp.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.mp.config.WxMpInitConfigRunner;
import com.pig4cloud.pigx.mp.constant.MsgTypeEnum;
import com.pig4cloud.pigx.mp.entity.WxAccount;
import com.pig4cloud.pigx.mp.entity.WxAccountFans;
import com.pig4cloud.pigx.mp.entity.WxMsg;
import com.pig4cloud.pigx.mp.mapper.WxAccountFansMapper;
import com.pig4cloud.pigx.mp.mapper.WxAccountMapper;
import com.pig4cloud.pigx.mp.mapper.WxMsgMapper;
import com.pig4cloud.pigx.mp.service.WxMsgService;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpKefuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信消息
 *
 * @author JL
 * @date 2019-05-28 16:12:10
 */
@Service
@AllArgsConstructor
public class WxMsgServiceImpl extends ServiceImpl<WxMsgMapper, WxMsg> implements WxMsgService {

	private final WxAccountFansMapper accountFansMapper;

	private final WxAccountMapper accountMapper;

	private final WxMsgMapper msgMapper;

	/**
	 * 保存信息并向用户推送
	 * @param wxMsg 推送消息内容
	 * @return
	 */
	@Override
	public R saveAndPushMsg(WxMsg wxMsg) {
		// 查询用户详细
		WxAccountFans wxUser = accountFansMapper.selectById(wxMsg.getWxUserId());
		WxAccount wxAccount = accountMapper
			.selectOne(Wrappers.<WxAccount>lambdaQuery().eq(WxAccount::getAppid, wxMsg.getAppId()));

		// 维护消息-用户
		wxMsg.setNickName(wxAccount.getName());
		wxMsg.setHeadimgUrl(wxUser.getHeadimgUrl());
		wxMsg.setType(MsgTypeEnum.MP2USER.getType());

		// 维护消息-公众号
		wxMsg.setAppLogo(wxAccount.getQrUrl());
		wxMsg.setAppName(wxAccount.getName());

		WxMpKefuMessage wxMpKefuMessage = null;
		if (WxConsts.KefuMsgType.TEXT.equals(wxMsg.getRepType())) {
			wxMsg.setRepContent(wxMsg.getRepContent());
			wxMpKefuMessage = WxMpKefuMessage.TEXT().build();
			wxMpKefuMessage.setContent(wxMsg.getRepContent());
		}

		if (WxConsts.KefuMsgType.IMAGE.equals(wxMsg.getRepType())) {// 图片
			wxMsg.setRepName(wxMsg.getRepName());
			wxMsg.setRepUrl(wxMsg.getRepUrl());
			wxMsg.setRepMediaId(wxMsg.getRepMediaId());
			wxMpKefuMessage = WxMpKefuMessage.IMAGE().build();
			wxMpKefuMessage.setMediaId(wxMsg.getRepMediaId());
		}

		if (WxConsts.KefuMsgType.VOICE.equals(wxMsg.getRepType())) {
			wxMsg.setRepName(wxMsg.getRepName());
			wxMsg.setRepUrl(wxMsg.getRepUrl());
			wxMsg.setRepMediaId(wxMsg.getRepMediaId());
			wxMpKefuMessage = WxMpKefuMessage.VOICE().build();
			wxMpKefuMessage.setMediaId(wxMsg.getRepMediaId());
		}

		if (WxConsts.KefuMsgType.VIDEO.equals(wxMsg.getRepType())) {
			wxMsg.setRepName(wxMsg.getRepName());
			wxMsg.setRepDesc(wxMsg.getRepDesc());
			wxMsg.setRepUrl(wxMsg.getRepUrl());
			wxMsg.setRepMediaId(wxMsg.getRepMediaId());
			wxMpKefuMessage = WxMpKefuMessage.VIDEO().build();
			wxMpKefuMessage.setMediaId(wxMsg.getRepMediaId());
			wxMpKefuMessage.setTitle(wxMsg.getRepName());
			wxMpKefuMessage.setDescription(wxMsg.getRepDesc());
		}

		if (WxConsts.KefuMsgType.MUSIC.equals(wxMsg.getRepType())) {
			wxMsg.setRepName(wxMsg.getRepName());
			wxMsg.setRepDesc(wxMsg.getRepDesc());
			wxMsg.setRepUrl(wxMsg.getRepUrl());
			wxMsg.setRepHqUrl(wxMsg.getRepHqUrl());
			wxMpKefuMessage = WxMpKefuMessage.MUSIC().build();
			wxMpKefuMessage.setTitle(wxMsg.getRepName());
			wxMpKefuMessage.setDescription(wxMsg.getRepDesc());
			wxMpKefuMessage.setMusicUrl(wxMsg.getRepUrl());
			wxMpKefuMessage.setHqMusicUrl(wxMsg.getRepHqUrl());
			wxMpKefuMessage.setThumbMediaId(wxMsg.getRepThumbMediaId());
		}

		if (WxConsts.KefuMsgType.NEWS.equals(wxMsg.getRepType())) {
			List<WxMpKefuMessage.WxArticle> list = new ArrayList<>();
			JSONArray jsonArray = JSONUtil.parseArray(wxMsg.getContent());
			for (Object obj : jsonArray) {
				JSONObject jsonObject = (JSONObject) obj;
				WxMpKefuMessage.WxArticle t = new WxMpKefuMessage.WxArticle();
				t.setTitle(jsonObject.getStr("title"));
				t.setDescription(jsonObject.getStr("digest"));
				t.setPicUrl(jsonObject.getStr("thumbUrl"));
				t.setUrl(jsonObject.getStr("url"));
				list.add(t);
			}
			wxMsg.setRepName(wxMsg.getRepName());
			wxMsg.setRepDesc(wxMsg.getRepDesc());
			wxMsg.setRepUrl(wxMsg.getRepUrl());
			wxMsg.setRepMediaId(wxMsg.getRepMediaId());
			wxMsg.setContent(wxMsg.getContent());
			wxMpKefuMessage = WxMpKefuMessage.NEWS().build();
			wxMpKefuMessage.setArticles(list);
		}

		if (wxMpKefuMessage == null) {
			return R.failed("非法消息类型");
		}

		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(wxMsg.getAppId());
		WxMpKefuService wxMpKefuService = wxMpService.getKefuService();
		wxMpKefuMessage.setToUser(wxUser.getOpenid());
		try {
			wxMpKefuService.sendKefuMessage(wxMpKefuMessage);
			msgMapper.insert(wxMsg);
			return R.ok(wxMsg);
		}
		catch (WxErrorException e) {
			return R.failed(e.getError().getErrorMsg());
		}
	}

}
