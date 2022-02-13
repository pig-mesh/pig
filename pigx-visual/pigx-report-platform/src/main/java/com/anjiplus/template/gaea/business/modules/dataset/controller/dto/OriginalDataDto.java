package com.anjiplus.template.gaea.business.modules.dataset.controller.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by raodeming on 2021/3/26.
 */
@Data
public class OriginalDataDto implements Serializable {

    /**总数*/
    private long total;

    /**获取的数据详情*/
    private List<JSONObject> data;

    public OriginalDataDto(List<JSONObject> data) {
        this.data = data;
    }

    public OriginalDataDto(long total, List<JSONObject> data) {
        this.total = total;
        this.data = data;
    }

    public OriginalDataDto() {
    }
}
