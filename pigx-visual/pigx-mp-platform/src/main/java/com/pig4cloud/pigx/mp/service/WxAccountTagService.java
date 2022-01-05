package com.pig4cloud.pigx.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.mp.entity.WxAccountTag;

/**
 * 微信账户标签服务
 *
 * @author lishangbu
 * @date 2021/12/31
 */
public interface WxAccountTagService extends IService<WxAccountTag> {

	/**
	 * 保存账户标签
	 * @param accountTag 账户标签
	 * @return 保存成功后的账户标签
	 */
	WxAccountTag saveAccountTag(WxAccountTag accountTag);

	/**
	 * 更新账户标签
	 * @param accountTag 待更新的账户标签
	 * @return 修改成功后的账户标签
	 */
	WxAccountTag updateAccountTag(WxAccountTag accountTag);

	/**
	 * 删除账户标签
	 * @param wxAccountTag 待删除的账户标签
	 * @return 删除成功返回true,删除失败返回false
	 */
	Boolean removeAccountTagById(WxAccountTag wxAccountTag);

	/**
	 * 同步账户标签
	 * @param appId appId
	 * @return Boolean
	 */
	Boolean syncAccountTags(String appId);

}
