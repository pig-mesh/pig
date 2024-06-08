package com.pig4cloud.pigx.common.sequence.sequence.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.pig4cloud.pigx.common.sequence.exception.SeqException;
import com.pig4cloud.pigx.common.sequence.range.BizName;
import com.pig4cloud.pigx.common.sequence.range.SeqRange;
import com.pig4cloud.pigx.common.sequence.range.SeqRangeMgr;
import com.pig4cloud.pigx.common.sequence.sequence.RangeSequence;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 序列号区间生成器接口默认实现
 *
 * @author xuan on 2018/1/10.
 * <p>
 * 根据biz name 自增
 */
public class DefaultRangeSequence implements RangeSequence {

	/**
	 * 获取区间是加一把独占锁防止资源冲突
	 */
	private final Lock lock = new ReentrantLock();

	/**
	 * 序列号区间管理器
	 */
	private SeqRangeMgr seqRangeMgr;

	/**
	 * 当前序列号区间
	 */
	private volatile SeqRange currentRange;

	private static Map<String, SeqRange> seqRangeMap = new ConcurrentHashMap<>(8);

	/**
	 * 需要获取区间的业务名称
	 */
	private BizName bizName;

	@Override
	public long nextValue() throws SeqException {
		String name = bizName.create();

		currentRange = seqRangeMap.get(name);
		// 当前区间不存在，重新获取一个区间
		if (null == currentRange) {
			lock.lock();
			try {
				if (null == currentRange) {
					currentRange = seqRangeMgr.nextRange(name);
					seqRangeMap.put(name, currentRange);
				}
			}
			finally {
				lock.unlock();
			}
		}

		// 当value值为-1时，表明区间的序列号已经分配完，需要重新获取区间
		long value = currentRange.getAndIncrement();
		if (value == -1) {
			lock.lock();
			try {
				for (;;) {
					if (currentRange.isOver()) {
						currentRange = seqRangeMgr.nextRange(name);
						seqRangeMap.put(name, currentRange);
					}

					value = currentRange.getAndIncrement();
					if (value == -1) {
						continue;
					}

					break;
				}
			}
			finally {
				lock.unlock();
			}
		}

		if (value < 0) {
			throw new SeqException("Sequence value overflow, value = " + value);
		}

		return value;
	}

	/**
	 * 下一个生成序号（带格式）
	 * @return
	 * @throws SeqException
	 */
	@Override
	public String nextNo() throws SeqException {
		return String.format("%s%05d", DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT), nextValue());
	}

	@Override
	public void setSeqRangeMgr(SeqRangeMgr seqRangeMgr) {
		this.seqRangeMgr = seqRangeMgr;
	}

	public SeqRangeMgr getSeqRangeMgr() {
		return seqRangeMgr;
	}

	@Override
	public void setName(BizName name) {
		this.bizName = name;
	}

}
