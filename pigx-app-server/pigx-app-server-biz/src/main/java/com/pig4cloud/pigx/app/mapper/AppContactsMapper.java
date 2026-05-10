package com.pig4cloud.pigx.app.mapper;

import com.pig4cloud.pigx.app.api.entity.AppContactsEntity;
import com.pig4cloud.pigx.common.data.datascope.PigxBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通讯录 Mapper。
 * <p>
 * 继承 {@link PigxBaseMapper} 后可配合数据权限插件执行范围查询。
 */
@Mapper
public interface AppContactsMapper extends PigxBaseMapper<AppContactsEntity> {


}
