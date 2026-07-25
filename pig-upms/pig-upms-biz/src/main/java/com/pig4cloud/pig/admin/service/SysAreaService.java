package com.pig4cloud.pig.admin.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pig.admin.api.dto.SysAreaSortDTO;
import com.pig4cloud.pig.admin.api.dto.SysAreaUpdateDTO;
import com.pig4cloud.pig.admin.api.entity.SysAreaEntity;
import com.pig4cloud.pig.admin.api.vo.SysAreaManageVO;
import com.pig4cloud.pig.common.core.util.R;

import java.util.List;

public interface SysAreaService extends IService<SysAreaEntity> {

	/**
	 * 查询行政区划树
	 * @param sysArea 查询条件
	 * @return 树
	 */
	List<Tree<Long>> selectTree(SysAreaEntity sysArea);

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param sysArea 行政区划
	 * @return Page
	 */
	Page selectPage(Page page, SysAreaEntity sysArea);

	/**
	 * 查询管理端直属子级
	 * @param pid 父级行政编码
	 * @return 直属子级
	 */
	List<SysAreaManageVO> listManageChildren(Long pid);

	/**
	 * 搜索管理端行政区划
	 * @param keyword 名称或行政编码
	 * @return 搜索结果
	 */
	List<SysAreaManageVO> searchManage(String keyword);

	/**
	 * 判断行政编码是否存在
	 * @param adcode 行政编码
	 * @return 是否存在
	 */
	boolean existsByAdcode(Long adcode);

	/**
	 * 更新同级行政区划排序
	 * @param sortDTO 排序参数
	 * @return 更新结果
	 */
	R updateAreaSort(SysAreaSortDTO sortDTO);

	/**
	 * 新增行政区划并追加到同级末尾
	 * @param sysArea 行政区划
	 * @return 新增结果
	 */
	R saveArea(SysAreaEntity sysArea);

	/**
	 * 更新行政区划可编辑字段
	 * @param updateDTO 可编辑字段
	 * @return 更新结果
	 */
	R updateArea(SysAreaUpdateDTO updateDTO);

	/**
	 * 删除叶子行政区划
	 * @param ids 主键列表
	 * @return 删除结果
	 */
	R removeAreas(List<Long> ids);

}
