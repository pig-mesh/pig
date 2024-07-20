package com.pig4cloud.pigx.mp.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.mp.entity.WxAccountTag;
import com.pig4cloud.pigx.mp.entity.dto.WxAccountTagDeleteDTO;
import com.pig4cloud.pigx.mp.service.WxAccountTagService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 微信账户标签
 *
 * @author lishangbu
 * @date 2021/12/31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wx-account-tag")
public class WxAccountTagController {

	private final WxAccountTagService wxAccountTagService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param wxAccountTag 账户标签查询条件
	 * @return
	 */
	@GetMapping("/page")
	public R<Page<WxAccountTag>> getWxAccountTagPage(Page<WxAccountTag> page, WxAccountTag wxAccountTag) {
		LambdaQueryWrapper<WxAccountTag> wrapper = Wrappers.<WxAccountTag>lambdaQuery()
			.like(StrUtil.isNotBlank(wxAccountTag.getTag()), WxAccountTag::getTag, wxAccountTag.getTag())
			.eq(StrUtil.isNotBlank(wxAccountTag.getWxAccountAppid()), WxAccountTag::getWxAccountAppid,
					wxAccountTag.getWxAccountAppid());
		return R.ok(wxAccountTagService.page(page, wrapper));
	}

	/**
	 * 列表查询
	 * @param wxAccountTag 账户标签查询条件
	 * @return
	 */
	@GetMapping("/list")
	public R<List<WxAccountTag>> listWxAccountTags(WxAccountTag wxAccountTag) {
		return R.ok(wxAccountTagService.list(Wrappers.query(wxAccountTag)));
	}

	/**
	 * 保存账户标签
	 * @param wxAccountTag 待保存的账户标签
	 * @return 包含保存成功后的账户标签的API调用结果
	 */
	@PostMapping
	@HasPermission("mp_wx_account_tag_add")
	public R<WxAccountTag> saveAccountTag(@RequestBody @Valid WxAccountTag wxAccountTag) {
		return R.ok(wxAccountTagService.saveAccountTag(wxAccountTag));
	}

	/**
	 * 修改账户标签
	 * @param wxAccountTag 待修改的账户标签
	 * @return 包含修改成功后的账户标签的API调用结果
	 */
	@PutMapping
	@HasPermission("mp_wx_account_tag_edit")
	public R<WxAccountTag> updateAccountTag(@RequestBody @Valid WxAccountTag wxAccountTag) {
		return R.ok(wxAccountTagService.updateAccountTag(wxAccountTag));
	}

	/**
	 * 删除
	 * @param wxAccountTag 标签
	 * @return R
	 */
	@DeleteMapping
	@HasPermission("mp_wx_account_tag_del")
	public R<Boolean> removeAccountTagById(@RequestBody WxAccountTagDeleteDTO deleteDTO) {

		deleteDTO.getIds().forEach(item -> {
			WxAccountTag wxAccountTag = new WxAccountTag();
			wxAccountTag.setId(item);
			wxAccountTag.setWxAccountAppid(deleteDTO.getWxAccountAppid());
			wxAccountTagService.removeAccountTagById(wxAccountTag);
		});
		return R.ok();
	}

	/**
	 * 同步粉丝
	 * @param appId
	 * @return
	 */
	@PostMapping("/sync/{appId}")
	@HasPermission("mp_wx_account_tag_sync")
	public R sync(@PathVariable String appId) {
		return R.ok(wxAccountTagService.syncAccountTags(appId));
	}

}
