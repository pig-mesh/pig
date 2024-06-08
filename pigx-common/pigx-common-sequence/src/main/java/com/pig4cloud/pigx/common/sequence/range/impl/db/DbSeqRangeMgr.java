package com.pig4cloud.pigx.common.sequence.range.impl.db;

import cn.hutool.core.thread.ThreadUtil;
import com.pig4cloud.pigx.common.sequence.exception.SeqException;
import com.pig4cloud.pigx.common.sequence.range.SeqRange;
import com.pig4cloud.pigx.common.sequence.range.SeqRangeMgr;

import java.util.concurrent.TimeUnit;

/**
 * DB区间管理器
 *
 * @author xuan on 2018/4/29.
 */
public class DbSeqRangeMgr implements SeqRangeMgr {

    /**
     * 区间步长
     */
    private int step = 1000;

    /**
     * 区间起始位置，真实从stepStart+1开始
     */
    private long stepStart = 0;

    /**
     * 获取区间失败重试次数
     */
    private int retryTimes = 100;

    /**
     * 表名，默认range
     */
    private String tableName = "range";

    @Override
    public SeqRange nextRange(String name) throws SeqException {
        if (isEmpty(name)) {
            throw new SecurityException("[DbSeqRangeMgr-nextRange] name is empty.");
        }

        Long oldValue;
        Long newValue;

        for (int i = 0; i < getRetryTimes(); i++) {

            oldValue = BaseDbHelper.selectRange(getRealTableName(), name, getStepStart());

            if (null == oldValue) {
                // 区间不存在，重试
                continue;
            }

            newValue = oldValue + getStep();

            if (BaseDbHelper.updateRange(getRealTableName(), newValue, oldValue, name)) {
                return new SeqRange(oldValue + 1, newValue);
            }

            // else 1秒后 失败重试
            ThreadUtil.sleep(1, TimeUnit.SECONDS);
        }

        throw new SeqException("Retried too many times, retryTimes = " + getRetryTimes());
    }

    @Override
    public void init() {
        checkParam();
    }

    private boolean isEmpty(String str) {
        return null == str || str.trim().isEmpty();
    }

    private String getRealTableName() {
        return getTableName();
    }

    private void checkParam() {
        if (step <= 0) {
            throw new SecurityException("[DbSeqRangeMgr-checkParam] step must greater than 0.");
        }
        if (stepStart < 0) {
            throw new SecurityException("[DbSeqRangeMgr-setStepStart] stepStart < 0.");
        }
        if (retryTimes <= 0) {
            throw new SecurityException("[DbSeqRangeMgr-setRetryTimes] retryTimes must greater than 0.");
        }
        if (isEmpty(tableName)) {
            throw new SecurityException("[DbSeqRangeMgr-setTableName] tableName is empty.");
        }
    }

    //////// getter and setter

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public long getStepStart() {
        return stepStart;
    }

    public void setStepStart(long stepStart) {
        this.stepStart = stepStart;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
