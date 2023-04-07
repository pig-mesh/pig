package com.pig4cloud.pigx.act.mapper;

import com.pig4cloud.pigx.common.data.datascope.PigxBaseMapper;
import org.activiti.engine.repository.Model;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 请假流程
 *
 * @author 冷冷
 * @date 2018-09-27 15:20:44
 */
@Mapper
public interface ProcessMapper extends PigxBaseMapper<Model> {

	void deleteByIds(@Param("ids") String[] ids);

}
