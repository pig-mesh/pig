package com.pig4cloud.pigx.mp.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.mp.constant.ReplyTypeEnum;
import com.pig4cloud.pigx.mp.entity.WxAutoReply;
import com.pig4cloud.pigx.mp.service.WxAutoReplyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 消息自动回复
 *
 * @author JL
 * @date 2019-04-18 15:40:39
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wx-auto-reply")
public class WxAutoReplyController {

	private final WxAutoReplyService wxAutoReplyService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param wxAutoReply 消息自动回复
	 * @return
	 */
	@GetMapping("/page")
	public R getWxAutoReplyPage(Page page, WxAutoReply wxAutoReply) {
		return R.ok(wxAutoReplyService.page(page, Wrappers.query(wxAutoReply)));
	}

	/**
	 * 通过id查询消息自动回复
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable("id") Long id) {
		return R.ok(wxAutoReplyService.getById(id));
	}

	/**
	 * 新增消息自动回复
	 * @param wxAutoReply 消息自动回复
	 * @return R
	 */
	@PostMapping
	@HasPermission("mp_wxautoreply_add")
	public R save(@RequestBody WxAutoReply wxAutoReply) {
		this.jude(wxAutoReply);
		return R.ok(wxAutoReplyService.save(wxAutoReply));
	}

	/**
	 * 修改消息自动回复
	 * @param wxAutoReply 消息自动回复
	 * @return R
	 */
	@PutMapping
	@HasPermission("mp_wxautoreply_edit")
	public R updateById(@RequestBody WxAutoReply wxAutoReply) {
		return R.ok(wxAutoReplyService.updateById(wxAutoReply));
	}

	/**
	 * 通过id删除消息自动回复
	 * @param id id
	 * @return R
	 */
	@DeleteMapping("/{id}")
	@HasPermission("mp_wxautoreply_del")
	public R removeById(@PathVariable Long id) {
		return R.ok(wxAutoReplyService.removeById(id));
	}

	/**
	 * //校验参数
	 * @param wxAutoReply
	 */
	public void jude(WxAutoReply wxAutoReply) {
		if (ReplyTypeEnum.MSG.getType().equals(wxAutoReply.getType())) {
			Wrapper<WxAutoReply> queryWrapper = Wrappers.<WxAutoReply>lambdaQuery()
				.eq(WxAutoReply::getReqType, wxAutoReply.getReqType());
			List<WxAutoReply> list = wxAutoReplyService.list(queryWrapper);
			if (wxAutoReply.getId() != null) {
				if (list != null && list.size() == 1) {
					if (!list.get(0).getId().equals(wxAutoReply.getId())) {
						throw new RuntimeException("请求消息类型重复");
					}
				}
				if (list != null && list.size() > 1) {
					throw new RuntimeException("请求消息类型重复");
				}
			}
		}
		if (ReplyTypeEnum.KEYWORD.getType().equals(wxAutoReply.getType())) {
			Wrapper<WxAutoReply> queryWrapper = Wrappers.<WxAutoReply>lambdaQuery()
				.eq(WxAutoReply::getReqKey, wxAutoReply.getReqKey())
				.eq(WxAutoReply::getRepType, wxAutoReply.getRepMate());
			List<WxAutoReply> list = wxAutoReplyService.list(queryWrapper);
			if (list != null && list.size() == 1) {
				if (!list.get(0).getId().equals(wxAutoReply.getId())) {
					throw new RuntimeException("关键词重复");
				}
			}
			if (list != null && list.size() > 1) {
				throw new RuntimeException("关键词重复");
			}
		}
	}

}
