package com.pig4cloud.pigx.flow.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.entity.ProcessGroup;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-25
 */
public interface IProcessGroupService extends IService<ProcessGroup> {

	/**
	 * 组列表
	 * @return
	 */
	R<List<ProcessGroup>> queryList();

	/**
	 * 新增流程分组
	 * @param processGroup 分组名
	 * @return 添加结果
	 */
	R create(ProcessGroup processGroup);

	/**
	 * 删除分组
	 * @param id
	 * @return
	 */
	R delete(long id);

}
