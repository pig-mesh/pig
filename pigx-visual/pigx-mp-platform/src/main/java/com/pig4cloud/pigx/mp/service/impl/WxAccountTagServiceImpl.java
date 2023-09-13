package com.pig4cloud.pigx.mp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.mp.config.WxMpInitConfigRunner;
import com.pig4cloud.pigx.mp.entity.WxAccount;
import com.pig4cloud.pigx.mp.entity.WxAccountTag;
import com.pig4cloud.pigx.mp.mapper.WxAccountMapper;
import com.pig4cloud.pigx.mp.mapper.WxAccountTagMapper;
import com.pig4cloud.pigx.mp.service.WxAccountTagService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 微信账户标签服务实现类
 *
 * @author lishangbu
 * @date 2021/12/31
 */
@Service
@RequiredArgsConstructor
public class WxAccountTagServiceImpl extends ServiceImpl<WxAccountTagMapper, WxAccountTag>
		implements WxAccountTagService {

	private final WxAccountMapper accountMapper;

	/**
	 * 保存账户标签
	 * @param accountTag 账户标签
	 * @return 保存成功true保存失败false
	 */
	@Override
	@SneakyThrows
	@Transactional(rollbackFor = Exception.class)
	public WxAccountTag saveAccountTag(WxAccountTag accountTag) {
		WxAccountTag wxAccountTag = baseMapper
			.selectOne(Wrappers.<WxAccountTag>lambdaQuery().eq(WxAccountTag::getTag, accountTag.getTag()));
		Assert.isNull(wxAccountTag, StrUtil.format("名称为[{}]的标签已经存在", accountTag.getTag()));

		// 调用微信公众号接口删除
		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(accountTag.getWxAccountAppid());
		WxUserTag wxUserTag = wxMpService.getUserTagService().tagCreate(accountTag.getTag());
		accountTag.setTagId(wxUserTag.getId());

		// 查询公众号详细信息
		WxAccount wxAccount = accountMapper
			.selectOne(Wrappers.<WxAccount>lambdaQuery().eq(WxAccount::getAppid, accountTag.getWxAccountAppid()));
		accountTag.setWxAccountId(wxAccount.getId());
		accountTag.setWxAccountAppid(wxAccount.getAppid());
		accountTag.setWxAccountName(wxAccount.getName());

		baseMapper.insert(accountTag);
		return accountTag;
	}

	/**
	 * 更新账户标签
	 * @param accountTag 待更新的账户标签
	 * @return
	 */
	@Override
	@SneakyThrows
	@Transactional(rollbackFor = Exception.class)
	public WxAccountTag updateAccountTag(WxAccountTag accountTag) {
		baseMapper.updateById(accountTag);

		// 调用微信公众号接口更新
		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(accountTag.getWxAccountAppid());
		wxMpService.getUserTagService().tagUpdate(accountTag.getTagId(), accountTag.getTag());
		return accountTag;
	}

	@Override
	@SneakyThrows
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeAccountTagById(WxAccountTag tag) {
		WxAccountTag wxAccountTag = baseMapper.selectById(tag.getId());
		Assert.notNull(wxAccountTag, "要删除的标签不存在");

		// 调用微信公众号接口删除
		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(tag.getWxAccountAppid());
		wxMpService.getUserTagService().tagDelete(wxAccountTag.getTagId());
		return removeById(wxAccountTag.getId());
	}

	/**
	 * 同步账户标签
	 * @param appId appId
	 * @return Boolean
	 */
	@Override
	@SneakyThrows
	@Transactional(rollbackFor = Exception.class)
	public Boolean syncAccountTags(String appId) {
		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
		List<WxUserTag> wxUserTags = wxMpService.getUserTagService().tagGet();

		// 删除旧数据
		baseMapper.delete(Wrappers.<WxAccountTag>lambdaQuery().eq(WxAccountTag::getWxAccountAppid, appId));

		WxAccount wxAccount = accountMapper.selectOne(Wrappers.<WxAccount>lambdaQuery().eq(WxAccount::getAppid, appId));
		for (WxUserTag wxUserTag : wxUserTags) {
			WxAccountTag tag = new WxAccountTag();
			tag.setTag(wxUserTag.getName());
			tag.setTagId(wxUserTag.getId());
			tag.setWxAccountId(wxAccount.getId());
			tag.setWxAccountAppid(wxAccount.getAppid());
			tag.setWxAccountName(wxAccount.getName());
			baseMapper.insert(tag);
		}

		return Boolean.TRUE;
	}

}
