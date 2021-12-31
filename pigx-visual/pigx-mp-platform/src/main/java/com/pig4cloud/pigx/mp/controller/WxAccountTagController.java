package com.pig4cloud.pigx.mp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.mp.entity.WxAccountTag;
import com.pig4cloud.pigx.mp.entity.WxAutoReply;
import com.pig4cloud.pigx.mp.service.WxAccountTagService;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
	public R<IPage<WxAccountTag>> getWxAccountTagPage(Page page, WxAccountTag wxAccountTag) {
		return R.ok(wxAccountTagService.page(page, Wrappers.query(wxAccountTag)));
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
	public R<WxAccountTag> saveAccountTag(@RequestBody @Valid WxAccountTag wxAccountTag) {
		return R.ok(wxAccountTagService.saveAccountTag(wxAccountTag));
	}

	/**
	 * 修改账户标签
	 * @param wxAccountTag 待修改的账户标签
	 * @return 包含修改成功后的账户标签的API调用结果
	 */
	@PutMapping
	public R<WxAccountTag> updateAccountTag(@RequestBody @Valid WxAccountTag wxAccountTag) {
		return R.ok(wxAccountTagService.updateAccountTag(wxAccountTag));
	}

	@DeleteMapping("/{id:\\d+}")
	public R<Boolean> removeAccountTagById(@PathVariable Long id) {
		return R.ok(wxAccountTagService.removeAccountTagById(id));
	}

}
