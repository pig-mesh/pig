/*
 *    Copyright (c) 2018-2026, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.pig4cloud.pig.common.data.datascope;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lengleng
 * @date 2018/12/26
 * <p>
 * 数据权限类型
 */
@Getter
@AllArgsConstructor
public enum DataScopeTypeEnum {

    /**
     * 查询全部数据
     */
    ALL(0, "全部"),

    /**
     * 自定义
     */
    CUSTOM(1, "自定义"),

    /**
     * 本级及子级
     */
    OWN_CHILD_LEVEL(2, "本级及子级"),

    /**
     * 本级
     */
    OWN_LEVEL(3, "本级"),

    /**
     * 本人
     */
    SELF_LEVEL(4, "本人");

    /**
     * 类型
     */
    private final int type;

    /**
     * 描述
     */
    private final String description;

    /**
     * 按类型获取
     *
     * @param dsType DS 型
     * @return {@link DataScopeTypeEnum }
     */
    public static DataScopeTypeEnum getByType(Integer dsType) {
        for (DataScopeTypeEnum value : DataScopeTypeEnum.values()) {
            if (value.getType() == dsType) {
                return value;
            }
        }
        return null;
    }
}
