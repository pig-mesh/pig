package com.anjiplus.template.gaea.business.modules.datasource.dao;

import org.apache.ibatis.annotations.Mapper;

import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anjiplus.template.gaea.business.modules.datasource.dao.entity.DataSource;

/**
* DataSource Mapper
* @author Raod
* @date 2021-03-18 12:09:57.728203200
**/
@Mapper
public interface DataSourceMapper extends GaeaBaseMapper<DataSource> {

}
