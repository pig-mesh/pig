package com.anjiplus.template.gaea.business.modules.dashboard.service;

import com.alibaba.fastjson.JSONObject;
import com.anjiplus.template.gaea.business.modules.dashboard.controller.dto.ChartDto;

import java.util.List;

/**
 * Created by raodeming on 2021/4/26.
 */
public interface ChartStrategy {

    /**
     * 图表类型
     * @return
     */
    String type();

    /**
     * 针对每种图表类型做单独的数据转换解析
     *
     * @param dto
     * @return
     */
    Object transform(ChartDto dto, List<JSONObject> data);
}
