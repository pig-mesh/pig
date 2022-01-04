package com.pig4cloud.pigx.mp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.mp.entity.WxAccountTag;
import com.pig4cloud.pigx.mp.mapper.WxAccountTagMapper;
import com.pig4cloud.pigx.mp.service.WxAccountTagService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 微信账户标签服务实现类
 *
 * @author lishangbu
 * @date 2021/12/31
 */
@Service
public class WxAccountTagServiceImpl extends ServiceImpl<WxAccountTagMapper, WxAccountTag>
		implements WxAccountTagService {

	/**
	 * 保存账户标签
	 * @param accountTag 账户标签
	 * @return 保存成功true保存失败false
	 */
	@Override
	public WxAccountTag saveAccountTag(WxAccountTag accountTag) {
		WxAccountTag wxAccountTag = baseMapper
				.selectOne(Wrappers.<WxAccountTag>lambdaQuery().eq(WxAccountTag::getTag, accountTag.getTag()));
		Assert.isNull(wxAccountTag, StrUtil.format("名称为[{}]的标签已经存在", accountTag.getTag()));
		baseMapper.insert(accountTag);
		return accountTag;
	}

	/**
	 * 更新账户标签
	 * @param accountTag 待更新的账户标签
	 * @return
	 */
	@Override
	public WxAccountTag updateAccountTag(WxAccountTag accountTag) {
		WxAccountTag wxAccountTag = baseMapper.selectOne(Wrappers.<WxAccountTag>lambdaQuery()
				.eq(WxAccountTag::getTag, accountTag.getTag()).ne(WxAccountTag::getId, accountTag.getId()));
		Assert.isNull(wxAccountTag, StrUtil.format("名称为[{}]的标签已经存在", accountTag.getTag()));
		baseMapper.updateById(accountTag);
		return accountTag;
	}

	@Override
	public Boolean removeAccountTagById(Long id) {
		WxAccountTag wxAccountTag = baseMapper.selectById(id);
		Assert.notNull(wxAccountTag, "要删除的标签不存在");
		return removeById(wxAccountTag.getId());
	}

}
