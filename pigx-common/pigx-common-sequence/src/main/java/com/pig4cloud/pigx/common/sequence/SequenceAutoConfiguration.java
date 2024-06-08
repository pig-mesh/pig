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

package com.pig4cloud.pigx.common.sequence;

import com.pig4cloud.pigx.common.sequence.properties.BaseSequenceProperties;
import com.pig4cloud.pigx.common.sequence.range.impl.db.SequenceTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anyline.metadata.Column;
import org.anyline.metadata.Index;
import org.anyline.metadata.Table;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * @author lengleng
 * @date 2019-05-26
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(BaseSequenceProperties.class)
public class SequenceAutoConfiguration {

    private final BaseSequenceProperties sequenceDbProperties;

    /**
     * 启动后自动创建表
     */
    @EventListener({WebServerInitializedEvent.class})
    public void onApplicationReady() throws Exception {
        // 判断是否需要创建表
        AnylineService service = ServiceProxy.service();
        Table tableMetadata = service.metadata().table(sequenceDbProperties.getDb().getTableName(), false);
        if (Objects.isNull(tableMetadata)) {
            Table table = Table.from(SequenceTable.class);
            table.setName(sequenceDbProperties.getDb().getTableName());
            table.setComment("序列号区间管理表");
            table.setPrimaryKey(SequenceTable.Fields.id);
            LinkedHashMap<String, Index> indexLinkedHashMap = new LinkedHashMap<>();
            Index nameUnique = new Index(SequenceTable.Fields.name);
            nameUnique.setUnique(true);
            nameUnique.addColumn(new Column(SequenceTable.Fields.name));
            indexLinkedHashMap.put(SequenceTable.Fields.name, nameUnique);
            table.setIndexes(indexLinkedHashMap);

            service.ddl().create(table);
            log.debug("发号器自动创建表成功:{} ", table);
        }
    }

}
