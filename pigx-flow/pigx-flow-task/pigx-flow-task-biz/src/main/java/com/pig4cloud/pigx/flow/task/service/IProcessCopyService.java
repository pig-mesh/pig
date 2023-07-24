package com.pig4cloud.pigx.flow.task.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.entity.ProcessCopy;

/**
 * <p>
 * 流程抄送数据 服务类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-20
 */
public interface IProcessCopyService extends IService<ProcessCopy> {

	/**
	 * 查询单个抄送详细信息
	 * @param id
	 * @return
	 */
	R querySingleDetail(long id);

}
