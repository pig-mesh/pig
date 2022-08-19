package com.anjiplus.template.gaea.business.modules.dashboard.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.anjiplus.template.gaea.business.modules.dashboard.controller.dto.ChartDto;
import com.anjiplus.template.gaea.business.modules.dashboard.service.ChartStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 柱状体或者折线图
 * Created by raodeming on 2021/4/26.
 */
@Component
public class BarChartServiceImpl implements ChartStrategy {
    /**
     * 图表类型
     *
     * @return
     */
    @Override
    public String type() {
        return "widget-barchart|widget-linechart";
    }

    /**
     * 针对每种图表类型做单独的数据转换解析
     *
     * @param dto
     * @param data
     * @return
     */
    @Override
    public Object transform(ChartDto dto, List<JSONObject> data) {
//        JSONObject json = new JSONObject();
//        List<Object> xAxis = new ArrayList<>();
//        List<Object> series = new ArrayList<>();
//        data.forEach(jsonObject -> {
//            jsonObject.forEach((s, o) -> {
//                if ("xAxis".equals(s)) {
//                    xAxis.add(o);
//                } else {
//                    series.add(o);
//                }
//            });
//        });
//
//        json.put("xAxis", xAxis);
//        JSONArray objects = new JSONArray();
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("data", series);
//        objects.add(jsonObject);
//        json.put("series", objects);
//        return json.toJSONString();
        return data;
    }


/*    {
        "xAxis": [
                "哈哈",
                "洗洗",
                "来了",
                "问问",
                "天天"
    ],
        "series": [
            {
                "data": [
                    1,
                    2,
                    3,
                    4,
                    5
            ]
        }
    ]
    }*/
}
