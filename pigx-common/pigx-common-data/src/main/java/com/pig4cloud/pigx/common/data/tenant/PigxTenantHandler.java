/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
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

package com.pig4cloud.pigx.common.data.tenant;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.pig4cloud.pigx.common.core.util.TenantTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Objects;

/**
 * @author lengleng
 * @date 2018-12-26
 * <p>
 * 租户维护处理器
 */
@Slf4j
@RequiredArgsConstructor
public class PigxTenantHandler implements TenantLineHandler {

    private final PigxTenantConfigProperties properties;

    /**
     * 获取租户 ID 值表达式，只支持单个 ID 值
     * <p>
     *
     * @return 租户 ID 值表达式
     */
    @Override
    public Expression getTenantId() {
        Long tenantId = TenantContextHolder.getTenantId();
        log.debug("当前租户为 >> {}", tenantId);

        if (tenantId == null) {
            return new NullValue();
        }
        return new LongValue(tenantId);
    }

    /**
     * 获取租户字段名
     *
     * @return 租户字段名
     */
    @Override
    public String getTenantIdColumn() {
        return properties.getColumn();
    }

    /**
     * 根据表名判断是否忽略拼接多租户条件
     * <p>
     * 默认都要进行解析并拼接多租户条件
     *
     * @param tableName 表名
     * @return 是否忽略, true:表示忽略，false:需要解析并拼接多租户条件
     */
    @Override
    public boolean ignoreTable(String tableName) {
        // 判断是否跳过当前查询的租户过滤
        if (TenantContextHolder.getTenantSkip()) {
            return Boolean.TRUE;
        }

        Long tenantId = TenantContextHolder.getTenantId();
        // 租户中ID 为空，查询全部，不进行过滤
        if (tenantId == null) {
            return Boolean.TRUE;
        }

        //  判断实体类是否标记为多租户隔离
        TableInfo tableInfo = TableInfoHelper.getTableInfo(tableName);
        TenantTable annotation = null;
        if (Objects.nonNull(tableInfo)) {
            annotation = AnnotationUtils.findAnnotation(tableInfo.getEntityType(), TenantTable.class);
        }
        return !properties.getTables().contains(tableName) && annotation == null;
    }

}
