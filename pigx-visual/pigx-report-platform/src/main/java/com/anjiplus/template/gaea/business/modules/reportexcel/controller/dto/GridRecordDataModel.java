package com.anjiplus.template.gaea.business.modules.reportexcel.controller.dto;


import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * 存储对象类
 *
 * @author Administrator
 */
@Data
public class GridRecordDataModel {
    /**
     * 记录序列
     */
    Long id;
    /**
     * 文档ID
     */
    String list_id;
    /**
     * 本记录的行_列
     */
    String row_col;
    /**
     * sheet序号
     */
    String index;
    /**
     * 状态是否当前sheet页
     */
    Integer status;
    /**
     * 块编号 第一块 fblock
     */
    String block_id;
    /**
     * json串
     */
    JSONObject json_data;
    /**
     * 排序位置
     */
    Integer order;
    /**
     * 是否删除
     */
    Integer is_delete;

    /**
     * sheet页数据 未编号分组
     */
    List<JSONObject> dataList;

}
