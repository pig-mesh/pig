package com.anjiplus.template.gaea.business.modules.dict.dao.entity;

import com.anji.plus.gaea.annotation.Unique;
import com.anji.plus.gaea.curd.entity.GaeaBaseEntity;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * (GaeaDict)实体类
 *
 * @author lr
 * @since 2021-02-23 10:01:02
 */
@TableName(keepGlobalPrefix=true, value = "gaea_dict")
public class GaeaDict extends GaeaBaseEntity implements Serializable {
    /**
     * 字典名称
     */
    private String dictName;
    /**
     * 字典编码
     */
    @Unique(code = ResponseCode.DICCODE_ISEXIST)
    private String dictCode;

    /**
     * 字典描述
     */
    private String remark;

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
