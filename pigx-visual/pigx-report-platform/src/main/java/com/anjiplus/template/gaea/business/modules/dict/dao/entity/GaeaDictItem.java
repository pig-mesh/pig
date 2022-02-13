package com.anjiplus.template.gaea.business.modules.dict.dao.entity;

import com.anji.plus.gaea.annotation.UnionUnique;
import com.anji.plus.gaea.annotation.UnionUniqueCode;
import com.anjiplus.template.gaea.business.code.*;
import com.anji.plus.gaea.curd.entity.GaeaBaseEntity;
import com.anjiplus.template.gaea.business.constant.BusinessConstant;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * 数据字典项(GaeaDictItem)实体类
 *
 * @author lirui
 * @since 2021-03-09 15:52:41
 */
@TableName(keepGlobalPrefix=true,value = "gaea_dict_item")
@UnionUniqueCode(group = BusinessConstant.DICT_ITEM_EXIST_GROUP, code = ResponseCode.DICT_ITEM_REPEAT)
public class GaeaDictItem extends GaeaBaseEntity implements Serializable {

    /**
     * 数据字典编码
     */
    @UnionUnique(group = BusinessConstant.DICT_ITEM_EXIST_GROUP)
    private String dictCode;
    /**
     * 字典项名称
     */
    private String itemName;
    /**
     * 字典项值
     */
    @UnionUnique(group = BusinessConstant.DICT_ITEM_EXIST_GROUP)
    private String itemValue;

    /**
     * 字典项扩展
     */
    private String itemExtend;
    /**
     * 语言标识
     */
    @UnionUnique(group = BusinessConstant.DICT_ITEM_EXIST_GROUP)
    private String locale;

    /**
     * 1：启用，0:禁用
     */
    private Integer enabled;
    /**
     * 描述
     */
    private String remark;
    /**
     * 排序
     */
    private Integer sort;

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getItemExtend() {
        return itemExtend;
    }

    public void setItemExtend(String itemExtend) {
        this.itemExtend = itemExtend;
    }
}
