package com.pig4cloud.pigx.common.sequence.range.impl.db;

import cn.hutool.core.util.RandomUtil;
import com.pig4cloud.pigx.common.sequence.exception.SeqException;
import org.anyline.data.param.ConfigStore;
import org.anyline.data.param.init.DefaultConfigStore;
import org.anyline.entity.DataRow;
import org.anyline.proxy.ServiceProxy;
import org.anyline.service.AnylineService;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 操作DB帮助类
 *
 * @author xuan on 2018/4/29.
 */
abstract class BaseDbHelper {
    private static final long DELTA = 100000000L;


    /**
     * 更新区间，乐观策略
     *
     * @param tableName 表名
     * @param newValue  更新新数据
     * @param oldValue  更新旧数据
     * @param name      区间名称
     * @return 成功/失败
     */
    static boolean updateRange(String tableName, Long newValue, Long oldValue, String name) {
        AnylineService service = ServiceProxy.service();
        SequenceTable sequenceTable = new SequenceTable();
        sequenceTable.setValue(newValue);
        sequenceTable.setGmtModified(LocalDateTime.now());
        ConfigStore configs = new DefaultConfigStore();
        configs.and("value", oldValue);
        return service.update(tableName, sequenceTable, configs) > 0;
    }

    /**
     * 查询区间，如果区间不存在，会新增一个区间，并返回null，由上层重新执行
     *
     * @param tableName 来源
     * @param name      区间名称
     * @param stepStart 初始位置
     * @return 区间值
     */
    static Long selectRange(String tableName, String name, long stepStart) {
        AnylineService service = ServiceProxy.service();
        DataRow dataRow = service.query(tableName, SequenceTable.Fields.name + ":" + name);

        if (Objects.isNull(dataRow)) {
            // 没有此类型数据，需要初始化
            SequenceTable sequenceTable = new SequenceTable();
            sequenceTable.setId(RandomUtil.getSecureRandom().nextLong());
            sequenceTable.setName(name);
            sequenceTable.setValue(stepStart);
            sequenceTable.setGmtCreate(LocalDateTime.now());
            sequenceTable.setGmtModified(LocalDateTime.now());
            service.insert(tableName, sequenceTable);
            return stepStart;
        }
        long oldValue = dataRow.getLong(SequenceTable.Fields.value);

        if (oldValue < 0) {
            String msg = "Sequence value cannot be less than zero, value = " + oldValue
                    + ", please check table sequence" + tableName;
            throw new SeqException(msg);
        }

        if (oldValue > Long.MAX_VALUE - DELTA) {
            String msg = "Sequence value overflow, value = " + oldValue + ", please check table sequence"
                    + tableName;
            throw new SeqException(msg);
        }

        return oldValue;
    }


}
