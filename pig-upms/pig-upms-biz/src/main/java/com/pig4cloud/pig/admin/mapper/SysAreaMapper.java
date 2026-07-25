package com.pig4cloud.pig.admin.mapper;

import com.pig4cloud.pig.admin.api.entity.SysAreaEntity;
import com.pig4cloud.pig.admin.api.vo.SysAreaChildCountVO;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface SysAreaMapper extends MPJBaseMapper<SysAreaEntity> {

	/**
	 * 按父级行政编码统计直属子级数量
	 * @param parentCodes 父级行政编码
	 * @return 子级数量
	 */
	List<SysAreaChildCountVO> selectChildCounts(@Param("parentCodes") Collection<Long> parentCodes);

}
