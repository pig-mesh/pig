package com.anjiplus.template.gaea.business.modules.dashboard.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.anjiplus.template.gaea.business.modules.dashboard.controller.dto.ChartDto;
import com.anjiplus.template.gaea.business.modules.dashboard.service.ChartStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 折柱图
 * Created by raodeming on 2021/4/26.
 */
@Component
public class BarLineChartServiceImpl implements ChartStrategy {
    /**
     * 图表类型
     *
     * @return
     */
    @Override
    public String type() {
        return "widget-barlinechart";
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
//        Map<String, List<Object>> series = new HashMap<>();
//        data.forEach(jsonObject -> {
//            jsonObject.forEach((s, o) -> {
//                if ("xAxis".equals(s)) {
//                    xAxis.add(o);
//                } else {
//                    List<Object> objects;
//                    if (series.containsKey(s)) {
//                        objects = series.get(s);
//                    } else {
//                        objects = new ArrayList<>();
//
//                    }
//                    objects.add(o);
//                    series.put(s, objects);
//
//                }
//            });
//        });
//
//        json.put("xAxis", xAxis);
//        List<JSONObject> result = new ArrayList<>();
//        series.forEach((s, objects) -> {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("name", s);
//            if (s.endsWith("bar")) {
//                jsonObject.put("type", "bar");
//            } else  {
//                jsonObject.put("type", "line");
//            }
//            jsonObject.put("data", objects);
//            result.add(jsonObject);
//        });
//        json.put("series", result);
//        return json.toJSONString();
        return data;
    }


    /*{
        "xAxis": [
                "1月",
                "2月",
                "3月"
        ],
        "series": [
            {
                "name": "指标1", //暂时用不上
                "type": "bar",   //需要处理
                "data": [
                    2,
                    49,
                    2
                ]
            },
            {
                 "name": "指标2",
                "type": "line",
                "yAxisIndex": 1,
                "data": [
                      2,
                     32,
                      4
            ]
        }
    ]
    }*/

}
