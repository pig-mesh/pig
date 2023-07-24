package com.pig4cloud.pigx.flow.task.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;

/**
 * 聚合接口
 */
public interface ICombinationGroupService {

	/**
	 * 查询表单组包含流程
	 * @param groupId 表单ID
	 * @return 表单组数据
	 */
	R listGroupWithProcess(Page page, Long groupId);

	/**
	 * 查询所有我可以发起的表单组
	 * @return
	 */
	R listCurrentUserStartGroup();

}
