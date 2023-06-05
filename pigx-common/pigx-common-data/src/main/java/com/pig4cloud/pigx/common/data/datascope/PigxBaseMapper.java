package com.pig4cloud.pigx.common.data.datascope;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 扩展通用 Mapper，支持数据权限 和批量插入
 *
 * @author lengleng
 * @date 2020-06-17
 */
public interface PigxBaseMapper<T> extends MPJBaseMapper<T> {

	/**
	 * 根据 entity 条件，查询全部记录
	 * @param queryWrapper 实体对象封装操作类（可以为 null）
	 * @param scope 数据权限范围
	 * @return List<T>
	 */
	List<T> selectListByScope(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper, DataScope scope);

	/**
	 * 根据 entity 条件，查询全部记录（并翻页）
	 * @param page 分页查询条件（可以为 RowBounds.DEFAULT）
	 * @param queryWrapper 实体对象封装操作类（可以为 null）
	 * @param scope 数据权限范围
	 * @return Page
	 */
	<E extends IPage<T>> E selectPageByScope(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper,
			DataScope scope);

	/**
	 * 根据 Wrapper 条件，查询总记录数
	 * @param queryWrapper 实体对象封装操作类（可以为 null）
	 * @param scope 数据权限范围
	 * @return Integer
	 */
	Long selectCountByScope(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper, DataScope scope);

}
