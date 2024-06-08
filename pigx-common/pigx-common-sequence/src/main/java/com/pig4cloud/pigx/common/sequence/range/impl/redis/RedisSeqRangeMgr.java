package com.pig4cloud.pigx.common.sequence.range.impl.redis;

import com.pig4cloud.pigx.common.sequence.exception.SeqException;
import com.pig4cloud.pigx.common.sequence.range.SeqRange;
import com.pig4cloud.pigx.common.sequence.range.SeqRangeMgr;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * Redis区间管理器
 *
 * @author xuan on 2018/5/8.
 */
public class RedisSeqRangeMgr implements SeqRangeMgr {

    /**
     * 前缀防止key重复
     */
    private final static String KEY_PREFIX = "x_sequence_";

    /**
     * redis客户端
     */
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 区间步长
     */
    private int step = 1000;

    /**
     * 区间起始位置，真实从stepStart+1开始
     */
    private long stepStart = 0;

    /**
     * 标记业务key是否存在，如果false，在取nextRange时，会取check一把 这个boolean只为提高性能，不用每次都取redis check
     */
    private volatile boolean keyAlreadyExist;

    @Override
    public SeqRange nextRange(String name) throws SeqException {
        if (!keyAlreadyExist) {

            if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(getRealKey(name)))) {
                // 第一次不存在，进行初始化,setnx不存在就set，存在就忽略
                stringRedisTemplate.opsForValue().setIfAbsent(getRealKey(name), String.valueOf(stepStart));
            }
            keyAlreadyExist = true;
        }

        Long max = stringRedisTemplate.opsForValue().increment(getRealKey(name), step);
        Long min = max - step + 1;
        return new SeqRange(min, max);
    }


    private String getRealKey(String name) {
        return KEY_PREFIX + name;
    }

    private boolean isEmpty(String str) {
        return null == str || str.trim().length() == 0;
    }

    public long getStepStart() {
        return stepStart;
    }

    public void setStepStart(long stepStart) {
        this.stepStart = stepStart;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
}
