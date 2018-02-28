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
 * 分钟拒绝客户端连接数
 * @author leifu
 * @Date 2017年6月16日
 * @Time 下午2:34:10
 */
public class MinuteRejectedConnectionsAlertStrategy extends AlertConfigStrategy {

    @Override
    public List<InstanceAlertValueResult> checkConfig(InstanceAlertConfig instanceAlertConfig, AlertConfigBaseData alertConfigBaseData) {
        Object object = getValueFromDiffInfo(alertConfigBaseData.getStandardStats(), RedisInfoEnum.rejected_connections.getValue());
        if (object == null) {
            return null;
        }
        long minuteRejectedConnections = NumberUtils.toLong(object.toString());
        boolean compareRight = isCompareLongRight(instanceAlertConfig, minuteRejectedConnections);
        if (compareRight) {
            return null;
        }
        InstanceInfo instanceInfo = alertConfigBaseData.getInstanceInfo();
        return Arrays.asList(new InstanceAlertValueResult(instanceAlertConfig, instanceInfo, String.valueOf(minuteRejectedConnections),
                instanceInfo.getAppId(), EMPTY));
    }

}
