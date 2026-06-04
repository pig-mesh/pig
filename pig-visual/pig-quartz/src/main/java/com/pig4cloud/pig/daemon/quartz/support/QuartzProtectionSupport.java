package com.pig4cloud.pig.daemon.quartz.support;

import cn.hutool.core.util.IdUtil;
import com.pig4cloud.pig.common.data.cache.RedisUtils;
import com.pig4cloud.pig.daemon.quartz.config.QuartzProtectionProperties;
import com.pig4cloud.pig.daemon.quartz.entity.SysJob;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Quartz 重复触发保护支撑组件。
 * <p>
 * 该组件负责封装 Quartz 防重逻辑所需的 Redis 锁、触发元数据生成和锁释放流程， 让调度入口与任务执行入口都能共享同一套保护规则。
 * </p>
 */
@Component
@RequiredArgsConstructor
public class QuartzProtectionSupport {

	private static final String FIRE_DEDUP_KEY_PREFIX = "quartz:fire:dedup:";

	private static final String RUNNING_LOCK_KEY_PREFIX = "quartz:job:running:";

	private final QuartzProtectionProperties properties;

	/**
	 * 判断是否启用 Quartz 防重保护。
	 * @return 启用返回 {@code true}，否则返回 {@code false}
	 */
	public boolean isProtectionEnabled() {
		return properties.isEnabled();
	}

	/**
	 * 判断是否记录被跳过的执行日志。
	 * @return 需要记录返回 {@code true}，否则返回 {@code false}
	 */
	public boolean shouldLogSkipped() {
		return properties.isLogSkipped();
	}

	/**
	 * 根据 Quartz 原始执行上下文构建统一元数据。
	 * @param jobExecutionContext Quartz 原始执行上下文，不允许为空
	 * @param trigger 当前触发器，可为空
	 * @return 统一封装后的执行元数据
	 */
	public QuartzExecutionMetadata buildExecutionMetadata(JobExecutionContext jobExecutionContext, Trigger trigger) {
		Date scheduledFireTime = jobExecutionContext.getScheduledFireTime();
		if (scheduledFireTime == null) {
			scheduledFireTime = jobExecutionContext.getFireTime();
		}
		if (scheduledFireTime == null) {
			scheduledFireTime = new Date();
		}
		return QuartzExecutionMetadata.of(scheduledFireTime, jobExecutionContext.getFireInstanceId(), trigger,
				"QUARTZ");
	}

	/**
	 * 尝试获取“同一触发点去重锁”。
	 * @param sysJob 当前任务配置，不允许为空
	 * @param metadata 当前触发元数据，不允许为空
	 * @return 去重锁句柄；当防护关闭时返回一个恒定视为已获取的空锁句柄
	 */
	public ProtectionLock tryAcquireFireDedupLock(SysJob sysJob, QuartzExecutionMetadata metadata) {
		if (!isProtectionEnabled()) {
			return ProtectionLock.disabled();
		}
		String token = IdUtil.fastSimpleUUID();
		String lockKey = FIRE_DEDUP_KEY_PREFIX + sysJob.getJobId() + ":" + metadata.scheduledFireTime().getTime();
		boolean acquired = RedisUtils.getLock(lockKey, token, toExpireSeconds(properties.getFireDedupTtlSeconds()));
		return new ProtectionLock(lockKey, token, acquired, false);
	}

	/**
	 * 尝试获取“任务运行锁”。
	 * @param sysJob 当前任务配置，不允许为空
	 * @return 运行锁句柄；当防护关闭时返回一个恒定视为已获取的空锁句柄
	 */
	public ProtectionLock tryAcquireRunningLock(SysJob sysJob) {
		if (!isProtectionEnabled()) {
			return ProtectionLock.disabled();
		}
		String token = IdUtil.fastSimpleUUID();
		String lockKey = RUNNING_LOCK_KEY_PREFIX + sysJob.getJobId();
		boolean acquired = RedisUtils.getLock(lockKey, token, toExpireSeconds(properties.getRunningLockTtlSeconds()));
		return new ProtectionLock(lockKey, token, acquired, true);
	}

	/**
	 * 释放运行锁。
	 * @param protectionLock 当前持有的锁句柄，为空或不可释放时直接忽略
	 */
	public void releaseRunningLock(ProtectionLock protectionLock) {
		if (protectionLock == null || !protectionLock.releasable || !protectionLock.acquired) {
			return;
		}
		RedisUtils.releaseLock(protectionLock.lockKey, protectionLock.token);
	}

	/**
	 * 将秒级过期时间转换为 Redis 锁所需的整型秒数。
	 * @param expireSeconds 配置中的过期秒数
	 * @return 适配 Redis 工具类后的整型秒数，最小为 1 秒
	 */
	private int toExpireSeconds(long expireSeconds) {
		if (expireSeconds <= 0) {
			return 1;
		}
		return expireSeconds > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) expireSeconds;
	}

	@Getter
	public static final class ProtectionLock {

		private final String lockKey;

		private final String token;

		private final boolean acquired;

		private final boolean releasable;

		/**
		 * 创建一个锁句柄。
		 * @param lockKey Redis 锁键，防护关闭时允许为空
		 * @param token 当前线程持有的锁值，防护关闭时允许为空
		 * @param acquired 是否已成功获取锁
		 * @param releasable 当前锁是否需要在结束时释放
		 */
		public ProtectionLock(String lockKey, String token, boolean acquired, boolean releasable) {
			this.lockKey = lockKey;
			this.token = token;
			this.acquired = acquired;
			this.releasable = releasable;
		}

		/**
		 * 创建一个“防护关闭”场景下的空锁句柄。
		 * @return 视为已获取且无需释放的空锁句柄
		 */
		public static ProtectionLock disabled() {
			return new ProtectionLock(null, null, true, false);
		}

	}

}
