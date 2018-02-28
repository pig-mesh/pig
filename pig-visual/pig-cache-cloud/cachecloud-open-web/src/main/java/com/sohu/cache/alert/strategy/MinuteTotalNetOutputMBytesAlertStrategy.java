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
 * 分钟输出网络流量
 * @author leifu
 * @Date 2017年6月16日
 * @Time 下午2:34:10
 */
public class MinuteTotalNetOutputMBytesAlertStrategy extends AlertConfigStrategy {
    @Override
    public List<InstanceAlertValueResult> checkConfig(InstanceAlertConfig instanceAlertConfig, AlertConfigBaseData alertConfigBaseData) {
        Object totalNetOutputBytesObject = getValueFromDiffInfo(alertConfigBaseData.getStandardStats(), RedisInfoEnum.total_net_output_bytes.getValue());
        if (totalNetOutputBytesObject == null) {
            return null;
        }
        // 关系比对
        long totalNetOutputBytes = NumberUtils.toLong(totalNetOutputBytesObject.toString());
        totalNetOutputBytes = changeByteToMB(totalNetOutputBytes);
        boolean compareRight = isCompareLongRight(instanceAlertConfig, totalNetOutputBytes);
        if (compareRight) {
            return null;
        }
        InstanceInfo instanceInfo = alertConfigBaseData.getInstanceInfo();
        return Arrays.asList(new InstanceAlertValueResult(instanceAlertConfig, instanceInfo, String.valueOf(totalNetOutputBytes),
                instanceInfo.getAppId(), MB_STRING));
    }

}
