package com.sohu.cache.alert.strategy;

import com.sohu.cache.alert.bean.AlertConfigBaseData;
import com.sohu.cache.entity.InstanceAlertConfig;
import com.sohu.cache.entity.InstanceAlertValueResult;
import com.sohu.cache.entity.InstanceInfo;
import com.sohu.cache.redis.enums.RedisInfoEnum;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 分钟aof delay fsync检测
 * @author leifu
 * @Date 2017年6月16日
 * @Time 下午2:34:10
 */
public class MinuteAofDelayedFsyncAlertStrategy extends AlertConfigStrategy {

    @Override
    public List<InstanceAlertValueResult> checkConfig(InstanceAlertConfig instanceAlertConfig, AlertConfigBaseData alertConfigBaseData) {
        Object object = getValueFromDiffInfo(alertConfigBaseData.getStandardStats(), RedisInfoEnum.aof_delayed_fsync.getValue());
        if (object == null) {
            return null;
        }
        long aofDelayedFsync = NumberUtils.toLong(object.toString());
        boolean compareRight = isCompareLongRight(instanceAlertConfig, aofDelayedFsync);
        if (compareRight) {
            return null;
        }
        InstanceInfo instanceInfo = alertConfigBaseData.getInstanceInfo();
        return Arrays.asList(new InstanceAlertValueResult(instanceAlertConfig, instanceInfo, String.valueOf(aofDelayedFsync),
                instanceInfo.getAppId(), EMPTY));
    }

}
