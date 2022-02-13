package com.anjiplus.template.gaea.business.modules.dict.service;

import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anjiplus.template.gaea.business.modules.dict.controller.param.GaeaDictItemParam;
import com.anjiplus.template.gaea.business.modules.dict.dao.entity.GaeaDictItem;

import java.util.Map;

/**
 * 数据字典项(GaeaDictItem)Service
 *
 * @author lirui
 * @since 2021-03-10 13:05:59
 */
public interface GaeaDictItemService extends GaeaBaseService<GaeaDictItemParam, GaeaDictItem> {

    /**
     * 根据字典编码获取字典项
     * @param dictCode
     * @return
     */
    Map<String,String> getItemMap(String dictCode);
}
