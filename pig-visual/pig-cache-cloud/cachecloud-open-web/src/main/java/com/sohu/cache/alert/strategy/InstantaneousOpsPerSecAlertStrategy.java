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
 * 实时ops
 * 
 * @author leifu
 * @Date 2017年6月16日
 * @Time 下午2:34:10
 */
public class InstantaneousOpsPerSecAlertStrategy extends AlertConfigStrategy {
    @Override
    public List<InstanceAlertValueResult> checkConfig(InstanceAlertConfig instanceAlertConfig, AlertConfigBaseData alertConfigBaseData) {
        Object object = getValueFromRedisInfo(alertConfigBaseData.getStandardStats(), RedisInfoEnum.instantaneous_ops_per_sec.getValue());
        if (object == null) {
            return null;
        }
        // 关系比对
        long instantaneousOpsPerSec = NumberUtils.toLong(object.toString());
        boolean compareRight = isCompareLongRight(instanceAlertConfig, instantaneousOpsPerSec);
        if (compareRight) {
            return null;
        }
        InstanceInfo instanceInfo = alertConfigBaseData.getInstanceInfo();
        return Arrays.asList(new InstanceAlertValueResult(instanceAlertConfig, instanceInfo, String.valueOf(instantaneousOpsPerSec),
                instanceInfo.getAppId(), EMPTY));
    }

}
