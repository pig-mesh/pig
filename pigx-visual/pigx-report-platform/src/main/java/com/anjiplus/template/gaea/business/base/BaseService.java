package com.anjiplus.template.gaea.business.base;

import com.anji.plus.gaea.curd.entity.BaseEntity;
import com.anji.plus.gaea.curd.params.PageParam;
import com.anji.plus.gaea.curd.service.GaeaBaseService;

/**
 * 项目级的Service公共处理基类
 *
 * @author WongBin
 * @date 2021/3/26
 */
public interface BaseService<P extends PageParam, T extends BaseEntity> extends GaeaBaseService<P, T> {

}
