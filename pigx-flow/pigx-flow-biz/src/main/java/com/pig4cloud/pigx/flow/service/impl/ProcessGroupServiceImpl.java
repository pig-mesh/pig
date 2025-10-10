package com.pig4cloud.pigx.flow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.entity.ProcessGroup;
import com.pig4cloud.pigx.flow.mapper.ProcessGroupMapper;
import com.pig4cloud.pigx.flow.service.IProcessGroupService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 流程分组服务实现类
 * <p>
 * 该类负责管理流程分组相关的业务逻辑，主要功能包括：
 * 1. 流程分组的增删改查操作
 * 2. 分组排序管理
 * 3. 为流程提供分类组织功能
 * 
 * 流程分组用于将相似或相关的流程进行分类管理，便于用户查找和使用
 * </p>
 *
 * @author Vincent
 * @since 2023-05-25
 */
@Service
public class ProcessGroupServiceImpl extends ServiceImpl<ProcessGroupMapper, ProcessGroup>
		implements IProcessGroupService {

	/**
	 * 查询所有流程分组列表
	 * <p>
	 * 获取系统中所有的流程分组，按照排序值（sort）升序排列
	 * </p>
	 * 
	 * @return R<List<ProcessGroup>> 包含所有流程分组的列表
	 */
	@Override
	public R<List<ProcessGroup>> queryList() {
		List<ProcessGroup> processGroupList = this.lambdaQuery().orderByAsc(ProcessGroup::getSort).list();

		return R.ok(processGroupList);
	}

	/**
	 * 新增表单分组
	 * <p>
	 * 创建一个新的流程分组，默认排序值为0
	 * </p>
	 * 
	 * @param processGroup 分组对象，需要包含groupName（分组名称）
	 * @return R 操作结果，成功返回R.ok()
	 */
	@Override
	public R create(ProcessGroup processGroup) {
		ProcessGroup pg = new ProcessGroup();
		pg.setSort(0);
		pg.setGroupName(processGroup.getGroupName());

		this.save(pg);
		return R.ok();
	}

	/**
	 * 删除分组
	 * <p>
	 * 根据分组ID删除流程分组。
	 * 注意：删除分组前应确保该分组下没有关联的流程，否则可能导致数据不一致
	 * </p>
	 * 
	 * @param id 分组ID
	 * @return R 操作结果，成功返回R.ok()
	 */
	@Override
	public R delete(long id) {
		this.removeById(id);
		return R.ok();
	}

}
